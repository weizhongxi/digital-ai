package com.wzx.digitalaicommon.common.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用于向下游进行推送的Json转换实体 - 用于外呼端通用
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushDBJsonContent {

    /**
     * 活动id
     */
    private Long actId;

    /**
     * 活动名称
     */
    private String actName;

    /**
     * 外呼号码
     */
    private List<Client> clientList;


    /**
     * 包装类
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static  class Client{
        /**
         * 具体的外呼号码
         */
        private String cellphone;
    }
}
