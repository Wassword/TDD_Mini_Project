package org.example.tdd_mini_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.tdd_mini_project.controller.OrderController;
import org.example.tdd_mini_project.model.Order;
import org.example.tdd_mini_project.ordernotfoundexception.OrderNotFoundException;
import org.example.tdd_mini_project.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Specify only WebMvcTest to load the OrderController class
@WebMvcTest(controllers = OrderController.class)
@ExtendWith(MockitoExtension.class)  // Use MockitoExtension for JUnit 5
@Import(OrderService.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc simulates HTTP requests to the controller

    @MockBean
    private OrderService orderService;  // Mock the OrderService to simulate service layer interactions

    private Order mockOrder;  // A mock Order object to use in tests

    @BeforeEach
    void setUp() {
        // This method runs before each test. Initializes the mockOrder object with sample data.
        mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setCustomerName("John Doe");
        mockOrder.setOrderDate(LocalDate.now());
        mockOrder.setShippingAddress("123 Main St");
        mockOrder.setTotal(100.00);
    }

    @Test
    public void testCreateOrder() throws Exception {
        // Set up a sample order
        Order order = new Order();
        order.setCustomerName("Jane Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("456 Elm St");
        order.setTotal(200.0);

        // Mock the behavior of the orderService to return the order when the createOrder method is called
        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        // Use MockMvc to simulate a POST request to "/orders"
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Jane Doe"));
    }

    @Test
    public void testGetOrderById() throws Exception {
        // Mock the behavior of the orderService to return the mock order when called
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(mockOrder));

        // Use MockMvc to simulate a GET request to "/orders/1"
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("John Doe"));
    }

    @Test
    public void testGetOrderById_NotFound() throws Exception {
        // Mock the behavior of the orderService to return empty when the order is not found
        when(orderService.getOrderById(1L)).thenReturn(Optional.empty());

        // Use MockMvc to simulate a GET request to "/orders/1"
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateOrder() throws Exception {
        // Set up an updated order
        Order updatedOrder = new Order();
        updatedOrder.setCustomerName("Jane Doe");
        updatedOrder.setOrderDate(LocalDate.now());
        updatedOrder.setShippingAddress("456 Oak St");
        updatedOrder.setTotal(150.00);
        updatedOrder.setId(1L);  // Set the same ID as the order being updated

        // Mock the behavior of the orderService to return the updated order
        when(orderService.updateOrder(eq(1L), any(Order.class))).thenReturn(updatedOrder);

        // Use MockMvc to simulate a PUT request to "/orders/1" with the updated order
        mockMvc.perform(put("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(updatedOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Jane Doe"));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        // Mock the behavior of the orderService to do nothing when deleteOrder is called
        doNothing().when(orderService).deleteOrder(1L);

        // Use MockMvc to simulate a DELETE request to "/orders/1"
        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNoContent());
    }

    @org.junit.Test
    @Test
    public void testDeleteOrder_NotFound() throws Exception {
        // Properly mock the behavior to throw a custom OrderNotFoundException when deleting a non-existent order
        doThrow(new OrderNotFoundException("Order not found")).when(orderService).deleteOrder(anyLong());

        mockMvc.perform(delete("/orders/999"))
                .andExpect(status().isNotFound());
    }


}
