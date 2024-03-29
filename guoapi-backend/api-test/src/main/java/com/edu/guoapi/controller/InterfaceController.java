package com.edu.guoapi.controller;

import com.edu.guoapi.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test")
public class InterfaceController {

    @GetMapping("/get")
    public String getNameByGet(String name, HttpServletRequest request) {
        return "GET 你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" + name;
    }

    // @PostMapping("/user")
    // public String getUsernameByPost(@RequestBody User user, HttpServletRequest request) {
    //     String accessKey = request.getHeader("accessKey");
    //     String nonce = request.getHeader("nonce");
    //     String timestamp = request.getHeader("timestamp");
    //     String sign = request.getHeader("sign");
    //     String body = request.getHeader("body");
    //     // todo 实际情况应该是去数据库中查是否已分配给用户
    //     if (!accessKey.equals("key")) {
    //         throw new RuntimeException("无权限");
    //     }
    //     if (Long.parseLong(nonce) > 10000) {
    //         throw new RuntimeException("无权限");
    //     }
    //     // todo 时间和当前时间不能超过 5 分钟
    //
    //     // todo 实际情况中是从数据库中查出 secretKey
    //     String serverSign = SignUtils.genSign(body, "key");
    //     if (!sign.equals(serverSign)) {
    //         throw new RuntimeException("无权限");
    //     }
    //     String result = "POST 用户名字是" + user.getName();
    //     return result;
    // }

    @PostMapping("/user")
    public String getUsernameByPost(HttpServletRequest request) {
        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");
        // 查询用户是否有 accessKey
        if (!accessKey.equals("key")) {
            throw new RuntimeException("无权限");
        }
        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }
        // 时间和当前时间不能超过 5 分钟
        if (System.currentTimeMillis() - Long.parseLong(timestamp) > TimeUnit.MINUTES.toMillis(5)) {
            throw new RuntimeException("无权限");
        }
        // todo 实际情况中是从数据库中查出 secretKey
        String serverSign = SignUtils.genSign(body, "key");
        if (!sign.equals(serverSign)) {
            throw new RuntimeException("无权限");
        }
        return "成功调用";
    }
}
