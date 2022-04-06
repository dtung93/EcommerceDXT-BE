package tech.getarrays.apimanager;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.getarrays.apimanager.model.User;

import java.util.Collection;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

   private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String email;
    private String address;
    private String phone;
    private String avatar;
    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;
    public MyUserDetails(Integer id, String username, String email, String password, String address,String phone, String avatar,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = Long.valueOf(id);
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar=avatar;
        this.address=address;
        this.phone=phone;
        this.authorities = authorities;
    }

    public static MyUserDetails build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new MyUserDetails(
                Math.toIntExact(user.getId()),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getPhone(),
                user.getAvatar(),
                user.getAddress(),
                authorities);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public Long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getAddress(){return address;}
    public String getPhone(){return phone;}
    public String getAvatar(){return avatar;}
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
       MyUserDetails user = (MyUserDetails) o;
        return Objects.equals(id, user.id);
    }
}
