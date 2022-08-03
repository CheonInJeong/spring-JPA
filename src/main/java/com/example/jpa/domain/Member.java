package com.example.jpa.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
public class Member {
    @Id @GeneratedValue
    @Column(name="member_id")
    private Long id;
    private String name;
    @Embedded //내장타입
    private Address address;
    //주인의 memeber 필드에 의해 매핑이됨. 읽기전용. 여기에 값을 셋팅해도 값이 변경 되지 않음. orders 테이블의 member의 값을 변경 해야 변경이 됨.
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
