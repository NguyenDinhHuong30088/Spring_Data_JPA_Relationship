package com.example.demo11.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/orders")
public class OrderApi {
    @Autowired
    OrderService orderService;

    @RequestMapping(method = RequestMethod.POST)
    public Order save(@RequestBody Order order){
        return orderService.save(order);
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<Order> findAll(){
        return orderService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public Order getDetail(@PathVariable int id) {
        return orderService.findById(id).get();
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public boolean delete(@PathVariable int id) {
        orderService.deleteById(id);
        return true;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public Order update(@PathVariable int id, @RequestBody Order updateOrder) {
        Order existing = orderService.findById(id).get();
        existing.setCustomerId(updateOrder.getCustomerId());
        existing.setTotalPrice(updateOrder.getTotalPrice());
        existing.setShipName(updateOrder.getShipName());
        existing.setShipAddress(updateOrder.getShipAddress());
        existing.setShipPhone(updateOrder.getShipPhone());
        existing.setStatus(updateOrder.getStatus());
        orderService.save(existing);
        return updateOrder;
    }
}
