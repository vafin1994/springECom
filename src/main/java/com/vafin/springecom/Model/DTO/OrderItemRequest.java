package com.vafin.springecom.Model.DTO;

public record OrderItemRequest(
        int productId,
        int quantity
) {
}
