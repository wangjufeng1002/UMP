package com.jbs.ump.log.sevice.impl;

import com.alibaba.fastjson.JSONObject;
import com.jbs.ump.log.entity.DingDingMsg;
import com.jbs.ump.log.enums.DingDingSendUrlEnum;
import com.jbs.ump.log.enums.RedisKeyEnum;
import com.jbs.ump.log.sevice.DingDingService;
import com.jbs.ump.log.sevice.LogUmpService;
import com.jbs.ump.log.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @created by wjf
 * @date 2019/12/26 11:55
 * @description:
 */
@Service
public class LogUmpServiceImpl implements LogUmpService {
    private String phone = "15191597187,15510786966";
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private DingDingService dingDingService;
    @Resource
    private Map<Integer, String> dingdingSendUrlMap;

    @Override
    public void analyMethodRunTime(String msg) throws Exception {
        //2019-12-26 10:47:50.739  INFO 9608 --- [io-10007-exec-6] ump&&com.jbs.hermes.module.bank.flow.service.impl.BankFlowServiceImpl&&1965103b-a554-4c49-9b12-3f5ae4198c26&&1577328470739&&start
        //2019-12-26 10:47:50.739  INFO 9608 --- [io-10007-exec-6] ump&&com.jbs.hermes.module.bank.flow.service.impl.BankFlowServiceImpl&&1965103b-a554-4c49-9b12-3f5ae4198c26&&1577328470739&&end
        JSONObject jsonObject = JSONObject.parseObject(msg);
        msg = jsonObject.getString("message");
        int ump = msg.indexOf("ump");
        if (ump < 0) {
            return;
        }
        String time = msg.substring(0, ump);

        String umpMsg = msg.substring(ump);
        //0:ump ,1:类名 2:uuid 3:时间, 4,开始 or 结束
        String[] split = umpMsg.split("&&");
        if (split.length < 4) {
            return;
        }
        if ("exception".equals(split[4])) {
            return;
        }
        String timeStr = redisUtil.redisHashGetValue(RedisKeyEnum.UMP_LOG.getKey(), split[2]);
        if (StringUtils.isEmpty(timeStr)) {
            redisUtil.redisSetHash(RedisKeyEnum.UMP_LOG.getKey(), split[2], split[3]);
        } else {
            long runTime = Long.parseLong(timeStr) - Long.parseLong(split[3]);
            runTime = Math.abs(runTime);
            String[] times = time.split(" ");
            this.senMsg(times[0] +" "+times[1]+ " -- " + split[1] + "--"+"执行时间:"+ runTime);
        }

    }

    private void senMsg(String msg) throws Exception {
        dingDingService.sendMsg(new DingDingMsg().setMsg("ump：" + msg).setServerUrl(
                dingdingSendUrlMap.get(DingDingSendUrlEnum.WARN.getCode())
        ).setTelephone(phone));
    }
}
