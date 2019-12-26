package com.mainacad.factory;

import org.hibernate.SessionFactory;

public interface ConnectionFactory {
    SessionFactory getSessionFactory();
}
