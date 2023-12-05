package no.pgr209.machinefactory;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Initial
@SpringBootApplication
public class MachineFactoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MachineFactoryApplication.class, args);
	}


	@Bean
	@Profile("!test") // Seperate CommandLine and Testing.
	CommandLineRunner commandLineRunner(
			AddressRepo addressRepo,
			CustomerRepo customerRepo,
			MachineRepo machineRepo,
			OrderRepo orderRepo,
			PartRepo partRepo,
			SubassemblyRepo subassemblyRepo
	){
		return args -> {
			List<Machine> machineList = new ArrayList<>();

			Customer customer = customerRepo.save(new Customer("Ola Nordmann", "ola@nordmann.no"));
			Address address = addressRepo.save(new Address("Storgata 33", "Oslo", 2204));

			var machine1 = new Machine("3D printer", "Electronics");
			var machine2 = new Machine("Speaker", "Electronics");
			machineList.add(machine1);
			machineList.add(machine2);

			Part part = partRepo.save(new Part("Printer nozzle"));
			Subassembly subassembly = subassemblyRepo.save(new Subassembly("Printer head"));

			Order order = orderRepo.save(new Order());

			order.setOrderDate(LocalDateTime.now());
			order.setAddress(address);
			order.setCustomer(customer);
			order.setMachines(machineList);

			orderRepo.save(order);
		};
	}
}
