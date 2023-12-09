package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.model.OrderDTO;
import no.pgr209.machinefactory.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> allOrders = orderService.getAllOrders();

        if (!allOrders.isEmpty()){
            return new ResponseEntity<>(allOrders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //Get orders by page
    @GetMapping("/page/{pageNr}")
    public ResponseEntity<List<Order>> getOrdersByPage(@PathVariable int pageNr) {
        List<Order> ordersByPage = orderService.getOrdersByPage(pageNr);

        if(!ordersByPage.isEmpty()) {
            return new ResponseEntity<>(ordersByPage, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //Get order by id
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order orderById = orderService.getOrderById(id);

        if(!(orderById == null)){
            return new ResponseEntity<>(orderById, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
