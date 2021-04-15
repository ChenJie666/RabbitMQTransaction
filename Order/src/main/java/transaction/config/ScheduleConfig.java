package transaction.config;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author CJ
 * @date: 2021/4/15 23:27
 */
@EnableScheduling
public class ScheduleConfig {

    @Scheduled(cron = "0/5 * * * * ? *")
    public void sendUnreadyMessage(){
        System.out.println("定时查询未发送成功的信息");
        // TODO 定时查询未确认的消息并发送，筛选出超过最大重试次数的记录报警

    }

}
