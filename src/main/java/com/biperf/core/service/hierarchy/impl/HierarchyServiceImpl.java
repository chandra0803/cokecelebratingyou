/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/hierarchy/impl/HierarchyServiceImpl.java,v $
 */

package com.biperf.core.service.hierarchy.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.biperf.core.dao.hierarchy.HierarchyDAO;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.participant.AutoCompleteService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.value.HierarchyDetail;

/**
 * HierarchyServiceImpl.
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
public class HierarchyServiceImpl implements HierarchyService
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  private static final String HIERARCHY_DATA_SECTION_NAME = "hierarchy_data";
  private static final String HIERARCHY_ASSET_NAME_SUFFIX = " Hierarchy";
  private static final String HIERARCHY_DATA_ASSET_TYPE_NAME = "_Hierarchy_Data";
  private static final String HIERARCHY_NAME_KEY = "HIERARCHY_NAME";
  private static final String HIERARCHY_DATA_ASSET_PREFIX = "hierarchy_data.hierarchy.";
  private static final String OUTPUT_RESULT_SET = "p_out_user_data";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private CMAssetService cmAssetService = null;
  private HierarchyDAO hierarchyDAO = null;

  // ---------------------------------------------------------------------------
  // Service Methods
  // ---------------------------------------------------------------------------

  /**
   * Deletes the given hierarchy.
   * 
   * @param hierarchy the hierarchy to delete.
   */
  public void delete( Hierarchy hierarchy ) throws ServiceErrorException
  {
    // Change the name of the hierarchy so that the name can be reused
    String hierarchyName = hierarchy.getName();
    Timestamp timestamp = new Timestamp( new Date().getTime() );
    hierarchy.setName( hierarchyName + "-" + timestamp );

    // update the CM asset to reflect the deletion and allow reuse of name
    CMDataElement cmDataElement = new CMDataElement( "Hierarchy Name", hierarchy.getNameCmKey(), hierarchy.getName(), true );

    cmAssetService.createOrUpdateAsset( HIERARCHY_DATA_SECTION_NAME, HIERARCHY_DATA_ASSET_TYPE_NAME, hierarchy.getName() + HIERARCHY_ASSET_NAME_SUFFIX, hierarchy.getCmAssetCode(), cmDataElement );

    hierarchy.setDeleted( true );
    this.hierarchyDAO.save( hierarchy );
  }

  /**
   * Returns a list of active, not deleted hierarchies.
   * 
   * @return a list of active, not deleted hierarchies, as a <code>List</code> of
   *         {@link com.biperf.core.domain.hierarchy.Hierarchy} objects.
   */
  public List getActiveHierarchies()
  {
    return hierarchyDAO.getActiveHierarchies();
  }

  /**
   * Returns a list of all not deleted hierarchies. The returned hierarchies may be either active or
   * not active.
   * 
   * @return a list of not deleted hierarchies, as a <code>List</code> of
   *         {@link com.biperf.core.domain.hierarchy.Hierarchy} objects.
   */
  public List getAll()
  {
    return this.hierarchyDAO.getAll();
  }

  /**
   * Returns a hierarchy by ID.
   * 
   * @param id the ID of the hierarchy to fetch.
   * @return the specified hierarchy.
   */
  public Hierarchy getById( Long id )
  {
    return this.hierarchyDAO.getById( id );
  }

  /**
   * Returns the primary hierarchy.
   * 
   * @return the primary hierarchy.
   */
  public Hierarchy getPrimaryHierarchy()
  {
    return this.hierarchyDAO.getPrimaryHierarchy();
  }

  /**
   * Save or update the given hierarchy.
   * 
   * @param hierarchy the hierarchy to save or update.
   * @return the saved/updated version of the given hierarchy.
   * @throws ServiceErrorException
   */
  public Hierarchy save( Hierarchy hierarchy ) throws ServiceErrorException
  {
    /*
     * TODO: add a business rule to check for unique hierarchy name; pending a decision on use of
     * code/name versus a unique name and support from CM
     */

    if ( hierarchy.getId() == null )
    {
      // set the cm asset and key fields for the new hierarchy
      hierarchy.setCmAssetCode( cmAssetService.getUniqueAssetCode( HIERARCHY_DATA_ASSET_PREFIX ) );
      hierarchy.setNameCmKey( HIERARCHY_NAME_KEY );
    }
    CMDataElement cmDataElement = new CMDataElement( "Hierarchy Name", hierarchy.getNameCmKey(), hierarchy.getName(), true );

    // to prevent Asset having too long of a name
    String assetName = hierarchy.getName();
    if ( assetName.length() + HIERARCHY_ASSET_NAME_SUFFIX.length() > 80 )
    {
      assetName = assetName.substring( 0, 80 - HIERARCHY_ASSET_NAME_SUFFIX.length() ) + HIERARCHY_ASSET_NAME_SUFFIX;
    }

    cmAssetService.createOrUpdateAsset( HIERARCHY_DATA_SECTION_NAME, HIERARCHY_DATA_ASSET_TYPE_NAME, assetName, hierarchy.getCmAssetCode(), cmDataElement );

    return this.hierarchyDAO.save( hierarchy );
  }

  /**
   * Verifies the specified hierarchy import file a page at a time.
   * 
   * @param importFileId
   * @param loadType
   * @param hierarchyId
   * @param userId
   * @return Map result set
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public Map verifyImportFile( Long importFileId, Long hierarchyId, Long userId )
  {
    Map output = hierarchyDAO.verifyImportFile( importFileId, "V", hierarchyId, userId );
    Object obj = output.get( OUTPUT_RESULT_SET );
    if ( obj instanceof List )
    {
      getAutoCompleteService().indexParticipants( (List)obj );
    }

    return output;
  }

  /**
   * Import the specified hierarchy import file a page at a time.
   * 
   * @param importFileId
   * @param loadType
   * @param hierarchyId
   * @param userId
   * @return Map result set
   */
  public Map importImportFile( Long importFileId, Long hierarchyId, Long userId )
  {
    return hierarchyDAO.verifyImportFile( importFileId, "L", hierarchyId, userId );
  }

  @Override
  public Long getHierarchyIdByRosterHierarchyId( UUID rosterHierarchyId )
  {
    return hierarchyDAO.getHierarchyIdByRosterHierarchyId( rosterHierarchyId );
  }

  @Override
  public UUID getRosterHierarchyIdByHierarchyId( Long hierarchyId )
  {
    return hierarchyDAO.getRosterHierarchyIdByHierarchyId( hierarchyId );
  }

  @Override
  public List<HierarchyDetail> getHierarchyDetailsByUserId( Long userId )
  {
    return hierarchyDAO.getHierarchyDetailsByUserId( userId );
  }
  
  public Hierarchy getHierarchyByRosterHierarchyId( UUID rosterHierarchyId )
  {
    return this.hierarchyDAO.getHierarchyByRosterHierarchyId( rosterHierarchyId );
  }

  // ---------------------------------------------------------------------------
  // Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Sets the Content Manager (CM) Asset service.
   * 
   * @param cmAssetService a reference to the CM Asset service.
   */
  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  /**
   * Sets the Hierarchy DAO.
   * 
   * @param hierarchyDAO a reference to the Hierarchy DAO.
   */
  public void setHierarchyDAO( HierarchyDAO hierarchyDAO )
  {
    this.hierarchyDAO = hierarchyDAO;
  }

  /**
   * @return
   */
  private static AutoCompleteService getAutoCompleteService()
  {
    return (AutoCompleteService)BeanLocator.getBean( AutoCompleteService.BEAN_NAME );
  }

}
