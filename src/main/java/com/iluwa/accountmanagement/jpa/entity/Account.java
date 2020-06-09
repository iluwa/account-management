package com.iluwa.accountmanagement.jpa.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "accounts", schema = "accounts")
@SequenceGenerator(name = "generator", schema = "accounts", sequenceName = "accounts_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Account extends BaseEntity {

    @Column(name = "num")
    private String accountNum;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "currency")
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
}
