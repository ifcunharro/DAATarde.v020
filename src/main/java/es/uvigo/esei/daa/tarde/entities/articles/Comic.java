package es.uvigo.esei.daa.tarde.entities.articles;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.LocalDate;

@Entity
@Table(name = "comics")
public class Comic extends Article {

    @Column(length = 150, nullable = true)
    private String serie;

    @Column(length = 150, nullable = true)
    private String editorial;

    @Column(length = 150, nullable = true)
    private String collection;

    protected Comic( ) {
        super();
    }

    public Comic(
        final String    name,
        final String    description,
        final LocalDate date,
        final String    serie,
        final String    collection,
        final String    editorial,
        final byte[ ]   picture
    ) {
        super(name, description, date, picture);
    }

    public Comic(final String name, final LocalDate date) {
        super(name, date);
    }

    public String getSerie( ) {
        return serie;
    }

    public void setSerie(final String serie) {
        this.serie = serie;
    }

    public String getEditorial( ) {
        return editorial;
    }

    public void setEditorial(final String editorial) {
        this.editorial = editorial;
    }

    public String getCollection( ) {
        return collection;
    }

    public void setCollection(final String collection) {
        this.collection = collection;
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
        if (obj == null || !(obj instanceof Comic)) return false;

        final Comic that = (Comic) obj;
        final EqualsBuilder builder = new EqualsBuilder();

        builder.append(that.getName(), getName());
        builder.append(that.getDate(), getDate());

        return builder.isEquals();
    }

}
