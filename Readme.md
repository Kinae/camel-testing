*Setup RabbitMQ docker env*

`docker pull rabbitmq:3.8-management`

`docker run -d --hostname rabbit_test --name rabbitmq -p 15672:15672 -p 5672:5672 rabbitmq:3.8-management`


*1: Camel-RabbitMQ*

`./mvnw spring-boot:run -Dspring-boot.run.arguments="--rabbitmq=true"`

*2: Camel-Spring-RabbitMQ*

`./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring-rabbitmq=true"`

*3: Camel-Spring-RabbitMQ with a fix*

`./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring-rabbitmq=true --camel.component.spring-rabbitmq.listener-container-factory=#fixDefaultListenerContainerFactory"`

*4: Camel-Slack*

`./mvnw spring-boot:run -Dspring-boot.run.arguments="--slack=true"`

*5: Camel-File*

`./mvnw spring-boot:run -Dspring-boot.run.arguments="--file=true"`

*6: Camel-Validate*

`./mvnw spring-boot:run -Dspring-boot.run.arguments="--validate=true"`

*7: Camel-CSV*

`./mvnw spring-boot:run -Dspring-boot.run.arguments="--csv=true"`

*8: Camel-Stream*

`./mvnw spring-boot:run -Dspring-boot.run.arguments="--file-stream=true"`
