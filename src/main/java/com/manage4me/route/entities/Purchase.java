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
@Entity(name = "app_purchase")
public class Purchase extends Auditable<String> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "class_name")
    private String className;

    @Column(name = "transaction_id")
    private String transactionId;
    private String platform;

    private String state;

    @Column(name = "purchase_id")
    private String purchaseId;

    @Column(name = "purchase_token", length = 1000)
    private String purchaseToken;

    @Column(name = "purchase_date")
    private String purchaseDate;

    @Column(name = "signature", length = 1000)
    private String signature;

    @Column(name = "receipt", length = 1000)
    private String receipt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "company_id")
    private String companyId;

    @Column(name = "statues_reason")
    private String statuesReason;
}
