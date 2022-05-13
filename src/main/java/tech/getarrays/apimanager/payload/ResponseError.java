package tech.getarrays.apimanager.payload;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseError {
    private Integer statusCode;
    private Integer errorCode;
    private String errorMessage;
}
