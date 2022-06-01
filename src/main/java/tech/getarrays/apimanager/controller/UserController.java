package tech.getarrays.apimanager.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tech.getarrays.apimanager.exception.StatusCode;
import tech.getarrays.apimanager.model.UserDTO;
import tech.getarrays.apimanager.payload.HandleUser;
import tech.getarrays.apimanager.payload.ResponseData;
import tech.getarrays.apimanager.exception.ResponseError;
import tech.getarrays.apimanager.model.User;
import tech.getarrays.apimanager.payload.UserChangePassword;
import tech.getarrays.apimanager.payload.MessageResponse;
import tech.getarrays.apimanager.repo.UserRepo;
import tech.getarrays.apimanager.service.UserService;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Transactional
public class UserController {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/verify-user/{verifyCode}")
    public ResponseEntity<?> getVerifyCode(@PathVariable("verifyCode") String verifyCode) {
        if (userService.verifyUser(verifyCode)) {
            ResponseData responseData = new ResponseData();
            responseData.setStatusCode(StatusCode.SuccessfulRequest);
            responseData.setMapData("verifyUser", userService.verifyUser(verifyCode));
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } else {
            ResponseError responseError = new ResponseError();
            responseError.setStatusCode(StatusCode.InternalError);
            responseError.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
            responseError.setErrorMessage("Error with server");
            return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("user/change-password")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR','USER','MASTER')")
    public ResponseEntity<?> changePassword(@RequestBody UserChangePassword user) {
        ResponseData responseData = new ResponseData();
        String password = userService.getPassword(user.getUsername());
        boolean hasUsername = encoder.matches(user.getOldPassword(), password);
        if (hasUsername) {
            int updatedUser = userService.changePassword(user.getUsername(), encoder.encode(user.getNewPassword()));
            responseData.setStatusCode(StatusCode.Created);
            responseData.setMapData("response", updatedUser);
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        } else {
            ResponseError responseError = new ResponseError();
            responseError.setStatusCode(StatusCode.InternalError);
            responseError.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
            responseError.setErrorMessage("Wrong password");
            return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MODERATOR','USER','MASTER')")
    public ResponseEntity findUser(@PathVariable("id") Integer id) {
        Optional<User> user = userService.findUserById(id);
        return new ResponseEntity(user, HttpStatus.OK);
    }

    @PutMapping("/user/update")
     public ResponseEntity<?> updateUser(@RequestBody UserDTO handleUser){
        ResponseData responseData=new ResponseData();
        try{
            userService.changeUser(handleUser);
            responseData.setStatusCode(StatusCode.SuccessfulRequest);
            responseData.setMapData("data",handleUser);
        }
        catch(Exception e){
            ResponseError error=new ResponseError();
            error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
            error.setErrorCode(StatusCode.InternalError);
            error.setErrorMessage(e.getMessage());
            logger.error(e.getMessage(),e);
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(responseData,HttpStatus.OK);
    }


    @PutMapping("role/update")
    public ResponseEntity<?> updateRole(@RequestBody User user) {
        int updateUser = userService.updateRole(user.getId(), user.getRoles().stream().findFirst().get().getId());
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }


    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER')")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        try {
            List<User> users;
            Pageable paging = (Pageable) PageRequest.of(page, size);
            Page<User> pageProds = null;
            if (username == null) {
                pageProds = (Page<User>) userService.getUsers(paging);
            } else {
                pageProds = userService.searchUser(username, paging);
            }

            users = pageProds.getContent();
            Map<String, Object> response = new HashMap<>();
            response.put("users", users);
            response.put("currentPage", pageProds.getNumber());
            response.put("totalUsers", pageProds.getTotalElements());
            response.put("totalPages", pageProds.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ResponseError responseError = new ResponseError();
            responseError.setErrorMessage(e.getMessage());
            responseError.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
            responseError.setStatusCode(StatusCode.InternalError);
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasRole('MASTER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('MASTER') or hasRole('ADMIN')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/master")
    @PreAuthorize("hasRole('MASTER')")
    public String masterAccess() {
        return "Master Board";
    }


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER')")
    public String adminAccess() {
        return "Admin Board.";
    }

}
