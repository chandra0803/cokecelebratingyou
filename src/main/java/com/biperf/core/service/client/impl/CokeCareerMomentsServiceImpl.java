
package com.biperf.core.service.client.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcodec.api.awt.FrameGrab;
import org.jcodec.common.io.ByteBufferSeekableByteChannel;
import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.dao.client.CokeCareerMomentsDAO;
import com.biperf.core.domain.client.CareerMomentsModuleSet;
import com.biperf.core.domain.client.CareerMomentsView;
import com.biperf.core.domain.client.ClientProfileComment;
import com.biperf.core.domain.client.ClientProfileLike;
import com.biperf.core.domain.client.JobChanges;
import com.biperf.core.domain.client.JobChangesDatum;
import com.biperf.core.domain.client.NewHires;
import com.biperf.core.domain.client.NewHiresDatum;
import com.biperf.core.domain.enums.EnvironmentTypeEnum;
import com.biperf.core.domain.enums.PurlContributorMediaStatus;
import com.biperf.core.domain.enums.PurlMediaState;
import com.biperf.core.domain.enums.SupportedEcardVideoTypes;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.client.CokeCareerMomentsService;
import com.biperf.core.service.imageservice.ImageServiceRepositoryFactory;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.purl.impl.PurlServiceImpl;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.strategy.ImageCropStrategy;
import com.biperf.core.strategy.ImageResizeStrategy;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.ProfileAvatarUploadUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PurlFileUploadValue;
import com.biperf.core.value.client.ActivityPod;
import com.biperf.core.value.client.ClientCommentsValueBean;
import com.biperf.core.value.client.CokeCommentsLikes;
import com.biperf.core.value.client.Commenter;
import com.biperf.core.value.client.LevelOneComment;
import com.biperf.core.value.client.Medium;
import com.biperf.core.value.client.Photo;
import com.biperf.core.value.client.UserInfo;
import com.biperf.core.value.client.Video;
import com.biperf.core.value.contributor.Media;
import com.objectpartners.cms.util.DateUtils;

public class CokeCareerMomentsServiceImpl implements CokeCareerMomentsService
{
  private static final Log logger = LogFactory.getLog( CokeCareerMomentsServiceImpl.class );
  private static final int MEGABYTES_TO_BYTES_MULTIPLIER = 1024 * 1024;
  public static final String GIF = "gif";
  
  private CokeCareerMomentsDAO cokeCareerMomentsDAO;
  private ParticipantService participantService;
  private List<String> acceptableExtentions;
  FileUploadStrategy appDataDirFileUploadStrategy;
  FileUploadStrategy webdavFileUploadStrategy;
  ImageCropStrategy imageCropStrategy;
  ImageResizeStrategy imageResizeStrategy;
  SystemVariableService systemVariableService;
  @Autowired
  ImageServiceRepositoryFactory imageServiceRepositoryFactory;

  @Override
  public CareerMomentsModuleSet getCareerMomentsModuleData()
  {
    CareerMomentsModuleSet careerMomentsSets = new CareerMomentsModuleSet();

    List<NewHiresDatum> newHiresData = new ArrayList<NewHiresDatum>();
    newHiresData.addAll( cokeCareerMomentsDAO.getCareerMomentsHireDataForModule() );

    List<JobChangesDatum> jobChangesData = new ArrayList<JobChangesDatum>();
    jobChangesData.addAll( cokeCareerMomentsDAO.getCareerMomentsJobDataForModule() );

    NewHires newHires = new NewHires();
    newHires.setDisplayDate( DateUtils.toDisplayString( new Date() ) );
    if(newHiresData.size()>0)
    {
      String siteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      for(NewHiresDatum newHire:newHiresData)
      {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put( "paxId", Long.valueOf(newHire.getId()) );
        paramMap.put( "isFullPage", "true" );
        newHire.setAdditionalProperty( "contributeUrl", ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PUBLIC_PROFILE_VIEW, paramMap ) );
      }
    }
    
    newHires.setNewHiresData( newHiresData );
    careerMomentsSets.setNewHires( newHires );

    JobChanges jobChanges = new JobChanges();
    jobChanges.setDisplayDate( DateUtils.toDisplayString( new Date() ) );
    jobChanges.setJobChangesData( jobChangesData );
    
