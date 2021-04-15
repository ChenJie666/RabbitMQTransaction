package transaction.service;

import com.baomidou.mybatisplus.extension.service.IService;
import transaction.entities.User;

/**
 * @author CJ
 * @date 2021/4/15 10:26
 */
public interface UserService extends IService<User> {
    String decreasePoints(Long userId, Long points);
}
