/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/RecognitionPromotion.java,v $
 */

package com.biperf.core.domain.promotion;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.PromotionBudgetSweep;
import com.biperf.core.domain.enums.PromotionCelebrationGenericEcardsType;
import com.biperf.core.domain.enums.PromotionCelebrationsImageType;
import com.biperf.core.domain.enums.PromotionCelebrationsVideoType;
import com.biperf.core.domain.enums.PublicRecognitionAudienceType;
import com.biperf.core.domain.enums.PurlPromotionMediaType;
import com.biperf.core.domain.enums.PurlPromotionMediaValue;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * RecognitionPromotion.
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
 * <td>sathish</td>
 * <td>Oct 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
public class RecognitionPromotion extends AbstractRecognitionPromotion
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * If true, then send a copy of the recognition notification e-mail message
   * to the recipient's manager; if false, do not.
   */
  private boolean copyRecipientManager;
  private boolean copyOthers;
  private boolean allowManagerAward;
  private Long mgrAwardPromotionId;
  /**
   * Identifies the certificates that can be sent to recognizees under this
   * promotion.
   */
  private Set promotionCertificates = new TreeSet();
  private Set homePageItems = new LinkedHashSet();

  private boolean openEnrollmentEnabled;
  private boolean selfRecognitionEnabled;
  private boolean budgetSweepEnabled;
  private boolean showInBudgetTracker;
  private boolean includePurl;
  private boolean displayPurlInPurlTile;
  private PurlPromotionMediaType purlPromotionMediaType;
  private PurlPromotionMediaValue purlMediaValue;
  private boolean allowRecognitionSendDate;
  private boolean allowPublicRecognitionPoints;
  private BudgetMaster publicRecogBudgetMaster;
  private Long publicRecogAwardAmountMin;
  private Long publicRecogAwardAmountMax;
  private PublicRecognitionAudienceType publicRecognitionAudienceType;

  private Set promotionPublicRecognitionAudiences = new LinkedHashSet();

  private boolean publicRecogAwardAmountTypeFixed;
  private Long publicRecogAwardAmountFixed;

  private Long maxDaysDelayed;

  private boolean behaviorRequired;

  private boolean mobAppEnabled;

  // PURL Auto Contribution Message fields
  private boolean purlStandardMessageEnabled = false;
  private String purlStandardMessage;
  private String defaultContributorAvatar;
  private String defaultContributorName;
  private String contentResourceCMCode;

  // Recognition Promotion Celebrations Fields
  private boolean includeCelebrations;
  private Long celebrationDisplayPeriod;
  private boolean allowOwnerMessage;
  private boolean allowDefaultMessage;
  private String defaultMessage;
  private boolean yearTileEnabled;
  private boolean timelineTileEnabled;
  private boolean videoTileEnabled;
  private PromotionCelebrationsVideoType videoPath;
  private boolean shareToMedia;
  private PromotionCelebrationsImageType celebrationFillerImage1;
  private PromotionCelebrationsImageType celebrationFillerImage2;
  private PromotionCelebrationsImageType celebrationFillerImage3;
  private PromotionCelebrationsImageType celebrationFillerImage4;
  private PromotionCelebrationsImageType celebrationFillerImage5;
  private boolean fillerImg1AwardNumberEnabled;
  private boolean fillerImg2AwardNumberEnabled;
  private boolean fillerImg3AwardNumberEnabled;
  private boolean fillerImg4AwardNumberEnabled;
  private boolean fillerImg5AwardNumberEnabled;
  private Boolean anniversaryInYears;
  private boolean serviceAnniversary;
  private PromotionCelebrationGenericEcardsType celebrationGenericEcard;

  // budget sweep
  private Set<PromotionBudgetSweep> promotionBudgetSweeps = new LinkedHashSet<PromotionBudgetSweep>();

  // Celebration Fields
  private String defaultCelebrationAvatar;
  private String defaultCelebrationName;
  private boolean emailEnabled;
  private boolean emailConfirmEnabled = true;

  // Changes related to wip #23129 starts
  private boolean apqConversion;
  // Changes related to wip #23129 ends
  
  // Client customizations for WIP #62128 starts
  private boolean cheersPromotion;
  // Client customizations for WIP #62128 end
  
  //Client customization start
  private boolean allowMeme;
  private boolean allowSticker;
  private boolean allowUploadOwnMeme;
  //Client customization end
  /**
   * Returns a deep copy of this object.
   *
   * @param cloneWithChildren
   * @param newPromotionName   the promotion name to be assigned to the copy.
   * @param newChildPromotionNameHolders
   * @return a deep copy of this <code>AbstractRecognitionPromotion</code> object.
   * @throws CloneNotSupportedException if one of this objects component objects
   *         does not support cloning.
   */
  public Object deepCopy( boolean cloneWithChildren, String newPromotionName, List newChildPromotionNameHolders ) throws CloneNotSupportedException
  {
    RecognitionPromotion clonedPromotion = (RecognitionPromotion)super.deepCopy( cloneWithChildren, newPromotionName, newChildPromotionNameHolders );

    clonedPromotion.setIncludePurl( this.isIncludePurl() );
    clonedPromotion.setAllowRecognitionSendDate( this.isAllowRecognitionSendDate() );
    clonedPromotion.setAllowYourOwnCard( this.isAllowYourOwnCard() );
    clonedPromotion.setDrawYourOwnCard( this.isDrawYourOwnCard() );
    clonedPromotion.setCopyRecipientManager( this.isCopyRecipientManager() );
    clonedPromotion.setCopyOthers( this.isCopyOthers() );
    clonedPromotion.setAllowManagerAward( this.isAllowManagerAward() );
    clonedPromotion.setMgrAwardPromotionId( this.mgrAwardPromotionId );
    clonedPromotion.setPromotionCertificates( new TreeSet() );
    if ( getPromotionCertificates() != null )
    {
      for ( Iterator iter = this.getPromotionCertificates().iterator(); iter.hasNext(); )
      {
        PromotionCert certificate = (PromotionCert)iter.next();
        clonedPromotion.addCertificate( certificate.deepCopy() );
      }
    }
    clonedPromotion.setHomePageItems( new LinkedHashSet() );
    if ( null != this.homePageItems && this.homePageItems.size() > 0 )
    {
      for ( Iterator iter = getHomePageItems().iterator(); iter.hasNext(); )
      {
        HomePageItem homePageItem = (HomePageItem)iter.next();
        HomePageItem clone = homePageItem.deepCopy();
        clonedPromotion.addHomePageItem( clone );
      }
    }

    clonedPromotion.setPromotionPublicRecognitionAudiences( new LinkedHashSet() );
    for ( Iterator audiencesIter = this.promotionPublicRecognitionAudiences.iterator(); audiencesIter.hasNext(); )
    {
      PromotionPublicRecognitionAudience promotionPublicRecognitionAudience = (PromotionPublicRecognitionAudience)audiencesIter.next();
      clonedPromotion.addPromotionPublicRecognitionAudience( (PromotionPublicRecognitionAudience)promotionPublicRecognitionAudience.clone() );
    }

    clonedPromotion.setPublicRecogAwardAmountMax( this.publicRecogAwardAmountMax );
    clonedPromotion.setPublicRecogAwardAmountMin( this.publicRecogAwardAmountMin );

    clonedPromotion.setPublicRecogAwardAmountTypeFixed( this.isPublicRecogAwardAmountTypeFixed() );
    clonedPromotion.setPublicRecogAwardAmountFixed( this.getPublicRecogAwardAmountFixed() );
    clonedPromotion.setPublicRecogBudgetMaster( this.getPublicRecogBudgetMaster() );
    if ( clonedPromotion.getPublicRecogBudgetMaster() != null )
    {
      if ( !clonedPromotion.getPublicRecogBudgetMaster().isMultiPromotion() )
      {
        // if the BudgetMaster is not shareable across promotions, clear
        // it out
        clonedPromotion.setPublicRecogBudgetMaster( null );
      }
    }
    if ( this.isAllowRecognitionSendDate() )
    {
      clonedPromotion.setMaxDaysDelayed( this.maxDaysDelayed );
    }

    clonedPromotion.setBehaviorRequired( this.isBehaviorRequired() );
    clonedPromotion.setIncludeCelebrations( this.includeCelebrations );
    clonedPromotion.setAllowOwnerMessage( this.allowOwnerMessage );
    clonedPromotion.setCelebrationDisplayPeriod( this.celebrationDisplayPeriod );
    clonedPromotion.setYearTileEnabled( this.yearTileEnabled );
    clonedPromotion.setTimelineTileEnabled( this.timelineTileEnabled );
    clonedPromotion.setVideoTileEnabled( this.videoTileEnabled );
    clonedPromotion.setVideoPath( this.videoPath );
    clonedPromotion.setShareToMedia( this.shareToMedia );
    clonedPromotion.setFillerImg1AwardNumberEnabled( this.fillerImg1AwardNumberEnabled );
    clonedPromotion.setFillerImg2AwardNumberEnabled( this.fillerImg2AwardNumberEnabled );
    clonedPromotion.setFillerImg3AwardNumberEnabled( this.fillerImg3AwardNumberEnabled );
    clonedPromotion.setFillerImg4AwardNumberEnabled( this.fillerImg4AwardNumberEnabled );
    clonedPromotion.setFillerImg5AwardNumberEnabled( this.fillerImg5AwardNumberEnabled );
    clonedPromotion.setCelebrationFillerImage1( this.celebrationFillerImage1 );
    clonedPromotion.setCelebrationFillerImage2( this.celebrationFillerImage2 );
    clonedPromotion.setCelebrationFillerImage3( this.celebrationFillerImage3 );
    clonedPromotion.setCelebrationFillerImage4( this.celebrationFillerImage4 );
    clonedPromotion.setCelebrationFillerImage5( this.celebrationFillerImage5 );
    clonedPromotion.setAnniversaryInYears( this.anniversaryInYears );
    clonedPromotion.setCelebrationGenericEcard( this.celebrationGenericEcard );
    //client customization start
    clonedPromotion.setAllowMeme( this.allowMeme );
    clonedPromotion.setAllowSticker( this.allowSticker );
    clonedPromotion.setAllowUploadOwnMeme(this.allowUploadOwnMeme);
    //client customization end
    return clonedPromotion;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

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

  public boolean isCopyRecipientManager()
  {
    return copyRecipientManager;
  }

  public void setCopyRecipientManager( boolean copyRecipientManager )
  {
    this.copyRecipientManager = copyRecipientManager;
  }

  public void setCopyOthers( boolean copyOthers )
  {
    this.copyOthers = copyOthers;
  }

  public boolean isCopyOthers()
  {
    return copyOthers;
  }

  // ---------------------------------------------------------------------------
  // Copy Methods
  // ---------------------------------------------------------------------------

  public boolean isAllowManagerAward()
  {
    return allowManagerAward;
  }

  public void setAllowManagerAward( boolean allowManagerAward )
  {
    this.allowManagerAward = allowManagerAward;
  }

  public Long getMgrAwardPromotionId()
  {
    return mgrAwardPromotionId;
  }

  public void setMgrAwardPromotionId( Long mgrAwardPromotionId )
  {
    this.mgrAwardPromotionId = mgrAwardPromotionId;
  }

  public Set<PromotionCert> getPromotionCertificates()
  {
    return promotionCertificates;
  }

  public void setPromotionCertificates( Set promotionCertificates )
  {
    this.promotionCertificates = promotionCertificates;
  }

  public void addCertificate( PromotionCert promotionCertificate )
  {
    promotionCertificate.setPromotion( this );
    this.promotionCertificates.add( promotionCertificate );
  }

  public void addPromotionCertificate( String certificateId, String orderNumber )
  {
    PromotionCert promotionCert = new PromotionCert( this, certificateId, orderNumber );
    this.promotionCertificates.add( promotionCert );
  }

  public void addHomePageItem( HomePageItem ahomePageItem )
  {
    ahomePageItem.setPromotion( this );
    this.homePageItems.add( ahomePageItem );
  }

  public Set getHomePageItems()
  {
    return homePageItems;
  }

  public void setHomePageItems( Set homePageItems )
  {
    this.homePageItems = homePageItems;
  }

  public boolean isBudgetSweepEnabled()
  {
    return budgetSweepEnabled;
  }

  public void setBudgetSweepEnabled( boolean budgetSweepEnabled )
  {
    this.budgetSweepEnabled = budgetSweepEnabled;
  }

  public boolean isOpenEnrollmentEnabled()
  {
    return openEnrollmentEnabled;
  }

  public void setOpenEnrollmentEnabled( boolean openEnrollmentEnabled )
  {
    this.openEnrollmentEnabled = openEnrollmentEnabled;
  }

  public boolean isSelfRecognitionEnabled()
  {
    return selfRecognitionEnabled;
  }

  public void setSelfRecognitionEnabled( boolean selfRecognitionEnabled )
  {
    this.selfRecognitionEnabled = selfRecognitionEnabled;
  }

  public boolean isShowInBudgetTracker()
  {
    return showInBudgetTracker;
  }

  public void setShowInBudgetTracker( boolean showInBudgetTracker )
  {
    this.showInBudgetTracker = showInBudgetTracker;
  }

  public boolean isIncludePurl()
  {
    return includePurl;
  }

  public void setIncludePurl( boolean includePurl )
  {
    this.includePurl = includePurl;
  }

  public boolean isDisplayPurlInPurlTile()
  {
    return displayPurlInPurlTile;
  }

  public void setDisplayPurlInPurlTile( boolean displayPurlInPurlTile )
  {
    this.displayPurlInPurlTile = displayPurlInPurlTile;
  }

  public PurlPromotionMediaType getPurlPromotionMediaType()
  {
    return purlPromotionMediaType;
  }

  public void setPurlPromotionMediaType( PurlPromotionMediaType purlPromotionMediaType )
  {
    this.purlPromotionMediaType = purlPromotionMediaType;
  }

  public PurlPromotionMediaValue getPurlMediaValue()
  {
    return purlMediaValue;
  }

  public void setPurlMediaValue( PurlPromotionMediaValue purlMediaValue )
  {
    this.purlMediaValue = purlMediaValue;
  }

  public void setAllowRecognitionSendDate( boolean allowRecognitionSendDate )
  {
    this.allowRecognitionSendDate = allowRecognitionSendDate;
  }

  public boolean isAllowRecognitionSendDate()
  {
    return allowRecognitionSendDate;
  }

  public void addPromotionPublicRecognitionAudience( PromotionPublicRecognitionAudience promotionPublicRecognitionAudience )
  {
    promotionPublicRecognitionAudience.setPromotion( this );
    this.promotionPublicRecognitionAudiences.add( promotionPublicRecognitionAudience );
  }

  public Set getPromotionPublicRecognitionAudiences()
  {
    return promotionPublicRecognitionAudiences;
  }

  public void setPromotionPublicRecognitionAudiences( Set promotionPublicRecognitionAudiences )
  {
    this.promotionPublicRecognitionAudiences = promotionPublicRecognitionAudiences;
  }

  public boolean isAllowPublicRecognitionPoints()
  {
    return allowPublicRecognitionPoints;
  }

  public void setAllowPublicRecognitionPoints( boolean allowPublicRecognitionPoints )
  {
    this.allowPublicRecognitionPoints = allowPublicRecognitionPoints;
  }

  public Long getPublicRecogAwardAmountMin()
  {
    return publicRecogAwardAmountMin;
  }

  public void setPublicRecogAwardAmountMin( Long publicRecogAwardAmountMin )
  {
    this.publicRecogAwardAmountMin = publicRecogAwardAmountMin;
  }

  public Long getPublicRecogAwardAmountMax()
  {
    return publicRecogAwardAmountMax;
  }

  public void setPublicRecogAwardAmountMax( Long publicRecogAwardAmountMax )
  {
    this.publicRecogAwardAmountMax = publicRecogAwardAmountMax;
  }

  public void setPublicRecogBudgetMaster( BudgetMaster publicRecogBudgetMaster )
  {
    this.publicRecogBudgetMaster = publicRecogBudgetMaster;
  }

  public BudgetMaster getPublicRecogBudgetMaster()
  {
    return publicRecogBudgetMaster;
  }

  public PublicRecognitionAudienceType getPublicRecognitionAudienceType()
  {
    return publicRecognitionAudienceType;
  }

  public void setPublicRecognitionAudienceType( PublicRecognitionAudienceType publicRecognitionAudienceType )
  {
    this.publicRecognitionAudienceType = publicRecognitionAudienceType;
  }

  /**
   * @return the Audience objects from the Primary PromotionAudience objects
   */
  public Set getPublicRecognitionAudiencesAsAudiences()
  {
    if ( getPromotionPublicRecognitionAudiences() == null )
    {
      return null;
    }

    Set audiences = new LinkedHashSet( getPromotionPublicRecognitionAudiences().size() );
    for ( Iterator iter = getPromotionPublicRecognitionAudiences().iterator(); iter.hasNext(); )
    {
      PromotionPublicRecognitionAudience promotionPublicRecognitionAudience = (PromotionPublicRecognitionAudience)iter.next();
      audiences.add( promotionPublicRecognitionAudience.getAudience() );
    }

    return audiences;
  }

  public boolean isPublicRecBudgetUsed()
  {
    return getPublicRecogBudgetMaster() != null;
  }

  public boolean isPublicRecogAwardAmountTypeFixed()
  {
    return publicRecogAwardAmountTypeFixed;
  }

  public void setPublicRecogAwardAmountTypeFixed( boolean publicRecogAwardAmountTypeFixed )
  {
    this.publicRecogAwardAmountTypeFixed = publicRecogAwardAmountTypeFixed;
  }

  public Long getPublicRecogAwardAmountFixed()
  {
    return publicRecogAwardAmountFixed;
  }

  public void setPublicRecogAwardAmountFixed( Long publicRecogAwardAmountFixed )
  {
    this.publicRecogAwardAmountFixed = publicRecogAwardAmountFixed;
  }

  public Long getMaxDaysDelayed()
  {
    return maxDaysDelayed;
  }

  public void setMaxDaysDelayed( Long maxDaysDelayed )
  {
    this.maxDaysDelayed = maxDaysDelayed;
  }

  public void setBehaviorRequired( boolean behaviorRequired )
  {
    this.behaviorRequired = behaviorRequired;
  }

  public boolean isBehaviorRequired()
  {
    return behaviorRequired;
  }

  public boolean isMobAppEnabled()
  {
    return mobAppEnabled;
  }

  public void setMobAppEnabled( boolean mobAppEnabled )
  {
    this.mobAppEnabled = mobAppEnabled;
  }

  public boolean isPurlStandardMessageEnabled()
  {
    return purlStandardMessageEnabled;
  }

  public void setPurlStandardMessageEnabled( boolean purlStandardMessageEnabled )
  {
    this.purlStandardMessageEnabled = purlStandardMessageEnabled;
  }

  public String getPurlStandardMessage()
  {
    return purlStandardMessage;
  }

  public void setPurlStandardMessage( String purlStandardMessage )
  {
    this.purlStandardMessage = purlStandardMessage;
  }

  public String getDefaultContributorAvatar()
  {
    return defaultContributorAvatar;
  }

  public void setDefaultContributorAvatar( String defaultContributorAvatar )
  {
    this.defaultContributorAvatar = defaultContributorAvatar;
  }

  public String getDefaultContributorName()
  {
    return defaultContributorName;
  }

  public void setDefaultContributorName( String defaultContributorName )
  {
    this.defaultContributorName = defaultContributorName;
  }

  public String getContentResourceCMCode()
  {
    return contentResourceCMCode;
  }

  public void setContentResourceCMCode( String contentResourceCMCode )
  {
    this.contentResourceCMCode = contentResourceCMCode;
  }

  public void setPromotionBudgetSweeps( Set<PromotionBudgetSweep> promotionBudgetSweeps )
  {
    this.promotionBudgetSweeps = promotionBudgetSweeps;
  }

  public Set<PromotionBudgetSweep> getPromotionBudgetSweeps()
  {
    return promotionBudgetSweeps;
  }

  public boolean isIncludeCelebrations()
  {
    return includeCelebrations;
  }

  public void setIncludeCelebrations( boolean includeCelebrations )
  {
    this.includeCelebrations = includeCelebrations;
  }

  public Long getCelebrationDisplayPeriod()
  {
    return celebrationDisplayPeriod;
  }

  public void setCelebrationDisplayPeriod( Long celebrationDisplayPeriod )
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

  public PromotionCelebrationsVideoType getVideoPath()
  {
    return videoPath;
  }

  public void setVideoPath( PromotionCelebrationsVideoType videoPath )
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

  public PromotionCelebrationsImageType getCelebrationFillerImage1()
  {
    return celebrationFillerImage1;
  }

  public void setCelebrationFillerImage1( PromotionCelebrationsImageType celebrationFillerImage1 )
  {
    this.celebrationFillerImage1 = celebrationFillerImage1;
  }

  public PromotionCelebrationsImageType getCelebrationFillerImage2()
  {
    return celebrationFillerImage2;
  }

  public void setCelebrationFillerImage2( PromotionCelebrationsImageType celebrationFillerImage2 )
  {
    this.celebrationFillerImage2 = celebrationFillerImage2;
  }

  public PromotionCelebrationsImageType getCelebrationFillerImage3()
  {
    return celebrationFillerImage3;
  }

  public void setCelebrationFillerImage3( PromotionCelebrationsImageType celebrationFillerImage3 )
  {
    this.celebrationFillerImage3 = celebrationFillerImage3;
  }

  public PromotionCelebrationsImageType getCelebrationFillerImage4()
  {
    return celebrationFillerImage4;
  }

  public void setCelebrationFillerImage4( PromotionCelebrationsImageType celebrationFillerImage4 )
  {
    this.celebrationFillerImage4 = celebrationFillerImage4;
  }

  public PromotionCelebrationsImageType getCelebrationFillerImage5()
  {
    return celebrationFillerImage5;
  }

  public void setCelebrationFillerImage5( PromotionCelebrationsImageType celebrationFillerImage5 )
  {
    this.celebrationFillerImage5 = celebrationFillerImage5;
  }

  public Boolean getAnniversaryInYears()
  {
    return anniversaryInYears;
  }

  public void setAnniversaryInYears( Boolean anniversaryInYears )
  {
    this.anniversaryInYears = anniversaryInYears;
  }

  public PromotionCelebrationGenericEcardsType getCelebrationGenericEcard()
  {
    return celebrationGenericEcard;
  }

  public void setCelebrationGenericEcard( PromotionCelebrationGenericEcardsType celebrationGenericEcard )
  {
    this.celebrationGenericEcard = celebrationGenericEcard;
  }

  public boolean isFillerImg1AwardNumberEnabled()
  {
    return fillerImg1AwardNumberEnabled;
  }

  public void setFillerImg1AwardNumberEnabled( boolean fillerImg1AwardNumberEnabled )
  {
    this.fillerImg1AwardNumberEnabled = fillerImg1AwardNumberEnabled;
  }

  public boolean isFillerImg2AwardNumberEnabled()
  {
    return fillerImg2AwardNumberEnabled;
  }

  public void setFillerImg2AwardNumberEnabled( boolean fillerImg2AwardNumberEnabled )
  {
    this.fillerImg2AwardNumberEnabled = fillerImg2AwardNumberEnabled;
  }

  public boolean isFillerImg3AwardNumberEnabled()
  {
    return fillerImg3AwardNumberEnabled;
  }

  public void setFillerImg3AwardNumberEnabled( boolean fillerImg3AwardNumberEnabled )
  {
    this.fillerImg3AwardNumberEnabled = fillerImg3AwardNumberEnabled;
  }

  public boolean isFillerImg4AwardNumberEnabled()
  {
    return fillerImg4AwardNumberEnabled;
  }

  public void setFillerImg4AwardNumberEnabled( boolean fillerImg4AwardNumberEnabled )
  {
    this.fillerImg4AwardNumberEnabled = fillerImg4AwardNumberEnabled;
  }

  public boolean isFillerImg5AwardNumberEnabled()
  {
    return fillerImg5AwardNumberEnabled;
  }

  public void setFillerImg5AwardNumberEnabled( boolean fillerImg5AwardNumberEnabled )
  {
    this.fillerImg5AwardNumberEnabled = fillerImg5AwardNumberEnabled;
  }

  public boolean isServiceAnniversary()
  {
    return serviceAnniversary;
  }

  public void setServiceAnniversary( boolean serviceAnniversary )
  {
    this.serviceAnniversary = serviceAnniversary;
  }

  public String getCelebrationsDefaultMessageText()
  {
    String defaultMessageText = null;
    if ( this.defaultMessage != null )
    {
      defaultMessageText = CmsResourceBundle.getCmsBundle().getString( this.defaultMessage, Promotion.RECOGNITION_CELEBRATIONS_MESSAGE_CM_ASSET_TYPE_KEY );
    }
    return defaultMessageText;
  }

  public boolean isAllowDefaultMessage()
  {
    return allowDefaultMessage;
  }

  public void setAllowDefaultMessage( boolean allowDefaultMessage )
  {
    this.allowDefaultMessage = allowDefaultMessage;
  }

  // Client customizations for WIP #62128 starts
  public boolean isCheersPromotion()
  {
    return cheersPromotion;
  }

  public void setCheersPromotion( boolean cheersPromotion )
  {
    this.cheersPromotion = cheersPromotion;
  }
  // Client customizations for WIP #62128 end

/**
 * @return the emailEnabled
 */
public boolean isEmailEnabled() {
	return emailEnabled;
}

/**
 * @param emailEnabled the emailEnabled to set
 */
public void setEmailEnabled(boolean emailEnabled) {
	this.emailEnabled = emailEnabled;
}

/**
 * @return the emailConfirmEnabled
 */
public boolean isEmailConfirmEnabled() {
	return emailConfirmEnabled;
}

/**
 * @param emailConfirmEnabled the emailConfirmEnabled to set
 */
public void setEmailConfirmEnabled(boolean emailConfirmEnabled) {
	this.emailConfirmEnabled = emailConfirmEnabled;
}


// Changes related to wip #23129 starts
public boolean isApqConversion()
{
  return apqConversion;
}

public void setApqConversion( boolean apqConversion )
{
  this.apqConversion = apqConversion;
}
// Changes related to wip #23129 ends
//Client customization start
public boolean isAllowMeme()
{
  return allowMeme;
}

public void setAllowMeme( boolean allowMeme )
{
  this.allowMeme = allowMeme;
}

public boolean isAllowSticker()
{
  return allowSticker;
}

public void setAllowSticker( boolean allowSticker )
{
  this.allowSticker = allowSticker;
}

public boolean isAllowUploadOwnMeme()
{
  return allowUploadOwnMeme;
}

public void setAllowUploadOwnMeme( boolean allowUploadOwnMeme )
{
  this.allowUploadOwnMeme = allowUploadOwnMeme;
}
//Client customization end
}
