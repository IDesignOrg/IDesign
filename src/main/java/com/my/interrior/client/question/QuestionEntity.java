package com.my.interrior.client.question;

import jakarta.persistence.*;

@Entity(name = "question")
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "q_no")
    private Long QNo;

    @Column(nullable = false, columnDefinition = "TEXT", name = "q_content")
    private String QContent;

    @Column(nullable = false, name="q_title")
    private String QTitle;

    @Column(nullable = false, name="q_category")
    private String QCategory;
}
