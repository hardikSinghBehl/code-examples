package io.reflectoring.entity;

import io.reflectoring.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Setter
@Getter
@DynamoDbBean
@TableName(name = "MedicalRecords")
public class MedicalRecord {

	private String id;

	private String patientName;

	private String diagnosis;

	private String treatmentPlan;

	private String attendingPhysicianName;

	@DynamoDbPartitionKey
	public String getId() {
		return id;
	}

}