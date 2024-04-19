package io.reflectoring.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SnsTopicExistenceValidator.class)
public @interface SnsTopicExists {

	String message() default "No SNS topic exists with configured name.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}