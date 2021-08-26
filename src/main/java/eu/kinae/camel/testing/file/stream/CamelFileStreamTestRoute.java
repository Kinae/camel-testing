package eu.kinae.camel.testing.file.stream;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CamelFileStreamTestRoute extends RouteBuilder {

    private boolean autoStartup;

    @Override
    public void configure() throws Exception {
        from("file:/tmp/utf8-convert/?charset=utf-8")
                .autoStartup(autoStartup)
                .split(body()).streaming()
                    .convertBodyTo(byte[].class, "iso-8859-1")
                    .to("stream:file?fileName=/tmp/utf8-convert-to-iso/converted.txt&closeOnDone=true")
                .end();
    }


    @Autowired
    public void setAutoStartup(@Value("${file-stream:false}") boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

}
