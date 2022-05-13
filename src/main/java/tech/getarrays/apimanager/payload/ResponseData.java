package tech.getarrays.apimanager.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ResponseData {
    private Integer statusCode;
    private Map data=new HashMap();

    public void setMapData(String key, Object updateData) {
        this.data.put(key,updateData);
    }
}
