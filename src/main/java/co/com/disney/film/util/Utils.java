package co.com.disney.film.util;

import co.com.disney.film.domain.model.Image;
import java.util.Objects;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class Utils {
    public static Image setUpImageOrThrowException(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        validFileType(file);
        try {
            validFilename(fileName);
            Image image = new Image();
            image.setFileName(fileName);
            image.setFileType(file.getContentType());
            image.setData(file.getBytes());
            image.setSize(file.getSize());
            return image;
        } catch (Exception e) {
            throw new Exception("Could not save file: " + fileName);
        }
    }

    private static void validFilename(String fileName) throws Exception {
        if (fileName.contains("..")) {
            throw new Exception("Filename contains invalid sequence");
        }
    }

    private static void validFileType(MultipartFile file) throws Exception {
        if(!(Objects.equals(file.getContentType(), "image/jpeg") || Objects.equals(file.getContentType(), "image/png"))) {
            throw new Exception("File type: " + file.getContentType() + ", it is not permitted.");
        }
    }
}
