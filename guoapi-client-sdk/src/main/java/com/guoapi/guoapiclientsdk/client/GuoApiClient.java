package com.guoapi.guoapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.guoapi.guoapiclientsdk.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.guoapi.guoapiclientsdk.utils.SignUtils.genSign;

/**
 * 调用第三方接口的客户端
 *
 * @author rivenzhou
 */
public class GuoApiClient {
    /**
     * 用户授权名
     */
    private String accessKey;

    /**
     * 用户授权密钥
     */
    private String secretKey;

    public static final String GATEWAY_HOST = "http://localhost:8090";

    public GuoApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    /**
     * get 请求
     *
     * @param name
     * @return
     */
    public String getNameByGet(String name) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        return HttpUtil.get(GATEWAY_HOST + "/api/name", map);
    }

    /**
     * post 请求（参数在 url 中拼接）
     *
     * @param name
     * @return
     */
    public String getNameByPost(String name) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        return HttpUtil.post(GATEWAY_HOST + "/api/name", map);
    }

    /**
     * post 请求（参数通过 Json 传输）
     *
     * @param user
     * @return
     */
    public String getUsernameByPost(User user) {
        String json = JSONUtil.toJsonStr(user);

        HttpResponse response = HttpRequest
                .post(GATEWAY_HOST + "/api/name/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        String body = response.body();
        return body;
    }


    /**
     * 添加请求头
     *
     * @return
     */
    private Map<String, String> getHeaderMap(String body) {
        HashMap<String, String> map = new HashMap<>();
        map.put("accessKey", accessKey);// 授权名
        // map.put("secretKey", secretKey);// 授权密钥（千万不能在服务器之间传输）
        map.put("nonce", RandomUtil.randomNumbers(4));// 随机数
        map.put("body", body);// 用户传递的参数
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));// 当前时间戳
        map.put("sign", genSign(body, secretKey));// 生成的签名

        return map;
    }
}