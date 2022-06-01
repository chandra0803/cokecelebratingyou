/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/hierarchy/hibernate/HierarchyDAOImpl.java,v $
 */

package com.biperf.core.dao.hierarchy.hibernate;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.jdbc.core.JdbcTemplate;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.hierarchy.HierarchyDAO;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.value.HierarchyDetail;

/**
 * HierarchyDAOImpl.
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
 * <td>kumars</td>
 * <td>Apr 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class HierarchyDAOImpl extends BaseDAO implements HierarchyDAO
{

  private DataSource dataSource;

  private JdbcTemplate jdbcTemplate;

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  /**
   * Deletes the hierarchy from the database. Overridden from
   * 
   * @see com.biperf.core.dao.hierarchy.HierarchyDAO#delete(com.biperf.core.domain.hierarchy.Hierarchy)
   * @param hierarchy
   */
  public void delete( Hierarchy hierarchy )
  {
    getSession().delete( hierarchy );
  }

  /**
   * Returns a list of active, not deleted hierarchies.
   * 
   * @return a list of active, not deleted hierarchies, as a <code>List</code> of
   *         {@link com.biperf.core.domain.hierarchy.Hierarchy} objects.
   */
  public List getActiveHierarchies()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.hierarchy.ActiveHierarchies" );

    return query.list();
  }

  /**
   * Retrieves all hierarchies from the database.
   * 
   * @return List a list of hierarchies
   */
  public List getAll()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.hierarchy.AllHierarchies" );

    return query.list();
  }

  /**
   * Get the hierarchy from the database by the id.
   * 
   * @param id
   * @return Hierarchy
   */
  public Hierarchy getById( Long id )
  {
    return (Hierarchy)getSession().get( Hierarchy.class, id );
  }

  /**
   * Retrieves the primary hierarchy
   * 
   * @return Hierarchy
   */
  public Hierarchy getPrimaryHierarchy()
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.hierarchy.PrimaryHierarchy" );

    return (Hierarchy)query.uniqueResult();
  }

  /**
   * Saves the hierarchy to the database.
   * 
   * @param hierarchy
   * @return Hierarchy
   */
  public Hierarchy save( Hierarchy hierarchy )
  {
    Session session = getSession();

    Hierarchy primaryHierarchy = getPrimaryHierarchy();

    // TODO this logic should be moved to the service class. I have it working properly for now, but
    // this needs refactoring

    // We can only do a merge if we are updating the primary one.
    if ( primaryHierarchy == null )
    {
      hierarchy.setPrimary( true );
      session.saveOrUpdate( hierarchy );
    }
    else if ( primaryHierarchy.getId().equals( hierarchy.getId() ) )
    {
      hierarchy.setPrimary( true );
      session.merge( hierarchy );
    }
    else
    {
      hierarchy.setPrimary( false );
      session.saveOrUpdate( hierarchy );
    }

    return hierarchy;
  }

  /**
   * Calls the stored procedure to verify hierarchy file
   * 
   * @param importFileId
   * @param loadType
   * @param hierarchyId
   * @param userId
   * @return Map result set
   */
  public Map verifyImportFile( Long importFileId, String loadType, Long hierarchyId, Long userId )
  {
    CallPrcHierarchyVerifyImport hierarchyVerifyProc = new CallPrcHierarchyVerifyImport( dataSource );
    return hierarchyVerifyProc.executeProcedure( importFileId, loadType, hierarchyId, userId );
  }

  @Override
  public Long getHierarchyIdByRosterHierarchyId( UUID rosterHierarchyId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.hierarchy.getHierarchyIdByRosterHierarchyId" );
    query.setParameter( "rosterHierarchyId", rosterHierarchyId.toString() );

    return (Long)query.uniqueResult();
  }

  @Override
  public UUID getRosterHierarchyIdByHierarchyId( Long hierarchyId )
  {

    Query query = getSession().getNamedQuery( "com.biperf.core.domain.hierarchy.getRosterHierarchyIdByHierarchyId" );
    query.setParameter( "hierarchyId", hierarchyId );
    return (UUID)query.uniqueResult();
  }

  @Override
  public List<HierarchyDetail> getHierarchyDetailsByUserId( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.hierarchy.getHierarchyDetailsByUserId" );
    query.setParameter( "userId", userId );
    query.setResultTransformer( Transformers.aliasToBean( HierarchyDetail.class ) );
    return (List<HierarchyDetail>)query.list();
  }

  public Hierarchy getHierarchyByRosterHierarchyId( UUID rosterHierarchyId )
  {
    Criteria searchCriteria = getSession().createCriteria( Hierarchy.class );
    searchCriteria.add( Restrictions.eq( "rosterHierarchyId", rosterHierarchyId ) );
    return (Hierarchy)searchCriteria.uniqueResult();
  }

}
