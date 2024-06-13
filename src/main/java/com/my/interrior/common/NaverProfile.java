package com.my.interrior.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NaverProfile {
	private String email;
    private String nickname;
    private String profile_image;
    private String age;
    private String gender;
    private String id;
    private String name;
    private String birthday;
    private String birthyear;
    private String mobile;
    private String mobile_e164;
}
