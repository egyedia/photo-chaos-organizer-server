package com.dubylon.photochaos.dao;

import com.dubylon.photochaos.model.db.User;
import com.dubylon.photochaos.rest.PCHandlerError;
import com.dubylon.photochaos.rest.PCHandlerResponse;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

public class GenericObjectDao<T> {

  public T getById(long id, long userId, Class clazz) throws PCHandlerError {
    SessionFactory sessionFactory = AbstractDao.getSessionFactory();

    Session session = sessionFactory.openSession();
    Transaction tx = null;
    T tobj = null;
    try {
      tx = session.beginTransaction();

      User ownUser = (User) session.get(User.class, userId);
      Criteria crit = session.createCriteria(clazz)
          .add(Restrictions.eq("id", id))
          .add(Restrictions.eq("owner", ownUser));
      tobj = (T) crit.uniqueResult();
      tx.commit();
      if (tobj == null) {
        throw new PCHandlerError(PCHandlerResponse.NOT_FOUND, "NO_SUCH_OBJECT");
      } else {
        return tobj;
      }
    } catch (HibernateException e) {
      AbstractDao.rollback(tx, e, session);
    }
    return tobj;
  }
}
