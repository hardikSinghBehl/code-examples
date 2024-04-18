## Implementing Publisher-Subsriber Pattern in Spring Boot using AWS SNS and SQS

Codebase demonstrating the implementation of publisher-subscriber pattern using AWS SNS and SQS in Spring Boot. [Spring Cloud AWS](https://spring.io/projects/spring-cloud-aws) is used to interact with AWS services in context.

[LocalStack](https://github.com/localstack/localstack) has been used to containerize the multi module maven project for local development. The below commands can be used to start the project

```bash
mvn clean package spring-boot:build-image
```
```bash
sudo docker-compose up -d
```

## Blog posts

Blog posts about this topic:

* [Pub/Sub Pattern using AWS SNS and SQS in Spring Boot](https://reflectoring.io/aws-pubsub-spring-boot/)