    if(jobChangesData.size()>0)
    {
      String siteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      for(JobChangesDatum jobChange:jobChangesData)
      {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put( "paxId", Long.valueOf(jobChange.getId()) );
        paramMap.put( "isFullPage", "true" );
        jobChange.setAdditionalProperty( "contributeUrl", ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PUBLIC_PROFILE_VIEW, paramMap ) );
      }
    }
    careerMomentsSets.setJobChanges( jobChanges );

    return careerMomentsSets;

  }
  
  public Map<Long, Long> getAboutMeLikesByUserId(Long userId, List<AboutMe> aboutMeQuestions)
  {
    return this.cokeCareerMomentsDAO.getAboutMeLikesByUserId( userId, aboutMeQuestions );
  }
  
  public ClientProfileLike saveLike(ClientProfileLike clientLike)
  {
    return this.cokeCareerMomentsDAO.saveLike( clientLike );
  }
  
  public List<CokeCommentsLikes> getCareerMomentsLikesCommentsCount(String date)
  {
    return this.cokeCareerMomentsDAO.getCareerMomentsLikesCommentsCount( date );
  }
  
  public void removeAboutMeLike( final Long userId, final Long profileUserId, final Long aboutMeId )
  {
    this.cokeCareerMomentsDAO.removeAboutMeLike( userId, profileUserId, aboutMeId );
  }

  public void setCokeCareerMomentsDAO( CokeCareerMomentsDAO cokeCareerMomentsDAO )
  {
    this.cokeCareerMomentsDAO = cokeCareerMomentsDAO;
  }
  
  public PurlFileUploadValue uploadPhotoForContributor( PurlFileUploadValue data ) throws ServiceErrorException
  {
    String thumbnailUrl = null;
    if ( validFileData( data ) )
    {
      try
      {
        ImageUtils imgInstance = new ImageUtils();
        BufferedImage full = imgInstance.readImage( data.getData() );
        byte[] dataGif = null;
        if ( ImageUtils.getFileExtension( data.getName() ).equalsIgnoreCase( GIF ) )
        {
          int targetCropDimension = full.getHeight() < full.getWidth() ? full.getHeight() : full.getWidth();
          dataGif = imageCropStrategy.process( data.getData(), targetCropDimension, targetCropDimension );
          data.setData( dataGif );
          full = imgInstance.readImage( dataGif );
        }
        else
        {
          full = imageResizeStrategy.process( full, PurlFileUploadValue.DETAIL_DIMENSION );
        }
        data.setFull( ImageUtils.getProfileDetailPath( data.getType(), data.getId(), data.getName() ) );
        if ( dataGif != null )
        {
          appDataDirFileUploadStrategy.uploadFileData( data.getFull(), dataGif );
        }
        else
        {
          appDataDirFileUploadStrategy.uploadFileData( data.getFull(), ImageUtils.convertToByteArray( full, ImageUtils.getFileExtension( data.getName() ) ) );
        }
        BufferedImage thumb = imgInstance.readImage( data.getData() );
        int targetCropDimension = thumb.getHeight() < thumb.getWidth() ? thumb.getHeight() : thumb.getWidth();
        thumb = imageCropStrategy.process( thumb, targetCropDimension, targetCropDimension );
        thumb = imageResizeStrategy.process( thumb, PurlFileUploadValue.THUMB_DIMENSION, PurlFileUploadValue.THUMB_DIMENSION );
        data.setThumb( ImageUtils.getPurlThumbPath( data.getType(), data.getId(), data.getName() ) );
        appDataDirFileUploadStrategy.uploadFileData( data.getThumb(), ImageUtils.convertToByteArray( thumb, ImageUtils.getFileExtension( data.getName() ) ) );
        
      }
      catch( Exception e )
      {
        logger.error( "Image upload failed:", e );
        throw new ServiceErrorException( "purl.contribution.IMAGE_UPLOAD_FAILED" );
      }
    }
    else
    {
      throw new ServiceErrorException( "purl.contribution.IMAGE_UPLOAD_INVALID" );
    }
    
    return data;
  }
  
  protected static final class WebDavUploadResult
  {
    private final boolean success;
    private final String uploadedCardFileName;
    private final String webDavUrl;

    public WebDavUploadResult()
    {
      success = false;
      uploadedCardFileName = "";
      webDavUrl = "";
    }

    public WebDavUploadResult( boolean success, String uploadedCardFileName, String webDavUrl )
    {
      this.success = success;
      this.uploadedCardFileName = uploadedCardFileName;
      this.webDavUrl = webDavUrl;
    }

    public boolean isSuccess()
    {
      return success;
    }

    public String getUploadedCardFileName()
    {
      return uploadedCardFileName;
    }

    public String getWebDavUrl()
    {
      return webDavUrl;
    }
  }

  
  protected WebDavUploadResult uploadToWebdav( String originalFileName, byte[] data, Long contributorId )
  {
    // user id will be null in case of external contributor
    String ownCardNameTemp = ( UserManager.getUserId() != null ? UserManager.getUserId() : contributorId ) + "_" + DateUtils.getCurrentDateAsLong() + "_" + originalFileName;
    String filePath = "profile" + '/' + ownCardNameTemp;

    try
    {
      boolean success = appDataDirFileUploadStrategy.uploadFileData( filePath, data );
      if ( !success )
      {
        return new WebDavUploadResult();
      }
    }
    catch( ServiceErrorException t )
    {
      return new WebDavUploadResult();
    }

    try
    {
      boolean ismovedtowebdav = moveFileToWebdav( filePath );
      if ( !ismovedtowebdav )
      {
        return new WebDavUploadResult();
      }
    }
    catch( Throwable t )
    {
      return new WebDavUploadResult();
    }

    // we made it this far so determine the full webDav URL to the media
    // we just uploaded, and return
    String ownCardWebDavPath = getProfileVideoUrl( ownCardNameTemp );

    return new WebDavUploadResult( true, ownCardNameTemp, ownCardWebDavPath );
  }
  
  public boolean moveFileToWebdav( String mediaUrl )
  {
    if ( logger.isInfoEnabled() )
    {
      logger.info( "transfering media [" + mediaUrl + "] to WebDav " );
    }
    try
    {
      byte[] media = appDataDirFileUploadStrategy.getFileData( mediaUrl );
      webdavFileUploadStrategy.uploadFileData( mediaUrl, media );

      appDataDirFileUploadStrategy.delete( mediaUrl );
      return true;
    }
    catch( Throwable e )
    {
      // Must not have the file in AppDataDir of server executing this
      // process
      logger.error( "Error transfering media [" + mediaUrl + "] to WebDav.  Message: " + e.getMessage() );
    }
    return false;
  }
  
  public String getProfileVideoUrl( String ownCardName )
  {
    String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = systemVariableService.getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    String ownCardUrl = null;
    if ( EnvironmentTypeEnum.isAws() )
    {
      siteUrlPrefix = systemVariableService.getContextName();
    }
    ownCardUrl = siteUrlPrefix + "-cm/cm3dam/profile" + '/' + ownCardName;

    return ownCardUrl;
  }
  
  protected String getDefaultVideoImageUrl()
  {
    return getSiteUrlPrefix() + "/assets/img/placeHolderVid.jpg";
  }
  
  protected String getSiteUrlPrefix()
  {
    return systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }
  
  
  public PurlFileUploadValue uploadVideoForContributor( PurlFileUploadValue data ) throws ServiceErrorException
  {
    if ( validVideoFileData( data ) )
    {
      try
      {
        // FrameGrab does not work with webm files. Use a default
        // thumbnail.
        String thumbnailUrl = null;
        String fileExtension = FilenameUtils.getExtension( data.getName() );
        if ( SupportedEcardVideoTypes.WEBM.getInformalName().equals( fileExtension ) )
        {
          thumbnailUrl = getDefaultVideoImageUrl();
        }
        else
        {
          // Create and upload thumbnail
          String thumbnailFilename = data.getName() + ".video.jpeg";
          ByteBuffer videoDataBuffer = ByteBuffer.wrap( data.getData() );
          ByteBufferSeekableByteChannel videoDataByteChannel = new ByteBufferSeekableByteChannel( videoDataBuffer );
          BufferedImage thumbnailImage = FrameGrab.getFrame( videoDataByteChannel, 1 );
          ByteArrayOutputStream thumbnailOutputStream = new ByteArrayOutputStream( 256 * 1024 );
          ImageIO.write( thumbnailImage, "jpeg", thumbnailOutputStream );
          byte[] thumbnailData = thumbnailOutputStream.toByteArray();
          WebDavUploadResult thumbnailUploadResult = uploadToWebdav( thumbnailFilename, thumbnailData, data.getId() );

          // Failing early if thumbnail upload failed
          if ( !thumbnailUploadResult.isSuccess() )
          {
            logger.error( "Video thumbnail upload failed" );
            throw new ServiceErrorException( "purl.contribution.VIDEO_UPLOAD_FAILED" );
          }
          else
          {
            thumbnailUrl = thumbnailUploadResult.getWebDavUrl();
          }
        }

        // Upload video
        WebDavUploadResult videoUploadResult = uploadToWebdav( data.getName(), data.getData(), data.getId() );

        if ( !videoUploadResult.isSuccess() )
        {
          logger.error( "Video upload failed" );
          throw new ServiceErrorException( "purl.contribution.VIDEO_UPLOAD_FAILED" );
        }

        data.setThumb( thumbnailUrl );
        data.setFull( videoUploadResult.getWebDavUrl() );
      }
      catch( Exception e )
      {
        logger.error( "Video upload failed:", e );
        throw new ServiceErrorException( "purl.contribution.VIDEO_UPLOAD_FAILED" );
      }
    }
    else
    {
      throw new ServiceErrorException( "purl.contribution.VIDEO_UPLOAD_INVALID" );
    }
    return data;
  }

  @Override
  public ClientCommentsValueBean getProfileCommentsByUserId( Long userId )
  {
    List<ClientProfileComment> commentsList = this.cokeCareerMomentsDAO.getMainLevelProfileCommentsByUserId( userId );
    ClientCommentsValueBean bean = new ClientCommentsValueBean();
    
    if(commentsList!=null && commentsList.size()>0)
    {
      List<ActivityPod> activityPods = new ArrayList<ActivityPod>();
      
      for(ClientProfileComment comment: commentsList)
      {
        List<Medium> media = new ArrayList<Medium>();
        List<UserInfo> userInfo = new ArrayList<UserInfo>();
        userInfo.add( getUserInfo( comment.getCommenterUserId() ) );
        
        ActivityPod activityPod = new ActivityPod();
        activityPod.setCommentId( comment.getId() );
        activityPod.setUserInfo( userInfo );
        activityPod.setCommentText( comment.getComments() );
        activityPod.setCommentLang( comment.getCommentLanguageId() );
        activityPod.setNumLikers( Integer.toUnsignedLong( this.cokeCareerMomentsDAO.getLikesCountByCommentId( comment.getId() ) ));
        if(activityPod.getNumLikers()>0)
        {
          activityPod.setIsLiked( true);
        }
        
        if(Objects.nonNull( comment.getImageUrl() ))
        {
          Medium mediaItem = new Medium();
          Photo photo = new Photo();
          photo.setSrc( comment.getImageUrl() );
          mediaItem.setPhoto( photo );
          mediaItem.setPhoto( photo );
          media.add( mediaItem );
        }
        else if(Objects.nonNull( comment.getVideoUrl() ))
        {
          Medium mediaItem = new Medium();
          Video video = new Video();
          video.setFileType( "mp4" );
          video.setSrc( comment.getVideoUrl() );
          mediaItem.setVideo( video );
          media.add( mediaItem );
        }
        
        List<LevelOneComment> levelOneComments = new ArrayList<LevelOneComment>();
        List<ClientProfileComment> subCommentsList = this.cokeCareerMomentsDAO.getSubLevelProfileComments( userId, comment.getId() );
        if(subCommentsList!=null && subCommentsList.size()>0)
        {
          for(ClientProfileComment subComment: subCommentsList) 
          {
            LevelOneComment levelOneComment = new LevelOneComment();
            levelOneComment.setCommentId( subComment.getId() );
            levelOneComment.setLevelOneId( subComment.getId() );
            levelOneComment.setId( subComment.getId() );
            levelOneComment.setCommenter( getCommenter( subComment.getCommenterUserId() ) );
            levelOneComment.setComment( subComment.getComments() );
            levelOneComment.setIsMine( false );
            if(UserManager.getUserId().equals( subComment.getCommenterUserId() ))
            {
              levelOneComment.setIsMine( true );
            }
            levelOneComment.setNumLikers( Integer.toUnsignedLong( this.cokeCareerMomentsDAO.getLikesCountByCommentId( subComment.getId() ) ));
            if(levelOneComment.getNumLikers()>0)
            {
              levelOneComment.setIsLiked( true );
            }
            levelOneComments.add( levelOneComment );
          }
        }

        activityPod.setLevelOneComments( levelOneComments );
        activityPod.setMedia( media );
        activityPods.add( activityPod );
        
      }
      bean.setActivityPods( activityPods );
      bean.setUserLang( UserManager.getUserLocale() );
      
    }
    return bean;
  }
  
  private UserInfo getUserInfo(Long commenterUserId)
  {
    UserInfo userInfo = new UserInfo();
    Participant commentedPax = participantService.getParticipantById( commenterUserId );
    userInfo.setContributorID( commenterUserId );
    userInfo.setFirstName( commentedPax.getFirstName() );
    userInfo.setLastName( commentedPax.getLastName() );
    userInfo.setProfilePhoto( commentedPax.getAvatarOriginal() );
    userInfo.setSignedIn( "false" );
    if(UserManager.getUserId().equals( commenterUserId ))
    {
      userInfo.setSignedIn( "true" );
    }
    userInfo.setProfileLink( "" );
    userInfo.setProfilePhoto( commentedPax.getAvatarSmall() );
    
    return userInfo;
  }
  
  private Commenter getCommenter(Long commenterUserId)
  {
    Commenter userInfo = new Commenter();
    Participant commentedPax = participantService.getParticipantById( commenterUserId );
    userInfo.setId( commenterUserId );
    userInfo.setFirstName( commentedPax.getFirstName() );
    userInfo.setLastName( commentedPax.getLastName() );
    userInfo.setAvatarUrl( commentedPax.getAvatarOriginal() );
    
    return userInfo;
  }
  
  public boolean validFileData( PurlFileUploadValue data )
  {
    // Check file type
    String extension = ImageUtils.getFileExtension( data.getName() );
    if ( !acceptableExtentions.contains( extension.toLowerCase() ) )
    {
      return false;
    }

    // Check file size
    if ( PurlFileUploadValue.TYPE_PICTURE.equals( data.getType() ) || PurlFileUploadValue.TYPE_AVATAR.equals( data.getType() ) )
    {
      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal();
      if ( data.getSize() > sizeLimit )
      {
        return false;
      }
    }

    return true;
  }

  public boolean validVideoFileData( PurlFileUploadValue data )
  {
    if ( PurlFileUploadValue.TYPE_VIDEO.equals( data.getType() ) )
    {
      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_VIDEO_SIZE_LIMIT ).getIntVal();
      if ( data.getSize() > sizeLimit )
      {
        return false;
      }
    }

    return true;
  }

  public ClientProfileComment saveComment(ClientProfileComment clientComment) throws ServiceErrorException
  {
    if ( !StringUtil.isEmpty( clientComment.getImageUrl() ) )
    {
      String imageServiceUrl;

      if ( !StringUtil.isEmpty( clientComment.getImageUrl() ) )
      {
        // launchMediaUploadProcess( comment.getImageUrl(), runByUser );
        imageServiceUrl = moveFileToImageService( clientComment.getImageUrl(), String.valueOf( clientComment.getProfileUserId() ) );
        clientComment.setImageUrl( imageServiceUrl );

      }
      if ( !StringUtil.isEmpty( clientComment.getImageUrlThumb() ) )
      {
        // launchMediaUploadProcess( comment.getImageUrlThumb(),
        // runByUser );
        imageServiceUrl = moveFileToImageService( clientComment.getImageUrlThumb(), String.valueOf( clientComment.getProfileUserId() ) );
        clientComment.setImageUrlThumb( imageServiceUrl );
      }
    }
    return this.cokeCareerMomentsDAO.saveComment( clientComment );
  }
  
  private String moveFileToImageService( String imageUrl, String purlContributorId ) throws ServiceErrorException
  {
    String imageServiceUrl;
    byte[] imageByte = appDataDirFileUploadStrategy.getFileData( imageUrl );
    String contentType = ProfileAvatarUploadUtil.getContentType( ImageUtils.getFilename( imageUrl ) );
    String encodedImage = Base64.getEncoder().encodeToString( imageByte );

    imageServiceUrl = imageServiceRepositoryFactory.getRepo().uploadImage( ProfileAvatarUploadUtil.getImageData( contentType, encodedImage ), purlContributorId, "profile_picture" );

    if ( !StringUtil.isEmpty( imageServiceUrl ) && imageServiceUrl.contains( "cloud/v1/images" ) )
    {
      appDataDirFileUploadStrategy.delete( imageUrl );
    }

    return imageServiceUrl;

  }
  
  public Map<Long, Boolean> getMyAboutMeLikes(List<AboutMe> aboutMeQuestions, Participant pax)
  {
    return this.cokeCareerMomentsDAO.getMyAboutMeLikes( aboutMeQuestions, pax );
  }
  
  public int getLikesCountByAboutMeId(Long aboutMeId)
  {
    return this.cokeCareerMomentsDAO.getLikesCountByAboutMeId( aboutMeId );
  }
  
  public void removeCommentLike( final Long userId, final Long profileUserId, final Long commentId )
  {
    this.cokeCareerMomentsDAO.removeCommentLike( userId, profileUserId, commentId );
  }
  
  public int getLikesCountByCommentId(Long commentId)
  {
    return this.cokeCareerMomentsDAO.getLikesCountByCommentId( commentId );
  }
  
  public List<CareerMomentsView> getCareerMomentsDataForDetail(String tabType, int current, String recType, String listVal, String locale, String username)
  {
    return this.cokeCareerMomentsDAO.getCareerMomentsJobDataForDetail( tabType, current, recType, listVal, locale, username );
  }
  
  
  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
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

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setAcceptableExtentions( List<String> acceptableExtentions )
  {
    this.acceptableExtentions = acceptableExtentions;
  }

}
