package org.example.tdd_mini_project.service;

// Import the new exception class

import org.example.tdd_mini_project.model.Order;
import org.example.tdd_mini_project.ordernotfoundexception.OrderNotFoundException;
import org.example.tdd_mini_project.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrder(Long id, Order orderDetails) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));  // Throw custom exception
        order.setCustomerName(orderDetails.getCustomerName());
        order.setOrderDate(orderDetails.getOrderDate());
        order.setShippingAddress(orderDetails.getShippingAddress());
        order.setTotal(orderDetails.getTotal());
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));  // Throw custom exception
        orderRepository.delete(order);
    }
}
