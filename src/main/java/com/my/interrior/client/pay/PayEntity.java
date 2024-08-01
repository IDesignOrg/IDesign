package com.my.interrior.client.pay;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "payment")
@Getter
@Setter
@ToString
public class PayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payNo;
    private String cardName;
    private String name;
    private BigDecimal paidAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate paidAt;
    private String payMethod;
    private String status;
    private boolean success;
    @Column(name = "merchant_uid")
    private String merchantUId;
}
