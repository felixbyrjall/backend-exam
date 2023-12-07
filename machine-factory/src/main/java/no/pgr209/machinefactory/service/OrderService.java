package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
    public List<Order> getAllOrders(int MAX_SIZE) {
        return orderRepo.findAll(PageRequest.of(0, MAX_SIZE)).stream().toList();
    }

    //Get orders by page
    public List<Order> getOrdersByPage(int pageNr) {
        return orderRepo.findAll(PageRequest.of(pageNr, 10)).stream().toList();
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
