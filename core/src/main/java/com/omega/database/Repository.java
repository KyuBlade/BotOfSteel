package com.omega.database;

public interface Repository<T> {

    /**
     * Create a new entity that can later be saved in this repository.
     *
     * @return new entity
     */
    T create() throws IllegalAccessException, InstantiationException;

    /**
     * Get an entity from its id.
     *
     * @param id entity id
     * @return the entity found
     */
    T findById(Object id);

    /**
     * Persist the entity.
     *
     * @param entity entity to save
     */
    void save(T entity);

    /**
     * Remove an entity from the database.
     *
     * @param entity entity to remove
     */
    void delete(T entity);
}
