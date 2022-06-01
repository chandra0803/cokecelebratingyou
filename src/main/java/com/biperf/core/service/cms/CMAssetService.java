/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/cms/CMAssetService.java,v $
 */

package com.biperf.core.service.cms;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.diycommunication.ResourceList;
import com.objectpartners.cms.exception.CmsLiveDataException;

/**
 * CMAssetService.
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
 * <td>Apr 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface CMAssetService extends SAO
{
  /** BEAN_NAME */
  public static final String BEAN_NAME = "cmassetService";

  /**
   * Get a list of all PickListItem assets from cm.
   * 
   * @return List
   */
  public List getAllPickListItemAssets();

  /**
   * Get a unique asset name based on prefix and sequence.
   * 
   * @param prefix
   * @return String
   * @throws ServiceErrorException
   */
  public String getUniqueAssetCode( String prefix ) throws ServiceErrorException;

  /**
   * Get a unique key fragment based on sequence.
   * 
   * @return String
   * @throws ServiceErrorException
   */
  public String getUniqueKeyFragment() throws ServiceErrorException;

  /**
   * Copy the source Asset to the destination asset, including all metadata and content.
   * 
   * @param sourceAssetCode
   * @param destinationAssetCode
   * @param destinationAssetName
   * @param supersedingData
   * @param createNewAssetType
   * @param newAssetTypeName
   * @throws ServiceErrorException
   */
  public void copyCMAsset( String sourceAssetCode, String destinationAssetCode, String destinationAssetName, Map supersedingData, boolean createNewAssetType, String newAssetTypeName )
      throws ServiceErrorException;

  /**
   * Delete a CM asset by assetKey. Also deletes the CMType metadata associated with this asset.
   * 
   * @param assetKey
   */
  public void deleteCMAsset( String assetKey );

  /**
   * Create or Update Asset and data. This method saves a lot of code for CM integration.
   * 
   * @param sectionCode
   * @param assetTypeName
   * @param assetName
   * @param assetCode
   * @param cmDataElement
   * @throws ServiceErrorException
   */
  public void createOrUpdateAsset( String sectionCode, String assetTypeName, String assetName, String assetCode, CMDataElement cmDataElement ) throws ServiceErrorException;

  /**
   * Create or Update Asset and data. This method saves a lot of code for CM integration.
   * 
   * @param sectionCode
   * @param assetTypeName
   * @param assetName
   * @param assetCode
   * @param cmDataElements
   * @throws ServiceErrorException
   */
  public void createOrUpdateAsset( String sectionCode, String assetTypeName, String assetName, String assetCode, List cmDataElements ) throws ServiceErrorException;

  /**
   * Create or Update Asset and data. This method saves a lot of code for CM integration.
   * 
   * @param sectionCode
   * @param assetTypeName
   * @param assetName
   * @param assetCode
   * @param cmDataElements
   * @param locale
   * @throws ServiceErrorException
   */
  public void createOrUpdateAsset( String sectionCode, String assetTypeName, String assetName, String assetCode, List cmDataElements, Locale locale, List<String> audienceNames )
      throws ServiceErrorException;

  /**
   * Update the audience in cms with the new audience name from beacon.
   * 
   * @param oldAudienceName
   * @param audienceName
   * @throws ServiceErrorException
   */
  public void updateCmsAudience( String oldAudienceName, String audienceName ) throws ServiceErrorException;

  /**
   * Create a audience in cms based on a new beacon audience.
   * 
   * @param audienceName
   * @throws ServiceErrorException
   */
  public void createCmsAudience( String audienceName ) throws ServiceErrorException;

  /**
   * Add an item to an existing pickList.
   * 
   * @param pickListItemAssetCode
   * @param itemName
   * @throws ServiceErrorException
   */
  public void addPickListItem( String pickListItemAssetCode, String itemName ) throws ServiceErrorException;

  /**
   * Add an item to an existing pickList.
   * 
   * @param pickListItemAssetCode
   * @param itemCode
   * @param itemName
   * @throws ServiceErrorException
   */
  public void addPickListItem( String pickListItemAssetCode, String itemCode, String itemName ) throws ServiceErrorException;

  public List getSupportedLocales( boolean excludeReaderLocale );

  /**
   * Get a string using asset, key and locale. 
   * with a split asset, key code and locale. 
   * 
   * @param asset
   * @param key
   * @param locale
   * @return non-null string, empty if key exists on asset but with null value, will have question
   *         marks if asset doesn't have key
   */
  public String getString( String asset, String key, Locale locale, boolean defaultEmpty );

  /**
   * @param assetCode
   * @return List of contents(includes all languages) for the given assetCode.
   */
  @SuppressWarnings( "rawtypes" )
  public List getContentsForAsset( String assetCode );

  public void expireContents( String cmAssetCode, List<ResourceList> resourceList, String systemDefaultLocale ) throws CmsLiveDataException;

  /**
   * Remove content. Fires asset changed event.
   */
  public void removeContent( String assetCode, Long contentId ) throws CmsLiveDataException;

  /**
   * 
   * @param assetCode
   * @return List of audience for non Multi Assets. Which means multi is false for the asset.
   */
  public List<com.objectpartners.cms.domain.Audience> getAudienceForAsset( String assetCode );

  /**
   * @param sectionCode
   * @return List of assets for the given sectionCode.
   */
  @SuppressWarnings( "rawtypes" )
  public List getAssetsForSection( String sectionCode );

  public String getTextFromCmsResourceBundle( String key );

}
