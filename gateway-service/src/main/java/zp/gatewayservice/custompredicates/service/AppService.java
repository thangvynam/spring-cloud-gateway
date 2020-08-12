package zp.gatewayservice.custompredicates.service;

import org.springframework.stereotype.Component;
import zp.gatewayservice.custompredicates.enums.AppEnum;

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
