package zp.gatewayservice.custompredicates.enums;

import java.util.HashMap;

/**
 *
 * @author namtv3
 */
public enum AppEnum {

    TOPUP(61),
    MOBILECARD(12),
    ;

    private final int value;

    private static final HashMap<Integer, AppEnum> appMap = new HashMap<>();

    private AppEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public String toString() {
        return this.name();
    }

    public static AppEnum fromInt(int iValue) {
        return (AppEnum) appMap.get(iValue);
    }

    static {
        AppEnum[] value = values();
        int length = value.length;

        for(int index = 0; index < length; ++index) {
            AppEnum appEnum = value[index];
            appMap.put(appEnum.value, appEnum);
        }
    }
}
