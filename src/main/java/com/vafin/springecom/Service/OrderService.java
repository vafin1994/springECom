package com.vafin.springecom.Service;

import com.vafin.springecom.Model.DTO.OrderItemRequest;
import com.vafin.springecom.Model.DTO.OrderItemResponse;
import com.vafin.springecom.Model.DTO.OrderRequest;
import com.vafin.springecom.Model.DTO.OrderResponse;
import com.vafin.springecom.Model.Order;
import com.vafin.springecom.Model.OrderItem;
import com.vafin.springecom.Model.Product;
import com.vafin.springecom.Repository.OrderRepository;
import com.vafin.springecom.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setCustomerName(orderRequest.customerName());
        order.setEmail(orderRequest.email());
        order.setStatus("PLACED");
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemReq : orderRequest.items()) {
            Product product = productRepository.findById(itemReq.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            product.setStockQuantity(product.getStockQuantity() - itemReq.quantity());
            productRepository.save(product);

            BigDecimal quantity = new BigDecimal(itemReq.quantity());
            BigDecimal totalPrice = product.getPrice().multiply(quantity);

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.quantity())
                    .totalPrice(product.getPrice().multiply(totalPrice))
                    .order(order)
                    .build();

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);
        List<OrderItemResponse> orderResponseItems = new ArrayList<>();

        for (OrderItem orderItem : savedOrder.getOrderItems()) {
            String productName = orderItem.getProduct().getName();
            int quantity = orderItem.getQuantity();
            BigDecimal totalPrice = orderItem.getTotalPrice();
            OrderItemResponse item = new OrderItemResponse(productName, quantity, totalPrice);
            orderResponseItems.add(item);
        }
        return new OrderResponse(
                order.getOrderId(),
                order.getCustomerName(),
                order.getEmail(),
                order.getStatus(),
                order.getOrderDate(),
                orderResponseItems
        );

    }

    public List<OrderResponse> getAllOrderResponses() {
        List<Order> orders = orderRepository.findAll();
        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orders) {
            List<OrderItemResponse> orderResponseItems = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                String productName = orderItem.getProduct().getName();
                int quantity = orderItem.getQuantity();
                BigDecimal totalPrice = orderItem.getTotalPrice();
                OrderItemResponse item = new OrderItemResponse(productName, quantity, totalPrice);
                orderResponseItems.add(item);
            }

            OrderResponse orderResponse = new OrderResponse(
                    order.getOrderId(),
                    order.getCustomerName(),
                    order.getEmail(),
                    order.getStatus(),
                    order.getOrderDate(),
                    orderResponseItems
            );

            orderResponses.add(orderResponse);
        }
        return orderResponses;
    }
}
