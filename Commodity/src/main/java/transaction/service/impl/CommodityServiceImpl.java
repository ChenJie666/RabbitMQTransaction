package transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import transaction.entities.Commodity;
import transaction.entities.TbAckOrder;
import transaction.mapper.CommodityMapper;
import transaction.service.CommodityService;

import java.io.IOException;

import static org.springframework.transaction.interceptor.TransactionAspectSupport.*;

/**
 * @author CJ
 * @date 2021/4/15 10:26
 */
@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {

    @RabbitListener(queues = "commodity.queue")
    @Transactional(rollbackFor = Exception.class)
    public void orderQueueConsumer(Message message, TbAckOrder tbAckOrder, Channel channel) throws IOException {
        // TODO 减库存，如果失败拒收消息进入私信队列
        try {
            Long num = tbAckOrder.getNum();
            Long commodityId = tbAckOrder.getCommodityId();
            decreaseStock(commodityId, num);
            int i = 10 / 0;
        } catch (Exception e) {
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            channel.basicNack(deliveryTag, false, false);
            currentTransactionStatus().setRollbackOnly();
        }
    }

    @RabbitListener(queues = "commodity.dead.queue")
    @Transactional(rollbackFor = Exception.class)
    public void deadQueueConsumer(Message message, TbAckOrder tbAckOrder, Channel channel) throws IOException {
        // TODO 减库存，如果失败拒收消息进行报警
        try {
            System.out.println("进入commodity死信队列");
            Long num = tbAckOrder.getNum();
            Long commodityId = tbAckOrder.getCommodityId();
            decreaseStock(commodityId, num);
        } catch (Exception e) {
            System.out.println("WARN -> 消费端消费消息两次都失败");
            System.out.println(e.getMessage());
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            channel.basicNack(deliveryTag, false, false);
            currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public Boolean decreaseStock(Long commodityId, Long num) {
        // TODO 扣减库存
        UpdateWrapper<Commodity> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", commodityId).setSql("stock = stock - " + num);
        return update(null, userUpdateWrapper);
    }

}
