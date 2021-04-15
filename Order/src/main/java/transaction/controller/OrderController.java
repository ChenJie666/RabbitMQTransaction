package transaction.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/order")
    public String order(@RequestParam Long commodityId,
                        @RequestParam Long userId,
                        @RequestParam Long points) {
        return orderService.order(commodityId, userId, points);
    }

}
