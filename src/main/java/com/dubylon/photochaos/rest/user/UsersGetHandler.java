package com.dubylon.photochaos.rest.user;

import com.dubylon.photochaos.model.db.User;
import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class UsersGetHandler implements IPhotoChaosHandler {

  public UsersGetHandler() {
  }

  @Override
  public UsersGetData handleRequest(HttpServletRequest request) throws PCHandlerError {

    SessionFactory sessionFactory = null;
    try {
      sessionFactory = HibernateUtil.getSessionFactory();
    } catch (Exception e) {
      throw new PCHandlerError("ERROR_CONNECTING_TO_DATASTORE", e);
    }

    Session session = sessionFactory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      List list = session.createCriteria(User.class).list();
      tx.commit();
      UsersGetData response = new UsersGetData();
      response.setUsers(list);
      return response;
    } catch (HibernateException e) {
      e.printStackTrace();
      if (tx != null) {
        tx.rollback();
      }
      throw new PCHandlerError("ERROR_WHILE_LISTING", e);
    } finally {
      session.close();
    }
  }
}
