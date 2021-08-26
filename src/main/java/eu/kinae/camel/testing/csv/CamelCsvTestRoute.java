package eu.kinae.camel.testing.csv;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.csv.CsvDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CamelCsvTestRoute extends RouteBuilder {

    private boolean autoStartup;

    @Override
    public void configure() throws Exception {
        CsvDataFormat csvWithHeader = new CsvDataFormat().setDelimiter(';').setHeader(new String[]{"contract_reference", "phone_number"});

        from("timer://CamelCsvTestRoute?repeatCount=1").autoStartup(autoStartup)
                .setHeader(Exchange.FILE_NAME, simple("name.csv"))
                .bean(AddRandomValue.class, "process")
                .marshal(csvWithHeader)
        .to("file:/tmp/");

    }


    @Autowired
    public void setAutoStartup(@Value("${csv:false}") boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

}
