package org.example.tdd_mini_project;

import org.example.tdd_mini_project.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.example.tdd_mini_project.model.Order;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testSaveOrder() {
        Order order = new Order("John Doe", LocalDate.now(), "123 Street", 250.0);
        Order savedOrder = orderRepository.save(order);

        assertNotNull(savedOrder.getId());
        assertEquals("John Doe", savedOrder.getCustomerName());
    }

    @Test
    public void testFindOrderById() {
        Order order = new Order("John Doe", LocalDate.now(), "123 Street", 250.0);
        orderRepository.save(order);

        Optional<Order> retrievedOrder = orderRepository.findById(order.getId());

        assertTrue(retrievedOrder.isPresent());
        assertEquals("John Doe", retrievedOrder.get().getCustomerName());
    }

    @Test
    public void testFindAllOrders() {
        Order order1 = new Order("John Doe", LocalDate.now(), "123 Street", 250.0);
        Order order2 = new Order("Jane Doe", LocalDate.now(), "456 Avenue", 300.0);
        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> orders = orderRepository.findAll();

        assertEquals(2, orders.size());
    }

    @Test
    public void testDeleteOrder() {
        Order order = new Order("John Doe", LocalDate.now(), "123 Street", 250.0);
        orderRepository.save(order);

        orderRepository.deleteById(order.getId());

        Optional<Order> deletedOrder = orderRepository.findById(order.getId());
        assertFalse(deletedOrder.isPresent());
    }
}
