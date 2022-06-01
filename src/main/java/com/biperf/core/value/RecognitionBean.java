
package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.enums.PromoNominationBehaviorType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionBehaviorType;
import com.biperf.core.domain.enums.PromotionCertificate;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.PromotionBehavior;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.promotion.PromotionECard;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelValueObject;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.CertUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsConfiguration;
import com.objectpartners.cms.util.ContentReaderManager;

@JsonInclude( Include.NON_NULL )
public final class RecognitionBean
{
  private static final Log logger = LogFactory.getLog( RecognitionBean.class );

  private final AbstractRecognitionPromotion promotion;

  private Long id;
  private String name;
  private String awardType;
  private boolean awardCalcLevels;
  private String promoType;
  private boolean ecardsActive;
  private boolean behaviorActive;
  private boolean behaviorRequired;
  private boolean messageActive;
  private boolean copyManagerActive;
  private boolean copyManagerAlways;
  private boolean copyMeActive;
  private boolean copyOthersActive;
  private boolean deliverDateActive;
  private String defaultDeliverDate;
  private Long awardMin;
  private Long awardMax;
  private Long awardFixed;
  private boolean easy;
  private String rulesText;
  private String pageTitle;
  private int daysRemaining;
  private boolean mobileAppEnabled;

  // need these for claim forms; not for JSON
  private boolean claimFormUsed;
  private ClaimForm claimForm;

  private List<EcardBean> eCards = new ArrayList<EcardBean>();
  private List<BehaviorBean> behaviors = new ArrayList<BehaviorBean>();
  private Map<String, List<AwardLevelBean>> awardLevels = new ConcurrentHashMap<String, List<AwardLevelBean>>();
  private List<String> customSections = new ArrayList<String>();

  private DrawToolSettings drawToolSettings;

  // PURL only
  private boolean contributorsActive;

  // Nomination Only
  private Long maxRecipients;
  private boolean teamActive;
  
  // Celebration
  private boolean anniversaryCelebrateActive;
  private boolean anniversaryYears;
  private boolean serviceAnniversary;

  // Make recognition private
  private boolean recognitionPrivateActive;
  private boolean allowYourOwnCard;

  private static final String AWARD_TYPE_NONE = "none";
  private static final String AWARD_TYPE_POINTS_FIXED = "pointsFixed";
  private static final String AWARD_TYPE_CASH = "cash";
  private static final String AWARD_TYPE_POINTS_RANGE = "pointsRange";
  
  // Client customization for WIP #39189 starts
  private boolean uploadDocument;
  // Client customization for WIP #39189 ends
  
  //client customization start
  private boolean memesActive;
  private boolean stickersActive;
  private boolean allowOwnMemeUpload;
  private List<EcardBean> memes = new ArrayList<EcardBean>();
  //client customization end
  

  public RecognitionBean( AbstractRecognitionPromotion promotion, boolean isEasy )
  {
    this( promotion );
    this.easy = isEasy;
  }

  public RecognitionBean( AbstractRecognitionPromotion promotion, String webRulesText, boolean isEasy )
  {
    this( promotion );
    this.rulesText = webRulesText;
    this.easy = isEasy;
  }

