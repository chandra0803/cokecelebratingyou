
package com.biperf.core.service.promotion.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.dao.nomination.NominationDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.domain.enums.CustomApproverType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationWizardOrder;
import com.biperf.core.domain.enums.PromoNominationBehaviorType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionCertificate;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.NominationPromotionTimePeriod;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.promotion.PromotionWizard;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.cashcurrency.CashCurrencyService;
import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.NominationPromotionService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.RecognitionBean.EcardBean;
import com.biperf.core.value.nomination.EligibleNominationPromotionValueObject;
import com.biperf.core.value.nomination.NominationStepWizard;
import com.biperf.core.value.nomination.NominationSubmitDataBehaviorValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataDrawSettingsValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataECardValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataPromotionValueBean;
import com.biperf.core.value.nomination.NominationsParticipantDataValueBean.ParticipantValueBean;
import com.biperf.core.value.nomination.NominationsPromotionListValueBean;
import com.biperf.core.value.nomination.NominationsSubmissionWizardTabValueBean;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.CmsConfiguration;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

public class NominationPromotionServiceImpl implements NominationPromotionService
{
  private static final String US_DOLLAR = "USD";
  private static final String FINAL_LEVEL = "finalLevel";
  private static final String EACH_LEVEL = "eachLevel";
  private static final String AWARD_NONE = "none";
  private static final String AWARD_FIXED = "pointsFixed";
  private static final String AWARD_RANGE = "pointsRange";
  private static final String AWARD_CALCULATED = "calculated";
  private PromotionService promotionService;
  private SystemVariableService systemVariableService;
  private GamificationService gamificationService;
  private PromotionDAO promotionDAO;
  private MainContentService mainContentService;
  private NominationClaimService nominationClaimService;
  private UserService userService;
  private ParticipantService participantService;
  private CashCurrencyService cashCurrencyService;
  private NominationDAO nominationDAO;

  @Override
  public NominationPromotion getNominationPromotion( Long promotionId )
  {
    Promotion promotion = promotionDAO.getPromotionById( promotionId );

    if ( promotion != null && promotion.isNominationPromotion() )
    {
      return (NominationPromotion)promotion;
    }
    else
    {
      return null;
    }
  }

  @Override
  public List<NominationsSubmissionWizardTabValueBean> getSubmissionWizardTabs( Long promotionId )
  {
    List<NominationsSubmissionWizardTabValueBean> tabs = new ArrayList<>();
    if ( promotionId == null )
    {
      return tabs;
    }

    SortedMap<Integer, NominationsSubmissionWizardTabValueBean> map = new TreeMap<Integer, NominationsSubmissionWizardTabValueBean>();

    NominationPromotion promotion = (NominationPromotion)promotionService.getPromotionById( promotionId );
    NominationsSubmissionWizardTabValueBean tab;
    for ( PromotionWizard wizard : promotion.getPromotionWizardOrder() )
    {
      if ( wizard.getWizardOrder() == null )
      {
        continue;
      }

      NominationStepWizard stepWizard = NominationStepWizard.getByDBOrderName( wizard.getWizardOrderName() );

      if ( stepWizard == null )
      {
        continue;
      }

      tab = new NominationsSubmissionWizardTabValueBean();
      int stepOneRemoved = Integer.valueOf( wizard.getWizardOrder() ) - 1;
      tab.setId( stepOneRemoved );
      tab.setName( stepWizard.getName() );
      tab.setActive( stepWizard.isActive() );
      tab.setState( stepWizard.getState() );
      tab.setContentSel( stepWizard.getContentSel() );
      tab.setWtvNumber( String.valueOf( stepOneRemoved ) );
      tab.setWtvName( stepWizard.getWtvName() );

      map.put( tab.getId(), tab );
    }

    /*
     * NominationsSubmissionWizardTab whyTabStep = NominationStepWizard.getWhyTabStep(); map.put(
     * whyTabStep.getId(), whyTabStep );
     */

    return new ArrayList<NominationsSubmissionWizardTabValueBean>( map.values() );
  }

