package com.example.jpa.repository.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery("select " +
                "new com.example.jpa.repository.order.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                "from Order o " +
                "join o.member m " +
                "join o.delivery d ", OrderSimpleQueryDto.class
        ).getResultList();
    }

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();
        result.forEach(o-> {
            List<OrderItemQueryDto> orderItems = findOrerItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderItemQueryDto> findOrerItems(Long orderId) {
        return em.createQuery("select " +
                        "new com.example.jpa.repository.order.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                        "from OrderItem oi " +
                        "join oi.item i " +
                        "where oi.order.id = :orderId", OrderItemQueryDto.class ).setParameter("orderId", orderId).getResultList();
    }

    public List<OrderQueryDto> findOrders() {
        return em.createQuery("select " +
                "new com.example.jpa.repository.order.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                "from Order o " +
                "join o.member m " +
                "join o.delivery d", OrderQueryDto.class).getResultList();
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();
        List<Long> orderIds = result.stream().map(o-> o.getOrderId()).collect(toList());
        Map<Long, List<OrderItemQueryDto>> orderItemMap = getOrderItemMap(orderIds);
        result.forEach(o->o.setOrderItems(orderItemMap.get(o.getOrderId())));
        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> getOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery("select " +
                "new com.example.jpa.repository.order.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                "from OrderItem oi " +
                "join oi.item i " +
                "where oi.order.id in :orderIds", OrderItemQueryDto.class).setParameter("orderIds", orderIds).getResultList();
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
        return orderItemMap;
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery("select " +
                "new com.example.jpa.repository.order.OrderFlatDto(o.id, m.name" +
                                                                    ", o.orderDate, o.status, d.address" +
                                                                    ", i.name, oi.orderPrice, oi.count)" +

                " from Order o" +
                " join o.member m" +
                " join o.delivery d" +
                " join o.orderItems oi" +
                " join oi.item i", OrderFlatDto.class).getResultList();
    }
}
