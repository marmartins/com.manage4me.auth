package com.manage4me.route.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.manage4me.route.commons.ENTITY;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "app_product_action")
public class ProductAction implements ENTITY<String> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Enumerated(EnumType.STRING)
    private AppAction action;

    @Column(name = "allowed_value")
    private int allowedValue;

    private boolean renewable;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Transient
    private int countUsed;
}
