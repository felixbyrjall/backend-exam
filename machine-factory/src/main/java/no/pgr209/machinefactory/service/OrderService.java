package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Customer;
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
    public List<Order> getOrders() {
        return orderRepo.findAll();
    }

    //Get order by specific id
    public Order getOrderById(Long id) {
        return orderRepo.findById(id).orElse(null);
    }

    //Create an order
    public Order createOrder(Order order) {
        return orderRepo.save(order);
    }

    //Delete an order
    public void deleteOrder(Long id) {
        orderRepo.deleteById(id);
    }
}
