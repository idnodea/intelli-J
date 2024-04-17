package com.shop.constant;

import javax.persistence.Enumerated;//p152  고유한 상수이므로 이넘으로
public enum Role {
    USER, ADMIN
    //GUEST를 나중에 //어차피 시큐리티를 통해 접근권한을 나눴기 때문에, 유저 어드민으로도 충분하긴 함
}
//
//JPA에서 Enum 타입은 @Enumerated 애너테이션을 사용하여 데이터베이스에 저장할 수 있습니다.
// @Enumerated 애너테이션은 EnumType.ORDINAL 또는 EnumType.STRING 값을 받아서
// Enum을 어떻게 저장할지 결정합니다:
//
//EnumType.ORDINAL: Enum 상수의 순서를 데이터베이스에 정수 값으로 저장합니다.
// 즉, USER는 0, ADMIN은 1, GUEST는 2로 저장됩니다.
// 이 방식의 문제점은 Enum 상수의 순서를 변경하거나 새로운 상수를 중간에 추가하는 경우,
// 데이터베이스에 저장된 값과 Enum 상수가 더 이상 일치하지 않게 되어
// 데이터의 무결성이 깨질 수 있다는 것입니다.

// EnumType.STRING: Enum 상수의 이름을 데이터베이스에 문자열로 저장합니다.
// 즉, USER는 "USER", ADMIN은 "ADMIN", GUEST는 "GUEST"로 저장됩니다.
// 이 방식은 Enum 상수의 이름이 바뀌지 않는 한 순서가 변경되거나 새로운 상수가 추가되더라도
// 문제가 발생하지 않습니다.
//따라서 EnumType.STRING으로 설정하면, Enum의 순서를 변경하거나 상수를 추가/삭제하는 경우에도
// 데이터베이스의 값과 일치하는 문자열로 저장되기 때문에, 순서가 중요하지 않아집니다.
// 이는 NullTypeException과 같은 오류를 방지하진 않지만,
// Enum 상수의 순서 변경에 따른 잘못된 데이터 연결 문제는 막을 수 있습니다.
// 열거형 상수의 이름 자체가 변경되면, 데이터베이스에 저장된 문자열과 더 이상 일치하지 않기 때문에,
// 이런 상황을 관리하기 위해서는 추가적인 데이터 마이그레이션 작업이 필요할 수 있습니다.