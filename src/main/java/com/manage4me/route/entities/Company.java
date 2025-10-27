package com.manage4me.route.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.manage4me.route.commons.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "app_company")
public class Company extends Auditable<String> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private String name;

    private String email;

    private String phone;

    private String address;

    @Column(name = "ducument_resgistration")
    private String documentRegistration;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ToString.Exclude
    @Column(columnDefinition = "MEDIUMBLOB")
    @Lob
    private byte[] logo;

    @ToString.Exclude
    @Column(columnDefinition = "MEDIUMBLOB")
    @Lob
    private byte[] logoRepeat;

    @Transient
    private Purchase purchase;
}
