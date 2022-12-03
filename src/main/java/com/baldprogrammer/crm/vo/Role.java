package com.baldprogrammer.crm.vo;

import lombok.Data;

import java.util.Date;

@Data
public class Role {

    private String id;

    private Short status;

    private Date gmtCreate;

    private Date gmtModified;

}