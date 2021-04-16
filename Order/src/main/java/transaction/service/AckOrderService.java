package transaction.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import transaction.entities.TbAckOrder;

import java.util.List;

/**
 * @author CJ
 * @date 2021/4/16 10:03
 */
public interface AckOrderService extends IService<TbAckOrder> {
    List<TbAckOrder> listAckOrder(QueryWrapper<TbAckOrder> queryWrapper);
    Long saveAckOrder(TbAckOrder tbAckOrder);
    Integer updateAckOrder(UpdateWrapper<TbAckOrder> updateWrapper);
}
