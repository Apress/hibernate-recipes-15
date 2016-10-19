package com.apress.hibernaterecipes.chapter10.recipe5;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Date;

public class AuditableInterceptor extends EmptyInterceptor {
  @Override
  public boolean onSave(Object entity, Serializable id,
                        Object[] state, String[] propertyNames,
                        Type[] types) {
    if (entity instanceof Auditable) {
      for (int i = 0; i < propertyNames.length; i++) {
        if ("createDate".equals(propertyNames[i])) {
          state[i] = new Date();
          return true;
        }
      }
    }
    return false;
  }
}
