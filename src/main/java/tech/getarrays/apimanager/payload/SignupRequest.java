package tech.getarrays.apimanager.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 4, max = 25)
    private String username;

    @Size(max=100)
    private String name;


    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    private Set<String> role;

    @Size(max=200)
    private String address;


    @NotBlank
    @Size(max=15)
    private String phone;

    @NotBlank
    @Size(min=6,max=500)
    private String password;

    @Size(max=64)
    private String avatar;

    private boolean enabled;

}
