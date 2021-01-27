package com.jbs.ump.unit;

import org.junit.Test;

/**
 * @created by wjf
 * @date 2019/12/25 14:32
 * @description:
 */

public class StringTest {

    @Test
    public void test() {
        String str = "\tat com.jbs.hermes.module.stockIn.service.impl.CheckLogisticsNoServiceImpl.checkExcel(CheckLogisticsNoServiceImpl.java:83)";
        System.out.println(str.length());
    }

    @Test
    public void testStrSplit() {
        String msg = "2019-12-26 10:47:50.739  INFO 9608 --- [io-10007-exec-6] &&com.jbs.hermes.module.bank.flow.service.impl.BankFlowServiceImpl&&1965103b-a554-4c49-9b12-3f5ae4198c26&&1577328470739&&start";
        int ump = msg.indexOf("ump");
        System.out.println(ump);

        String substring = msg.substring(57);
        System.out.println(substring);
    }
}
