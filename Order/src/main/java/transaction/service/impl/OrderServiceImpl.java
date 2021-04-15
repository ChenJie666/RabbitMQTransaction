package transaction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import transaction.entities.TbOrder;
import transaction.mapper.OrderMapper;
import transaction.service.OrderService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author CJ
 * @date 2021/4/15 10:26
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, TbOrder> implements OrderService  {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void confirmCallback(){
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                // TODO 将冗余表状态修改为已接收

            }
        });
    }

    @Override
    public String order(Long commodityId, Long userId, Long points) {
        // TODO 创建订单并发送消息

        return null;
    }

}
