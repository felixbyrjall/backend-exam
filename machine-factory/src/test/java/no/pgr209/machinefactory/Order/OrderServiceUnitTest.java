package no.pgr209.machinefactory.Order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import no.pgr209.machinefactory.repo.MachineRepo;
import no.pgr209.machinefactory.repo.OrderRepo;
import no.pgr209.machinefactory.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("dev") // Exclude CommandLineRunner from Unit test, ensuring clean database
public class OrderServiceUnitTest {

    @Autowired
    OrderService orderService;

    @MockBean
    private OrderRepo orderRepo;

    @MockBean
    private CustomerRepo customerRepo;

    @MockBean
    private AddressRepo addressRepo;

    @MockBean
    private MachineRepo machineRepo;

    @Test // Mock and test fetching all orders
    void shouldReturnAllOrders() {
        List<Order> mockOrders = new ArrayList<>();
        when(orderRepo.findAll()).thenReturn(mockOrders);
        List<Order> orders = orderService.getAllOrders();

        assertEquals(mockOrders, orders);
    }

    @Test // Mock and test fetching order by id
    void shouldReturnOrderById() {
        Order mockOrder = new Order();
        when(orderRepo.findById(1L)).thenReturn(Optional.of(mockOrder));
        Order order = orderService.getOrderById(1L);

        assertEquals(mockOrder, order);
    }

    @Test // Comprehensive unit-testing, creating an order
    void shouldCreateOrder() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomerId(1L);
        orderDTO.setAddressId(1L);
        List<Long> machineIds = List.of(1L, 2L);
        orderDTO.setMachineId(machineIds);
        orderDTO.setOrderDate(LocalDateTime.now());

        Customer mockCustomer = new Customer("Ola Nordmann", "ola@nordmann.no");
        Address mockAddress = new Address("Storgata 33", "Oslo", "0154");
        Machine firstMachine = new Machine("Surface Mount Technology Machine", "Electronics");
        Machine secondMachine = new Machine("Pick and Place Machine", "Assembly");

        when(customerRepo.existsById(1L)).thenReturn(true);
        when(customerRepo.findById(1L)).thenReturn(Optional.of(mockCustomer));
        when(addressRepo.existsById(1L)).thenReturn(true);
        when(addressRepo.findById(1L)).thenReturn(Optional.of(mockAddress));
        when(machineRepo.existsById(any())).thenReturn(true);
        when(machineRepo.findAllById(machineIds)).thenReturn(List.of(firstMachine, secondMachine));

        Order mockOrder = new Order();
        mockOrder.setCustomer(mockCustomer);
        mockOrder.setAddress(mockAddress);
        mockOrder.setMachines(List.of(firstMachine, secondMachine));
        when(orderRepo.save(any())).thenReturn(mockOrder);

        Order createdOrder = orderService.createOrder(orderDTO);

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getCustomer()).isEqualTo(mockCustomer);
        assertThat(createdOrder.getAddress()).isEqualTo(mockAddress);
        assertThat(createdOrder.getMachines()).containsExactlyInAnyOrderElementsOf(List.of(firstMachine, secondMachine));
    }
}
