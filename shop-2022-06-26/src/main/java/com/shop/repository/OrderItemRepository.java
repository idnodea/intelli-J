package com.shop.repository;

import com.shop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

//page 213 지연로딩
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}