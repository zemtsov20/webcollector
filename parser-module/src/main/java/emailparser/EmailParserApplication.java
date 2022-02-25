package emailparser;

import emailparser.beans.GetMailsFromHtmlBean;
import emailparser.schedule.ScheduledTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages={"common.repository"})
public class EmailParserApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailParserApplication.class, args);
    }
}
