package com.dubylon.photochaos.rest.task;

import com.dubylon.photochaos.model.db.TaskDefinition;
import com.dubylon.photochaos.model.db.User;
import com.dubylon.photochaos.model.request.TaskCreate;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.generic.AbstractPCHandler;
import com.dubylon.photochaos.rest.tasks.TasksPostData;
import com.dubylon.photochaos.util.HibernateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TaskPutHandler extends AbstractPCHandler {

  public TaskPutHandler() {
  }

  @Override
  public TaskPutData handleRequest(HttpServletRequest request) throws PCHandlerError {

    String content = readNonEmptyContent(request, "TaskDefinition data should be passed in a json object in request " +
        "body.");

    ObjectMapper mapper = new ObjectMapper();

    TaskCreate tc = null;
    try {
      tc = mapper.readValue(content, TaskCreate.class);
    } catch (IOException e) {
      e.printStackTrace();
    }

    long id = extractIdFromPathInfo(request, "Task id");
    long userId = getUserId(request);

    SessionFactory sessionFactory = null;
    try {
      sessionFactory = HibernateUtil.getSessionFactory();
    } catch (Exception e) {
      throw new PCHandlerError("ERROR_CONNECTING_TO_DATASTORE", e);
    }

    TaskPutData response = new TaskPutData();

    Session session = sessionFactory.openSession();
    Transaction tx = null;
    try {
      tx = session.beginTransaction();

      User ownUser = (User) session.get(User.class, userId);
      Criteria crit = session.createCriteria(TaskDefinition.class)
          .add(Restrictions.eq("id", id))
          .add(Restrictions.eq("owner", ownUser));
      TaskDefinition task = (TaskDefinition) crit.uniqueResult();

      if (task != null) {
        task.setClassName(tc.getClassName());
        task.setParameters(tc.getParameters());
        task.setName(tc.getTaskName());

        session.update(task);
        tx.commit();
        response.setId(task.getId());
        response.setUpdatedObject(task);
      }

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
