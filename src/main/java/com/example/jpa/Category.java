package com.example.jpa;

import com.example.jpa.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {
    @Id @GeneratedValue
    @Column(name="category_id")
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name="category_item", joinColumns = @JoinColumn(name="category_id")
    ,inverseJoinColumns=@JoinColumn(name="item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="parent_id") //자식은 하나의 부모를 가진다.
    private Category parent;

    @OneToMany(mappedBy = "parent") //부모는 여러 자식을 가질 수 있다.
    private List<Category> child = new ArrayList<>();
}
