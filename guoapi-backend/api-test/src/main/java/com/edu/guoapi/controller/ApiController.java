package com.edu.guoapi.controller;

import cn.hutool.http.HttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invoke")
public class ApiController {
    /**
     * 随机输出一句冷笑话
     *
     * @param charset 返回编码类型[gbk|utf-8]默认utf-8
     * @param encode  返回格式类型[text|js|json]默认text
     * @return 返回的状态码
     */
    @GetMapping("/randomMessage")
    public String randomMessage(String charset, String encode) {
        HttpRequest request = HttpRequest.get("https://api.btstu.cn/yan/api.php");
        if (StringUtils.isNotBlank(charset)) {
            request.form(charset, charset);
        }

        if (StringUtils.isNotBlank(encode)) {
            request.form(encode, encode);
        }
        return request.execute().body();
    }
}
