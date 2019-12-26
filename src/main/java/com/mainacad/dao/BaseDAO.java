package com.mainacad.dao;

import com.mainacad.factory.ConnectionFactory;
import com.mainacad.factory.H2Factory;
import com.mainacad.factory.PostgresFactory;
import com.mainacad.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;

@Getter
@Setter
public abstract class BaseDAO<T extends BaseEntity> {

    @Autowired
    ConnectionFactory connectionFactory;

    private Class<T> persistentClass;

    public BaseDAO() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public T save(T entity) {
        Session session = connectionFactory.getSessionFactory().openSession();
        session.getTransaction().begin();
        Integer id = (Integer) session.save(entity);
        session.getTransaction().commit();
        session.close();
        entity.setId(id);
        return entity;
    }

    public T update(T entity){
        if (entity.getId() == null) {
            return null;
        }
        Session session = connectionFactory.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.update(entity);
        session.getTransaction().commit();
        session.close();
        return entity;
    }

    public T findOne(Integer id){
        Session session = connectionFactory.getSessionFactory().openSession();
        session.getTransaction().begin();
        T entityFromDB = session.find(getPersistentClass(), id);
        session.getTransaction().commit();
        session.close();
        return entityFromDB;
    }

    public void delete(T entity) {
        Session session = connectionFactory.getSessionFactory().openSession();
        session.getTransaction().begin();
        session.delete(entity);
        session.getTransaction().commit();
        session.close();
    }
}
