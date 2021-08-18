package eu.kinae.camel.testing.rabbit.retry.fix;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.AbstractMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

@Component
public class AllowNullMessageConverter extends AbstractMessageConverter {

    private final String defaultCharset = Charset.defaultCharset().name();

    @Override
    protected Message createMessage(Object body, MessageProperties messageProperties) {
        if (body == null) {
            return new Message(null, messageProperties);
        }

        boolean text = body instanceof String;
        byte[] data;
        try {
            String encoding = messageProperties.getContentEncoding();
            if (encoding != null) {
                data = ((String) body).getBytes(encoding);
            } else {
                data = ((String) body).getBytes(defaultCharset);
                messageProperties.setContentEncoding(defaultCharset);
            }
        } catch (UnsupportedEncodingException e) {
            throw new MessageConversionException(
                    "failed to convert to byte[] for rabbitmq message", e);
        }

        messageProperties.setContentLength(data.length);
        Message answer = new Message(data, messageProperties);
        if (MessageProperties.DEFAULT_CONTENT_TYPE.equals(messageProperties.getContentType()) && text) {
            messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        }
        return answer;
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        Object content = null;
        MessageProperties properties = message.getMessageProperties();
        if (properties != null) {
            String contentType = properties.getContentType();
            if (contentType != null && contentType.startsWith("text")) {
                String encoding = properties.getContentEncoding();
                if (encoding == null) {
                    encoding = defaultCharset;
                }
                try {
                    content = new String(message.getBody(), encoding);
                } catch (UnsupportedEncodingException e) {
                    throw new MessageConversionException(
                            "failed to convert text-based Message content", e);
                }
            }
        }
        if (content == null) {
            content = message.getBody();
        }
        return content;
    }
}
