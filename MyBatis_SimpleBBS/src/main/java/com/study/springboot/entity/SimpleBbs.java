package com.study.springboot.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class SimpleBbs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String writer;
    private String title;
    private String content;
}
