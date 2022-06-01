
package com.biperf.core.service.diycommunications.impl;

import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.biperf.core.dao.diycommunications.ParticipantDIYCommunicationsDAO;
import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.news.NewsDetailsView;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.diycommunications.ParticipantDIYCommunicationsService;
import com.biperf.core.service.oracle.OracleSequenceService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.strategy.ImageCropStrategy;
import com.biperf.core.strategy.ImageResizeStrategy;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.DIYCommunicationsFileUploadValue;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.diycommunication.ResourceList;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.domain.Asset;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.enums.DataTypeEnum;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * 
 * DIYQuizServiceImpl.
 * 
 * @author kandhi
 * @since Jul 9, 2013
 * @version 1.0
 */
public class ParticipantDIYCommunicationsServiceImpl implements ParticipantDIYCommunicationsService
{
  private static final int MEGABYTES_TO_BYTES_MULTIPLIER = 1024 * 1024;

  private static final String HTTP_CONSTANT = "http://";
  private static final String SECURE_HTTP_CONSTANT = "https://";

  private ParticipantDIYCommunicationsDAO participantDIYCommunicationsDAO;
  private CMAssetService cmAssetService;
  private SystemVariableService systemVariableService;
  private FileUploadStrategy appDataDirFileUploadStrategy;
  private FileUploadStrategy webdavFileUploadStrategy;
  private ImageCropStrategy imageCropStrategy;
  private ImageResizeStrategy imageResizeStrategy;
  private List<String> acceptableExtentions;
  OracleSequenceService oracleSequenceService;

