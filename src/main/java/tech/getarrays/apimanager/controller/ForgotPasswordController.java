package tech.getarrays.apimanager.controller;

import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.apimanager.exception.ResponseError;
import tech.getarrays.apimanager.exception.StatusCode;
import tech.getarrays.apimanager.exception.UserNotFoundException;
import tech.getarrays.apimanager.model.User;
import tech.getarrays.apimanager.payload.MessageResponse;
import tech.getarrays.apimanager.payload.ResetPasswordRequest;
import tech.getarrays.apimanager.payload.ResponseData;
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


    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostMapping("/forgot-password")
    public ResponseEntity<?> processForgotPassword(@RequestBody String email, HttpServletRequest request) {
        String token = RandomString.make(30);
        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = "http://localhost:4200/reset-password";
            sendEmail(email,resetPasswordLink,token);
           return new ResponseEntity<>(new MessageResponse(token),HttpStatus.OK);
        } catch (UserNotFoundException | UnsupportedEncodingException | MessagingException ex) {
            logger.error(ex.getMessage(),ex);
            ResponseError responseError = new ResponseError();
            responseError.setErrorMessage("No users found");
            responseError.setStatusCode(StatusCode.BadRequest);
            responseError.setErrorCode(HttpStatus.BAD_REQUEST.ordinal());
            return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<?> processResetPassword(@RequestBody ResetPasswordRequest data) {
        String token=data.getToken();
        String password=data.getPassword();
        User user = userService.getByResetPasswordToken(token);
      try {
          if (user == null) {
              ResponseError responseError = new ResponseError();
              responseError.setErrorMessage("Invalid or expired token");
              responseError.setStatusCode(StatusCode.BadRequest);
              responseError.setErrorCode(HttpStatus.BAD_REQUEST.ordinal());
              return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
          } else {
              userService.updatePassword(user, password);
              ResponseData responseData = new ResponseData();
              responseData.setStatusCode(StatusCode.SuccessfulRequest);
              responseData.setMapData("response", 1);
              return new ResponseEntity<>(responseData, HttpStatus.OK);
          }
      }
      catch (Exception e){
          ResponseError responseError = new ResponseError();
          responseError.setErrorMessage(e.getMessage());
          responseError.setStatusCode(StatusCode.BadRequest);
          responseError.setErrorCode(HttpStatus.BAD_REQUEST.ordinal());
          logger.error(e.getMessage(),e);
          return new ResponseEntity<>(responseError, HttpStatus.BAD_REQUEST);
      }
    }
}
