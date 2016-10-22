package ru.domain.java3.lesson2.DAO;


import ru.domain.java3.lesson2.exceptions.EntityDeleteError;
import ru.domain.java3.lesson2.exceptions.EntityInsertError;
import ru.domain.java3.lesson2.exceptions.EntityUpdateError;

import java.util.Collection;

interface DAO<T, K> {

    Collection<?> select(T entity);

    Collection<?> selectAll();

    void insert(T entity) throws EntityInsertError;

    void update(T entity) throws EntityUpdateError;

    void delete(K id) throws EntityDeleteError;


}