  @SuppressWarnings( "unchecked" )
  public List<NewsDetailsView> getNewsList( String contextPath )
  {
    List<Content> diyNews = getDiyCommunications( DIYCommunications.COMMUNICATION_TYPE_NEWS, DIYCommunications.DIY_NEWS_SECTION_CODE );
    Set<String> diyIds = new HashSet<String>();
    for ( Content content : diyNews )
    {
      Map<String, String> contentDataMap = content.getContentDataMap();
      String uniqueId = contentDataMap.get( "UNIQUE_NEWS_ID" );
      diyIds.add( uniqueId );
    }
    List<Content> news = (List<Content>)ContentReaderManager.getContentReader().getContent( "home.news" );
    List<Content> activeContents = new ArrayList<Content>();

    if ( diyNews != null && diyNews.size() > 0 )
    {
      diyNews = diyNews.stream().filter( new Predicate<Content>()
      {

        @Override
        public boolean test( Content c )
        {
          return c.getContentDataMap().size() > 0 ? true : false;
        }
      } ).collect( Collectors.toList() );

      activeContents.addAll( diyNews );
    }
    if ( news != null && news.size() > 0 )
    {
      activeContents.addAll( news );
    }

    if ( isSortByCMSortOrder( activeContents ) )
    {
      // use second level check
      Collections.sort( activeContents, new NewsSortOrderComparator() );
    }
    else
    {
      Collections.sort( activeContents, new NewsDateComparator() );
    }

    List<Content> newsList = activeContents;
    List<NewsDetailsView> newsView = new ArrayList<NewsDetailsView>();

    for ( Content content : newsList )
    {
      if ( diyIds.contains( content.getContentDataMap().get( "UNIQUE_NEWS_ID" ) ) )
      {
        newsView.add( buildNewsJson( content, contextPath, true ) );
      }
      else
      {
        newsView.add( buildNewsJson( content, contextPath, false ) );
      }
    }

    return newsView;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private boolean isSortByCMSortOrder( List<Content> activeMessages )
  {
    Content content = null;
    Set sortOrderSet = new HashSet();
    for ( int i = 0; i < activeMessages.size(); i++ )
    {
      content = activeMessages.get( i );
      String sortOrder = (String)content.getContentDataMap().get( "SORT_ORDER" );
      // First level check -- Sort Order is not Mandatory
      if ( sortOrder == null )
      {
        return false;
      }
      // Second level check -- Invalid Sort Order
      if ( sortOrder.equals( "" ) || sortOrder.equals( "0" ) )
      {
        return false;
      }
      // Third level check -- Duplicate Sort Order
      if ( !sortOrderSet.add( sortOrder ) )
      {
        return false;
      }
    }
    return true;
  }

  @SuppressWarnings( "unchecked" )
  private NewsDetailsView buildNewsJson( Content newsContent, String baseUri, boolean diy )
  {
    DateFormat originalFormat = new SimpleDateFormat( "MM/dd/yyyy" );
    DateFormat targetFormat = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );

    Map<String, String> contentDataMap = newsContent.getContentDataMap();
    String startDate = "";

    if ( diy )
    {
      startDate = contentDataMap.get( "START_DATE" );
    }
    else
    {
      startDate = contentDataMap.get( "DATE" );
    }
    StringTokenizer st = new StringTokenizer( startDate, "/" );

    String month = st.nextToken();
    String date = st.nextToken();
    String year = st.nextToken();
    if ( month.length() == 1 )
    {
      month = "0" + month;
    }
    if ( date.length() == 1 )
    {
      date = "0" + date;
    }
    // month = getMonthForInt( Integer.parseInt( month ) - 1 );
    String sortDate = year + "-" + month + "-" + date;
    String storyName = contentDataMap.get( "TITLE" );
    String uniqueStoryId = contentDataMap.get( "UNIQUE_NEWS_ID" );
    String storySlug = baseUri + "/newsDetail.do";
    String storyContent = "<p>" + contentDataMap.get( "FULL_TXT" ) + "</p>";
    String storyContentShort = contentDataMap.get( "SHORT_TXT" );

    String storyImageUrl = contentDataMap.get( "NEWS_IMAGE" );
    String storyImageUrlMax = contentDataMap.get( "NEWS_IMAGE_MAX" );
    String storyImageUrlMobile = contentDataMap.get( "NEWS_IMAGE_MOBILE" );

    NewsDetailsView newsDetails = new NewsDetailsView();
    newsDetails.setSortDate( sortDate );
    newsDetails.setStoryName( storyName );
    newsDetails.setStorySlug( storySlug );
    newsDetails.setStoryDate( DateUtils.toConvertDateFormatString( originalFormat, targetFormat, startDate ) );
    newsDetails.setStoryContent( storyContent );
    if ( storyContentShort != null )
    {
      String shortText = "<p>" + storyContentShort + "</p>";
      newsDetails.setStoryContentShort( shortText );
    }
    newsDetails.setId( uniqueStoryId );

    newsDetails.setStoryImageUrl( storyImageUrl );
    newsDetails.setStoryImageUrl_max( storyImageUrlMax );
    newsDetails.setStoryImageUrl_mobile( storyImageUrlMobile );

    return newsDetails;
  }

  public List<DIYCommunications> getActiveByCommunicationType( Long managerId, String communicationType )
  {
    return participantDIYCommunicationsDAO.getActiveByCommunicationType( managerId, communicationType );
  }

  public List<DIYCommunications> getActiveByCommunicationType( String communicationType )
  {
    return participantDIYCommunicationsDAO.getActiveByCommunicationType( communicationType );
  }

  public List<DIYCommunications> getArchievedByCommunicationType( Long managerId, String communicationType )
  {
    return participantDIYCommunicationsDAO.getArchievedByCommunicationType( managerId, communicationType );
  }

  public DIYCommunications saveDIYCommunications( DIYCommunications diyCommunications ) throws UniqueConstraintViolationException, ServiceErrorException
  {
    DIYCommunications dbCommunications = this.getCommunicationsByTitleAndType( diyCommunications.getContentTitle(), diyCommunications.getCommunicationType() );
    if ( dbCommunications != null )
    {
      // if we found a record in the database with the given contentTitle, and our diyCommunications
      // ID
      // is
      // null,
      // we are trying to insert a duplicate record.
      if ( diyCommunications.getId() == null )
      {
        throw new UniqueConstraintViolationException();
      }

      // if we found a record in the database with the given contentTitle, but the ids are not
      // equal,
      // we are trying to update to a contentTitle that already exists so throw a
      // UniqueConstraintViolationException
      if ( dbCommunications.getId().compareTo( diyCommunications.getId() ) != 0 )
      {
        throw new UniqueConstraintViolationException();
      }

    }
    return participantDIYCommunicationsDAO.saveDIYCommunications( diyCommunications );
  }

