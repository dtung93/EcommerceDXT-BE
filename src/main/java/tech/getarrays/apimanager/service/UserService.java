package tech.getarrays.apimanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.getarrays.apimanager.model.User;
import tech.getarrays.apimanager.repo.RefreshTokenRepo;
import tech.getarrays.apimanager.repo.UserRepo;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {
    private UserRepo userRepo;
    private RefreshTokenRepo refreshRepo;
    @Autowired
    public UserService(UserRepo userRepo,RefreshTokenRepo refreshRepo) {
        this.refreshRepo=refreshRepo;
        this.userRepo = userRepo;
    }
  public int updateRole(Integer userId, Integer id){
        return userRepo.updateUserRole(userId,id);
  }

   public Page<User> getUsers(Pageable paging){
       return (Page<User>) userRepo.findAll(paging);
   }

   public Optional<User> findUserById(Integer id){
       return Optional.ofNullable(userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User id= " + id + " not found")));
    }


    public Page<User> findUserByUsername(String username, Pageable pageable){
        return  (Page<User>)userRepo.findByUsername(username,pageable);
    }
   public Page<User> findUserByUsernameContaining(String username,Pageable pageable){
        return (Page<User>)userRepo.findByUsernameContaining(username,pageable);
   }
    public User updateUser(User user){
       return userRepo.save(user);
    }
    


    @Transactional
    public void deleteUser(Integer id){
        refreshRepo.deleteByUserId(id);
       userRepo.deleteById(id);
    }
}
