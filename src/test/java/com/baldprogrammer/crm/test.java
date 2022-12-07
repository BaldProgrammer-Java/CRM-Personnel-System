package com.baldprogrammer.crm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baldprogrammer.crm.model.TreeModel;
import com.baldprogrammer.crm.service.ModuleService;
import org.apache.naming.TransactionRef;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @PROJECT_NAME: CRM-Personnel-System
 * @DESCRIPTION:
 * @USER: baldprogrammer
 * @DATE: 2022/12/6 1:03 PM
 */
@SpringBootTest
public class test {

    @Autowired
    private ModuleService moduleService;


    @Test
    void queryAllModules() {
        List<TreeModel> treeModelList = moduleService.queryAllModules();
        String jsontest = JSON.toJSONString(treeModelList);
        System.out.println(jsontest);
    }
}
