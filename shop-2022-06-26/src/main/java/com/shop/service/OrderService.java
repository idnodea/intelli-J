package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.entity.*;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.shop.dto.OrderHistDto;
import com.shop.dto.OrderItemDto;
import com.shop.repository.ItemImgRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import org.thymeleaf.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;

    private final OrderRepository orderRepository;

    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email){

        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    //page315 주문목록을 조회하는 로직구현
    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {

        List<Order> orders = orderRepository.findOrders(email, pageable);
        //유저의 아이디와 페이징 조건을 이용하여 주문목록을 조회
        Long totalCount = orderRepository.countOrder(email);
        //유저의 주문 총 개수를 구함

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for (Order order : orders) {
        //주문 리스트를 순회하면서 구매 이력 페이지에 전달할 DTO를 생성
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn
                        (orderItem.getItem().getId(), "Y");
        //주문한 상품의 대표 이미지를 조회
                OrderItemDto orderItemDto =
                        new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }

            orderHistDtos.add(orderHistDto);
        }

        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
        //페이지 구현 객체를 생성하여 반환
    }

    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        //현재 로그인한 사용자와 주문 데이터를 생성한 사용자가 같은지 검사
        //같을 때는 true를 반환하고 ,같지 않다면 false를 반환

        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }

        return true;
    }

    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
        //주문 취소상태로 변경하면 변경감지기능에 의해서 트랜잭션이 끝날 때
        //update쿼리가 실행됨
    }

    public Long orders(List<OrderDto> orderDtoList, String email){
    //책에선 포문이 아닌데, 포문을 통해 Item객체를 포문 안으로 
    //page320에서 for문 설정    
    //page362 장바구니에서 주문할 상품 데이터를 전달받아서 주문을 생성하는 로직을 만듬
        
        Member member = memberRepository.findByEmail(email); 
        List<OrderItem> orderItemList = new ArrayList<>();
        //현재 로그인한 회원의 이메일 정보를 이용해 회원정보를 조회
        
        for (OrderDto orderDto : orderDtoList) {
            //page363 주문할 상품리스트를 만듦

            Item item = itemRepository.findById(orderDto.getItemId())
                    .orElseThrow(EntityNotFoundException::new);
            //주문할 상품을 조회

            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            //주문할 상품엔티티와 주문수량을 이용해서 주문상품엔티티를 생성
            orderItemList.add(orderItem);
        }

        Order order = Order.createOrder(member, orderItemList); 
        //회원정보와 주문할 상품리스트정보를 이용해 주문 엔티티를 생성
        //page363 현재 로그인한 회원과 주문상품목록을 이용해 주문 엔티티를 만듬

        orderRepository.save(order); //생성한 주문엔티티를 저장

        return order.getId();
    }

}