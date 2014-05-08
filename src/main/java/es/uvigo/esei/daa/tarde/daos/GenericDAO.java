package es.uvigo.esei.daa.tarde.daos;

import java.lang.reflect.ParameterizedType;

import javax.persistence.Entity;

public abstract class GenericDAO<T> {

    protected String getEntityName( ) {
        final Class<T> clazz = getGenericClass();
        final String name = clazz.getAnnotation(Entity.class).name();

        if (name.isEmpty()) return clazz.getSimpleName();
        else return name;
    }

    @SuppressWarnings("unchecked")
    protected final Class<T> getGenericClass() {
        return (Class<T>) (
            (ParameterizedType) getClass().getGenericSuperclass()
        ).getActualTypeArguments()[0];
    }

}
