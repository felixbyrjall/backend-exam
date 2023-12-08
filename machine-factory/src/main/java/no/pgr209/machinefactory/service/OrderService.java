package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
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

    //Update an order
    public ResponseEntity<Order> updateOrder(Long id, Order updatedOrder) {
        Order existingOrder = orderRepo.findById(id).orElse(null);

        if(existingOrder != null) {

            existingOrder.setOrderDate(updatedOrder.getOrderDate());
            return new ResponseEntity<>(orderRepo.save(existingOrder), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
