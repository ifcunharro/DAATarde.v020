package es.uvigo.esei.daa.tarde.entities.articles;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.LocalDate;

@Entity
@Table(name = "movies")
public class Movie extends Article {

    @Column(length = 150, nullable = true)
    private String director;

    @Column(length = 150, nullable = true)
    private String duration;

    @Column(length = 150, nullable = true)
    private String genre;

    protected Movie( ) {
        super();
    }

    public Movie(
        final String    name,
        final String    description,
        final LocalDate date,
        final String    director,
        final String    duration,
        final String    genre,
        final byte[ ]   picture
    ) {
        super(name, description, date, picture);
    }

    public Movie(final String name, final LocalDate date) {
        super(name, date);
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(final String director) {
        this.director = director;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(final String duration) {
        this.duration = duration;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    @Override
    public final int hashCode( ) {
        final HashCodeBuilder builder = new HashCodeBuilder();

        builder.append(getName());
        builder.append(getDate());

        return builder.toHashCode();
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) return true;
        if (obj == null || !(obj instanceof Movie)) return false;

        final Movie that = (Movie) obj;
        final EqualsBuilder builder = new EqualsBuilder();

        builder.append(that.getName(), getName());
        builder.append(that.getDate(), getDate());

        return builder.isEquals();
    }

}
