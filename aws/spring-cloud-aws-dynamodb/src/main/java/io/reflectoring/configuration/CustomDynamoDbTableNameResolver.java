package io.reflectoring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import io.reflectoring.annotation.TableName;

@Configuration
public class CustomDynamoDbTableNameResolver {

	@Bean
	public DynamoDbTableNameResolver tableNameResolver() {
		return new DynamoDbTableNameResolver() {

			@Override
			public <T> String resolve(Class<T> clazz) {
				TableName tableName = clazz.getAnnotation(TableName.class);
				if (tableName != null && StringUtils.hasText(tableName.name())) {
					return tableName.name();
				}
				throw new IllegalArgumentException(String.format(
						"@TableName annotation is missing or has an empty name on class '%s'.",
						clazz.getSimpleName()));
			}

		};
	}

}
