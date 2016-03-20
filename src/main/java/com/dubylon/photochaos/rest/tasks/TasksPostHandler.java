package com.dubylon.photochaos.rest.tasks;

import com.dubylon.photochaos.model.db.TaskDefinition;
import com.dubylon.photochaos.model.db.User;
import com.dubylon.photochaos.model.request.TaskCreate;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.generic.AbstractPCHandler;
import com.dubylon.photochaos.util.HibernateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

public class TasksPostHandler extends AbstractPCHandler {

  public TasksPostHandler() {
  }

  @Override
  public TasksPostData handleRequest(HttpServletRequest request) throws PCHandlerError {
    String content = readNonEmptyContent(request, "TaskDefinition data should be passed in a json object in request " +
        "body.");

    ObjectMapper mapper = new ObjectMapper();
    /*JsonNode parsedObject = null;
    try {
      parsedObject = mapper.readTree(content);
    } catch (IOException e) {
      throw new PCHandlerError("ERROR_PARSING_REQUEST", e);
    }

    if (parsedObject == null) {
      throw new PCHandlerError("NULL_REQUEST", "Request is empty");
    }*/

    TaskCreate tc = null;
    try {
      tc = mapper.readValue(content, TaskCreate.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    long userId = getUserId(request);

    TaskDefinition td = new TaskDefinition();
    td.setClassName(tc.getClassName());
    td.setParameters(tc.getParameters());
    td.setName("Task @ " + new Date().toString());

    SessionFactory sessionFactory = null;
    try {
      sessionFactory = HibernateUtil.getSessionFactory();
    } catch (Exception e) {
      throw new PCHandlerError("ERROR_CONNECTING_TO_DATASTORE", e);
    }

    TasksPostData response = new TasksPostData();

    Session session = sessionFactory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();
      User owner = (User) session.get(User.class, userId);
      td.setOwner(owner);

      session.save(td);
      tx.commit();
      response.setId(td.getId());
      response.setCreatedObject(td);
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
