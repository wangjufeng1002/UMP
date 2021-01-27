package com.jbs.ump.log.sevice;


import com.jbs.ump.log.entity.DingDingMsg;

public interface DingDingService {
    /**
     *钉钉消息通知
     */
    void  sendMsg(DingDingMsg dingDingMsg) throws Exception;
}
