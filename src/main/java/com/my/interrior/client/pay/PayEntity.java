package com.my.interrior.client.pay;

import jakarta.persistence.*;

@Entity(name = "pay")
public class PayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "p_no")
    private Long PNo;

    @Column(name = "approval_url")
    private String ApprovalUrl;

    @Column(name = "cid")
    private String CId;

    @Column(name = "cancel_url")
    private String CancelUrl;

    @Column(name = "fail_url")
    private String FailUrl;

    @Column(name = "item_name")
    private String ItemName;

    @Column(name = "partner_order_id")
    private String PartnerOrderId;

    @Column(name = "partner_user_id")
    private String PartnerUserId;

    @Column(name = "quantity")
    private String Quantity;

    @Column(name = "tax_free_amount")
    private String TaxFreeAmount;

    @Column(name = "total_amount")
    private String TotalAmount;
}
