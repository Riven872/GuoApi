package com.edu.guoapi.invokeUrl;

import cn.hutool.http.HttpUtil;

import java.util.HashMap;

@Deprecated
public class InvokeApi {
    public String ResolveTestApi(String requestParams){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", requestParams);
        String res = HttpUtil.get("localhost:9101/api/test/get");
        return res;
    }
}