  @Override
  public List<NominationSubmitDataECardValueBean> getECards( NominationPromotion promotion, String userLocale )
  {
    if ( promotion == null )
    {
      return new ArrayList<>();
    }

    if ( userLocale == null )
    {
      userLocale = "";
    }

    // Gets e-cards only...
    List<NominationSubmitDataECardValueBean> cards = promotionDAO.getECards( promotion.getId(), userLocale );

    // Transform the image URL to be locale-sensitive
    String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    Iterator<NominationSubmitDataECardValueBean> cardIterator = cards.iterator();
    while ( cardIterator.hasNext() )
    {
      NominationSubmitDataECardValueBean card = cardIterator.next();

      if ( card.isTranslatable() && StringUtil.isValid( card.getLocale() ) )
      {
        card.setSmallImage( card.getSmallImage() );
        card.setLargeImage( card.getLargeImage() );
      }
      else
      {
        card.setSmallImage( card.getSmallImage() );
        card.setLargeImage( card.getLargeImage() );
      }
    }

    // Add certificates to the list. They come from CM and will need to be mapped to the value
    // object here.
    // This code is similar to that in core.value.RecognitionBean
    // If explicitly one cert per promotion, then we'll actually skip adding certificates to the
    // list - the one is implied
    // once certificate per promotion condition removed
    NominationPromotion nominationPromotion = (NominationPromotion)promotion;
    List<Content> allCertificates = PromotionCertificate.getList( PromotionType.NOMINATION );
    Set<PromotionCert> promoCertificates = promotion.getPromotionCertificates();
    for ( PromotionCert promoCert : promoCertificates )
    {
      String certId = promoCert.getCertificateId();
      EcardBean oldCertBean = new EcardBean( certId, allCertificates, siteUrlPrefix, nominationPromotion );
      NominationSubmitDataECardValueBean newCertBean = new NominationSubmitDataECardValueBean();

      newCertBean.setId( oldCertBean.getId() );
      newCertBean.setName( oldCertBean.getName() );
      newCertBean.setSmallImage( oldCertBean.getSmallImage() );
      newCertBean.setLargeImage( oldCertBean.getLargeImage() );
      newCertBean.setCanEdit( oldCertBean.isCanEdit() );
      newCertBean.setCardType( oldCertBean.getCardType() );

      newCertBean.setTranslatable( false );
      newCertBean.setLocale( null );

      cards.add( newCertBean );
    }

    return cards;
  }

  @Override
  public NominationSubmitDataDrawSettingsValueBean getDrawToolSettings( Long promotionId )
  {
    return promotionDAO.getDrawToolSettings( promotionId );
  }

