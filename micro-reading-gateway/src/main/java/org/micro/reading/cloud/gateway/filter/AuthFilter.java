package org.micro.reading.cloud.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import org.micro.reading.cloud.common.pojo.account.User;
import org.micro.reading.cloud.common.result.HttpCodeEnum;
import org.micro.reading.cloud.common.result.Result;
import org.micro.reading.cloud.gateway.common.config.SystemPropertiesConfig;
import org.micro.reading.cloud.gateway.common.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * @author micro-paul
 * @date 2022年03月21日 15:23
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {


    @Autowired
    private SystemPropertiesConfig systemPropertiesConfig;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 白名单Path
        Set<String> whitelist = getWhiteList();
        String path = exchange.getRequest().getPath().toString();
        // 主页接口、图书接口正则匹配
        boolean indexMatch = Pattern.matches("/index[^\\s]*", path);
        boolean bookMatch = Pattern.matches("/book/[^\\s]*", path);
        // 白名单接口、开放接口放行
        if (bookMatch || indexMatch || whitelist.contains(path)) {
            return chain.filter(exchange);
        }
        String[] segments = path.split("/");
        if (!whitelist.contains(segments[1])) {

            // 认证
            String token = exchange.getRequest().getHeaders().getFirst("token");
            Result result = JwtUtil.validationToken(token);
            if (result.getCode() == HttpCodeEnum.OK.getCode()) {
                // 认证通过
                User user = (User) result.getData();
                // 追加请求头用户信息
                Consumer<HttpHeaders> headersConsumer = httpHeaders -> {
                    httpHeaders.set("userId",user.getId().toString());
                    httpHeaders.set("nickName",user.getNickName());
                };
                ServerHttpRequest httpRequest = exchange.getRequest()
                        .mutate()
                        .headers(headersConsumer)
                        .build();
                exchange.mutate().request(httpRequest).build();
                return chain.filter(exchange);
            }
            // 认证过期、失败，均返回401
            ServerHttpResponse response = exchange.getResponse();
            byte[] bits = JSONObject.toJSONString(result).getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bits);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            // 指定编码，否则在浏览器中会中文乱码
            response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Set<String> getWhiteList(){
        String whitelists = systemPropertiesConfig.getWhitelist();
        if (StringUtils.isEmpty(whitelists)) {
            return new HashSet<>();
        }
        Set<String> whiteList = new HashSet<>();
        String[] whiteArray = whitelists.split(",");
        for (int i = 0; i < whiteArray.length; i++) {
            whiteList.add(whiteArray[i]);
        }
        return whiteList;
    }
}
