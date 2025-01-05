package io.github.mmirzakhani93;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark specific fields within a class for inclusion in hash calculations.
 * Fields annotated with {@code @HashableField} will be considered when determining
 * the hash value of an object, using utilities such as in {@code HashingUtils}.
 *
 * The annotation includes an {@code order} parameter which determines the sequence
 * in which the annotated fields are processed in the hash calculation. This ensures
 * consistent hash values, regardless of the declared order of fields in the class.
 *
 * Usage of this annotation is particularly useful for cases where only a subset
 * of fields in a class should participate in determining equality or unique identifiers.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HashableField {
    int order();
}
