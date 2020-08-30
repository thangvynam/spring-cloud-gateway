package zp.secondservice.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/telco")
public class SimpleController {
    @PostMapping(value = "/refund")
    public Mono<String> getData(ServerHttpRequest request, ServerHttpResponse response) {
        System.out.println("Inside Second Service getData method");
        HttpHeaders headers = request.getHeaders();

        headers.forEach((k, v) -> {
            System.out.println(k + " : " + v);
        });
        Mono<String> data = Mono.just("Hello from Reactive Second Service getData method!!");
        return data;
    }
}
