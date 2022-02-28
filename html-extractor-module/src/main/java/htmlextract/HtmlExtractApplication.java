package htmlextract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages={"common","htmlextract"})
@EnableScheduling
@EnableJpaRepositories("common.repository")
@EntityScan(basePackages = {"common.entity"})
public class HtmlExtractApplication {
	public static void main(String[] args) {
		SpringApplication.run(HtmlExtractApplication.class, args);
	}

}
