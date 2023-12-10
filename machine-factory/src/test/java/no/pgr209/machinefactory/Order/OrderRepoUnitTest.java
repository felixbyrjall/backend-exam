package no.pgr209.machinefactory.Order;

import no.pgr209.machinefactory.model.Address;
import no.pgr209.machinefactory.model.Machine;
import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.MachineRepo;
import no.pgr209.machinefactory.repo.OrderRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("dev") // Seperate CommandLine and Data Jpa test.
public class OrderRepoUnitTest {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private MachineRepo machineRepo;

    @Test
    public void save_shouldReturnSavedOrder() {
        Order order = new Order();
        Order savedOrder = orderRepo.save(order);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getOrderId()).isNotNull();
    }

    @Test // Testing One-to-Many machines.
    public void save_shouldReturnSavedOrderWithMachines() {
        Machine machineOne = machineRepo.save(new Machine("3D Printer", "Electronics"));
        Machine machineTwo = machineRepo.save(new Machine("3D Scanner", "Electronics"));
        List<Machine> allMachines = Arrays.asList(machineOne, machineTwo);

        Order createOrder = new Order();
        createOrder.setMachines(allMachines);
        Order savedOrder = orderRepo.save(createOrder);

        Optional<Order> findOrder = orderRepo.findById(savedOrder.getOrderId());
        findOrder.ifPresent(order -> assertEquals(allMachines, findOrder.get().getMachines()));
    }

    @Test
    public void findAll_shouldReturnNonEmptyList() {
        Order firstOrder = new Order();
        Order secondOrder = new Order();
        orderRepo.save(firstOrder);
        orderRepo.save(secondOrder);

        List<Order> orders = orderRepo.findAll();

        assertThat(orders).isNotNull();
        assertThat(orders.size()).isGreaterThan(0);
    }

    @Test
    public void findById_shouldReturnOrder() {
        Order order = new Order();
        Order savedOrder = orderRepo.save(order);

        Optional<Order> foundOrder = orderRepo.findById(savedOrder.getOrderId());

        assertThat(foundOrder).isPresent();
    }

    @Test
    public void findById_shouldNotReturnNonExistentOrder() {
        Long nonExistentId = 65561L;

        Optional<Order> foundOrder = orderRepo.findById(nonExistentId);

        assertThat(foundOrder).isNotPresent();
    }

    @Test
    public void update_shouldUpdateExistingOrder() {

        // Create the order with address information.
        Order createOrder = new Order();
        Address address = addressRepo.save(new Address("Karihaugsveien 78", "Skjetten", 2013));
        createOrder.setAddress(address);
        Order savedOrder = orderRepo.save(createOrder);

        // Find the created order and check the information.
        Optional<Order> findSavedOrder = orderRepo.findById(savedOrder.getOrderId());
        findSavedOrder.ifPresent(order -> assertEquals("Karihaugsveien 78", findSavedOrder.get().getAddress().getAddressStreet()));

        // Update order with a new address:
        Address newAddress = addressRepo.save(new Address("Kongens Gate 101", "Oslo", 5126));
        savedOrder.setAddress(newAddress);
        orderRepo.save(savedOrder);

        // Check order address after update.
        Optional<Order> findOrderAfterUpdate = orderRepo.findById(savedOrder.getOrderId());
        findOrderAfterUpdate.ifPresent(order -> assertEquals("Kongens Gate 101", findOrderAfterUpdate.get().getAddress().getAddressStreet()));
    }

    @Test
    public void deleteById_shouldRemoveOrder() {
        Order order = new Order();
        Order savedOrder = orderRepo.save(order);
        Optional<Order> findSavedOrder = orderRepo.findById(savedOrder.getOrderId());

        assertThat(findSavedOrder).isPresent();

        orderRepo.deleteById(savedOrder.getOrderId());
        Optional<Order> findDeletedOrder = orderRepo.findById(savedOrder.getOrderId());

        assertThat(findDeletedOrder).isNotPresent();
    }

}
