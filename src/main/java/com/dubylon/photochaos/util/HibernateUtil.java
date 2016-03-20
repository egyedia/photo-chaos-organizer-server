package com.dubylon.photochaos.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public final class HibernateUtil {

  private static SessionFactory sessionFactory;
  private static ServiceRegistry serviceRegistry;

  private HibernateUtil() {
  }

  public static SessionFactory getSessionFactory() {
    Configuration configuration = new Configuration();
    configuration.configure();
    serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
    return sessionFactory;
  }
}