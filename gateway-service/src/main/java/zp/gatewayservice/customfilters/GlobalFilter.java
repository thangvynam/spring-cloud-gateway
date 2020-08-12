package zp.gatewayservice.customfilters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
	
	public GlobalFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {

		System.out.println("inside SCGWGlobalFilter.apply method");
		
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest()
					.mutate()
					.header("global-header", "This is Global").build();
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