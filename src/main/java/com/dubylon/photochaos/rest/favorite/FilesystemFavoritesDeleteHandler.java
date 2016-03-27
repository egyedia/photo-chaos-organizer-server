package com.dubylon.photochaos.rest.favorite;

import com.dubylon.photochaos.model.db.FavoritePath;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.rest.generic.AbstractPCHandler;
import com.dubylon.photochaos.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;

public class FilesystemFavoritesDeleteHandler extends AbstractPCHandler {

  public FilesystemFavoritesDeleteHandler() {
  }

  @Override
  public FilesystemFavoritesDeleteData handleRequest(HttpServletRequest request) throws PCHandlerError {
    long id = extractIdFromPathInfo(request, "Favorite id");

    SessionFactory sessionFactory = null;
    try {
      sessionFactory = HibernateUtil.getSessionFactory();
    } catch (Exception e) {
      throw new PCHandlerError("ERROR_CONNECTING_TO_DATASTORE", e);
    }

    long userId = getUserId(request);

    FilesystemFavoritesDeleteData response = new FilesystemFavoritesDeleteData();

    Session session = sessionFactory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      FavoritePath path = (FavoritePath) session.get(FavoritePath.class, id);
      if (path != null) {
        if (path.getOwner() != null && path.getOwner().getId() != userId) {
          throw new PCHandlerError("NOT_OWN_FAVORITE", "You can not delete just your favorites");
        }
        session.delete(path);
      } else {
        response.setResponseCode(PCHandlerResponse.NOT_FOUND);
      }
      tx.commit();
      return response;
    } catch (HibernateException e) {
      e.printStackTrace();
      if (tx != null) {
        tx.rollback();
      }
      throw new PCHandlerError("ERROR_WHILE_DELETING", e);
    } finally {
      session.close();
    }
  }

}
