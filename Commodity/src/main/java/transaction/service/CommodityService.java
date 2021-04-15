package transaction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import transaction.entities.Commodity;

/**
 * @author CJ
 * @date 2021/4/15 10:26
 */
public interface CommodityService extends IService<Commodity> {
    String decreaseStock(Long commodityId);
}
