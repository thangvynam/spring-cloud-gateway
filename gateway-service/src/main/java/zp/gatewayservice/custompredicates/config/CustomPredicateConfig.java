//package com.nt.msgw.custompredicates.config;
//
//import com.nt.msgw.custompredicates.factories.CustomerRoutePredicateFactory;
//import com.nt.msgw.custompredicates.service.AppService;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.route.RouteLocator;
//import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
///**
// *
// * @author namtv3
// */
//@Configuration
//public class CustomPredicateConfig {
//
//    @Bean
//    public CustomerRoutePredicateFactory goldenCustomer(AppService appService) {
//        return new CustomerRoutePredicateFactory(appService);
//    }
//
//    @Bean
//    public RouteLocator routes(RouteLocatorBuilder builder, CustomerRoutePredicateFactory gf ) {
//        return builder.routes()
//                .route("dsl_golden_route", r -> r
//                        .path("/api/**")
//                        .and()
////                        .readBody(String.class, s -> true)
////                        .filters(f -> f.filter(new GatewayFilter() {
////                            @Override
////                            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
////                                try {
////
////                                } catch (Exception ex) {
////
////                                }
////
////                                return chain.filter(exchange);
////                            }
////                        }))
//                        .uri("https://www.facebook.com.vn/")
//                        .predicate(gf.apply(new CustomerRoutePredicateFactory.DataRequest(61))))
//                .route("dsl_common_route", r -> r.path("/api/**")
//                        .uri("https://www.facebook.com.vn/")
//                        .predicate(gf.apply(new CustomerRoutePredicateFactory.DataRequest(12))))
//                .build();
//    }
//}
