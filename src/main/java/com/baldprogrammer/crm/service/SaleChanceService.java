package com.baldprogrammer.crm.service;

import com.baldprogrammer.crm.base.BaseService;
import com.baldprogrammer.crm.dao.SaleChanceMapper;
import com.baldprogrammer.crm.vo.SaleChance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @PROJECT_NAME: CRM
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/10/27 3:28 PM
 */
@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    @Autowired
    private SaleChanceMapper saleChanceMapper;


}
