package transaction.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import transaction.entities.TbAckOrder;
import transaction.mapper.AckOrderMapper;
import transaction.service.AckOrderService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author CJ
 * @date 2021/4/16 10:03
 */
@Service
public class AckOrderServiceImpl extends ServiceImpl<AckOrderMapper, TbAckOrder> implements AckOrderService {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<TbAckOrder> listAckOrder(QueryWrapper<TbAckOrder> queryWrapper) {
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    public Long saveAckOrder(TbAckOrder tbAckOrder) {
        save(tbAckOrder);
        return tbAckOrder.getId();
    }

    @Override
    public Integer updateAckOrder(UpdateWrapper<TbAckOrder> updateWrapper) {
        return getBaseMapper().update(null, updateWrapper);
    }

    @PostConstruct
    public void confirmCallback() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                // TODO 将冗余表状态修改为已接收
                System.out.println("消息进入broker的回调");
                String ackOrderId = correlationData.getId();
                if (!ack) {
                    System.out.println("MQ队列应答失败，orderId是：" + ackOrderId);
                }

                try {
                    UpdateWrapper<TbAckOrder> tbAckOrderUpdateWrapper = new UpdateWrapper<>();
                    tbAckOrderUpdateWrapper.eq("id",ackOrderId).set("status", 1);
                    boolean update = update(null, tbAckOrderUpdateWrapper);
                    Assert.isTrue(update,"回调修改冗余表状态失败");
                } catch (Exception e) {
                    System.out.println();
                }
            }
        });
    }
}
