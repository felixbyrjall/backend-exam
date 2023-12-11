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

        if(!customerRepo.existsById(orderDTO.getCustomerId())) {
            return null;
        }
        Customer customer = customerRepo.findById(orderDTO.getCustomerId()).orElse(null);
        newOrder.setCustomer(customer);

        if(!addressRepo.existsById(orderDTO.getAddressId())) {
            return null;
        }
        Address address = addressRepo.findById(orderDTO.getAddressId()).orElse(null);
        newOrder.setAddress(address);

        List<Long> machineIds = orderDTO.getMachineId();
        if(!machineIds.stream().allMatch(machineRepo::existsById)) {
            return null;
        }
        newOrder.setMachines(machineRepo.findAllById(machineIds));

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

        if(existingOrder != null) {

            if(orderDTO.getCustomerId() != null) {
                Customer customer = customerRepo.findById(orderDTO.getCustomerId()).orElse(null);
                existingOrder.setCustomer(customer);
            } else {
                return null;
            }

            if(orderDTO.getAddressId() != null) {
                Address address = addressRepo.findById(orderDTO.getAddressId()).orElse(null);
                existingOrder.setAddress(address);
            } else {
                return null;
            }

            if(!orderDTO.getMachineId().isEmpty()) {
                List<Machine> machine = machineRepo.findAllById(orderDTO.getMachineId());
                existingOrder.setMachines(machine);
            } else {
                return null;
            }

            return orderRepo.save(existingOrder);

        } else {
            return null;
        }
    }
}
