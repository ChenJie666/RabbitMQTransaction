package transaction.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import transaction.entities.TbOrder;
import transaction.service.OrderService;

import javax.annotation.Resource;

/**
 * @author CJ
 * @date 2021/4/15 10:23
 */
@RestController
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/order")
    public String order(@RequestBody TbOrder tbOrder) {
        return orderService.order(tbOrder);
    }

}
