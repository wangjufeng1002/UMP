package com.jbs.ump.log.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DingDingMsg {
    /**
     * 钉钉群消息机器人地址
     */
    private String serverUrl;
    /**
     *推送内容
     */
    private String msg;
    /**
     * 需要@人的电话
     */
    private String telephone;

}
