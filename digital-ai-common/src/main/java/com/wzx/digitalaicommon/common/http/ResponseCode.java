package com.wzx.digitalaicommon.common.http;

public enum ResponseCode {

    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR"),

    // 成功的一些特殊提示
    SUCCESS_ONCE_LINK_MSG(0,"下载成功,一次性链接已经失效"),
    // 通用请求参数校验
    ILLEGAL_ARGUMENT(1,"请求参数格式错误"),
    EMPTY_ARGUMENT(1,"请求参数为空"),

    /** modeify by wzx
     */
    NO_SMS_BAD(1,"短信内容有问题"),

    NO_MATCH_ARGUMENT_SET(1,"不能满足要求的参数设置"),
    NO_FILE_INPUT(1,"没有文件输入"),
    // 特殊需要进行前端返回说明的参数定义
    TASK_NAME_IS_EXIST(1,"任务名称已经存在"),
    ONCE_LINK_MSG_ERROR(1,"链接失效,下载文件失败~"),
    // 请求结果性的错误
    NODATA_ERROR(1,"查询结果为空"),
    TASK_BUILD_ERROR(1,"任务建立失败"),
    NO_RELEVANT_CONTENT_WAS_FOUND(1,"未查询到相关内容"),
    DECRYPT_ERROR(1,"解密错误,请联系我");


    private final int code;
    private final String desc;

    ResponseCode(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
