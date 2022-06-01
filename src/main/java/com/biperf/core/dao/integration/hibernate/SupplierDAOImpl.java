/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/integration/hibernate/SupplierDAOImpl.java,v $
 */

package com.biperf.core.dao.integration.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.biperf.core.dao.integration.SupplierDAO;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * SupplierDAOImpl.
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
 * <td>sedey</td>
 * <td>Jun 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class SupplierDAOImpl implements SupplierDAO
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.integration.SupplierDAO#getSupplierById(java.lang.Long)
   * @param id
   * @return Supplier
   */
  public Supplier getSupplierById( Long id )
  {
    Session session = HibernateSessionManager.getSession();
    return (Supplier)session.get( Supplier.class, id );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.integration.SupplierDAO#getSupplierByName(java.lang.String)
   * @param supplierName
   * @return Supplier
   */
  public Supplier getSupplierByName( String supplierName )
  {
    Session session = HibernateSessionManager.getSession();
    return (Supplier)session.getNamedQuery( "com.biperf.core.domain.supplier.SupplierByName" ).setString( "supplierName", supplierName ).uniqueResult();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.integration.SupplierDAO#getAll()
   * @return List
   */
  public List getAll()
  {
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "com.biperf.core.domain.supplier.AllSupplierList" ).list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.integration.SupplierDAO#getAll()
   * @return List
   */
  public List getAllActive()
  {
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "com.biperf.core.domain.supplier.AllActiveSupplierList" ).list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.integration.SupplierDAO#saveSupplier(com.biperf.core.domain.supplier.Supplier)
   * @param supplier
   * @return Supplier
   */
  public Supplier saveSupplier( Supplier supplier )
  {
    return (Supplier)HibernateUtil.saveOrUpdateOrShallowMerge( supplier );
  }

  public Long getNumberOfAssociationsForSupplier( Long supplierCode )
  {
    Query query = HibernateSessionManager.getSession().getNamedQuery( "com.biperf.core.domain.supplier.getNumberOfAssociationsForSupplier" );

    query.setParameter( "supplierCode", supplierCode );

    return (Long)query.uniqueResult();
  }

}
