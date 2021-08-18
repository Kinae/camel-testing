package eu.kinae.camel.testing.rabbit.retry.fix;

import org.apache.camel.component.springrabbit.DefaultListenerContainerFactory;
import org.apache.camel.component.springrabbit.SpringRabbitMQEndpoint;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.stereotype.Component;

@Component
public class FixDefaultListenerContainerFactory extends DefaultListenerContainerFactory {

    @Override
    public AbstractMessageListenerContainer createListenerContainer(SpringRabbitMQEndpoint endpoint) {
        AbstractMessageListenerContainer listener = super.createListenerContainer(endpoint);
        listener.setAdviceChain(advices());
        listener.setDefaultRequeueRejected(false);
        return listener;
    }

    /**
     * Do not let Spring AMQP re-try messages upon failure, leave it to Camel
     */
    private RetryOperationsInterceptor advices() {
        return RetryInterceptorBuilder.stateless()
                .retryPolicy(new NeverRetryPolicy())
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build();
    }
}
