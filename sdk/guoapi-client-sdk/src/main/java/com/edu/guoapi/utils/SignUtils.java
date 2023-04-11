package com.edu.guoapi.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

public class SignUtils {
    /**
     * 加密生成签名
     *
     * @param map
     * @param secretKey
     * @return
     */
    public static String genSign(String map, String secretKey) {
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String content = map + "." + secretKey;
        return md5.digestHex(content);
    }
}
