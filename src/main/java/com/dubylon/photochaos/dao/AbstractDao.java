package com.dubylon.photochaos.dao;

import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class AbstractDao {

  public static SessionFactory getSessionFactory() throws PCHandlerError {
    SessionFactory sessionFactory = null;
    try {
      sessionFactory = HibernateUtil.getSessionFactory();
    } catch (Exception e) {
      throw new PCHandlerError("ERROR_CONNECTING_TO_DATASTORE", e);
    }
    return sessionFactory;
  }

  public static void rollback(Transaction tx, Exception e, Session session) throws PCHandlerError {
    e.printStackTrace();
    if (tx != null) {
      try {
        tx.rollback();
      } catch (Exception ex) {
        ex.printStackTrace();
      } finally {
        session.close();
      }
    }
    throw new PCHandlerError("ERROR_WHILE_READING", e);
  }
}
