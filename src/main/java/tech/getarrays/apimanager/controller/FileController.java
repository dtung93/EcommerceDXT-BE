package tech.getarrays.apimanager.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.getarrays.apimanager.exception.ResponseError;
import tech.getarrays.apimanager.exception.StatusCode;
import tech.getarrays.apimanager.model.FileDB;
import tech.getarrays.apimanager.payload.MessageResponse;
import tech.getarrays.apimanager.payload.ResponseFile;
import tech.getarrays.apimanager.service.FileStorageService;
import tech.getarrays.apimanager.service.UserService;

@Controller
@RequestMapping("/api")
public class FileController {
    @Autowired
    private FileStorageService storageService;
    @Autowired
    private UserService userService;
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping(value="/upload",consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> uploadFile(@RequestPart("userId") String userId, @RequestPart("file") MultipartFile multiPartFile) {
        String message = "";
        try {
            storageService.uploadFile(multiPartFile,userId);
            message = "Uploaded the file successfully: " + multiPartFile.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
        } catch (Exception e) {
            ResponseError responseError=new ResponseError();
            responseError.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
            responseError.setStatusCode(StatusCode.InternalError);
            responseError.setErrorMessage(e.getMessage());
            logger.error(e.getMessage(),e);
            return new ResponseEntity<>(responseError,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

@DeleteMapping("/file/delete/{userId}")
public ResponseEntity<?> deleteFile(@PathVariable String userId){
        try{
            storageService.deleteFile(userId);
            String message = "File successfully deleted ";
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
        }
        catch (Exception e) {
            ResponseError responseError=new ResponseError();
            responseError.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
            responseError.setStatusCode(StatusCode.InternalError);
            responseError.setErrorMessage(e.getMessage());
            logger.error(e.getMessage(),e);
            return new ResponseEntity<>(responseError,HttpStatus.INTERNAL_SERVER_ERROR);
        }
}


    @GetMapping("/files")
    public ResponseEntity<List<ResponseFile>> getListFiles() {
        List<ResponseFile> files = storageService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getId().toString())
                    .toUriString();
            return new ResponseFile(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }

    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        FileDB fileDB = storageService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }

    @GetMapping("/file/{userid}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long userid) {
        try {
            FileDB fileDB = storageService.getFileByUserId(userid);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                    .body(fileDB.getData());
        } catch (Exception e) {
            ResponseError responseError = new ResponseError();
            responseError.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
            responseError.setStatusCode(StatusCode.InternalError);
            responseError.setErrorMessage(e.getMessage());
            logger.error(e.getMessage(), e);
            return (ResponseEntity<byte[]>) ResponseEntity.badRequest();
        }
    }
}