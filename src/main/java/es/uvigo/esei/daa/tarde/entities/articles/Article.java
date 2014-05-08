package es.uvigo.esei.daa.tarde.entities.articles;

import static org.apache.commons.lang3.Validate.notNull;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.joda.time.LocalDate;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @Type(value = Book.class,         name = "book"),
    @Type(value = Comic.class,        name = "comic"),
    @Type(value = Movie.class,        name = "movie"),
    @Type(value = MusicStorage.class, name = "music")
})

@Entity
@Table(name = "articles")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Article {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 150, nullable = false)
    protected String name;

    @Column(nullable = false)
    @org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    protected LocalDate date;

    @Lob
    @Column(length = 2000, nullable = false)
    protected String description = "";

    @Lob
    @Column(nullable = false)
    protected byte[ ] picture = new byte[ ] { 0 };

    @Column(nullable = false, name = "verified")
    protected boolean isVerified = false;

    protected Article( ) { }

    protected Article(
        final String name,
        final String description,
        final LocalDate date,
        final byte[ ] picture
    ) {
        notNull(name, "Article's name cannot be null");
        notNull(description, "Article's description cannot be null");
        notNull(date, "Article's date cannot be null");
        notNull(picture, "Article's picture cannot be null");

        this.name        = name;
        this.description = description;
        this.date        = date;
        this.picture     = picture;
    }

    protected Article(final String name, final LocalDate date) {
        notNull(name, "Article's name cannot be null");
        notNull(date, "Article's date cannot be null");

        this.name = name;
        this.date = date;
    }

    public Long getId( ) {
        return id;
    }

    public String getName( ) {
        return name;
    }

    public String getDescription( ) {
        return description;
    }

    public LocalDate getDate( ) {
        return date;
    }

    public byte[ ] getPicture( ) {
        return picture;
    }

    public boolean isVerified( ) {
        return this.isVerified;
    }

    public void setDescription(final String description) {
        notNull(description, "Article's description cannot be null");
        this.description = description;
    }

    public void setPicture(final byte[ ] picture) {
        notNull(picture, "Article's picture cannot be null");
        this.picture = Arrays.copyOf(picture, picture.length);
    }

    public void setVerified(final boolean isVerified) {
        this.isVerified = isVerified;
    }

    @Transient @JsonIgnore
    public boolean isPersisted( ) {
        return getId() != null;
    }

    @Override
    public abstract int hashCode( );

    @Override
    public abstract boolean equals(final Object obj);

}

