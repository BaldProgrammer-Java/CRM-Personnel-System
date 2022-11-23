package com.baldprogrammer.crm.query;

import com.baldprogrammer.crm.base.BaseQuery;
import lombok.Data;

/**
 * @PROJECT_NAME: CRM
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/11/22 4:11 PM
 */
@Data
public class UserQuery extends BaseQuery {
    private String userName;
    private String email;
    private String phone;
}
