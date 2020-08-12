package zp.gatewayservice.data;
/**
 *
 * @author namtv3
 */
public class DataRequest {

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
