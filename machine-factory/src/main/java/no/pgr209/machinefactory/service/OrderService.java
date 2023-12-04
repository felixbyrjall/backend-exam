package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    //Connection to the order repository
    private final OrderRepo orderRepo;

    //Constructor for orderRepo
    @Autowired
    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
    }

    //Get ALL orders
    public List<Order> getAllOrders(){
        return orderRepo.findAll();
    }

}
