package transaction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import transaction.entities.Commodity;
import transaction.mapper.CommodityMapper;
import transaction.service.CommodityService;

/**
 * @author CJ
 * @date 2021/4/15 10:26
 */
@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements CommodityService {

    @RabbitListener(queues = "order-queue")
    public void orderQueueConsumer(){
        // TODO 减库存，如果失败拒收消息进入私信队列
    }

    @RabbitListener(queues = "dead-queue")
    public void deadQueueConsumer(){
        // TODO 减库存，如果失败拒收消息进行报警
    }

    @Override
    public String decreaseStock(Long commodityId) {
        // TODO
        return null;
    }

}
