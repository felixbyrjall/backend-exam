package no.pgr209.machinefactory;

import no.pgr209.machinefactory.controller.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MachineFactoryApplicationTests {

	@Autowired
	private AddressController addressController;

	@Autowired
	private CustomerController customerController;

	@Autowired
	private MachineController machineController;

	@Autowired
	private OrderController orderController;

	@Autowired
	private PartController partController;

	@Autowired
	private SubassemblyController subassemblyController;

	@Test // Check if context load successfully.
	void contextLoads() {
		assertThat(addressController).isNotNull();
		assertThat(customerController).isNotNull();
		assertThat(orderController).isNotNull();
		assertThat(machineController).isNotNull();
		assertThat(subassemblyController).isNotNull();
		assertThat(partController).isNotNull();
	}

}
