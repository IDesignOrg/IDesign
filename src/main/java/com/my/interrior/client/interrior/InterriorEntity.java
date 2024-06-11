package com.my.interrior.client.interrior;

import jakarta.persistence.*;

@Entity(name = "interrior")
public class InterriorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i_no")
    private Long INo;

    @Column(nullable = false, name = "i_category")
    private String ICategory;

    @Column(nullable = false, name = "i_title")
    private String ITitle;

    @Column(nullable = false, name = "i_price")
    private Integer IPrice;

    @Column(nullable = false, name="i_total_price")
    private Long ITotalPrice;

    @Column(nullable = false, name="i_quantity")
    private Integer IQuantity;

    @Column(nullable = false, name="i_img")
    private String IImg;

    @Column(nullable = false, columnDefinition = "TEXT", name="i_content")
    private String IContent;
}
