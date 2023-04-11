package com.edu.guoapi.client;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

import static com.edu.guoapi.utils.SignUtils.genSign;

/**
 * 调用第三方接口的客户端
 *
 * @author rivenzhou
 */
public class GuoApiClient {
    /**
     * 用户授权名
     */
    private final String accessKey;

    /**
     * 用户授权密钥
     */
    private final String secretKey;

    /**
     * 请求到指定的网关，经过网关过滤
     */
    public static final String GATEWAY_HOST = "http://localhost:9000";

    public GuoApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }


    /**
     * 测试接口是否调通（自定义接口）
     *
     * @param requestParams
     * @return
     */
    public String testUrl(String requestParams) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", requestParams);
        String res = HttpRequest
                .get(GATEWAY_HOST + "/api/test/get")
                .form(map)
                .addHeaders(getHeaderMap(null))
                .execute()
                .body();
        return res;
    }

    /**
     * 测试接口（Post 且 Json 格式）
     *
     * @param requestParams
     * @return
     */
    public String testUser(Object requestParams) {
        String json = JSONUtil.toJsonStr(requestParams);

        HttpResponse response = HttpRequest
                .post(GATEWAY_HOST + "/api/test/user")
                .addHeaders(getHeaderMap(json))
                .body(json)
                .execute();
        String body = response.body();
        return body;
    }

    // region 留存
    // /**
    //  * http 调用接口
    //  * @param url 接口地址
    //  * @param method 接口调用类型
    //  * @param requestParams 接口参数
    //  * @return
    //  */
    // public String invokeUrl(String url, String method, String requestParams){
    //     if ("get".equalsIgnoreCase(method)){
    //         HashMap<String, Object> map = new HashMap<>();
    //         return HttpUtil.get(GATEWAY_HOST + "/api/name", map);
    //     }
    //     if ("post".equalsIgnoreCase(method)){
    //         // todo
    //         return null;
    //     }
    //     return null;
    // }
    //
    // /**
    //  * get 请求
    //  *
    //  * @param name
    //  * @return
    //  */
    // public String getNameByGet(String name) {
    //     HashMap<String, Object> map = new HashMap<>();
    //     map.put("name", name);
    //     return HttpUtil.get(GATEWAY_HOST + "/api/name", map);
    // }
    //
    // /**
    //  * post 请求（参数在 url 中拼接）
    //  *
    //  * @param name
    //  * @return
    //  */
    // public String getNameByPost(String name) {
    //     HashMap<String, Object> map = new HashMap<>();
    //     map.put("name", name);
    //     return HttpUtil.post(GATEWAY_HOST + "/api/name", map);
    // }
    //
    // /**
    //  * post 请求（参数通过 Json 传输）
    //  *
    //  * @param object
    //  * @return
    //  */
    // public String getUsernameByPost(Object object) {
    //     String json = JSONUtil.toJsonStr(object);
    //
    //     HttpResponse response = HttpRequest
    //             .post(GATEWAY_HOST + "/api/name/user")
    //             .addHeaders(getHeaderMap(json))
    //             .body(json)
    //             .execute();
    //     String body = response.body();
    //     return body;
    // }
    //
    //

    // endregion

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