  public List<Audience> getPublicAudienceList()
  {
    return participantDIYCommunicationsDAO.getPublicAudienceList();
  }

  public DIYCommunications getDIYCommunicationsById( Long id )
  {
    return participantDIYCommunicationsDAO.getDIYCommunicationsById( id );
  }

  public DIYCommunications getCommunicationsByTitleAndType( String communicationsTitle, String communicationsType )
  {
    return participantDIYCommunicationsDAO.getCommunicationsByTitleAndType( communicationsTitle, communicationsType );
  }

  public boolean isValidImageFormat( String format )
  {
    if ( format != null && acceptableExtentions.contains( format.toLowerCase() ) )
    {
      return true;
    }
    return false;
  }

  public boolean validFileData( DIYCommunicationsFileUploadValue data )
  {
    // Check file size
    if ( DIYCommunicationsFileUploadValue.TYPE_PICTURE.equals( data.getType() ) )
    {
      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal();
      double lowerSizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * .001;
      if ( data.getSize() > sizeLimit )
      {
        return false;
      }
      else if ( data.getSize() <= lowerSizeLimit )
      {
        return false;
      }
    }

    else if ( DIYCommunicationsFileUploadValue.TYPE_PDF.equals( data.getType() ) || DIYCommunicationsFileUploadValue.TYPE_XLS.equals( data.getType() )
        || DIYCommunicationsFileUploadValue.TYPE_XLSX.equals( data.getType() ) )
    {
      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_PDF_SIZE_LIMIT ).getIntVal();
      if ( data.getSize() > sizeLimit )
      {
        return false;
      }
    }

