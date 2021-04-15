package transaction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import transaction.entities.TbOrder;

/**
 * @author CJ
 * @date 2021/4/15 10:26
 */
public interface OrderService extends IService<TbOrder> {
    String order(Long commodityId, Long userId, Long points);
}
