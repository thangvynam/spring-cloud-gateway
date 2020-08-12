package zp.gatewayservice.customfilters;

import com.google.gson.Gson;
import io.netty.buffer.ByteBufAllocator;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.bouncycastle.util.Strings;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import zp.gatewayservice.data.DataRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * @author namtv3
 */
@Component
public class AppGatewayFilterFactory implements GatewayFilterFactory<AppGatewayFilterFactory.Config> {

    private static final String FIRST_SERVICE = "http://localhost:9091";

    private static final String SECOND_SERVICE = "http://localhost:9092";

    private static final String REQUEST_BODY = "cachedRequestBodyObject";

    @Override
    public GatewayFilter apply(Config config) {
        return new OrderedGatewayFilter((exchange, chain) -> {

            if (exchange.getRequest().getHeaders().getContentType() == null) {
                return chain.filter(exchange);
            }

            String data = exchange.getAttribute(REQUEST_BODY);
            DataRequest dataReq = parseData(data);

            URI newUri = genUri(dataReq, exchange.getRequest().getPath().toString());
            if (newUri == null) {
                return null;
            }

            exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newUri);
            return chain.filter(exchange);

        }, RouteToRequestUrlFilter.ROUTE_TO_URL_FILTER_ORDER + 1);
    }

    private URI genUri(DataRequest dataReq, String path) {

        String host = "";
        URI newUri = null;
        int appId = dataReq.appid;

        if (appId == 441) {
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

    public static class Config {

    }
}


