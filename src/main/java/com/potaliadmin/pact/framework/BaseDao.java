package com.potaliadmin.pact.framework;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate4.HibernateTemplate;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Created by Shakti Singh on 10/3/14.
 */
public interface BaseDao {

  /**
   * @param key
   * @return
   */
  public <T> T get(Class<T> c, Serializable key);

  /**
   * @return
   */
  public <T> List<T> getAll(Class<T> c);

  /**
   * @param c
   * @param key
   * @return
   */
  public <T> T load(Class<T> c, Serializable key);

  /**
   * @param c
   * @param key
   * @return
   */
  public <T> List<T> loadAll(Class<T> c);

  /**
   * @param <T>
   * @param c
   * @param entity
   */
  public void update(Object entity);

  /**
   * @param entity
   * @return
   */
  public Object save(Object entity);

  /**
   * @param c
   * @param entity
   */
  public void saveOrUpdate(Object entity);

  /**
   * @param <T>
   * @param c
   * @param key
   */
  public void delete(Object entity);

  /**
   * @param entities
   */

  @SuppressWarnings("unchecked")
  public void deleteAll(Collection entities);

  /**
   * @param query
   * @return
   */
  @SuppressWarnings("unchecked")
  public List findByQuery(String query);

  /**
   * @param query
   * @param bindings
   * @return
   */
  @SuppressWarnings("unchecked")
  public List findByQuery(String query, Object[] bindings);


  /**
   * @param queryString
   * @return
   */
  public Object findUnique(String queryString);

  /**
   * @param query
   * @param binds
   * @return
   */
  public Object findUnique(String query, Object[] binds);


  /**
   * @param queryString
   * @param params
   * @param values
   * @return
   */
  public Object findUniqueByNamedParams(String queryString, String[] params, Object[] values);

  /**
   * Finds the objects within the range defined by the start row and max row
   *
   * @param query
   * @param bindings
   * @param startRow
   * @param maxRows
   * @return
   */
  @SuppressWarnings("unchecked")
  public List find(String query, Object[] bindings, int startRow, int maxRows);


  /**
   * @param queryString
   * @param paramNames
   * @param paramValues
   * @return
   */
  public Object findUniqueByNamedQueryAndNamedParam(String queryString, String[] paramNames, Object[] paramValues);


  /**
   * @param namedQuery
   * @return
   */
  public List findByNamedQuery(String namedQuery);

  /**
   * @param object
   * @return
   */
  public <T> List<T> findByExample(T object);

  /**
   * @param entities the persistent instances to save or update (to be associated with the Hibernate Session)
   * @throws org.springframework.dao.DataAccessException in case of Hibernate errors
   * @see org.hibernate.Session#saveOrUpdate(Object)
   */
  @SuppressWarnings("unchecked")
  void saveOrUpdate(Collection entities) throws DataAccessException;

  /**
   * Execute a query based on a given Hibernate criteria object.
   *
   * @param criteria the detached Hibernate criteria object, which can for example be held in an instance variable of
   *                 a DAO
   * @return a List containing 0 or more persistent instances
   * @throws org.springframework.dao.DataAccessException
   *          in case of Hibernate errors
   * @see org.hibernate.criterion.DetachedCriteria#getExecutableCriteria(org.hibernate.Session)
   */
  List findByCriteria(DetachedCriteria criteria) throws DataAccessException;

  /**
   * Execute a query based on a given Hibernate criteria object.
   *
   * @param criteria    the detached Hibernate criteria object, which can for example be held in an instance variable of
   *                    a DAO
   * @param firstResult the index of the first result object to be retrieved (numbered from 0)
   * @param maxResults  the maximum number of result objects to retrieve (or <=0 for no limit)
   * @return a List containing 0 or more persistent instances
   * @throws org.springframework.dao.DataAccessException
   *          in case of Hibernate errors
   * @see org.hibernate.criterion.DetachedCriteria#getExecutableCriteria(org.hibernate.Session)
   * @see org.hibernate.Criteria#setFirstResult(int)
   * @see org.hibernate.Criteria#setMaxResults(int)
   */
  List findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) throws DataAccessException;


  List findForPageByCriteria(DetachedCriteria criteria, int pageNo, int perPage) throws DataAccessException;

  List findForPageByCriteria(DetachedCriteria criteria, boolean hasDistinctRootEntity, int pageNo, int perPage) throws DataAccessException;

  /**
   * @return All objects with id in ids
   */
  public <T> List<T> getAll(Class<T> c, List<Long> ids, String idPropertyName);

  /**
   * Validates whether an object with the specified key of the same class type already exists in the current session
   *
   * @param entityClass
   * @param key
   * @return
   */
  @SuppressWarnings("unchecked")
  public boolean containsKey(final Class entityClass, final Serializable key);

  /**
   * Validates whether an object with the specified key of the same hibernate type already exists in the current
   * session
   *
   * @param entityName
   * @param key
   * @return
   */
  public boolean containsKey(final String entityName, final Serializable key);

  public <T> T findDataObject(Class<T> dataObjectClass, String[] propertyNames, Object[] values);

  public <T> List<T> findDataObjects(Class<T> dataObjectClass, String[] propertyNames, Object[] values);

  public <T> T getByQuery(String query, Object... parameters);

  public int executeNativeSql(final String sql, final Object... params);



  public List<Object[]> findByNativeSql(final String sql, final int start, final int rows, final Object... params);

  /**
   * Executes Native SQL Count Query
   *
   * @param sql
   * @param params
   * @return count
   */
  public int countByNativeSql(final String sql, final Object... params);

  public long countByNamedParams(String queryString, String[] params, Object[] values);

  public List findByNamedQueryAndNamedParam(String namedQuery, String[] paramNames, Object[] paramValues);

  public HibernateTemplate getHibernateTemplate();


  public Query createQuery(String queryString);

  /**
   * Flush the session
   */
  public void flush();

  /**
   * Executes HQL Count Query
   *
   * @param sql
   * @param params
   * @return
   */
  public long count(String sql, Object[] params);


  public void refresh(Object entity);


}
