package no.pgr209.machinefactory.Order;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.repo.OrderRepo;
import no.pgr209.machinefactory.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceUnitTest {

    @MockBean
    private OrderRepo orderRepo;

    @Autowired
    private OrderService orderService;

    @Test // Fetch all orders
    void shouldFetchAllOrders(){
        List<Order> orderList = List.of(new Order(), new Order());
        when(orderRepo.findAll()).thenReturn(orderList);

        var orders = orderService.getAllOrders();
        assert orders.size() == 2;
    }

    @Test // Fetch all orders for any page, with the pagination method.
    void shouldFetchAllOrdersWithPagination(){
        List<Order> allOrders = IntStream.range(0, 12)
                .mapToObj(i -> new Order())
                .collect(Collectors.toList());

        Page<Order> orderPage = new PageImpl<>(allOrders);
        when(orderRepo.findAll(any(PageRequest.class))).thenReturn(orderPage);

        List<Order> orders = orderService.getOrdersByPage(0);
        assertTrue(orders.size() == 12);
    }
}
