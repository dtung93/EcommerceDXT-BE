package tech.getarrays.apimanager.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tech.getarrays.apimanager.model.FileDB;

import javax.transaction.Transactional;

public interface FileDBRepository extends JpaRepository<FileDB,String > {
    FileDB findById(Long id);


    @Query(nativeQuery = true,value="select * from file_db f join users u where u.id=f.user_id and u.id=:id")
    FileDB getByUserId(Long id);


    @Query(nativeQuery = true,value="select * from file_db f join users u where u.id=f.user_id and u.id=:userId")
      FileDB findUserId(String userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from file_db f where f.user_id=:userId")
    void deleteFile(String userId);
}
