package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public Page<Order> getOrders(Pageable pageable) {
        return orderRepo.findAll(pageable);
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
    public void deleteOrderById(Long id) {
        orderRepo.deleteById(id);
    }
}
