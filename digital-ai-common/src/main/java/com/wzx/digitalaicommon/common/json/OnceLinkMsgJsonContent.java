package com.wzx.digitalaicommon.common.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用于进行一次性链接返回的转换Json体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnceLinkMsgJsonContent implements Serializable {

    /**
     * 链接地址
     */
    private String onceLink;


}
