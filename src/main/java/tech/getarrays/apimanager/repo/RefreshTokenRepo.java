package tech.getarrays.apimanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.getarrays.apimanager.model.RefreshToken;
import tech.getarrays.apimanager.model.User;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Long> {
    @Override
    Optional<RefreshToken> findById(Long id);
    Optional<RefreshToken> findByToken(String token);


    @Query("delete from RefreshToken rt where rt.user.id=:id")
    @Modifying
    void deleteByUserId(@Param("id") Integer id);

}
