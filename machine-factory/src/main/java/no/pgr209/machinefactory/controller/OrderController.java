package no.pgr209.machinefactory.controller;

import no.pgr209.machinefactory.model.Order;
import no.pgr209.machinefactory.model.OrderDTO;
import no.pgr209.machinefactory.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> allOrders = orderService.getAllOrders();

        if (!allOrders.isEmpty()){
            return new ResponseEntity<>(allOrders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/page/{pageNr}")
    public ResponseEntity<List<Order>> getOrdersByPage(@PathVariable int pageNr) {
        List<Order> ordersByPage = orderService.getOrdersByPage(pageNr);

        if(!ordersByPage.isEmpty()) {
            return new ResponseEntity<>(ordersByPage, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order orderById = orderService.getOrderById(id);

        if(orderById != null) {
            return new ResponseEntity<>(orderById, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO) {
        Order createdOrder = orderService.createOrder(orderDTO);

        if(createdOrder != null && !createdOrder.getMachines().isEmpty()) {
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } else {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.set("Error", "One or more IDs not found");
            return new ResponseEntity<>(responseHeaders, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderById(@PathVariable Long id) {
        if (orderService.orderExists(id)) {
            orderService.deleteOrderById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        Order updatedOrder = orderService.updateOrder(id, orderDTO);

        if (updatedOrder != null && updatedOrder.getCustomer() != null && !updatedOrder.getMachines().isEmpty() && updatedOrder.getAddress() != null){
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
