package no.pgr209.machinefactory;

import no.pgr209.machinefactory.service.DataFeedService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

// Initial
@SpringBootApplication
public class MachineFactoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MachineFactoryApplication.class, args);
	}

	@Bean
	@Profile("!test") // Ensure it runs only when not in the test profile
	CommandLineRunner commandLineRunner(DataFeedService dataFeedService) {
		return args -> dataFeedService.initializeData(); // Initialize data for API requests.
	}
}
