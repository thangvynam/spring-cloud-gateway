package zp.gatewayservice.custompredicates.service;

import org.springframework.stereotype.Component;

/**
 * @author namtv3
 */
@Component
public class AppService {

    public boolean checkRefundService(int appId) {
        return appId != 441;
    }
}
