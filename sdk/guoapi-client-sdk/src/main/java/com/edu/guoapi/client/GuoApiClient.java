package com.edu.guoapi.client;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.edu.guoapi.utils.HttpResponseDataUtils;
import com.edu.guoapi.utils.Params2JsonUtils;
import com.google.gson.JsonElement;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

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

    /**
     * 随机输出一句冷笑话
     * charset 返回编码类型[gbk|utf-8]默认utf-8
     * encode 返回格式类型[text|js|json]默认text
     *
     * @return 状态码和响应值
     */
    public ImmutablePair<Integer, String> randomMessage(String params) {
        String charset, encode;
        HttpRequest request = HttpRequest.get(GATEWAY_HOST + "/invoke/randomMessage");
        if (StringUtils.isNotBlank(params)) {
            JsonElement jsonParams = Params2JsonUtils.getJsonParams(params);
            charset = jsonParams.getAsJsonObject().get("charset").getAsString();
            encode = jsonParams.getAsJsonObject().get("encode").getAsString();
            // 不传参时使用默认参数
            request = StringUtils.isNotBlank(charset) ? request.form("charset", charset) : request.form("charset", "utf-8");
            request = StringUtils.isNotBlank(encode) ? request.form("encode", encode) : request.form("encode", "text");
        }

        HttpResponse response = request.addHeaders(getHeaderMap(params)).execute();
        return HttpResponseDataUtils.resData(response);
    }

    // public String randomAvatar(String params) {
    //     String method;// 输出壁纸端[mobile(手机端),pc（电脑端）,zsy（手机电脑自动判断）]默认为pc
    //     String 	lx;// 输出头像类型[a1（男头）|b1（女头）|c1（动漫头像）|c2（动漫女头）|c3（动漫男头）]默认为c1
    //     String format;// 输出壁纸格式[json|images]默认为images
    // }

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