  public RecognitionBean( AbstractRecognitionPromotion promotion )
  {
    this.promotion = promotion;
    this.setId( promotion.getId() );
    this.setName( promotion.getName() );

    this.claimFormUsed = promotion.isClaimFormUsed();
    this.claimForm = promotion.getClaimForm();

    // always add one to the days remaining, so that if today is the last day,
    // the daysRemaining is one and not zero
    if ( promotion.getSubmissionEndDate() == null )
    {
      this.daysRemaining = -1;
    }
    else
    {
      this.daysRemaining = DateUtils.getElapsedDays( new Date(), promotion.getSubmissionEndDate() ) + 1;
    }

    if ( promotion.isAwardActive() )
    {
    	 // Client customization for WIP #42701 starts
        if ( promotion.getAdihCashOption() != null && promotion.getAdihCashOption() )
        {
          awardType = AWARD_TYPE_CASH;
        }
        else
        {
        	if( promotion.isNominationPromotion() /*
                                    * && ((NominationPromotion) promotion).getAwardSpecifierType()
                                    * != null && ( ( (NominationPromotion)promotion
                                    * ).getAwardSpecifierType().isApprover() )
                                    */ )
      {
        awardType = AWARD_TYPE_NONE;
      }
      else
      {
        if ( promotion.getAwardType().isPointsAwardType() )
        {
          if ( promotion.isAwardAmountTypeFixed() )
          {
            awardType = AWARD_TYPE_POINTS_FIXED;
          }
          else if ( promotion.getCalculator() != null )
          {
            awardType = "calculated";
          }
          else
          {
            awardType = AWARD_TYPE_POINTS_RANGE;
          }
        }
        else if ( promotion.getAwardType().isMerchandiseAwardType() )
        {
          if ( promotion.getCalculator() != null )
          {
            if ( promotion.getScoreBy() == null || promotion.getScoreBy().isScoreByGiver() )
            {
              awardType = "calculated";
              awardCalcLevels = false;
            }
            else
            {
              awardType = AWARD_TYPE_NONE;
            }
          }
          else
          {
            awardType = "levels";
            awardCalcLevels = true;
          }
        }
      }
    }
    }// Client customization for WIP #42701 ends
    else
    {
      awardType = AWARD_TYPE_NONE;
    }

    setClaimFormSteps( promotion );

    // determine promotion type
    if ( promotion.isNominationPromotion() )
    {
      promoType = PublicRecognitionFormattedValueBean.PROMO_TYPE_NOMINATION;
      pageTitle = ContentReaderManager.getText( "recognition.submit", "NOM_TITLE" );
      if ( promotion.isAllowPublicRecognition() && promotion.isAllowPromotionPrivate() )
      {
        recognitionPrivateActive = true;
      }
      else
      {
        recognitionPrivateActive = false;
      }
      // Client customization for WIP #39189 starts
      NominationPromotion np = (NominationPromotion)promotion;
      uploadDocument = np.isEnableFileUpload();
      // Client customization for WIP #39189 ends
    }
    else if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion rp = (RecognitionPromotion)promotion;
      mobileAppEnabled = rp.isMobAppEnabled();

      if ( rp.isIncludePurl() )
      {
        promoType = PublicRecognitionFormattedValueBean.PROMO_TYPE_PURL;
        pageTitle = ContentReaderManager.getText( "recognition.submit", "PURL_TITLE" );
        contributorsActive = true;
        recognitionPrivateActive = false;
      }
      else
      {
        pageTitle = ContentReaderManager.getText( "recognition.submit", "TITLE" );
        promoType = "none";
        if ( rp.isAllowPublicRecognition() && rp.isAllowPromotionPrivate() )
        {
          recognitionPrivateActive = true;
        }
        else
        {
          recognitionPrivateActive = false;
        }
      }
      behaviorRequired = rp.isBehaviorRequired();
      allowYourOwnCard = rp.isAllowYourOwnCard();

      if ( rp.isServiceAnniversary() )
      {
        anniversaryCelebrateActive = rp.isIncludeCelebrations();
      }
      else
      {
        anniversaryCelebrateActive = false;
      }
      if ( anniversaryCelebrateActive )
      {
        if ( rp.isServiceAnniversary() && rp.getAnniversaryInYears() != null )
        {
          anniversaryYears = rp.getAnniversaryInYears().booleanValue();
        }
      }
      // Client customization for WIP #42701 starts
      // Override promoType if promotion of type recognition with cash enabled.
      if ( rp.getAdihCashOption() != null && rp.getAdihCashOption().booleanValue() )
      {
        promoType = PublicRecognitionFormattedValueBean.PROMO_TYPE_RECOGNITION_CASH;
      }
      // Client customization for WIP #42701 ends
    }

