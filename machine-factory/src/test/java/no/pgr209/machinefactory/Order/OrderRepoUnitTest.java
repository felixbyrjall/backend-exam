package no.pgr209.machinefactory.Order;

import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.repo.OrderRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class OrderRepoUnitTest {

    @Autowired
    OrderRepo orderRepo;

    @Test
    void shouldFetchOrders(){
        List<Order> orders = orderRepo.findAll();
        assert orders.size() == 2;
    }
}
