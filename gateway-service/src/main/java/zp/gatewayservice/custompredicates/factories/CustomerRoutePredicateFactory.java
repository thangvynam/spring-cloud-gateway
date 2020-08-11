//package com.nt.msgw.custompredicates.factories;
//
//import java.net.URI;
//
//import com.nt.msgw.custompredicates.enums.AppEnum;
//import com.nt.msgw.custompredicates.service.AppService;
//import org.bouncycastle.util.Strings;
//import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
//import org.springframework.cloud.gateway.support.DefaultServerRequest;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.core.io.buffer.DataBufferUtils;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.util.UriComponentsBuilder;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.atomic.AtomicReference;
//import java.util.function.Predicate;
//
//
///**
// *
// * @author namtv3
// */
//public class CustomerRoutePredicateFactory extends AbstractRoutePredicateFactory<CustomerRoutePredicateFactory.DataRequest> {
//
//    private final AppService appService;
//
//    public CustomerRoutePredicateFactory(AppService appService) {
//        super(DataRequest.class);
//        this.appService = appService;
//    }
//
//    @Override
//    public Predicate<ServerWebExchange> apply(DataRequest config) {
//        return (ServerWebExchange t) -> {
//            if (t.getRequest().getHeaders().getContentType() == null) {
//                return false;
//            }
//
//            HttpHeaders httpHeaders = t.getRequest().getHeaders();
//            List<String> values = httpHeaders.get("App");
//            int appId = Integer.parseInt(values.get(0));
//            boolean check = appService.predicateApp(appId) == AppEnum.MOBILECARD ? true : false;
//            return check;
////            Flux<DataBuffer> requestBody = t.getRequest().getBody();
////            StringBuilder sb = new StringBuilder();
////            requestBody.subscribe(buffer -> {
////                byte[] bytes = new byte[buffer.readableByteCount()];
////                buffer.read(bytes);
////                DataBufferUtils.release(buffer);
////                String bodyString = new String(bytes, StandardCharsets.UTF_8);
////                sb.append(bodyString);
////            });
////
////            String str = sb.toString();
////            System.out.println(str);
////
////            return false;
//        };
//    }
//
//    private static String toRaw(Flux<DataBuffer> body) {
//        AtomicReference<String> rawRef = new AtomicReference<>();
//        body.subscribe(buffer -> {
//            byte[] bytes = new byte[buffer.readableByteCount()];
//            buffer.read(bytes);
//            DataBufferUtils.release(buffer);
//            rawRef.set(Strings.fromUTF8ByteArray(bytes));
//        });
//        return rawRef.get();
//    }
//
//    public static class DataRequest {
//
//        public int appId;
//
//        public DataRequest() { }
//
//        public DataRequest(int appId) {
//            this.appId = appId;
//        }
//    }
//}
