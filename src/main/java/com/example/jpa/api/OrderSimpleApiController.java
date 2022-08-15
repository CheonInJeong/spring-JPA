package com.example.jpa.api;

import com.example.jpa.domain.Address;
import com.example.jpa.domain.Order;
import com.example.jpa.domain.OrderStatus;
import com.example.jpa.repository.OrderRepository;
import com.example.jpa.repository.OrderSearch;
import com.example.jpa.repository.order.OrderSimpleQueryDto;
import com.example.jpa.repository.order.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress();
        }
        return orders;
    }
    @GetMapping("/api/v2/simple-orders")
    public  List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
       return orders.stream().map(SimpleOrderDto::new).collect(toList());
    }
    @GetMapping("/api/v3/simple-orders")
    public  List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllMemberDelivery();
        return orders.stream().map(SimpleOrderDto::new).collect(toList());
    }

    //V4가 원하는 결과만 가져오기 때문에 성능 최적화 면에서 V3보다는 낫지만, 활용성에서는 V3이 좋다.
    //repository는 entity를 조회하는 용도로 사용해야한다.
    //v4 의 경우 api 스펙이 들어가서 좋지 않음.
    //따라서 화면에 의존성이 있는 경우에는 repository를 분리하는 것이 좋음 -> OrderQueryRepository
    @GetMapping("/api/v4/simple-orders")
    public  List<OrderSimpleQueryDto> ordersV4() {
        List<OrderSimpleQueryDto> orders = orderQueryRepository.findOrderDtos();
        return orders;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
