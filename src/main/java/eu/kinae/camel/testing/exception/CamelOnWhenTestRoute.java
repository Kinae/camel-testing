package eu.kinae.camel.testing.exception;

import org.apache.camel.CamelException;
import org.apache.camel.CamelUnitOfWorkException;
import org.apache.camel.LoggingLevel;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CamelOnWhenTestRoute extends RouteBuilder {

    private boolean autoStartup;

    @Override
    public void configure() throws Exception {
        onException(CamelException.class)
                .handled(true)
                .process(exchange -> System.out.println("AAAA"))
                .log("A thrown... ${exception.message}");
        onException(CamelUnitOfWorkException.class)
                .handled(true)
                .process(exchange -> System.out.println("BBBB"))
                .log("B thrown... ${exception.message}");

        from("timer://CamelOnWhenTestRoute?delay=-1").routeId("CamelOnWhenTestRoute")//.autoStartup(autoStartup)
                .process(exchange -> System.out.println("start"))
                .doTry()
                    .throwException(RuntimeCamelException.class, "A")
                .doCatch(RuntimeCamelException.class)
                    .process(exchange -> System.out.println("in catch A"))
                    .log(LoggingLevel.WARN, "catch A message : ${exception.message}")
                    .onWhen(simple("${exception.message} contains 'A'"))
                        .process(exchange -> System.out.println("in onWhen A"))
                        .throwException(new CamelException("No Duplicate A allowed"));
//                    .onWhen(exceptionMessage().contains("B"))
//                        .throwException(CamelUnitOfWorkException.class, "No Duplicate B allowed");
    }

    @Autowired
    public void setAutoStartup(@Value("${exception-onWhen:true}") boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

}
