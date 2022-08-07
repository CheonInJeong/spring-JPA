package com.example.jpa.repository;

import com.example.jpa.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;

@Getter @Setter
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;
}
