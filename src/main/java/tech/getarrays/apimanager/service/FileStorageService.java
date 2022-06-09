package tech.getarrays.apimanager.service;

import jdk.jshell.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tech.getarrays.apimanager.exception.StatusCode;
import tech.getarrays.apimanager.model.FileDB;
import tech.getarrays.apimanager.model.User;
import tech.getarrays.apimanager.repo.FileDBRepository;
import tech.getarrays.apimanager.repo.UserRepo;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.stream.Stream;

@Service
public class FileStorageService {
    @Autowired
    private FileDBRepository fileDBRepository;
    @Autowired
    private UserRepo userRepo;

    public FileDB uploadFile(MultipartFile file, String userId) throws IOException {
        if (this.getFileById(userId) == null) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            FileDB FileDB = new FileDB();
            FileDB.setName(fileName);
            FileDB.setType(file.getContentType());
            FileDB.setData(file.getBytes());
            FileDB.setUser(this.findUser(userId));
            return fileDBRepository.save(FileDB);
        } else {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            FileDB fileDB = this.getFileById(userId);
            fileDB.setName(fileName);
            fileDB.setType(file.getContentType());
            fileDB.setData(file.getBytes());
            return fileDBRepository.save(fileDB);
        }
    }

    @Transactional
    public void deleteFile(String userId) {
     if(this.getFileById(userId)!=null)
          fileDBRepository.deleteFile(userId);
     else {
         throw new IllegalArgumentException("No photo found");
     }
    }


    public User findUser(String id) {
        return userRepo.getId(id);
    }

    public FileDB getFile(Long id) {
        return fileDBRepository.findById(id);
    }

    public FileDB getFileByUserId(Long id) {
        return fileDBRepository.getByUserId(id);
    }

    public FileDB getFileById(String id) {
        return fileDBRepository.findUserId(id);
    }

    public Stream<FileDB> getAllFiles() {
        return fileDBRepository.findAll().stream();
    }

}
