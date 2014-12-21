package com.potaliadmin.impl.framework;

import com.potaliadmin.pact.framework.BaseDao;
import org.apache.commons.lang.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.PersistenceContext;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;

import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Shakti Singh on 10/3/14.
 */
@Repository
@Primary
@SuppressWarnings("unchecked")
public class BaseDaoImpl extends HibernateDaoSupport implements BaseDao {

  @Autowired
  public void init(@Qualifier("sessionFactory") SessionFactory factory,
                   @Qualifier("hibernateTemplate") HibernateTemplate hibernateTemplate) {
    setSessionFactory(factory);
    setHibernateTemplate(hibernateTemplate);

  }

  @SuppressWarnings("unchecked")
  public boolean containsKey(Class entityClass, Serializable key) {
    return containsKey(entityClass.getName(), key);
  }

  public boolean containsKey(final String entityName, final Serializable key) {
    Boolean contains = (Boolean) getHibernateTemplate().execute(new HibernateCallback() {
      public Object doInHibernate(Session session) throws HibernateException {
        SessionImplementor sessionImpl = (SessionImplementor) session;
        return containsKey(entityName, key, sessionImpl);
      }
    });
    return contains;
  }

  public void delete(Object entity) {
    //prepareHibernateForWrite();
    getHibernateTemplate().delete(entity);
    //resetHibernateAfterWrite();
  }

  @SuppressWarnings("unchecked")
  public void deleteAll(Collection entities) {
    //prepareHibernateForWrite();
    getHibernateTemplate().deleteAll(entities);
    //resetHibernateAfterWrite();
  }

  private boolean containsKey(String entityName, Serializable key, SessionImplementor sessionImpl) {
    // if key is null then there is no object possible
    if (key == null) {
      return false;
    }
    // get the entity persisted for this entity type
    EntityPersister persister = sessionImpl.getFactory().getEntityPersister(entityName);
    // create the possible entity key
    EntityKey keyToLoad = new EntityKey(key, persister);
    // then search in the persistence context
    PersistenceContext persistenceContext = sessionImpl.getPersistenceContext();
    return persistenceContext.containsEntity(keyToLoad);
  }

  @SuppressWarnings("unchecked")
  public List findByQuery(String queryString) {
    return getHibernateTemplate().find(queryString);
  }

