package com.jbs.ump.log.sevice.impl;


import com.alipay.api.internal.util.codec.Base64;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.jbs.ump.log.entity.DingDingMsg;
import com.jbs.ump.log.sevice.DingDingService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
public class DingDingServiceImpl implements DingDingService {
    private final Logger logger = LoggerFactory.getLogger(DingDingService.class);

    private String secret1 = "SECe3b8df20811d55aca3f708c4fac8142506283e4f92b5e15d71302fb81acc6546";

    private String secret2 ="SEC47a9fbb2ce3ffbc527de4bf135cb7d05bf612eab39596d53332b7fda9a577f8d";

    @Override
    public void sendMsg(DingDingMsg dingDingMsg) throws Exception {
        DingTalkClient client = new DefaultDingTalkClient(this.sign(dingDingMsg.getServerUrl()));
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");

        //是否要 @ 人
        if(StringUtils.isNotBlank(dingDingMsg.getTelephone())){
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            at.setAtMobiles(Arrays.asList(dingDingMsg.getTelephone().split(",")));
            request.setAt(at);
        }

        //消息内容
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(dingDingMsg.getMsg());

        request.setText(text);

        try {
            OapiRobotSendResponse response = client.execute(request);
            if (response.isSuccess()) {
                logger.info("钉钉消息通知发送成功");
            } else {
                logger.error("钉钉消息通知发生失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String sign(String url) throws Exception {
        Long timestamp = System.currentTimeMillis();
        String stringToSign = timestamp + "\n" + secret1;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret1.getBytes("UTF-8"), "HmacSHA256"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        String encode = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");
        return url+"&timestamp="+timestamp+"&sign="+encode;
    }

}
