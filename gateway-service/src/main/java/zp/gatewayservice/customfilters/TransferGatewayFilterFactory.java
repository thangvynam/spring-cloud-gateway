package zp.gatewayservice.customfilters;

import com.google.gson.Gson;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.RouteToRequestUrlFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;
import zp.gatewayservice.data.DataRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * @author namtv3
 */
@Component
public class TransferGatewayFilterFactory implements GatewayFilterFactory<TransferGatewayFilterFactory.Config> {

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

        host = SECOND_SERVICE;

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


