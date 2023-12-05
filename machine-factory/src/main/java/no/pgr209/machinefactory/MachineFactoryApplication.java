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
			List<Machine> machineList1 = new ArrayList<>();


			Customer customer1 = customerRepo.save(new Customer("Ola Normann", "ola@normann.no"));
			Address address1 = addressRepo.save(new Address("Storgata 33", "Oslo", 2204));

			var machine1 = new Machine("3D printer", "Electronics");
			var machine2 = new Machine("Speaker", "Electronics");

			machineList1.add(machine1);
			machineList1.add(machine2);

			Part part = partRepo.save(new Part("Printer nozzle"));
			Subassembly subassembly = subassemblyRepo.save(new Subassembly("Printer head"));

			Order order1 = orderRepo.save(new Order());

			order1.setOrderDate(LocalDateTime.now());
			order1.setAddress(address1);
			order1.setCustomer(customer1);
			order1.setMachines(machineList1);

			orderRepo.save(order1);

			/*List<Machine> machineList2 = new ArrayList<>();

			Customer customer2 = customerRepo.save(new Customer("Kong Harald", "ola@normann.no"));

			Address address2 = addressRepo.save(new Address("Trondheimsveien 21", "Oslo", 1234));
			Address address3 = addressRepo.save(new Address("Karl Johans gate 12", "Oslo", 4342));

			var machine3 = new Machine("3D printer", "Electronics");
			var machine4 = new Machine("Speaker", "Electronics");
			var machine5 = new Machine("Monitor", "Electronics");

			machineList2.add(machine3);
			machineList2.add(machine4);
			machineList2.add(machine5);

			Order order2 = orderRepo.save(new Order());

			order2.setOrderDate(LocalDateTime.now());
			order2.setAddress(address3);
			order2.setCustomer(customer2);
			order2.setMachines(machineList2);

			orderRepo.save(order2);*/
		};
	}
}
