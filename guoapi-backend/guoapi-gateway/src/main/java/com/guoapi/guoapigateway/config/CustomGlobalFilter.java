package com.guoapi.guoapigateway.config;

import com.guoapi.guoapiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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


    /**
     * 全局过滤器
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1、全局请求日志
        ServerHttpRequest request = exchange.getRequest();
        log.info("请求唯一标识：{}", request.getId());
        log.info("请求目标路径：{}", request.getPath().value());
        log.info("请求方法：{}", request.getMethodValue());
        log.info("请求参数：{}", request.getQueryParams());
        log.info("请求来源地址：{}", request.getLocalAddress());

        // 2、访问控制，黑白名单（直接设置状态码将请求拦截掉并返回）
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(request.getLocalAddress().getHostName())) {
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
        if (nonce == null || Long.parseLong(nonce) > 10000L) {
            return noAuthHandler(response);
        }
        // 时间不能超过 5 分钟
        long currentTime = System.currentTimeMillis() / 1000;
        if (timestamp == null || currentTime - Long.parseLong(timestamp) >= FIVE_MINUTES) {
            return noAuthHandler(response);
        }
        // todo 实际情况应从数据库中查出 secretKey
        String secretKey = "secretKey";
        String serverSign = SignUtils.genSign(body, secretKey);
        if (!serverSign.equals(sign)) {
            return noAuthHandler(response);
        }

        // 4、校验请求的接口是否在数据库中存在
        // todo 因为是在网关层，不建议再写一遍逻辑，可以通过远程调用来使用别的模块中现成的查询方法

        // 5、请求转发，调用模拟接口 -> 6、响应日志
        this.responseLog(exchange, chain);

        return chain.filter(exchange);
    }

    /**
     * 使用装饰器模式对 response 做功能的增强，如调用日志、调用成功失败后逻辑
     *
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> responseLog(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口之后才会响应并执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        //log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            //
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);// 释放掉内存

                                // 构建日志
                                String data = new String(content, StandardCharsets.UTF_8);//data
                                log.info("响应结果：{}", data);

                                // todo 7、调用成功，接口调用次数 + 1

                                return bufferFactory.wrap(content);
                            }));
                        }
                        else {
                            // 8、调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 返回装饰过的response
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);//降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }


    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> noAuthHandler(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
}
