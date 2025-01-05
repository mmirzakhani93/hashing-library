package io.github.mmirzakhani93;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class providing methods for hashing objects and handling fields with specific annotations.
 * The class is designed to support secure and consistent generation of hash values for objects,
 * particularly by focusing on annotated fields to determine which parts of an object should
 * contribute to its hash computation. It also includes methods for recognizing simple data types
 * and wrapper types.
 *
 * The primary purpose of this utility is to standardize the creation of hash values for objects
 * where only specific, annotated fields contribute to the hashing process.
 */
public class HashingUtils {

    /**
     * A static and immutable set of classes representing the types
     * commonly used as primitive wrapper types or simple value objects.
     * This includes classes such as String, the wrapper classes for primitive
     * types (e.g., Boolean, Integer), and the Date class.
     *
     * Used for operations or checks where these specific types need to be
     * differentiated or handled distinctly from other object types.
     */
    private static final Set<Class<?>> PRIMITIVE_WRAPPER_TYPES = Set.of(
            String.class, Boolean.class, Byte.class, Character.class, Double.class,
            Float.class, Integer.class, Long.class, Short.class, Date.class
    );

    /**
     * A singleton instance of ObjectMapper used for converting objects to JSON strings.
     * Configured to exclude null values during serialization. This instance provides
     * thread-safe and reusable JSON processing capabilities and is designed to handle
     * serialization and deserialization in a consistent manner across the application.
     */
    private final static ObjectMapper mapper = new ObjectMapper();
    static {
        // Configure ObjectMapper to exclude null values
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * Computes a hash value for the given object using the specified algorithm.
     * Only the fields annotated with {@code @HashableField} are included in the hash computation.
     * The resulting hash value is returned as a Base64-encoded string.
     *
     * @param object The object to be hashed. Must not be null. Only fields annotated with {@code @HashableField}
     *               are considered during the hashing process. Nested objects and collections
     *               are processed recursively to include their hashable fields.
     * @param algorithm The name of the hash algorithm to use (e.g., "SHA-256", "MD5"). Must be a valid algorithm
     *                  recognized by {@code java.security.MessageDigest}.
     * @return A Base64-encoded string representing the hash value of the given object.
     * @throws NoSuchAlgorithmException If the specified algorithm is not available in the environment.
     * @throws JsonProcessingException If an error occurs during serialization of the object to JSON.
     * @throws IllegalAccessException If there is an issue accessing the fields of the provided object.
     */
    public static String hashObject(Object object, HashAlgorithm algorithm)
            throws NoSuchAlgorithmException, JsonProcessingException, IllegalAccessException {

        // Create a MessageDigest instance for the specified hash algorithm
        MessageDigest digest = MessageDigest.getInstance(algorithm.toString());

        // Create a simplified object with only the fields annotated with @HashableField
        Object filteredObject = extractHashableFields(object);

        // Serialize the object to a JSON string, convert it to bytes using UTF-8 encoding,
        // and then compute the hash using the MessageDigest
        byte[] hashBytes = digest.digest(mapper.writeValueAsString(filteredObject).getBytes(StandardCharsets.UTF_8));

        // Encode the hash bytes to a Base64 string and return it
        return Base64.getEncoder().encodeToString(hashBytes);
    }

    /**
     * Extracts a map of hashable fields and their corresponding values from the given object.
     * Only fields annotated with {@code @HashableField} are included in the resulting map.
     * Nested objects and collections are recursively processed to extract their hashable fields.
     *
     * @param obj The object to extract hashable fields from. Can be any object or null.
     * @return A map containing the field names as keys and their corresponding values as map values.
     *         If the object is null, an empty map is returned. Each key corresponds to a field
     *         annotated with {@code @HashableField}, and if the value is a collection or nested object,
     *         it is recursively processed.
     * @throws IllegalAccessException If there is an issue accessing the fields of the provided object.
     */
    private static Map<String, Object> extractHashableFields(Object obj) throws IllegalAccessException {

        // If the object is null, return an empty map
        if (obj == null) {
            return Collections.emptyMap();
        }

        // Initialize a map to store the results
        Map<String, Object> resultMap = new LinkedHashMap<>();

        // Start with the class of the provided object
        Class<?> currentClass = obj.getClass();

        // Iterate through the class hierarchy (current class and its superclasses)
        while (currentClass != null) {
            // Get all declared fields of the current class that are annotated with @HashableField
            List<Field> sortedFields = Arrays.stream(currentClass.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(HashableField.class)) // Filter fields with the annotation
                    .sorted(Comparator.comparingInt(field -> field.getAnnotation(HashableField.class).order())) // Sort by 'order' annotation value
                    .collect(Collectors.toList());

            // Process each of the sorted fields
            for (Field field : sortedFields) {
                field.setAccessible(true); // Make the field accessible (even if it's private)

                // Get the value of the field for the provided object
                Object value = field.get(obj);

                // If the field value is null, skip it
                if (value == null) {
                    continue;
                }

                // If the value is a collection, process each item in the collection recursively
                if (value instanceof Collection<?>) {
                    List<Map<String, Object>> processedItems = new ArrayList<>();
                    for (Object item : (Collection<?>) value) {
                        if (item != null) {
                            processedItems.add(extractHashableFields(item)); // Recursively include hashable fields of items
                        }
                    }
                    resultMap.put(field.getName(), processedItems); // Add the processed collection to the result map
                } else if (!isPrimitiveOrWrapper(value.getClass())) {
                    // If the value is a complex object (not primitive or wrapper), process it recursively
                    resultMap.put(field.getName(), extractHashableFields(value));
                } else {
                    // If the value is a primitive or wrapper, just add it directly
                    resultMap.put(field.getName(), value);
                }
            }

            // Move to the superclass to continue processing fields from the class hierarchy
            currentClass = currentClass.getSuperclass();
        }

        // Return the map with all processed fields and their values
        return resultMap;
    }

    /**
     * Checks if a given class represents a primitive type or its corresponding wrapper type.
     *
     * @param type The class to be checked.
     * @return {@code true} if the provided class is a primitive type or a wrapper type; {@code false} otherwise.
     */
    private static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() || PRIMITIVE_WRAPPER_TYPES.contains(type); // Check if it's in the list of primitive wrappers
    }
}
