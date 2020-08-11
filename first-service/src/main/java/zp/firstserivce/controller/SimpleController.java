package zp.firstserivce.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Mono;
import zp.firstserivce.config.DataRequest;

import java.util.Map;

@RestController
@RequestMapping("/nt-ms")
public class SimpleController {

	@PostMapping(value = "/get-data", consumes = "application/x-www-form-urlencoded")

//	public Mono<String> getData(ServerHttpRequest request, ServerHttpResponse response, @RequestBody DataRequest dataRequest) {
	public Mono<String> getData(@RequestParam Map<String, String> body ) {
		System.out.println("Inside SC-MS1 getData method");
//		HttpHeaders headers = request.getHeaders();
//
//		headers.forEach((k,v)->{
//			System.out.println(k + " : " + v);
//		});
//
//		ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from("nt-ms1-cookie", "ntMs1CookieValue");
//		ResponseCookie cookie = builder.build();
//		response.addCookie(cookie);
		Mono<String> data = Mono.just("Hello from Reactive SC-MS1 getData method!!");
		return data;
	}
}