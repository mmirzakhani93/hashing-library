package io.github.mmirzakhani93;

/**
 * An enumeration representing supported hash algorithms.
 * This is typically used to abstract the names of hashing algorithms and
 * provide a more type-safe way to reference them within the application.
 *
 * The enumeration currently includes:
 * - SHA256: Representing the "SHA-256" hash algorithm.
 *
 * Each constant has an associated human-readable algorithm name string.
 * This name is primarily used when interacting with utilities such as
 * {@code java.security.MessageDigest} or similar frameworks that require
 * the algorithm's name as a parameter.
 */
public enum HashAlgorithm {

    SHA256("SHA-256");

    // Field to store the human-readable algorithm name.
    private final String algorithmName;

    // Parameterized constructor for the enum.
    HashAlgorithm(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    @Override
    public String toString() {
        return algorithmName;
    }
}
