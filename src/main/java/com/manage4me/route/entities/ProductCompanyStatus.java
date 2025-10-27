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
@Entity(name = "app_product_company_status")
public class ProductCompanyStatus extends Auditable<String> {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne
    @JoinColumn(name = "company_product_id")
    private CompanyProducts companyProducts;

    @ManyToOne
    @JoinColumn(name = "product_action_id")
    private ProductAction productAction;

    @Column(name = "count_used")
    private int countUsed;

    public boolean isValid() {
        return countUsed < productAction.getAllowedValue() || productAction.getAllowedValue() == 0;
    }

}
