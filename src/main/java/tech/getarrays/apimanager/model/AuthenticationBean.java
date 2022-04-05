package tech.getarrays.apimanager.model;

public class AuthenticationBean {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public AuthenticationBean(String message){
        this.message=message;
    }
    private String message;
    @Override
    public String toString(){
        return String.format("HelloWorldBean [message=%s]",message);
    }
}
