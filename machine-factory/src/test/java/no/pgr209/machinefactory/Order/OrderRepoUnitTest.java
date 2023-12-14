package no.pgr209.machinefactory.Order;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.model.Machine;
import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import no.pgr209.machinefactory.repo.MachineRepo;
import no.pgr209.machinefactory.repo.OrderRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("dev") // Exclude CommandLineRunner from Unit test, ensuring clean database
public class OrderRepoUnitTest {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private MachineRepo machineRepo;

    @Test // Ensure order is created
    public void save_shouldReturnOrder() {
        Order order = orderRepo.save(new Order());

        assertThat(order).isNotNull();
        assertThat(order.getOrderId()).isNotNull();
    }

    @Test // Test many-to-one relationship with Customer
    public void save_shouldReturnOrderWithCustomer() {
        Customer customer = customerRepo.save(new Customer("Harald Skog", "harald@skog.no"));

        Order order = orderRepo.save(new Order());
        order.setCustomer(customer);

        Optional<Order> findOrder = orderRepo.findById(order.getOrderId());
        findOrder.ifPresent(checkOrder -> assertEquals(customer, findOrder.get().getCustomer()));
    }

    @Test // Test many-to-one relationship with Address
    public void save_shouldReturnOrderWithAddress() {
        Address address = addressRepo.save(new Address("Kongens Gate 15", "Oslo", "0153"));

        Order order = orderRepo.save(new Order());
        order.setAddress(address);

        Optional<Order> findOrder = orderRepo.findById(order.getOrderId());
        findOrder.ifPresent(checkOrder -> assertEquals(address, findOrder.get().getAddress()));
    }

    @Test // Test many-to-many relationship with Machine
    public void save_shouldReturnOrderWithMachines() {
        machineRepo.save(new Machine("3D Printer", "Electronics"));
        machineRepo.save(new Machine("Robot Scanner", "Electronics"));
        List<Machine> allMachines = machineRepo.findAll();

        Order order = orderRepo.save(new Order());
        order.setMachines(allMachines);

        Optional<Order> findOrder = orderRepo.findById(order.getOrderId());
        findOrder.ifPresent(checkOrder -> assertEquals(allMachines, findOrder.get().getMachines()));
    }

    @Test // Test findAll and ensure correct amount is returned
    public void findAll_shouldReturnNonEmptyListOfOrders() {
        orderRepo.save(new Order());
        orderRepo.save(new Order());
        List<Order> orders = orderRepo.findAll();

        assertThat(orders).isNotNull();
        assertThat(orders.size()).isEqualTo(2);
    }

    @Test // Test finding order by id
    public void findById_shouldReturnOrder() {
        Order order = orderRepo.save(new Order());

        Optional<Order> foundOrder = orderRepo.findById(order.getOrderId());
        assertThat(foundOrder).isPresent();
    }

    @Test // Test finding a non-existing order.
    public void findById_shouldNotReturnNonExistentOrder() {
        Long nonExistentOrder = 65561L;

        Optional<Order> findOrder = orderRepo.findById(nonExistentOrder);
        assertThat(findOrder).isNotPresent();
    }

    @Test // Create and then update an order
    public void update_shouldUpdateExistingOrder() {

        // Create the order with address information
        Order order = orderRepo.save(new Order());
        Address address = addressRepo.save(new Address("Karihaugsveien 78", "Skjetten", "2013"));
        order.setAddress(address);

        // Find the created order and check the address
        Optional<Order> findOrder = orderRepo.findById(order.getOrderId());
        findOrder.ifPresent(checkOrder -> assertEquals("Karihaugsveien 78", findOrder.get().getAddress().getAddressStreet()));

        // Update order with a new address
        Address newAddress = addressRepo.save(new Address("Kongens Gate 101", "Oslo", "5126"));
        order.setAddress(newAddress);

        // Check order address after update
        Optional<Order> findOrderAfterUpdate = orderRepo.findById(order.getOrderId());
        findOrderAfterUpdate.ifPresent(checkOrder -> assertEquals("Kongens Gate 101", findOrderAfterUpdate.get().getAddress().getAddressStreet()));
    }

    @Test // Create an order, check if order exist, delete the order and then check if order still exist
    public void deleteById_shouldRemoveOrder() {
        Order order = orderRepo.save(new Order());
        Optional<Order> findOrder = orderRepo.findById(order.getOrderId());

        assertThat(findOrder).isPresent();

        orderRepo.deleteById(order.getOrderId());

        Optional<Order> findDeletedOrder = orderRepo.findById(order.getOrderId());
        assertThat(findDeletedOrder).isNotPresent();
    }

}
