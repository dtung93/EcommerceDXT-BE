package tech.getarrays.apimanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.getarrays.apimanager.model.ERole;
import tech.getarrays.apimanager.model.Role;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role,Long> {
   Optional<Role> findByName(ERole name);
}
