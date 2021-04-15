package transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import transaction.entities.TbOrder;

/**
 * @author CJ
 * @date: 2021/4/15 23:31
 */
@Component
public interface OrderMapper extends BaseMapper<TbOrder> {
}
