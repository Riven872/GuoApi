package com.guoapi.guoapigateway.config;

import com.guoapi.guoapiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

/**
 * 全局过滤器
 *
 * @author rivenzhou
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    // 自定义白名单地址
    public static final List<String> IP_WHITE_LIST = Collections.singletonList("127.0.0.1");

    // 5分钟
    public static final Long FIVE_MINUTES = 60 * 5L;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1、全局请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识：{}",request.getId());
        log.info("请求目标路径：{}",request.getPath().value());
        log.info("请求方法：{}",request.getMethodValue());
        log.info("请求参数：{}",request.getQueryParams());
        log.info("请求来源地址：{}",request.getLocalAddress());
        // 2、访问控制，黑白名单（直接设置状态码将请求拦截掉并返回）
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(request.getLocalAddress().getHostName())){
            return noAuthHandler(response);
        }
        // 3、用户鉴权（判断 ak、sk 是否合法）
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        // todo 实际情况应查询数据库查看用户是否拥有ak
        if (!"key".equals(accessKey)) {
            return noAuthHandler(response);
        }
        if (nonce == null || Long.parseLong(nonce) > 10000) {
            return noAuthHandler(response);
        }
        // 时间不能超过 5 分钟
        long currentTime = System.currentTimeMillis() / 1000;
        if (timestamp == null || currentTime - Long.parseLong(timestamp) >= FIVE_MINUTES){
            return noAuthHandler(response);
        }
        // todo 实际情况应从数据库中查出 secretKey
        String secretKey = "secretKey";
        String serverSign = SignUtils.genSign(body, secretKey);
        if (!serverSign.equals(sign)){
            return noAuthHandler(response);
        }
        // 4、校验请求的接口是否在数据库中存在
        // todo 因为是在网关层，不建议再写一遍逻辑，可以通过远程调用来使用别的模块中现成的查询方法

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> noAuthHandler(ServerHttpResponse response){
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
}
