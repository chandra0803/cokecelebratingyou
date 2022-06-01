/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/cms/impl/CMAssetServiceImpl.java,v $
 */

package com.biperf.core.service.cms.impl;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.oracle.OracleSequenceDAO;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.NonUniqueDataServiceErrorException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.value.diycommunication.ResourceList;
import com.objectpartners.cms.domain.Asset;
import com.objectpartners.cms.domain.AssetType;
import com.objectpartners.cms.domain.AssetTypeItem;
import com.objectpartners.cms.domain.Audience;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.ContentKey;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.domain.enums.ContentStatusEnum;
import com.objectpartners.cms.exception.CmsDataValidationException;
import com.objectpartners.cms.exception.CmsLiveDataException;
import com.objectpartners.cms.exception.CmsServiceException;
import com.objectpartners.cms.exception.NonUniqueDataValidationException;
import com.objectpartners.cms.service.AssetService;
import com.objectpartners.cms.service.AudienceService;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.service.ContentService;
import com.objectpartners.cms.service.MetaDataService;
import com.objectpartners.cms.service.request.AssetFullyLoadedFetchGroupRequest;
import com.objectpartners.cms.service.request.AssetToAssetTypeItemsFetchGroupRequest;
import com.objectpartners.cms.service.request.AudienceToApplicationFetchGroupRequest;
import com.objectpartners.cms.service.request.ContentKeyToContentFetchGroupRequest;
import com.objectpartners.cms.service.request.ContentToAudiencesFetchGroupRequest;
import com.objectpartners.cms.service.request.FetchGroupRequestCollection;
import com.objectpartners.cms.util.CmsConfiguration;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * CMAssetServiceImpl.
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
public class CMAssetServiceImpl implements CMAssetService
{
  public static final String PICK_LIST_ITEM_ASSET_TYPE_NAME = "Picklist Item";
  public static final String DEFAULT_AUDIENCE_NAME = "Default Audience";

  private static final Log log = LogFactory.getLog( CMAssetServiceImpl.class );

  /** CMAssetDAO */
  private static final int MAX_ASSET_LENGTH = 80;
  private static final int MAX_KEY_LENGTH = 40;

  private OracleSequenceDAO oracleSequenceDAO;

  private AudienceService getCmsAudienceService()
  {
    return (AudienceService)ContentReaderManager.getContentReader().getApplicationContext().getBean( AudienceService.BEAN_NAME );
  }

  private AssetService getCmsAssetService()
  {
    return (AssetService)ContentReaderManager.getContentReader().getApplicationContext().getBean( AssetService.BEAN_NAME );
  }

  private MetaDataService getCmsMetaDataService()
  {
    return (MetaDataService)ContentReaderManager.getContentReader().getApplicationContext().getBean( MetaDataService.BEAN_NAME );
  }

  private ContentService getCmsContentService()
  {
    return (ContentService)ContentReaderManager.getContentReader().getApplicationContext().getBean( ContentService.BEAN_NAME );
  }

