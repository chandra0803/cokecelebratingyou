/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/process/hibernate/ProcessInvocationDAOImpl.java,v $
 */

package com.biperf.core.dao.process.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.process.ProcessInvocationDAO;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessInvocation;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * ProcessInvocationDAOImpl.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>leep</td>
 * <td>November 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ProcessInvocationDAOImpl extends BaseDAO implements ProcessInvocationDAO
{

  public static final String BEAN_NAME = "processInvocationDAO";

  /**
   * Get the process from the database by the id.
   * 
   * @param id
   * @return ProcessInvocation
   */
  public ProcessInvocation getProcessInvocationById( Long id )
  {

    ProcessInvocation processInvocation = (ProcessInvocation)getSession().get( ProcessInvocation.class, id );

    return processInvocation;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.promotion.PromotionDAO#getPromotionByIdWithAssociations(java.lang.Long,
   *      com.biperf.core.service.AssociationRequestCollection)
   * @param id
   * @param associationRequestCollection
   * @return Promotion
   */
  public ProcessInvocation getProcessInvocationByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    Session session = HibernateSessionManager.getSession();
    ProcessInvocation processInvocation = (ProcessInvocation)session.get( ProcessInvocation.class, id );

    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( processInvocation );
    }

    return processInvocation;
  }

  /**
   * Get the process invocations from the database by the process.
   * 
   * @param process
   * @return ProcessInvocation
   */
  public List getProcessInvocationsByProcess( Process process )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.process.ProcessInvocationByProcess" );
    query.setParameter( "process", process );

    return query.list();
  }

  public int getProcessInvocationCountByProcess( Process process )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.process.ProcessInvocationCountByProcess" );
    query.setParameter( "process", process );
    Long value = (Long)query.uniqueResult();
    if ( null != value )
    {
      return value.intValue();
    }
    else
    {
      return 0;
    }
  }

  /**
   * Get the process invocations from the database by the process.
   * 
   * @param process
   * @param associationRequestCollection
   * @return ProcessInvocation
   */
  public List getProcessInvocationsByProcessWithAssociations( Process process, AssociationRequestCollection associationRequestCollection )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.process.ProcessInvocationByProcess" );
    query.setParameter( "process", process );
    List processInvocationList = query.list();

    /*
     * Iterator iter = processInvocationList.iterator(); while ( iter.hasNext() ) {
     * ProcessInvocation processInvocation = (ProcessInvocation)iter.next(); if (
     * associationRequestCollection != null ) { associationRequestCollection.process(
     * processInvocation ); } }
     */

    return processInvocationList;
  }

  public List getProcessInvocationsByProcessWithAssociations( Process process, AssociationRequestCollection associationRequestCollection, int pageNumber, int pageSize )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.process.ProcessInvocationByProcess" );
    query.setParameter( "process", process );
    query.setMaxResults( pageSize );
    if ( pageNumber > 1 )
    {
      query.setFirstResult( pageSize * ( pageNumber - 1 ) );
    }
    List processInvocationList = query.list();

    Iterator iter = processInvocationList.iterator();

    while ( iter.hasNext() )
    {
      ProcessInvocation processInvocation = (ProcessInvocation)iter.next();

      if ( associationRequestCollection != null )
      {
        associationRequestCollection.process( processInvocation );
      }
    }

    return processInvocationList;
  }

  /**
   * Saves the processInvocation to the database.
   * 
   * @param processInvocation
   * @return ProcessInvocation
   */
  public ProcessInvocation save( ProcessInvocation processInvocation )
  {

    return (ProcessInvocation)HibernateUtil.saveOrUpdateOrDeepMerge( processInvocation );
  }
}
