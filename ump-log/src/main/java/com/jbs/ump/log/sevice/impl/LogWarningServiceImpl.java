package com.jbs.ump.log.sevice.impl;

import com.alibaba.fastjson.JSONObject;
import com.jbs.ump.log.entity.DingDingMsg;
import com.jbs.ump.log.enums.DingDingSendUrlEnum;
import com.jbs.ump.log.sevice.DingDingService;
import com.jbs.ump.log.sevice.LogWarningService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @created by wjf
 * @date 2019/12/25 14:04
 * @description:
 */
@Service
public class LogWarningServiceImpl implements LogWarningService {

    private String phone = "15191597187,15510786966";



    @Resource
    private DingDingService dingDingService;

    @Resource
    private Map<Integer,String> dingdingSendUrlMap;

    @Override
    public void analysizeLog(String msg) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String message = jsonObject.getString("message");
        if (StringUtils.isEmpty(message)) {
            return;
        }
        if(message.contains("Error") || message.contains("Exception")){
            this.senMsg(message);
        }

    }

    public void senMsg(List<String> msgsplit) throws Exception {
       String msg =  msgsplit.get(0)+" "+msgsplit.get(1);
        dingDingService.sendMsg(new DingDingMsg().setMsg("测试不写 baojing ："+msg).setServerUrl(
                dingdingSendUrlMap.get(DingDingSendUrlEnum.WARN.getCode())
        ).setTelephone(phone));
    }
    private void senMsg(String msg) throws Exception {
        dingDingService.sendMsg(new DingDingMsg().setMsg("测试不写 baojing："+msg).setServerUrl(
                dingdingSendUrlMap.get(DingDingSendUrlEnum.WARN.getCode())
        )/*.setTelephone(phone)*/);
    }


}
