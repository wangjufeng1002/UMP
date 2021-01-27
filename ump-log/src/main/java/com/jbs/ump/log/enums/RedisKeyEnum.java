package com.jbs.ump.log.enums;

/**
 * @created by wjf
 * @date 2019/11/14 15:30
 * @description:
 */
public enum RedisKeyEnum {

    INVOICE_ATTESTATION_KEY("INVOICE_ATTESTATION_KEY", 5, "税票认证防重"),
    WEB_TOKEN("WEB_TOKEN",24*60*60,"webToken"),
    BANK_FLOW_VERIFY_EDITION("BANK_FLOW_VERIFY_EDITION_%s",60,"银行流水校验版本批次"),
    BANK_FLOW_KING_NEW_DATA("BANK_FLOW_KING_NEW_DATA_%s",65,"需要新增的金蝶资料"),
    BANK_FLOW_IMPORT_DATA("BANK_FLOW_IMPORT_DATA_%s",65,"需要保存的银行流水信息"),
    BANK_FLOW_VERIGY_RESULTINFO("BANK_FLOW_VERIGY_RESULTINFO_%s",65,"需要保存的校验结果"),

    INTRA_BRANCH_VERIFY_EDITION("INTRA_BRANCH_VERIFY_EDITION_%s",7200,"银行流水校验版本批次"),
    INTRA_BRANCH_KING_NEW_DATA("INTRA_BRANCH_KING_NEW_DATA_%s",7200,"需要新增的金蝶资料"),
    INTRA_BRANCH_IMPORT_DATA("INTRA_BRANCH_IMPORT_DATA_%s",7200,"需要保存的银行流水信息"),
    INTRA_BRANCH_VERIGY_RESULTINFO("INTRA_BRANCH_VERIGY_RESULTINFO_%s",7200,"需要保存的校验结果"),


    ACCESS_LOG_INFO_KEY("ACCESS_LOG_INFO_KEY",Integer.MAX_VALUE,"日志信息保存"),

    UMP_LOG("UMP_LOG",Integer.MAX_VALUE,"ump 分析 日志")
    ;
    private String key;
    private int expireTime;
    private String desc;

    RedisKeyEnum(String key, int expireTime, String desc) {
        this.key = key;
        this.expireTime = expireTime;
        this.desc = desc;
    }

    public static String formatKey(RedisKeyEnum keyEnum, Object... args) {
        return String.format(keyEnum.getKey(), args);
    }
    public String formatKey(Object... args){
        return String.format(this.getKey(), args);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
