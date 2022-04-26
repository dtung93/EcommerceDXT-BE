package tech.getarrays.apimanager;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password-form";
    }

    @PostMapping("/forgot-password")
    public MessageResponse processForgotPassword(@RequestBody String email, HttpServletRequest request) {
        String token = RandomString.make(30);
        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = "http://localhost:4200/reset-password";
            sendEmail(email,resetPasswordLink,token);
            return new MessageResponse(token);
        } catch (UserNotFoundException | UnsupportedEncodingException | MessagingException ex) {
            return new MessageResponse("Could not find any user with the email address");
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
                +"<span>Confirmation code:" + token+ "</span>"
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

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);

        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "reset-password-form";
    }
    @PostMapping("/reset-password")
    public MessageResponse processResetPassword(@RequestBody ResetPasswordRequest data, Model model) {
        String token=data.getToken();
        String password=data.getPassword();
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");
        if (user== null) {
            model.addAttribute("message", "Invalid Token");
            return new MessageResponse("Password update failed!");
        } else {
            userService.updatePassword(user, password);
            model.addAttribute("message", "You have successfully changed your password.");
            return new MessageResponse("Your password has successfully changed");
        }
    }
}
