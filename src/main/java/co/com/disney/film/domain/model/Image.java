package co.com.disney.film.domain.model;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
public class Image {

    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;

    private long size;

}
