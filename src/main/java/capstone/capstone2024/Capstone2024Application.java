package capstone.capstone2024;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Capstone2024Application {

	public static void main(String[] args) {
		SpringApplication.run(Capstone2024Application.class, args);
	}

}
