package tech.getarrays.apimanager.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
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
    private List<String> roles;
    private boolean enabled;



    public JwtResponse(String accessToken, String refreshToken, Long id, String username, String email,String avatar, String address,String phone, List<String> roles,boolean enabled) {

        this.refreshToken=refreshToken;
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.avatar=avatar;
        this.address=address;
        this.phone=phone;
        this.roles = roles;
        this.enabled=enabled;
    }


}
