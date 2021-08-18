package eu.kinae.camel.testing.validate;

import org.apache.camel.Expression;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CamelValidateTestRoute extends RouteBuilder {

    private boolean autoStartup;

    @Override
    public void configure() throws Exception {
        from("timer://runOnce?delay=-1").autoStartup(autoStartup)
//            .setHeader("Test", constant(2222))
            .validate((Expression) simple("${in.header.Test} == null || ${in.header.Test} regex '\\d{4}'"));
    }


    @Autowired
    public void setAutoStartup(@Value("${validate:false}") boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

}
