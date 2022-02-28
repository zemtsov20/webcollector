package emailparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.locks.LockSupport;

@SpringBootApplication(scanBasePackages={"common","emailparser"})
@EnableScheduling
@EnableJpaRepositories("common.repository")
@EntityScan(basePackages = {"common.entity"})
public class EmailParserApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailParserApplication.class, args);
//        new Thread(()->{while (true) LockSupport.parkNanos(Long.MAX_VALUE);}).start();
    }
}
