package tech.getarrays.apimanager;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import tech.getarrays.apimanager.model.AuthenticationBean;
import tech.getarrays.apimanager.model.Role;
import tech.getarrays.apimanager.model.User;
import tech.getarrays.apimanager.repo.UserRepo;
import tech.getarrays.apimanager.service.UserService;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Transactional
public class UserController {
    @Autowired
private UserService userService;


    @GetMapping("/user/{id}")
    public ResponseEntity findUser(@PathVariable("id") Integer id) {
        Optional<User> user = userService.findUserById(id);
        return new ResponseEntity(user, HttpStatus.OK);
    }

    @PutMapping("/user/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updateUser = userService.updateUser(user);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }
    @PutMapping("role/update")
    public ResponseEntity<?> updateRole(@RequestBody User user) {
        int  updateUser = userService.updateRole(user.getId(), user.getRoles().stream().findFirst().get().getId());
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }


    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER')")
       public ResponseEntity<Map<String,Object>> getAllUsers(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue = "8") int size
    ){
        try{
            List<User> users;
            Pageable paging= (Pageable) PageRequest.of(page,size);
            Page<User> pageProds ;
            if(username==null)
            { pageProds = (Page<User>) userService.getUsers(paging);}
            else
            { pageProds=userService.findUserByUsernameContaining(username,paging);}
            users= pageProds.getContent();
            Map<String,Object> response=new HashMap<>();
            response.put("users",users);
            response.put("currentPage",pageProds.getNumber());
            response.put("totalItems",pageProds.getTotalElements());
            response.put("totalPages",pageProds.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
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
    public String masterAccess(){
        return "Master Board";
    }


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER')")
    public String adminAccess() {
        return "Admin Board.";
    }

}
