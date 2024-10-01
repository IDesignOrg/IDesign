package com.my.interrior.admin.page.adminDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaqDTO {

	private Long faqNo;
    private String faqTitle;
    private String faqCategory;
    private String faqContent;
}
