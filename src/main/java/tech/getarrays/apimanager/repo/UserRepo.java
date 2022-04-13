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

   @Query(nativeQuery=true, value="update user_roles set role_id=:id where user_id=:userId")
   @Modifying
   int updateUserRole(@Param("userId") Integer userId, @Param("id") Integer id);

   public User findByEmail(String email);

   public User findByResetPasswordToken(String token);

   Page<User> findByUsernameContaining(String username,Pageable pageable);
   Boolean existsByPhone(String phone);
   Page<User> findByUsername(String username,Pageable pageable);
   Page<User> findById(Integer id, Pageable pageable);
   Boolean existsByUsername(String username);
   Boolean existsByEmail(String email);
   Optional<User> findById(Long id);
   Page<User> findAll(Pageable pageable);
}
