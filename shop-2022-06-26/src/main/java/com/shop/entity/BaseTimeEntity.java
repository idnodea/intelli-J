package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//page224 등록일,수정일,등록자,수정자가 모두 들어가는 auditing
//예외적인 Auditing. 

//어디팅을 적용하기 위해 @EntityListener어노테이션추가
@EntityListeners(value = {AuditingEntityListener.class})
//공통매핑정보가 필요할 때 사용하는 어노테이션으로 부모클래스를
//상속받는 자식 클래스에 매핑정보만 제공
@MappedSuperclass
@Getter @Setter
public abstract class BaseTimeEntity {

    @CreatedDate //엔티티가 생성되어 저장될 때 시간을 자동으로 저장
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate //엔티티의 값을 변경할 때 시간을 자동으로 저장
    private LocalDateTime updateTime;

}