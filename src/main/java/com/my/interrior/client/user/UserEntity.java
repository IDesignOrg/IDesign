package com.my.interrior.client.user;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Entity(name = "user")
@Getter
@Setter
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("UNo")
    @Column(name = "u_no")
    private Long UNo;

    @JsonProperty("UId")
    @Column(nullable = false, unique = true, name = "u_id")
    private String UId;

    @JsonProperty("UPw")
    @Column(nullable = false, name = "u_pw")
    private String UPw;

    @JsonProperty("UName")
    @Column(nullable = false, name = "u_name")
    private String UName;

    @JsonProperty("UMail")
    @Column(nullable = false, name = "u_mail")
    private String UMail;

    @JsonProperty("UBirth")
    @Column(nullable = false, name = "u_birth")
    private String UBirth;

    @JsonProperty("UTel")
    @Column(nullable = false, name = "u_tel")
    private String UTel;

    @JsonProperty("URegister")
    @Column(nullable = false, name = "u_register")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate URegister;
}
