package com.dubylon.photochaos.rest.user;

import com.dubylon.photochaos.model.db.FavoritePath;
import com.dubylon.photochaos.model.db.User;
import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.favorite.FilesystemFavoritesPostData;
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

public class UsersPostHandler implements IPhotoChaosHandler {

  public UsersPostHandler() {
  }

  @Override
  public UsersPostData handleRequest(HttpServletRequest request) throws PCHandlerError {
    String content = null;
    try {
      //TODO handle body reading
      content = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new PCHandlerError("ERROR_READING_REQUEST_CONTENT", e);
    }
    if (content == null || content.length() == 0) {
      throw new PCHandlerError("MISSING_CONTENT", "Path should be passed as json object in request body.");
    }

    ObjectMapper mapper = new ObjectMapper();
    JsonNode parsedObject = null;
    try {
      parsedObject = mapper.readTree(content);
    } catch (IOException e) {
      throw new PCHandlerError("ERROR_PARSING_REQUEST", e);
    }

    if (parsedObject == null) {
      throw new PCHandlerError("NULL_REQUEST", "Request is empty");
    }

    JsonNode displayNameNode = parsedObject.get("displayName");
    if (displayNameNode == null) {
      throw new PCHandlerError("NO_DISPLAY_NAME_IN_REQUEST", "Display name is not present in request");
    }

    String displayName = displayNameNode.asText();

    User user = new User();
    user.setDisplayName(displayName);

    SessionFactory sessionFactory = null;
    try {
      sessionFactory = HibernateUtil.getSessionFactory();
    } catch (Exception e) {
      throw new PCHandlerError("ERROR_CONNECTING_TO_DATASTORE", e);
    }

    UsersPostData response = new UsersPostData();

    Session session = sessionFactory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      session.save(user);
      tx.commit();
      response.setId(user.getId());
      response.setCreatedObject(user);
      return response;
    } catch (HibernateException e) {
      e.printStackTrace();
      if (tx != null) {
        tx.rollback();
      }
      throw new PCHandlerError("ERROR_WHILE_SAVING", e);
    } finally {
      session.close();
    }
  }

}
