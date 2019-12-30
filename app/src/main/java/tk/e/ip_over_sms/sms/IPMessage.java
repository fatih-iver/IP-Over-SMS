package tk.e.ip_over_sms.sms;

import java.util.HashMap;
import java.util.Map;

public class IPMessage {

    private int type;
    private Map<String,String> params;

    public IPMessage(int type) {
        this.type = type;
    }

    public IPMessage(int type, Map<String, String> params) {
        this.type = type;
        this.params = params;
    }

    public int getType() {
        return type;
    }

    public IPMessage setType(int type) {
        this.type = type;
        return this;
    }

    public IPMessage initializeParams(){
        params = new HashMap<>();
        return this;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public IPMessage addParam(String key, String value){
        params.put(key, value);
        return this;
    }

    public IPMessage setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public String toString() {
        return "IPMessage{" +
                "type=" + type +
                ", params=" + params +
                '}';
    }
}
