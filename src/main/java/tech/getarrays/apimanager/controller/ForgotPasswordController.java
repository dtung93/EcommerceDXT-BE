package tech.getarrays.apimanager.controller;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.apimanager.exception.UserNotFoundException;
import tech.getarrays.apimanager.model.User;
import tech.getarrays.apimanager.payload.MessageResponse;
import tech.getarrays.apimanager.payload.ResetPasswordRequest;
import tech.getarrays.apimanager.service.UserService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api")
public class ForgotPasswordController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserService userService;



    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> processForgotPassword(@RequestBody String email, HttpServletRequest request) {
        String token = RandomString.make(30);
        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = "http://localhost:4200/reset-password";
            sendEmail(email,resetPasswordLink,token);
           return new ResponseEntity<>(new MessageResponse(token),HttpStatus.OK);
        } catch (UserNotFoundException | UnsupportedEncodingException | MessagingException ex) {
            return new ResponseEntity<>(new MessageResponse("NO USER FOUND"),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendEmail(String recipientEmail, String link, String token)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("contact@dxt.com", "DXT Support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                +"<span>Confirmation code  " + token+ "</span>"
                + "<p>Hello,we received a request to change your account password at DXT.</p>"
                + "<p>Below is the link to update your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Please contact us at contact@dxt.com if you"
                + " have not made the request.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> processResetPassword(@RequestBody ResetPasswordRequest data) {
        String token=data.getToken();
        String password=data.getPassword();
        User user = userService.getByResetPasswordToken(token);
        if (user== null) {
       return new ResponseEntity<>(new MessageResponse("INVALID TOKEN"),HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            userService.updatePassword(user, password);
           return new ResponseEntity<>(new MessageResponse("SUCCESS"),HttpStatus.OK);
        }
    }
}
