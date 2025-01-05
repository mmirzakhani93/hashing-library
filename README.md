# HashingUtils Library

The `HashingUtils` library provides a utility for generating hash values for objects based on annotated fields. It processes object hierarchies, collections, and nested objects, allowing you to compute consistent hash values for complex data structures.

## Features

- **Custom Hashing:** Generate consistent hash values using specified algorithms like `SHA-256`.
- **Annotation-Based Selection:** Only fields annotated with `@HashableField` are included in the hashing process.
- **Recursive Processing:** Handles nested objects and collections.
- **Base64 Encoding:** Produces Base64-encoded hash strings.
- **Field Sorting:** Sorts fields based on the `@HashableField(order)` value to ensure order-independent hashing.

---

## Requirements

- **Java Version:** 11 or higher

---

## Installation

To include this library in your project, copy the `HashingUtils` class and ensure you have the required dependencies (such as Jackson). You can add the following Maven dependency for Jackson in your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.mmirzakhani93</groupId>
    <artifactId>hashing-library</artifactId>
    <version>1.0.0</version> <!-- Use the latest version -->
</dependency>
```

---

## Usage

### 1. Annotate Fields for Hashing

Use the `@HashableField` annotation to specify which fields should be included in the hashing process. Optionally, define the `order` attribute if you want to control the order of fields during hashing.

**Example:**

```java
import io.github.mmirzakhani93.HashableField;

public class Person {
    @HashableField(order = 1)
    private String name;

    @HashableField(order = 2)
    private int age;

    @HashableField(order = 3)
    private Address address;

    // Getters and setters...
}
```

Here, only the `name`, `age`, and `address` fields will be considered in the hashing process.

---

### 2. Hash an Object

Use the `HashingUtils` library to compute the hash of an object. You need to provide an object with annotated fields and specify a hashing algorithm (e.g., `SHA-256` or `MD5`).

**Example:**

```java
import io.github.mmirzakhani93.HashingUtils;
import io.github.mmirzakhani93.HashAlgorithm;

public class Main {
    public static void main(String[] args) {
        try {
            Person person = new Person();
            person.setName("John Doe");
            person.setAge(30);

            Address address = new Address();
            address.setCity("New York");
            address.setZipcode("10001");

            person.setAddress(address);

            // Calculate hash
            String hash = HashingUtils.hashObject(person, HashAlgorithm.SHA256);

            System.out.println("Object Hash: " + hash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

---

### 3. Supported Algorithms

Use the `HashAlgorithm` enum to specify the algorithm. Supported values include:

- `SHA-256`

---

### 4. Advanced Features

#### Nested Objects

The library processes nested objects and collections recursively. All annotated fields within nested objects are included in the hash computation.

**Example:**

```java
public class Address {
    @HashableField(order = 1)
    private String city;

    @HashableField(order = 2)
    private String zipcode;

    // Getters and setters...
}
```

#### Collections

For fields that are collections, each item in the collection is processed recursively. For example:

```java
public class Team {
    @HashableField(order = 1)
    private String name;

    @HashableField(order = 2)
    private List<Person> members;

    // Getters and setters...
}
```

All the hashable fields from each `Person` in the `members` list will be processed.

---

## Exceptions Handled

- **`NoSuchAlgorithmException`:** Thrown if the specified hashing algorithm is unavailable.
- **`JsonProcessingException`:** Occurs if object serialization to JSON fails.
- **`IllegalAccessException`:** Thrown if the library cannot access annotated fields.

---

## Contribution

If you would like to contribute to this library, please feel free to submit a pull request or report issues on the project repository.

---

## License

This library is open-source and freely available for use.