  @Override
  public List<NominationSubmitDataBehaviorValueBean> getBehaviors( Long promotionId )
  {
    CmsConfiguration cmsConfiguration = (CmsConfiguration)BeanLocator.getBean( "cmsConfiguration" );
    return getBehaviors( promotionId, UserManager.getLocale(), cmsConfiguration.getDefaultLocale() );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<NominationSubmitDataBehaviorValueBean> getBehaviors( Long promotionId, Locale userLocale, Locale defaultLocale )
  {
    if ( promotionId == null )
    {
      return new ArrayList<>();
    }

    String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    // DAO gives us just the type codes. We'll apply logic here to grab all the different
    // information we need for each type.
    List<String> behaviorTypes = promotionDAO.getBehaviorTypes( promotionId );

    // The list of behavior value beans we'll return. 1:1 correspondence with above type list.
    List<NominationSubmitDataBehaviorValueBean> behaviors = new ArrayList<>( behaviorTypes.size() );

    for ( int i = 0; i < behaviorTypes.size(); ++i )
    {
      String type = behaviorTypes.get( i );
      NominationSubmitDataBehaviorValueBean behavior = new NominationSubmitDataBehaviorValueBean();

      String badgeImageName = "";
      BadgeRule badgeRule = gamificationService.getBadgeRuleByBehaviorName( type, promotionId );
      if ( badgeRule != null )
      {
        List<BadgeLibrary> earnedNotEarnedImageList = gamificationService.getEarnedNotEarnedImageList( badgeRule.getBadgeLibraryCMKey() );
        Iterator<BadgeLibrary> imageListIterator = earnedNotEarnedImageList.iterator();
        // Not sure why this was a loop - it looks like it could just use the last element in the
        // list instead
        while ( imageListIterator.hasNext() )
        {
          BadgeLibrary badgeLibrary = imageListIterator.next();
          badgeImageName = siteUrlPrefix + badgeLibrary.getEarnedImageSmall();
        }
      }

      if ( !userLocale.toString().equalsIgnoreCase( defaultLocale.getDisplayLanguage() ) )
      {
        String cmType = PromoNominationBehaviorType.lookup( type ).getCode();
        if ( cmType != null )
        {
          type = cmType;
        }
      }

      // Set pojo attributes
      behavior.setId( type ); // this need to be modified
      behavior.setName( PromoNominationBehaviorType.lookup( type ).getName() );
      if ( StringUtils.isNotEmpty( badgeImageName ) )
      {
        behavior.setImage( badgeImageName );
      }

      behavior.setPosition( i );

      // Don't forget to actually add to list
      behaviors.add( behavior );
    }

    // Return our list of POJOs with logic applied
    return behaviors;
  }

  @Override
  public NominationSubmitDataPromotionValueBean getNominationForSubmission( Long promotionId, String userLocale )
  {
    // Value object to place information we're interested in.. in
    NominationSubmitDataPromotionValueBean nomPromoVO = new NominationSubmitDataPromotionValueBean();

    // Grab nomination promotion object, grab relevant info from it.
    NominationPromotion promotion = getNominationPromotion( promotionId );

    // Toss out null return value if the promotion came back null - rather than hitting NPE ahead
    if ( promotion == null )
    {
      return null;
    }
    nomPromoVO.setRecommendedAward( promotion.isNominatorRecommendedAward() );
    nomPromoVO.setId( promotion.getId() );
    nomPromoVO.setName( promotion.getPromotionName() );
    nomPromoVO.setRulesText( promotion.getWebRulesText() );

    nomPromoVO.setIndividualOrTeam( promotion.getAwardGroupType().getNameOfIndividualOrTeamOnlyNotBoth() );
    nomPromoVO.setNominatingType( promotion.getAwardGroupType().getCode() );

    nomPromoVO.setDefaultWhyActive( promotion.isWhyNomination() ); // I am not sure why this is
                                                                   // whyPromotion
    nomPromoVO.setCustomBeforeDefault( customFormElementBeforeDefaultWhy( promotion ) );

    nomPromoVO.setBehaviorsActive( promotion.isBehaviorActive() );
    nomPromoVO.seteCardsActive( promotion.isCardActive() );
    nomPromoVO.setCustomFieldsActive( promotion.getClaimForm() != null && promotion.getClaimForm().hasCustomFormElements() );
    nomPromoVO.setMaxParticipants( promotion.getMaxGroupMembers() );
    nomPromoVO.setPrivateNomination( promotion.isAllowPromotionPrivate() );

    populateAwardsInfo( promotion, nomPromoVO );

    // Participants list is left empty - assume there are no existing nominees
    // Other list attributes are delegated through other service calls
    nomPromoVO.setBehaviors( this.getBehaviors( promotionId ) );
    nomPromoVO.setDrawToolSettings( this.getDrawToolSettings( promotionId ) );
    nomPromoVO.seteCards( this.getECards( promotion, userLocale ) );
    nomPromoVO.setMoreThanOneBehavioursAllowed( !isBehaviorBasedApproverTypeExist( promotionId ) );
    
    // Client customizations for WIP #59418 start
    if ( promotion.isTeam() )
    {
      String teamCmAssetText = StringUtils.EMPTY;

      if ( promotion.getTeamCmAsset() != null && promotion.getTeamCmKey() != null )
      {
        teamCmAssetText = CmsResourceBundle.getCmsBundle().getString( promotion.getTeamCmAsset(), promotion.getTeamCmKey() );
      }
      nomPromoVO.setTeamNameCopyBlock( teamCmAssetText );
    }
    // Client customizations for WIP #59418 ends

    return nomPromoVO;
  }

  @Override
  public boolean customFormElementBeforeDefaultWhy( NominationPromotion promotion )
  {

    Set<PromotionWizard> wizards = promotion.getPromotionWizardOrder();
    for ( PromotionWizard promotionWizard : wizards )
    {
      if ( promotionWizard.getWizardOrder() != null )
      {
        continue;
      }

      return NominationWizardOrder.WHY_AFTER.equalsIgnoreCase( promotionWizard.getWizardOrderName() );

    }
    return false;
  }

  @Override
  public void populateAwardsInfo( NominationPromotion promotion, NominationSubmitDataPromotionValueBean nomPromoVO )
  {
    nomPromoVO.setAwardsActive( promotion.isAwardActive() );

    if ( promotion.isAwardActive() )
    {
      // If payout at each level, we don't send any of the award amount information
      if ( EACH_LEVEL.equals( promotion.getPayoutLevel() ) )
      {
        for ( NominationPromotionLevel level : promotion.getNominationLevels() )
        {
          // At the time of submission, we send level 1 award details if the promotion award is at
          // each level
          if ( level.getLevelIndex() == 1 )
          {
            assignAwardValuesFromLevel( level, nomPromoVO );
          }
        }
      }
      else if ( FINAL_LEVEL.equals( promotion.getPayoutLevel() ) )
      {
        boolean firstLevelAward = promotion.getCustomApproverOptionsByLevel( 1L ).stream().anyMatch( ( option ) -> CustomApproverType.AWARD.equals( option.getApproverType().getCode() ) );
        // Bug fix 69975
        boolean lastLevelAward = promotion.getCustomApproverOptionsByLevel( promotion.getApprovalNodeLevels().longValue() ).stream()
            .anyMatch( ( option ) -> CustomApproverType.AWARD.equals( option.getApproverType().getCode() ) );

        boolean isCustomApprover = !promotion.getCustomApproverOptions().isEmpty();

        if ( isCustomApprover && ! ( firstLevelAward || lastLevelAward )
            && promotion.getNominationLevels().stream().anyMatch( ( level ) -> level.getNominationAwardAmountMin() != null || level.getCalculator() != null ) )
        {
          nomPromoVO.setAwardType( AWARD_NONE );
        }
        else
        {
          for ( NominationPromotionLevel level : promotion.getNominationLevels() )
          {
            // At the time of submission, we send final level award details if the promotion award
            // is at final level
            if ( level.getLevelIndex() == promotion.getApprovalNodeLevels().intValue() )
            {
              assignAwardValuesFromLevel( level, nomPromoVO );
            }
          }
          if ( firstLevelAward )
          {
            // If first level is award for custom approver types, Fixed payout type and Other award
            // type are not allowed and taken care from the front end.
            // Setting fixed payout type to null just in case.
            nomPromoVO.setAwardFixed( null );
          }
        }
      }
    }
  }

  private String getAwardType( NominationPromotionLevel level )
  {
    String awardType = null;

    if ( level.getAwardPayoutType().getCode().equals( PromotionAwardsType.POINTS ) || level.getAwardPayoutType().getCode().equals( PromotionAwardsType.CASH ) )
    {
      if ( level.getCalculator() != null )
      {
        awardType = AWARD_CALCULATED;
      }
      else
      {
        awardType = level.isNominationAwardAmountTypeFixed() ? AWARD_FIXED : AWARD_RANGE;
      }
    }
    else
    {
      awardType = AWARD_NONE;
    }
    return awardType;
  }

  private void assignAwardValuesFromLevel( NominationPromotionLevel level, NominationSubmitDataPromotionValueBean nomPromoVO )
  {
    String awardType = getAwardType( level );
    nomPromoVO.setAwardType( awardType );
    Participant pax = participantService.getParticipantById( UserManager.getUserId() );
    String paxCurrency = pax.getPrimaryAddress().getAddress().getCountry().getCurrencyCode();

    if ( PromotionAwardsType.CASH.equals( level.getAwardPayoutType().getCode() ) )
    {
      if ( !US_DOLLAR.equalsIgnoreCase( paxCurrency ) )
      {
        // Non US Currency. Convert the currency and show it to the logged in user
        BigDecimal convertedCurrencyAmount = cashCurrencyService.convertCurrency( US_DOLLAR, paxCurrency, level.getNominationAwardAmountMin(), null );
        nomPromoVO.setAwardMin( convertedCurrencyAmount != null ? convertedCurrencyAmount.setScale( 2, BigDecimal.ROUND_FLOOR ).toPlainString() : null );

        convertedCurrencyAmount = cashCurrencyService.convertCurrency( US_DOLLAR, paxCurrency, level.getNominationAwardAmountMax(), null );
        nomPromoVO.setAwardMax( convertedCurrencyAmount != null ? convertedCurrencyAmount.setScale( 2, BigDecimal.ROUND_FLOOR ).toPlainString() : null );

        convertedCurrencyAmount = cashCurrencyService.convertCurrency( US_DOLLAR, paxCurrency, level.getNominationAwardAmountFixed(), null );
        nomPromoVO.setAwardFixed( convertedCurrencyAmount != null ? convertedCurrencyAmount.toPlainString() : null );
      }
      else
      {
        // US Currency. No need to convert the values
        nomPromoVO.setAwardMin( level.getNominationAwardAmountMin() + "" );
        nomPromoVO.setAwardMax( level.getNominationAwardAmountMax() + "" );
        nomPromoVO.setAwardFixed( level.getNominationAwardAmountFixed() + "" );
      }
      nomPromoVO.setCurrencyLabel( paxCurrency );
    }
    if ( PromotionAwardsType.POINTS.equals( level.getAwardPayoutType().getCode() ) )
    {
      nomPromoVO.setAwardMin( level.getNominationAwardAmountMin() + "" );
      nomPromoVO.setAwardMax( level.getNominationAwardAmountMax() + "" );
      nomPromoVO.setAwardFixed( level.getNominationAwardAmountFixed() + "" );
    }
  }

  @Override
  public NominationsPromotionListValueBean getNominationForSubmissionList( Long promotionId, AuthenticatedUser user, String userLocale )
  {
    NominationsPromotionListValueBean result = new NominationsPromotionListValueBean();

    // Total number of promotions based on size of list... feel like this could be optimized
    int totalPromotionCount = getEligibleNomPromotions( user ).size();
    result.setTotalEligiblePromotionCount( totalPromotionCount );

    // Promotion comes from above service method
    NominationSubmitDataPromotionValueBean nomPromoBean = getNominationForSubmission( promotionId, userLocale );
    nomPromoBean.setTotalPromotionCount( totalPromotionCount );
    result.setPromotion( nomPromoBean );

    /*
     * Set<UserNode> userNodes = userService.getUserNodes( user.getUserId() ); for ( UserNode
     * userNode : userNodes ) { NominationsPromotionListValueBean.NodeValueBean node = new
     * NominationsPromotionListValueBean.NodeValueBean( userNode.getNode().getId(),
     * userNode.getNode().getName() ); result.addNode( node ); }
     */

    // If only one node available, pre-select it
    if ( result.getNodes() != null && result.getNodes().size() == 1 )
    {
      result.getNodes().get( 0 ).setSelected( true );
      if ( result.getPromotion().getNominatingType().equals( "individual" ) )
      {
        result.getPromotion().setIndividualOrTeam( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL ).getName() );
        result.getPromotion().setCurrentStep( "stepNominee" );
      }
      else if ( result.getPromotion().getNominatingType().equals( "team" ) )
      {
        result.getPromotion().setIndividualOrTeam( NominationAwardGroupType.lookup( NominationAwardGroupType.TEAM ).getName() );
        result.getPromotion().setCurrentStep( "stepNominee" );
      }
      else if ( result.getPromotion().getNominatingType().equals( "both" ) )
      {
        result.getPromotion().setIndividualOrTeam( NominationAwardGroupType.lookup( NominationAwardGroupType.INDIVIDUAL_OR_TEAM ).getName() );
        result.getPromotion().setCurrentStep( "stepNominating" );
      }
    }
    return result;
  }

