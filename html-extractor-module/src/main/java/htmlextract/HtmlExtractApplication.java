package htmlextract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HtmlExtractApplication {
	public static void main(String[] args) {
		SpringApplication.run(HtmlExtractApplication.class, args);
	}

}
