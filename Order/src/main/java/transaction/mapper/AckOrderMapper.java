package transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Component;
import transaction.entities.TbAckOrder;

/**
 * @author CJ
 * @date 2021/4/16 10:02
 */
@Component
public interface AckOrderMapper extends BaseMapper<TbAckOrder> {
}
