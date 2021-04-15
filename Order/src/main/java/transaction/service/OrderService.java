package transaction.service;

/**
 * @author CJ
 * @date 2021/4/15 10:26
 */
public interface OrderService {
    String order(Long commodityId, Long userId, Long points);
}
