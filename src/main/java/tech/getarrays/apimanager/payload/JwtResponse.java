package tech.getarrays.apimanager.payload;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private String address;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public JwtResponse(String accessToken, String refreshToken, Long id, String username, String email,String avatar, String address,String phone, List<String> roles) {

        this.refreshToken=refreshToken;
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.avatar=avatar;
        this.address=address;
        this.phone=phone;
        this.roles = roles;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar){
        this.avatar=avatar;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address){
        this.address=address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    private List<String> roles;
}
