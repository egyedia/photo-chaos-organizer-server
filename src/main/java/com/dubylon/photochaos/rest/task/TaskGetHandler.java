package com.dubylon.photochaos.rest.task;

import com.dubylon.photochaos.model.db.TaskDefinition;
import com.dubylon.photochaos.model.db.User;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import com.dubylon.photochaos.rest.generic.AbstractPCHandler;
import com.dubylon.photochaos.util.HibernateUtil;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;

public class TaskGetHandler extends AbstractPCHandler {

  public TaskGetHandler() {
  }

  @Override
  public TaskGetData handleRequest(HttpServletRequest request) throws PCHandlerError {
    long id = extractIdFromPathInfo(request, "Task id");

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
      Criteria crit = session.createCriteria(TaskDefinition.class)
          .add(Restrictions.eq("id", id))
          .add(Restrictions.eq("owner", ownUser));
      TaskDefinition task = (TaskDefinition)crit.uniqueResult();
      tx.commit();
      if (task == null) {
        throw new PCHandlerError(PCHandlerResponse.NOT_FOUND, "NO_SUCH_TASK");
      } else {
        TaskGetData response = new TaskGetData();
        response.setTask(task);
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
