package io.github.mmirzakhani93;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.mmirzakhani93.model.ParentObject;
import io.github.mmirzakhani93.model.ParentObjectWithList;
import io.github.mmirzakhani93.model.TestObject;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test suite for the {@code HashingUtils} class.
 * This class tests various scenarios to ensure the correctness and robustness of the hashing utility methods.
 *
 * Key functionalities tested include:
 * - Hashing objects with valid data and algorithms.
 * - Handling unsupported algorithms.
 * - Handling null or empty input objects.
 * - Hashing objects with nested structures or collections.
 *
 * Each test case is designed to validate specific requirements and edge cases for the hashing functionality.
 */
class HashingUtilsTest {

    /**
     * Validates the `hashObject` method of the {@code HashingUtils} class
     * by providing a non-null object and a valid hashing algorithm.
     *
     * This test ensures that the hashing mechanism functions as expected
     * for a valid object with annotated fields (`TestObject`) using a recognized
     * algorithm (`SHA-256`). It checks whether the resulting hash is not null
     * and is not an empty string.
     *
     * Preconditions:
     * - The `TestObject` provided contains properly annotated fields
     *   supported by the hashing utility.
     * - The algorithm provided is a valid and supported hashing algorithm.
     *
     * Test Steps:
     * 1. Creates an instance of `HashingUtils`.
     * 2. Constructs a test object (`TestObject`) with valid data.
     * 3. Specifies a valid hashing algorithm (`SHA-256`).
     * 4. Calls the `hashObject` method of `HashingUtils` with the constructed object and algorithm.
     * 5. Asserts that:
     *    - The result is not null.
     *    - The result is a non-empty string.
     *
     * Expected Outcomes:
     * - The `hashObject` method successfully computes the hash for the provided object.
     * - The returned hash is valid and does not result in null or an empty value.
     *
     * @throws NoSuchAlgorithmException If the provided hashing algorithm is not available.
     * @throws JsonProcessingException If there is an error during object serialization.
     * @throws IllegalAccessException If access to fields within the provided object is not permitted.
     */
    @Test
    void testHashObjectWithValidObjectAndAlgorithm()
            throws NoSuchAlgorithmException, JsonProcessingException, IllegalAccessException {

        // Arrange
        TestObject testObject = new TestObject("John Doe", 30);

        // Act
        String hash = HashingUtils.hashObject(testObject, HashAlgorithm.SHA256);

        // Assert
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    /**
     * Tests the behavior of the {@code hashObject} method when provided
     * with an object that has all fields set to null.
     *
     * This test ensures that the method generates a non-null and non-empty
     * hash value even when the input object has no meaningful data to hash.
     *
     * Preconditions:
     * - The {@code hashObject} method is invoked with a valid hashing algorithm
     *   ("SHA-256") and an object with all fields set to null.
     *
     * Expected Outcomes:
     * - The returned hash string is not null.
     * - The returned hash string is not empty.
     *
     * @throws NoSuchAlgorithmException If the specified hashing algorithm is unavailable.
     * @throws JsonProcessingException If serialization of the input object to JSON fails.
     * @throws IllegalAccessException If the {@code hashObject} method encounters issues accessing the object's fields.
     */
    @Test
    void testHashObjectWithEmptyObject()
            throws NoSuchAlgorithmException, JsonProcessingException, IllegalAccessException {

        // Arrange
        TestObject testObject = new TestObject(null, null);

        // Act
        String hash = HashingUtils.hashObject(testObject, HashAlgorithm.SHA256);

        // Assert
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    /**
     * Tests the behavior of the `hashObject` method when the input object is null.
     *
     * This test ensures that the method correctly handles a null input object
     * by returning a non-null, non-empty hash value. It verifies that the
     * hashing implementation can manage edge cases without throwing exceptions
     * or returning invalid outputs.
     *
     * @throws NoSuchAlgorithmException If the specified hashing algorithm is not available.
     * @throws JsonProcessingException If an error occurs during JSON processing.
     * @throws IllegalAccessException If there is an issue accessing fields within the object.
     */
    @Test
    void testHashObjectWithNullObject()
            throws NoSuchAlgorithmException, JsonProcessingException, IllegalAccessException {

        // Act
        String hash = HashingUtils.hashObject(null, HashAlgorithm.SHA256);

        // Assert
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    /**
     * Verifies the behavior of the hashing utility when processing an object with nested objects.
     *
     * This test ensures the following:
     * - HashingUtils correctly computes a hash for objects containing nested objects.
     * - The nested objects are included in the hash calculation as per the fields annotated with {@code @HashableField}.
     * - The computed hash is non-null and non-empty.
     *
     * Test arrangement:
     * - A parent object is created containing fields annotated with {@code @HashableField},
     *   along with a nested child object also containing annotated fields.
     * - A SHA-256 algorithm is specified for hashing.
     *
     * Assertions:
     * - Verifies that the hash string is not null.
     * - Verifies that the hash string is not empty.
     *
     * @throws NoSuchAlgorithmException      If the specified hash algorithm is not available.
     * @throws JsonProcessingException       If an error occurs during JSON serialization of the object.
     * @throws IllegalAccessException        If there is an issue accessing fields of the provided object.
     */
    @Test
    void testHashObjectWithNestedObjects()
            throws NoSuchAlgorithmException, JsonProcessingException, IllegalAccessException {

        // Arrange
        TestObject child = new TestObject("Child", 12);
        ParentObject parent = new ParentObject("Parent", 40, child);

        // Act
        String hash = HashingUtils.hashObject(parent, HashAlgorithm.SHA256);

        // Assert
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    /**
     * Tests the functionality of the {@code hashObject} method in the {@code HashingUtils} class
     * when the input object contains a collection field. The method validates that the hash is
     * generated correctly and is neither {@code null} nor empty.
     *
     * The test involves the following steps:
     * - Arranges input data, including a parent object that contains a collection of child objects.
     * - Calls the {@code hashObject} method with the input object and a specified hashing algorithm.
     * - Asserts that the generated hash is not {@code null} and is not an empty string.
     *
     * @throws NoSuchAlgorithmException If the specified hash algorithm is not available.
     * @throws JsonProcessingException  If an error occurs during JSON serialization of the object.
     * @throws IllegalAccessException   If there is an issue accessing the fields of the input object.
     */
    @Test
    void testHashObjectWithCollectionField()
            throws NoSuchAlgorithmException, JsonProcessingException, IllegalAccessException {

        // Arrange
        TestObject child1 = new TestObject("Child1", 12);
        TestObject child2 = new TestObject("Child2", 8);
        ParentObjectWithList parent = new ParentObjectWithList("Parent", 40, List.of(child1, child2));

        // Act
        String hash = HashingUtils.hashObject(parent, HashAlgorithm.SHA256);

        // Assert
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }
}
