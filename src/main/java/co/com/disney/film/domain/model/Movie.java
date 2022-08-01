package co.com.disney.film.domain.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "fileType", column = @Column(name = "file_type")),
        @AttributeOverride(name = "fileName", column = @Column(name = "file_name"))
    })
    private Image image;
    private String title;
    private LocalDate createDate;
    private Integer rate;

    @ManyToMany(
        cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        fetch = FetchType.LAZY
    )
    @JoinTable(
        name = "movie_character",
        joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "movieId"),
        inverseJoinColumns = @JoinColumn(name = "character_id", referencedColumnName = "characterId")
    )
    //@JsonManagedReference
    @JsonIgnore
    private Set<Character> characters = new HashSet<>();

    @ManyToOne(
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY
    )
    @JoinColumn(
        name = "genre_id",
        referencedColumnName = "genreId"
    )
    //@JsonBackReference
    @JsonIgnore
    private Genre genre;

    @PrePersist
    private void createDate() {
        createDate = LocalDate.now();
    }

    @Override
    public String toString() {
        return "Movie= {" +
            "movieId =" + movieId +
            ", image ='" + image.getFileName() + '\'' +
            ", title ='" + title + '\'' +
            ", createDate =" + createDate +
            ", rate =" + rate +
            ", genre =" + genre.getName() +
            '}';
    }
}
