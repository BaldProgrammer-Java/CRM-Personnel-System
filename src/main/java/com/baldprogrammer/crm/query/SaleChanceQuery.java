package com.baldprogrammer.crm.query;

import com.baldprogrammer.crm.base.BaseQuery;
import lombok.Data;

/**
 * 营销机会查询类
 *
 * @PROJECT_NAME: CRM
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/10/28 8:41 AM
 */
@Data
public class SaleChanceQuery extends BaseQuery {


    private String customerName; //客户名
    private String createMan; //创建人
    private Integer state;  //分配状态  0=未分配  1=已分配
   /* private String devResult;
    private String assignMan;*/

}
