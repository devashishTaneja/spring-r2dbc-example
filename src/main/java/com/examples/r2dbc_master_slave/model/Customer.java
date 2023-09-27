package com.examples.r2dbc_master_slave.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Setter
@Getter
@AllArgsConstructor
public class Customer {
    @Id
    private Long id;
    @Column(value = "first_name")
    private String firstName;
    @Column(value = "last_name")
    private String lastName;

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }
}