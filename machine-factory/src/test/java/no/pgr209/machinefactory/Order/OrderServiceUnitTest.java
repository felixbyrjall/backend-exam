package no.pgr209.machinefactory.Order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import no.pgr209.machinefactory.repo.MachineRepo;
import no.pgr209.machinefactory.repo.OrderRepo;
import no.pgr209.machinefactory.service.DataFeedService;
import no.pgr209.machinefactory.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceUnitTest {

    @Autowired
    DataFeedService dataFeedService;

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private AddressRepo addressRepo;

    @Mock
    private MachineRepo machineRepo;

    @InjectMocks
    private OrderService orderService;


    @Test
    void shouldReturnAllOrders() {
        List<Order> mockOrders = new ArrayList<>();
        when(orderRepo.findAll()).thenReturn(mockOrders);
        List<Order> orders = orderService.getAllOrders();

        assertEquals(mockOrders, orders);
    }

    @Test
    void shouldReturnOrderById() {
        Order mockOrder = new Order();
        when(orderRepo.findById(1L)).thenReturn(Optional.of(mockOrder));
        Order order = orderService.getOrderById(1L);

        assertEquals(mockOrder, order);
    }

    @Test
    void shouldCreateOrder() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCustomerId(1L);
        orderDTO.setAddressId(1L);
        List<Long> machineIds = List.of(1L, 2L);
        orderDTO.setMachineId(machineIds);
        orderDTO.setOrderDate(LocalDateTime.now());

        Customer mockCustomer = new Customer("Ola Nordmann", "ola@nordmann.no");
        Address mockAddress = new Address("Storgata 33", "Oslo", 2204);
        Machine firstMachine = new Machine("3D Printer", "Electronics");
        Machine secondMachine = new Machine("Speaker", "Electronics");

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
        when(orderRepo.save(any(Order.class))).thenReturn(mockOrder);

        Order createdOrder = orderService.createOrder(orderDTO);

        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getCustomer()).isEqualTo(mockCustomer);
        assertThat(createdOrder.getAddress()).isEqualTo(mockAddress);
        assertThat(createdOrder.getMachines()).containsExactlyInAnyOrderElementsOf(List.of(firstMachine, secondMachine));
    }


}
