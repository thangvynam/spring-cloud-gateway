package zp.gatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import zp.gatewayservice.customfilters.TelcoGatewayFilterFactory;
import zp.gatewayservice.customfilters.TransferGatewayFilterFactory;
import zp.gatewayservice.custompredicates.RefundPredicateFactory;

/**
 * @author namtv3
 */
@Configuration
public class GatewayAutoConfiguration {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, TransferGatewayFilterFactory factory,
                                 TelcoGatewayFilterFactory factory1, RefundPredicateFactory refundPredicateFactory) {

        return builder.routes()
                .route("transfer_refund", r -> r
                        .path("/refund/**")
                        .filters(f -> f
                                .rewritePath("/refund/abc", "/transfer/refund")
                                .addRequestHeader("header-refund", "transfer")
                                //.filter(factory1.apply(new App1GatewayFilterFactory.Config()))
                        )
                        .uri("http://localhost:9091/")
                        .asyncPredicate(refundPredicateFactory.applyAsync((c) -> {
                            c.setPredicate(String.class, requestBody -> true).isTransferPlaform(true);
                        })))
                .route("telco_refund", r -> r
                        .path("/refund/**")
                        .filters(f -> f
                                .rewritePath("/refund/abc", "/telco/refund")
                                .addRequestHeader("header-refund", "telco")
                                //.filter(factory.apply(new AppGatewayFilterFactory.Config()))
                        )
                        .uri("http://localhost:9092/")
                        .asyncPredicate(refundPredicateFactory.applyAsync((c) -> {
                            c.setPredicate(String.class, requestBody -> true).isTransferPlaform(false);
                    })))
                .build();
    }
}
