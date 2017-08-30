package com.omega.core.database;

import com.omega.core.database.repository.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;

public class DatastoreManager {

    private Map<Class<?>, Repository> repositories;

    public DatastoreManager() {
        this.repositories = new HashMap<>();
    }

    public void addRepositories(Repository... repositories) {
        stream(repositories).forEach(this::addRepository);
    }

    public void addRepository(Repository repository) {
        Class<?> repositoryClass = repository.getClass();
        Class<?> repositoryInterface = Arrays.stream(repositoryClass.getInterfaces())
            .filter(aInterface -> aInterface.isInstance(repository))
            .findFirst()
            .orElse(null);
        if (repositoryInterface != null) {
            repositories.put(repositoryInterface, repository);
        }

        repositories.put(repositoryClass, repository);
    }

    @SafeVarargs
    public final void removeRepositories(Class<? extends Repository>... repositoryTypes) {
        stream(repositoryTypes).forEach(this::removeRepository);
    }

    public void removeRepository(Class<? extends Repository> repositoryType) {
        Class<?> repositoryInterface = Arrays.stream(repositoryType.getInterfaces())
            .filter(aInterface -> aInterface.isAssignableFrom(repositoryType))
            .findFirst()
            .orElse(null);
        if (repositoryInterface != null) {
            repositories.remove(repositoryInterface);
        }

        repositories.remove(repositoryType);
    }

    @SuppressWarnings("unchecked")
    public <T extends Repository> T getRepository(Class<T> repositoryType) {
        return (T) repositories.get(repositoryType);
    }
}
