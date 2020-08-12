package zp.gatewayservice.customfilters;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

/**
 * @author namtv3
 */
@Configuration
public class GatewayAutoConfiguration {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, AppGatewayFilterFactory factory) {
        return builder.routes()
                .route(r -> r.path("/api/**")
                        .and().readBody(String.class, requestBody -> {
                            return true;
                        })
                        .filters(f -> f
                                .rewritePath("/api/abc", "/cpstelcoconfig/zpi/gettelcogrouppackage")
                                .filter(factory.apply(new AppGatewayFilterFactory.Config()))
                        )
                        .uri("http://localhost:9092/")
                        .id("app-service"))
                .build();
    }
}
