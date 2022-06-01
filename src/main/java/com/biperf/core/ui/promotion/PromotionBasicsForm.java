/**
 *
 */

package com.biperf.core.ui.promotion;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.domain.enums.ApproverType;
import com.biperf.core.domain.enums.ChallengePointAwardType;
import com.biperf.core.domain.enums.MerchGiftCodeType;
import com.biperf.core.domain.enums.NominationAwardGroupSizeType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationAwardSpecifierType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.ProgressLoadType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionCertificate;
import com.biperf.core.domain.enums.PromotionIssuanceType;
import com.biperf.core.domain.enums.PromotionProcessingModeType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.PurlPromotionMediaType;
import com.biperf.core.domain.enums.PurlPromotionMediaValue;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.enums.SweepstakesWinnersType;
import com.biperf.core.domain.enums.TeamUnavailableResolverType;
import com.biperf.core.domain.enums.ThrowdownPromotionType;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.EngagementPromotion;
import com.biperf.core.domain.promotion.EngagementPromotions;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.promotion.WellnessPromotion;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimFormAssociationRequest;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.cms.CMDataElement;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.ServiceLocator;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.PromotionBasicsBadgeFormBean;
import com.biperf.core.value.PurlMediaUploadValue;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.enums.DataTypeEnum;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * PromotionBasicsForm.
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
 * <td>sondgero</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class PromotionBasicsForm extends BaseForm
{
  private static final String CM_BADGE_NAME_HTML_KEY = "HTML_KEY";
  private String promotionId;
  private String processingMode;
  private boolean batchProcessing;
  private String awardsType;
  private String awardsTypeName;
  // Bug fix # 24645
  private boolean taxable = true;
  private String activityForm;
  private String activityFormName;
  private String promotionName;
  private String promotionObjective;
  private String startDate = DateUtils.displayDateFormatMask;
  private String endDate = DateUtils.displayDateFormatMask;
  private String goalSelectionStartDate = DateUtils.displayDateFormatMask;
  private String goalSelectionEndDate = DateUtils.displayDateFormatMask;
  private String challengePointAwardType;
  private String programNumber;
  private String finalProcessDateString = DateUtils.displayDateFormatMask;
  private String returnActionUrl;
  private String promotionTypeCode;
  private String promotionTypeName;
  private String promotionStatus;
  private String expired;
  private String live;
  private long version;
  private String liveOrExpired = "false";
  private int screen;
  private String method;
  private long dateCreated;
  private String createdBy;
  private String issuanceMethod;
  private boolean recognitionCopy;
  private boolean allowManagerAward;
  private boolean copyOthers;
  private Long mgrAwardPromotionId;
  private boolean publicationDateActive;
  private String publicationDate = DateUtils.displayDateFormatMask;
  private String approvalEndDate;
  private long quizId;
  private String quizName;

  // method of netry of basics
  private boolean onlineEntry;
  private boolean fileLoadEntry;
  private String programId;
  private boolean includePurl = false;
  private boolean displayPurlInPurlTile = true;
  private boolean allowPublicRecognition = false;
  private boolean allowPromotionPrivate = false;
  private String purlPromotionMediaType;
  private String purlMediaValue;
  private boolean allowRecognitionSendDate = false;

  // parent data
  private boolean hasParent;
  private String parentPromotionName;
  private String parentPromotionActivityFormName;
  private String parentStartDate;
  private String parentEndDate;
  private boolean parentOnlineEntry;
  private boolean parentFileLoadEntry;

  private SweepstakesWinnersType sweepstakeWinnerType;
  private boolean allowUnlimitedAttempts;
  private int maximumAttempts;
  private boolean selfNomination;

  private String evaluationType;
  private String awardGroupMethod;
  private String purpose;
  private int teamMaxGroupMembers;
  private int bothMaxGroupMembers;
  private NominationAwardSpecifierType nomAwardSpecifierType;

  // DIY Quiz
  private Long badgeId;
  private List<PromotionBasicsBadgeFormBean> promotionBasicsBadgeFormBeanList = new ArrayList<PromotionBasicsBadgeFormBean>();
  private List<PromotionCertificateFormBean> promotionCertificateFormBeanList = new ArrayList<PromotionCertificateFormBean>();

  private String certificate;
  private String progressLoadTypeCode;
  private String progressLoadTypeName;

  // GQ Partner
  private String gqpartnersEnabled = "false";
  private String merchGiftCodeType = null;

  private String maxDaysDelayed;

  // CR - Convert to PerQs - START
  private boolean apqConversion;

  private String tileDisplayStartDate = DateUtils.displayDateFormatMask;
  private String tileDisplayEndDate = DateUtils.displayDateFormatMask;

  private String overviewDetailsText;
  private String overview;

  // Throwdown

  private String promotionTheme;
  private String promotionThemeName;
  private String unevenPlaySelection;
  private String unevenPlaySelectionName;
  private boolean displayTeamProgress = true;
  private String daysPriorToRoundStartSchedule = "2";
  private boolean smackTalkAvailable = true;

  // Survey data
  private boolean corpAndMngr;

  // Recognition Mob App
  private boolean mobAppEnabled = false;

  // PURL Auto Contribution Message fields
  private static final int MEDIA_DEFAULT_SIZE = 20;

  private boolean purlStandardMessageEnabled = false;
  private String purlStandardMessage;
  private String defaultContributorAvatar;
  private String defaultContributorName;
  private String contributor_uploadmedia_pho;
  private FormFile fileAsset;
  private List<PurlMediaUploadValue> mediaUploads;
  private String data;
  private String mediaUrl;
  private String mediaFilePath;
  private String imageUrlPath = "";
  private String imageUrl = "";
  private String imagePicUrlPath = "";
  private String imagePicUrl = "";
  private FormFile fileAssetPic;
  private String contributor_uploadmedia_pho_pic;
  private String uploadType;
  private String videoUrl;
  private String videoUrlMp4;
  private String videoUrl3gp;
  private String videoUrlOgg;
  private String videoUrlWebm;
  private List<FormFile> fileAssetVideo;
  private String defaultContributorPicture;

  // Engagement
  private Long[] engagementSelectedPromos;
  private Long[] engagementNotSelectedPromos;
  private boolean engagementEligiblePromotionsModified;

  // Recognition Promotion Celebrations Fields
  private boolean includeCelebrations;

  // SSI
  private String[] selectedContests;
  private String maxContestsToDisplay = "5";
  private String daysToArchive = "14";

  private FormFile fileAssetPdf;
  private String contestFilePath;

  private String teamAwardGroupSizeType;
  private String bothAwardGroupSizeType;

  private boolean whyNomination = true;

  private boolean viewPastWinners = false;

  private boolean includeUnderArmour = false;
  
  private boolean levelSelectionByApprover;// Client customization f
  // Client customization for WIP #58122 starts
  private boolean levelPayoutByApproverAvailable;
  private Integer capPerPax;
  // Client customization for WIP #58122 ends
  // Client customization for WIP #59420 starts
  private String allowedFileTypes;
  // Client customizations for WIP #62128 starts
  private boolean allowCheersPromotion = false;
  // Client customizations for WIP #62128 end.
  


  public String getAllowedFileTypes()
  {
    return allowedFileTypes;
  }

  public void setAllowedFileTypes( String allowedFileTypes )
  {
    this.allowedFileTypes = allowedFileTypes;
  }
  // Client customization for WIP #59420 ends
  
//Client customizations for WIP #59418 
  private String teamCmAssetText;
  
  /*Customization WIP#42198 start*/
  private Boolean  cashEnabled= Boolean.FALSE;
  private Long maxAwardInUSD ;
  
  // Client customization for WIP #39189 starts
  private Boolean enableFileUploader;
  private Integer minNumberOfFiles;
  private Integer maxNumberOfFiles;
  // Client customization for WIP #39189 ends
  

  public Long getMaxAwardInUSD() {
	return maxAwardInUSD;
}

public void setMaxAwardInUSD(Long maxAwardInUSD) {
	this.maxAwardInUSD = maxAwardInUSD;
}

public Boolean  getCashEnabled() {
	return cashEnabled;
}

public void setCashEnabled(Boolean cashEnabled) {
	this.cashEnabled = cashEnabled;
}
  public boolean isApqConversion()
  {
    return apqConversion;
  }

  public void setApqConversion( boolean apqConversion )
  {
    this.apqConversion = apqConversion;
  }

  // CR - Convert to PerQs - END

  public String getCertificate()
  {
    return certificate;
  }

  public void setCertificate( String certificate )
  {
    this.certificate = certificate;
  }

  public NominationAwardSpecifierType getNomAwardSpecifierType()
  {
    return nomAwardSpecifierType;
  }

  public void setNomAwardSpecifierType( NominationAwardSpecifierType nomAwardSpecifierType )
  {
    this.nomAwardSpecifierType = nomAwardSpecifierType;
  }

  public Long getBadgeId()
  {
    return badgeId;
  }

  public void setBadgeId( Long badgeId )
  {
    this.badgeId = badgeId;
  }

  public List<PromotionBasicsBadgeFormBean> getPromotionBasicsBadgeFormBeanList()
  {
    return promotionBasicsBadgeFormBeanList;
  }

  public void setPromotionBasicsBadgeFormBeanList( List<PromotionBasicsBadgeFormBean> promotionBasicsBadgeFormBeanList )
  {
    this.promotionBasicsBadgeFormBeanList = promotionBasicsBadgeFormBeanList;
  }

  public List<PromotionCertificateFormBean> getPromotionCertificateFormBeanList()
  {
    return promotionCertificateFormBeanList;
  }

  public void setPromotionCertificateFormBeanList( List<PromotionCertificateFormBean> promotionCertificateFormBeanList )
  {
    this.promotionCertificateFormBeanList = promotionCertificateFormBeanList;
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    super.reset( mapping, request );

    // Ensures the checkbox was checked in order to be true
    // (otherwise unchecking the box does not update the value)

    // this.recognitionCopy = false;
    this.selfNomination = false;
    mediaUploads = getEmptyMediaUploads( MEDIA_DEFAULT_SIZE );
  }

  private List<PurlMediaUploadValue> getEmptyMediaUploads( int count )
  {
    List<PurlMediaUploadValue> list = new ArrayList<PurlMediaUploadValue>();
    for ( int i = 0; i < count; ++i )
    {
      list.add( new PurlMediaUploadValue() );
    }
    return list;
  }

  private List<PurlMediaUploadValue> getEmptyPictureMediaUploads( int count )
  {
    List<PurlMediaUploadValue> list = new ArrayList<PurlMediaUploadValue>();
    for ( int i = 0; i < count; ++i )
    {
      list.add( new PurlMediaUploadValue() );
    }
    return list;
  }

  /**
   * @param method
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * @return method Strut action uses
   */
  public String getMethod()
  {
    return this.method;
  }

  /**
   * @return screen
   */
  public int _getScreen()
  {
    return this.screen;
  }

  /**
   * @param screen
   */
  public void _setScreen( int screen )
  {
    this.screen = screen;
  }

  /**
   * @return processingMode
   */
  public String getProcessingMode()
  {
    return processingMode;
  }

  /**
   * @param processingMode
   */
  public void setProcessingMode( String processingMode )
  {
    this.processingMode = processingMode;
  }

  /**
   * @return taxable
   */
  public boolean isTaxable()
  {
    return taxable;
  }

  /**
   * @param taxable
   */
  public void setTaxable( boolean taxable )
  {
    this.taxable = taxable;
  }

  /**
   * @return true if promotion is batch processing; false if not (real time)
   */
  public boolean isBatchProcessing()
  {
    return batchProcessing;
  }

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

  /**
   * @param batchProcessing
   */
  public void setBatchProcessing( boolean batchProcessing )
  {
    this.batchProcessing = batchProcessing;
  }

  /**
   * @return activityForm
   */
  public String getActivityForm()
  {
    return activityForm;
  }

  /**
   * @param activityForm
   */
  public void setActivityForm( String activityForm )
  {
    this.activityForm = activityForm;
  }

  /**
   * @return activityFormName
   */
  public String getActivityFormName()
  {
    return activityFormName;
  }

  /**
   * @param activityFormName
   */
  public void setActivityFormName( String activityFormName )
  {
    this.activityFormName = activityFormName;
  }

  /**
   * @return promotionName
   */
  public String getPromotionName()
  {
    return promotionName;
  }

  /**
   * @param promotionName
   */
  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  /**
   * @return endDate
   */
  public String getEndDate()
  {
    return endDate;
  }

  /**
   * @param endDate
   */
  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  /**
   * @return startDate
   */
  public String getStartDate()
  {
    return startDate;
  }

  /**
   * @param startDate
   */
  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  /**
   * @return returnActionUrl
   */
  public String getReturnActionUrl()
  {
    return returnActionUrl;
  }

  /**
   * @param returnActionUrl
   */
  public void setReturnActionUrl( String returnActionUrl )
  {
    this.returnActionUrl = returnActionUrl;
  }

  /**
   * @return promotionType
   */
  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  /**
   * @param promotionTypeCode
   */
  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  /**
   * @return promotionTypeName
   */
  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  /**
   * @param promotionTypeName
   */
  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  /**
   * @return promotionId
   */
  public String getPromotionId()
  {
    return promotionId;
  }

  /**
   * @param promotionId
   */
  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * @return expired
   */
  public String getExpired()
  {
    return expired;
  }

  /**
   * @param expired
   */
  public void setExpired( String expired )
  {
    this.expired = expired;
    if ( "true".equals( expired ) )
    {
      setLiveOrExpired( expired );
    }
  }

  /**
   * @return live
   */
  public String getLive()
  {
    return live;
  }

  /**
   * @param live
   */
  public void setLive( String live )
  {
    this.live = live;
    if ( "true".equals( live ) )
    {
      setLiveOrExpired( live );
    }
  }

  /**
   * This exists solely to allow the validation XML test to work, since validation tests only allow
   * one 'or'. In other words the following test is not allowed: ((expired == "true") or (live ==
   * "true") or (*this* != null))
   * 
   * @return boolean
   */
  public String isLiveOrExpired()
  {
    return liveOrExpired;
  }

  /**
   * @param liveOrExpired
   */
  public void setLiveOrExpired( String liveOrExpired )
  {
    this.liveOrExpired = liveOrExpired;
  }

  /**
   * Load the form
   * 
   * @param promotion
   */
  public void load( Promotion promotion )
  {

    if ( promotion.getId() != null )
    {
      this.promotionId = String.valueOf( promotion.getId() );
    }
    else
    {
      this.promotionId = "";
    }

    if ( promotion.isProductClaimPromotion() )
    {
      PromotionProcessingModeType promoProcessingMode = ( (ProductClaimPromotion)promotion ).getPromotionProcessingMode();

      if ( null != promoProcessingMode )
      {
        this.processingMode = String.valueOf( promoProcessingMode.getCode() );
        this.batchProcessing = Boolean.valueOf( promoProcessingMode.getCode().equals( "batch" ) ).booleanValue();
      }
    }

    this.promotionName = promotion.getPromotionName();
    /*Customization WIP#42198 start*/
    this.cashEnabled=promotion.getAdihCashOption();
    if(this.cashEnabled == null){
    	this.cashEnabled= false;
    	this.maxAwardInUSD=0L;
    	  	
    }
    else if((this.cashEnabled== true)  ){
    	 this.maxAwardInUSD=promotion.getAdihCashMaxAward();
    }else{
    	this.maxAwardInUSD= 0L;
    }
    /*Customization WIP#42198 End*/
    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promotion;
      this.promotionObjective = goalQuestPromotion.getObjective();
      this.merchGiftCodeType = null != goalQuestPromotion.getMerchGiftCodeType() ? goalQuestPromotion.getMerchGiftCodeType().getCode() : null;
      this.overview = promotion.getOverview();
      this.overviewDetailsText = promotion.getOverviewDetailsText();

    }

    if ( promotion.isThrowdownPromotion() )
    {
      ThrowdownPromotion throwdownPromotion = (ThrowdownPromotion)promotion;
      this.promotionTheme = throwdownPromotion.getThrowdownPromotionType().getCode();
      this.unevenPlaySelection = throwdownPromotion.getTeamUnavailableResolverType().getCode();
      this.overview = throwdownPromotion.getOverview();
      this.overviewDetailsText = throwdownPromotion.getOverviewDetailsText();
      this.displayTeamProgress = throwdownPromotion.isDisplayTeamProgress();
      this.daysPriorToRoundStartSchedule = String.valueOf( throwdownPromotion.getDaysPriorToRoundStartSchedule() );
      this.smackTalkAvailable = throwdownPromotion.isSmackTalkAvailable();
    }

    if ( promotion.getAwardType() != null )
    {
      this.awardsType = promotion.getAwardType().getCode();
      this.awardsTypeName = promotion.getAwardType().getName();
    }
    this.taxable = promotion.isTaxable();

    if ( !promotion.isGoalQuestPromotion() && !promotion.isWellnessPromotion() )
    {
      if ( promotion.getClaimForm() != null )
      {
        this.activityForm = String.valueOf( promotion.getClaimForm().getId() );
        this.activityFormName = promotion.getClaimForm().getName();
      }
    }

    if ( promotion.isQuizPromotion() )
    {
      QuizPromotion quizPromotion = (QuizPromotion)promotion;

      if ( quizPromotion.getQuiz() != null )
      {
        this.activityForm = String.valueOf( quizPromotion.getQuiz().getId() );
        this.activityFormName = quizPromotion.getQuiz().getName();
      }
      this.overview = promotion.getOverview();
      this.overviewDetailsText = promotion.getOverviewDetailsText();

    }

    if ( promotion.getSubmissionStartDate() != null )
    {
      this.startDate = DateUtils.toDisplayString( promotion.getSubmissionStartDate() );
    }

    if ( promotion.getSubmissionEndDate() != null )
    {
      this.endDate = DateUtils.toDisplayString( promotion.getSubmissionEndDate() );
    }

    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.purpose = promotion.getPurpose();
    if ( promotion.getPromotionStatus() != null )
    {
      this.promotionStatus = promotion.getPromotionStatus().getCode();
    }
    this.expired = String.valueOf( promotion.isExpired() );
    this.live = String.valueOf( promotion.isLive() );
    if ( promotion.getVersion() != null )
    {
      this.version = promotion.getVersion().longValue();
    }

    if ( null != promotion.getAuditCreateInfo().getDateCreated() )
    {
      this.dateCreated = promotion.getAuditCreateInfo().getDateCreated().getTime();
    }

    if ( promotion.getAuditCreateInfo().getCreatedBy() != null )
    {
      this.createdBy = promotion.getAuditCreateInfo().getCreatedBy().toString();
    }

    // load parent data if it exists
    this.hasParent = promotion.hasParent();
    if ( hasParent )
    {
      ProductClaimPromotion parentPromotion = ( (ProductClaimPromotion)promotion ).getParentPromotion();

      this.parentPromotionName = parentPromotion.getName();

      // child must have same activity form
      this.parentPromotionActivityFormName = parentPromotion.getClaimForm().getName();

      // child must have same method of entry
      this.parentOnlineEntry = parentPromotion.isOnlineEntry();
      this.parentFileLoadEntry = parentPromotion.isFileLoadEntry();

      if ( parentPromotion.getSubmissionStartDate() != null )
      {
        this.parentStartDate = DateUtils.toDisplayString( parentPromotion.getSubmissionStartDate() );
      }

      if ( parentPromotion.getSubmissionEndDate() != null )
      {
        this.parentEndDate = DateUtils.toDisplayString( parentPromotion.getSubmissionEndDate() );
      }

    }

    // Get RecognitionPromotion data
    if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
      /*
       * PromotionIssuanceType issuanceType = recognitionPromotion.getIssuanceType(); if (
       * issuanceType == null ) { this.issuanceMethod = PickListItem.getDefaultItem(
       * PromotionIssuanceType.class ).getCode(); } else { this.issuanceMethod =
       * issuanceType.getCode(); }
       */
      if ( promotion.isFileLoadEntry() )
      {
        this.issuanceMethod = PromotionIssuanceType.FILE_LOAD;
      }
      if ( promotion.isOnlineEntry() )
      {
        this.issuanceMethod = PromotionIssuanceType.ONLINE;
      }
      if ( this.issuanceMethod == null || "".equals( this.issuanceMethod ) )
      {
        this.issuanceMethod = PickListItem.getDefaultItem( PromotionIssuanceType.class ).getCode();
      }

      this.includePurl = recognitionPromotion.isIncludePurl();
      this.displayPurlInPurlTile = recognitionPromotion.isDisplayPurlInPurlTile();
      this.allowPublicRecognition = recognitionPromotion.isAllowPublicRecognition();
      this.allowPromotionPrivate = recognitionPromotion.isAllowPromotionPrivate();
      this.recognitionCopy = recognitionPromotion.isCopyRecipientManager();
      this.allowManagerAward = recognitionPromotion.isAllowManagerAward();
      this.copyOthers = recognitionPromotion.isCopyOthers();
      this.allowRecognitionSendDate = recognitionPromotion.isAllowRecognitionSendDate();
      if ( this.allowRecognitionSendDate )
      {
        this.maxDaysDelayed = "" + recognitionPromotion.getMaxDaysDelayed().intValue();
      }

      if ( this.allowManagerAward )
      {
        this.mgrAwardPromotionId = recognitionPromotion.getMgrAwardPromotionId();
        // this.calculatorId =
        // promotion.getCalculator().getId();recognitionPromotion.getMgrAwardPromotionId() ;
      }
      else
      {
        this.mgrAwardPromotionId = new Long( 0 );
      }

      if ( this.includePurl )
      {
        if ( recognitionPromotion.getPurlPromotionMediaType() != null )
        {
          this.purlPromotionMediaType = recognitionPromotion.getPurlPromotionMediaType().getCode();
        }

        if ( recognitionPromotion.getPurlMediaValue() != null )
        {
          this.purlMediaValue = recognitionPromotion.getPurlMediaValue().getCode();
        }

        this.purlStandardMessageEnabled = recognitionPromotion.isPurlStandardMessageEnabled();
        if ( purlStandardMessageEnabled )
        {
          this.purlStandardMessage = recognitionPromotion.getPurlStandardMessage();
          this.defaultContributorName = recognitionPromotion.getDefaultContributorName();
          this.imageUrl = recognitionPromotion.getDefaultContributorAvatar();
        }
      }

      this.mobAppEnabled = recognitionPromotion.isMobAppEnabled();

      this.includeCelebrations = recognitionPromotion.isIncludeCelebrations();
      this.allowCheersPromotion = recognitionPromotion.isCheersPromotion();
      

    }

    // Get Nomination Promotion data
    if ( promotion.isNominationPromotion() )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)promotion;

      if ( nominationPromotion.getAwardGroupType() != null )
      {
        this.awardGroupMethod = nominationPromotion.getAwardGroupType().getCode();
      }
      if ( nominationPromotion.getEvaluationType() != null )
      {
        this.evaluationType = nominationPromotion.getEvaluationType().getCode();
      }
      if ( nominationPromotion.getMaxGroupMembers() != null && this.awardGroupMethod != null )
      {
        if ( this.awardGroupMethod.equals( NominationAwardGroupType.TEAM ) )
        {
          this.teamMaxGroupMembers = nominationPromotion.getMaxGroupMembers().intValue();

          if ( nominationPromotion.getAwardGroupSizeType() != null )
          {
            this.teamAwardGroupSizeType = nominationPromotion.getAwardGroupSizeType().getCode();
          }
        }
        else if ( this.awardGroupMethod.equals( NominationAwardGroupType.INDIVIDUAL_OR_TEAM ) )
        {
          this.bothMaxGroupMembers = nominationPromotion.getMaxGroupMembers().intValue();

          if ( nominationPromotion.getAwardGroupSizeType() != null )
          {
            this.bothAwardGroupSizeType = nominationPromotion.getAwardGroupSizeType().getCode();
          }
        }
      }

      this.selfNomination = nominationPromotion.isSelfNomination();
      this.levelSelectionByApprover = nominationPromotion.isLevelSelectionByApprover();//Client customization for WIP #56492
      // Client customization for WIP 58122
      this.levelPayoutByApproverAvailable = nominationPromotion.isLevelPayoutByApproverAvailable();//Client customization for WIP #58122
      this.capPerPax = nominationPromotion.getCapPerPax();//Client customization for WIP #58122
    //Client customizations for WIP #59418 starts
      if ( nominationPromotion.getTeamCmAsset() != null && nominationPromotion.getTeamCmKey() != null )
      {
        this.teamCmAssetText = CmsResourceBundle.getCmsBundle().getString( nominationPromotion.getTeamCmAsset(),
                nominationPromotion.getTeamCmKey() );
      }
    //Client customizations for WIP #59418 ends
      this.allowPublicRecognition = nominationPromotion.isAllowPublicRecognition();
      this.allowPromotionPrivate = nominationPromotion.isAllowPromotionPrivate();
      this.publicationDateActive = nominationPromotion.isPublicationDateActive();
      this.whyNomination = nominationPromotion.isWhyNomination();
      this.viewPastWinners = nominationPromotion.isViewPastWinners();
      if ( nominationPromotion.getPublicationDate() != null )
      {
        this.publicationDate = DateUtils.toDisplayString( nominationPromotion.getPublicationDate() );
      }
      if ( nominationPromotion.getApprovalEndDate() != null )
      {
        this.approvalEndDate = DateUtils.toDisplayString( nominationPromotion.getApprovalEndDate() );
      }
      // Client customizations for WIP #39189 starts
      this.enableFileUploader = nominationPromotion.isEnableFileUpload();
      if ( nominationPromotion.isEnableFileUpload() )
      {
        this.minNumberOfFiles = nominationPromotion.getFileMinNumber();
        this.maxNumberOfFiles = nominationPromotion.getFileMaxNumber();
        this.allowedFileTypes = nominationPromotion.getAllowedFileTypes();
      }
      // Client customizations for WIP #39189 ends

    }

    if ( promotion.isQuizPromotion() )
    {
      QuizPromotion quizPromotion = (QuizPromotion)promotion;
      this.sweepstakeWinnerType = quizPromotion.getSweepstakesPrimaryBasisType();
      this.quizName = quizPromotion.getName();

      if ( quizPromotion.isAllowUnlimitedAttempts() )
      {
        this.allowUnlimitedAttempts = true;
      }
      else
      {
        this.allowUnlimitedAttempts = false;
        this.maximumAttempts = quizPromotion.getMaximumAttempts();
      }
    }

    if ( promotion.isRecognitionPromotion() || promotion.isQuizPromotion() )
    {
      this.certificate = promotion.getCertificate();
    }

    if ( promotion.isDIYQuizPromotion() )
    {
      QuizPromotion quizPromotion = (QuizPromotion)promotion;
      if ( quizPromotion.getPromotionCertificates() != null && !quizPromotion.getPromotionCertificates().isEmpty() )
      {
        List<Content> certificateList = PromotionCertificate.getList( promotionTypeCode );
        for ( int i = 0; i < certificateList.size(); i++ )
        {
          Content content = certificateList.get( i );
          boolean found = false;
          for ( PromotionCert promotionCert : quizPromotion.getPromotionCertificates() )
          {
            if ( content.getContentDataMap().get( "ID" ).equals( promotionCert.getCertificateId() ) )
            {
              PromotionCertificateFormBean certificateFormBean = new PromotionCertificateFormBean();
              certificateFormBean.setCertificateId( promotionCert.getCertificateId() );
              certificateFormBean.setId( promotionCert.getId() );
              certificateFormBean.setSelected( true );
              if ( quizPromotion.isLive() )
              {
                certificateFormBean.setDisable( "true" );
              }
              else
              {
                certificateFormBean.setDisable( "false" );
              }
              this.promotionCertificateFormBeanList.add( certificateFormBean );
              found = true;
              break;
            }
          }
          if ( !found )
          {
            PromotionCertificateFormBean certificateFormBean = new PromotionCertificateFormBean();
            certificateFormBean.setDisable( "false" );
            this.promotionCertificateFormBeanList.add( certificateFormBean );
          }
        }
      }
      if ( quizPromotion.getBadge() != null )
      {
        this.badgeId = quizPromotion.getBadge().getId();
        if ( quizPromotion.getBadge().getBadgeRules() != null && !quizPromotion.getBadge().getBadgeRules().isEmpty() )
        {
          List<BadgeLibrary> badgeList = getPromotionService().buildBadgeLibraryList();
          List<BadgeRule> badgeRulesList = new ArrayList<BadgeRule>( quizPromotion.getBadge().getBadgeRules() );
          for ( BadgeLibrary badge : badgeList )
          {
            boolean found = false;
            for ( BadgeRule badgeRule : badgeRulesList )
            {
              if ( badgeRule.getBadgeLibraryCMKey().equals( badge.getBadgeLibraryId() ) )
              {
                PromotionBasicsBadgeFormBean badgeFormBean = new PromotionBasicsBadgeFormBean();
                badgeFormBean.setBadgeRuleId( badgeRule.getId() );
                badgeFormBean.setBadgeId( badgeRule.getBadgePromotion().getId() );
                badgeFormBean.setCmAssetKey( badgeRule.getBadgeLibraryCMKey() );
                String desc = CmsResourceBundle.getCmsBundle().getString( badgeRule.getBadgeName().trim(), CM_BADGE_NAME_HTML_KEY );
                badgeFormBean.setBadgeName( desc );
                badgeFormBean.setSelected( true );
                if ( quizPromotion.isLive() )
                {
                  badgeFormBean.setDisable( "true" );
                }
                else
                {
                  badgeFormBean.setDisable( "false" );
                }
                this.promotionBasicsBadgeFormBeanList.add( badgeFormBean );
                found = true;
                break;
              }
            }
            if ( !found )
            {
              PromotionBasicsBadgeFormBean badgeFormBean = new PromotionBasicsBadgeFormBean();
              badgeFormBean.setDisable( "false" );
              this.promotionBasicsBadgeFormBeanList.add( badgeFormBean );
            }
          }
        }
      }
    }

    if ( promotion.isEngagementPromotion() )
    {
      EngagementPromotion engagementPromotion = (EngagementPromotion)promotion;
      List<Long> existingEligiblePromotions = new ArrayList<Long>();
      if ( engagementPromotion.getEngagementPromotions() != null )
      {
        for ( EngagementPromotions engagementPromotions : engagementPromotion.getEngagementPromotions() )
        {
          if ( engagementPromotions.getEligiblePromotion() != null && engagementPromotions.getEligiblePromotion().getId() != null )
          {
            existingEligiblePromotions.add( engagementPromotions.getEligiblePromotion().getId() );
          }
        }
      }
      this.setEngagementSelectedPromos( existingEligiblePromotions.toArray( new Long[0] ) );

      List<FormattedValueBean> engagementEligiblePromoList = getPromotionService().getEngagementEligiblePromotionList();
      List<Long> eligiblePromotionsNotSelected = new ArrayList<Long>();
      for ( FormattedValueBean valueBean : engagementEligiblePromoList )
      {
        if ( engagementSelectedPromos == null || !existingEligiblePromotions.contains( valueBean.getId() ) )
        {
          eligiblePromotionsNotSelected.add( valueBean.getId() );
        }
      }
      this.setEngagementNotSelectedPromos( eligiblePromotionsNotSelected.toArray( new Long[0] ) );
    }

    if ( promotion.isSurveyPromotion() )
    {
      SurveyPromotion surveyPromotion = (SurveyPromotion)promotion;
      this.corpAndMngr = surveyPromotion.isCorpAndMngr();

      if ( surveyPromotion.getSurvey() != null )
      {
        this.activityForm = String.valueOf( surveyPromotion.getSurvey().getId() );
        this.activityFormName = surveyPromotion.getSurvey().getName();
      }
    }

    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promotion;
      if ( goalQuestPromotion.getGoalCollectionStartDate() != null )
      {
        this.goalSelectionStartDate = DateUtils.toDisplayString( goalQuestPromotion.getGoalCollectionStartDate() );
      }

      if ( goalQuestPromotion.getGoalCollectionEndDate() != null )
      {
        this.goalSelectionEndDate = DateUtils.toDisplayString( goalQuestPromotion.getGoalCollectionEndDate() );
      }

      if ( goalQuestPromotion.getFinalProcessDate() != null )
      {
        this.finalProcessDateString = DateUtils.toDisplayString( goalQuestPromotion.getFinalProcessDate() );
      }

      if ( goalQuestPromotion.getProgramId() != null )
      {
        this.programId = goalQuestPromotion.getProgramId();
      }
      if ( goalQuestPromotion.getProgressLoadType() != null )
      {
        this.progressLoadTypeCode = goalQuestPromotion.getProgressLoadType().getCode();
        this.progressLoadTypeName = goalQuestPromotion.getProgressLoadType().getName();
      }
      // CR - Convert to PerQs - START
      this.apqConversion = goalQuestPromotion.isApqConversion();
      // CR - Convert to PerQs - END

      // Under Armour
      this.includeUnderArmour = goalQuestPromotion.isAllowUnderArmour();

      // A promotion must be a GoalQuest for it to be a ChallengePoint
      if ( promotion.getPromotionType().getCode().equals( PromotionType.CHALLENGE_POINT ) )
      {
        ChallengePointPromotion challengePointPromotion = (ChallengePointPromotion)promotion;

        if ( challengePointPromotion.getChallengePointAwardType() != null )
        {
          this.challengePointAwardType = challengePointPromotion.getChallengePointAwardType().getCode();

        }
        if ( challengePointPromotion.getAwardType() != null )
        {
          this.awardsType = challengePointPromotion.getAwardType().getCode();
          this.awardsTypeName = challengePointPromotion.getAwardType().getName();
        }
      }
    }

    if ( promotion.isWellnessPromotion() )
    {
      WellnessPromotion wellnessPromotion = (WellnessPromotion)promotion;

      if ( promotion.isFileLoadEntry() )
      {
        this.issuanceMethod = PromotionIssuanceType.FILE_LOAD;
      }
      if ( promotion.isOnlineEntry() )
      {
        this.issuanceMethod = PromotionIssuanceType.ONLINE;
      }
      if ( this.issuanceMethod == null || "".equals( this.issuanceMethod ) )
      {
        this.issuanceMethod = PickListItem.getDefaultItem( PromotionIssuanceType.class ).getCode();
      }

      // this.issuanceMethod = PromotionIssuanceType.FILE_LOAD;
    }

    if ( promotion.isSSIPromotion() )
    {
      SSIPromotion ssiPromotion = (SSIPromotion)promotion;
      List<String> selectedContestsList = new ArrayList<String>();
      if ( ssiPromotion.isAwardThemNowSelected() )
      {
        selectedContestsList.add( "1" );
      }
      if ( ssiPromotion.isDoThisGetThatSelected() )
      {
        selectedContestsList.add( "2" );
      }
      if ( ssiPromotion.isObjectivesSelected() )
      {
        selectedContestsList.add( "4" );
      }
      if ( ssiPromotion.isStackRankSelected() )
      {
        selectedContestsList.add( "8" );
      }
      if ( ssiPromotion.isStepItUpSelected() )
      {
        selectedContestsList.add( "16" );
      }
      this.selectedContests = selectedContestsList.toArray( new String[0] );

      this.maxContestsToDisplay = String.valueOf( ssiPromotion.getMaxContestsToDisplay() );
      this.daysToArchive = String.valueOf( ssiPromotion.getDaysToArchive() );
    }

    this.onlineEntry = promotion.isOnlineEntry();
    this.fileLoadEntry = promotion.isFileLoadEntry();
    this.tileDisplayStartDate = DateUtils.toDisplayString( promotion.getTileDisplayStartDate() );
    this.tileDisplayEndDate = DateUtils.toDisplayString( promotion.getTileDisplayEndDate() );
    /*Customization WIP#42198 start*/
    this.cashEnabled=promotion.getAdihCashOption();
    if(this.cashEnabled == null){
    	this.cashEnabled= false;
    	this.maxAwardInUSD=0L;
      	
    }
    else if(this.cashEnabled== true)
    {
    	this.maxAwardInUSD=promotion.getAdihCashMaxAward();
    }else{
    	this.maxAwardInUSD= 0L;
    }  /*Customization WIP#42198 End*/
  }

  public void load( String leftColumn,
                    String rightColumn,
                    String uploadTypeInput,
                    int numberColumns,
                    int slideNumber,
                    String mediaPath,
                    String videoUrlMp4,
                    String videoUrlWebm,
                    String videoUrl3gp,
                    String videoUrlOgg )
  {

    this.mediaFilePath = mediaPath;

    if ( uploadTypeInput.equalsIgnoreCase( "image" ) )
    {
      this.uploadType = uploadTypeInput;
      this.imageUrl = leftColumn;
    }
    else if ( uploadTypeInput.equalsIgnoreCase( "video" ) )
    {
      this.uploadType = uploadTypeInput;
      this.videoUrl = leftColumn;
      this.videoUrlMp4 = videoUrlMp4;
      this.videoUrlWebm = videoUrlWebm;
      this.videoUrl3gp = videoUrl3gp;
      this.videoUrlOgg = videoUrlOgg;
    }
  }

  private static boolean isEmpty( String str )
  {
    return str == null || str.length() == 0;
  }

  /**
   * Validate the form before submitting Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );

    String imageUrlFullPath = (String)request.getSession().getAttribute( "imageUrlFullPath" );

    this.imageUrl = imageUrlFullPath;

    // ***** Validate promoName length ( < 60 char) Bug# 9415 changed from 30 to 60 *****/
    // To Fix bug 21222 validate promotion name length (<50 char)changed from 60 to 50
    String promoName = request.getParameter( "promotionName" );
    Date d_StartDate = null;

    if ( promoName != null && promoName.length() > 50 )
    {
      errors.add( "promotionName", new ActionMessage( "promotion.basics.errors.NAME_TOO_LONG" ) );
    }

    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );

    // If not survey promotion or goalquest or cp or wellness
    if ( !PromotionType.GOALQUEST.equals( promotionTypeCode ) && !PromotionType.CHALLENGE_POINT.equals( promotionTypeCode ) && !PromotionType.WELLNESS.equals( promotionTypeCode )
        && !PromotionType.DIY_QUIZ.equals( promotionTypeCode ) && !PromotionType.THROWDOWN.equals( promotionTypeCode ) && !PromotionType.ENGAGEMENT.equals( promotionTypeCode )
        && !PromotionType.SELF_SERV_INCENTIVES.equals( promotionTypeCode ) )
    {
      // ***** Validate activityForm *****/
      String claimForm = request.getParameter( "activityForm" );
      // Make sure its not empty
      if ( claimForm == null || claimForm.length() == 0 )
      {
        errors.add( "activityForm", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.ACTIVITY_FORM" ) ) );
      }
    }

    Date dateStart = null;
    Date dateEnd = null;
    Date parentStart = null;
    Date parentEnd = null;

    String startKey = "promotion.basics.START_DATE";

    // ***** Validate promoStartDate *****/
    String promoStartDate = request.getParameter( "startDate" );

    // ***** Validate promoEndDate *****/
    String promoEndDate = request.getParameter( "endDate" );

    if ( PromotionType.GOALQUEST.equals( promotionTypeCode ) || PromotionType.CHALLENGE_POINT.equals( promotionTypeCode ) || PromotionType.THROWDOWN.equals( promotionTypeCode ) )
    {
      startKey = "promotion.basics.PROMOTION_START_DATE";
      sdf.setLenient( false );
    }
    else if ( PromotionType.ENGAGEMENT.equals( promotionTypeCode ) )
    {
      startKey = "promotion.basics.METRIC_START_DATE";
    }
    // Make sure its not empty
    if ( promoStartDate == null || promoStartDate.length() == 0 )
    {
      errors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( startKey ) ) );
    }
    else
    {
      // Now validate the date
      try
      {
        dateStart = sdf.parse( promoStartDate );
      }
      catch( ParseException e )
      {
        errors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( startKey ) ) );
      }
    }

    if ( PromotionType.GOALQUEST.equals( promotionTypeCode ) || PromotionType.CHALLENGE_POINT.equals( promotionTypeCode ) || PromotionType.THROWDOWN.equals( promotionTypeCode ) )
    {
      if ( promoEndDate == null || promoEndDate.length() == 0 )
      {
        errors.add( "endDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PROMOTION_END_DATE" ) ) );
      }

    }

    // ***** Validate promoEndDate *****/
    if ( endDate != null && endDate.length() > 0 )
    {
      try
      {
        dateEnd = sdf.parse( endDate );
        if ( !this.promotionStatus.equals( "live" ) && !this.promotionStatus.equals( "complete" ) )
        {
          if ( dateEnd.before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
          {
            // The date is before current date
            errors.add( "endDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_TO_DATE_PROMOTION ) );
          }
        }
        if ( dateStart != null && dateEnd.before( dateStart ) )
        {
          // The date is before the start date
          errors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE_RANGE_PROMOTION ) );
        }
      }
      catch( ParseException e )
      {
        errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.END" ) ) );
      }
    }

    // if an error refers to more than one field leave it global errors
    // If the promotion has a parent, then make sure the child dates come within the range
    // of the parent dates
    if ( hasParent )
    {
      try
      {
        parentStart = sdf.parse( parentStartDate );

        // Child start date can't be before parent start date
        if ( dateStart != null && dateStart.before( parentStart ) )
        {
          errors.add( "startDate", new ActionMessage( "promotion.basics.errors.CHILD_DATE_RANGE" ) );
        }
      }
      catch( ParseException e1 )
      {
        // This shouldn't ever happen, since a parent promotion must be completed
        errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PARENT_START" ) ) );
      }

      // If the parent has end dates, then check the child date ranges
      if ( parentEndDate != null && parentEndDate.length() > 0 )
      {
        try
        {
          parentEnd = sdf.parse( parentEndDate );

          // Child start date can't be after parent end date
          if ( dateStart != null && dateStart.after( parentEnd ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.basics.errors.CHILD_DATE_RANGE" ) );
          }

          // Child end date can't be null or after parent end date
          if ( dateEnd == null || dateEnd.after( parentEnd ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.basics.errors.CHILD_DATE_RANGE" ) );
          }
        }
        catch( ParseException e1 )
        {
          // This shouldn't ever happen, since a parent promotion must be completed
          errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PARENT_END" ) ) );
        }
      }
    } // end if(hasParent)

    if ( PromotionType.ENGAGEMENT.equals( promotionTypeCode ) )
    {
      if ( StringUtil.isEmpty( tileDisplayStartDate ) )
      {
        errors.add( "tileDisplayStartDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.DISPLAY_DATE" ) ) );
      }
      else
      {
        if ( promoStartDate != null && promoStartDate.length() > 0 )
        {
          if ( DateUtils.toDate( tileDisplayStartDate ).before( DateUtils.toDate( startDate ) ) )
          {
            errors.add( "tileDisplayStartDate",
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.DISPLAY_GT_START" ) ) );
          }
          else
          {
            try
            {
              sdf.parse( tileDisplayStartDate );
            }
            catch( ParseException e1 )
            {
              errors.add( "tileDisplayStartDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.DISPLAY_DATE" ) ) );
            }
          }
        }
      }

      if ( engagementSelectedPromos == null || engagementSelectedPromos.length == 0 )
      {
        errors.add( "engagementEligiblePromos",
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.SELECT_ELIGIBLE_PROMOTIONS" ) ) );
      }
    }

    if ( PromotionType.WELLNESS.equals( promotionTypeCode ) )
    {
      if ( issuanceMethod == null || issuanceMethod.length() == 0 )
      {
        errors.add( ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.METHOD_OF_ISSUANCE" ) ) );
      }
    }

    // If recognition promotion
    if ( PromotionType.RECOGNITION.equals( promotionTypeCode ) )
    {

      if ( issuanceMethod == null || issuanceMethod.length() == 0 )
      {
        errors.add( ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.METHOD_OF_ISSUANCE" ) ) );
      }

      // String strMgrAwardPromoId = request.getParameter("mgrAwardPromotionId");

      if ( allowManagerAward && mgrAwardPromotionId.equals( new Long( 0 ) ) )
      {
        errors.add( ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.errors.SELECT_MGR_PROMOTION" ) ) );

      }
      
      // Client customizations for WIP #62128 starts
      if ( allowCheersPromotion  )
      {
        Long cheersPromoId = getPromotionService().getCheersPromotionId();
        if ( Objects.nonNull( cheersPromoId ) && cheersPromoId > 0 
            && org.apache.commons.lang.StringUtils.isNotEmpty( promotionId ) && !cheersPromoId.equals( Long.parseLong( promotionId ) ) )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "client.cheers.recognition.ALLOW_ONE_CHEERS_PROMOTION" ) );
        }
      }
      // Client customizations for WIP #62128 ends
    }
    // If nomination promotion
    if ( PromotionType.NOMINATION.equals( promotionTypeCode ) )
    {

      if ( !org.apache.commons.lang3.StringUtils.isBlank( approvalEndDate ) && !org.apache.commons.lang3.StringUtils.isBlank( endDate ) )
      {
        try
        {
          Date approvalEndDateDate = sdf.parse( approvalEndDate );

          if ( dateEnd.after( approvalEndDateDate ) )
          {
            // The nomination end date is after the Nomination Approval End Date
            errors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( "promotion.basics.errors.DATE_AFTER_APPROVAL_DATE", CmsResourceBundle.getCmsBundle().getString( "promotion.basics.END" ), approvalEndDate ) );
          }
        }
        catch( ParseException e )
        {
          errors.add( "approvalDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.END" ) ) );
        }
      }
      if ( !org.apache.commons.lang3.StringUtils.isBlank( approvalEndDate ) && org.apache.commons.lang3.StringUtils.isBlank( endDate ) )
      {
        // Must have a nomination end date if there is an approval end date
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.basics.errors.END_DATE_MISSING", approvalEndDate ) );
      }

      if ( this.awardGroupMethod != null )
      {
        if ( this.awardGroupMethod.equals( NominationAwardGroupType.TEAM ) )
        {
          if ( teamAwardGroupSizeType == null )
          {
            errors.add( ActionMessages.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.errors.TEAM_SIZE" ) ) );
          }
        }
        else if ( this.awardGroupMethod.equals( NominationAwardGroupType.INDIVIDUAL_OR_TEAM ) )
        {
          if ( bothAwardGroupSizeType == null )
          {
            errors.add( ActionMessages.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.errors.INDIVIDUAL_TEAM_SIZE" ) ) );
          }
        }
      }

      if ( this.awardGroupMethod != null && ( this.awardGroupMethod.equals( NominationAwardGroupType.TEAM ) || this.awardGroupMethod.equals( NominationAwardGroupType.INDIVIDUAL_OR_TEAM ) ) )
      {
        if ( this.teamAwardGroupSizeType != null && this.teamAwardGroupSizeType.equals( NominationAwardGroupSizeType.LIMITED ) )
        {
          if ( this.teamMaxGroupMembers <= 1 )
          {
            errors.add( ActionMessages.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.errors.AWARD_GROUP_MAX_MEMBERS" ) ) );
          }
        }
        else if ( this.bothAwardGroupSizeType != null && this.bothAwardGroupSizeType.equals( NominationAwardGroupSizeType.LIMITED ) )
        {
          if ( this.bothMaxGroupMembers <= 1 )
          {
            errors.add( ActionMessages.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.errors.AWARD_GROUP_MAX_MEMBERS" ) ) );
          }
        }
      }

      if ( this.publicationDateActive )
      {
        if ( publicationDate == null || publicationDate.equals( "" ) )
        {

          errors.add( ActionMessages.GLOBAL_MESSAGE,
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.errors.PUB_DATE_NULL" ) ) );
        }
        else
        {
          try
          {
            Date datePublication = sdf.parse( publicationDate );
            if ( this.publicationDateActive && dateStart != null && datePublication.before( dateStart ) )
            {
              // The date is before the start date
              errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.basics.errors.PUB_DATE_BEFORE_START" ) );
            }

            if ( this.publicationDateActive && dateEnd != null && datePublication.before( DateUtils.getNextDay( dateEnd ) ) )
            {
              // The date is before the end date
              errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.basics.errors.PUB_DATE_BEFORE_END" ) );
            }

          }
          catch( ParseException e )
          {
            errors.add( "publicationDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.errors.PUB_DATE_NULL" ) ) );
          }
        }
      }

      // Restrict Activity Form if it have required fields for Mob App
      String claimFormString = request.getParameter( "activityForm" );
      if ( claimFormString != null && claimFormString.length() > 0 )
      {
        // Validate that "default why active" is not selected at the same time as a form with
        // "default why" element
        List<ClaimFormStepElement> formStepElements = getClaimFormService().getAllClaimFormStepElementsByClaimFormId( new Long( this.getActivityForm() ) );
        long numWhyFieldElements = formStepElements.stream().filter( ( el ) -> el.isWhyField() ).count();
        if ( whyNomination )
        {
          if ( numWhyFieldElements > 0 )
          {
            errors.add( "whyNomination", new ActionMessage( "promotion.basics.errors.DEFAULT_AND_FORM_WHY" ) );
          }
        }
        // Not "is default why" - must have a form element chosen as the why field
        else
        {
          if ( numWhyFieldElements == 0 )
          {
            errors.add( "whyNomination", new ActionMessage( "promotion.basics.errors.DEFAULT_NOR_FORM_WHY" ) );
          }
        }
      }
      
      // Client Customization for WIP #39189 starts
      if ( this.enableFileUploader == null )
      {
        errors.add( ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "client.nominationSubmit.ENABLE_FILE_UPLOAD" ) ) );
      }
      else if ( this.enableFileUploader.booleanValue() )
      {
        if ( this.minNumberOfFiles == null || this.minNumberOfFiles.intValue() < 0 )
        {
          errors.add( ActionMessages.GLOBAL_MESSAGE,
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "client.nominationSubmit.MIN_FILES" ) ) );
        }
        if ( this.maxNumberOfFiles == null || this.maxNumberOfFiles.intValue() <= 0 )
        {
          errors.add( ActionMessages.GLOBAL_MESSAGE,
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "client.nominationSubmit.MAX_FILES" ) ) );
        }
        if ( this.minNumberOfFiles != null && this.maxNumberOfFiles != null && this.minNumberOfFiles.intValue() > this.maxNumberOfFiles.intValue() )
        {
          errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "client.nominationSubmit.MAX_FILES_SHOULD_MORE" ) );
        }
        if ( StringUtil.isNullOrEmpty( this.allowedFileTypes ) )
        {
          errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "client.nominationSubmit.ALLOWED_FILE_TYPES" ) );// TODO
        }
      }
      // Client Customization for WIP #39189 ends

    }

    if ( PromotionType.QUIZ.equals( promotionTypeCode ) )
    {
      if ( !this.allowUnlimitedAttempts && this.maximumAttempts < 1 )
      {
        errors.add( "maximumAttempts", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.MAXIMUM_ATTEMPTS" ) ) );
      }
      if ( StringUtils.isEmpty( this.overviewDetailsText ) )
      {
        errors.add( "overviewDetailsText", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "quiz.form.QUIZ_DETAILS" ) ) );
      }
    }

    if ( PromotionType.DIY_QUIZ.equals( promotionTypeCode ) )
    {
      if ( this.promotionBasicsBadgeFormBeanList != null )
      {
        for ( PromotionBasicsBadgeFormBean promotionBasicsBadgeFormBean : promotionBasicsBadgeFormBeanList )
        {
          if ( promotionBasicsBadgeFormBean.isSelected() && StringUtils.isEmpty( promotionBasicsBadgeFormBean.getBadgeName() ) )
          {
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "promotion.basics.errors.BADGE_NAME_REQUIRED" ) );
            break;
          }
        }
      }
    }

    if ( ( awardsType == null || awardsType.length() == 0 ) && !PromotionType.DIY_QUIZ.equals( promotionTypeCode ) && !PromotionType.SURVEY.equals( promotionTypeCode )
        && !PromotionType.ENGAGEMENT.equals( promotionTypeCode ) && !PromotionType.SELF_SERV_INCENTIVES.equals( promotionTypeCode ) && !PromotionType.NOMINATION.equals( promotionTypeCode ) )
    {
      errors.add( "awardsType", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.TYPE" ) ) );
    }

    String onlineEntry = request.getParameter( "onlineEntry" );
    String fileLoadEntry = request.getParameter( "fileLoadEntry" );

    if ( PromotionType.PRODUCT_CLAIM.equals( promotionTypeCode ) && !this.hasParent )
    {
      if ( !"true".equals( onlineEntry ) && !"true".equals( fileLoadEntry ) )
      {

        errors.add( ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.errors.METHOD_OF_ENTRY_REQUIRED" ) ) );
      }
    }

    if ( PromotionType.GOALQUEST.equals( promotionTypeCode ) || PromotionType.CHALLENGE_POINT.equals( promotionTypeCode ) )
    {
      Date goalSelectionDateEnd = null;
      Date goalSelectionDateStart = null;
      Date finalProcessDate = null;

      if ( promotionObjective == null || promotionObjective.length() == 0 )
      {
        errors.add( "promotionObjective", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PROMOTION_OBJECTIVE" ) ) );
      }

      // Make sure its not empty
      if ( goalSelectionStartDate == null || goalSelectionStartDate.length() == 0 )
      {
        errors.add( "goalSelectionStartDate",
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.GOAL_SELECTION_START_DATE" ) ) );
      }
      else
      {
        // Now validate the date
        try
        {
          goalSelectionDateStart = sdf.parse( goalSelectionStartDate );
          if ( goalSelectionDateStart != null )
          {
            if ( dateEnd != null && dateEnd.before( goalSelectionDateStart ) )
            {
              // The date is before the start date
              errors.add( "goalSelectionStartDate", new ActionMessage( "promotion.basics.errors.GOAL_SELECTION_DATES_NOT_VALID" ) );
            }
            if ( dateStart != null && dateStart.after( goalSelectionDateStart ) )
            {
              // The date is before the start date
              errors.add( "goalSelectionStartDate", new ActionMessage( "promotion.basics.errors.GOAL_SELECTION_DATES_NOT_VALID" ) );
            }
          }
        }
        catch( ParseException e )
        {
          errors.add( "goalSelectionStartDate",
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.GOAL_SELECTION_START_DATE" ) ) );
        }
      }

      // ***** Validate goalSelectionEndDate *****/
      if ( goalSelectionEndDate == null || goalSelectionEndDate.length() == 0 )
      {
        errors.add( "goalSelectionEndDate",
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.GOAL_SELECTION_END_DATE" ) ) );
      }
      else
      {
        try
        {
          goalSelectionDateEnd = sdf.parse( goalSelectionEndDate );
          if ( goalSelectionDateEnd.before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
          {
            // The date is before current date
            errors.add( "goalSelectionEndDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_TO_DATE_GOALSELECTION ) );
          }
          if ( goalSelectionDateStart != null )
          {
            if ( goalSelectionDateEnd.before( goalSelectionDateStart ) )
            {
              // The date is before the start date
              errors.add( "goalSelectionEndDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE_RANGE_GOALSELECTION ) );
            }
            if ( dateStart != null && goalSelectionDateEnd.before( dateStart ) )
            {
              // The date is before the start date
              errors.add( "goalSelectionEndDate", new ActionMessage( "promotion.basics.errors.GOAL_SELECTION_DATES_NOT_VALID" ) );
            }
            if ( dateEnd != null && goalSelectionDateEnd.after( dateEnd ) )
            {
              // The date is before the start date
              errors.add( "goalSelectionEndDate", new ActionMessage( "promotion.basics.errors.GOAL_SELECTION_DATES_NOT_VALID" ) );
            }
          }
        }
        catch( ParseException e )
        {
          errors.add( "goalSelectionEndDate",
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.GOAL_SELECTION_END_DATE" ) ) );
        }
      }
      if ( finalProcessDateString == null || finalProcessDateString.length() == 0 )
      {
        errors.add( "finalProcessDateString",
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.FINAL_PROCESS_DATE" ) ) );
      }
      else
      {
        // ***** Validate finalProcessDate *****/
        try
        {
          finalProcessDate = sdf.parse( finalProcessDateString );
          if ( finalProcessDate != null && goalSelectionDateEnd != null )
          {
            if ( finalProcessDate.before( goalSelectionDateEnd ) || dateEnd != null && finalProcessDate.after( dateEnd ) )
            {
              // The date is before the end of goal selection or date is after promotion end date
              errors.add( "finalProcessDateString", new ActionMessage( "promotion.basics.errors.FINAL_PROCESS_DATE_NOT_VALID" ) );
            }
          }
        }
        catch( ParseException e )
        {
          errors.add( "finalProcessDateString", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.FINAL_PROCESS_DATE" ) ) );
        }
      }

      if ( !org.apache.commons.lang3.StringUtils.isEmpty( awardsType ) && !awardsType.equals( PromotionAwardsType.POINTS ) )
      {
        if ( programId == null || programId.trim().length() == 0 )
        {
          errors.add( "programId", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PROGRAM_ID" ) ) );
        }
        else if ( !StringUtils.isValidNumeric( this.programId ) )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "system.errors.NONNUMERIC", CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PROGRAM_ID" ) ) );

        }

        if ( merchGiftCodeType == null || merchGiftCodeType.isEmpty() )
        {
          errors.add( "merchGiftCodeType", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.AWARD_BY_PAX" ) ) );
        }
      }

      // GoalQuest-Specific Validation
      if ( PromotionType.GOALQUEST.equals( promotionTypeCode ) )
      {
        if ( org.apache.commons.lang3.StringUtils.isEmpty( progressLoadTypeCode ) )
        {
          errors.add( "progressLoadTypeCode",
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PROGRESS_LOAD_TYPE" ) ) );
        }
      }
      // ChallengePoint-Specific Validation
      if ( PromotionType.CHALLENGE_POINT.equals( promotionTypeCode ) )
      {
        if ( challengePointAwardType == null || challengePointAwardType.length() == 0 )
        {
          errors.add( "challengePointAwardType",
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.awards.CHALLENGEPOINT_AWARD_TYPE" ) ) );
        }
      }

      if ( StringUtils.isEmpty( this.overviewDetailsText ) )
      {
        errors.add( "overviewDetailsText", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PROMOTION_OVERVIEW" ) ) );
      }

    } // end GoalQuest/ChallengePoint

    if ( PromotionType.THROWDOWN.equals( promotionTypeCode ) )
    {
      if ( StringUtils.isEmpty( this.overviewDetailsText ) )
      {
        errors.add( "overviewDetailsText", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PROMOTION_OVERVIEW" ) ) );
      }
      if ( !StringUtils.isEmpty( this.overviewDetailsText ) && this.overviewDetailsText.length() > 75 )
      {
        errors.add( "overviewDetailsText",
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PROMOTION_OVERVIEW_SIZE_LIMIT" ) ) );
      }
      if ( StringUtils.isEmpty( this.promotionTheme ) )
      {
        errors.add( "promotionTheme", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.PROMOTION_THEME" ) ) );
      }
      if ( StringUtils.isEmpty( this.unevenPlaySelection ) )
      {
        errors.add( "unevenPlaySelection",
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.UNEVEN_PLAY_SELECTION" ) ) );
      }
      if ( StringUtils.isEmpty( this.daysPriorToRoundStartSchedule ) || !StringUtils.isValidNumeric( daysPriorToRoundStartSchedule ) || Integer.parseInt( daysPriorToRoundStartSchedule ) < 1 )
      {
        errors.add( "daysPriorToRoundStartSchedule", new ActionMessage( "promotion.basics.errors.INVALID_DAYS_PRIOR_TO_ROUND" ) );
      }
    }

    errors = validateDateFormat( errors );

    if ( allowRecognitionSendDate )
    {
      try
      {
        if ( StringUtils.isEmpty( maxDaysDelayed ) )
        {
          errors.add( "maxDaysDelayed", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.MAX_NUM_OF_DAYS" ) ) );
        }
        else
        {
          if ( Long.parseLong( maxDaysDelayed ) <= 0 || Long.parseLong( maxDaysDelayed ) > 90 )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.basics.errors.RANGE_INVALID" ) );
          }
          else if ( dateEnd != null && DateUtils.getElapsedDays( dateStart, dateEnd ) < Integer.parseInt( maxDaysDelayed ) )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.basics.errors.DELAY_RANGE_INVALID", DateUtils.toDisplayString( dateEnd, UserManager.getLocale() ) ) );
          }
        }
      }
      catch( NumberFormatException e )
      {
        errors.add( ActionMessages.GLOBAL_MESSAGE,
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.errors.RANGE_INVALID" ) ) );
      }
    }

    if ( PromotionType.GOALQUEST.equals( promotionTypeCode ) || PromotionType.CHALLENGE_POINT.equals( promotionTypeCode ) || PromotionType.THROWDOWN.equals( promotionTypeCode ) )
    {
      if ( tileDisplayStartDate == null || tileDisplayStartDate.length() == 0 )
      {
        errors.add( "tileDisplayStartDate",
                    new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.TILE_DISPLAY_START_DATE" ) ) );
      }

      if ( tileDisplayEndDate == null || tileDisplayEndDate.length() == 0 )
      {
        errors.add( "tileDisplayEndDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.TILE_DISPLAY_END_DATE" ) ) );
      }
      else
      {
        try
        {
          Date tileDisplayDateEnd = sdf.parse( tileDisplayEndDate );
          if ( tileDisplayDateEnd.before( DateUtils.toStartDate( DateUtils.getCurrentDate() ) ) )
          {
            // The date is before current date
            errors.add( "tileDisplayEndDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_TO_DATE_TILEDISPLAY ) );
          }

          if ( tileDisplayStartDate != null )
          {
            Date tileDisplayDateStart = sdf.parse( tileDisplayStartDate );

            // The tile can't show up prior to the promotion start date for throwdown
            if ( PromotionType.THROWDOWN.equals( promotionTypeCode ) )
            {
              if ( tileDisplayDateStart.before( dateStart ) )
              {
                errors.add( "tileDisplayEndDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_TD_TILESTART_BEFORE_PROMO_START ) );
              }
            }
            if ( tileDisplayDateEnd.before( tileDisplayDateStart ) )
            {
              // The date is before the start date
              errors.add( "tileDisplayEndDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE_RANGE_TILEDISPLAY ) );
            }
          }
          if ( dateEnd != null && tileDisplayDateEnd.before( dateEnd ) )
          {
            errors.add( "tileDisplayEndDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE_RANGE_TILEDISPLAY_END_TO_DATE ) );
          }
        }
        catch( ParseException e )
        {
          //
        }
      }
    }

    if ( PromotionType.RECOGNITION.equals( promotionTypeCode ) && this.mobAppEnabled && !this.live.equals( "true" ) )
    {
      // Restrict Activity Form if it have required fields for Mob App
      String claimFormString = request.getParameter( "activityForm" );
      if ( claimFormString != null && claimFormString.length() > 0 )
      {
        boolean requiredFields = false;
        AssociationRequestCollection arc = new AssociationRequestCollection();
        arc.add( new ClaimFormAssociationRequest( ClaimFormAssociationRequest.STEPS ) );

        ClaimForm claimForm = getClaimFormService().getClaimFormByIdWithAssociations( new Long( this.getActivityForm() ), arc );

        for ( ClaimFormStep claimFormStep : claimForm.getClaimFormSteps() )
        {
          for ( ClaimFormStepElement claimFormStepElement : claimFormStep.getClaimFormStepElements() )
          {
            if ( claimFormStepElement.isRequired() )
            {
              requiredFields = true;
              break;
            }
          }
        }
        if ( requiredFields )
        {
          errors.add( "activityForm", new ActionMessage( "promotion.basics.errors.MOB_APP_ACTIVITY_FORM" ) );
        }
      }

      // Restrict Plateau type award if MobAPP enabled
      if ( awardsType.equals( PromotionAwardsType.MERCHANDISE ) )
      {
        errors.add( "awardsType", new ActionMessage( "promotion.basics.errors.MOB_APP_AWARDS_TYPE" ) );
      }

      // Restrict File load entry if MobAPP enabled
      if ( PromotionIssuanceType.FILE_LOAD.equals( issuanceMethod ) )
      {
        errors.add( "issuanceMethod", new ActionMessage( "promotion.basics.errors.MOB_APP_ISSUANCE_METHOD" ) );
      }

      // Restrict PURL if MobAPP enabled
      if ( isIncludePurl() )
      {
        errors.add( "includePurl", new ActionMessage( "promotion.basics.errors.MOB_APP_PURL" ) );
      }
    }

    if ( PromotionType.RECOGNITION.equals( promotionTypeCode ) && isIncludePurl() && this.purlStandardMessageEnabled )
    {
      if ( this.purlStandardMessage == null || this.purlStandardMessage.equals( "" ) )
      {
        errors.add( "purlStandardMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.STANDARD_MESSAGE" ) ) );
      }

      if ( this.defaultContributorName == null || this.defaultContributorName.equals( "" ) )
      {
        errors.add( "defaultContributorName", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.basics.NAME" ) ) );
      }
    }

    if ( PromotionType.SELF_SERV_INCENTIVES.equals( promotionTypeCode ) )
    {
      if ( selectedContests == null || selectedContests.length == 0 )
      {
        errors.add( "contestType", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.ssi.basics.ALLOWED_CONTEST_TYPE" ) ) );
      }
      else if ( !StringUtil.isNullOrEmpty( this.promotionId ) )
      {
        List<String> removedContestTypes = new ArrayList<String>();
        if ( !Arrays.asList( selectedContests ).contains( SSIContestType.AWARD_THEM_NOW ) )
        {
          removedContestTypes.add( SSIContestType.AWARD_THEM_NOW );
        }
        if ( !Arrays.asList( selectedContests ).contains( SSIContestType.DO_THIS_GET_THAT ) )
        {
          removedContestTypes.add( SSIContestType.DO_THIS_GET_THAT );
        }
        if ( !Arrays.asList( selectedContests ).contains( SSIContestType.OBJECTIVES ) )
        {
          removedContestTypes.add( SSIContestType.OBJECTIVES );
        }
        if ( !Arrays.asList( selectedContests ).contains( SSIContestType.STACK_RANK ) )
        {
          removedContestTypes.add( SSIContestType.STACK_RANK );
        }
        if ( !Arrays.asList( selectedContests ).contains( SSIContestType.STEP_IT_UP ) )
        {
          removedContestTypes.add( SSIContestType.STEP_IT_UP );
        }
        if ( removedContestTypes.size() > 0 )
        {
          Map<String, Integer> contestCount = getSSIContestService().getOpenContestCount( Long.parseLong( promotionId ), removedContestTypes );
          boolean removedActiveContestType = false;
          List<String> removedactiveContestTypeNames = new ArrayList<String>();
          for ( String ct : removedContestTypes )
          {
            if ( contestCount.get( ct ) != null && contestCount.get( ct ).intValue() > 0 )
            {
              removedActiveContestType = true;
              removedactiveContestTypeNames.add( SSIContestType.lookup( ct ).getName() );
            }
          }
          // TODO change error message
          if ( removedActiveContestType )
          {
            errors.add( "contestType",
                        new ActionMessage( ServiceErrorMessageKeys.SSI_PROMOTION_CONTEST_REMOVED_ERR, org.apache.commons.lang3.StringUtils.join( removedactiveContestTypeNames, "," ) ) );
          }
        }
      }
      try
      {
        Integer maxContestsToDisplayInt = Integer.parseInt( maxContestsToDisplay );
        if ( StringUtils.isEmpty( maxContestsToDisplay ) )
        {
          errors.add( "maxContestsToDisplay",
                      new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.ssi.basics.MAXIMUM_CONTEST_TO_DISPLAY" ) ) );
        }
        else
        {
          if ( maxContestsToDisplayInt <= 0 || maxContestsToDisplayInt > 25 )
          {
            errors.add( "maxContestsToDisplay", new ActionMessage( "promotion.ssi.basics.INVALID_MAXIMUM_CONTEST_COUNT" ) );
          }
        }
      }
      catch( NumberFormatException e )
      {
        errors.add( "maxContestsToDisplay", new ActionMessage( "promotion.ssi.basics.INVALID_MAXIMUM_CONTEST_COUNT" ) );
      }

      try
      {
        Integer daysToArchiveInt = Integer.parseInt( daysToArchive );

        if ( StringUtils.isEmpty( daysToArchive ) )
        {
          errors.add( "daysToArchive", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.ssi.basics.DAYS_TO_ARCHIVE" ) ) );
        }
        else
        {
          if ( daysToArchiveInt <= 0 || daysToArchiveInt > 60 )
          {
            errors.add( "daysToArchive", new ActionMessage( "promotion.ssi.basics.INVALID_DAYS_TO_ARCHIVE" ) );
          }
        }
      }
      catch( NumberFormatException e )
      {
        errors.add( "daysToArchive", new ActionMessage( "promotion.ssi.basics.INVALID_DAYS_TO_ARCHIVE" ) );
      }

      String contestGuideUrl = (String)request.getSession().getAttribute( "ssiPdfFullPath" );

      if ( StringUtils.isEmpty( this.contestFilePath ) && StringUtils.isEmpty( contestGuideUrl ) )
      {
        errors.add( "contestGuideUrl", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.ssi.basics.CONTEST_GUIDE" ) ) );
      }
      else
      {
        this.setContestFilePath( getSSIPromotionService().buildSSIContestGuideUrl( contestGuideUrl ) );
      }
    }
    return errors;
  }

  private ActionErrors validateDateFormat( ActionErrors errors )
  {

    Date formatDate = null;
    if ( startDate != null && startDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( startDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( startDate ) )
      {
        errors.add( "startDate", new ActionMessage( "promotion.basics.errors.INVALID_DATE_FORMAT" ) );

      }
    }
    if ( endDate != null && endDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( endDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( endDate ) )
      {
        errors.add( "endDate", new ActionMessage( "promotion.basics.errors.INVALID_DATE_FORMAT" ) );
      }
    }

    if ( goalSelectionStartDate != null && goalSelectionStartDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( goalSelectionStartDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( goalSelectionStartDate ) )
      {
        errors.add( "goalSelectionStartDate", new ActionMessage( "promotion.basics.errors.INVALID_DATE_FORMAT" ) );
      }
    }

    if ( goalSelectionEndDate != null && goalSelectionEndDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( goalSelectionEndDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( goalSelectionEndDate ) )
      {
        errors.add( "goalSelectionEndDate", new ActionMessage( "promotion.basics.errors.INVALID_DATE_FORMAT" ) );
      }
    }

    if ( finalProcessDateString != null && finalProcessDateString.length() > 0 )
    {
      formatDate = DateUtils.toDate( finalProcessDateString );
      if ( !DateUtils.toDisplayString( formatDate ).equals( finalProcessDateString ) )
      {
        errors.add( "finalProcessDateString", new ActionMessage( "promotion.basics.errors.INVALID_DATE_FORMAT" ) );
      }
    }
    if ( publicationDate != null && publicationDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( publicationDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( publicationDate ) )
      {
        errors.add( "publicationDate", new ActionMessage( "promotion.basics.errors.INVALID_DATE_FORMAT" ) );
      }
    }

    if ( tileDisplayStartDate != null && tileDisplayStartDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( tileDisplayStartDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( tileDisplayStartDate ) )
      {
        errors.add( "tileDisplayStartDate", new ActionMessage( "promotion.basics.errors.INVALID_DATE_FORMAT" ) );
      }
    }

    if ( tileDisplayEndDate != null && tileDisplayEndDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( tileDisplayEndDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( tileDisplayEndDate ) )
      {
        errors.add( "tileDisplayEndDate", new ActionMessage( "promotion.basics.errors.INVALID_DATE_FORMAT" ) );
      }
    }

    return errors;
  }

  /**
   * @return version
   */
  public long getVersion()
  {
    return version;
  }

  /**
   * @param version
   */
  public void setVersion( long version )
  {
    this.version = version;
  }

  /**
   * Set the promotion domain object with values from the form
   * 
   * @param promotion
   * @return Promotion
   */
  public Promotion toDomain( Promotion promotion )
  {
    if ( promotion == null )
    {
      return null;
    }

    if ( this.promotionId == null )
    {
      promotion.setId( null );
    }
    else
    {
      if ( this.promotionId.equals( "" ) )
      {
        promotion.setId( null );
      }
      else
      {
        promotion.setId( new Long( this.promotionId ) );
      }
    }

    promotion.setVersion( new Long( this.version ) );
    if ( promotion.isSurveyPromotion() )
    {
      promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    }
    else
    {
      promotion.setAwardType( PromotionAwardsType.lookup( this.awardsType ) );
    }
    // Bug fix # 63336
    if ( PromotionType.lookup( promotionTypeCode ).isSelfServIncentivesPromotion() )
    {
      promotion.setTaxable( false );
    }
    else
    {
      promotion.setTaxable( this.taxable );
    }
    promotion.setPurpose( this.purpose );

    // Set RecognitionPromotion or ProductClaimPromotion data
    if ( promotion.isRecognitionPromotion() || promotion.isProductClaimPromotion() || promotion.isWellnessPromotion() )
    {
      promotion.setOnlineEntry( this.onlineEntry );
      promotion.setFileLoadEntry( this.fileLoadEntry );
    }
    // Child promotion inherits Parent's Method of Entry
    if ( promotion.hasParent() )
    {
      promotion.setOnlineEntry( this.parentOnlineEntry );
      promotion.setFileLoadEntry( this.parentFileLoadEntry );
    }
    /*
     * else if ( promotion.isNominationPromotion() ) { promotion.setOnlineEntry( this.onlineEntry );
     * promotion.setFileLoadEntry( this.fileLoadEntry ); }
     */

    promotion.setPromotionType( PromotionType.lookup( this.promotionTypeCode ) );
    if ( promotion.getPromotionStatus() == null )
    {
      promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );
    }
    promotion.setName( this.promotionName );
    promotion.setAdihCashOption(this.cashEnabled);
    promotion.setAdihCashMaxAward(this.maxAwardInUSD);
    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      GoalQuestPromotion goalQuestPromotion = (GoalQuestPromotion)promotion;
      goalQuestPromotion.setObjective( this.promotionObjective );
      promotion.setOverview( this.overview );
      goalQuestPromotion.setMerchGiftCodeType( MerchGiftCodeType.lookup( this.merchGiftCodeType ) );
    }

    if ( promotion.isThrowdownPromotion() )
    {
      ThrowdownPromotion throwdownPromotion = (ThrowdownPromotion)promotion;
      throwdownPromotion.setOverview( this.overview );
      throwdownPromotion.setThrowdownPromotionType( ThrowdownPromotionType.lookup( this.promotionTheme ) );
      throwdownPromotion.setTeamUnavailableResolverType( TeamUnavailableResolverType.lookup( this.unevenPlaySelection ) );
      if ( throwdownPromotion.getHeadToHeadStartDate() == null )
      {
        throwdownPromotion.setHeadToHeadStartDate( new Date() );
      }
      throwdownPromotion.setDisplayTeamProgress( isDisplayTeamProgress() );
      throwdownPromotion.setDaysPriorToRoundStartSchedule( Integer.parseInt( this.daysPriorToRoundStartSchedule ) );
      throwdownPromotion.setSmackTalkAvailable( isSmackTalkAvailable() );

    }

    // If the promotion is not expired or not live, then parse the date
    if ( !promotion.isExpired() || !promotion.isLive() )
    {
      if ( this.startDate != null && this.startDate.length() > 0 )
      {
        promotion.setSubmissionStartDate( DateUtils.toDate( this.startDate ) );
      }
    }

    if ( this.endDate != null && this.endDate.length() > 0 )
    {
      Date parsedDate = DateUtils.toDate( this.endDate );

      // this allows the same end date as start date to create a promotion. -- 02/12/07 lax
      if ( promotion.getSubmissionStartDate().before( parsedDate ) || promotion.getSubmissionStartDate().equals( parsedDate ) )
      {
        promotion.setSubmissionEndDate( parsedDate );
      }
    }
    else
    {
      promotion.setSubmissionEndDate( null );
    }

    if ( !promotion.isExpired() )
    {
      // TODO : Update with data from Form
      if ( promotion.isProductClaimPromotion() )
      {
        // TODO: form should not call the service layer by itself
        PropertySetItem claimProcessingMode = getSystemVariableService().getPropertyByName( SystemVariableService.CLAIM_PROCESSING_ALLOW_BATCH );

        // Processing will always default to real time
        String promoProcessingMode = PromotionProcessingModeType.REAL_TIME;

        // if system variable for batch processing is set to true, then save the form data
        if ( claimProcessingMode.getBooleanVal() )
        {
          // boolean batchProcessing = promoBasicsForm.isBatchProcessing();
          if ( this.batchProcessing )
          {
            promoProcessingMode = PromotionProcessingModeType.BATCH;
          }
        }
        ( (ProductClaimPromotion)promotion ).setPromotionProcessingMode( PromotionProcessingModeType.lookup( promoProcessingMode ) );
      }

      // Set RecognitionPromotion data
      if ( promotion.isRecognitionPromotion() )
      {
        RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;
        // recognitionPromotion.setIssuanceType( PromotionIssuanceType.lookup( issuanceMethod ) );
        if ( certificate != null && certificate.length() > 0 )
        {
          recognitionPromotion.setIncludeCertificate( true );
        }
        else
        {
          recognitionPromotion.setIncludeCertificate( false );
        }
        recognitionPromotion.setIncludePurl( includePurl );
        recognitionPromotion.setAllowPublicRecognition( allowPublicRecognition );
        if ( isAllowPublicRecognition() )
        {
          recognitionPromotion.setAllowPromotionPrivate( allowPromotionPrivate );
        }
        if ( isIncludePurl() )
        {
          recognitionPromotion.setDisplayPurlInPurlTile( displayPurlInPurlTile );
          recognitionPromotion.setPurlPromotionMediaType( PurlPromotionMediaType.lookup( this.purlPromotionMediaType ) );
          recognitionPromotion.setPurlMediaValue( PurlPromotionMediaValue.lookup( this.purlMediaValue ) );
          // PURL standard Message fields
          recognitionPromotion.setPurlStandardMessageEnabled( purlStandardMessageEnabled );
          if ( purlStandardMessageEnabled )
          {
            recognitionPromotion.setPurlStandardMessage( purlStandardMessage );
            recognitionPromotion.setDefaultContributorAvatar( imageUrl );
            recognitionPromotion.setDefaultContributorName( defaultContributorName );
          }
          else
          {
            recognitionPromotion.setPurlStandardMessage( null );
            recognitionPromotion.setDefaultContributorAvatar( null );
            recognitionPromotion.setDefaultContributorName( null );
          }
        }
        recognitionPromotion.setAllowRecognitionSendDate( allowRecognitionSendDate );
        if ( allowRecognitionSendDate )
        {
          recognitionPromotion.setMaxDaysDelayed( new Long( this.maxDaysDelayed ) );
        }
        recognitionPromotion.setCopyRecipientManager( recognitionCopy );
        recognitionPromotion.setAllowManagerAward( allowManagerAward );
        recognitionPromotion.setMgrAwardPromotionId( allowManagerAward ? mgrAwardPromotionId : new Long( 0 ) );
        recognitionPromotion.setCopyOthers( copyOthers );

        // this is for file load entry
        // as we have individual fields for these we need to set each
        if ( PromotionIssuanceType.FILE_LOAD.equals( issuanceMethod ) )
        {
          promotion.setFileLoadEntry( true );
        }
        else
        {
          promotion.setFileLoadEntry( false );
        }

        if ( PromotionIssuanceType.ONLINE.equals( issuanceMethod ) )
        {
          promotion.setOnlineEntry( true );
        }
        else
        {
          promotion.setOnlineEntry( false );
        }
        if ( promotion.isComplete() || promotion.isLive() )
        {
          if ( Boolean.valueOf( recognitionPromotion.isMobAppEnabled() ).compareTo( Boolean.valueOf( mobAppEnabled ) ) != 0 )
          {
            promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );
          }
        }

        recognitionPromotion.setMobAppEnabled( mobAppEnabled );

        recognitionPromotion.setIncludeCelebrations( includeCelebrations );
        
        //Cheers promotion must be only one for the system either from system variable or basic page.
        //String cheersPromotionFromSystem = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.COKE_CHEERS_PROMO_ID ).getStringVal();         	 
        
        //if( recognitionPromotion.isCheersPromotion()){
        	//if( null !=  cheersPromotionFromSystem && new Long(cheersPromotionFromSystem) == promotion.getId())        			   	 
 		  // {
        recognitionPromotion.setCheersPromotion( allowCheersPromotion );

 		  // }
     	// }
      }

      if ( promotion.isWellnessPromotion() )
      {
        WellnessPromotion wellnessPromotion = (WellnessPromotion)promotion;

        // this is for file load entry
        // as we have individual fields for these we need to set each
        if ( PromotionIssuanceType.FILE_LOAD.equals( issuanceMethod ) )
        {
          promotion.setFileLoadEntry( true );
        }
        else
        {
          promotion.setFileLoadEntry( false );
        }

        if ( PromotionIssuanceType.ONLINE.equals( issuanceMethod ) )
        {
          promotion.setOnlineEntry( true );
        }
        else
        {
          promotion.setOnlineEntry( false );
        }
      }

      // Set NominationPromotion data
      if ( promotion.isNominationPromotion() )
      {
        NominationPromotion nominationPromotion = (NominationPromotion)promotion;

        // This field removed as part of 5.6.3 nominations update - let it stay null
        // BugFix 19073 set the certificates as null when include certificate is None
        nominationPromotion.setCertificate( null );
        nominationPromotion.setIncludeCertificate( false );

        // BugFix 67114 set nomination isOnlineEntry to true
        nominationPromotion.setOnlineEntry( true );

        nominationPromotion.setSelfNomination( selfNomination );
        nominationPromotion.setLevelSelectionByApprover( levelSelectionByApprover );//Client customization for WIP #56492
        // Client customization for WIP 58122
        nominationPromotion.setLevelPayoutByApproverAvailable( levelPayoutByApproverAvailable );//Client customization for WIP #58122
        nominationPromotion.setCapPerPax( capPerPax );//Client customization for WIP #58122
        nominationPromotion.setAllowPublicRecognition( allowPublicRecognition );
        
      //Client customizations for WIP #59418 starts
        if ( teamCmAssetText != null && teamCmAssetText.length() > 0 )
        {
            nominationPromotion.setTeamCmAssetText(teamCmAssetText);
            try
            {
              if ( nominationPromotion.getTeamCmAsset() == null )
              {
                // Create Unique Asset
                String newAssetName = getCmAssetService().getUniqueAssetCode( nominationPromotion.CM_TEAM_ASSET_PREFIX );
                String newAssetName_promoid=newAssetName+"_"+nominationPromotion.getId();
                nominationPromotion.setTeamCmAsset( newAssetName_promoid );
              }
              nominationPromotion.setTeamCmKey( nominationPromotion.CM_TEAM_TEXT_KEY_PREFIX );
              CMDataElement cmDataElement = new CMDataElement( "Promotion team Name", nominationPromotion.getTeamCmKey(), nominationPromotion.getTeamCmAssetText(), false, DataTypeEnum.HTML );
              List elements = new ArrayList();
              elements.add( cmDataElement );

              getCmAssetService().createOrUpdateAsset( nominationPromotion.CM_TEAM_DATA_SECTION,
                      nominationPromotion.TEAM_ASSET_TYPE_NAME,
                      nominationPromotion.CM_TEAM_TEXT_KEY_DESC,
                      nominationPromotion.getTeamCmAsset(),
                                                  elements,
                                                  ContentReaderManager.getCurrentLocale(),
                                                  null);
            }
            catch( ServiceErrorException e )
            {
              List errors = new ArrayList();
              errors.add( new ServiceError( ServiceErrorMessageKeys.CM_SERVICE_SAVE_ERROR ) );
              
            }
        }
        else
        {
            nominationPromotion.setTeamCmKey(null);
            nominationPromotion.setTeamCmAsset(null);
        }
        
      //Client customizations for WIP #59418 starts 
        nominationPromotion.setWhyNomination( this.isWhyNomination() );
        if ( isAllowPublicRecognition() )
        {
          nominationPromotion.setAllowPromotionPrivate( allowPromotionPrivate );
        }
        NominationEvaluationType nominationEvalType = NominationEvaluationType.lookup( evaluationType );
        nominationPromotion.setEvaluationType( nominationEvalType );
        NominationAwardGroupType nominationAwardGroupType = NominationAwardGroupType.lookup( awardGroupMethod );
        // BUG # 12751 -- wipe out approver selection if award group is changed
        if ( null != nominationAwardGroupType && !nominationAwardGroupType.equals( nominationPromotion.getAwardGroupType() ) )
        {
          // BUG # 13365 -- wipe out approver selection if award group is changed
          if ( ( promotion.getApproverType() == null ) || ( !ApproverType.SPECIFIC_APPROVERS.equals( promotion.getApproverType().getCode() ) ) )
          {
            promotion.setApproverType( null );
          }
        }
        // END BUG # 12751
        nominationPromotion.setAwardGroupType( nominationAwardGroupType );

        if ( this.teamMaxGroupMembers > 0 )
        {
          nominationPromotion.setMaxGroupMembers( new Integer( this.teamMaxGroupMembers ) );
        }
        else
        {
          nominationPromotion.setMaxGroupMembers( new Integer( this.bothMaxGroupMembers ) );
        }

        if ( NominationEvaluationType.CUMULATIVE.equals( nominationPromotion.getEvaluationType().getCode() ) )
        {
          // when cumulative, disable ecards
          nominationPromotion.setCardActive( false );

          // when cumulative, force one cert per promotion to true
          nominationPromotion.setOneCertPerPromotion( true );

          // when cumulative, force recommended award to no
          nominationPromotion.setNominatorRecommendedAward( false );

          // when cumulative, award range specifer forced to Approver only          
           nominationPromotion.setAwardSpecifierType( NominationAwardSpecifierType.lookup(
           NominationAwardSpecifierType.APPROVER ) );           
        }

        nominationPromotion.setPublicationDateActive( this.publicationDateActive );
        if ( publicationDate != null )
        {
          nominationPromotion.setPublicationDate( DateUtils.toDate( this.publicationDate ) );
        }

        String awardGroupSizeType = null;
        if ( this.teamAwardGroupSizeType != null && !this.teamAwardGroupSizeType.isEmpty() )
        {
          awardGroupSizeType = teamAwardGroupSizeType;
        }
        else
        {
          awardGroupSizeType = bothAwardGroupSizeType;
        }

        //if ( nominationAwardGroupType != null && awardGroupSizeType != null && ( nominationAwardGroupType.isTeam() || nominationAwardGroupType.isIndividualOrTeam() ) )
        if ( nominationAwardGroupType != null && awardGroupSizeType != null && !nominationAwardGroupType.isIndividual() ) 
        {
          NominationAwardGroupSizeType nominationAwardGroupSizeType = NominationAwardGroupSizeType.lookup( awardGroupSizeType );
          nominationPromotion.setAwardGroupSizeType( nominationAwardGroupSizeType );
        }
        
        // Client customizations for WIP #39189 starts
        if ( this.getEnableFileUploader() != null )
        {
          if ( this.getEnableFileUploader().booleanValue() )
          {
            nominationPromotion.setEnableFileUpload( true );
            nominationPromotion.setFileMinNumber( minNumberOfFiles );
            nominationPromotion.setFileMaxNumber( maxNumberOfFiles );
            nominationPromotion.setAllowedFileTypes( allowedFileTypes );
          }
          else
          {
            nominationPromotion.setEnableFileUpload( false );
            nominationPromotion.setFileMinNumber( 0 );
            nominationPromotion.setFileMaxNumber( 0 );
            nominationPromotion.setAllowedFileTypes( "" );
          }
        }
        // Client customizations for WIP #39189 ends
        nominationPromotion.setViewPastWinners( this.isViewPastWinners() );
      }

      if ( promotion.isSurveyPromotion() )
      {
        SurveyPromotion surveyPromotion = (SurveyPromotion)promotion;
        surveyPromotion.setCorpAndMngr( this.corpAndMngr );
      }

      if ( promotion.isQuizPromotion() )
      {
        QuizPromotion quizPromotion = (QuizPromotion)promotion;

        // BugFix 67114 set quiz isOnlineEntry to true
        quizPromotion.setOnlineEntry( true );

        if ( this.allowUnlimitedAttempts )
        {
          quizPromotion.setAllowUnlimitedAttempts( this.allowUnlimitedAttempts );
          quizPromotion.setMaximumAttempts( 0 );
        }
        else
        {
          quizPromotion.setAllowUnlimitedAttempts( this.allowUnlimitedAttempts );
          quizPromotion.setMaximumAttempts( this.maximumAttempts );
        }

        if ( certificate != null && certificate.length() > 0 )
        {
          quizPromotion.setCertificate( certificate );
          quizPromotion.setIncludePassingQuizCertificate( true );
        }
        else
        {
          quizPromotion.setCertificate( null );
          quizPromotion.setIncludePassingQuizCertificate( false );
        }
        promotion.setOverview( this.overview );

      }

      if ( promotion.isEngagementPromotion() )
      {
        EngagementPromotion engagementPromotion = (EngagementPromotion)promotion;
        engagementPromotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
        List<Long> existingEligiblePromotions = new ArrayList<Long>();
        if ( engagementPromotion.getEngagementPromotions() != null )
        {
          for ( EngagementPromotions engagementPromotions : engagementPromotion.getEngagementPromotions() )
          {
            if ( engagementPromotions.getEligiblePromotion() != null && engagementPromotions.getEligiblePromotion().getId() != null )
            {
              existingEligiblePromotions.add( engagementPromotions.getEligiblePromotion().getId() );
            }
          }
        }

        // Add newly selected eligible promotions.
        if ( this.engagementSelectedPromos != null )
        {
          for ( Long selectedEligiblePromoId : engagementSelectedPromos )
          {
            if ( !existingEligiblePromotions.contains( selectedEligiblePromoId ) )
            {
              Promotion eligiblePromotion = getPromotionService().getPromotionById( selectedEligiblePromoId );
              EngagementPromotions engagementPromotions = new EngagementPromotions();
              engagementPromotions.setEligiblePromotion( eligiblePromotion );
              engagementPromotion.addEngagementPromotions( engagementPromotions );
              this.engagementEligiblePromotionsModified = true;
            }
          }
        }

        // Remove the one's that is no longer is engagement eligible
        if ( !existingEligiblePromotions.isEmpty() )
        {
          Iterator<EngagementPromotions> iter = engagementPromotion.getEngagementPromotions().iterator();
          while ( iter.hasNext() )
          {
            EngagementPromotions engagementEligiblePromotion = (EngagementPromotions)iter.next();
            boolean found = false;
            for ( Long selectedEligiblePromoId : engagementSelectedPromos )
            {
              if ( engagementEligiblePromotion.getEligiblePromotion().getId() != null && engagementEligiblePromotion.getEligiblePromotion().getId().equals( selectedEligiblePromoId ) )
              {
                found = true;
                break;
              }
            }
            if ( !found )
            {
              iter.remove();
              this.engagementEligiblePromotionsModified = true;
            }
          }
        }
        promotion.setOverview( this.overview );
      }

      if ( promotion.isDIYQuizPromotion() )
      {
        QuizPromotion quizPromotion = (QuizPromotion)promotion;

        // For DIY Quiz the quiz will be null as there can be multiple quizzes that
        // can be tied to this promotion
        quizPromotion.setQuiz( null );

        quizPromotion.setAllowUnlimitedAttempts( this.allowUnlimitedAttempts );
        quizPromotion.setMaximumAttempts( this.maximumAttempts );
        promotion.setApprovalType( ApprovalType.lookup( ApprovalType.AUTOMATIC_IMMEDIATE ) );

        // For DIY always points
        promotion.setOverview( this.overview );
        PromotionAwardsType awardType = PromotionAwardsType.lookup( PromotionAwardsType.POINTS );
        quizPromotion.setAwardType( awardType );

        quizPromotion.setPromotionCertificates( new HashSet<PromotionCert>() );
        for ( PromotionCertificateFormBean promotionCertificateFormBean : getPromotionCertificateFormBeanList() )
        {
          if ( quizPromotion != null && promotionCertificateFormBean != null )
          {
            if ( quizPromotion.isUnderConstruction() && promotionCertificateFormBean.isSelected() )
            {
              PromotionCert promotionCert = new PromotionCert();
              if ( promotionCertificateFormBean != null )
              {
                if ( promotionCertificateFormBean.getId().longValue() != 0 )
                {
                  promotionCert.setId( promotionCertificateFormBean.getId() );
                }
              }
              promotionCert.setCertificateId( promotionCertificateFormBean.getCertificateId() );
              quizPromotion.addCertificate( promotionCert );
            }
            else if ( !quizPromotion.isUnderConstruction()
                && ( promotionCertificateFormBean.isSelected() || promotionCertificateFormBean.getId() != null && promotionCertificateFormBean.getId() > 0 ) )
            {
              PromotionCert promotionCert = new PromotionCert();
              if ( promotionCertificateFormBean != null )
              {
                if ( promotionCertificateFormBean.getId() != null )
                {
                  if ( promotionCertificateFormBean.getId().longValue() != 0 )
                  {
                    promotionCert.setId( promotionCertificateFormBean.getId() );
                  }
                }
              }
              promotionCert.setCertificateId( promotionCertificateFormBean.getCertificateId() );
              quizPromotion.addCertificate( promotionCert );
            }
          }
        }
        if ( getPromotionCertificateFormBeanList() != null && getPromotionCertificateFormBeanList().size() > 0 )
        {
          quizPromotion.setCertificate( certificate );
          quizPromotion.setIncludePassingQuizCertificate( true );
        }
        else
        {
          quizPromotion.setCertificate( null );
          quizPromotion.setIncludePassingQuizCertificate( false );
        }
      }

      if ( promotion.isGoalQuestOrChallengePointPromotion() )
      {
        GoalQuestPromotion promoGoalQuest = (GoalQuestPromotion)promotion;
        // method of entry is always file load for goal quest promotions
        promotion.setFileLoadEntry( true );

        if ( this.goalSelectionStartDate != null && this.goalSelectionStartDate.length() > 0 )
        {
          promoGoalQuest.setGoalCollectionStartDate( DateUtils.toDate( this.goalSelectionStartDate ) );
        }
        if ( this.goalSelectionEndDate != null && this.goalSelectionEndDate.length() > 0 )
        {
          promoGoalQuest.setGoalCollectionEndDate( DateUtils.toDate( this.goalSelectionEndDate ) );
        }
        else
        {
          promoGoalQuest.setGoalCollectionEndDate( null );
        }
        if ( this.finalProcessDateString != null && this.finalProcessDateString.length() > 0 )
        {
          promoGoalQuest.setFinalProcessDate( DateUtils.toDate( this.finalProcessDateString ) );
        }
        if ( this.progressLoadTypeCode != null && this.progressLoadTypeCode.length() > 0 )
        {
          promoGoalQuest.setProgressLoadType( ProgressLoadType.lookup( this.progressLoadTypeCode ) );
        }
        promoGoalQuest.setProgramId( programId );
        // CR - Convert to PerQs - START
        promoGoalQuest.setApqConversion( this.apqConversion );
        // CR - Convert to PerQs - END
        // Under Armour
        promoGoalQuest.setAllowUnderArmour( this.isIncludeUnderArmour() );

        if ( promotion.isChallengePointPromotion() )
        {
          if ( this.challengePointAwardType != null && this.challengePointAwardType.length() > 0 )
          {
            ( (ChallengePointPromotion)promotion ).setChallengePointAwardType( ChallengePointAwardType.lookup( this.challengePointAwardType ) );
          }
        }
      }
    }
    else
    {
      // if the promotion is expired, but the end date has been extended, then it should go back to
      // "live"
      if ( promotion.getSubmissionEndDate() == null || promotion.getSubmissionStartDate().before( promotion.getSubmissionEndDate() ) )
      {
        promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
      }
    }

    if ( !"".equals( this.createdBy ) && null != this.createdBy )
    {
      promotion.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    }

    if ( this.dateCreated != 0 )
    {
      promotion.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );
    }

    if ( this.tileDisplayStartDate != null && this.tileDisplayStartDate.length() > 0 )
    {
      promotion.setTileDisplayStartDate( DateUtils.toDate( this.tileDisplayStartDate ) );
    }

    if ( this.tileDisplayEndDate != null && this.tileDisplayEndDate.length() > 0 )
    {
      promotion.setTileDisplayEndDate( DateUtils.toDate( this.tileDisplayEndDate ) );
    }

    if ( promotion.isSSIPromotion() )
    {
      SSIPromotion ssiPromo = (SSIPromotion)promotion;

      long allSelectedContests = 0L;
      if ( selectedContests != null )
      {
        for ( String selectedContest : selectedContests )
        {
          if ( !StringUtil.isNullOrEmpty( selectedContest ) )
          {
            allSelectedContests = allSelectedContests + Long.valueOf( selectedContest );
          }
        }
      }
      ssiPromo.setSelectedContests( allSelectedContests );
      ssiPromo.setMaxContestsToDisplay( Integer.parseInt( maxContestsToDisplay ) );
      ssiPromo.setDaysToArchive( Integer.parseInt( daysToArchive ) );
      // Won't be used but is required.
      ssiPromo.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    }
    return promotion;

  }

  public String getHtmlImageString( String url )
  {
    StringBuilder htmlImageString = new StringBuilder();
    htmlImageString.append( "<p><img src=\"" + url + "\" alt=\"Photo\" class=\"thumb\"/></p>" );

    return htmlImageString.toString();
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)ServiceLocator.getService( SystemVariableService.BEAN_NAME );
  }

  private ClaimFormDefinitionService getClaimFormService()
  {
    return (ClaimFormDefinitionService)ServiceLocator.getService( ClaimFormDefinitionService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)ServiceLocator.getService( PromotionService.BEAN_NAME );
  }

  private SSIPromotionService getSSIPromotionService()
  {
    return (SSIPromotionService)ServiceLocator.getService( SSIPromotionService.BEAN_NAME );
  }

  private SSIContestService getSSIContestService()
  {
    return (SSIContestService)ServiceLocator.getService( SSIContestService.BEAN_NAME );
  }

  /**
   * @return dateCreated
   */
  public long getDateCreated()
  {
    return dateCreated;
  }

  /**
   * @param dateCreated
   */
  public void setdateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  /**
   * @return createdBy
   */
  public String getCreatedBy()
  {
    return createdBy;
  }

  /**
   * @param createdBy
   */
  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  /**
   * @return true if promotion has parent promotion; return false if doesn't
   */
  public boolean isHasParent()
  {
    return hasParent;
  }

  /**
   * @param hasParent
   */
  public void setHasParent( boolean hasParent )
  {
    this.hasParent = hasParent;
  }

  /**
   * @return parentEndDate
   */
  public String getParentEndDate()
  {
    return parentEndDate;
  }

  /**
   * @param parentEndDate
   */
  public void setParentEndDate( String parentEndDate )
  {
    this.parentEndDate = parentEndDate;
  }

  /**
   * @return parentPromotionActivityFormName
   */
  public String getParentPromotionActivityFormName()
  {
    return parentPromotionActivityFormName;
  }

  /**
   * @param parentPromotionActivityFormName
   */
  public void setParentPromotionActivityFormName( String parentPromotionActivityFormName )
  {
    this.parentPromotionActivityFormName = parentPromotionActivityFormName;
  }

  /**
   * @return parentPromotionName
   */
  public String getParentPromotionName()
  {
    return parentPromotionName;
  }

  /**
   * @param parentPromotionName
   */
  public void setParentPromotionName( String parentPromotionName )
  {
    this.parentPromotionName = parentPromotionName;
  }

  /**
   * @return parentStartDate
   */
  public String getParentStartDate()
  {
    return parentStartDate;
  }

  /**
   * @param parentStartDate
   */
  public void setParentStartDate( String parentStartDate )
  {
    this.parentStartDate = parentStartDate;
  }

  /**
   * @return promotionStatus
   */
  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  /**
   * @param promotionStatus
   */
  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public String getIssuanceMethod()
  {
    return issuanceMethod;
  }

  public void setIssuanceMethod( String issuanceMethod )
  {
    this.issuanceMethod = issuanceMethod;
  }

  public boolean isRecognitionCopy()
  {
    return recognitionCopy;
  }

  public void setRecognitionCopy( boolean recognitionCopy )
  {
    this.recognitionCopy = recognitionCopy;
  }

  public void setSweepstakeWinnerType( SweepstakesWinnersType sweepstageWinnerType )
  {
    this.sweepstakeWinnerType = sweepstageWinnerType;
  }

  public SweepstakesWinnersType getSweepstakeWinnerType()
  {
    return this.sweepstakeWinnerType;
  }

  public long getQuizId()
  {
    return this.quizId;
  }

  public void setQuizId( long quizId )
  {
    this.quizId = quizId;
  }

  public String getQuizName()
  {
    return this.quizName;
  }

  public void setQuizName( String quizName )
  {
    this.quizName = quizName;
  }

  public String getAwardsType()
  {
    return awardsType;
  }

  public void setAwardsType( String awardsType )
  {
    this.awardsType = awardsType;
  }

  public boolean isAllowUnlimitedAttempts()
  {
    return allowUnlimitedAttempts;
  }

  public void setAllowUnlimitedAttempts( boolean allowUnlimitedAttempts )
  {
    this.allowUnlimitedAttempts = allowUnlimitedAttempts;
  }

  public int getMaximumAttempts()
  {
    return maximumAttempts;
  }

  public void setMaximumAttempts( int maximumAttempts )
  {
    this.maximumAttempts = maximumAttempts;
  }

  public boolean isFileLoadEntry()
  {
    return fileLoadEntry;
  }

  public void setFileLoadEntry( boolean fileLoadEntry )
  {
    this.fileLoadEntry = fileLoadEntry;
  }

  public boolean isOnlineEntry()
  {
    return onlineEntry;
  }

  public void setOnlineEntry( boolean onlineEntry )
  {
    this.onlineEntry = onlineEntry;
  }

  public String getAwardGroupMethod()
  {
    return awardGroupMethod;
  }

  public void setAwardGroupMethod( String awardGroupMethod )
  {
    this.awardGroupMethod = awardGroupMethod;
  }

  public boolean isSelfNomination()
  {
    return selfNomination;
  }

  public void setSelfNomination( boolean selfNomination )
  {
    this.selfNomination = selfNomination;
  }

  public String getPurpose()
  {
    return purpose;
  }

  public void setPurpose( String purpose )
  {
    this.purpose = purpose;
  }

  public String getEvaluationType()
  {
    return evaluationType;
  }

  public void setEvaluationType( String evaluationType )
  {
    this.evaluationType = evaluationType;
  }

  public boolean isParentFileLoadEntry()
  {
    return parentFileLoadEntry;
  }

  public void setParentFileLoadEntry( boolean parentFileLoadEntry )
  {
    this.parentFileLoadEntry = parentFileLoadEntry;
  }

  public boolean isParentOnlineEntry()
  {
    return parentOnlineEntry;
  }

  public void setParentOnlineEntry( boolean parentOnlineEntry )
  {
    this.parentOnlineEntry = parentOnlineEntry;
  }

  public String getPublicationDate()
  {
    return publicationDate;
  }

  public void setPublicationDate( String publicationDate )
  {
    this.publicationDate = publicationDate;
  }

  public boolean isPublicationDateActive()
  {
    return publicationDateActive;
  }

  public void setPublicationDateActive( boolean publicationDateActive )
  {
    this.publicationDateActive = publicationDateActive;
  }

  public String getApprovalEndDate()
  {
    return approvalEndDate;
  }

  public void setApprovalEndDate( String approvalEndDate )
  {
    this.approvalEndDate = approvalEndDate;
  }

  public String getGoalSelectionEndDate()
  {
    return goalSelectionEndDate;
  }

  public void setGoalSelectionEndDate( String goalSelectionEndDate )
  {
    this.goalSelectionEndDate = goalSelectionEndDate;
  }

  public String getGoalSelectionStartDate()
  {
    return goalSelectionStartDate;
  }

  public void setGoalSelectionStartDate( String goalSelectionStartDate )
  {
    this.goalSelectionStartDate = goalSelectionStartDate;
  }

  public String getFinalProcessDateString()
  {
    return finalProcessDateString;
  }

  public void setFinalProcessDateString( String finalProcessDateString )
  {
    this.finalProcessDateString = finalProcessDateString;
  }

  public String getProgramId()
  {
    return programId;
  }

  public void setProgramId( String programId )
  {
    this.programId = programId;
  }

  public String getProgressLoadTypeCode()
  {
    return progressLoadTypeCode;
  }

  public void setProgressLoadTypeCode( String progressLoadTypeCode )
  {
    this.progressLoadTypeCode = progressLoadTypeCode;
  }

  public String getProgressLoadTypeName()
  {
    return progressLoadTypeName;
  }

  public void setProgressLoadTypeName( String progressLoadTypeName )
  {
    this.progressLoadTypeName = progressLoadTypeName;
  }

  public String getGqpartnersEnabled()
  {
    return gqpartnersEnabled;
  }

  public void setGqpartnersEnabled( String gqpartnersEnabled )
  {
    this.gqpartnersEnabled = gqpartnersEnabled;
  }

  public String getProgramNumber()
  {
    return programNumber;
  }

  public void setProgramNumber( String programNumber )
  {
    this.programNumber = programNumber;
  }

  public String getChallengePointAwardType()
  {
    return challengePointAwardType;
  }

  public void setChallengePointAwardType( String challengePointAwardType )
  {
    this.challengePointAwardType = challengePointAwardType;
  }

  public String getAwardsTypeName()
  {
    return awardsTypeName;
  }

  public void setAwardsTypeName( String awardsTypeName )
  {
    this.awardsTypeName = awardsTypeName;
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

  public String getPurlPromotionMediaType()
  {
    return purlPromotionMediaType;
  }

  public void setPurlPromotionMediaType( String purlPromotionMediaType )
  {
    this.purlPromotionMediaType = purlPromotionMediaType;
  }

  public String getPurlMediaValue()
  {
    return purlMediaValue;
  }

  public void setPurlMediaValue( String purlMediaValue )
  {
    this.purlMediaValue = purlMediaValue;
  }

  public void setAllowPublicRecognition( boolean allowPublicRecognition )
  {
    this.allowPublicRecognition = allowPublicRecognition;
  }

  public boolean isAllowPublicRecognition()
  {
    return allowPublicRecognition;
  }

  public void setCopyOthers( boolean copyOthers )
  {
    this.copyOthers = copyOthers;
  }

  public boolean isCopyOthers()
  {
    return copyOthers;
  }

  public void setAllowRecognitionSendDate( boolean allowRecognitionSendDate )
  {
    this.allowRecognitionSendDate = allowRecognitionSendDate;
  }

  public boolean isAllowRecognitionSendDate()
  {
    return allowRecognitionSendDate;
  }

  public String getMaxDaysDelayed()
  {
    return maxDaysDelayed;
  }

  public void setMaxDaysDelayed( String maxDaysDelayed )
  {
    this.maxDaysDelayed = maxDaysDelayed;
  }

  public String getPromotionObjective()
  {
    return promotionObjective;
  }

  public void setPromotionObjective( String promotionObjective )
  {
    this.promotionObjective = promotionObjective;
  }

  public String getTileDisplayStartDate()
  {
    return tileDisplayStartDate;
  }

  public void setTileDisplayStartDate( String tileDisplayStartDate )
  {
    this.tileDisplayStartDate = tileDisplayStartDate;
  }

  public String getTileDisplayEndDate()
  {
    return tileDisplayEndDate;
  }

  public void setTileDisplayEndDate( String tileDisplayEndDate )
  {
    this.tileDisplayEndDate = tileDisplayEndDate;
  }

  public String getOverview()
  {
    return overview;
  }

  public void setOverview( String overview )
  {
    this.overview = overview;
  }

  public String getOverviewDetailsText()
  {
    return overviewDetailsText;
  }

  public void setOverviewDetailsText( String overviewDetailsText )
  {
    this.overviewDetailsText = overviewDetailsText;
  }

  public String getMerchGiftCodeType()
  {
    return merchGiftCodeType;
  }

  public void setMerchGiftCodeType( String merchGiftCodeType )
  {
    this.merchGiftCodeType = merchGiftCodeType;
  }

  public PromotionBasicsBadgeFormBean getPromotionBasicsBadgeFormBean( int index )
  {
    if ( this.promotionBasicsBadgeFormBeanList == null )
    {
      this.promotionBasicsBadgeFormBeanList = new ArrayList<PromotionBasicsBadgeFormBean>();
    }

    // indexes to not come in order, populate empty spots
    while ( index >= this.promotionBasicsBadgeFormBeanList.size() )
    {
      this.promotionBasicsBadgeFormBeanList.add( new PromotionBasicsBadgeFormBean() );
    }

    // return the requested item
    return promotionBasicsBadgeFormBeanList.get( index );
  }

  public PromotionCertificateFormBean getPromotionCertificateFormBean( int index )
  {
    if ( this.promotionCertificateFormBeanList == null )
    {
      this.promotionCertificateFormBeanList = new ArrayList<PromotionCertificateFormBean>();
    }

    // indexes to not come in order, populate empty spots
    while ( index >= this.promotionCertificateFormBeanList.size() )
    {
      this.promotionCertificateFormBeanList.add( new PromotionCertificateFormBean() );
    }

    // return the requested item
    return promotionCertificateFormBeanList.get( index );
  }

  public String getPromotionTheme()
  {
    return promotionTheme;
  }

  public void setPromotionTheme( String promotionTheme )
  {
    this.promotionTheme = promotionTheme;
  }

  public String getUnevenPlaySelection()
  {
    return unevenPlaySelection;
  }

  public void setUnevenPlaySelection( String unevenPlaySelection )
  {
    this.unevenPlaySelection = unevenPlaySelection;
  }

  public String getPromotionThemeName()
  {
    return promotionThemeName;
  }

  public void setPromotionThemeName( String promotionThemeName )
  {
    this.promotionThemeName = promotionThemeName;
  }

  public String getUnevenPlaySelectionName()
  {
    return unevenPlaySelectionName;
  }

  public void setUnevenPlaySelectionName( String unevenPlaySelectionName )
  {
    this.unevenPlaySelectionName = unevenPlaySelectionName;
  }

  public boolean isDisplayTeamProgress()
  {
    return displayTeamProgress;
  }

  public void setDisplayTeamProgress( boolean displayTeamProgress )
  {
    this.displayTeamProgress = displayTeamProgress;
  }

  public String getDaysPriorToRoundStartSchedule()
  {
    return daysPriorToRoundStartSchedule;
  }

  public void setDaysPriorToRoundStartSchedule( String daysPriorToRoundStartSchedule )
  {
    this.daysPriorToRoundStartSchedule = daysPriorToRoundStartSchedule;
  }

  public boolean isSmackTalkAvailable()
  {
    return smackTalkAvailable;
  }

  public void setSmackTalkAvailable( boolean smackTalkAvailable )
  {
    this.smackTalkAvailable = smackTalkAvailable;
  }

  public boolean isCorpAndMngr()
  {
    return corpAndMngr;
  }

  public void setCorpAndMngr( boolean corpAndMngr )
  {
    this.corpAndMngr = corpAndMngr;
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

  public FormFile getFileAsset()
  {
    return fileAsset;
  }

  public void setFileAsset( FormFile fileAsset )
  {
    this.fileAsset = fileAsset;
  }

  public List<PurlMediaUploadValue> getMediaUploads()
  {
    return mediaUploads;
  }

  public void setMediaUploads( List<PurlMediaUploadValue> mediaUploads )
  {
    this.mediaUploads = mediaUploads;
  }

  public String getMediaUrl()
  {
    return mediaUrl;
  }

  public void setMediaUrl( String mediaUrl )
  {
    this.mediaUrl = mediaUrl;
  }

  public String getMediaFilePath()
  {
    return mediaFilePath;
  }

  public void setMediaFilePath( String mediaFilePath )
  {
    this.mediaFilePath = mediaFilePath;
  }

  public String getImageUrlPath()
  {
    return this.imageUrl;
  }

  public String getImageUrl()
  {
    return imageUrl;
  }

  public void setImageUrl( String imageUrl )
  {
    this.imageUrl = imageUrl;
  }

  public String getData()
  {
    return data;
  }

  public void setData( String data )
  {
    this.data = data;
  }

  private String getSiteUrlPrefix()
  {
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    return siteUrlPrefix;
  }

  public String getContributor_uploadmedia_pho()
  {
    return contributor_uploadmedia_pho;
  }

  public void setContributor_uploadmedia_pho( String contributor_uploadmedia_pho )
  {
    this.contributor_uploadmedia_pho = contributor_uploadmedia_pho;
  }

  public String getImagePicUrlPath()
  {
    String withoutHtml = (String)StringUtil.escapeHTML( this.getImagePicUrl() );
    if ( this.getImagePicUrl() != null && !this.getImagePicUrl().isEmpty() && !withoutHtml.contains( "undefined" ) )
    {
      imagePicUrlPath = this.imagePicUrl;
    }
    return imagePicUrlPath;
  }

  public void setImagePicUrlPath( String imagePicUrlPath )
  {
    this.imagePicUrlPath = imagePicUrlPath;
  }

  public String getImagePicUrl()
  {
    return imagePicUrl;
  }

  public void setImagePicUrl( String imagePicUrl )
  {
    this.imagePicUrl = imagePicUrl;
  }

  public FormFile getFileAssetPic()
  {
    return fileAssetPic;
  }

  public void setFileAssetPic( FormFile fileAssetPic )
  {
    this.fileAssetPic = fileAssetPic;
  }

  public String getContributor_uploadmedia_pho_pic()
  {
    return contributor_uploadmedia_pho_pic;
  }

  public void setContributor_uploadmedia_pho_pic( String contributor_uploadmedia_pho_pic )
  {
    this.contributor_uploadmedia_pho_pic = contributor_uploadmedia_pho_pic;
  }

  public String getVideoUrl()
  {
    return videoUrl;
  }

  public void setVideoUrl( String videoUrl )
  {
    this.videoUrl = videoUrl;
  }

  public String getVideoUrlMp4()
  {
    return videoUrlMp4;
  }

  public void setVideoUrlMp4( String videoUrlMp4 )
  {
    this.videoUrlMp4 = videoUrlMp4;
  }

  public String getVideoUrl3gp()
  {
    return videoUrl3gp;
  }

  public void setVideoUrl3gp( String videoUrl3gp )
  {
    this.videoUrl3gp = videoUrl3gp;
  }

  public String getVideoUrlOgg()
  {
    return videoUrlOgg;
  }

  public void setVideoUrlOgg( String videoUrlOgg )
  {
    this.videoUrlOgg = videoUrlOgg;
  }

  public String getVideoUrlWebm()
  {
    return videoUrlWebm;
  }

  public void setVideoUrlWebm( String videoUrlWebm )
  {
    this.videoUrlWebm = videoUrlWebm;
  }

  public List<FormFile> getFileAssetVideo()
  {
    return fileAssetVideo;
  }

  public void setFileAssetVideo( List<FormFile> fileAssetVideo )
  {
    this.fileAssetVideo = fileAssetVideo;
  }

  public String getUploadType()
  {
    return uploadType;
  }

  public void setUploadType( String uploadType )
  {
    this.uploadType = uploadType;
  }

  public Long[] getEngagementSelectedPromos()
  {
    return engagementSelectedPromos;
  }

  public void setEngagementSelectedPromos( Long[] engagementSelectedPromos )
  {
    this.engagementSelectedPromos = engagementSelectedPromos;
  }

  public Long[] getEngagementNotSelectedPromos()
  {
    return engagementNotSelectedPromos;
  }

  public void setEngagementNotSelectedPromos( Long[] engagementNotSelectedPromos )
  {
    this.engagementNotSelectedPromos = engagementNotSelectedPromos;
  }

  public boolean isEngagementEligiblePromotionsModified()
  {
    return engagementEligiblePromotionsModified;
  }

  public void setEngagementEligiblePromotionsModified( boolean engagementEligiblePromotionsModified )
  {
    this.engagementEligiblePromotionsModified = engagementEligiblePromotionsModified;
  }

  public String getDefaultContributorPicture()
  {
    return defaultContributorPicture;
  }

  public void setDefaultContributorPicture( String defaultContributorPicture )
  {
    this.defaultContributorPicture = defaultContributorPicture;
  }

  public void loadPurlStandardMessagePictureObjects( String leftColumn,
                                                     String rightColumn,
                                                     String uploadTypeInput,
                                                     String mediaPath,
                                                     String videoUrlMp4,
                                                     String videoUrlWebm,
                                                     String videoUrl3gp,
                                                     String videoUrlOgg )
  {
    this.mediaFilePath = mediaPath;

    if ( uploadTypeInput.equalsIgnoreCase( "image" ) )
    {
      this.uploadType = uploadTypeInput;
      this.imagePicUrl = leftColumn;
    }
    else if ( uploadTypeInput.equalsIgnoreCase( "video" ) )
    {
      this.uploadType = uploadTypeInput;
      this.videoUrl = leftColumn;
      this.videoUrlMp4 = videoUrlMp4;
      this.videoUrlWebm = videoUrlWebm;
      this.videoUrl3gp = videoUrl3gp;
      this.videoUrlOgg = videoUrlOgg;
    }
  }

  public boolean isAllowPromotionPrivate()
  {
    return allowPromotionPrivate;
  }

  public void setAllowPromotionPrivate( boolean allowPromotionPrivate )
  {
    this.allowPromotionPrivate = allowPromotionPrivate;
  }

  public boolean isIncludeCelebrations()
  {
    return includeCelebrations;
  }

  public void setIncludeCelebrations( boolean includeCelebrations )
  {
    this.includeCelebrations = includeCelebrations;
  }

  public String[] getSelectedContests()
  {
    return selectedContests;
  }

  public void setSelectedContests( String[] selectedContests )
  {
    this.selectedContests = selectedContests;
  }

  public String getMaxContestsToDisplay()
  {
    return maxContestsToDisplay;
  }

  public void setMaxContestsToDisplay( String maxContestsToDisplay )
  {
    this.maxContestsToDisplay = maxContestsToDisplay;
  }

  public String getDaysToArchive()
  {
    return daysToArchive;
  }

  public void setDaysToArchive( String daysToArchive )
  {
    this.daysToArchive = daysToArchive;
  }

  public FormFile getFileAssetPdf()
  {
    return fileAssetPdf;
  }

  public void setFileAssetPdf( FormFile fileAssetPdf )
  {
    this.fileAssetPdf = fileAssetPdf;
  }

  public String getContestFilePath()
  {
    return contestFilePath;
  }

  public void setContestFilePath( String contestFilePath )
  {
    this.contestFilePath = contestFilePath;
  }

  public int getTeamMaxGroupMembers()
  {
    return teamMaxGroupMembers;
  }

  public void setTeamMaxGroupMembers( int teamMaxGroupMembers )
  {
    this.teamMaxGroupMembers = teamMaxGroupMembers;
  }

  public int getBothMaxGroupMembers()
  {
    return bothMaxGroupMembers;
  }

  public void setBothMaxGroupMembers( int bothMaxGroupMembers )
  {
    this.bothMaxGroupMembers = bothMaxGroupMembers;
  }

  public String getTeamAwardGroupSizeType()
  {
    return teamAwardGroupSizeType;
  }

  public void setTeamAwardGroupSizeType( String teamAwardGroupSizeType )
  {
    this.teamAwardGroupSizeType = teamAwardGroupSizeType;
  }

  public String getBothAwardGroupSizeType()
  {
    return bothAwardGroupSizeType;
  }

  public void setBothAwardGroupSizeType( String bothAwardGroupSizeType )
  {
    this.bothAwardGroupSizeType = bothAwardGroupSizeType;
  }

  public boolean isWhyNomination()
  {
    return whyNomination;
  }

  public void setWhyNomination( boolean whyNomination )
  {
    this.whyNomination = whyNomination;
  }

  public boolean isViewPastWinners()
  {
    return viewPastWinners;
  }

  public void setViewPastWinners( boolean viewPastWinners )
  {
    this.viewPastWinners = viewPastWinners;
  }

  public boolean isIncludeUnderArmour()
  {
    return includeUnderArmour;
  }

  public void setIncludeUnderArmour( boolean includeUnderArmour )
  {
    this.includeUnderArmour = includeUnderArmour;
  }

  // Client customization for WIP #56492 starts
  public boolean isLevelSelectionByApprover()
  {
    return levelSelectionByApprover;
  }

  public void setLevelSelectionByApprover( boolean levelSelectionByApprover )
  {
    this.levelSelectionByApprover = levelSelectionByApprover;
  }
  // Client customization for WIP #56492 ends
  //Client customization for WIP #58122 starts
  public boolean isLevelPayoutByApproverAvailable()
  {
    return levelPayoutByApproverAvailable;
  }

  public void setLevelPayoutByApproverAvailable( boolean levelPayoutByApproverAvailable )
  {
    this.levelPayoutByApproverAvailable = levelPayoutByApproverAvailable;
  }

  public Integer getCapPerPax()
  {
    return capPerPax;
  }

  public void setCapPerPax( Integer capPerPax )
  {
    this.capPerPax = capPerPax;
  }
  
//Client customization for WIP #58122 ends
  
//Client customizations for WIP #59418 starts
private CMAssetService getCmAssetService()
{
  return (CMAssetService)BeanLocator.getBean( CMAssetService.BEAN_NAME );
}

public String getTeamCmAssetText() {
    return teamCmAssetText;
}

public void setTeamCmAssetText(String teamCmAssetText) {
    this.teamCmAssetText = teamCmAssetText;
}
//Client customizations for WIP #59418 ends

//Client customization for WIP #39189 starts
public Boolean getEnableFileUploader()
{
 return enableFileUploader;
}

public void setEnableFileUploader( Boolean enableFileUploader )
{
 this.enableFileUploader = enableFileUploader;
}

public Integer getMinNumberOfFiles()
{
 return minNumberOfFiles;
}

public void setMinNumberOfFiles( Integer minNumberOfFiles )
{
 this.minNumberOfFiles = minNumberOfFiles;
}

public Integer getMaxNumberOfFiles()
{
 return maxNumberOfFiles;
}

public void setMaxNumberOfFiles( Integer maxNumberOfFiles )
{
 this.maxNumberOfFiles = maxNumberOfFiles;
}
// Client customization for WIP #39189 ends

  // Client customizations for WIP #62128 starts
  public boolean isAllowCheersPromotion()
  {
    return allowCheersPromotion;
  }

  public void setAllowCheersPromotion( boolean allowCheersPromotion )
  {
    this.allowCheersPromotion = allowCheersPromotion;
  }

 

  // Client customizations for WIP #62128 ends

}
