package zp.gatewayservice.customfilters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class PreFilter extends AbstractGatewayFilterFactory<PreFilter.Config> {

	public PreFilter() {
		super(Config.class);
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		System.out.println("inside SCGWPreFilter.apply method");
		
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest()
					.mutate()
					.uri(exchange.getRequest().getURI())
					.header("pre-header", "This is Pre-Header")
					.build();
			return chain.filter(exchange.mutate().request(request).build());
		};
	}
	
	public static class Config {

		private String name;
		
		public String getName() {
			return this.name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
	}
}