  private List<NominationPromotion> getEligibleNomPromotionsFromCache( AuthenticatedUser user )
  {
    List<NominationPromotion> eligibleNomPromotions = new ArrayList<>();
    List<PromotionMenuBean> eligiblePromotions = mainContentService.buildEligiblePromoList( user );

    for ( PromotionMenuBean promoMenuBean : eligiblePromotions )
    {
      Promotion promotion = (Promotion)promoMenuBean.getPromotion();
      if ( promotion.isNominationPromotion() && promoMenuBean.isCanSubmit() )
      {
        eligibleNomPromotions.add( (NominationPromotion)promotion );
      }
    }
    return eligibleNomPromotions;
  }

  @Override
  public List<EligibleNominationPromotionValueObject> getEligibleNomPromotions( AuthenticatedUser user )
  {
    List<NominationPromotion> promos = getEligibleNomPromotionsFromCache( user );
    List<EligibleNominationPromotionValueObject> eligiblePromoList = new ArrayList<EligibleNominationPromotionValueObject>();

    for ( NominationPromotion promo : promos )
    {
      EligibleNominationPromotionValueObject eligiblePromo = new EligibleNominationPromotionValueObject();
      eligiblePromo.setPromoId( promo.getId() );
      eligiblePromo.setName( promo.getPromotionName() );
      if ( promo.isTimePeriodActive() )
      {
        Set<NominationPromotionTimePeriod> promoTimePeriods = promo.getNominationTimePeriods();
        for ( NominationPromotionTimePeriod timeperiod : promoTimePeriods )
        {
          Date today = new Date();
          if ( DateUtils.isDateBetween( today, timeperiod.getTimePeriodStartDate(), timeperiod.getTimePeriodEndDate(), user.getTimeZoneId() ) )
          {
            Integer maxAllowed = timeperiod.getMaxSubmissionAllowed();
            eligiblePromo.setMaxSubmissionAllowed( maxAllowed );
            int usedCount = getUsedClaimSubmissionCount( timeperiod, UserManager.getUserId() );
            eligiblePromo.setUsedSubmission( (long)usedCount );
            if ( maxAllowed != null && usedCount == maxAllowed )
            {
              int numberOfDaysLeft = DateUtils.getNumberOfDaysLeft( today, timeperiod.getTimePeriodEndDate() );
              String message = MessageFormat.format( ContentReaderManager.getText( "promotion.nomination.submit", "MAX_SUBMISSION_MESSAGE_TRY_AFTER" ), new Object[] { numberOfDaysLeft } );
              eligiblePromo.setMessage( message );
            }
            break;
          }
        }
      }
      eligiblePromoList.add( eligiblePromo );
    }
    return eligiblePromoList;
  }

