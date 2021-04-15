package transaction.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import transaction.entities.User;
import transaction.mapper.UserMapper;
import transaction.service.UserService;

/**
 * @author CJ
 * @date 2021/4/15 10:26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @RabbitListener(queues = "")
    public void consumer(){
        // TODO
    }

    @Override
    public String decreasePoints(Long userId, Long points) {
        // TODO
        return null;
    }

}
