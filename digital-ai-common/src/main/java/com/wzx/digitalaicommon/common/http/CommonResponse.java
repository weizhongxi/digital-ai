package com.wzx.digitalaicommon.common.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


//属性为 空（""） 或者为 NULL 都不序列化，则返回的json是没有这个字段的。这样对移动端会更省流量
@JsonSerialize
@Getter // 设置get方法
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
public class CommonResponse<T> implements Serializable {

    // 成功还是失败的状态标识 0,成功 1,失败
    private int status;

    // 返回信息
    private String msg;

    // 返回的结果数据
    private T data;


    /*
        封装所有的构造器为私有的
     */

    private CommonResponse(int status) {
        this.status = status;
    }

    private CommonResponse(int status, T data) { // ps: 当调用T为String类型时候,会默认调用下面的ServerResponse(int status, String msg)类型的构造器
        this.status = status;
        this.data = data;
    }

    private CommonResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private CommonResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    /*
        对外开放调用的静态方法,用来调用私有构造器,来返回成功结果给前台
     */
    //返回成功码和默认的成功信息
    public static <T> CommonResponse<T> createBySuccess() {
        return new CommonResponse<T>(ResponseCode.SUCCESS.getCode(),ResponseCode.SUCCESS.getDesc());
    }

    //返回成功码和成功信息
    public static <T> CommonResponse<T> createBySuccessMessage(String msg) {
        return new CommonResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    //返回成功码和数据
    public static <T> CommonResponse<T> createBySuccess(T data) {
        return new CommonResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    //返回错误码和错误描述
    public static <T> CommonResponse<T> createBySuccessByCode(ResponseCode responseCode){
        return new CommonResponse<T>(responseCode.getCode(),responseCode.getDesc());
    }

    //返回成功码和成功信息和数据
    public static <T> CommonResponse<T> createBySuccess(String msg, T data) {
        return new CommonResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    /*
       对外开放调用的静态方法,用来调用私有构造器,来返回失败结果给前台
    */
    //返回错误码和错误描述
    public static <T> CommonResponse<T> createByError(ResponseCode responseCode){
        return new CommonResponse<T>(responseCode.getCode(),responseCode.getDesc());
    }

    //返回错误码和错误信息(传入)
    public static <T> CommonResponse<T> createByErrorMessage(String errorMessage){
        return new CommonResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    //返回错误码(传入)和错误信息(传入)
    public static <T> CommonResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new CommonResponse<T>(errorCode,errorMessage);
    }
}
