package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return orderService.getAllOrders(Integer.MAX_VALUE);
    }

    //Get orders by page
    @GetMapping("/page/{pageNr}")
    public List<Order> getOrdersByPage(@PathVariable int pageNr) {
        return orderService.getOrdersByPage(pageNr);
    }

    //Get order by id
    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    //Create an order
    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    //Delete order by id
    @DeleteMapping("/{id}")
    public void deleteOrderById(@PathVariable Long id) {
        orderService.deleteOrderById(id);
    }
}
