package com.baldprogrammer.crm.vo;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id;

    private String userName;

    private String userPwd;

    private String trueName;

    private String email;

    private String phone;

    private Integer isValid;

    private Date createDate;

    private Date updateDate;

}