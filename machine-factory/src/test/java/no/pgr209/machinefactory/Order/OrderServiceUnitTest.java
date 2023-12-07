package no.pgr209.machinefactory.Order;

import no.pgr209.machinefactory.model.Customer;
import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.repo.CustomerRepo;
import no.pgr209.machinefactory.repo.OrderRepo;
import no.pgr209.machinefactory.service.CustomerService;
import no.pgr209.machinefactory.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceUnitTest {

    @MockBean
    private OrderRepo orderRepo;

    @Autowired
    private OrderService orderService;

    @Test
    void shouldFetchAllOrders(){
        List<Order> orderList = List.of(new Order(), new Order(), new Order());
        when(orderRepo.findAll()).thenReturn(orderList);

        var ordersOnAllPages = orderService.getOrders(Pageable.unpaged());
        List<Order> orders = ordersOnAllPages.getContent();
        assert orders.size() == 3;
    }

}
