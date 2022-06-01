
package com.biperf.core.ui.promotion;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.enums.PromotionCelebrationGenericEcardsType;
import com.biperf.core.domain.enums.PromotionCelebrationsVideoType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PromotionCelebrationsForm extends BaseActionForm
{
  public static final String SESSION_KEY = "promotionCelebrationsForm";

  private String promotionId;
  private String promotionName;
  private Long version;
  private String promotionTypeName;
  private String promotionTypeCode;
  private String promotionStatus;
  private String method;

  private String celebrationDisplayPeriod;
  private boolean allowOwnerMessage;
  private boolean allowDefaultMessage;

  private String imageUrl;
  private String imageUrlPath = "";
  private FormFile fileAsset;

  private String defaultMessage;

  private boolean yearTileEnabled;
  private boolean timelineTileEnabled;
  private boolean videoTileEnabled;
  private String videoPath;
  private boolean shareToMedia;
  private boolean anniversaryInYears = true;
  private String celebrationGenericEcard;
  private boolean serviceAnniversary = true;
  private String defaultCelebrationAvatar;
  private String defaultCelebrationName;

  public void load( Promotion promotion )
  {
    // Promotion display information
    this.promotionId = promotion.getId().toString();
    this.promotionName = promotion.getName();
    this.version = promotion.getVersion();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionStatus = promotion.getPromotionStatus().getCode();

    RecognitionPromotion recPromo = (RecognitionPromotion)promotion;

    if ( recPromo.getCelebrationDisplayPeriod() != null )
    {
      this.celebrationDisplayPeriod = recPromo.getCelebrationDisplayPeriod().toString();
    }

    this.allowOwnerMessage = recPromo.isAllowOwnerMessage();
    this.allowDefaultMessage = recPromo.isAllowDefaultMessage();

    this.imageUrl = recPromo.getDefaultCelebrationAvatar();
    this.defaultCelebrationName = recPromo.getDefaultCelebrationName();

    this.defaultMessage = recPromo.getCelebrationsDefaultMessageText();
    if ( recPromo.isServiceAnniversary() )
    {
      this.yearTileEnabled = recPromo.isYearTileEnabled();
    }
    this.timelineTileEnabled = recPromo.isTimelineTileEnabled();
    this.videoTileEnabled = recPromo.isVideoTileEnabled();
    if ( recPromo.getVideoPath() != null )
    {
      this.videoPath = recPromo.getVideoPath().getCode();
    }
    this.shareToMedia = recPromo.isShareToMedia();

    this.serviceAnniversary = recPromo.isServiceAnniversary();

    if ( recPromo.isServiceAnniversary() )
    {
      this.anniversaryInYears = recPromo.getAnniversaryInYears();
    }
    if ( recPromo.getCelebrationGenericEcard() != null )
    {
      this.celebrationGenericEcard = recPromo.getCelebrationGenericEcard().getCode();
    }

  }

  public Promotion toDomainObject( Promotion promotion )
  {
    RecognitionPromotion recPromo = (RecognitionPromotion)promotion;
    recPromo.setCelebrationDisplayPeriod( new Long( this.getCelebrationDisplayPeriod() ) );
    recPromo.setAllowOwnerMessage( this.isAllowOwnerMessage() );
    recPromo.setAllowDefaultMessage( this.isAllowDefaultMessage() );
    recPromo.setServiceAnniversary( this.serviceAnniversary );
    if ( isServiceAnniversary() )
    {
      if ( isAnniversaryInYears() )
      {
        recPromo.setYearTileEnabled( this.isYearTileEnabled() );
      }
    }
    recPromo.setTimelineTileEnabled( this.isTimelineTileEnabled() );
    recPromo.setVideoTileEnabled( this.isVideoTileEnabled() );
    recPromo.setVideoPath( PromotionCelebrationsVideoType.lookup( this.videoPath ) );
    recPromo.setShareToMedia( this.isShareToMedia() );
    if ( recPromo.isAllowDefaultMessage() )
    {
      recPromo.setDefaultCelebrationAvatar( this.imageUrl );
    }
    else
    {
      recPromo.setDefaultCelebrationAvatar( null );
    }
    recPromo.setDefaultCelebrationName( this.defaultCelebrationName );

    if ( isServiceAnniversary() )
    {
      recPromo.setAnniversaryInYears( this.isAnniversaryInYears() );
    }
    else
    {
      recPromo.setAnniversaryInYears( null );
    }

    recPromo.setCelebrationGenericEcard( PromotionCelebrationGenericEcardsType.lookup( this.celebrationGenericEcard ) );

    return recPromo;
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    String imageUrlCelebrationPath = (String)request.getSession().getAttribute( "imageUrlPath" );

    this.imageUrl = imageUrlCelebrationPath;

    // request.getSession().removeAttribute( "imageUrlFullPath" );

    if ( celebrationDisplayPeriod == null )
    {
      errors.add( "celebrationDisplayPeriod",
                  new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.celebrations.DISPLAY_PERIOD" ) ) );
    }

    else
    {
      try
      {
        long celebrationDisplayLongValue = Long.parseLong( celebrationDisplayPeriod );

        if ( celebrationDisplayLongValue < 0 || celebrationDisplayLongValue > 365 )
        {
          errors.add( "celebrationDisplayPeriod", new ActionMessage( "promotion.celebrations.errors.DISPLAY_PERIOD_RANGE" ) );
        }
      }
      catch( NumberFormatException e )
      {
        errors.add( "awardAmountTypeFixed", new ActionMessage( "promotion.celebrations.errors.VALID_DISPLAY_PERIOD" ) );
      }
    }
    if ( this.isAllowDefaultMessage() )
    {
      if ( this.defaultCelebrationName == null || this.defaultCelebrationName.equals( "" ) )
      {
        errors.add( "defaultCelebrationName", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.NAME" ) ) );
      }
      if ( this.defaultMessage == null || this.defaultMessage.equals( "" ) )
      {
        errors.add( "defaultMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.celebrations.DEFAULT_MESSAGE" ) ) );
      }
      // Bug Fix #72716 Start
      if ( StringUtils.isEmpty( imageUrl ) )
      {
        errors.add( "defaultMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.AVATAR" ) ) );
      }
      // Bug Fix #72716 End
    }

    if ( videoTileEnabled && ( videoPath == null || videoPath.isEmpty() ) )
    {
      errors.add( "videoTileEnabled", new ActionMessage( "promotion.celebrations.errors.VIDEO_REQUIRED" ) );
    }

    // To avoid selecting same filler image twice
    /*
     * if( celebrationFillerImage1 != null && celebrationFillerImage2 != null &&
     * celebrationFillerImage3 != null && celebrationFillerImage4 != null && celebrationFillerImage5
     * != null ) { int count=5; Set<String> validateImageSet = new HashSet<String>();
     * validateImageSet.add( celebrationFillerImage1 ); validateImageSet.add(
     * celebrationFillerImage2 ); validateImageSet.add( celebrationFillerImage3 );
     * validateImageSet.add( celebrationFillerImage4 ); validateImageSet.add(
     * celebrationFillerImage5 ); if( validateImageSet.size() != count ) { errors.add(
     * "fillerImages", new ActionMessage( "promotion.celebrations.errors.DISTINCT_FILLER_IMAGE" ) );
     * } }
     */

    return errors;
  }

  public String getCelebrationDisplayPeriod()
  {
    return celebrationDisplayPeriod;
  }

  public void setCelebrationDisplayPeriod( String celebrationDisplayPeriod )
  {
    this.celebrationDisplayPeriod = celebrationDisplayPeriod;
  }

  public boolean isAllowOwnerMessage()
  {
    return allowOwnerMessage;
  }

  public void setAllowOwnerMessage( boolean allowOwnerMessage )
  {
    this.allowOwnerMessage = allowOwnerMessage;
  }

  public String getDefaultMessage()
  {
    return defaultMessage;
  }

  public void setDefaultMessage( String defaultMessage )
  {
    this.defaultMessage = defaultMessage;
  }

  public boolean isYearTileEnabled()
  {
    return yearTileEnabled;
  }

  public void setYearTileEnabled( boolean yearTileEnabled )
  {
    this.yearTileEnabled = yearTileEnabled;
  }

  public boolean isTimelineTileEnabled()
  {
    return timelineTileEnabled;
  }

  public void setTimelineTileEnabled( boolean timelineTileEnabled )
  {
    this.timelineTileEnabled = timelineTileEnabled;
  }

  public boolean isVideoTileEnabled()
  {
    return videoTileEnabled;
  }

  public void setVideoTileEnabled( boolean videoTileEnabled )
  {
    this.videoTileEnabled = videoTileEnabled;
  }

  public String getVideoPath()
  {
    return videoPath;
  }

  public void setVideoPath( String videoPath )
  {
    this.videoPath = videoPath;
  }

  public boolean isShareToMedia()
  {
    return shareToMedia;
  }

  public void setShareToMedia( boolean shareToMedia )
  {
    this.shareToMedia = shareToMedia;
  }

  public boolean isAnniversaryInYears()
  {
    return anniversaryInYears;
  }

  public void setAnniversaryInYears( boolean anniversaryInYears )
  {
    this.anniversaryInYears = anniversaryInYears;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getCelebrationGenericEcard()
  {
    return celebrationGenericEcard;
  }

  public void setCelebrationGenericEcard( String celebrationGenericEcard )
  {
    this.celebrationGenericEcard = celebrationGenericEcard;
  }

  public boolean isServiceAnniversary()
  {
    return serviceAnniversary;
  }

  public void setServiceAnniversary( boolean serviceAnniversary )
  {
    this.serviceAnniversary = serviceAnniversary;
  }

  public FormFile getFileAsset()
  {
    return fileAsset;
  }

  public void setFileAsset( FormFile fileAsset )
  {
    this.fileAsset = fileAsset;
  }

  public String getImageUrl()
  {
    return imageUrl;
  }

  public void setImageUrl( String imageUrl )
  {
    this.imageUrl = imageUrl;
  }

  public String getDefaultCelebrationAvatar()
  {
    return defaultCelebrationAvatar;
  }

  public void setDefaultCelebrationAvatar( String defaultCelebrationAvatar )
  {
    this.defaultCelebrationAvatar = defaultCelebrationAvatar;
  }

  public String getDefaultCelebrationName()
  {
    return defaultCelebrationName;
  }

  public void setDefaultCelebrationName( String defaultCelebrationName )
  {
    this.defaultCelebrationName = defaultCelebrationName;
  }

  public String getImageUrlPath()
  {
    return this.imageUrl;
  }

  public void setImageUrlPath( String imageUrlPath )
  {
    this.imageUrlPath = imageUrlPath;
  }

  public boolean isAllowDefaultMessage()
  {
    return allowDefaultMessage;
  }

  public void setAllowDefaultMessage( boolean allowDefaultMessage )
  {
    this.allowDefaultMessage = allowDefaultMessage;
  }

}
