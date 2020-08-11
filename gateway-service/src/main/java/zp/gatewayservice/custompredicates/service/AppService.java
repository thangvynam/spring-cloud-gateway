package zp.gatewayservice.custompredicates.service;

import zp.gatewayservice.custompredicates.enums.AppEnum;
import org.springframework.stereotype.Component;

/**
 *
 * @author namtv3
 */
@Component
public class AppService {

    public AppEnum predicateApp(int appId) {
        return AppEnum.fromInt(appId);
    }
}
