package com.incident.management;


import cn.hutool.core.util.StrUtil;
import com.incident.management.domain.Incident;
import com.incident.management.service.IncidentService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class IncidentServiceTest {

    @Autowired
    IncidentService incidentService;

    //测试事件添加
    @Test
    void testAddIncident(){
        Incident incident = new Incident();
        //检验非空
        incident.setDescription("事件");
        String s1 = incidentService.save(incident);
        // 断言预期结果的正确性。
        /**
         * 参数一：测试失败的提示信息。
         * 参数二：期望值。
         * 参数三：实际值
         */
        // public static void assertEquals(String message, Object expected, Object actual)
        Assert.assertEquals("添加参数有错误，请检查！","success",s1);
        //检验编号长度不小于4位
        incident.setCode("tes");
        incident.setName("inc1");
        incident.setDescription("事件");
        incident.setIsCache("0");
        String s2 = incidentService.save(incident);
        Assert.assertEquals("添加参数有错误，请检查！","success",s2);
        //检验是否插入成功
        incident.setCode("test");
        incident.setName("inc1");
        incident.setDescription("事件");
        incident.setIsCache("0");
        String s3 = incidentService.save(incident);
        Assert.assertTrue(StrUtil.equals("success",s3));
        //检验事件编码是否已存在
        Incident incident2 = new Incident();
        incident2.setCode("test");
        incident2.setName("inc1");
        incident2.setDescription("事件");
        incident2.setIsCache("0");
        String s4 = incidentService.save(incident2);
        Assert.assertEquals("添加参数有错误，请检查!","success",s4);
    }

}
