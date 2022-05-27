package tech.getarrays.apimanager.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusCode  {
    public static final Integer AuthError=401;
    public static final Integer NoResource=404;
    public static final Integer InternalError=500;
    public static final Integer BadRequest=400;
    public static final Integer SuccessfulRequest=200;
    public static final Integer Created=201;

}
