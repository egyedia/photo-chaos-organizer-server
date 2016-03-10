package com.dubylon.photochaos.rest.user;

import com.dubylon.photochaos.model.db.User;
import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.rest.users.UsersGetData;
import com.dubylon.photochaos.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.NoSuchFileException;
import java.util.List;

public class UserGetHandler implements IPhotoChaosHandler {

  public UserGetHandler() {
  }

  @Override
  public UserGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    //TODO have this code in util method
    String pathInfo = request.getPathInfo();
    if (pathInfo == null || pathInfo.length() <= 1) {
      throw new PCHandlerError("MISSING_ID", "User id should be specified in the path.");
    }
    pathInfo = pathInfo.substring(1);
    long id = 0;
    try {
      id = Long.parseLong(pathInfo);
    } catch (Exception ex) {
      throw new PCHandlerError("INVALID_ID", ex);
    }

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
