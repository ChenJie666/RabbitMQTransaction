package transaction.entities;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import java.io.Serializable;

/**
 * @author CJ
 * @date 2021/4/15 10:15
 */
@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commodity implements Serializable {
    private static final long serialVersionUID = Long.MIN_VALUE;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId
    private Long id;

    private String name;

    private Long stock;

}
