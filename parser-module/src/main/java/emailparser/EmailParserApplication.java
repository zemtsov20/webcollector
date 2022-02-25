package emailparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmailParserApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailParserApplication.class, args);
    }
}
