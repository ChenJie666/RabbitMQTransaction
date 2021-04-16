package transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import transaction.entities.TbAckOrder;
import transaction.entities.User;
import transaction.mapper.UserMapper;
import transaction.service.UserService;

import java.io.IOException;

import static org.springframework.transaction.interceptor.TransactionAspectSupport.currentTransactionStatus;


/**
 * @author CJ
 * @date 2021/4/15 10:26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @RabbitListener(queues = "user.queue")
    @Transactional(rollbackFor = Exception.class)
    public void orderQueueConsumer(Message message, TbAckOrder tbAckOrder, Channel channel) throws IOException {
        // TODO 减库存，如果失败拒收消息进入私信队列
        try {
            Long points = tbAckOrder.getPoints();
            Long userId = tbAckOrder.getUserId();
            decreasePoints(userId, points);
        } catch (Exception e) {
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            channel.basicNack(deliveryTag,false,false);
            currentTransactionStatus().setRollbackOnly();
        }
    }

    @RabbitListener(queues = "user.dead.queue")
    @Transactional(rollbackFor = Exception.class)
    public void deadQueueConsumer(Message message, TbAckOrder tbAckOrder, Channel channel) throws IOException {
        // TODO 减库存，如果失败拒收消息进行报警
        try {
            System.out.println("进入user死信队列");
            Long points = tbAckOrder.getPoints();
            Long userId = tbAckOrder.getUserId();
            decreasePoints(userId, points);
        } catch (Exception e) {
            System.out.println("WARN -> 消费端消费消息两次都失败");
            System.out.println(e.getMessage());
            long deliveryTag = message.getMessageProperties().getDeliveryTag();
            channel.basicNack(deliveryTag,false,false);
            currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public Boolean decreasePoints(Long userId, Long points) {
        // TODO 扣减积分
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", userId).setSql("points = points - " + points);
        return update(null, userUpdateWrapper);
    }

}
