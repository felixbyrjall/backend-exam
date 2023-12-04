package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.repo.OrderRepo;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    //Connection to the order repository
    private final OrderRepo orderRepo;

    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }
}
