package eu.kinae.camel.testing.aggregate;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CamelAggregateTestRoute extends RouteBuilder {

    private boolean autoStartup;

    @Override
    public void configure() throws Exception {
        from("file:/tmp/test-aggregate.txt?moveFailed=.error/")
                .split().tokenize(",", 1)
                    .to("direct:test1?synchronous=true");

        from("direct:test1")
                .log(LoggingLevel.INFO, "Before aggregate, file still locked")
                .aggregate(body(), new MyCompletionStrategy()).completionSize(1000)
                .log(LoggingLevel.INFO, "After aggregate, file is moved to .camel as OK")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println("Sleeping...");
                        Thread.sleep(100000);
                    }
                })
                .throwException(new RuntimeException("CustomException"));

    }

    public final class MyCompletionStrategy implements AggregationStrategy {



        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            if (oldExchange == null) {
                return newExchange;
            }

            throw new RuntimeException("ONONOn");
        }
    }

    @Autowired
    public void setAutoStartup(@Value("${aggregate:false}") boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

}