  @SuppressWarnings("unchecked")
  public List findByQuery(String queryString, Object... bindings) {
    return getHibernateTemplate().find(queryString, bindings);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Object findUnique(String queryString) {
    List<Object> results = (List<Object>)getHibernateTemplate().find(queryString);

    if (results.size() == 1) {
      return results.get(0);
    }
    return null;
  }

  @Override
  public Query createQuery(String queryString) {
    return getSessionFactory().getCurrentSession().createQuery(queryString);
  }

  /*
  * @SuppressWarnings("unchecked") @Override
  */
  /*
  * public List findByNamedParams(String namedQuery, Map<String, Object> namedParams) { // create the query Query
  * query = getSession().createQuery(namedQuery); // set the bindings if (namedParams != null && namedParams.size() >
  * 0) { for (Map.Entry<String, Object> namedParamEntry : namedParams.entrySet()) {
  * query.setParameter(namedParamEntry.getKey(), namedParamEntry.getValue()); } } return query.list(); }
  */

  @SuppressWarnings("unchecked")
  public List find(String queryString, Object[] bindings, int startRow, int maxRows) {
    // create the query
    Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
    // set the bindings
    if (bindings != null) {
      for (int i = 0; i < bindings.length; i++) {
        query.setParameter(i, bindings[i]);
      }
    }
    // and the boundaries
    query.setFirstResult(startRow);
    if (maxRows != 0) {
      query.setMaxResults(maxRows);
    }
    return query.list();

  }

  public List findByNamedQuery(String namedQuery) {
    return getHibernateTemplate().findByNamedQuery(namedQuery);
  }

  @SuppressWarnings("unchecked")
  public List findByCriteria(DetachedCriteria criteria) throws DataAccessException {
    return getHibernateTemplate().findByCriteria(criteria);
  }

  @SuppressWarnings("unchecked")
  public List findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) throws DataAccessException {
    return getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults);
  }

  @Override
  public List findForPageByCriteria(DetachedCriteria criteria, int pageNo, int perPage) throws DataAccessException {
    return findForPageByCriteria(criteria, false, pageNo, perPage);
  }

  @Override
  public List findForPageByCriteria(DetachedCriteria criteria, boolean hasDistinctRootEntity, int pageNo, int perPage) throws DataAccessException {
    /*int totalResults = count(criteria, hasDistinctRootEntity);*/
    criteria.setProjection(null);
    if (hasDistinctRootEntity) {
      criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
    } else {
      criteria.setResultTransformer(Criteria.ROOT_ENTITY);
    }
    int firstResult = (pageNo - 1) * perPage;
    return findByCriteria(criteria, firstResult, perPage);
  }

  @SuppressWarnings({"unchecked", "hiding"})
  public <T> List<T> findByExample(T object) {
    return getHibernateTemplate().findByExample(object);
  }

  @SuppressWarnings("unchecked")
  public List findByNamedParams(String queryString, String[] params, Object[] values) {
    return getHibernateTemplate().findByNamedParam(queryString, params, values);
  }

  public Object findUniqueByNamedParams(String queryString, String[] params, Object[] values) {
    List<Object> results = (List<Object>)getHibernateTemplate().findByNamedParam(queryString, params, values);

    if (results.size() == 1) {
      return results.get(0);
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  public Object findUnique(String queryString, Object[] binds) {

    List<Object> results = (List<Object>)getHibernateTemplate().find(queryString, binds);

    if (results.size() == 1) {
      return results.get(0);
    }

    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Object findUniqueByNamedQueryAndNamedParam(String queryString, String[] paramNames, Object[] paramValues) {
    List<Object> results = (List<Object>)getHibernateTemplate().findByNamedQueryAndNamedParam(queryString, paramNames, paramValues);

    if (results.size() == 1) {
      return results.get(0);
    }

    return null;
  }

  @Override
  public List findByNamedQueryAndNamedParam(String namedQuery, String[] paramNames, Object[] paramValues) {
    return getHibernateTemplate().findByNamedQueryAndNamedParam(namedQuery, paramNames, paramValues);
  }

  @SuppressWarnings({"unchecked", "hiding"})
  public <T> T get(Class<T> c, Serializable key) {
    return (T) getHibernateTemplate().get(c, key);
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> getAll(Class<T> c) {
    return (List<T>) getHibernateTemplate().find("from " + c.getName());
  }

  public <T> List<T> getAll(Class<T> c, List<Long> ids, String idPropertyName) {
    if (ids == null || ids.isEmpty()) {
      return Collections.EMPTY_LIST;
    }
    DetachedCriteria criteria = DetachedCriteria.forClass(c).add(Restrictions.in(idPropertyName, ids));

    return findByCriteria(criteria);
  }

  @SuppressWarnings("unchecked")
  public <T> T load(Class<T> c, Serializable key) {
    return (T) getHibernateTemplate().load(c, key);
  }

  @SuppressWarnings("unchecked")
  public <T> List<T> loadAll(Class<T> c) {
    return (List<T>) getHibernateTemplate().loadAll(c);
  }

  public Object save(Object entity) {
    //prepareHibernateForWrite();
    Object updatedEntity = getHibernateTemplate().merge(entity);
    //resetHibernateAfterWrite();
//    testSave() ;
    return updatedEntity;
  }

/*    private void prepareHibernateForWrite() {
        getHibernateTemplate().getSessionFactory().getCurrentSession().setFlushMode(FlushMode.AUTO);
        getHibernateTemplate().setCheckWriteOperations(false);
    }

    private void resetHibernateAfterWrite() {
        getHibernateTemplate().flush();
        getHibernateTemplate().getSessionFactory().getCurrentSession().setFlushMode(FlushMode.MANUAL);
        getHibernateTemplate().setCheckWriteOperations(true);
    }*/

  public void saveOrUpdate(Object entity) {
    //prepareHibernateForWrite();
    getHibernateTemplate().saveOrUpdate(entity);
    //resetHibernateAfterWrite();

//     testSave() ;

  }

  /*private void testSave() {
      TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronizationAdapter() {
          @Override
          public void afterCommit() {
            int maxShoppingCartId = countByNativeSql("select max(id) from shopping_cart");
            int maxShoppingCartIdInPayment = countByNativeSql("select max(cart_id) from payment");

            if(maxShoppingCartId < maxShoppingCartIdInPayment){
              throw new RuntimeException("CHECK WITH DEV SHOPPING CART ID WRONG ");
            }

          }
        });
  }*/

  @SuppressWarnings("unchecked")
  public void saveOrUpdate(Collection entities) throws DataAccessException {
    //prepareHibernateForWrite();
    //getHibernateTemplate().saveOrUpdateAll(entities);
//    testSave() ;
    //resetHibernateAfterWrite();
  }

  public void update(Object entity) {
    //prepareHibernateForWrite();
    getHibernateTemplate().update(entity);
//    testSave() ;
    //resetHibernateAfterWrite();
  }

  public <T> T findDataObject(Class<T> dataObjectClass, String[] propertyNames, Object[] values) {
    StringBuilder sb = new StringBuilder();
    sb.append("from ").append(dataObjectClass.getName()).append(" where ");
    int i = 0;
    for (String propertyName : propertyNames) {
      i++;
      sb.append(propertyName).append(" = ?");
      if (i < propertyNames.length) {
        sb.append(" and ");
      }
    }

    return (T) getByQuery(sb.toString(), values);
  }

  public <T> T getByQuery(String query, Object... parameters) {
    List<T> results = findByQuery(query, parameters);
    return getFirst(results);
  }

  private <T> T getFirst(List<T> results) {
    if (results.size() != 1) {
      return null;
    }
    return results.get(0);
  }

  public <T> List<T> findDataObjects(Class<T> dataObjectClass, String[] propertyNames, Object[] values) {
    StringBuilder sb = new StringBuilder();
    sb.append("from ").append(dataObjectClass.getName()).append(" where ");
    int i = 0;
    for (String propertyName : propertyNames) {
      i++;
      sb.append(propertyName).append(" = ?");
      if (i < propertyNames.length) {
        sb.append(" and ");
      }
    }

    return (List<T>) findByQuery(sb.toString(), values);
  }

  public int executeNativeSql(final String sql, final Object... params) {
    return ((Integer) getHibernateTemplate().execute(new HibernateCallback() {

      @Override
      public Object doInHibernate(Session session) throws HibernateException {
        SQLQuery sqlQuery = session.createSQLQuery(sql);

        int i = 0;
        for (Object param : params) {
          sqlQuery.setParameter(i, param);
          i++;
        }

        return sqlQuery.executeUpdate();
      }

    })).intValue();
  }


  public List<Object[]> findByNativeSql(final String sql, final int start, final int rows, final Object... params) {
    return (List<Object[]>) getHibernateTemplate().execute(new HibernateCallback() {

      @Override
      public Object doInHibernate(Session session) throws HibernateException {
        SQLQuery sqlQuery = session.createSQLQuery("select * from (" + sql + ") t");

        int i = 0;
        for (Object param : params) {
          sqlQuery.setParameter(i, param);
          i++;
        }

        if (rows != 0) {
          sqlQuery.setMaxResults(rows);
        }
        sqlQuery.setFirstResult(start);

        return sqlQuery.list();
      }

    });
  }

  public List findByNativeSqlNoType(final String sql, final int start, final int rows, final Object... params) {
    return findByNativeSql(sql, start, rows, params);
  }

  protected String getSortType(String sortHow) {
    if (StringUtils.isBlank(sortHow)) {
      return "ASC";
    }

    if (StringUtils.equalsIgnoreCase("ASC", sortHow)) {
      return "ASC";
    }
    return "DESC";
  }

  @Override
  public int countByNativeSql(String sql, Object... params) {
    List result = findByNativeSql(sql, 0, 0, params);
    if (result.isEmpty() || result.get(0) == null) {
      return 0;
    }
    return ((Number) result.get(0)).intValue();
  }

  @SuppressWarnings("unchecked")
  @Override
  public long count(String sql, Object... params) {
    List result = findByQuery(sql, params);
    return ((Number) result.get(0)).longValue();
  }

  @SuppressWarnings("unchecked")
  @Override
  public long countByNamedParams(String queryString, String[] params, Object[] values) {
    List result = findByNamedParams(queryString, params, values);
    return ((Number) result.get(0)).longValue();
  }

  @Override
  public void flush() {
    getHibernateTemplate().flush();
  }

  @SuppressWarnings("unchecked")
  private int count(DetachedCriteria criteria, boolean hasDistinctRootEntity) {
    int totalResults = 0;
    if (hasDistinctRootEntity) {
      criteria.setProjection(Projections.countDistinct("id"));
    } else {
      criteria.setProjection(Projections.rowCount());
    }
    List totalResultsList = getHibernateTemplate().findByCriteria(criteria);
    totalResults = (Integer) totalResultsList.get(0);

    return totalResults;
  }

  @Override
  public void refresh(Object entity) {
    getHibernateTemplate().refresh(entity);
  }
}
