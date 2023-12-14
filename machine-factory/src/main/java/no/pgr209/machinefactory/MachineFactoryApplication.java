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
	@Profile("!dev") // Exclude CommandLineRunner from testing environments using active profile "dev"
	CommandLineRunner commandLineRunner(DataFeedService dataFeedService) {
		return args -> dataFeedService.initializeData(); // Initialize sample data for API requests.
	}
}
