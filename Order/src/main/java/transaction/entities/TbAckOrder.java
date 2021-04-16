package transaction.entities;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author CJ
 * @date 2021/4/16 9:44
 */
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TbAckOrder implements Serializable {
    private static final long serialVersionUID = Long.MIN_VALUE;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 购买的商品id
     */
    private Long commodityId;

    /**
     * 购买的数量
     */
    private Long num;

    /**
     * 使用的积分数
     */
    private Long points;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 累积发送次数，3次及以上发送且未确认，需要报警
     */
    private Short count;

    /**
     * 消息状态；0表示未确认，1表示已确认
     */
    private Short status;
}
