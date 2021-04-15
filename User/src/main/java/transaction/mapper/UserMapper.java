package transaction.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import transaction.entities.User;

/**
 * @author CJ
 * @date: 2021/4/15 23:41
 */
@Component
public interface UserMapper extends BaseMapper<User> {
}
