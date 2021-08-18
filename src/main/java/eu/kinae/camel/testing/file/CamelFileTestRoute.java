package eu.kinae.camel.testing.file;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CamelFileTestRoute extends RouteBuilder {

    private boolean autoStartup;

    @Override
    public void configure() throws Exception {
        from("file:/tmp/utf8-convert/?charset=utf-8")
                .autoStartup(autoStartup)
                .convertBodyTo(byte[].class, "iso-8859-1")
                .to("file:/tmp/utf8-convert-to-iso/");
    }


    @Autowired
    public void setAutoStartup(@Value("${file:false}") boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

}
