
package com.biperf.core.service.celebration.impl;

import java.awt.image.BufferedImage;
import java.util.List;

import com.biperf.core.dao.promotion.CelebrationManagerMessageDAO;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.celebration.CelebrationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.strategy.ImageCropStrategy;
import com.biperf.core.strategy.ImageResizeStrategy;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.value.CelebrationAvatarUploadValue;
import com.biperf.core.value.CelebrationManagerReminderBean;

public class CelebrationServiceImpl implements CelebrationService
{
  private static final int MEGABYTES_TO_BYTES_MULTIPLIER = 1024 * 1024;
  private SystemVariableService systemVariableService;
  private ImageCropStrategy imageCropStrategy;
  private ImageResizeStrategy imageResizeStrategy;
  private FileUploadStrategy appDataDirFileUploadStrategy;

  private CelebrationManagerMessageDAO celebrationManagerMessageDAO;

  public CelebrationManagerMessage saveCelebrationManagerMessage( CelebrationManagerMessage celebrationManagerMessage )
  {
    return celebrationManagerMessageDAO.saveCelebrationManagerMessage( celebrationManagerMessage );
  }

  public CelebrationManagerMessage getCelebrationManagerMessageById( Long id )
  {
    return celebrationManagerMessageDAO.getCelebrationManagerMessageById( id );
  }

  public CelebrationManagerMessage getCelebrationManagerMessageById( Long id, AssociationRequestCollection associationRequestCollection )
  {
    CelebrationManagerMessage celebrationManagerMessage = this.celebrationManagerMessageDAO.getCelebrationManagerMessageById( id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( celebrationManagerMessage );
    }
    return celebrationManagerMessage;
  }

  public List<CelebrationManagerReminderBean> getCelebrationManagerRemindersList( Long participantId )
  {
    List<CelebrationManagerReminderBean> reminderList = celebrationManagerMessageDAO.getCelebrationManagerRemindersList( participantId );
    return reminderList;
  }

  @Override
  public String getServiceAnniversaryEcardOrDefaultCelebrationEcard( String ecardFlashName, Long promotionId, String locale )
  {
    String ecard = celebrationManagerMessageDAO.getServiceAnniversaryEcardOrDefaultCelebrationEcard( ecardFlashName, promotionId, locale );
    return ecard;
  }

  public List<CelebrationManagerMessage> getCelebrationManagerByPromotion( Long promotionId )
  {
    return celebrationManagerMessageDAO.getCelebrationManagerByPromotion( promotionId );
  }

  public void setCelebrationManagerMessageDAO( CelebrationManagerMessageDAO celebrationManagerMessageDAO )
  {
    this.celebrationManagerMessageDAO = celebrationManagerMessageDAO;
  }

  public boolean validFileData( CelebrationAvatarUploadValue data )
  {
    // Check file type
    String extension = ImageUtils.getFileExtension( data.getName() );

    // Check file size
    if ( CelebrationAvatarUploadValue.TYPE_AVATAR.equals( data.getType() ) )
    {
      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal();
      if ( data.getSize() > sizeLimit )
      {
        return false;
      }
    }

    return true;
  }

  public CelebrationAvatarUploadValue uploadAvatarForCelebration( CelebrationAvatarUploadValue data ) throws ServiceErrorException
  {
    if ( validFileData( data ) )
    {
      try
      {
        // Upload Avatar to AppDataDir
        ImageUtils imgInstance = new ImageUtils();
        BufferedImage thumb = imgInstance.readImage( data.getData() );
        int targetCropDimension = thumb.getHeight() < thumb.getWidth() ? thumb.getHeight() : thumb.getWidth();
        thumb = imageCropStrategy.process( thumb, targetCropDimension, targetCropDimension );
        thumb = imageResizeStrategy.process( thumb, CelebrationAvatarUploadValue.THUMB_DIMENSION, CelebrationAvatarUploadValue.THUMB_DIMENSION );
        String avatarUrl = ImageUtils.getCelebrationThumbPath( data.getType(), data.getId(), data.getName() );
        data.setThumb( avatarUrl );
        appDataDirFileUploadStrategy.uploadFileData( data.getThumb(), ImageUtils.convertToByteArray( thumb, ImageUtils.getFileExtension( data.getName() ) ) );

      }
      catch( Exception e )
      {
        throw new ServiceErrorException( "quiz.diy.errors.IMAGE_UPLOAD_FAILED" );
      }
    }
    else
    {
      throw new ServiceErrorException( "purl.contribution.IMAGE_UPLOAD_INVALID" );
    }
    return data;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setAppDataDirFileUploadStrategy( FileUploadStrategy appDataDirFileUploadStrategy )
  {
    this.appDataDirFileUploadStrategy = appDataDirFileUploadStrategy;
  }

  public void setImageCropStrategy( ImageCropStrategy imageCropStrategy )
  {
    this.imageCropStrategy = imageCropStrategy;
  }

  public void setImageResizeStrategy( ImageResizeStrategy imageResizeStrategy )
  {
    this.imageResizeStrategy = imageResizeStrategy;
  }

}
