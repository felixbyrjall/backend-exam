package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.model.OrderDTO;
import no.pgr209.machinefactory.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    //Get all orders
    @GetMapping()
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    //Get orders by page
    @GetMapping("/page/{pageNr}")
    public ResponseEntity<List<Order>> getOrdersByPage(@PathVariable int pageNr) {
        return orderService.getOrdersByPage(pageNr);
    }

    //Get order by id
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    //Create an order
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.createOrder(orderDTO);
    }

    //Delete order by id
    @DeleteMapping("/{id}")
    public void deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        return orderService.updateOrder(id, orderDTO);
    }
}
