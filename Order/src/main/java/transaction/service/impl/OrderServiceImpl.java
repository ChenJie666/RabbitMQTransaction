package transaction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import transaction.entities.TbAckOrder;
import transaction.entities.TbOrder;
import transaction.mapper.OrderMapper;
import transaction.service.AckOrderService;
import transaction.service.OrderService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author CJ
 * @date 2021/4/15 10:26
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, TbOrder> implements OrderService {

    @Resource
    private AckOrderService ackOrderService;

    @Resource
    private RabbitTemplate rabbitTemplate;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String order(TbOrder tbOrder) {
        // TODO 创建订单并发送消息
        // 1. 存储订单
        boolean save = save(tbOrder);

        // 2. 存储冗余表
        TbAckOrder tbAckOrder = new TbAckOrder(
                null, tbOrder.getUserId(), tbOrder.getCommodityId(), tbOrder.getNum(),
                tbOrder.getPoints(), tbOrder.getId(),(short)0, (short) 0);
        Long ackOrderId = ackOrderService.saveAckOrder(tbAckOrder);

        // 3. 发送消息
        rabbitTemplate.convertAndSend("order.exchange","",tbAckOrder,new CorrelationData(ackOrderId.toString()));

        return "ok";
    }

}
