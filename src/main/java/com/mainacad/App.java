package com.mainacad;

import com.mainacad.dao.UserDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;


public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.mainacad");
        UserDAO userDAO = applicationContext.getBean(UserDAO.class);
        System.out.println(userDAO.toString());
    }
}
