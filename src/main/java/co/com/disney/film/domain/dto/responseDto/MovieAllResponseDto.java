package co.com.disney.film.domain.dto.responseDto;


import lombok.Data;

@Data
public class MovieAllResponseDto {

    private Long movieId;
    private String image;
    private String title;
    private String createDate;
    private Integer rate;
    private String genre;
}
