package no.pgr209.machinefactory.service;

import no.pgr209.machinefactory.model.*;
import no.pgr209.machinefactory.repo.AddressRepo;
import no.pgr209.machinefactory.repo.CustomerRepo;
import no.pgr209.machinefactory.repo.MachineRepo;
import no.pgr209.machinefactory.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
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
        return (orderRepo.findById(id).orElse(null));
    }

    //Create an order
    public Order createOrder(OrderDTO orderDTO) {
        Order newOrder = new Order(LocalDateTime.now());

        if (orderDTO.getCustomerId() == null || orderDTO.getAddressId() == null ||
                orderDTO.getMachineId() == null || !orderDTO.getMachineId().stream().allMatch(machineRepo::existsById) ||
                !customerRepo.existsById(orderDTO.getCustomerId()) || !addressRepo.existsById(orderDTO.getAddressId())) {
            return null;
        }

        newOrder.setCustomer(customerRepo.findById(orderDTO.getCustomerId()).orElse(null));
        newOrder.setAddress(addressRepo.findById(orderDTO.getAddressId()).orElse(null));
        newOrder.setMachines(machineRepo.findAllById(orderDTO.getMachineId()));

        return orderRepo.save(newOrder);
    }

    //Delete an order
    public void deleteOrderById(Long id) {
        orderRepo.deleteById(id);
    }

    //Check if an order exists
    public boolean orderExists(Long id) {
        return orderRepo.existsById(id);
    }

    //Update an order
    public Order updateOrder(Long id, OrderDTO orderDTO) {
        Order existingOrder = orderRepo.findById(id).orElse(null);

        if (existingOrder == null || orderDTO.getCustomerId() == null ||
                orderDTO.getAddressId() == null || orderDTO.getMachineId() == null) {
            return null;
        }

        existingOrder.setCustomer(customerRepo.findById(orderDTO.getCustomerId()).orElse(null));
        existingOrder.setAddress(addressRepo.findById(orderDTO.getAddressId()).orElse(null));

        List<Long> machineIds = orderDTO.getMachineId();

        if (machineIds.isEmpty()) {
            existingOrder.setMachines(Collections.emptyList());
            return orderRepo.save(existingOrder);
        }

        List<Machine> machines = machineRepo.findAllById(machineIds);

        if (machines.size() != machineIds.size()) {
            return null;
        }

        existingOrder.setMachines(machines);
        return orderRepo.save(existingOrder);
    }
}
