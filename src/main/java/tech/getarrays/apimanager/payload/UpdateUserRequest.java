package tech.getarrays.apimanager.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserRequest {

    @Size(max = 50)
    @Email
    private String email;
    private Set<String> role;

    @Size(max=200)
    private String address;

    @Size(max=15)
    private String phone;


    @Size(min=6,max=500)
    private String password;

    @Size(max=64)
    private String avatar;
}
