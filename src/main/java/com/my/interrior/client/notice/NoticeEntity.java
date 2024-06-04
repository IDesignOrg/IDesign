package com.my.interrior.client.notice;

import jakarta.persistence.*;

@Entity(name = "notice")
public class NoticeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "n_no")
    private Long NNo;

    @Column(nullable = false, name = "n_title")
    private String NTitle;

    @Column(nullable = false, columnDefinition = "TEXT", name = "n_content")
    private String NContent;
}
