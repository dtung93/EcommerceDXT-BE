package tech.getarrays.apimanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.getarrays.apimanager.model.FileDB;

public interface FileDBRepository extends JpaRepository<FileDB,String > {
}
