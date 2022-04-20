package tech.getarrays.apimanager;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.getarrays.apimanager.exception.TokenRefreshException;
import tech.getarrays.apimanager.jwt.JwtUtils;
import tech.getarrays.apimanager.model.ERole;
import tech.getarrays.apimanager.model.RefreshToken;
import tech.getarrays.apimanager.model.Role;
import tech.getarrays.apimanager.model.User;
import tech.getarrays.apimanager.payload.*;
import tech.getarrays.apimanager.repo.RoleRepo;
import tech.getarrays.apimanager.repo.UserRepo;
import tech.getarrays.apimanager.service.FileUpLoadService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.tomcat.jni.SSL.setPassword;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    UserRepo userRepo;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    RoleRepo roleRepo;
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,String siteUrl) throws MessagingException, UnsupportedEncodingException {
        if (userRepo.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("This username is already taken!"));
        }
        if (userRepo.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("This email is already taken!"));

        }
        if(userRepo.existsByPhone(signUpRequest.getPhone())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Phone numbers already in use!"));
        }


        // Create new user's account
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setAddress(signUpRequest.getAddress());
        user.setPhone(signUpRequest.getPhone());
//        String fileName= StringUtils.cleanPath(multipartFile.getOriginalFilename());
//        user.setAvatar(fileName);
        user.setAvatar(signUpRequest.getAvatar());
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null) {
            Role userRole = roleRepo.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepo.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepo.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    case "master":
                        Role masterRole = roleRepo.findByName(ERole.ROLE_MASTER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(masterRole);
                        break;
                    default:
                        Role userRole = roleRepo.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        user.setVerificationCode(RandomString.make(40));
        user.setEnabled(false);
        userRepo.save(user);
        siteUrl="http://localhost:4200/verified-account";
//       String uploadDir="user-photos/" + savedUser.getId();
//      FileUpLoadService.saveFile(uploadDir,fileName,multipartFile);
        sendVerificationEmail(user, siteUrl,user.getVerificationCode());
        return ResponseEntity.ok(new MessageResponse("Your new account is created!"));
    }
  public void sendVerificationEmail(User user, String siteUrl,String token) throws MessagingException,UnsupportedEncodingException{
      String toAddress = user.getEmail();
      String fromAddress = "dxt@global.com";
      String senderName = "DXT";
      String subject = "Please verify your registration";
      String content = "Dear [[name]],<br>"
              + "Please click the link below to verify your registration:<br>"
              + "<h5><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h5>"
              +"<div>token:"+token+"</div>"
              + "Thank you,<br>"
              + "DXT Global";

      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message);

      helper.setFrom(fromAddress, senderName);
      helper.setTo(toAddress);
      helper.setSubject(subject);

      content = content.replace("[[name]]", user.getUsername());
      String verifyURL = siteUrl+"/?verify-code="+user.getVerificationCode();

      content = content.replace("[[URL]]", verifyURL);

      helper.setText(content, true);

      mailSender.send(message);
  }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest.getUsername()+" "+ loginRequest.getPassword());
        UsernamePasswordAuthenticationToken request = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword());
     Authentication authentication = authenticationManager.authenticate(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return ResponseEntity.ok(new JwtResponse(jwt,
                refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getPhone(),
                userDetails.getAvatar(),
                userDetails.getAddress(),
                roles
 ));
    }
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Could not find Refresh token!"));
    }
}
