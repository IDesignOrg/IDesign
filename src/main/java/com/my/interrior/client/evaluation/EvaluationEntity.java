package com.my.interrior.client.evaluation;

import com.my.interrior.client.user.UserEntity;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity(name = "evaluation")
public class EvaluationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "e_no")
    private Long ENo;

    @Column(nullable = false, name = "e_rating")
    private String ERating;

    @Column(nullable = false, name = "e_img")
    private String EImg;

    @Column(nullable = false, columnDefinition = "TEXT", name = "e_content")
    private String EContent;

    @Column(nullable = false, name = "e_write_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate EWriteTime;

    @ManyToOne
    @JoinColumn(name = "u_no")
    private UserEntity UNo;

}