    ecardsActive = promotion.isCardActive();
    //client customization start
    memesActive = promotion.isAllowMeme();
    stickersActive = promotion.isAllowSticker();
    allowOwnMemeUpload = promotion.isAllowUploadOwnMeme();
    //client customization end
    behaviorActive = promotion.isBehaviorActive();

    if ( promotion.getAwardAmountFixed() != null )
    {
      awardFixed = promotion.getAwardAmountFixed();
    }
    if ( promotion.getAwardAmountMin() != null )
    {
      awardMin = promotion.getAwardAmountMin();
    }
    if ( promotion.getAwardAmountMax() != null )
    {
      awardMax = promotion.getAwardAmountMax();
    }
    
    // Client customization for WIP #42701 starts
    // Override AwardMax if promotion of type recognition with cash enabled.
    if ( promotion.getAdihCashOption() != null && promotion.getAdihCashOption() )
    {
      awardMax = promotion.getAdihCashMaxAward();
    }
    // Client customization for WIP #42701 ends
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    if ( promotion.isRecognitionPromotion() )
    {
      RecognitionPromotion recognitionPromo = (RecognitionPromotion)promotion;

      // copyManagerActive indicates whether the "copy manager" checkbox should
      // even be displayed; currently, it is always available, so hard code it to true.
      // copyManagerAlways indicates whether the "copy manager" checkbox is checked
      // by default, and cannot be unchecked by the user.
      copyManagerActive = true;
      copyManagerAlways = recognitionPromo.isCopyRecipientManager();

      copyOthersActive = recognitionPromo.isCopyOthers();
      if ( recognitionPromo.isIncludePurl() )
      {
        messageActive = false;
        copyMeActive = false;
        copyManagerActive = false;
      }
      else
      {
        messageActive = true;
        copyMeActive = true;
      }
      deliverDateActive = recognitionPromo.isAllowRecognitionSendDate();
      if ( deliverDateActive )
      {
        defaultDeliverDate = DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.getCurrentDate(), UserManager.getUser().getTimeZoneId() ) );
      }

      List<Content> allCertificates = PromotionCertificate.getList( PromotionType.RECOGNITION );
      for ( PromotionCert cert : recognitionPromo.getPromotionCertificates() )
      {
        eCards.add( new EcardBean( cert, allCertificates, siteUrlPrefix ) );
      }
    }

    if ( promotion.isNominationPromotion() )
    {
      messageActive = true;
      copyManagerActive = false;

      NominationPromotion nominationPromo = (NominationPromotion)promotion;
      teamActive = nominationPromo.isTeam();
      if ( nominationPromo.getMaxGroupMembers() > 0 )
      {
        maxRecipients = new Long( nominationPromo.getMaxGroupMembers() );
      }

      List<Content> allCertificates = PromotionCertificate.getList( PromotionType.NOMINATION );
      String certId = nominationPromo.getCertificate();
      if ( StringUtils.isNotEmpty( certId ) )
      {
        eCards.add( new EcardBean( certId, allCertificates, siteUrlPrefix, nominationPromo ) );
      }
    }

    for ( PromotionECard ecard : promotion.getPromotionECard() )
    {
      if(ecard.geteCard().getName().contains( "biwcokememe" ))
      {
        memes.add( new EcardBean( ecard, true, siteUrlPrefix ) );
      }
      else
      {
        eCards.add( new EcardBean( ecard, promotion.isDrawYourOwnCard(), siteUrlPrefix ) );
      }
      
    }

    if ( !eCards.isEmpty() )
    {
      if ( promotion.isRecognitionPromotion() )
      {
        if ( promotion.getPaxDisplayOrder() == null || promotion.getPaxDisplayOrder().equals( AbstractRecognitionPromotion.PAX_STANDARDORRANDOM_DISP_ORDER ) )
        {
          Collections.shuffle( eCards );
        }
        else if ( promotion.getPaxDisplayOrder().equals( AbstractRecognitionPromotion.PAX_ALPHABETICAL_DISPLAY_ORDER ) )
        {
          eCards.sort( Comparator.comparing( EcardBean::getName ) );
        }
        else if ( promotion.getPaxDisplayOrder().equals( AbstractRecognitionPromotion.PAX_CUSTOM_DISPLAY_ORDER ) )
        {
          eCards.sort( Comparator.comparingInt( EcardBean::getOrderNumber ) );
        }
      }
      else
      {
        Collections.shuffle( eCards );
      }
    }

    try
    {
      GamificationService gamificationService = getGamificationService();
      Long promotionId = promotion.getId();
      for ( PromotionBehavior behavior : promotion.getPromotionBehaviors() )
      {
        String badgeImgName = "";
        BadgeRule badgeRule = gamificationService.getBadgeRuleByBehaviorName( behavior.getPromotionBehaviorType().getCode(), promotionId );
        if ( badgeRule != null )
        {
          List earnedNotEarnedImageList = gamificationService.getEarnedNotEarnedImageList( badgeRule.getBadgeLibraryCMKey() );
          Iterator itr = earnedNotEarnedImageList.iterator();
          while ( itr.hasNext() )
          {
            BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
            badgeImgName = siteUrlPrefix + badgeLib.getEarnedImageSmall();
          }
        }
        behaviors.add( new BehaviorBean( behavior, badgeImgName, promotion.getPromotionType() ) ); // bug
                                                                                                   // 56948
                                                                                                   // added
                                                                                                   // PromotionType
                                                                                                   // )
                                                                                                   // );
      }
      if ( !behaviors.isEmpty() )
      {
        Collections.sort( behaviors );
      }
    }
    catch( Exception e )
    {
      logger.error( e.getMessage() + " for promotion ID " + promotion.getId(), e );
    }

    for ( PromoMerchCountry promoMerchCountry : promotion.getPromoMerchCountries() )
    {
      AwardBanqMerchResponseValueObject merchlinqLevelData = null;
      try
      {
        merchlinqLevelData = getMerchLevelService().getMerchlinqLevelDataWebService( promoMerchCountry.getProgramId(), false, false );
      }
      catch( Exception e )
      {
        logger.error( e.getMessage() + " for Program Id " + ( ( null == promoMerchCountry ) ? null : promoMerchCountry.getProgramId() ), e );
      }

      if ( merchlinqLevelData != null )
      {
        List<AwardLevelBean> awardBeans = new ArrayList<AwardLevelBean>();
        Collection<MerchLevelValueObject> levels = merchlinqLevelData.getMerchLevel();
        if ( levels != null )
        {
          for ( PromoMerchProgramLevel level : promoMerchCountry.getLevels() )
          {
            awardBeans.add( new AwardLevelBean( level, levels ) );
          }
        }
        awardBeans.sort( Comparator.comparing( AwardLevelBean::getName ) );
        awardLevels.put( promoMerchCountry.getCountry().getCountryCode(), awardBeans );
      }
    }

    drawToolSettings = new DrawToolSettings( (promotion.isAllowYourOwnCard()||promotion.isAllowUploadOwnMeme()), promotion.isDrawYourOwnCard() );//Client customization
  }

  public boolean isRegularRecognitionPromotion()
  {
    return "none".equals( promoType );
  }

  public boolean isPointsAwardActive()
  {
    return AWARD_TYPE_POINTS_FIXED.equals( awardType ) || AWARD_TYPE_POINTS_RANGE.equals( awardType );
  }

  public boolean isPointsFixedAward()
  {
    return AWARD_TYPE_POINTS_FIXED.equals( awardType );
  }

  public boolean isPointsRangeAward()
  {
    return AWARD_TYPE_POINTS_RANGE.equals( awardType );
  }

  private void setClaimFormSteps( AbstractRecognitionPromotion promotion )
  {
    if ( promotion != null && promotion.isClaimFormUsed() )
    {
      if ( addCustomSection( promotion.getClaimForm() ) )
      {
        customSections.add( "customSection-" + promotion.getId() );
      }
    }
  }

  private boolean addCustomSection( ClaimForm claimForm )
  {
    if ( claimForm != null && claimForm.hasCustomFormElements() )
    {
      return true;
    }
    return false;
  }

  public static class EcardBean extends NameableBean
  {
    private static final String TYPE_CARD = "card";
    private static final String TYPE_CERTIFICATE = "cert";
    private String smallImage;
    private String largeImage;
    private String cardType;
    private boolean canEdit;
    private int orderNumber;

    public EcardBean( PromotionECard ecard, boolean drawYourOwnCard, String siteUrlPrefix )
    {
      if ( ecard != null )
      {
        ECard card = ecard.geteCard();
        setId( card.getId() );
        setName( card.getName() );
        this.smallImage = card.getSmallImageNameLocale();
        this.largeImage = card.getLargeImageNameLocale();
        this.cardType = TYPE_CARD;
        this.canEdit = drawYourOwnCard;
        if ( StringUtils.isNotBlank( ecard.getOrderNumber() ) )
        {
          this.orderNumber = Integer.parseInt( ecard.getOrderNumber() );
        }
      }
    }

    // For Recognition
    public EcardBean( PromotionCert cert, List<Content> allCertificates, String siteUrlPrefix )
    {
      if ( cert != null )
      {
        setId( new Long( cert.getCertificateId() ) );
        for ( Content certificate : allCertificates )
        {
          String certificateId = (String)certificate.getContentDataMap().get( "ID" );
          if ( certificateId.equals( getId().toString() ) )
          {
            setName( (String)certificate.getContentDataMap().get( "NAME" ) );
            this.smallImage = siteUrlPrefix + ECard.CERTIFICATES_FOLDER + (String)certificate.getContentDataMap().get( "THUMBNAIL_IMAGE" );
            this.largeImage = siteUrlPrefix + ECard.CERTIFICATES_FOLDER + (String)certificate.getContentDataMap().get( "BACKGROUND_IMG" );
            break;
          }
        }

        this.cardType = TYPE_CERTIFICATE;
        this.canEdit = false;

        if ( StringUtils.isNotBlank( cert.getOrderNumber() ) )
        {
          this.orderNumber = Integer.parseInt( cert.getOrderNumber() );
        }
      }
    }

    // For Nomination. Determines evaluation type based on promotion setup.
    public EcardBean( String certId, List<Content> allCertificates, String siteUrlPrefix, NominationPromotion nominationPromotion )
    {
      this( certId, allCertificates, siteUrlPrefix, nominationPromotion, "" );
    }

    // For nomination
    public EcardBean( String certId, List<Content> allCertificates, String siteUrlPrefix, NominationPromotion nominationPromotion, String evaluationType )
    {
      if ( StringUtils.isNotEmpty( certId ) )
      {
        setId( new Long( certId ) );
        this.cardType = TYPE_CERTIFICATE;

        if ( StringUtils.isBlank( evaluationType ) && nominationPromotion.getAwardGroupType() != null )
        {
          if ( nominationPromotion.getAwardGroupType().isIndividualOrTeam() )
          {
            evaluationType = "team";
          }
          else if ( nominationPromotion.getAwardGroupType().isIndividual() )
          {
            evaluationType = "individual";
          }
          else if ( nominationPromotion.getAwardGroupType().isTeam() )
          {
            evaluationType = "team";
          }
        }

        for ( Content certificate : allCertificates )
        {
          String certificateId = (String)certificate.getContentDataMap().get( "ID" );
          if ( certificateId.equals( getId().toString() ) )
          {
            setName( (String)certificate.getContentDataMap().get( "NAME" ) );
            this.smallImage = CertUtils.getThumbnailCertificateUrl( certificate, siteUrlPrefix, nominationPromotion.getPromotionType() );
            this.largeImage = CertUtils.getPreviewCertificateUrl( Long.valueOf( certId ), siteUrlPrefix, evaluationType, nominationPromotion.getPromotionType() );

            // Can get by without order number here. Nomination is sorting cards a different way.
            break;
          }
        }
        this.canEdit = false;
      }
    }

    public String getSmallImage()
    {
      return smallImage;
    }

    public String getLargeImage()
    {
      return largeImage;
    }

    public String getCardType()
    {
      return cardType;
    }

    public boolean isCanEdit()
    {
      return canEdit;
    }

    public int getOrderNumber()
    {
      return orderNumber;
    }
  }

  public static class BehaviorBean implements Serializable, Comparable
  {
    private String id;
    private String name;
    private String img;

    public BehaviorBean( PromotionBehavior behavior, String badgeImgName, PromotionType promotionType ) // bug
                                                                                                        // 56948
                                                                                                        // added
                                                                                                        // PromotionType
    {
      if ( behavior != null )
      {
        PromotionBehaviorType promotionBehaviorType = behavior.getPromotionBehaviorType();
        // If user's locale is not system default get behavior from CM
        // Since behaviors are cached along with promotions, we want to refetch from cm for other
        // locales

        // modified code to fetch the system default locale from cmsconfiguration file instead of
        // the system variable "default.lanugae" to avoid issue mentioned in bug 57770.
        CmsConfiguration cmsConfiguration = (CmsConfiguration)BeanLocator.getBean( "cmsConfiguration" );
        Locale systemLocale = cmsConfiguration.getDefaultLocale();

        if ( !UserManager.getLocale().toString().equalsIgnoreCase( systemLocale.getDisplayLanguage() ) )
        {
          if ( promotionType.isRecognitionPromotion() ) // bug 56948
          {
            promotionBehaviorType = PromoRecognitionBehaviorType.lookup( behavior.getPromotionBehaviorType().getCode() );
          }
          else if ( promotionType.isNominationPromotion() )
          {
            promotionBehaviorType = PromoNominationBehaviorType.lookup( behavior.getPromotionBehaviorType().getCode() );
          }
        }
        if ( promotionBehaviorType == null )
        {
          promotionBehaviorType = behavior.getPromotionBehaviorType();
        }
        id = promotionBehaviorType.getCode();
        name = promotionBehaviorType.getName();
        if ( StringUtils.isNotEmpty( badgeImgName ) )
        {
          img = badgeImgName;
        }
      }
    }

    public String getId()
    {
      return id;
    }

    public String getName()
    {
      return name;
    }

    public String getImg()
    {
      return img;
    }

    public int compareTo( Object object ) throws ClassCastException
    {
      if ( ! ( object instanceof BehaviorBean ) )
      {
        throw new ClassCastException( "A BehaviorBean was expected." );
      }
      BehaviorBean behavior = (BehaviorBean)object;
      return this.getName().compareTo( behavior.getName() );

    }
  }

  public static class AwardLevelBean extends NameableBean
  {
    private int points;

    public AwardLevelBean( PromoMerchProgramLevel level, Collection<MerchLevelValueObject> levels )
    {
      if ( level != null )
      {
        setId( level.getId() );
        setName( level.getDisplayLevelName() );
        for ( Iterator<MerchLevelValueObject> iter = levels.iterator(); iter.hasNext(); )
        {
          MerchLevelValueObject merchLevel = iter.next();
          if ( level.getLevelName().equals( merchLevel.getName() ) )
          {
            this.points = merchLevel.getMaxValue();
            break;
          }
        }
      }
    }

    public int getPoints()
    {
      return points;
    }
  }

  public static class DrawToolSettings implements Serializable
  {
    private boolean canUpload;
    private boolean canDraw;
    private List<Integer> sizes = new ArrayList<Integer>();
    private List<ColorBean> colors = new ArrayList<ColorBean>();
    //Client customization start
    private String memeFontSizes;

    public DrawToolSettings( boolean canUpload, boolean canDraw )
    {
      this.canUpload = canUpload;
      this.canDraw = canDraw;
      this.memeFontSizes = getSystemVariableService().getPropertyByName( SystemVariableService.COKE_MEME_FONT_SIZES ).getStringVal();

      sizes.add( new Integer( 4 ) );
      sizes.add( new Integer( 8 ) );
      sizes.add( new Integer( 16 ) );
      sizes.add( new Integer( 32 ) );

      colors.add( new ColorBean( "000000", "black" ) );
      colors.add( new ColorBean( "ffffff", "white" ) );
      colors.add( new ColorBean( "676767", "dark gray" ) );
      colors.add( new ColorBean( "ADADAD", "gray" ) );
      colors.add( new ColorBean( "E9E9E9", "light gray" ) );
      colors.add( new ColorBean( "960000", "burgundy" ) );
      colors.add( new ColorBean( "F50008", "red" ) );
      colors.add( new ColorBean( "FB5A15", "orange" ) );
      colors.add( new ColorBean( "FCA605", "tangerine" ) );
      colors.add( new ColorBean( "FEE150", "gold" ) );
      colors.add( new ColorBean( "F8F338", "yellow" ) );
      colors.add( new ColorBean( "67CF31", "green" ) );
      colors.add( new ColorBean( "26821F", "grass" ) );
      colors.add( new ColorBean( "0B4E1B", "pine" ) );
      colors.add( new ColorBean( "73B592", "seafoam" ) );
      colors.add( new ColorBean( "2AA3E5", "sky" ) );
      colors.add( new ColorBean( "1747BE", "blue" ) );
      colors.add( new ColorBean( "0B2666", "navy" ) );
      colors.add( new ColorBean( "5B3999", "indigo" ) );
      colors.add( new ColorBean( "AB4BCB", "purple" ) );
      colors.add( new ColorBean( "FB77A9", "pink" ) );
      colors.add( new ColorBean( "664629", "brown" ) );
      colors.add( new ColorBean( "B28E48", "tan" ) );
      colors.add( new ColorBean( "F2D992", "sand" ) );
    }
    
    public boolean isCanUpload()
    {
      return canUpload;
    }

    public boolean isCanDraw()
    {
      return canDraw;
    }

    public String getMemeFontSizes()
    {
      return memeFontSizes;
    }

    public List<Integer> getSizes()
    {
      return sizes;
    }

    public List<ColorBean> getColors()
    {
      return colors;
    }

    public static class ColorBean
    {
      private String hex;
      private String title;

      public ColorBean( String hex, String title )
      {
        this.hex = hex;
        this.title = title;
      }

      public String getHex()
      {
        return hex;
      }

      public String getTitle()
      {
        return title;
      }
    }
  }

  @JsonIgnore
  public AbstractRecognitionPromotion getPromotion()
  {
    return promotion;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getAwardType()
  {
    return awardType;
  }

  @JsonProperty( value = "isAwardCalcLevels" )
  public boolean isAwardCalcLevels()
  {
    return awardCalcLevels;
  }

  public String getPromoType()
  {
    return promoType;
  }

  @JsonIgnore
  public boolean isNomination()
  {
    return PublicRecognitionFormattedValueBean.PROMO_TYPE_NOMINATION.equals( promoType );
  }

  public boolean isEcardsActive()
  {
    return ecardsActive;
  }

  public boolean isBehaviorActive()
  {
    return behaviorActive;
  }

  public boolean isBehaviorRequired()
  {
    return behaviorRequired;
  }

  public boolean isMessageActive()
  {
    return messageActive;
  }

  public boolean isCopyManagerActive()
  {
    return copyManagerActive;
  }

  public boolean isCopyManagerAlways()
  {
    return copyManagerAlways;
  }

  public boolean isCopyMeActive()
  {
    return copyMeActive;
  }

  public boolean isCopyOthersActive()
  {
    return copyOthersActive;
  }

  public boolean isDeliverDateActive()
  {
    return deliverDateActive;
  }

  public Long getAwardMin()
  {
    return awardMin;
  }

  public Long getAwardMax()
  {
    return awardMax;
  }

  public Long getAwardFixed()
  {
    return awardFixed;
  }

  @JsonProperty( value = "isEasy" )
  public boolean isEasy()
  {
    return easy;
  }

  public List<EcardBean> geteCards()
  {
    return eCards;
  }

  public List<BehaviorBean> getBehaviors()
  {
    return behaviors;
  }

  public Map<String, List<AwardLevelBean>> getAwardLevels()
  {
    return awardLevels;
  }

  public boolean isContributorsActive()
  {
    return contributorsActive;
  }

  public Long getMaxRecipients()
  {
    return maxRecipients;
  }

  public boolean isTeamActive()
  {
    return teamActive;
  }

  public List<String> getCustomSections()
  {
    return customSections;
  }

  public DrawToolSettings getDrawToolSettings()
  {
    return drawToolSettings;
  }

  public String getDefaultDeliverDate()
  {
    return defaultDeliverDate;
  }

  public String getRulesText()
  {
    return rulesText;
  }

  public String getPageTitle()
  {
    return pageTitle;
  }

  public int getDaysRemaining()
  {
    return daysRemaining;
  }

  @JsonIgnore
  public boolean isClaimFormUsed()
  {
    return claimFormUsed;
  }

  @JsonIgnore
  public ClaimForm getClaimForm()
  {
    return claimForm;
  }

  public boolean isMobileAppEnabled()
  {
    return mobileAppEnabled;
  }

  public boolean isAnniversaryCelebrateActive()
  {
    return anniversaryCelebrateActive;
  }

  public void setAnniversaryCelebrateActive( boolean anniversaryCelebrateActive )
  {
    this.anniversaryCelebrateActive = anniversaryCelebrateActive;
  }

  public boolean isAnniversaryYears()
  {
    return anniversaryYears;
  }

  public void setAnniversaryYears( boolean anniversaryYears )
  {
    this.anniversaryYears = anniversaryYears;
  }

  public boolean isServiceAnniversary()
  {
    return serviceAnniversary;
  }

  public void setServiceAnniversary( boolean serviceAnniversary )
  {
    this.serviceAnniversary = serviceAnniversary;
  }

  public boolean isRecognitionPrivateActive()
  {
    return recognitionPrivateActive;
  }

  public void setRecognitionPrivateActive( boolean recognitionPrivateActive )
  {
    this.recognitionPrivateActive = recognitionPrivateActive;
  }

  public boolean isPurlPromotion()
  {
    return PublicRecognitionFormattedValueBean.PROMO_TYPE_PURL.equalsIgnoreCase( promoType );
  }

  private MerchLevelService getMerchLevelService() throws Exception
  {
    return (MerchLevelService)BeanLocator.getBean( MerchLevelService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private GamificationService getGamificationService() throws Exception
  {
    return (GamificationService)BeanLocator.getBean( GamificationService.BEAN_NAME );
  }

  public boolean isAllowYourOwnCard()
  {
    return allowYourOwnCard;
  }
  
//Client customization for WIP #39189 starts
 public boolean isUploadDocument()
 {
   return uploadDocument;
 }

 public void setUploadDocument( boolean uploadDocument )
 {
   this.uploadDocument = uploadDocument;
 }
 // Client customization for WIP #39189 ends

public boolean isAllowOwnMemeUpload()
{
  return allowOwnMemeUpload;
}

public void setAllowOwnMemeUpload( boolean allowOwnMemeUpload )
{
  this.allowOwnMemeUpload = allowOwnMemeUpload;
}

public boolean isMemesActive()
{
  return memesActive;
}

public boolean isStickersActive()
{
  return stickersActive;
}

public List<EcardBean> getMemes()
{
  return memes;
}

public void setMemes( List<EcardBean> memes )
{
  this.memes = memes;
}

}
