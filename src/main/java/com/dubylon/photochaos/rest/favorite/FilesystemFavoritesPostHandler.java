package com.dubylon.photochaos.rest.favorite;

import com.dubylon.photochaos.model.db.FavoritePath;
import com.dubylon.photochaos.model.db.User;
import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
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
import java.util.List;

public class FilesystemFavoritesPostHandler implements IPhotoChaosHandler {

  public FilesystemFavoritesPostHandler() {
  }

  @Override
  public FilesystemFavoritesPostData handleRequest(HttpServletRequest request) throws PCHandlerError {
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

    JsonNode pathNode = parsedObject.get("path");
    if (pathNode == null) {
      throw new PCHandlerError("NO_PATH_IN_REQUEST", "Path is not present in request");
    }

    JsonNode userIdNode = parsedObject.get("userId");
    if (userIdNode == null) {
      throw new PCHandlerError("NO_USER_ID_IN_REQUEST", "UserId is not present in request");
    }

    //TODO handle base64 decoding in one step
    Long userId = userIdNode.asLong();
    String path = pathNode.asText();

    FavoritePath fp = new FavoritePath();
    fp.setPath(path);
    Path realPath = Paths.get(path);
    fp.setTitle(realPath.getFileName().toString());

    SessionFactory sessionFactory = null;
    try {
      sessionFactory = HibernateUtil.getSessionFactory();
    } catch (Exception e) {
      throw new PCHandlerError("ERROR_CONNECTING_TO_DATASTORE", e);
    }

    FilesystemFavoritesPostData response = new FilesystemFavoritesPostData();

    Session session = sessionFactory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();

      User owner = (User) session.get(User.class, userId);
      fp.setOwner(owner);

      session.save(fp);
      tx.commit();
      response.setId(fp.getId());
      response.setCreatedObject(fp);
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
