/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/hierarchy/HierarchyService.java,v $
 */

package com.biperf.core.service.hierarchy;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.HierarchyDetail;

/**
 * HierarchyService.
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
 *
 */
public interface HierarchyService extends SAO
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /** * BEAN_NAME is the reference for spring ** */
  public static final String BEAN_NAME = "hierarchyService";

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Deletes the given hierarchy.
   * 
   * @param hierarchy the hierarchy to delete.
   */
  void delete( Hierarchy hierarchy ) throws ServiceErrorException;

  /**
   * Returns a list of active, not deleted hierarchies.
   * 
   * @return a list of active, not deleted hierarchies, as a <code>List</code> of
   *         {@link com.biperf.core.domain.hierarchy.Hierarchy} objects.
   */
  List getActiveHierarchies();

  /**
   * Returns a list of all not deleted hierarchies. The returned hierarchies may be either active or
   * not active.
   * 
   * @return a list of not deleted hierarchies, as a <code>List</code> of
   *         {@link com.biperf.core.domain.hierarchy.Hierarchy} objects.
   */
  List getAll();

  /**
   * Returns a hierarchy by ID.
   * 
   * @param id the ID of the hierarchy to fetch.
   * @return the specified hierarchy.
   */
  Hierarchy getById( Long id );

  /**
   * Returns the primary hierarchy.
   * 
   * @return the primary hierarchy.
   */
  Hierarchy getPrimaryHierarchy();

  /**
   * Save or update the given hierarchy.
   * 
   * @param hierarchy the hierarchy to save or update.
   * @return the saved/updated version of the given hierarchy.
   * @throws ServiceErrorException
   */
  Hierarchy save( Hierarchy hierarchy ) throws ServiceErrorException;

  /**
   * Verifies the specified hierarchy import file a page at a time.
   * 
   * @param importFileId
   * @param loadType
   * @param hierarchyId
   * @param userId
   * @return Map result set
   */
  public Map verifyImportFile( Long importFileId, Long hierarchyId, Long userId );

  /**
   * Import the specified hierarchy import file a page at a time.
   * 
   * @param importFileId
   * @param loadType
   * @param hierarchyId
   * @param userId
   * @return Map result set
   */
  public Map importImportFile( Long importFileId, Long hierarchyId, Long userId );

  public Long getHierarchyIdByRosterHierarchyId( UUID rosterHierarchyId );

  public UUID getRosterHierarchyIdByHierarchyId( Long hierarchyId );

  public List<HierarchyDetail> getHierarchyDetailsByUserId( Long userId );

  Hierarchy getHierarchyByRosterHierarchyId( UUID rosterHierarchyId );
}
