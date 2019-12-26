package com.mainacad.config;

import com.mainacad.dao.ItemDAO;
import com.mainacad.dao.UserDAO;
import com.mainacad.factory.ConnectionFactory;
import com.mainacad.factory.H2Factory;
import com.mainacad.factory.PostgresFactory;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;

@Configuration
public class AppConfig {

    @Bean
    @Profile("test")
    public ConnectionFactory getH2Factory() {
        return new H2Factory();
    }

    @Bean
    @Profile({"dev", "prod", "stage"})
    public ConnectionFactory getPostgresFactory() {
        return new PostgresFactory();
    }

    @Bean
    public UserDAO getUserDAO(){
        return new UserDAO();
    }

    @Bean
    public ItemDAO getItemDAO(){
        return new ItemDAO();
    }

}
