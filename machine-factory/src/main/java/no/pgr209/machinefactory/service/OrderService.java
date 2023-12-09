package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import no.pgr209.machinefactory.repo.MachineRepo;
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
    private final CustomerRepo customerRepo;
    private final AddressRepo addressRepo;
    private final MachineRepo machineRepo;

    //Constructor for orderRepo
    @Autowired
    public OrderService(OrderRepo orderRepo, CustomerRepo customerRepo, AddressRepo addressRepo, MachineRepo machineRepo) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.addressRepo = addressRepo;
        this.machineRepo = machineRepo;
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
    public ResponseEntity<Order> updateOrder(Long id, UpdateOrderDTO updateOrderDTO) {
        Order existingOrder = orderRepo.findById(id).orElse(null);

        if(existingOrder != null) {

            if(updateOrderDTO.getCustomerId() != null) {
                Customer customer = customerRepo.findById(updateOrderDTO.getCustomerId()).orElse(null);
                existingOrder.setCustomer(customer);
            }

            if(updateOrderDTO.getAddressId() != null) {
                Address address = addressRepo.findById(updateOrderDTO.getAddressId()).orElse(null);
                existingOrder.setAddress(address);
            }

            if(updateOrderDTO.getMachineId() != null) {
                List<Machine> machine = machineRepo.findAllById(updateOrderDTO.getMachineId());
                existingOrder.setMachines(machine);
            }

            return new ResponseEntity<>(orderRepo.save(existingOrder), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
