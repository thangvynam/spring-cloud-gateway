package zp.gatewayservice.customfilters;

import com.google.gson.Gson;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.bouncycastle.util.Strings;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * @author namtv3
 */
@Component
public class AppGatewayFilterFactory implements GatewayFilterFactory<AppGatewayFilterFactory.Config> {

    private static final String FIRST_SERVICE = "http://localhost:9091";

    private static final String SECOND_SERVICE = "http://localhost:9091";

    @Override
    public Class getConfigClass() {
        return Config.class;
    }

    @Override
    public Config newConfig() {
        Config config = new Config();
        return config;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {
            if (exchange.getRequest().getHeaders().getContentType() == null) {
                return chain.filter(exchange);
            }

            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {

                        DataBufferUtils.retain(dataBuffer);
                        Flux<DataBuffer> cachedFlux = Flux.defer(() -> Flux.just(dataBuffer.slice(0, dataBuffer.readableByteCount())));

                        String queryString = toRaw(cachedFlux);

                        DataRequest dataReq = parseData(queryString);

                        URI newUri = genUri(dataReq, exchange.getRequest().getPath().toString());
                        if (newUri == null) {
                            return null;
                        }

                        ServerHttpRequest request = exchange.getRequest().mutate()
                                .uri(newUri)
                                .build();

                        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newUri);
                        return chain.filter(exchange.mutate().request(request).build());
                    });
        }, RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER + 1);
    }

    private URI genUri(DataRequest dataReq, String path) {
        String host = "";
        URI newUri = null;
        int appId = dataReq.appid;

        if (appId == 12) {
            host = FIRST_SERVICE;
        } else {
            host = SECOND_SERVICE;
        }

        try {
            newUri = new URI(host + path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return newUri;
    }

    private DataRequest parseData(String queryString) {
        final List<NameValuePair> params =
                URLEncodedUtils.parse(queryString, StandardCharsets.UTF_8);
        String value = params.get(0).getValue();

        Gson gson = new Gson();
        DataRequest dataReq = gson.fromJson(value, DataRequest.class);
        return dataReq;
    }

    private static String toRaw(Flux<DataBuffer> body) {
        AtomicReference<String> rawRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            byte[] bytes = new byte[buffer.readableByteCount()];
            buffer.read(bytes);
            DataBufferUtils.release(buffer);
            rawRef.set(Strings.fromUTF8ByteArray(bytes));
        });
        return rawRef.get();
    }


    public static class Config {

    }

    public static class DataRequest {
        public int appid;
        public int zalopayid;

        public DataRequest() {
        }

        public DataRequest(int appid, int zalopayid) {
            this.appid = appid;
            this.zalopayid = zalopayid;
        }

        public int getAppid() {
            return appid;
        }

        public void setAppid(int appid) {
            this.appid = appid;
        }

        public int getZalopayid() {
            return zalopayid;
        }

        public void setZalopayid(int zalopayid) {
            this.zalopayid = zalopayid;
        }
    }
}