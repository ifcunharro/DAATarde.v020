package es.uvigo.esei.daa.tarde.entities.articles;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.LocalDate;

@Entity
@Table(name = "books")

public class Book extends Article {

    @Column(length = 150, nullable = true)
    private String author;

    @Column(length = 150, nullable = true)
    private String editorial;

    protected Book( ) {
        super();
    }

    public Book(
        final String    name,
        final String    description,
        final LocalDate date,
        final String    author,
        final String    editorial,
        final byte[ ]   picture
    ) {
        super(name, description, date, picture);
    }

    public Book(final String name, final LocalDate date) {
        super(name, date);
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(final String editorial) {
        this.editorial = editorial;
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
        if (obj == null || !(obj instanceof Book)) return false;

        final Book that = (Book) obj;
        final EqualsBuilder builder = new EqualsBuilder();

        builder.append(that.getName(), getName());
        builder.append(that.getDate(), getDate());

        return builder.isEquals();
    }

}
