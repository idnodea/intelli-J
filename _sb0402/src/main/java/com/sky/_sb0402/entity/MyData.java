package com.sky._sb0402.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
//@Builder(toBuilder = true) //기존필드의 일부만
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MyData {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String mail;
    private Integer age;
    private String memo;
}