  @Override
  public boolean canSubmitClaimForTimePeriod( NominationPromotionTimePeriod timePeriod, Date systemDate, String submitterTimeZoneId, Long submitterId )
  {
    if ( timePeriod == null )
    {
      return true;
    }

    if ( DateUtils.isDateBetween( systemDate, timePeriod.getTimePeriodStartDate(), timePeriod.getTimePeriodEndDate(), submitterTimeZoneId ) )
    {
      Integer maxAllowed = timePeriod.getMaxSubmissionAllowed();
      if ( maxAllowed != null )
      {
        int usedCount = getUsedClaimSubmissionCount( timePeriod, submitterId );
        if ( usedCount == maxAllowed )
        {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public int getUsedClaimSubmissionCount( NominationPromotionTimePeriod timeperiod, Long submitterId )
  {
    return nominationClaimService.getNominationClaimsSubmittedCountByPeriod( timeperiod.getId(), submitterId );
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

  public void setPromotionDAO( PromotionDAO promotionDAO )
  {
    this.promotionDAO = promotionDAO;
  }

  public void setMainContentService( MainContentService mainContentService )
  {
    this.mainContentService = mainContentService;
  }

  @Override
  public Set<NominationPromotionTimePeriod> getPromotionsTimePeriods( Long promoId )
  {
    Promotion promotion = promotionService.getPromotionById( promoId );

    if ( promotion instanceof NominationPromotion )
    {
      Set<NominationPromotionTimePeriod> nominationTimePeriods = ( (NominationPromotion)promotion ).getNominationTimePeriods();
      return nominationTimePeriods;
    }
    return new HashSet<NominationPromotionTimePeriod>();

  }

  public void setNominationClaimService( NominationClaimService nominationClaimService )
  {
    this.nominationClaimService = nominationClaimService;
  }

  @Override
  public boolean canSubmitClaimToday( Long promotionId, String timeZoneID, Long submitterId )
  {
    Set<NominationPromotionTimePeriod> nomTimePeriods = getPromotionsTimePeriods( promotionId );
    Date systemDate = DateUtils.getCurrentDateTrimmed();

    for ( NominationPromotionTimePeriod timePeriod : nomTimePeriods )
    {
      if ( !canSubmitClaimForTimePeriod( timePeriod, systemDate, timeZoneID, submitterId ) )
      {
        return false;
      }
    }
    return true;
  }

  @Override
  public NominationPromotionTimePeriod getCurrentTimePeriod( Long promotionId, String timeZoneID, Long submitterId )
  {
    Set<NominationPromotionTimePeriod> nomTimePeriods = getPromotionsTimePeriods( promotionId );
    Date systemDate = DateUtils.getCurrentDateTrimmed();

    for ( NominationPromotionTimePeriod timePeriod : nomTimePeriods )
    {
      if ( canSubmitClaimForTimePeriod( timePeriod, systemDate, timeZoneID, submitterId ) )
      {
        return timePeriod;
      }
    }
    return null;
  }

  @Override
  public List<ServiceError> validateNomineeCountByTimePeriod( RecognitionClaimSubmission submission )
  {
    List<ServiceError> validationResult = new ArrayList<ServiceError>();
    Integer maxAllowed = null;
    int submittedCount;

    Set<NominationPromotionTimePeriod> nomTimePeriods = getPromotionsTimePeriods( submission.getPromotionId() );
    Date systemDate = DateUtils.getCurrentDateTrimmed();

    for ( NominationPromotionTimePeriod timePeriod : nomTimePeriods )
    {
      if ( DateUtils.isDateBetween( systemDate, timePeriod.getTimePeriodStartDate(), timePeriod.getTimePeriodEndDate(), UserManager.getTimeZoneID() ) )
      {

        List<ParticipantValueBean> recipients = submission.getParticipants();

        for ( ParticipantValueBean recipint : recipients )
        {
          maxAllowed = timePeriod.getMaxNominationsAllowed();
          if ( maxAllowed != null )
          {
            submittedCount = nominationClaimService.getNominationClaimsSubmittedCount( timePeriod.getId(), UserManager.getUserId(), recipint.getId() );
            if ( submittedCount == maxAllowed )
            {

              StringBuilder b = new StringBuilder();
              b.append( recipint.getLastName() ).append( " " ).append( recipint.getFirstName() ).append( " reached max nominee count " );

              ServiceError message = new ServiceError( b.toString() );
              validationResult.add( message );
            }
          }
        }
      }
    }
    return validationResult;
  }

  @Override
  public Map<String, Object> nominationsWinnersModule( Long userId )
  {
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put( "approverUserId", userId );
    return nominationDAO.nominationsWinnersModule( parameters );
  }

  @Override
  public NominationSubmitDataPromotionValueBean getCertificateImages( Long promotionId, NominationAwardGroupType nominationType )
  {
    NominationSubmitDataPromotionValueBean submitData = new NominationSubmitDataPromotionValueBean();
    NominationPromotion promotion = (NominationPromotion)promotionService.getPromotionById( promotionId );
    String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    String evaluationType = null;
    if ( promotion.isCumulative() )
    {
      evaluationType = "cumulative";
    }
    else if ( NominationAwardGroupType.INDIVIDUAL.equals( nominationType.getCode() ) )
    {
      evaluationType = "individual";
    }
    else if ( NominationAwardGroupType.TEAM.equals( nominationType.getCode() ) )
    {
      evaluationType = "team";
    }

    List<Content> allCertificates = PromotionCertificate.getList( PromotionType.NOMINATION );
    Set<PromotionCert> promoCertificates = promotion.getPromotionCertificates();
    for ( PromotionCert promoCert : promoCertificates )
    {
      String certId = promoCert.getCertificateId();
      NominationSubmitDataECardValueBean certBean = new NominationSubmitDataECardValueBean();

      EcardBean ecardBean = new EcardBean( certId, allCertificates, siteUrlPrefix, promotion, evaluationType );
      certBean.setId( ecardBean.getId() );
      certBean.setLargeImage( ecardBean.getLargeImage() );

      submitData.geteCards().add( certBean );
    }

    return submitData;
  }

  public boolean isBehaviorBasedApproverTypeExist( Long promotionId )
  {
    return promotionDAO.isBehaviorBasedApproverTypeExist( promotionId );
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setCashCurrencyService( CashCurrencyService cashCurrencyService )
  {
    this.cashCurrencyService = cashCurrencyService;
  }

  public void setNominationDAO( NominationDAO nominationDAO )
  {
    this.nominationDAO = nominationDAO;
  }
}
