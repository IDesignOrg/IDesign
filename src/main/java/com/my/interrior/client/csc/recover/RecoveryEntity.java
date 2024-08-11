package com.my.interrior.client.csc.recover;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.my.interrior.client.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity(name = "recovery")
@Getter
@Setter
public class RecoveryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("RecoverNo")
    @Column(name = "Recover_no")
    private Long RecoverNo;

    @ManyToOne
    @JoinColumn(name = "u_no", nullable = false)
    private UserEntity user;

    @JsonProperty("requestDate")
    @Column(nullable = false, name = "request_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate requestDate;

    @JsonProperty("reason")
    @Column(nullable = false, length = 1000)
    private String reason;

    @JsonProperty("status")
    @Column(nullable = false)
    private String status = "PENDING";

    @JsonProperty("processedDate")
    @Column(name = "processed_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate processedDate;
}
