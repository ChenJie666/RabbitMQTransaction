package transaction.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import transaction.entities.TbAckOrder;
import transaction.service.AckOrderService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author CJ
 * @date: 2021/4/15 23:27
 */
@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Resource
    private AckOrderService ackOrderService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0/5 * * * * ?")
    public void sendUnreadyMessage() {
        System.out.println("定时查询未发送成功的信息");
        // TODO 定时查询未确认的消息并发送，筛选出超过最大重试次数的记录报警
        QueryWrapper<TbAckOrder> tbAckOrderQueryWrapper = new QueryWrapper<>();
        tbAckOrderQueryWrapper.eq("status", 0);
        List<TbAckOrder> tbAckOrders = ackOrderService.listAckOrder(tbAckOrderQueryWrapper);
        tbAckOrders.forEach(tbAckOrder -> {
            Long id = tbAckOrder.getId();
            // 如果重试超过一定次数，则报警
            Short count = tbAckOrder.getCount();
            if (count > 3) {
                System.out.println("WARN -> 冗余表中的" + id + "记录重试超过次数");
                return;
            }
            UpdateWrapper<TbAckOrder> tbAckOrderUpdateWrapper = new UpdateWrapper<>();
            tbAckOrderUpdateWrapper.eq("id", id).set("count",tbAckOrder.getCount() + 1);
            ackOrderService.updateAckOrder(tbAckOrderUpdateWrapper);
            System.out.println("重试发送信息：" + id);
            rabbitTemplate.convertAndSend("order.exchange", "", tbAckOrder, new CorrelationData(id.toString()));
        });

    }

}
