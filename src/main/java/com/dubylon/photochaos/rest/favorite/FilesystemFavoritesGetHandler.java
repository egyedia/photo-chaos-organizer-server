package com.dubylon.photochaos.rest.favorite;

import com.dubylon.photochaos.model.db.FavoritePath;
import com.dubylon.photochaos.model.db.User;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.generic.AbstractPCHandler;
import com.dubylon.photochaos.util.HibernateUtil;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class FilesystemFavoritesGetHandler extends AbstractPCHandler {

  public FilesystemFavoritesGetHandler() {
  }

  @Override
  public FilesystemFavoritesGetData handleRequest(HttpServletRequest request) throws PCHandlerError {

    SessionFactory sessionFactory = null;
    try {
      sessionFactory = HibernateUtil.getSessionFactory();
    } catch (Exception e) {
      throw new PCHandlerError("ERROR_CONNECTING_TO_DATASTORE", e);
    }

    long userId = getUserId(request);

    Session session = sessionFactory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      User ownUser = (User) session.get(User.class, userId);
      Criteria crit = session.createCriteria(FavoritePath.class)
          .add(Restrictions.eq("owner", ownUser));
      List list = crit.list();
      tx.commit();
      FilesystemFavoritesGetData response = new FilesystemFavoritesGetData();
      response.setFavorites(list);
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
