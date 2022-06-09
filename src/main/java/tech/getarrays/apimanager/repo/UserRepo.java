package tech.getarrays.apimanager.repo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.getarrays.apimanager.model.User;

import java.util.Optional;
@Repository
public interface UserRepo extends CrudRepository <User,Integer>, JpaRepository<User,Integer>, JpaSpecificationExecutor<User>, PagingAndSortingRepository<User,Integer> {
 @Query(nativeQuery = true, value="SELECT * FROM users u where u.verification_code=:verifyCode")
   public User findByVerificationCode(String verifyCode);

 @Query(nativeQuery = true,value="select * from users u where u.id=:id")
public User getId(String id);

 @Query(nativeQuery = true, value="update users u set u.password=:newPassword where u.username=:username")
 @Modifying
int changePassword(String username, String newPassword);


    public User findUserByUsername(String username);


   @Query(nativeQuery=true, value="update user_roles set role_id=:id where user_id=:userId")
   @Modifying
   int updateUserRole(@Param("userId") Integer userId, @Param("id") Integer id);

   public User findByEmail(String email);

   @Query(nativeQuery = true, value="SELECT * FROM users u where u.reset_password_token=:token")
   public User findByResetPasswordToken(@Param("token") String token);

   @Query(nativeQuery = true, value="SELECT * FROM users u where u.username like %:usernameoremail% or u.email like %:usernameoremail%")
   Page<User> searchUser(String usernameoremail, Pageable pageable);

   Boolean existsByPhone(String phone);
   Page<User> findByUsername(String username,Pageable pageable);
   Page<User> findById(Integer id, Pageable pageable);
   Boolean existsByUsername(String username);
   @Query("select (count(u) > 0) from User u where u.email = ?1")
   Boolean existsByEmail(String email);
   Optional<User> findById(Long id);
   Page<User> findAll(Pageable pageable);
}
