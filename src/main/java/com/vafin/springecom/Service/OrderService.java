package com.vafin.springecom.Service;

import com.vafin.springecom.Model.DTO.OrderRequest;
import com.vafin.springecom.Model.DTO.OrderResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {


    public OrderResponse placeOrder(OrderRequest orderRequest) {
        return null;
    }

    public List<OrderResponse> getAllOrderResponses() {
        List<OrderResponse> orders = new ArrayList<>();
        return orders;
    }
}
