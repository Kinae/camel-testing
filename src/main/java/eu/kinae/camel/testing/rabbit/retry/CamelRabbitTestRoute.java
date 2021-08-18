package eu.kinae.camel.testing.rabbit.retry;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;

@Service
public class CamelRabbitTestRoute extends RouteBuilder {

    private boolean autoStartup;

    @Override
    public void configure() throws Exception {
        onException(Throwable.class)
                .log(LoggingLevel.INFO, "InException CamelRabbitTestRoute");

        from("timer://CamelRabbitTestRoute?repeatCount=1").autoStartup(autoStartup)
                .setBody(constant("Message to camel-rabbitmq"))
                .to("rabbitmq://camel-rabbitmq?routingKey=camel-rabbitmq-rk&queue=camel-rabbitmq-queue&autoDelete=true");

        from("rabbitmq://camel-rabbitmq?routingKey=camel-rabbitmq-rk&queue=camel-rabbitmq-queue&autoDelete=true")
                .throwException(new SocketTimeoutException("Fake external HTTP call resulting in Timeout"))
                .end();
    }

    @Autowired
    public void setAutoStartup(@Value("${rabbitmq:false}") boolean autoStartup) {
        this.autoStartup = autoStartup;
    }
}
