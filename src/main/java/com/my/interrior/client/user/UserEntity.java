package com.my.interrior.client.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Entity(name = "user")
@Getter
@Setter
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "u_no")
    private Long UNo;

    @Column(nullable = false, unique = true, name = "u_id")
    private String UId;

    @Column(nullable = false, name = "u_pw")
    private String UPw;

    @Column(nullable = false, name = "u_name")
    private String UName;

    @Column(nullable = false, name = "u_mail")
    private String UMail;

    @Column(nullable = false, name = "u_birth")
    private String UBirth;

    @Column(nullable = false, name = "u_tel")
    private String UTel;

    @Column(nullable = false, name = "u_register")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate URegister;
}
