package eu.kinae.camel.testing.rabbit.retry;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CamelSpringRabbitTestRoute extends RouteBuilder {

    private boolean autoStartup;

    @Override
    public void configure() throws Exception {
//        onException(Throwable.class)
//                .log(LoggingLevel.INFO, "InException CamelSpringRabbitTestRoute");
//
//        from("timer://CamelSpringRabbitTestRoute?repeatCount=1").autoStartup(autoStartup)
//                .setBody(constant(null))
//                .to("spring-rabbitmq://ex?routingKey=rk&queues=q&arg.queue.autoDelete=false&arg.exchange.autoDelete=false");
//
//        from("spring-rabbitmq://ex?routingKey=rk&queues=q&arg.queue.autoDelete=false&arg.exchange.autoDelete=false" +
//                "&arg.queue.x-dead-letter-exchange=ex-dlq&arg.queue.x-dead-letter-routing-key=dlq-rk")
//                .throwException(new SocketTimeoutException("Fake external HTTP call resulting in Timeout"))
//                .end();
//
//        from("spring-rabbitmq://ex-dlq?routingKey=dlq-rk&queues=q-dlq&arg.queue.autoDelete=false&arg.exchange.autoDelete=false")
//                .process(exchange -> System.out.println("HI from DLX"));
    }

    @Autowired
    public void setAutoStartup(@Value("${spring-rabbitmq:false}") boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

}
