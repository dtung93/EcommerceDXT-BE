package tech.getarrays.apimanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.getarrays.apimanager.exception.UpdateUserException;
import tech.getarrays.apimanager.exception.UserNotFoundException;
import tech.getarrays.apimanager.model.User;
import tech.getarrays.apimanager.model.UserDTO;
import tech.getarrays.apimanager.payload.UserPaging;
import tech.getarrays.apimanager.repo.RefreshTokenRepo;
import tech.getarrays.apimanager.repo.UserRepo;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private UserRepo userRepo;
    private RefreshTokenRepo refreshRepo;

    @Autowired
    public UserService(UserRepo userRepo, RefreshTokenRepo refreshRepo) {
        this.refreshRepo = refreshRepo;
        this.userRepo = userRepo;
    }

    public boolean verifyUser(String verificationCode) {
        User user = userRepo.findByVerificationCode(verificationCode);
        if (user == null) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepo.save(user);
            return true;
        }
    }

    //Randomly generated a token to a user that has email address
    public void updateResetPasswordToken(String token, String email) throws UserNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepo.save(user);
        } else {
            throw new UserNotFoundException("Could not find any user with the email " + email);
        }

    }

    //  Get the user match the token string
    public User getByResetPasswordToken(String token) {
        return userRepo.findByResetPasswordToken(token);
    }

    //  Update password and set passwordresettoken to null
    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        userRepo.save(user);
    }

    public int updateRole(Integer userId, Integer id) {
        return userRepo.updateUserRole(userId, id);
    }

    public Page<User> getUsers(UserPaging user, Pageable paging) {
       Page<User> pageUsers=null;
       if(user.getUsername()==null)
           pageUsers=userRepo.findAll(paging);
       else
            pageUsers=userRepo.searchUser(user.getUsername(),paging);
        return pageUsers;
    }

    public Optional<User> findUserById(Integer id) {
        return Optional.ofNullable(userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User id= " + id + " not found")));
    }


    public Page<User> findUserByUsername(String username, Pageable pageable) {
        return (Page<User>) userRepo.findByUsername(username, pageable);
    }

    public Page<User> searchUser(String usernameoremail, Pageable paging) {
        return (Page<User>) userRepo.searchUser(usernameoremail, paging);
    }

    public boolean existByUsername(String username) {
        return userRepo.existsByUsername(username);
    }

    public boolean existByEmai(String email) {
        return userRepo.existsByEmail(email);
    }

    public boolean existByPhone(String phone) {
        return userRepo.existsByPhone(phone);
    }


    public User updateUser(User user) {
        return userRepo.save(user);
    }

    public UserDTO changeUser(UserDTO handleUser) {
        if (handleUser == null || handleUser.getUser() == null) {
            throw new UpdateUserException("No user");
        }
        if (handleUser.getEmail() != null && !Objects.equals(handleUser.getEmail(),handleUser.getUser().getEmail())) {
            if (this.existByEmai(handleUser.getEmail())) {
                throw new UpdateUserException("Email already exists!");
            }
        }
        if (handleUser.getPhone() != null && !Objects.equals(handleUser.getPhone(),handleUser.getUser().getPhone())) {
            if (this.existByPhone(handleUser.getPhone()))
                throw new UpdateUserException("Phone already exists");
        }
            User updateUser=this.findUserById(handleUser.getUser().getId()).get();
            updateUser.setName(handleUser.getName());
            updateUser.setEmail(handleUser.getEmail());
            updateUser.setPhone(handleUser.getPhone());
            updateUser.setAddress(handleUser.getAddress());
            updateUser.setRoles(handleUser.getRoles());
            this.updateUser(updateUser);
            handleUser.setName(updateUser.getName());
            handleUser.setEmail(updateUser.getEmail());
            handleUser.setPhone(updateUser.getPhone());
            handleUser.setAddress(updateUser.getAddress());
            handleUser.setAddress(updateUser.getAddress());
            handleUser.setRoles(updateUser.getRoles());
            return handleUser;
    }

    @Transactional
    public void deleteUser(Integer id) {
        refreshRepo.deleteByUserId(id);
        userRepo.deleteById(id);
    }

    public int changePassword(String username, String newPassword) {
        return userRepo.changePassword(username, newPassword);
    }

    public String getPassword(String username) {
        return userRepo.findUserByUsername(username).getPassword();
    }
}
