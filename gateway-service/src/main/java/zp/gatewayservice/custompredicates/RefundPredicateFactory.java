package zp.gatewayservice.custompredicates;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.handler.AsyncPredicate;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import zp.gatewayservice.custompredicates.service.AppService;
import zp.gatewayservice.data.DataRequest;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author namtv3
 */
@Component
public class RefundPredicateFactory extends AbstractRoutePredicateFactory<RefundPredicateFactory.Config> {

    private static final Log LOGGER = LogFactory.getLog(RefundPredicateFactory.class);

    private static final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

    @Autowired
    private AppService appService;

    public RefundPredicateFactory() {
        super(Config.class);
    }

    @Override
    public AsyncPredicate<ServerWebExchange> applyAsync(Config config) {
        return (ServerWebExchange exchange) -> {
            Class inClass = config.getInClass();
            Object cachedBody = exchange.getAttribute("cachedRequestBodyObject");
            if (cachedBody != null) {
                try {
                    boolean test = config.predicate.test(cachedBody);
                    exchange.getAttributes().put("read_body_predicate_test_attribute", test);

                    DataRequest dataReq = parseData(String.valueOf(cachedBody));
                    boolean checkLogic = appService.checkRefundService(dataReq.appid);

                    return Mono.just(config.isTransferPlaform ? checkLogic : !checkLogic);
                } catch (ClassCastException var7) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Predicate test failed because class in predicate does not match the cached body object", var7);
                    }

                    return Mono.just(false);
                }
            }

            return DataBufferUtils.join(exchange.getRequest().getBody()).flatMap((dataBuffer) -> {
                DataBufferUtils.retain(dataBuffer);

                final Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                    return Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount()));
                });

                ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
                    public Flux<DataBuffer> getBody() {
                        return cachedFlux;
                    }
                };

                return ServerRequest.create(exchange.mutate().request(mutatedRequest).build(), messageReaders).bodyToMono(inClass).doOnNext((objectValue) -> {
                    exchange.getAttributes().put("cachedRequestBodyObject", objectValue);
                    exchange.getAttributes().put("cachedRequestBody", cachedFlux);
                }).map((objectValue) -> {
                    DataRequest dataReq = parseData(String.valueOf(objectValue));
                    boolean checkLogic = appService.checkRefundService(dataReq.appid);

                    return config.isTransferPlaform ? checkLogic : !checkLogic;
                });
            });
        };
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        throw new UnsupportedOperationException("RefundPredicateFactory is only async.");
    }

    private DataRequest parseData(String queryString) {

        final List<NameValuePair> params =
                URLEncodedUtils.parse(queryString, StandardCharsets.UTF_8);
        String value = params.get(0).getValue();

        Gson gson = new Gson();
        DataRequest dataReq = gson.fromJson(value, DataRequest.class);
        return dataReq;

    }

    @Validated
    public static class Config {
        public boolean isTransferPlaform;
        private Class inClass;
        private Predicate predicate;
        private Map<String, Object> hints;

        public Config() {
        }

        public Class getInClass() {
            return this.inClass;
        }

        public RefundPredicateFactory.Config setInClass(Class inClass) {
            this.inClass = inClass;
            return this;
        }

        public Predicate getPredicate() {
            return this.predicate;
        }

        public RefundPredicateFactory.Config setPredicate(Predicate predicate) {
            this.predicate = predicate;
            return this;
        }

        public RefundPredicateFactory.Config isTransferPlaform(boolean isTransferPlaform) {
            this.isTransferPlaform = isTransferPlaform;
            return this;
        }

        public <T> RefundPredicateFactory.Config setPredicate(Class<T> inClass, Predicate<T> predicate) {
            this.setInClass(inClass);
            this.predicate = predicate;
            return this;
        }

        public Map<String, Object> getHints() {
            return this.hints;
        }

        public RefundPredicateFactory.Config setHints(Map<String, Object> hints) {
            this.hints = hints;
            return this;
        }
    }
}
