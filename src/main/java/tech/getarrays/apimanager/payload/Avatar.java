package tech.getarrays.apimanager.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class Avatar {
    private Integer userId;
    private MultipartFile multipartFile;
}
