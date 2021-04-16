package transaction.config;

import org.springframework.amqp.core.*;
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
        FanoutExchange fanoutExchange = new FanoutExchange("order.exchange",true,false);
        DirectExchange userDeadExchange = new DirectExchange("user.dead.exchange", true, false);
        DirectExchange commodityDeadExchange = new DirectExchange("commodity.dead.exchange", true, false);

        amqpAdmin.declareExchange(fanoutExchange);
        amqpAdmin.declareExchange(userDeadExchange);
        amqpAdmin.declareExchange(commodityDeadExchange);
    }

    @PostConstruct
    public void createQueue() {
        HashMap<String, Object> userArguments = new HashMap<>(4);
        userArguments.put("x-dead-letter-exchange", "user.dead.exchange");
        userArguments.put("x-dead-letter-routing-key", "user.dead.routing");
        Queue userQueue = new Queue("user.queue", true, false, false, userArguments);

        HashMap<String, Object> commodityArguments = new HashMap<>(4);
        commodityArguments.put("x-dead-letter-exchange", "commodity.dead.exchange");
        commodityArguments.put("x-dead-letter-routing-key", "commodity.dead.routing");
        Queue commodityQueue = new Queue("commodity.queue", true, false, false, commodityArguments);


        Queue userDeadQueue = new Queue("user.dead.queue", true, false, false);

        Queue commodityDeadQueue = new Queue("commodity.dead.queue", true, false, false);

        amqpAdmin.declareQueue(userQueue);
        amqpAdmin.declareQueue(commodityQueue);
        amqpAdmin.declareQueue(userDeadQueue);
        amqpAdmin.declareQueue(commodityDeadQueue);
    }

    @PostConstruct
    public void binding() {
        Binding userBinding = new Binding("user.queue", Binding.DestinationType.QUEUE, "order.exchange", "", null);
        amqpAdmin.declareBinding(userBinding);
        Binding commodityBinding = new Binding("commodity.queue", Binding.DestinationType.QUEUE, "order.exchange", "", null);
        amqpAdmin.declareBinding(commodityBinding);

        Binding userDeadBinding = new Binding("user.dead.queue", Binding.DestinationType.QUEUE, "user.dead.exchange", "user.dead.routing", null);
        amqpAdmin.declareBinding(userDeadBinding);

        Binding commodityDeadBinding = new Binding("commodity.dead.queue", Binding.DestinationType.QUEUE, "commodity.dead.exchange", "commodity.dead.routing", null);
        amqpAdmin.declareBinding(commodityDeadBinding);
    }

}
