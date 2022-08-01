package co.com.disney.film.service.contract;

import java.util.Optional;

public interface GenericDAO<E> {

    Optional<E> findById(Long id);

    E save(E entity);

    Iterable<E> findAll();

    void deleteById(Long id);
}
