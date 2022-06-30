package eu.kinae.camel.testing.rabbit.transfer;

import com.rabbitmq.client.ConnectionFactory;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class CamelRabbitTransferRoute extends RouteBuilder {

    @Bean
    public ConnectionFactory cloudAMQPConnectionFactory() {
        ConnectionFactory brokerConnectionFactory = new ConnectionFactory();
        brokerConnectionFactory.setHost("dramatic-dromedary.rmq.cloudamqp.com");
        brokerConnectionFactory.setVirtualHost("wfbfdzqj");
        brokerConnectionFactory.setUsername("wfbfdzqj");
        brokerConnectionFactory.setPassword("gQPA0Qs45x0MlMIjtbiVuZ1prULmYIFG");
        brokerConnectionFactory.setAutomaticRecoveryEnabled(true);
        return brokerConnectionFactory;
    }

    @Override
    public void configure() throws Exception {
        from("rabbitmq://event.exchange.fileo?queue=dead.event.change.updateannualreferenceconsumption.to.billing&autoAck=false")
//                .autoStartup(autoStartup)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("GET : " + exchange.getIn().getBody(String.class));
                    }
                });
    }

}
