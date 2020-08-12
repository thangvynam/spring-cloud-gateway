package zp.firstserivce.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author namtv3
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping(value = "/abc")
    public Mono<String> test(ServerHttpRequest request, ServerHttpResponse response) {

        System.out.println("Inside first service test method");

        HttpHeaders headers = request.getHeaders();
        headers.forEach((k, v) -> {
            System.out.println(k + " : " + v);
        });

        Mono<String> data = Mono.just("Hello from Reactive First Service test method!!");
        return data;

    }
}
