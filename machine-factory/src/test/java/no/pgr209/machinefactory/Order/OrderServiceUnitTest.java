package no.pgr209.machinefactory.Order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.repo.OrderRepo;
import no.pgr209.machinefactory.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceUnitTest {

    @Mock
    private OrderRepo orderRepo;

    /*
    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private AddressRepo addressRepo;

    @Mock
    private MachineRepo machineRepo;
     */

    @InjectMocks
    private OrderService orderService;


    @Test
    void shouldReturnAllOrders() {

        List<Order> mockOrders = new ArrayList<>();
        when(orderRepo.findAll()).thenReturn(mockOrders);

        List<Order> orders = orderService.getAllOrders();

        assertEquals(mockOrders, orders);
    }


}
