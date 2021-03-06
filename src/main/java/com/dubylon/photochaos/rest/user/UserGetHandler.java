package com.dubylon.photochaos.rest.user;

import com.dubylon.photochaos.model.db.User;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.rest.generic.AbstractPCHandler;
import com.dubylon.photochaos.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;

public class UserGetHandler extends AbstractPCHandler {

  public UserGetHandler() {
  }

  @Override
  public UserGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    long id = extractIdFromPathInfo(request, "User id");

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
      User user = (User) session.get(User.class, id);
      tx.commit();
      if (user == null) {
        throw new PCHandlerError(PCHandlerResponse.NOT_FOUND, "NO_SUCH_USER");
      } else {
        UserGetData response = new UserGetData();
        response.setUser(user);
        return response;
      }
    } catch (HibernateException e) {
      e.printStackTrace();
      if (tx != null) {
        tx.rollback();
      }
      throw new PCHandlerError("ERROR_WHILE_READING", e);
    } finally {
      session.close();
    }
  }
}
