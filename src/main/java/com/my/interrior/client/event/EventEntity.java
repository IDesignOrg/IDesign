package com.my.interrior.client.event;

import jakarta.persistence.*;

@Entity(name = "event")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_no")
    private Long EventNo;

    @Column(nullable = false, name = "event_title")
    private String EventTitle;

    @Column(nullable = false, columnDefinition = "TEXT", name = "event_content")
    private String EventContent;

    @Column(nullable = false, name = "event_img")
    private String EventImg;
}
