package eu.kinae.camel.testing.infinite.retry;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;

@Service
public class CamelSpringRabbitTestRoute extends RouteBuilder {

    private boolean autoStartup;

    @Override
    public void configure() throws Exception {
        onException(Throwable.class)
                .log(LoggingLevel.INFO, "InException CamelSpringRabbitTestRoute");

        from("timer://runOnce?repeatCount=1").autoStartup(autoStartup)
                .setBody(constant("Message to camel-spring-rabbitmq"))
                .to("spring-rabbitmq://camel-spring-rabbitmq?routingKey=camel-spring-rabbitmq-rk&queues=camel-spring-rabbitmq-queue&arg.queue.autoDelete=true&arg.exchange.autoDelete=true");

        from("spring-rabbitmq://camel-spring-rabbitmq?routingKey=camel-spring-rabbitmq-rk&queues=camel-spring-rabbitmq-queue&arg.queue.autoDelete=true&arg.exchange.autoDelete=true")
                .throwException(new SocketTimeoutException("Fake external HTTP call resulting in Timeout"))
                .end();
    }

    @Autowired
    public void setAutoStartup(@Value("${spring-rabbitmq:false}") boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

}
