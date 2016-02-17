package com.dubylon.photochaos.handler;

import com.dubylon.photochaos.handler.iface.IPhotoChaosHandler;
import com.dubylon.photochaos.model.FavoritePath;
import com.dubylon.photochaos.util.HibernateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FilesystemFavoritesHandler implements IPhotoChaosHandler {

  @Override
  public PCResponseObject doGet(HttpServletRequest request) {
    return PCResponseObject.methodNotAllowed();
  }

  @Override
  public PCResponseObject doPost(HttpServletRequest request) {
    String content = null;
    try {
      content = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (content == null || content.length() == 0) {
      return PCResponseObject.error("MISSING_CONTENT", "Path should be passed as json object in request body.");
    } else {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode parsedObject = null;
      try {
        parsedObject = mapper.readTree(content);
      } catch (IOException e) {
        return PCResponseObject.error("ERROR_PARSING_REQUEST", e);
      }
      if (parsedObject == null) {
        return PCResponseObject.error("NULL_REQUEST");
      } else {
        JsonNode pathNode = parsedObject.get("path");
        if (pathNode == null) {
          return PCResponseObject.error("NO_PATH_IN_REQUEST");
        } else {
          //TODO handle base64 decoding in one step
          String pathEncoded = pathNode.asText();
          String path = StringUtils.newStringUtf8(Base64.decodeBase64(pathEncoded));

          FavoritePath fp = new FavoritePath();
          fp.setPath(path);
          fp.setTitle(path);
          System.out.println("save new favorite path:" + path);

          SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

          Session session = sessionFactory.openSession();
          Transaction tx = null;
          try {
            tx = session.beginTransaction();
            session.save(fp);
            tx.commit();
            //TODO create favorite location here
            return PCResponseObject.created("path-to-favorite/" + fp.getId(), fp);
          } catch (HibernateException e) {
            e.printStackTrace();
            if (tx != null) {
              tx.rollback();
            }
            return PCResponseObject.error("ERROR_WHILE_SAVING", e);
          } finally {
            session.close();
          }
        }
      }
    }
  }

  @Override
  public PCResponseObject doPut(HttpServletRequest request) {
    return null;
  }

  @Override
  public PCResponseObject doDelete(HttpServletRequest request) {
    return PCResponseObject.methodNotAllowed();
  }

}