    return true;
  }

  public DIYCommunicationsFileUploadValue uploadFileForCommunications( DIYCommunicationsFileUploadValue data, String communicationsType ) throws ServiceErrorException
  {
    if ( data.getType().equalsIgnoreCase( DIYCommunicationsFileUploadValue.TYPE_PDF ) || data.getType().equalsIgnoreCase( DIYCommunicationsFileUploadValue.TYPE_XLS )
        || data.getType().equalsIgnoreCase( DIYCommunicationsFileUploadValue.TYPE_XLSX ) || data.getType().equalsIgnoreCase( DIYCommunicationsFileUploadValue.TYPE_DOC )
        || data.getType().equalsIgnoreCase( DIYCommunicationsFileUploadValue.TYPE_DOCX ) || data.getType().equalsIgnoreCase( DIYCommunicationsFileUploadValue.TYPE_PPT )
        || data.getType().equalsIgnoreCase( DIYCommunicationsFileUploadValue.TYPE_PPTX ) )
    {
      try
      {
        data.setFull( ImageUtils.getCommunicationsResourceCenterDetailPath( data.getId(), data.getName(), communicationsType ) );
        appDataDirFileUploadStrategy.uploadFileData( data.getFull(), data.getData() );
      }
      catch( Exception e )
      {
        throw new ServiceErrorException( "quiz.learning.IMAGE_UPLOAD_FAILED:" + e );
      }
    }
    else
    {
      throw new ServiceErrorException( "quiz.learning.PDF_UPLOAD_INVALID" );
    }
    return data;
  }

  public DIYCommunicationsFileUploadValue uploadPhotoForCommunications( DIYCommunicationsFileUploadValue data, String communicationsType ) throws ServiceErrorException
  {

    if ( validFileData( data ) )
    {
      try
      {
        data.setFull( ImageUtils.getCommunicationsResourceCenterDetailPath( data.getId(), data.getName(), communicationsType ) );
        appDataDirFileUploadStrategy.uploadFileData( data.getFull(), data.getData() );
        String fileExtension = ImageUtils.getFileExtension( data.getFull() );
        // BufferedImage thumb = ImageUtils.convertToBufferedImage(data.getData());
        ImageUtils imgInstance = new ImageUtils();
        BufferedImage thumb = imgInstance.readImage( data.getData() );
        int targetCropDimension = thumb.getHeight() < thumb.getWidth() ? thumb.getHeight() : thumb.getWidth();
        thumb = imageCropStrategy.process( thumb, targetCropDimension, targetCropDimension );
        thumb = imageResizeStrategy.process( thumb, DIYCommunicationsFileUploadValue.DIY_THUMB_DIMENSION, DIYCommunicationsFileUploadValue.DIY_THUMB_DIMENSION );
        data.setThumb( ImageUtils.getCommunicationsThumbPath( data.getId(), data.getName(), communicationsType ) );
        appDataDirFileUploadStrategy.uploadFileData( data.getThumb(), ImageUtils.convertToByteArray( thumb, fileExtension ) );

      }
      catch( Exception e )
      {
        throw new ServiceErrorException( "quiz.learning.IMAGE_UPLOAD_FAILED:" + e );
      }
    }
    else
    {
      throw new ServiceErrorException( "quiz.learning.IMAGE_UPLOAD_INVALID" );
    }
    return data;
  }

  public boolean moveFileToWebdav( String mediaUrl )
  {
    try
    {
      byte[] media = appDataDirFileUploadStrategy.getFileData( mediaUrl );
      webdavFileUploadStrategy.uploadFileData( mediaUrl, media );

      appDataDirFileUploadStrategy.delete( mediaUrl );
      return true;
    }
    catch( Throwable e )
    {
      // Must not have the file in AppDataDir of server executing this process
    }
    return false;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public DIYCommunications saveBannerContents( DIYCommunications diycommunication, ResourceList bannerResource, List<String> audience, Map<String, Object> deafultLangMap ) throws ServiceErrorException
  {
    String bannerResourceLink;
    try
    {
      if ( StringUtils.isEmpty( diycommunication.getCmAssetCode() ) )
      {
        // Create and set asset to BannerObject
        String newAssetName = cmAssetService.getUniqueAssetCode( DIYCommunications.DIY_BANNER_CMASSET_PREFIX );
        diycommunication.setCmAssetCode( newAssetName );
      }

      if ( bannerResource.getLink() != null && !bannerResource.getLink().startsWith( HTTP_CONSTANT ) && !bannerResource.getLink().startsWith( SECURE_HTTP_CONSTANT ) )
      {
        bannerResourceLink = HTTP_CONSTANT + bannerResource.getLink();
      }
      else
      {
        bannerResourceLink = bannerResource.getLink();
      }

      CMDataElement cmDataElementImageSize = new CMDataElement( DIYCommunications.DIY_BANNER_CMASSET_NAME,
                                                                DIYCommunications.DIY_BANNER_CMASSET_IMAGESIZE,
                                                                bannerResource.getImageSize(),
                                                                false,
                                                                DataTypeEnum.HTML );
      CMDataElement cmDataElementImageSizeMax = new CMDataElement( DIYCommunications.DIY_BANNER_CMASSET_NAME,
                                                                   DIYCommunications.DIY_BANNER_CMASSET_IMAGESIZE_MAX,
                                                                   bannerResource.getImageSize_max(),
                                                                   false,
                                                                   DataTypeEnum.HTML );
      CMDataElement cmDataElementImageSizeMobile = new CMDataElement( DIYCommunications.DIY_BANNER_CMASSET_NAME,
                                                                      DIYCommunications.DIY_BANNER_CMASSET_IMAGESIZE_MOBILE,
                                                                      bannerResource.getImageSize_mobile(),
                                                                      false,
                                                                      DataTypeEnum.HTML );
      CMDataElement cmDataElementLinkUrl = new CMDataElement( DIYCommunications.DIY_BANNER_CMASSET_NAME, DIYCommunications.DIY_BANNER_CMASSET_LINK_URL, bannerResourceLink, false, DataTypeEnum.HTML );
      CMDataElement cmDataElementTarget = new CMDataElement( DIYCommunications.DIY_BANNER_CMASSET_NAME, DIYCommunications.DIY_BANNER_CMASSET_TARGET, "_blank", false, DataTypeEnum.HTML );
      CMDataElement cmDataElementReferenceKey = new CMDataElement( DIYCommunications.DIY_BANNER_CMASSET_NAME,
                                                                   DIYCommunications.DIY_BANNER_CMASSET_REFERENCE_CONTENTKEY,
                                                                   bannerResource.getImageId(),
                                                                   false,
                                                                   DataTypeEnum.HTML );
      CMDataElement cmDataElementDefaultLanguage = new CMDataElement( DIYCommunications.DIY_BANNER_CMASSET_NAME,
                                                                      DIYCommunications.SOURCE_LOCALE,
                                                                      (String)deafultLangMap.get( "sourceLocale" ),
                                                                      false,
                                                                      DataTypeEnum.HTML );

      List elements = new ArrayList();
      elements.add( cmDataElementImageSize );
      elements.add( cmDataElementImageSizeMax );
      elements.add( cmDataElementImageSizeMobile );
      elements.add( cmDataElementLinkUrl );
      elements.add( cmDataElementTarget );
      elements.add( cmDataElementReferenceKey );
      elements.add( cmDataElementDefaultLanguage );

      cmAssetService.createOrUpdateAsset( DIYCommunications.DIY_BANNER_SECTION_CODE,
                                          DIYCommunications.DIY_BANNER_CMASSET_TYPE_NAME,
                                          DIYCommunications.DIY_BANNER_CMASSET_NAME,
                                          diycommunication.getCmAssetCode(),
                                          elements,
                                          (Locale)deafultLangMap.get( "locale" ),
                                          audience );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return diycommunication;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public DIYCommunications saveResourceCenterResources( DIYCommunications diyCommunications, ResourceList resourceCenterContent, List<String> audienceNames, Map<String, Object> deafultLangMap )
      throws ServiceErrorException
  {
    String resourceCenterLink;
    try
    {
      if ( StringUtils.isEmpty( diyCommunications.getCmAssetCode() ) )
      {
        String newAssetName = cmAssetService.getUniqueAssetCode( DIYCommunications.DIY_RESOURCE_CMASSET_PREFIX );
        diyCommunications.setCmAssetCode( newAssetName );
      }

      if ( resourceCenterContent.getLink() != null && !resourceCenterContent.getLink().startsWith( HTTP_CONSTANT ) && !resourceCenterContent.getLink().startsWith( SECURE_HTTP_CONSTANT ) )
      {
        resourceCenterLink = HTTP_CONSTANT + resourceCenterContent.getLink();
      }
      else
      {
        resourceCenterLink = resourceCenterContent.getLink();
      }

      CMDataElement cmDataElementLinkTitle = new CMDataElement( DIYCommunications.DIY_RESOURCE_CMASSET_NAME,
                                                                DIYCommunications.DIY_RESOURCE_CMASSET_LINK_TITLE,
                                                                resourceCenterContent.getTitle(),
                                                                false,
                                                                DataTypeEnum.HTML );

      CMDataElement cmDataElementLinkUrl = new CMDataElement( DIYCommunications.DIY_RESOURCE_CMASSET_NAME,
                                                              DIYCommunications.DIY_RESOURCE_CMASSET_LINK_URL,
                                                              resourceCenterLink,
                                                              false,
                                                              DataTypeEnum.HTML );

      CMDataElement cmDataElementDefaultLanguage = new CMDataElement( DIYCommunications.DIY_RESOURCE_CMASSET_NAME,
                                                                      DIYCommunications.SOURCE_LOCALE,
                                                                      (String)deafultLangMap.get( "sourceLocale" ),
                                                                      false,
                                                                      DataTypeEnum.HTML );

      List<CMDataElement> elements = new ArrayList<CMDataElement>();
      elements.add( cmDataElementLinkTitle );
      elements.add( cmDataElementLinkUrl );
      elements.add( cmDataElementDefaultLanguage );

      cmAssetService.createOrUpdateAsset( DIYCommunications.DIY_RESOURCE_SECTION_CODE,
                                          DIYCommunications.DIY_RESOURCE_CMASSET_TYPE_NAME,
                                          DIYCommunications.DIY_RESOURCE_CMASSET_NAME,
                                          diyCommunications.getCmAssetCode(),
                                          elements,
                                          (Locale)deafultLangMap.get( "locale" ),
                                          !audienceNames.isEmpty() ? audienceNames : null );

    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return diyCommunications;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public DIYCommunications saveTipsResources( DIYCommunications diyCommunications, ResourceList tipsContent, List<String> audienceNames, Map<String, Object> deafultLangMap )
      throws ServiceErrorException
  {
    try
    {
      if ( StringUtils.isEmpty( diyCommunications.getCmAssetCode() ) )
      {
        String newAssetName = cmAssetService.getUniqueAssetCode( DIYCommunications.DIY_TIPS_CMASSET_PREFIX );
        diyCommunications.setCmAssetCode( newAssetName );
      }

      CMDataElement cmDataElementLinkTitle = new CMDataElement( DIYCommunications.DIY_TIPS_CMASSET_NAME, DIYCommunications.DIY_TIPS_CMASSET_TITLE, tipsContent.getTitle(), false, DataTypeEnum.HTML );
      CMDataElement cmDataElementContent = new CMDataElement( DIYCommunications.DIY_TIPS_CMASSET_NAME,
                                                              DIYCommunications.DIY_RESOURCE_CMASSET_LINK_TITLE,
                                                              tipsContent.getContent(),
                                                              false,
                                                              DataTypeEnum.HTML );

      CMDataElement cmDataElementDefaultLanguage = new CMDataElement( DIYCommunications.DIY_TIPS_CMASSET_NAME,
                                                                      DIYCommunications.SOURCE_LOCALE,
                                                                      (String)deafultLangMap.get( "sourceLocale" ),
                                                                      false,
                                                                      DataTypeEnum.HTML );

      List<CMDataElement> elements = new ArrayList<CMDataElement>();
      elements.add( cmDataElementLinkTitle );
      elements.add( cmDataElementContent );
      elements.add( cmDataElementDefaultLanguage );

      cmAssetService.createOrUpdateAsset( DIYCommunications.DIY_TIPS_SECTION_CODE,
                                          DIYCommunications.DIY_TIPS_CMASSET_TYPE_NAME,
                                          DIYCommunications.DIY_TIPS_CMASSET_NAME,
                                          diyCommunications.getCmAssetCode(),
                                          elements,
                                          (Locale)deafultLangMap.get( "locale" ),
                                          !audienceNames.isEmpty() ? audienceNames : null );

    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }

    return diyCommunications;
  }

  public DIYCommunications saveNewsContents( DIYCommunications diycommunication, ResourceList newsContent, List<String> audience, Map<String, Object> deafultLangMap ) throws ServiceErrorException
  {
    if ( StringUtils.isEmpty( diycommunication.getCmAssetCode() ) )
    {
      // Create and set asset to QuizLearningObject
      String newAssetName = cmAssetService.getUniqueAssetCode( DIYCommunications.DIY_NEWS_CMASSET_PREFIX );
      diycommunication.setCmAssetCode( newAssetName );
    }

    CMDataElement cmDataElementImageSize = new CMDataElement( DIYCommunications.DIY_NEWS_CMASSET_NAME,
                                                              DIYCommunications.DIY_NEWS_CMASSET_IMAGESIZE,
                                                              newsContent.getImageSize(),
                                                              false,
                                                              DataTypeEnum.HTML );
    CMDataElement cmDataElementImageSizeMax = new CMDataElement( DIYCommunications.DIY_NEWS_CMASSET_NAME,
                                                                 DIYCommunications.DIY_NEWS_CMASSET_IMAGESIZE_MAX,
                                                                 newsContent.getImageSize_max(),
                                                                 false,
                                                                 DataTypeEnum.HTML );
    CMDataElement cmDataElementImageSizeMobile = new CMDataElement( DIYCommunications.DIY_NEWS_CMASSET_NAME,
                                                                    DIYCommunications.DIY_NEWS_CMASSET_IMAGESIZE_MOBILE,
                                                                    newsContent.getImageSize_mobile(),
                                                                    false,
                                                                    DataTypeEnum.HTML );
    CMDataElement cmDataElementStartDate = new CMDataElement( DIYCommunications.DIY_NEWS_CMASSET_NAME,
                                                              DIYCommunications.DIY_NEWS_CMASSET_START_DATE,
                                                              DateUtils.toDisplayString( diycommunication.getStartDate(), Locale.US ),
                                                              false,
                                                              DataTypeEnum.DATE );
    CMDataElement cmDataElementEndDate = new CMDataElement( DIYCommunications.DIY_NEWS_CMASSET_NAME,
                                                            DIYCommunications.DIY_NEWS_CMASSET_END_DATE,
                                                            DateUtils.toDisplayString( diycommunication.getEndDate(), Locale.US ),
                                                            false,
                                                            DataTypeEnum.DATE );

    CMDataElement cmDataElementDefaultLanguage = new CMDataElement( DIYCommunications.DIY_NEWS_CMASSET_NAME,
                                                                    DIYCommunications.SOURCE_LOCALE,
                                                                    (String)deafultLangMap.get( "sourceLocale" ),
                                                                    false,
                                                                    DataTypeEnum.HTML );

    // save assert with user current locale
    diycommunication = saveAsset( diycommunication,
                                  newsContent,
                                  audience,
                                  cmDataElementImageSize,
                                  cmDataElementImageSizeMax,
                                  cmDataElementImageSizeMobile,
                                  cmDataElementStartDate,
                                  cmDataElementEndDate,
                                  cmDataElementDefaultLanguage,
                                  (Locale)deafultLangMap.get( "locale" ) );
    return diycommunication;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private DIYCommunications saveAsset( DIYCommunications diycommunication,
                                       ResourceList newsContent,
                                       List<String> audience,
                                       CMDataElement cmDataElementImageSize,
                                       CMDataElement cmDataElementImageSizeMax,
                                       CMDataElement cmDataElementImageSizeMobile,
                                       CMDataElement cmDataElementStartDate,
                                       CMDataElement cmDataElementEndDate,
                                       CMDataElement cmDataElementDefaultLanguage,
                                       Locale locale )
      throws ServiceErrorException
  {
    try
    {
      Long uniqueNewsId = oracleSequenceService.getOracleSequenceNextValue( "cm_asset_diy_commn_id_pk_sq" );

      CMDataElement cmDataElementHeadline = new CMDataElement( DIYCommunications.DIY_NEWS_CMASSET_NAME,
                                                               DIYCommunications.DIY_NEWS_CMASSET_HEADLINE,
                                                               newsContent.getHeadline(),
                                                               false,
                                                               DataTypeEnum.HTML );
      CMDataElement cmDataElementUniqueNewsId = new CMDataElement( DIYCommunications.DIY_NEWS_CMASSET_NAME,
                                                                   DIYCommunications.DIY_NEWS_CMASSET_UNIQUE_NEWS_ID,
                                                                   uniqueNewsId.toString(),
                                                                   false,
                                                                   DataTypeEnum.NUMBER );
      CMDataElement cmDataElementReferenceKeyForImage = new CMDataElement( DIYCommunications.DIY_NEWS_CMASSET_NAME,
                                                                           DIYCommunications.DIY_NEWS_CMASSET_REFERENCE_CONTENTKEY,
                                                                           newsContent.getImageId(),
                                                                           false,
                                                                           DataTypeEnum.HTML );
      CMDataElement cmDataElementStory = new CMDataElement( DIYCommunications.DIY_NEWS_CMASSET_NAME,
                                                            DIYCommunications.DIY_NEWS_CMASSET_STORY_CONTENT,
                                                            newsContent.getStory(),
                                                            false,
                                                            DataTypeEnum.HTML );

      List elements = new ArrayList();
      elements.add( cmDataElementUniqueNewsId );
      elements.add( cmDataElementImageSize );
      elements.add( cmDataElementImageSizeMax );
      elements.add( cmDataElementImageSizeMobile );
      elements.add( cmDataElementHeadline );
      elements.add( cmDataElementStory );
      elements.add( cmDataElementStartDate );
      elements.add( cmDataElementEndDate );
      elements.add( cmDataElementReferenceKeyForImage );
      elements.add( cmDataElementDefaultLanguage );

      cmAssetService.createOrUpdateAsset( DIYCommunications.DIY_NEWS_SECTION_CODE,
                                          DIYCommunications.DIY_NEWS_CMASSET_TYPE_NAME,
                                          DIYCommunications.DIY_NEWS_CMASSET_NAME,
                                          diycommunication.getCmAssetCode(),
                                          elements,
                                          locale,
                                          audience );
    }
    catch( ServiceErrorException e )
    {
      List errors = new ArrayList();
      errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
      throw e;
    }
    return diycommunication;
  }

  public List<Content> getDiyCommunications( String communicationType, String sectionCode )
  {
    List<Content> diyList = new ArrayList<Content>();

    List<DIYCommunications> activeDiyComms = getActiveByCommunicationType( communicationType );

    List assets = cmAssetService.getAssetsForSection( sectionCode );

    for ( Iterator assetsIter = assets.iterator(); assetsIter.hasNext(); )
    {
      Object contentObject = null;
      Content content = null;

      Asset asset = (Asset)assetsIter.next();
      contentObject = ContentReaderManager.getContentReader().getContent( asset.getCode() );

      if ( contentObject instanceof Content )
      {
        content = (Content)contentObject;
      }
      else if ( contentObject instanceof List )
      {
        content = ! ( (List)contentObject ).isEmpty() ? (Content) ( (List)contentObject ).get( 0 ) : new Content();
      }

      if ( content != null )
      {
        for ( DIYCommunications diyCommunication : activeDiyComms )
        {
          if ( asset.getCode().equals( diyCommunication.getCmAssetCode() ) )
          {
            diyList.add( content );
            break;
          }
        }
      }
    }

    return diyList;
  }

  public void setAcceptableExtentions( List<String> acceptableExtentions )
  {
    this.acceptableExtentions = acceptableExtentions;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setParticipantDIYCommunicationsDAO( ParticipantDIYCommunicationsDAO participantDIYCommunicationsDAO )
  {
    this.participantDIYCommunicationsDAO = participantDIYCommunicationsDAO;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setAppDataDirFileUploadStrategy( FileUploadStrategy appDataDirFileUploadStrategy )
  {
    this.appDataDirFileUploadStrategy = appDataDirFileUploadStrategy;
  }

  public void setWebdavFileUploadStrategy( FileUploadStrategy webdavFileUploadStrategy )
  {
    this.webdavFileUploadStrategy = webdavFileUploadStrategy;
  }

  public void setImageCropStrategy( ImageCropStrategy imageCropStrategy )
  {
    this.imageCropStrategy = imageCropStrategy;
  }

  public void setImageResizeStrategy( ImageResizeStrategy imageResizeStrategy )
  {
    this.imageResizeStrategy = imageResizeStrategy;
  }

  public void setOracleSequenceService( OracleSequenceService oracleSequenceService )
  {
    this.oracleSequenceService = oracleSequenceService;
  }

  @Override
  public List<FormattedValueBean> getAudienceParticipants( Long audienceId )
  {
    return participantDIYCommunicationsDAO.getAudienceParticipants( audienceId );
  }
}
