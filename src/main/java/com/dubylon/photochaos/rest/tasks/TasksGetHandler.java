package com.dubylon.photochaos.rest.tasks;

import com.dubylon.photochaos.model.db.FavoritePath;
import com.dubylon.photochaos.model.db.TaskDefinition;
import com.dubylon.photochaos.model.db.User;
import com.dubylon.photochaos.rest.IPhotoChaosHandler;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.generic.AbstractPCHandler;
import com.dubylon.photochaos.rest.users.UsersGetData;
import com.dubylon.photochaos.util.HibernateUtil;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class TasksGetHandler extends AbstractPCHandler {

  public TasksGetHandler() {
  }

  @Override
  public TasksGetData handleRequest(HttpServletRequest request) throws PCHandlerError {

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
          .add(Restrictions.eq("owner", ownUser));
      List list = crit.list();
      tx.commit();
      TasksGetData response = new TasksGetData();
      response.setTasks(list);
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
