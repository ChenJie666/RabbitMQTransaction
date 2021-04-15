package transaction.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author CJ
 * @date: 2021/4/15 23:15
 */
@Configuration
public class RabbitConfig {

    @Resource
    private AmqpAdmin amqpAdmin;

    @PostConstruct
    public void createExchange() {
        DirectExchange orderExchange = new DirectExchange("order-exchange", true, false);
        DirectExchange deadExchange = new DirectExchange("dead-exchange", true, false);

        amqpAdmin.declareExchange(orderExchange);
        amqpAdmin.declareExchange(deadExchange);
    }

    @PostConstruct
    public void createQueue() {
        Queue orderQueue = new Queue("order-queue", true, false, false);

        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange-delay", "deadExchange");
        Queue deadQueue = new Queue("dead-queue", true, false, false, arguments);

        amqpAdmin.declareQueue(orderQueue);
        amqpAdmin.declareQueue(deadQueue);
    }

    @PostConstruct
    public void binding() {
        Binding orderBinding = new Binding("order-queue", Binding.DestinationType.QUEUE, "order-exchange", "order.routing", null);
        amqpAdmin.declareBinding(orderBinding);

        Binding deadBinding = new Binding("dead-queue", Binding.DestinationType.QUEUE, "dead-exchange", "dead.routing", null);
        amqpAdmin.declareBinding(deadBinding);
    }

}
