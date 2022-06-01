/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/dao/hierarchy/HierarchyDAO.java,v $
 */

package com.biperf.core.dao.hierarchy;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.value.HierarchyDetail;

/**
 * HierarchyDAO.
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
public interface HierarchyDAO extends DAO
{
  static final String BEAN_NAME = "hierarchyDAO";

  /**
   * Deletes the hierarchy from the database.
   * 
   * @param hierarchy
   */
  void delete( Hierarchy hierarchy );

  /**
   * Returns a list of active, not deleted hierarchies.
   * 
   * @return a list of active, not deleted hierarchies, as a <code>List</code> of
   *         {@link com.biperf.core.domain.hierarchy.Hierarchy} objects.
   */
  List getActiveHierarchies();

  /**
   * Retrieves all hierarchies from the database.
   * 
   * @return List a list of hierarchies
   */
  List getAll();

  /**
   * Get the hierarchy from the database by the id.
   * 
   * @param id
   * @return Hierarchy
   */
  Hierarchy getById( Long id );

  /**
   * Retrieves the primary hierarchy
   * 
   * @return Hierarchy
   */
  Hierarchy getPrimaryHierarchy();

  /**
   * Saves the hierarchy to the database.
   * 
   * @param hierarchy
   * @return Hierarchy
   */
  Hierarchy save( Hierarchy hierarchy );

  /**
   * Calls the stored procedure to verify hierarchy file
   * 
   * @param importFileId
   * @param loadType
   * @param hierarchyId
   * @param userId
   * @return Map result set
   */
  public Map verifyImportFile( Long importFileId, String loadType, Long hierarchyId, Long userId );

  public Long getHierarchyIdByRosterHierarchyId( UUID rosterHierarchyId );

  public UUID getRosterHierarchyIdByHierarchyId( Long hierarchyId );

  public List<HierarchyDetail> getHierarchyDetailsByUserId( Long userId );

  Hierarchy getHierarchyByRosterHierarchyId( UUID rosterHierarchyId );
}
