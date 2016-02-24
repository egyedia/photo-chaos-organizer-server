package com.dubylon.photochaos.rest.favorite;

import com.dubylon.photochaos.model.db.FavoritePath;
import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.util.HibernateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilesystemFavoritesDeleteHandler implements IPhotoChaosHandler {

  public FilesystemFavoritesDeleteHandler() {
  }

  @Override
  public FilesystemFavoritesDeleteData handleRequest(HttpServletRequest request) throws PCHandlerError {
    String pathInfo = request.getPathInfo();
    if (pathInfo == null || pathInfo.length() <= 1) {
      throw new PCHandlerError("MISSING_ID", "Favorite id should be specified in the path.");
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

    FilesystemFavoritesDeleteData response = new FilesystemFavoritesDeleteData();

    Session session = sessionFactory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      Object o = session.get(FavoritePath.class, id);
      if (o != null) {
        session.delete(o);
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
