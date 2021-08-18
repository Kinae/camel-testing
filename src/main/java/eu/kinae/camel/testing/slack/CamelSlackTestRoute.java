package eu.kinae.camel.testing.slack;

import com.slack.api.model.Message;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CamelSlackTestRoute extends RouteBuilder {

    private boolean autoStartup;

    @Override
    public void configure() throws Exception {
        from("timer://CamelSlackTestRoute_A?repeatCount=1").autoStartup(autoStartup)
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Message message = new Message();
                        message.setBlocks(Collections.singletonList(SectionBlock
                                .builder()
                                .text(MarkdownTextObject
                                        .builder()
                                        .text("*Hello from Camel!*")
                                        .build())
                                .build()));

                        exchange.getIn().setBody(message);
                    }
                })
                .to("slack:#billing-test?token=RAW(XXXX)");

        from("timer://CamelSlackTestRoute_B?repeatCount=5").autoStartup(autoStartup)
                .setBody(simple("Hello from Camel 3.9.0-SNAPSHOT ${date:now}"))
                .to("slack:#random?webhookUrl=https://hooks.slack.com/services/XXX/XXX/XXX");

        from("slack:billing-test?conversationType=PRIVATE_CHANNEL&token=RAW(XXX)&delay=15000")
                .autoStartup(autoStartup)
                .process(exchange -> System.out.println("Received : " + exchange.getIn().getBody()));
    }

    @Autowired
    public void setAutoStartup(@Value("${slack:false}") boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

}