  /**
   * Get a unique asset name based on prefix and sequence.
   * 
   * @param prefix
   * @return String
   * @throws ServiceErrorException
   */
  public String getUniqueAssetCode( String prefix ) throws ServiceErrorException
  {
    if ( prefix == null || prefix.equals( "" ) )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_EMPTY_ASSET_PREFIX_ERROR ) );
      throw new ServiceErrorExceptionWithRollback( errors );
    }

    if ( !prefix.endsWith( "." ) )
    {
      prefix = prefix + '.';
    }

    String uniqueAssetName = prefix + getStringFromSequence();

    // the name of the asset can only be 80 characters....this is the size of the database column.
    if ( uniqueAssetName.length() > MAX_ASSET_LENGTH )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_GENERATED_ASSET_LENGTH_ERROR ) );
      throw new ServiceErrorExceptionWithRollback( errors );
    }
    return uniqueAssetName;
  }

  /**
   * Get a unique key fragment based on sequence.
   * 
   * @return String
   * @throws ServiceErrorException
   */
  public String getUniqueKeyFragment() throws ServiceErrorException
  {
    String uniqueKeyFragment = getStringFromSequence();

    // the name of the key can only be 40 characters....this is the size of the database column.
    if ( uniqueKeyFragment.length() > MAX_KEY_LENGTH )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_GENERATED_KEY_LENGTH_ERROR ) );
      throw new ServiceErrorExceptionWithRollback( errors );
    }
    return uniqueKeyFragment;
  }

  /**
   * Get a list of all PickListItem assets from cm. Overridden from
   * 
   * @see com.biperf.core.service.cms.CMAssetService#getAllPickListItemAssets()
   * @return List
   */
  public List getAllPickListItemAssets()
  {
    List assetList = new ArrayList();
    Collection pickListItems = ContentReaderManager.getContentReader().getAssetsByType( PICK_LIST_ITEM_ASSET_TYPE_NAME );
    for ( Iterator iterator = pickListItems.iterator(); iterator.hasNext(); )
    {
      assetList.add( iterator.next() );
    }

    return assetList;
  }

  /**
   * Delete a CM asset by assetKey. Also deletes the CMType metadata associated with this asset.
   * 
   * @param assetKey
   */
  public void deleteCMAsset( String assetKey )
  {
    // NOTE: Deleting Assets is NOT allowed in CM2 at this time. You can delete the CONTENT of an
    // Asset if it is in the EXPIRED or EDITED status however.

    // todo jjd migrate
  }

  /**
   * Get a string that represents the value of the CM sequence.
   * 
   * @return String
   */
  private String getStringFromSequence() throws ServiceErrorException
  {
    String unique = null;

    try
    {
      unique = String.valueOf( oracleSequenceDAO.getOracleSequenceNextValue( "CMS_UNIQUE_NAME_SQ" ) );
    }
    catch( BeaconRuntimeException e )
    {
      log.error( "BeaconRuntimeException getting sequence next value.", e );
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_FETCH_ERROR ) );
      throw new ServiceErrorExceptionWithRollback( errors, e );
    }

    return unique;

  }

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
      throws ServiceErrorException
  {
    // clone and save asset (which also clones the asset type, contents and any childAssets)
    Asset asset = getCmsAsset( sourceAssetCode );
    try
    {
      Asset newAsset = asset.deepCopy( asset.getSection().getApplication(), asset.getSection() );
      // copy the AssetType of the Asset being cloned so we don't save a duplicate
      newAsset.setAssetType( asset.getAssetType() );
      newAsset.setCode( destinationAssetCode );
      newAsset.setName( destinationAssetName );
      newAsset.setDescription( destinationAssetName );
      Iterator assetContentKeysIterator = newAsset.getContentKeys().iterator();
      while ( assetContentKeysIterator.hasNext() )
      {
        ContentKey contentKey = (ContentKey)assetContentKeysIterator.next();
        Iterator assetContentsIterator = contentKey.getContents().iterator();
        while ( assetContentsIterator.hasNext() )
        {
          Content content = (Content)assetContentsIterator.next();
          content.setContentStatus( ContentStatusEnum.LIVE );
        }
      }

      if ( createNewAssetType )
      {
        AssetType assetType = asset.getAssetType().deepCopy( asset.getSection().getApplication() );
        assetType.setName( newAssetTypeName );
        newAsset.setAssetType( assetType );
        getCmsMetaDataService().storeAssetType( asset.getSection().getApplication().getId(), assetType );
      }
      if ( supersedingData != null )
      {
        Iterator keySetIterator = supersedingData.keySet().iterator();
        while ( keySetIterator.hasNext() )
        {
          String key = (String)keySetIterator.next();
          Iterator contentKeyIterator = newAsset.getContentKeys().iterator();
          while ( contentKeyIterator.hasNext() )
          {
            ContentKey contentKey = (ContentKey)contentKeyIterator.next();
            Iterator contentIterator = contentKey.getContents().iterator();
            while ( contentIterator.hasNext() )
            {
              Content content = (Content)contentIterator.next();
              content.getContentDataMap().put( key, supersedingData.get( key ) );
              content.getContentDataMapList().put( key, new Translations( (String)supersedingData.get( key ) ) );
            }
          }
        }
      }
      getCmsAssetService().addAsset( asset.getSection().getId(), newAsset );
      // now save the contents - since there is no cascade in the hibernate mappings to content from
      // asset.
      Iterator contentKeyIterator = newAsset.getContentKeys().iterator();
      while ( contentKeyIterator.hasNext() )
      {
        ContentKey contentKey = (ContentKey)contentKeyIterator.next();
        Iterator contentIterator = contentKey.getContents().iterator();
        while ( contentIterator.hasNext() )
        {
          Content content = (Content)contentIterator.next();
          getCmsContentService().saveContent( content,
                                              new ArrayList( content.getContentKey().getAudiences() ),
                                              newAsset.getAssetType().getAssetTypeItems(),
                                              newAsset.getId(),
                                              newAsset.getAssetType().getId(),
                                              content.getContentDataMapList(),
                                              false );
        }
      }
    }
    catch( CloneNotSupportedException e )
    {
      log.error( "Exception copying CM Asset", e );
      throw new ServiceErrorExceptionWithRollback( ServiceErrorMessageKeys.CM_SERVICE_FETCH_ERROR, e );
    }
    catch( CmsDataValidationException e )
    {
      log.error( "Exception copying CM Asset", e );
      throw new ServiceErrorExceptionWithRollback( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR, e );
    }
    catch( CmsServiceException e )
    {
      log.error( "Exception copying CM Asset", e );
      throw new ServiceErrorExceptionWithRollback( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR, e );
    }
  }

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
  public void createOrUpdateAsset( String sectionCode, String assetTypeName, String assetName, String assetCode, List cmDataElements, Locale locale, List<String> audienceNames )
      throws ServiceErrorException
  {
    ContentReader reader = ContentReaderManager.getContentReader();
    if ( audienceNames == null )
    {
      audienceNames = ( (CmsConfiguration)reader.getApplicationContext().getBean( "cmsConfiguration" ) ).getDefaultAudienceNames();
    }
    List audienceList = new ArrayList();
    for ( int i = 0; i < audienceNames.size(); i++ )
    {
      String audienceName = (String)audienceNames.get( i );
      FetchGroupRequestCollection fetchGroupRequests = new FetchGroupRequestCollection();
      fetchGroupRequests.add( new AudienceToApplicationFetchGroupRequest() );
      Audience audience = getCmsAudienceService().findAudienceByName( audienceName, reader.getApplicationCode(), fetchGroupRequests );
      audienceList.add( audience );
    }
    boolean assetTypeChanged = false;
    AssetType assetType = getCmsMetaDataService().findAssetType( reader.getApplicationCode(), assetTypeName );
    if ( assetType == null )
    {
      // create new assetType..
      assetType = new AssetType( assetTypeName, assetTypeName );
      assetType.setMulti( false );
      assetType.setDisplay( true );
      assetType.setPublik( true );
      assetTypeChanged = true;
    }
    // now check if assetTypeItems contains key...
    for ( int i = 0; i < cmDataElements.size(); i++ )
    {
      CMDataElement cmDataElement = (CMDataElement)cmDataElements.get( i );
      if ( !isKeyDefined( cmDataElement.getKey(), assetType.getAssetTypeItems() ) )
      {
        AssetTypeItem item = new AssetTypeItem( cmDataElement.getKey(), cmDataElement.getLabel(), cmDataElement.getLabel(), cmDataElement.getDataTypeEnum(), 1 );
        item.setUnique( cmDataElement.isUnique() );
        item.setRequired( cmDataElement.isRequired() );
        assetType.addAssetTypeItem( item );
        assetTypeChanged = true;
      }
      AssetTypeItem item = getAssetTypeItem( cmDataElement.getKey(), assetType.getAssetTypeItems() );
      if ( item.isUnique() != cmDataElement.isUnique() || item.isRequired() != cmDataElement.isRequired() )
      {
        item.setUnique( cmDataElement.isUnique() );
        item.setRequired( cmDataElement.isRequired() );
        assetTypeChanged = true;
      }
    }
    try
    {
      if ( assetTypeChanged )
      {
        getCmsMetaDataService().storeAssetType( reader.getApplicationCode(), assetType );
      }
    }
    catch( CmsDataValidationException e )
    {
      // todo jjd define error message
      log.error( "Unable to maintain asset", e );
      throw new ServiceErrorExceptionWithRollback( "service.error.message", e );
    }

    // create asset if necessary
    Asset asset = getCmsAsset( assetCode );
    if ( asset == null )
    {
      // create new asset
      asset = new Asset( assetCode, assetName, assetName, assetType, null );
      try
      {
        getCmsAssetService().addAsset( reader.getApplicationCode(), sectionCode, asset );
      }
      catch( CmsDataValidationException e )
      {
        // todo jjd define error message
        log.error( "Unable to maintain asset", e );
        throw new ServiceErrorExceptionWithRollback( "service.error.message", e );
      }
      catch( CmsServiceException e )
      {
        // todo jjd define error message
        log.error( "Unable to maintain asset", e );
        throw new ServiceErrorExceptionWithRollback( "service.error.message", e );
      }
    }

    // avoid a stale object exception from hibernate.
    ContentReader contentReader = ContentReaderManager.getContentReader();
    contentReader.assetChangedEvent( assetCode );

    // create or update content
    Map dataMap = null;

    // Load content if it exists - get directly, not from reader cache (cache is not
    // sufficiently hydrated for this operation)
    List liveContentStatusList = new ArrayList();
    liveContentStatusList.add( ContentStatusEnum.LIVE );
    Object contentObject = getCmsContentService().getContent( assetCode, locale, liveContentStatusList, contentReader.getApplicationCode() );

    Content content = null;
    if ( contentObject == null )
    {
      contentObject = new Content();
    }

    ContentKey contentKey = null;
    if ( !asset.getContentKeys().isEmpty() )
    {
      contentKey = (ContentKey)asset.getContentKeys().iterator().next();
    }

    /*
     * G5 Requirement : Starting G5, we have requirements to handle multi asset. But at this point
     * in time, it is only to the need of able to add and edit first indexed asset.
     */
    if ( contentObject instanceof Content )
    {
      // content is defined - update it.
      content = (Content)contentObject;
    }
    else if ( contentObject instanceof List )
    {
      content = ! ( (List)contentObject ).isEmpty() ? (Content) ( (List)contentObject ).get( 0 ) : new Content();
    }

    if ( content != null )
    {
      if ( content.getContentStatus() == null )
      {
        // this is new content - create new content
        content.setLocale( locale );
        content.setVersion( 1 );
        content.getContentKey().setSortOrder( 1 );
        if ( contentKey != null )
        {
          content.setContentKey( contentKey );
        }
        dataMap = new HashMap();
      }
      else if ( content.getContentStatus().equals( ContentStatusEnum.LIVE ) )
      {
        // can't update LIVE content, so make a copy and set status to Edited
        Content newContent = content.deepCopy( content.getContentKey() );
        newContent.setVersion( content.getVersion() + 1 ); // increment version by 1
        newContent.setGuid( new UID().toString() );
        newContent.getContentKey().setAudiences( content.getContentKey().getAudiences() );
        content = newContent;
        dataMap = content.getContentDataMapList();
      }
      else
      {
        dataMap = content.getContentDataMapList();
      }
    }

    for ( int i = 0; i < cmDataElements.size(); i++ )
    {
      CMDataElement cmDataElement = (CMDataElement)cmDataElements.get( i );
      dataMap.put( cmDataElement.getKey(), new Translations( cmDataElement.getData() ) );
    }
    try
    {
      if ( content != null )
      {
        content.setContentStatus( ContentStatusEnum.LIVE );
      }
      getCmsContentService().saveContent( content, audienceList, assetType.getAssetTypeItems(), asset.getId(), assetType.getId(), dataMap, false );
    }
    catch( NonUniqueDataValidationException e )
    {
      log.error( "Unable to maintain asset", e );
      throw new NonUniqueDataServiceErrorException( "admin.characteristic.errors.DUPLICATE", e );
    }
    catch( CmsDataValidationException e )
    {
      // todo jjd define error message
      log.error( "Unable to maintain asset", e );
      throw new ServiceErrorExceptionWithRollback( "service.error.message", e );
    }
    // done.
  }

  /**
   * Update the audience in cms with the new audience name from beacon.
   * 
   * @param oldAudienceName
   * @param audienceName
   * @throws ServiceErrorException
   */
  public void updateCmsAudience( String oldAudienceName, String audienceName ) throws ServiceErrorException
  {
    ContentReader reader = ContentReaderManager.getContentReader();

    com.objectpartners.cms.domain.Audience cmsAudience = getCmsAudienceService().findAudienceByName( oldAudienceName, reader.getApplicationCode(), null );
    cmsAudience.setName( audienceName );
    cmsAudience.setDescription( audienceName );
    try
    {
      getCmsAudienceService().storeAudience( reader.getApplicationCode(), cmsAudience );
    }
    catch( CmsDataValidationException e )
    {
      // todo jjd define error message
      throw new ServiceErrorExceptionWithRollback( "service.error.msg", e );
    }
  }

  /**
   * Create a audience in cms based on a new beacon audience.
   * 
   * @param audienceName
   * @throws ServiceErrorException
   */
  public void createCmsAudience( String audienceName ) throws ServiceErrorException
  {
    ContentReader reader = ContentReaderManager.getContentReader();

    com.objectpartners.cms.domain.Audience cmsAudience = new com.objectpartners.cms.domain.Audience();
    cmsAudience.setName( audienceName );
    cmsAudience.setCode( "beaconAudience." + getUniqueKeyFragment() );
    cmsAudience.setDescription( audienceName );
    // cmsAudience.setApplication( application );
    try
    {
      getCmsAudienceService().storeAudience( reader.getApplicationCode(), cmsAudience );
    }
    catch( CmsDataValidationException e )
    {
      // todo jjd define error message
      throw new ServiceErrorExceptionWithRollback( "service.error.msg", e );
    }
  }

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
  public void createOrUpdateAsset( String sectionCode, String assetTypeName, String assetName, String assetCode, CMDataElement cmDataElement ) throws ServiceErrorException
  {
    List cmDataElements = new ArrayList();
    cmDataElements.add( cmDataElement );
    createOrUpdateAsset( sectionCode, assetTypeName, assetName, assetCode, cmDataElements );
  }

  public void createOrUpdateAsset( String sectionCode, String assetTypeName, String assetName, String assetCode, List cmDataElements ) throws ServiceErrorException
  {
    createOrUpdateAsset( sectionCode, assetTypeName, assetName, assetCode, cmDataElements, ContentReaderManager.getCurrentLocale(), null );
  }

  private boolean isKeyDefined( String key, Set assetTypeItems )
  {
    if ( assetTypeItems != null )
    {
      for ( Iterator iterator = assetTypeItems.iterator(); iterator.hasNext(); )
      {
        AssetTypeItem item = (AssetTypeItem)iterator.next();
        if ( item.getKey().equals( key ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  private AssetTypeItem getAssetTypeItem( String key, Set assetTypeItems )
  {
    if ( assetTypeItems != null )
    {
      for ( Iterator iterator = assetTypeItems.iterator(); iterator.hasNext(); )
      {
        AssetTypeItem item = (AssetTypeItem)iterator.next();
        if ( item.getKey().equals( key ) )
        {
          return item;
        }
      }
    }
    return null;

  }

  private Asset getCmsAsset( String assetCode )
  {
    FetchGroupRequestCollection fetchGroupRequests = new FetchGroupRequestCollection();
    fetchGroupRequests.add( new AssetFullyLoadedFetchGroupRequest() );
    return getCmsAssetService().getAsset( assetCode, ContentReaderManager.getContentReader().getApplicationCode(), fetchGroupRequests );
  }

  /**
   * Add an item to an existing pickList.
   * 
   * @param pickListItemAssetCode
   * @param itemName
   * @throws ServiceErrorException
   */
  public void addPickListItem( String pickListItemAssetCode, String itemName ) throws ServiceErrorException
  {
    addPickListItem( pickListItemAssetCode, "item." + getUniqueKeyFragment(), itemName );
  }

  /**
   * Add an item to an existing pickList.
   * 
   * @param pickListItemAssetCode
   * @param itemCode
   * @param itemName
   * @throws ServiceErrorException
   */
  public void addPickListItem( String pickListItemAssetCode, String itemCode, String itemName ) throws ServiceErrorException
  {
    Asset pickListItemAsset = getCmsAsset( pickListItemAssetCode );
    if ( pickListItemAsset == null || !pickListItemAsset.getAssetType().getName().equals( PICK_LIST_ITEM_ASSET_TYPE_NAME ) )
    {
      throw new ServiceErrorExceptionWithRollback( "pick.list.item.asset.error" );
    }

    ContentReader contentReader = ContentReaderManager.getContentReader();
    Object contentObject = contentReader.getContent( pickListItemAssetCode );
    if ( contentObject == null || ! ( contentObject instanceof List ) )
    {
      throw new ServiceErrorExceptionWithRollback( "pick.list.item.asset.error" );
    }

    // todo jjd enforce unique item name?

    // get the first pick list item content object from the list and clone it
    List pickListItemContents = (List)contentObject;

    List audienceList = new ArrayList();
    if ( null != pickListItemContents && pickListItemContents.size() > 0 )
    {
      // Get the list of audience from existing Picklist Item
      Content existingPickListItem = (Content)pickListItemContents.get( 0 );
      audienceList.addAll( existingPickListItem.getContentKey().getAudiences() );
    }
    else
    {
      // Set default audience
      ContentReader reader = ContentReaderManager.getContentReader();

      com.objectpartners.cms.domain.Audience defaultAudience = getCmsAudienceService().findAudienceByName( DEFAULT_AUDIENCE_NAME, reader.getApplicationCode(), null );

      audienceList.add( defaultAudience );
    }

    Content newPickListItem = new Content();

    Locale defaultLocale = CmsUtil.getLocale( CmsUtil.getCmsProperty( ContentReaderManager.getContentReader().getApplicationContext(), "defaultLocale" ) );
    newPickListItem.setLocale( defaultLocale );
    newPickListItem.getContentKey().setSortOrder( pickListItemContents.size() + 1 );

    Map dataMap = new HashMap();
    dataMap.put( PickListItem.ITEM_CODE_KEY, new Translations( itemCode ) );
    dataMap.put( PickListItem.ITEM_NAME_KEY, new Translations( itemName ) );
    dataMap.put( PickListItem.ITEM_DESC_KEY, new Translations( itemName ) );
    dataMap.put( PickListItem.ITEM_STATUS_KEY, new Translations( "true" ) );
    dataMap.put( PickListItem.ITEM_ABBR_KEY, new Translations( "" ) );
    try
    {
      newPickListItem.setContentStatus( ContentStatusEnum.LIVE );
      getCmsContentService().saveContent( newPickListItem,
                                          audienceList,
                                          pickListItemAsset.getAssetType().getAssetTypeItems(),
                                          pickListItemAsset.getId(),
                                          pickListItemAsset.getAssetType().getId(),
                                          dataMap,
                                          false );
      contentReader.assetChangedEvent( pickListItemAssetCode );
    }
    catch( NonUniqueDataValidationException e )
    {
      log.error( "Unable to maintain asset", e );
      throw new NonUniqueDataServiceErrorException( "service.error.message", e );
    }
    catch( CmsDataValidationException e )
    {
      // todo jjd define error message
      log.error( "Unable to maintain asset", e );
      throw new ServiceErrorExceptionWithRollback( "service.error.message", e );
    }
  }

  /**
   * Get a string using asset, key and locale. This is an overload of the getString method in ResourceBundle
   * with a split asset, key code and locale. 
   * 
   * @param asset
   * @param key
   * @param locale
   * @return non-null string, empty if key exists on asset but with null value, will have question
   *         marks if asset doesn't have key
   */
  public String getString( String asset, String key, Locale locale, boolean defaultEmpty )
  {
    Map dataMap = Collections.EMPTY_MAP;
    ContentReader contentReader = ContentReaderManager.getContentReader();
    Content content = null;
    if ( contentReader != null )
    {
      content = CmsUtil.getContentFromReaderObject( contentReader.getContent( asset, locale ) );
      if ( content != null )
      {
        dataMap = content.getContentDataMapList();
      }
    }
    if ( dataMap.containsKey( key ) )
    {
      Translations translations = (Translations)dataMap.get( key );
      String value = null;
      if ( translations != null )
      {
        if ( !StringUtils.isEmpty( translations.getValue() ) )
        {
          value = translations.getValue();
        }
        else
        {
          value = translations.getGoogleValue();
        }
      }
      return value != null ? value : "";
    }
    if ( defaultEmpty )
    {
      return "";
    }
    else
    {
      return "???" + asset + "." + key + "???";
    }
  }

  public List getSupportedLocales( boolean excludeReaderLocale )
  {
    ContentReader contentReader = ContentReaderManager.getContentReader();
    Object contentObject = contentReader.getContent( "default.locale.items" );
    List list = new ArrayList();

    if ( contentObject != null )
    {
      if ( contentObject instanceof Content )
      {
        list.add( contentObject );
      }
      else if ( contentObject instanceof List )
      {
        list = (List)contentObject;
      }
    }

    // This for loop is to delete locales which are not active

    for ( Iterator iter = list.iterator(); iter.hasNext(); )
    {
      Content content = (Content)iter.next();
      String localeStatus = (String)content.getContentDataMap().get( "STATUS" );
      if ( localeStatus != null && localeStatus.equals( "false" ) )
      {
        iter.remove();
      }
    }

    if ( excludeReaderLocale )
    {
      for ( Iterator iter = list.iterator(); iter.hasNext(); )
      {
        Content content = (Content)iter.next();
        String localeCode = (String)content.getContentDataMap().get( "CODE" );
        if ( localeCode != null && localeCode.equals( contentReader.getLocale().toString() ) )
        {
          iter.remove();
        }
      }
    }
    return list;
  }

  /**
   * @param oracleSequenceDAO value for oracleSequenceDAO property
   */
  public void setOracleSequenceDAO( OracleSequenceDAO oracleSequenceDAO )
  {
    this.oracleSequenceDAO = oracleSequenceDAO;
  }

  /**
   * @param assetCode
   * @return List of contents(includes all languages) for the given assetCode.
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public List getContentsForAsset( String assetCode )
  {
    Asset asset = getCmsAsset( assetCode );
    Long assetId = asset.getId();
    List contents = new ArrayList<Content>();

    FetchGroupRequestCollection fetchGroupRequests = new FetchGroupRequestCollection();
    fetchGroupRequests.add( new ContentKeyToContentFetchGroupRequest() );
    fetchGroupRequests.add( new ContentToAudiencesFetchGroupRequest() );

    contents = getCmsContentService().getContents( assetId, fetchGroupRequests );
    return contents;
  }

  /**
   * 
   * @param assetCode
   * @return List of audience for Multi Assets. Limitation : Restricting it to get for the first asset
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public List<com.objectpartners.cms.domain.Audience> getAudienceForAsset( String assetCode )
  {
    List<com.objectpartners.cms.domain.Audience> audienceList = new ArrayList<com.objectpartners.cms.domain.Audience>();
    List contentList = getContentsForAsset( assetCode );

    if ( contentList != null && !contentList.isEmpty() )
    {
      Content content = (Content)contentList.get( 0 );
      audienceList.addAll( content.getContentKey().getAudiences() );
    }
    return audienceList;
  }

  @SuppressWarnings( "rawtypes" )
  public void expireContents( String cmAssetCode, List<ResourceList> resourceList, String systemDefaultLocale ) throws CmsLiveDataException
  {
    List contents = getContentsForAsset( cmAssetCode );

    for ( Iterator contentIter = contents.iterator(); contentIter.hasNext(); )
    {
      Content content = (Content)contentIter.next();
      boolean deleteLocale = true;
      boolean systemDefaultLocaleDeleted = false;

      for ( ResourceList resources : resourceList )
      {
        if ( content.getLocale().toString().equals( resources.getLanguage() ) )
        {
          deleteLocale = false;
          break;
        }

        if ( deleteLocale && content.getLocale().toString().equals( systemDefaultLocale ) )
        {
          systemDefaultLocaleDeleted = true;
          break;
        }
      }

      if ( deleteLocale && !systemDefaultLocaleDeleted )
      {
        getCmsContentService().removeContent( content.getId() );
      }
    }
  }

  @Override
  public void removeContent( String assetCode, Long contentId ) throws CmsLiveDataException
  {
    getCmsContentService().removeContent( contentId );
    ContentReaderManager.getContentReader().assetChangedEvent( assetCode );
  }

  /**
   * @param sectionCode
   * @return List of assets for the given sectionCode.
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public List getAssetsForSection( String sectionCode )
  {
    List assets = new ArrayList();

    ContentReader reader = ContentReaderManager.getContentReader();

    FetchGroupRequestCollection fetchGroupRequests = new FetchGroupRequestCollection();
    fetchGroupRequests.add( new AssetToAssetTypeItemsFetchGroupRequest() );
    assets = getCmsAssetService().getAssets( reader.getApplicationCode(), sectionCode, fetchGroupRequests );

    return assets;
  }

  @Override
  public String getTextFromCmsResourceBundle( String key )
  {
    return CmsResourceBundle.getCmsBundle().getString( key );
  }
}
