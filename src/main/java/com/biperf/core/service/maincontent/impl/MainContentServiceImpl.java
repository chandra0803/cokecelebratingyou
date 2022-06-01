/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/maincontent/impl/MainContentServiceImpl.java,v $
 */

package com.biperf.core.service.maincontent.impl;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;

import com.biperf.awardslinqDataRetriever.client.MerchLevel;
import com.biperf.awardslinqDataRetriever.client.MerchLevelProduct;
import com.biperf.awardslinqDataRetriever.client.MerchlinqLevelData;
import com.biperf.awardslinqDataRetriever.client.ProductGroupDescription;
import com.biperf.core.dao.claim.hibernate.ApproverSeekingClaimGroupQueryConstraint;
import com.biperf.core.dao.claim.hibernate.ApproverSeekingClaimQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.dao.reports.ReportsDAO;
import com.biperf.core.domain.Address;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ManagerWebRulesAudienceType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.enums.WebRulesAudienceType;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.AccountSummary;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AwardBanqResponseView;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.HomePageItem;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.PromoMerchCountry;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionBehavior;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserPhone;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.BaseAssociationRequest;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.claim.ClaimApproverSnapshotService;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.maincontent.DesignThemeService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.maincontent.Menu;
import com.biperf.core.service.maincontent.MenuOption;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromoMerchCountryService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.proxy.ProxyService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.shopping.ShoppingService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.PromotionMenuUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BudgetMeter;
import com.biperf.core.value.BudgetMeterData;
import com.biperf.core.value.BudgetMeterDetailBean;
import com.biperf.core.value.BudgetMeterDetailPromoBean;
import com.biperf.core.value.DailyTipValueBean;
import com.biperf.core.value.MerchAwardReminderBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.WhatsNewBean;
import com.biperf.core.value.RecognitionBean.BehaviorBean;
import com.biperf.core.value.client.PromotionCacheBean;
import com.biperf.util.StringUtils;
import com.biperf.util.java.TimedMap;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * MainContentServiceImpl.
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
 * <td>sharma</td>
 * <td>Apr 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
@SuppressWarnings( { "unchecked", "rawtypes" } )
public class MainContentServiceImpl implements MainContentService
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static Log log = LogFactory.getLog( MainContentServiceImpl.class );

  AudienceService audienceService;
  AuthorizationService aznService;
  ClaimService claimService;
  ClaimGroupService claimGroupService;
  ClaimApproverSnapshotService approverService;
  ParticipantService participantService;
  UserService userService;
  PromotionService promotionService;
  ShoppingService shoppingService;
  SystemVariableService sysVarService;
  ProxyService proxyService;
  JournalService journalService;
  PaxGoalService paxGoalService;
  ReportsDAO reportsDAO;
  PromoMerchCountryService promoMerchCountryService;
  private BudgetMasterService budgetService;
  private PlatformTransactionManager transactionManager;
  private MerchLevelService merchLevelService;
  private MerchOrderService merchOrderService;
  private DesignThemeService designThemeService;
  private CountryService countryService;
  private CMAssetService cmAssetService;
  private AwardBanQServiceFactory awardBanQServiceFactory;

  private Map menuCache = new ConcurrentHashMap();
  private Map featuredAwardsCache = new ConcurrentHashMap();
  private Map awardRemindersCache = new ConcurrentHashMap();
  private Map purlRemindersCache = new ConcurrentHashMap();
  private Map budgetTrackerCache = new ConcurrentHashMap();
  private Map eligiblePromotions = new ConcurrentHashMap();
  
  /* customization start */
  private GamificationService gamificationService ;
  private Map publicRecognitionDepartmentCache = new ConcurrentHashMap();
  /* customization end */

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------
  public AwardBanQService getAwardBanQService()
  {
    return awardBanQServiceFactory.getAwardBanQService();
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public void setReportsDAO( ReportsDAO reportsDAO )
  {
    this.reportsDAO = reportsDAO;
  }

  public void setAuthorizationService( AuthorizationService aznService )
  {
    this.aznService = aznService;
  }

  // public void setCacheFactory( CacheFactory cacheFactory )
  // {
  // menuCache = cacheFactory.getCache( "menus" );
  // featuredAwardsCache = cacheFactory.getCache( "featuredAwards" );
  // awardRemindersCache = cacheFactory.getCache( "awardRemindersCache" );
  // purlRemindersCache = cacheFactory.getCache( "purlRemindersCache" );
  // budgetTrackerCache = cacheFactory.getCache( "budgetTrackerCache" );
  // eligiblePromotions = cacheFactory.getCache( "eligiblePromotions" );
  // }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public void setClaimGroupService( ClaimGroupService claimGroupService )
  {
    this.claimGroupService = claimGroupService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setSystemVariableService( SystemVariableService sysVarService )
  {
    this.sysVarService = sysVarService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setDesignThemeService( DesignThemeService designThemeService )
  {
    this.designThemeService = designThemeService;
  }

  public void setCountryService( CountryService countryService )
  {
    this.countryService = countryService;
  }

  public void setShoppingService( ShoppingService shoppingService )
  {
    this.shoppingService = shoppingService;
  }

  public void setProxyService( ProxyService proxyService )
  {
    this.proxyService = proxyService;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public void setApproverService( ClaimApproverSnapshotService approverService )
  {
    this.approverService = approverService;
  }

  public void setPromoMerchCountryService( PromoMerchCountryService promoMerchCountryService )
  {
    this.promoMerchCountryService = promoMerchCountryService;
  }

  public void setBudgetService( BudgetMasterService budgetService )
  {
    this.budgetService = budgetService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  // ---------------------------------------------------------------------------
  // Menu Methods
  // ---------------------------------------------------------------------------
  /**
   * Overridden from
   *
   * @see com.biperf.core.service.maincontent.MainContentService#getUserMenus(AuthenticatedUser, List,boolean)
   * @param authenticatedUser
   * @param eligiblePromoList
   * @return List of Menu objects
   */
  public List getUserMenus( AuthenticatedUser authenticatedUser, List eligiblePromoList )
  {
    List result = (List)menuCache.get( authenticatedUser.getUserId() );

    if ( result == null )
    {
      log.debug( "creating user menus" );
      result = new ArrayList();
      if ( authenticatedUser.isParticipant() )
      {
        result = buildAdminParticipantMenuList();
      }
      else if ( authenticatedUser.isUser() )
      {
        result = buildUserMenuList();
      }

      // ----------------
      // Cache the menus
      // ----------------
      menuCache.put( authenticatedUser.getUserId(), result );
    }
    else
    {
      log.debug( "using cached user menus" );
    }
    return result;
  }

  /**
   * Build the G3 Redux default menu for Client Admin
   * @return List of Menu(s)
   */
  private List buildAdminParticipantMenuList()
  {
    List menuList = new ArrayList();
    UserInfo userInfo = new UserInfo( this );
    SystemInfo sysInfo = new SystemInfo( this );

    addMenu( menuList, buildG3ReduxHomeMenuTab( userInfo, sysInfo ) );
    addMenu( menuList, buildG3ReduxAdminMenuTab( userInfo, sysInfo ) );

    return menuList;
  }

  private MerchlinqLevelData getFeatureAwardsForPromotion( Promotion promo, Country country ) throws ServiceErrorException
  {
    // check cache first
    AbstractRecognitionPromotion promotion = (AbstractRecognitionPromotion)promo;
    Map promotionMap = (Map)featuredAwardsCache.get( promotion.getId() );
    // set default timeout for 1 hour
    if ( null == promotionMap )
    {
      promotionMap = new TimedMap( 3600 );
      featuredAwardsCache.put( promotion.getId(), promotionMap );
    }

    String programId = getValidFeaturedAwardsProgramId( promotion, country );
    MerchlinqLevelData featuredAwards = (MerchlinqLevelData)promotionMap.get( programId );

    if ( featuredAwards == null )
    {
      AwardBanqMerchResponseValueObject merchLinqResponse = merchLevelService.getMerchlinqLevelDataWebService( programId, true, true );
      featuredAwards = merchLevelService.buildMerchLinqLevelData( merchLinqResponse );
      promotionMap.put( programId, featuredAwards );
    }

    return featuredAwards;
  }

  // get the correct program ID for this promotion
  private String getValidFeaturedAwardsProgramId( AbstractRecognitionPromotion promotion, Country country )
  {
    PromoMerchCountry promoMerchCountry = getPromoMerchCountry( promotion, country );
    if ( promoMerchCountry == null )
    {
      return null;
    }

    return promoMerchCountry.getProgramId();
  }

  private PromoMerchCountry getPromoMerchCountry( Promotion promotion, Country country )
  {
    Set promoMerchCountries = promotion.getPromoMerchCountries();
    if ( promoMerchCountries == null || promoMerchCountries.size() == 0 )
    {
      return null;
    }

    Iterator merchCountryIter = promoMerchCountries.iterator();
    while ( merchCountryIter.hasNext() )
    {
      PromoMerchCountry pmc = (PromoMerchCountry)merchCountryIter.next();
      if ( pmc.getCountry().equals( country ) )
      {
        return pmc;
      }
    }

    // default to whatever is in there
    return (PromoMerchCountry)promoMerchCountries.iterator().next();
  }

  public List getWhatsNewList( List eligiblePromotionList, Country country )
  {
    List whatsNewList = new ArrayList();
    for ( Iterator i = eligiblePromotionList.iterator(); i.hasNext(); )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)i.next();
      Promotion promotion = promoMenuBean.getPromotion();
      if ( promotion.isRecognitionPromotion() && promotion.isLive() && ( promoMenuBean.isCanReceive() || promoMenuBean.isCanSubmit() ) )
      {
        RecognitionPromotion recPromo = (RecognitionPromotion)promotion;
        if ( recPromo.getHomePageItems() != null && !recPromo.getHomePageItems().isEmpty() )
        {
          addToWhatsNewList( whatsNewList, recPromo, country );
        }
      }
    }

    return whatsNewList;
  }

  private void addToWhatsNewList( List whatsNewList, RecognitionPromotion promotion, Country country )
  {
    // get product to look up
    HomePageItem homePageItem = (HomePageItem)promotion.getHomePageItems().iterator().next();
    String productId = homePageItem.getProductId();

    // get product list from awardslinq
    MerchlinqLevelData merchlinqLevelData = null;
    try
    {
      merchlinqLevelData = getFeatureAwardsForPromotion( promotion, country );
    }
    catch( ServiceErrorException e )
    {
      log.error( e.getMessage(), e );
    }

    // look up product level (in awardslinq product list)
    String levelName = null;
    if ( merchlinqLevelData != null )
    {
      for ( Iterator i = merchlinqLevelData.getLevels().iterator(); i.hasNext(); )
      {
        MerchLevel merchLevel = (MerchLevel)i.next();
        if ( merchLevel != null && merchLevel.getProducts() != null )
        {
          for ( Iterator j = merchLevel.getProducts().iterator(); j.hasNext(); )
          {
            MerchLevelProduct merchLevelProduct = (MerchLevelProduct)j.next();
            if ( productId.equals( merchLevelProduct.getProductIds().iterator().next() ) )
            {
              levelName = merchLevel.getName();
              ProductGroupDescription prodDescr = merchLevelProduct.getProductGroupDescriptions().get( UserManager.getLocale() );

              if ( prodDescr == null )
              {
                prodDescr = merchLevelProduct.getProductGroupDescriptions().get( UserManager.getDefaultLocale() );
              }
              homePageItem.setLongDescription( prodDescr != null ? prodDescr.getCopy() : "" );
              break;
            }
          }
        }
      }
    }

    // look up the CM asset key for the product level
    String cmAssetKey = null;
    if ( levelName != null )
    {
      PromoMerchCountry promoMerchCountry = getPromoMerchCountry( promotion, country );
      for ( Iterator i = promoMerchCountry.getLevels().iterator(); i.hasNext(); )
      {
        PromoMerchProgramLevel promoMerchProgramLevel = (PromoMerchProgramLevel)i.next();
        if ( levelName.equals( promoMerchProgramLevel.getLevelName() ) )
        {
          cmAssetKey = promoMerchProgramLevel.getCmAssetKey();
          break;
        }
      }
    }

    // set level display name
    if ( cmAssetKey != null )
    {
      homePageItem.setDisplayLevelName( CmsResourceBundle.getCmsBundle().getString( cmAssetKey, PromoMerchProgramLevel.SPOTLIGHT_LEVEL_NAME_KEY ) );
    }

    // update image urls with https
    if ( !StringUtils.isEmpty( homePageItem.getDetailImgUrl() ) )
    {
      homePageItem.setDetailImgUrl( homePageItem.getDetailImgUrl().toString().replaceFirst( "http:", "https:" ) );
    }
    if ( !StringUtils.isEmpty( homePageItem.getTmbImageUrl() ) )
    {
      homePageItem.setTmbImageUrl( homePageItem.getTmbImageUrl().toString().replaceFirst( "http:", "https:" ) );
    }

    String browseAwards = PromotionMenuUtils.getBrowseAwardsLink( null, promotion );
    WhatsNewBean whatNewBean = new WhatsNewBean( homePageItem, browseAwards );
    whatsNewList.add( whatNewBean );
  }

  /*
   * clients should call this method, which leverages the cache and has a safety method should it be
   * null for some reason
   */
  public List<Promotion> getAllLivePromotionsFromCache()
  {
    if ( null == eligiblePromotions.get( "eligiblePromotions" ) )
    {
      this.eligiblePromotions.put( "eligiblePromotions", buildAndHydrateAllLivePromotionList() );
    }
    return (List<Promotion>)eligiblePromotions.get( "eligiblePromotions" );
  }

  /*
   * This method should be called from a refresh process or from any event that should trigger a
   * cache event TODO: Do we bother to Synchronize/lock this cache?
   */
  public void refreshAllLivePromotionCache()
  {
    List<Promotion> eligiblePromotions = buildAndHydrateAllLivePromotionList();
    this.eligiblePromotions.put( "eligiblePromotions", eligiblePromotions );
  }

  /*
   * this method always hits the database to get the latest
   */
  private List<Promotion> buildAndHydrateAllLivePromotionList()
  {
    SystemInfo sysInfo = new SystemInfo( this );
    PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
    if ( sysInfo.hasProductClaims() )
    {
      promoQueryConstraint.setMasterOrChildConstraint( new Boolean( true ) );
    }
    promoQueryConstraint
        .setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ),
                                                                      PromotionStatusType.lookup( PromotionStatusType.COMPLETE ),
                                                                      PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
    promoQueryConstraint.setOrderByPromotionNameCaseInsensitive( true );

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    // only grab the neccessary Associations
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CHILD_PROMOTIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ECARDS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CERTIFICATES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BEHAVIORS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISION_ROUNDS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_NOMINATION_TIME_PERIODS ) );

    return promotionService.getPromotionListWithAssociationsForHomePage( promoQueryConstraint, promoAssociationRequestCollection );
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.maincontent.MainContentService#buildEligiblePromoList(AuthenticatedUser)
   * @param authenticatedUser
   * @return List
   */
  public List<PromotionMenuBean> buildEligiblePromoList( AuthenticatedUser authenticatedUser )
  {
    List<PromotionMenuBean> eligiblePromotionList = new ArrayList<PromotionMenuBean>();
    UserInfo userInfo = new UserInfo( this );

    List<Promotion> allPromoList = getAllLivePromotionsFromCache();
    
    Participant participant = userInfo.getParticipant();
    for ( Promotion promotion : allPromoList )
    {
      PromotionMenuBean promoMenuBean = new PromotionMenuBean();
      promoMenuBean.setPromotion( promotion );
      
    //Client customization start WIP#25589
      promoMenuBean.setUtilizeParentBudgets( promotion.isUtilizeParentBudgets() );
      //Client customization end WIP#25589
      
      boolean showSubmitClaim = checkIfShowSubmitClaim( promotion, participant );

      if ( showSubmitClaim )
      {
        promoMenuBean.setCanSubmit( true );
      }
      boolean isPartnerAudience = isPartnerAudience( promotion, participant );
      // users can be in both the goal selection role AND the Partner role
      if ( promotion.isGoalQuestOrChallengePointPromotion() )
      {
        promoMenuBean.setPartner( isPartnerAudience );
      }
      if ( promotion.isGoalQuestOrChallengePointPromotion() && ( isPartnerAudience || isPromoAudience( promotion, participant ) ) )
      {
        Date now = new Date();
        boolean viewable = now.after( promotion.getTileDisplayStartDate() ) && now.before( promotion.getTileDisplayEndDate() ) && !promotion.isExpired();
        if ( viewable )
        {
          promoMenuBean.setViewTile( true );
        }
      }
      if ( checkIfShowPromoRules( promotion, participant ) )
      {
        promoMenuBean.setCanViewRules( true );
      }
      if ( checkIfReceivable( promotion, participant ) )
      {
        promoMenuBean.setCanReceive( true );
      }
      // If promotion is of type quiz then check the user is in audience set
      if ( promotion instanceof QuizPromotion )
      {
        QuizPromotion quizPromotion = (QuizPromotion)promotion;
        if ( quizPromotion.isDIYQuizPromotion() )
        {
          eligiblePromotionList.add( promoMenuBean );
        }
        else
        {
          if ( quizPromotion.getPrimaryAudienceType().getCode().equals( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
          {
            eligiblePromotionList.add( promoMenuBean );
          }
          else
          {
            if ( audienceService.isParticipantInPrimaryAudience( promotion, participant ) )
            {
              eligiblePromotionList.add( promoMenuBean );
            }
          }
        }
      }
      else if ( promotion instanceof ThrowdownPromotion )
      {
        if ( promotion.getPrimaryAudienceType().getCode().equals( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
        {
          eligiblePromotionList.add( promoMenuBean );
        }
        else // check the audiences.....
        {
          Set<Audience> allAudiences = ( (ThrowdownPromotion)promotion ).getSpectatorAudiences();
          for ( Audience audience : allAudiences )
          {
            if ( audienceService.isParticipantInAudience( participant.getId(), audience ) )
            {
              eligiblePromotionList.add( promoMenuBean );
              break;
            }
          }
        }
      }
      else
      {
        eligiblePromotionList.add( promoMenuBean );
      }
    }
    return eligiblePromotionList;
  }

  /**
   * Overridden from
   *
   * @see com.biperf.core.service.maincontent.MainContentService#buildEligiblePromoList(AuthenticatedUser,boolean)
   * @param authenticatedUser
   *  @param activityFlag
   * @return List
   */
  public List buildEligiblePromoList( AuthenticatedUser authenticatedUser, boolean activityFlag )
  { // TODO: Can this delegate to the other method???? this is almost the same..
    List eligiblePromotionList = new ArrayList();
    UserInfo userInfo = new UserInfo( this );

    List<Promotion> allPromoList = getAllLivePromotionsFromCache();

    Participant participant = userInfo.getParticipant();
    Iterator it = allPromoList.iterator();
    while ( it.hasNext() )
    {
      Promotion promotion = (Promotion)it.next();
      PromotionMenuBean promoMenuBean = new PromotionMenuBean();
      promoMenuBean.setPromotion( promotion );
      boolean flag = false;
      boolean showSubmitClaim = checkIfShowSubmitClaim( promotion, participant, activityFlag );

      if ( showSubmitClaim )
      {
        promoMenuBean.setCanSubmit( true );
      }
      if ( !showSubmitClaim && promotion.isGoalQuestPromotion() )
      {
        promoMenuBean.setPartner( isPartnerAudience( promotion, participant ) );
      }
      if ( checkIfShowPromoRules( promotion, participant ) )
      {
        promoMenuBean.setCanViewRules( true );
        flag = true;
      }
      if ( checkIfReceivable( promotion, participant ) )
      {
        promoMenuBean.setCanReceive( true );
        flag = true;
      }
      if ( ( showSubmitClaim || flag ) && activityFlag )
      {
        eligiblePromotionList.add( promoMenuBean );
      }
      else if ( !activityFlag )
      {
        if ( isPromoAudience( promotion, participant ) )
        {
          eligiblePromotionList.add( promoMenuBean );
        }

      }
    }
    return eligiblePromotionList;
  }
  
  /*START Client customization (home page tuning)*/
  /*
   * this method will hold only live and online promotions.
   */
  public List<PromotionCacheBean> getAllOnlineLivePromotionsFromCache()
  {
    if ( null == eligiblePromotions.get( "eligiblePromotionsOnline" ) )
    {
      List<Promotion> promotions = buildAndHydrateAllOnlineLivePromotionList() ;
      List<PromotionCacheBean> eligiblePromotionCacheBeans = new ArrayList<PromotionCacheBean>() ;
      for( Promotion promotion:promotions )
      {
        PromotionCacheBean cacheBean = new PromotionCacheBean() ;
        cacheBean.setPromotion( promotion ) ;
        if( promotion.isAbstractRecognitionPromotion() )
        {
          cacheBean.setBehaviors( buildBehaviors( (AbstractRecognitionPromotion)promotion ) ) ;
        }
        eligiblePromotionCacheBeans.add( cacheBean ) ;
      }
      this.eligiblePromotions.put( "eligiblePromotionsOnline", eligiblePromotionCacheBeans );
    }
    return (List<PromotionCacheBean>)eligiblePromotions.get( "eligiblePromotionsOnline" );
  }
  
  public List<PromotionMenuBean> getEligiblePromotionsFromCache( final String cacheKey )
  {
    List<PromotionMenuBean> eligiblePromotionMenuBeans = (List<PromotionMenuBean>)eligiblePromotions.get( cacheKey );
    return eligiblePromotionMenuBeans;
  }
  /**
   * Overridden from
   *
   * @see com.biperf.core.service.maincontent.MainContentService#buildOnlineEligiblePromoList(AuthenticatedUser)
   * @param authenticatedUser
   * @return List
   */
  public List<PromotionMenuBean> buildOnlineEligiblePromoList( AuthenticatedUser authenticatedUser )
  {
    String cacheKey = "OnlineEligiblePromotionMenuBeans - "+authenticatedUser.getUsername();
    List<PromotionMenuBean> eligiblePromotionList = getEligiblePromotionsFromCache( cacheKey );
    if (null == eligiblePromotionList)
    {
      eligiblePromotionList = new ArrayList<PromotionMenuBean>();
      UserInfo userInfo = new UserInfo( this );
  
      List<PromotionCacheBean> allPromoList = getAllOnlineLivePromotionsFromCache();
  
      Participant participant = userInfo.getParticipant();
      for ( PromotionCacheBean promotionCacheBean : allPromoList )
      {
        PromotionMenuBean promoMenuBean = new PromotionMenuBean();
        Promotion promotion = promotionCacheBean.getPromotion() ;
        promoMenuBean.setPromotion( promotion );
        promoMenuBean.setBehaviors( promotionCacheBean.getBehaviors() );
        boolean showSubmitClaim = checkIfShowSubmitClaim( promotion, participant );
  
        if ( showSubmitClaim )
        {
          promoMenuBean.setCanSubmit( true );
        }
        // users can be in both the goal selection role AND the Partner role
        if ( promotion.isGoalQuestOrChallengePointPromotion() )
        {
          promoMenuBean.setPartner( isPartnerAudience( promotion, participant ) );
        }          
        if( (promotion.isGoalQuestOrChallengePointPromotion() ) && 
            ( isPartnerAudience( promotion, participant ) || isPromoAudience( promotion, participant ) ) )
        {
          Date now = new Date() ;
          boolean viewable = ( now.after( promotion.getTileDisplayStartDate() ) && now.before( promotion.getTileDisplayEndDate() ) && !promotion.isExpired() ) ;
          if ( viewable )
          {
            promoMenuBean.setViewTile( true );
          }
        }
        if ( checkIfShowPromoRules( promotion, participant ) )
        {
          promoMenuBean.setCanViewRules( true );
        }
        if ( checkIfReceivable( promotion, participant ) )
        {
          promoMenuBean.setCanReceive( true );
        }
        // If promotion is of type quiz then check the user is in audience set
        if ( promotion instanceof QuizPromotion )
        {
          QuizPromotion quizPromotion = (QuizPromotion)promotion;
          if ( quizPromotion.isDIYQuizPromotion() )
          {
            eligiblePromotionList.add( promoMenuBean );
          }
          else
          {
            if ( quizPromotion.getPrimaryAudienceType().getCode().equals( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
              eligiblePromotionList.add( promoMenuBean );
            else
            {
              if ( audienceService.isParticipantInPrimaryAudience( promotion, participant ) )
              {
                eligiblePromotionList.add( promoMenuBean );
              }
            }
          }
        }
        else if ( promotion instanceof ThrowdownPromotion )
        {
          if( promotion.getPrimaryAudienceType().getCode().equals(  PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) )
          {
            eligiblePromotionList.add( promoMenuBean ) ;
          }
          else // check the audiences.....
          {
            Set<Audience> allAudiences = ((ThrowdownPromotion)promotion).getSpectatorAudiences() ;
            for( Audience audience:allAudiences )
            {
              if( audienceService.isParticipantInAudience( participant.getId(), audience ) )
              {
                eligiblePromotionList.add( promoMenuBean ) ;
                break ;
              }
            }
          }
        }
        else
        {
          eligiblePromotionList.add( promoMenuBean );
        }
      }
      eligiblePromotions.put( cacheKey, eligiblePromotionList);
    }
    return eligiblePromotionList;
  }
  
  private List<BehaviorBean> buildBehaviors( AbstractRecognitionPromotion promotion )
  {
    String siteUrlPrefix = sysVarService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    List<BehaviorBean> behaviors = new ArrayList<BehaviorBean>() ;

    GamificationService gamificationService = getGamificationService();
    Long promotionId = promotion.getId();
    for ( PromotionBehavior behavior : promotion.getPromotionBehaviors() )
    {
      String badgeImgName = "";
      BadgeRule badgeRule = gamificationService.getBadgeRuleByBehaviorName( behavior.getPromotionBehaviorType().getCode(), promotionId );
      if ( badgeRule != null )
      {
        List<BadgeLibrary> earnedNotEarnedImageList = gamificationService.getEarnedNotEarnedImageList( badgeRule.getBadgeLibraryCMKey() );
        for( BadgeLibrary badgeLib:earnedNotEarnedImageList )
        {
          badgeImgName = siteUrlPrefix + badgeLib.getEarnedImageSmall();
        }
      }
      behaviors.add( new BehaviorBean( behavior, badgeImgName, promotion.getPromotionType() ) ); //bug 56948 added PromotionType ) );
    }
    if ( !behaviors.isEmpty() )
    {
      Collections.sort( behaviors );
    }
    return behaviors ;
  }
  
  /*
   * this method always hits the database to get the latest
   * live and online promotions
   */
  private List<Promotion> buildAndHydrateAllOnlineLivePromotionList()
  {
    SystemInfo sysInfo = new SystemInfo( this );
    PromotionQueryConstraint promoQueryConstraint = new PromotionQueryConstraint();
    if ( sysInfo.hasProductClaims() )
    {
      promoQueryConstraint.setMasterOrChildConstraint( new Boolean( true ) );
    }
    promoQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE )} );
    promoQueryConstraint.setOnlineEntryOrMerchOrEngagementOrSSI( new Boolean( true ) );
    promoQueryConstraint.setOrderByPromotionNameCaseInsensitive( true );

    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    // only grab the neccessary Associations
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CHILD_PROMOTIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_MERCHANDISE_COUNTRIES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CLAIM_FORM_DEFINITION ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.ECARDS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CERTIFICATES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BEHAVIORS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISIONS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_DIVISION_ROUNDS ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_NOMINATION_TIME_PERIODS ) );

    return promotionService.getPromotionListWithAssociationsForRecognitions( promoQueryConstraint, promoAssociationRequestCollection );
  }
  
  public List getPublicRecognitionDepartmentCache()
  {
    List result = (List)publicRecognitionDepartmentCache.get("publicRecognitionDepartmentCache");

    if ( result == null )
    {
      log.debug( "creating PublicRecognitionDepartmentCache" );
      result = participantService.getAllActiveDepartmentsForPublicRecognition();

      // -------------------------------------
      // Cache public recognition departments
      // -------------------------------------
      this.publicRecognitionDepartmentCache.put( "publicRecognitionDepartmentCache", result ) ;
    }
    else
    {
      log.debug( "using cached PublicRecognitionDepartmentCache" );
    }
    return result;
  }
  
  /*  customization start */
  public GamificationService getGamificationService()
  {
    return this.gamificationService ;
  }
  
  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService ;
  }
  /*  customization end */
  /*END Client customization (home page tuning)*/

  private boolean isPromoAudience( Promotion promotion, Participant participant )
  {
    return audienceService.isParticipantInSecondaryAudience( promotion, participant, null ) || audienceService.isParticipantInPrimaryAudience( promotion, participant );
  }

  public List getMerchAwardRemindersForAcivityList( Long participantId, List eligiblePromotions, Country country )
  {
    // Check cache
    List awardReminders = (List)awardRemindersCache.get( participantId );
    if ( awardReminders == null || awardReminders.size() == 0 )
    {
      // Load award reminders from database
      awardReminders = merchOrderService.getMerchAwardReminders( participantId );

      // Store in cache
      awardRemindersCache.put( participantId, awardReminders );

      // Set award reminder URLs
      if ( awardReminders.size() > 0 )
      {
        // Load participant
        Participant participant = participantService.getParticipantById( UserManager.getUserId() );

        for ( Iterator i = awardReminders.iterator(); i.hasNext(); )
        {
          MerchAwardReminderBean awardReminder = (MerchAwardReminderBean)i.next();

          // find promotion
          Promotion promotion = findMerchPromotion( eligiblePromotions, awardReminder.getPromotionId() );

          if ( promotion != null )
          {
            String url = null;
            if ( promotion.isGoalQuestPromotion() )
            {
              PaxGoal paxgoal = getPaxGoal( promotion.getId(), participant.getId() );
              url = getGQShoppingURL( (GoalQuestPromotion)promotion, participant, awardReminder.getMerchOrderId(), paxgoal );
            }
            else if ( promotion.isChallengePointPromotion() )
            {
              url = getCPShoppingURL( (ChallengePointPromotion)promotion, participant, awardReminder.getMerchOrderId() );
            }
            else
            {
              url = getMerchOrderService().getOnlineShoppingUrl( promotion, awardReminder.getMerchOrderId(), country, participant, false );
            }

            // get URL
            awardReminder.setOnlineShoppingUrl( url );
          }
        }
      }
    }

    return awardReminders;
  }

  private PaxGoal getPaxGoal( Long promotionId, Long userId )
  {
    PaxGoal paxgoal = getPaxGoalService().getPaxGoalByPromotionIdAndUserId( promotionId, userId );
    return paxgoal;
  }

  private String getGQShoppingURL( GoalQuestPromotion promotion, Participant pax, Long merchOrderId, PaxGoal paxGoal )
  {
    if ( PromotionAwardsType.POINTS.equals( promotion.getAwardType().getCode() ) )
    {
      // -------------------------
      // Check the shopping type
      // ---------------------------
      String shoppingType = shoppingService.checkShoppingType( pax.getId() );
      if ( ShoppingService.INTERNAL.equals( shoppingType ) )
      {
        return ClientStateUtils.generateEncodedLink( sysVarService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                     "/shopping.do?method=displayInternal",
                                                     null );
      }
      else if ( ShoppingService.EXTERNAL.equals( shoppingType ) )
      {
        return ClientStateUtils.generateEncodedLink( sysVarService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                     "/externalSupplier.do?method=displayExternal",
                                                     null );
      }
    }
    else
    {
      MerchOrder merchOrder = merchOrderService.getMerchOrderById( merchOrderId );

      if ( !StringUtils.isEmpty( merchOrder.getFullGiftCode() ) )
      {
        Map shoppingParameters = new HashMap();
        shoppingParameters.put( "giftcode", merchOrder.getFullGiftCode() );
        if ( paxGoal != null && !StringUtils.isEmpty( paxGoal.getProductSetId() ) && !StringUtils.isEmpty( paxGoal.getCatalogId() ) )
        {
          shoppingParameters.put( "productSetId", paxGoal.getProductSetId() );
          shoppingParameters.put( "catalogId", paxGoal.getCatalogId() );
        }
        if ( paxGoal != null && paxGoal.getGoalQuestPromotion() != null )
        {
          shoppingParameters.put( "promotionId", paxGoal.getGoalQuestPromotion().getId() );
        }
        shoppingParameters.put( "allowPointConversion", ( (GoalQuestPromotion)promotion ).isApqConversion() + "" );

        return ClientStateUtils.generateEncodedLink( sysVarService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                     "/shopping.do?method=displayMerchLinq",
                                                     shoppingParameters );
      }
    }
    return null;
  }

  private String getCPShoppingURL( ChallengePointPromotion promotion, Participant pax, Long merchOrderId )
  {
    if ( PromotionAwardsType.POINTS.equals( promotion.getChallengePointAwardType().getCode() ) )
    {
      // -------------------------
      // Check the shopping type
      // ---------------------------
      String shoppingType = shoppingService.checkShoppingType( pax.getId() );
      log.debug( "Shopping Type--->" + shoppingType );
      if ( ShoppingService.INTERNAL.equals( shoppingType ) )
      {
        return ClientStateUtils.generateEncodedLink( sysVarService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                     "/shopping.do?method=displayInternal",
                                                     null );
      }
      else if ( ShoppingService.EXTERNAL.equals( shoppingType ) )
      {
        return ClientStateUtils.generateEncodedLink( sysVarService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                     "/externalSupplier.do?method=displayExternal",
                                                     null );
      }
    }
    else
    {
      MerchOrder merchOrder = merchOrderService.getMerchOrderById( merchOrderId );

      if ( !StringUtils.isEmpty( merchOrder.getFullGiftCode() ) )
      {
        Map shoppingParameters = new HashMap();
        shoppingParameters.put( "giftcode", merchOrder.getFullGiftCode() );

        if ( null != pax )
        {
          loadParticipantAddressInfo( shoppingParameters, pax );
        }

        loadLevelLabels( shoppingParameters, promotion );
        return ClientStateUtils.generateEncodedLink( sysVarService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                     "/shopping.do?method=displayChallengepointOnline",
                                                     shoppingParameters );
      }
    }
    return null;
  }

  private void loadParticipantAddressInfo( Map shoppingParameters, Participant pax )
  {
    if ( null != pax )
    {
      AssociationRequestCollection userAssociations = new AssociationRequestCollection();
      userAssociations.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ALL ) );

      pax = participantService.getParticipantByIdWithAssociations( pax.getId(), userAssociations );
      shoppingParameters.put( "firstName", pax.getFirstName() );
      shoppingParameters.put( "lastName", pax.getLastName() );

      // if ( pax.getPrimaryAddress()!=null && pax.getPrimaryAddress().getAddress()!=null)

      if ( pax.getPrimaryAddress() != null )
      {
        shoppingParameters.put( "accountNum", pax.getAwardBanqNumber() );
        shoppingParameters.put( "centraxId", pax.getCentraxId() );
        shoppingParameters.put( "omPaxId", pax.getCentraxId() );
        if ( null != pax.getLanguageType() )
        {
          shoppingParameters.put( "locale", pax.getLanguageType().getCode() );
        }

        Country country = null;
        if ( pax.getPrimaryAddress().getAddress() != null )
        {
          country = pax.getPrimaryAddress().getAddress().getCountry();
        }

        if ( country != null )
        {
          shoppingParameters.put( "campaignId", country.getCampaignNbr() );
          shoppingParameters.put( "campaignPassword", country.getCampaignPassword() );
        }

        UserAddress userAddress = pax.getPrimaryAddress();
        Address address = userAddress.getAddress();

        if ( address != null )
        {
          shoppingParameters.put( "addressLine1", address.getAddr1() );
          shoppingParameters.put( "addressLine2", address.getAddr2() );
          shoppingParameters.put( "addressLine3", address.getAddr3() );
          shoppingParameters.put( "city", address.getCity() );

          if ( address.getStateType() != null )
          {
            shoppingParameters.put( "stateCode", address.getStateType().getAbbr() );
          }

          shoppingParameters.put( "zipCode", address.getPostalCode() );

          if ( address.getCountry() != null )
          {
            shoppingParameters.put( "country", address.getCountry().getAwardbanqAbbrev() );
          }

          if ( null != pax.getPrimaryEmailAddress() )
          {
            shoppingParameters.put( "emailAddress", pax.getPrimaryEmailAddress().getEmailAddr() );
          }

          if ( null != pax.getPrimaryPhone() )
          {
            shoppingParameters.put( "daytimePhone", pax.getPrimaryPhone().getPhoneNbr() );
          }
        }
      }
    }
  }

  private void loadLevelLabels( Map shoppingParameters, ChallengePointPromotion promotion )
  {
    shoppingParameters.put( "promotionId", promotion.getId() );
  }

  @Override
  public BudgetMeter getBudgetMeter( Long participantId, String timezone, List<PromotionMenuBean> eligiblePromotions )
  {
    // Setup variables
    BudgetMeter budgetMeter = new BudgetMeter();
    final BigDecimal US_MEDIA_VALUE = countryService.getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
    final BigDecimal USER_MEDIA_VALUE = userService.getBudgetMediaValueForUser( participantId );
    String currentDateTime = getCurrentDateTimeByTimeZone( timezone, UserManager.getLocale() );
    String currentDate = getCurrentDateByTimeZone( timezone );

    List<Long> eligiblePromotionIds = new ArrayList<Long>();
    for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
    {
      eligiblePromotionIds.add( promotionMenuBean.getPromotion().getId() );
    }

    // Fetch raw budget meter data
    List<BudgetMeterData> rawDataList = budgetService.getBudgetMeterDataForPax( participantId, eligiblePromotionIds, currentDate );

    // Transform raw data to budget meter bean
    List<BudgetMeterDetailBean> budgetMeterDetails = budgetMeter.getBudgetMeterDetails();
    for ( BudgetMeterData rawData : rawDataList )
    {
      // If budget id already exists, just add new promo, otherwise create new budget meter detail
      BudgetMeterDetailBean currentBmDetail = budgetMeter.getBudgetMeterDetailForBudgetId( rawData.getBudgetId() );
      BigDecimal remainingBudget = new BigDecimal( 0 );
      BigDecimal usedBudget = new BigDecimal( 0 );
      BigDecimal totalUnapprovedAwardQty = new BigDecimal( 0 );
      Promotion promotion = promotionService.getPromotionById( rawData.getPromotionId() );
      Long budgetMasterId = null;
      if ( currentBmDetail == null )
      {
        currentBmDetail = new BudgetMeterDetailBean();
        budgetMeterDetails.add( currentBmDetail );

        currentBmDetail.setBudgetId( rawData.getBudgetId() );
        currentBmDetail.setBudgetType( rawData.getBudgetMasterType() );
        currentBmDetail.setBudgetMasterId( rawData.getBudgetMasterId() );
        if ( Objects.nonNull( rawData.getBudgetMasterId() ) )
        {
          BudgetMaster budgetMaster = budgetService.getBudgetMasterById( rawData.getBudgetMasterId(), null );
          budgetMasterId = budgetMaster.isMultiPromotion() ? budgetMaster.getId() : null;
        }        
        currentBmDetail.setStartDate( rawData.getBudgetStartDate() );
        currentBmDetail.setEndDate( rawData.getBudgetEndDate() );
        currentBmDetail.setNodeName( rawData.getBudgetNodeName() );
        currentBmDetail.setNodeId( rawData.getBudgetNodeId() );      
        if(rawData.getBudgetNodeIsPrimary()!=null)
        {
        currentBmDetail.setIsPrimaryNode( rawData.getBudgetNodeIsPrimary() );
        }
        if ( promotion.getBudgetMaster().isCentralBudget() )
        {
          if ( promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isIncludePurl() )
          {
            totalUnapprovedAwardQty = promotionService.getTotalUnapprovedAwardQuantityPurl( promotion.getId(), null, null, budgetMasterId );
          }
          else
          {
            totalUnapprovedAwardQty = promotionService.getTotalUnapprovedAwardQuantity( promotion.getId(), null, null, budgetMasterId );
          }
        }
        else if ( promotion.getBudgetMaster().isParticipantBudget() )
        {
          if ( promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isIncludePurl() )
          {
            totalUnapprovedAwardQty = promotionService.getTotalUnapprovedAwardQuantityPurl( promotion.getId(), participantId, null, budgetMasterId );
          }
          else
          {
            totalUnapprovedAwardQty = promotionService.getTotalUnapprovedAwardQuantity( promotion.getId(), participantId, null, budgetMasterId );
          }
        }
        else if ( promotion.getBudgetMaster().isNodeBudget() )
        {
          if ( promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isIncludePurl() )
          {
            totalUnapprovedAwardQty = promotionService.getTotalUnapprovedAwardQuantityPurl( promotion.getId(), null, rawData.getBudgetNodeId(), budgetMasterId );
          }
          else
          {
            totalUnapprovedAwardQty = promotionService.getTotalUnapprovedAwardQuantity( promotion.getId(), null, rawData.getBudgetNodeId(), budgetMasterId );
          }
        }

        if ( rawData.getRemainingBudget().subtract( totalUnapprovedAwardQty ).compareTo( BigDecimal.ZERO ) > 0 )
        {
          remainingBudget = rawData.getRemainingBudget().subtract( totalUnapprovedAwardQty );
        }
        else
        {
          remainingBudget = new BigDecimal( 0 );
        }
        usedBudget = rawData.getUsedBudget().add( totalUnapprovedAwardQty );

        if ( usedBudget.compareTo( BigDecimal.ZERO ) < 0 )
        {
          usedBudget = rawData.getTotalBudget();
        }

        // Bug 67645 start
        BigDecimal convertedUsedBudget = BudgetUtils.applyMediaConversion( usedBudget, US_MEDIA_VALUE, USER_MEDIA_VALUE );
        BigDecimal convertedTotalBudget = BudgetUtils.applyMediaConversion( rawData.getTotalBudget(), US_MEDIA_VALUE, USER_MEDIA_VALUE );
        remainingBudget = BudgetUtils.applyMediaConversion( remainingBudget, US_MEDIA_VALUE, USER_MEDIA_VALUE );
        
        currentBmDetail.setUsedBudget( (int)Math.floor( convertedUsedBudget.doubleValue() ) );
        currentBmDetail.setTotalBudget( (int)Math.floor( convertedTotalBudget.doubleValue() ) );
        currentBmDetail.setRemainingBudget( (int)Math.floor( remainingBudget.doubleValue() ) );
        // Bug 67645 end
      }

      BudgetMeterDetailPromoBean bmPromo = new BudgetMeterDetailPromoBean();
      bmPromo.setPromoId( rawData.getPromotionId() );
      bmPromo.setPromoName( rawData.getPromotionName() );
      bmPromo.setPromoStartDate( rawData.getPromotionStartDate() );
      bmPromo.setPromoEndDate( rawData.getPromotionEndDate() );
      if ( promotion.isUtilizeParentBudgets() )
      {
        bmPromo.setBudgetOwner( rawData.getBudgetNodeName() );
      }
      currentBmDetail.getPromoList().add( bmPromo );
      //Remove duplicate promos
      HashSet<Object> existingPromo=new HashSet<>();
      currentBmDetail.getPromoList().removeIf(e->!existingPromo.add(e.getPromoId())); 
    }

    budgetMeter.sortAndPopulateDisplayNames();
    budgetMeter.setAsOfDate( currentDateTime );
    budgetMeter.setTimezone( timezone );
    return budgetMeter;
  }

  private String getCurrentDateTimeByTimeZone( String timeZone, Locale loggedInUserLocale )
  {
    SimpleDateFormat formatter = new SimpleDateFormat( DateFormatterUtil.getDateTimePattern( loggedInUserLocale ) );
    formatter.setTimeZone( TimeZone.getTimeZone( timeZone ) );

    return formatter.format( new Date() );
  }

  private String getCurrentDateByTimeZone( String timeZone )
  {
    SimpleDateFormat formatter = new SimpleDateFormat( "dd/MM/YYYY" );// oracle date format
    formatter.setTimeZone( TimeZone.getTimeZone( timeZone ) );

    return formatter.format( new Date() );
  }

  public boolean isParticipantHasBudgetMeter( Long participantId, List<PromotionMenuBean> eligiblePromotions )
  {
    List<Long> eligiblePromotionIds = new ArrayList<Long>();
    for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
    {
      eligiblePromotionIds.add( promotionMenuBean.getPromotion().getId() );
    }

    return budgetService.isParticipantHasBudgetMeter( participantId, eligiblePromotionIds );
  }

  private Promotion findMerchPromotion( List eligiblePromotions, Long promotionId )
  {
    for ( Iterator i = eligiblePromotions.iterator(); i.hasNext(); )
    {
      PromotionMenuBean promoMenuBean = (PromotionMenuBean)i.next();
      Promotion promo = promoMenuBean.getPromotion();
      if ( promo.getId().equals( promotionId ) )
      {
        return promo;
      }
    }

    return null;
  }

  /**
   * Callback on logout - clears the menuCache
   *
   * @param authenticatedUser
   */
  public void processLogout( AuthenticatedUser authenticatedUser )
  {
    clearMenuCache( authenticatedUser );
    clearAwardRemindersCache( authenticatedUser );
    clearPurlRemindersCache( authenticatedUser );
    clearBudgetTrackerCache( authenticatedUser );
  }

  /**
   * Clear the awardRemindersCache, which will be reloaded on next build call.
   *
   * @param authenticatedUser
   */
  private void clearAwardRemindersCache( AuthenticatedUser authenticatedUser )
  {
    awardRemindersCache.remove( authenticatedUser.getUserId() );
  }

  private void clearPurlRemindersCache( AuthenticatedUser authenticatedUser )
  {
    purlRemindersCache.remove( authenticatedUser.getUserId() );
  }

  /**
   * Clear the budgetTrackerCache, which will be reloaded on next build call.
   *
   * @param authenticatedUser
   */
  public void clearBudgetTrackerCache( AuthenticatedUser authenticatedUser )
  {
    budgetTrackerCache.remove( authenticatedUser.getUserId() );
    budgetTrackerCache.remove( "PROMOTION_DETAILS" );
    budgetTrackerCache.remove( "SHAREDBUDGET_DETAILS" );
  }

  /**
   * Clear the menu menuCache, which will be reloaded on next menu build call.
   *
   * @param authenticatedUser
   */
  public void clearMenuCache( AuthenticatedUser authenticatedUser )
  {
    menuCache.remove( authenticatedUser.getUserId() );
  }

  /**
   * Add a menu to a menu list if it is non-null
   */
  private void addMenu( List menuList, Menu menu )
  {
    if ( menu != null )
    {
      menuList.add( menu );
    }
  }

  /**
   * Builds the G3 Redux Admin Menu Page
   * @return pax admin Menu
   */
  public Menu buildG3ReduxParticipantAdminMenuPage()
  {
    List menuList = new ArrayList();
    UserInfo userInfo = new UserInfo( this );
    SystemInfo sysInfo = new SystemInfo( this );
    Menu paxAdminMenu = buildAdminMenuTab( userInfo, sysInfo );
    return paxAdminMenu;
  }

  /**
   * Builds the G3 Redux Home Menu Tab
   * @param userInfo
   * @param sysInfo
   * @return home Menu
   */
  private Menu buildG3ReduxHomeMenuTab( UserInfo userInfo, SystemInfo sysInfo )
  {
    return new Menu( "home.nav", "HOME", "/homePage.do", false );
  }

  /**
   * Builds the g3 redux Admin menu tab
   * @param userInfo
   * @param sysInfo
   * @return account Menu
   */
  private Menu buildG3ReduxAdminMenuTab( UserInfo userInfo, SystemInfo sysInfo )
  {
    if ( !userInfo.hasAdminMenuAccess() )
    {
      return null;
    }
    return new Menu( "home.nav", "ADMIN", "/participantAdmin.do", false );
  }

  /**
   * build the User Menu
   *
   * @return List of the user menu
   */
  private List buildUserMenuList()
  {
    List menuList = new ArrayList();
    UserInfo userInfo = new UserInfo( this );
    SystemInfo sysInfo = new SystemInfo( this );

    if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isViewParticipants() )
    {
      addMenu( menuList, buildParticipantsMenuTab( userInfo, sysInfo ) );
    }
    addMenu( menuList, buildPromotionsMenuTab( userInfo, sysInfo ) );
    addMenu( menuList, buildBudgetsMenuTab( userInfo, sysInfo ) );
    addMenu( menuList, buildCommunicationsMenuTab( userInfo, sysInfo ) );
    addMenu( menuList, buildSystemMenuTab( userInfo, sysInfo ) );
    addMenu( menuList, buildUserHelpMenuTab( userInfo, sysInfo ) );
    addMenu( menuList, buildUserReportsMenuTab( userInfo, sysInfo ) );
    return menuList;
  }

  /**
   * Builds the Admin menu for a pax
   *
   * @return admin menu
   */
  // TODO: Modify this for new roles
  public Menu buildAdminMenuTab( UserInfo userInfo, SystemInfo sysInfo )
  {
    if ( !userInfo.hasAdminMenuAccess() )
    {
      return null;
    }

    // Admin menu - menu 3
    Menu admin = new Menu( "home.navmenu.admin", "TABNAME", null, true );

    if ( userInfo.isViewParticipants() )
    {
      admin.addMenuOption( buildPaxMenu( userInfo, sysInfo ) );
    }

    admin.addMenuOption( buildSystemMenu( userInfo, sysInfo ) );
    admin.addMenuOption( buildAdminHelpMenu( userInfo, sysInfo ) );

    // For historical purpose, other [currently unused] menu methods are:
    // buildPromotionMenu
    // buildBudgetMenu
    // buildCommunicationsMenu

    return admin;
  }

  /**
   * Builds the Participant submenu for a Pax > Admin menu
   *
   * @return participant menu
   */
  private MenuOption buildPaxMenu( UserInfo userInfo, SystemInfo sysInfo )
  {
    MenuOption paxMenu = new MenuOption( "PARTICIPANTS", null );

    String returnUrl = "/participant/participantDisplay.do?method=display";

    MenuOption searchPax = new MenuOption( "SEARCH_PAX", "/participant/listBuilderPaxDisplay.do?&audienceMembersLookupReturnUrl=" + returnUrl + "&singleResult=true" );
    paxMenu.addSubMenuOption( searchPax );

    // if ( !userInfo.isStandardClientAdminComm() )
    // {
    // MenuOption addPax = new MenuOption( "ADD_PAX", "/participant/participantDisplay.do" );
    // paxMenu.addSubMenuOption( addPax );
    // }

    return paxMenu;
  }

  /**
   * Builds the Promotion submenu for a Pax > Admin menu
   *
   * @return promotion menu
   */
  private MenuOption buildPromotionMenu( UserInfo userInfo, SystemInfo sysInfo )
  {
    MenuOption promotionMenu = new MenuOption( "PROMOTIONS", null );

    MenuOption promoList = new MenuOption( "VIEW_PROMO_LIST", "/promotion/promotionListDisplay.do" );
    promotionMenu.addSubMenuOption( promoList );

    if ( ! ( userInfo.isStandardClientAdmin() || userInfo.isStandardClientAdminPromo() ) )
    {
      MenuOption addPax = new MenuOption( "ADD_PROMO", "/promotion/startPromotionWizard.do" );
      promotionMenu.addSubMenuOption( addPax );
    }

    MenuOption viewSweepstakesList = new MenuOption( "VIEW_SWEEPSTAKES", "/promotion/promotionSweepstakesListDisplay.do" );
    promotionMenu.addSubMenuOption( viewSweepstakesList );

    if ( sysInfo.hasBadges() )
    {
      MenuOption viewBadgeList = new MenuOption( "VIEW_BADGE_LIST", "/promotion/badgeList.do" );
      promotionMenu.addSubMenuOption( viewBadgeList );
    }

    if ( sysInfo.hasLeaderBoard() )
    {
      MenuOption viewLeaderBoardList = new MenuOption( "VIEW_LEADERBOARD_LIST", "/leaderBoardsList.do" );
      promotionMenu.addSubMenuOption( viewLeaderBoardList );
    }

    if ( sysInfo.hasQuizzes() )
    {
      MenuOption diyQuiz = new MenuOption( "VIEW_PARTICIPANT_DIY_QUIZ", "/promotion/diyQuizList.do" );
      promotionMenu.addSubMenuOption( diyQuiz );
    }

    return promotionMenu;
  }

  /**
   * Builds the Budget submenu for a Pax > Admin menu
   *
   * @return budget menu
   */
  private MenuOption buildBudgetMenu( UserInfo userInfo, SystemInfo sysInfo )
  {
    MenuOption budgetMenu = new MenuOption( "BUDGETS", null );

    MenuOption listBudgets = new MenuOption( "VIEW_BUDGET_MASTER_LIST", "/admin/budgetMasterListDisplay.do" );
    budgetMenu.addSubMenuOption( listBudgets );

    if ( userInfo.isClientAdmin() )
    {
      MenuOption addBudget = new MenuOption( "ADD_BUDGET_MASTER", "/admin/budgetMasterMaintainDisplay.do?method=prepareCreate" );
      budgetMenu.addSubMenuOption( addBudget );
    }

    if ( userInfo.isStandardClientAdmin() )
    {
      MenuOption inactiveBudget = new MenuOption( "EXTRACT_INACTIVE_BUDGETS", "/admin/extractInactiveBudgetsDisplay.do?method=display" );
      budgetMenu.addSubMenuOption( inactiveBudget );
    }
    return budgetMenu;
  }

  /**
   * Builds the Communications submenu for a Pax > Admin menu
   *
   * @return Communications menu
   */
  private MenuOption buildCommunicationsMenu( UserInfo userInfo, SystemInfo sysInfo )
  {
    MenuOption communicationsMenu = new MenuOption( "COMMUNICATIONS", null );

    MenuOption msgLib = new MenuOption( "VIEW_MSG_LIB", "/admin/messageList.do" );
    communicationsMenu.addSubMenuOption( msgLib );

    MenuOption adHoc = new MenuOption( "SEND_ADHOC_MSG", "/admin/sendAdHocMessageDisplay.do?method=prepareSend" );
    communicationsMenu.addSubMenuOption( adHoc );

    return communicationsMenu;
  }

  /**
   * Builds the System submenu for a Pax > Admin menu
   *
   * @return system menu
   */
  private MenuOption buildSystemMenu( UserInfo userInfo, SystemInfo sysInfo )
  {
    // System Menu
    MenuOption systemMenu = new MenuOption( "SYSTEM", null );

    if ( userInfo.isAnyClientAdminButReports() )
    {
      MenuOption audienceDefn = new MenuOption( "AUDIENCE", "/participant/audienceListDisplay.do" );
      systemMenu.addSubMenuOption( audienceDefn );
    }

    if ( userInfo.isClientAdmin() || userInfo.isStandardClientAdmin() || userInfo.isStandardClientAdminPromo() )
    {
      if ( !sysInfo.isSalesMaker() )
      {
        MenuOption calculatorLibrary = new MenuOption( "CALCULATOR_LIBRARY", "/calculator/calculatorLibraryListDisplay.do" );
        systemMenu.addSubMenuOption( calculatorLibrary );
      }
    }

    if ( userInfo.isClientAdmin() || userInfo.isStandardClientAdmin() || userInfo.isStandardClientAdminComm() )
    {
      MenuOption contentManager = new MenuOption( "CONTENT_MGR", "/admin/cmSso.do?method=sso", "_blank" );
      systemMenu.addSubMenuOption( contentManager );
    }

    if ( userInfo.isClientAdmin() )
    {
      MenuOption employersSetup = new MenuOption( "EMPLOYERS", "/admin/employerListDisplay.do" );
      systemMenu.addSubMenuOption( employersSetup );
    }

    if ( userInfo.isClientAdmin() )
    {
      MenuOption fileLoads = new MenuOption( "FILE_LOADS", "/admin/displayImportFileList.do" );
      systemMenu.addSubMenuOption( fileLoads );
    }

    if ( userInfo.isClientAdmin() || userInfo.isStandardClientAdmin() || userInfo.isStandardClientAdminPromo() )
    {
      MenuOption formLibrary = new MenuOption( "FORM_LIBRARY", "/claim/claimFormList.do" );
      systemMenu.addSubMenuOption( formLibrary );
    }

    if ( userInfo.isClientAdmin() || userInfo.isStandardClientAdmin() )
    {
      if ( !sysInfo.isSalesMaker() )
      {
        MenuOption formLibrary = new MenuOption( "FORUM_LIBRARY", "/forum/forumTopicList.do" );
        systemMenu.addSubMenuOption( formLibrary );
      }
    }

    if ( userInfo.isClientAdmin() || userInfo.isStandardClientAdmin() || userInfo.isStandardClientAdminPax() || userInfo.isStandardClientAdminPromo() )
    {
      MenuOption hierarchyManagement = new MenuOption( "HIERARCHY_MGT", "/hierarchy/hierarchyListDisplay.do" );
      systemMenu.addSubMenuOption( hierarchyManagement );
    }

    if ( userInfo.isClientAdmin() || userInfo.isStandardClientAdmin() || userInfo.isStandardClientAdminPax() )
    {
      MenuOption participantCharacteristics = new MenuOption( "PAX_CHARACTERISTICS", "/participant/characteristicListDisplayUser.do?method=displayList" );
      systemMenu.addSubMenuOption( participantCharacteristics );
    }

    if ( userInfo.isClientAdmin() || userInfo.isStandardClientAdmin() || userInfo.isStandardClientAdminPax() || userInfo.isStandardClientAdminPromo() )
    {
      MenuOption processes = new MenuOption( "PROCESSES", "/process/processList.do" );
      systemMenu.addSubMenuOption( processes );
    }

    if ( sysInfo.hasProductClaims() && ( userInfo.isClientAdmin() || userInfo.isStandardClientAdmin() || userInfo.isStandardClientAdminPromo() ) )
    {
      MenuOption productCategories = new MenuOption( "PRODUCT_CATEGORIES", "/product/productCategoryListDisplay.do" );
      systemMenu.addSubMenuOption( productCategories );

      MenuOption productLibrary = new MenuOption( "PRODUCT_LIB", "/product/productLibrary.do" );
      systemMenu.addSubMenuOption( productLibrary );
    }

    if ( sysInfo.hasQuizzes() && ( userInfo.isClientAdmin() || userInfo.isStandardClientAdmin() || userInfo.isStandardClientAdminPromo() ) )
    {
      MenuOption quizLibrary = new MenuOption( "QUIZ_LIB", "/quiz/quizFormListDisplay.do" );
      systemMenu.addSubMenuOption( quizLibrary );
    }

    return systemMenu.isEmpty() ? null : systemMenu;
  }

  /**
   * Builds the Admin Help submenu for a Pax > Admin menu
   *
   * @return admin help menu
   */
  private MenuOption buildAdminHelpMenu( UserInfo userInfo, SystemInfo sysInfo )
  {
    MenuOption helpMenu = new MenuOption( "ADMIN_HELP", null );

    MenuOption contactSupport = new MenuOption( "CONTACT_SUPPORT", "/contactUs.do?method=view&isFullPage=true" );
    helpMenu.addSubMenuOption( contactSupport );

    return helpMenu;
  }

  private boolean checkIfShowSubmitClaim( Promotion promotion, Participant participant )
  {
    // Following conditions should hold 'true' to show submit claim menu item
    // 1. Promotion should be claimable by participant
    // 2. If promotion is type 'recognition' or 'productclaim', then online flag should be 'true'
    // 3. If PURL enabled and promotion is type 'recognition' and 'isIncludePurl', the participant
    // should be node.owner
    if ( promotion.isRecognitionPromotion() || promotion.isProductClaimPromotion() )
    {
      if ( !promotion.isOnlineEntry() )
      {
        return false;
      }
    }
    
    // Client customizations for WIP #62128 starts
    if ( promotion.isLive() && promotion.isRecognitionPromotion() && ( (RecognitionPromotion)promotion ).isCheersPromotion() )
    {
      return true;
    }
    // Client customizations for WIP #62128 end

    return promotionService.isPromotionClaimableByParticipant( promotion, participant );
  }

  private boolean checkIfShowSubmitClaim( Promotion promotion, Participant participant, boolean flag )
  {
    // Following conditions should hold 'true' to show submit claim menu item
    // 1. Promotion should be claimable by participant
    // 2. If promotion is type 'recognition' or 'productclaim', then online flag should be 'true'
    boolean showSubmitClaim = promotionService.isPromotionClaimableByParticipant( promotion, participant, flag );
    if ( showSubmitClaim && ( promotion.isRecognitionPromotion() || promotion.isProductClaimPromotion() ) )
    {
      showSubmitClaim = promotion.isOnlineEntry();
    }
    return showSubmitClaim;
  }

  private boolean isPartnerAudience( Promotion promotion, Participant participant )
  {
    boolean showSubmitClaim = false;
    if ( promotion.isGoalQuestOrChallengePointPromotion() )
    {
      promotion = BaseAssociationRequest.initializeAndUnproxy( promotion );
      GoalQuestPromotion gqPromo = (GoalQuestPromotion)promotion;

      if ( gqPromo.isPartnersEnabled() )
      {
        showSubmitClaim = audienceService.isUserInParticipantPartnerAudiences( participant, promotion );
      }
    }
    return showSubmitClaim;
  }

  private boolean checkIfShowPromoRules( Promotion promotion, Participant participant )
  {
    boolean showPromoRules = false;
    String timeZoneID = UserManager.getUser().getTimeZoneId();

    if ( promotion.isGoalQuestPromotion() )
    {
      if ( !showPromoRules && promotion.isWebRulesActive() && ( promotion.isComplete() || promotion.isLive() || promotion.isExpired() )
          && DateUtils.isDateBetween( new Date(), promotion.getWebRulesStartDate(), promotion.getWebRulesEndDate(), timeZoneID ) )
      {
        GoalQuestPromotion gqPromo = (GoalQuestPromotion)promotion;
        if ( promotion.isWebRulesActive() && ( promotion.isComplete() || promotion.isLive() || promotion.isExpired() )
            && DateUtils.isDateBetween( new Date(), promotion.getWebRulesStartDate(), promotion.getWebRulesEndDate(), timeZoneID )
            && audienceService.isParticipantInWebRulesAudience( promotion, participant ) )
        {
          showPromoRules = true;
        }

        if ( !showPromoRules && gqPromo.getPartnerAudienceType() != null )
        {
          if ( gqPromo.getPartnerWebRulesAudienceType() == null )
          {
            return false;
          }
          AssociationRequestCollection ascReqColl = new AssociationRequestCollection();
          PromotionAssociationRequest promoAscReq = new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_AUDIENCES );
          ascReqColl.add( promoAscReq );
          if ( gqPromo.getPartnerWebRulesAudienceType().getCode().equals( WebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) )
          {
            showPromoRules = true;
          }
          else if ( gqPromo.getPartnerWebRulesAudienceType().getCode().equals( WebRulesAudienceType.CREATE_AUDIENCE_CODE ) )
          {
            // web rules has own audiences set
            showPromoRules = audienceService.isUserInPromotionAudiences( participant, promotion.getPromotionWebRulesAudiences() );
          }
          else if ( gqPromo.getPartnerWebRulesAudienceType().getCode().equals( WebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_AND_SECONDARY_CODE ) )
          {
            showPromoRules = audienceService.isUserInPromotionPartnerAudiences( participant, promotionService.getPromotionByIdWithAssociations( gqPromo.getId(), ascReqColl ).getPartnerAudiences() );
          }
        }

        if ( !showPromoRules && gqPromo.getManagerWebRulesAudienceType() != null )
        {
          if ( gqPromo.getManagerWebRulesAudienceType() == null )
          {
            return false;
          }
          AssociationRequestCollection ascReqColl = new AssociationRequestCollection();
          PromotionAssociationRequest promoAscReq = new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_MANAGERS );
          ascReqColl.add( promoAscReq );
          if ( gqPromo.getManagerWebRulesAudienceType().getCode().equals( ManagerWebRulesAudienceType.ALL_ACTIVE_PAX_CODE ) )
          {
            showPromoRules = true;
          }
          else if ( gqPromo.getManagerWebRulesAudienceType().getCode().equals( ManagerWebRulesAudienceType.ALL_ELIGIBLE_PRIMARY_AND_SECONDARY_CODE ) )
          {
            // web rules has own audiences set
            showPromoRules = audienceService.isUserInPromotionAudiences( participant, promotion.getPromotionWebRulesAudiences() );
          }
          else if ( gqPromo.getManagerWebRulesAudienceType().getCode().equals( ManagerWebRulesAudienceType.CREATE_AUDIENCE_CODE ) )
          {
            showPromoRules = audienceService
                .isUserInPromotionManagerAudiences( participant,
                                                    ( (GoalQuestPromotion)promotionService.getPromotionByIdWithAssociations( gqPromo.getId(), ascReqColl ) ).getPromotionManagerWebRulesAudience() );
          }
        }
      }
    }
    else
    {
      if ( promotion.isWebRulesActive() && ( promotion.isComplete() || promotion.isLive() || promotion.isExpired() )
          && DateUtils.isDateBetween( new Date(), promotion.getWebRulesStartDate(), promotion.getWebRulesEndDate(), timeZoneID )
          && audienceService.isParticipantInWebRulesAudience( promotion, participant ) )
      {
        showPromoRules = true;
      }
    }

    return showPromoRules;
  }

  public boolean checkIfReceivable( Promotion promotion, Participant participant )
  {
    String timeZoneID = UserManager.getUser().getTimeZoneId();
    if ( promotion.isLive() && DateUtils.isDateBetween( new Date(), promotion.getSubmissionStartDate(), promotion.getSubmissionEndDate(), timeZoneID )
        && audienceService.isParticipantInSecondaryAudience( promotion, participant, null ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Builds the Participant menu for Users
   *
   * @return participantsMenu
   */
  private Menu buildParticipantsMenuTab( UserInfo userInfo, SystemInfo sysInfo )
  {
    Menu participantsMenu = new Menu( "home.navmenu.user.participants", "PARTICIPANTS", null, false );

    if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isViewParticipants() )
    {
      if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() )
      {
        MenuOption addParticipant = new MenuOption( "ADD_PAX", "/participant/participantDisplay.do" );
        participantsMenu.addMenuOption( addParticipant );
      }
      String returnUrl = "/participant/participantDisplay.do?method=display";

      MenuOption searchPax = new MenuOption( "SEARCH_PAX", "/participant/listBuilderPaxDisplay.do?&audienceMembersLookupReturnUrl=" + returnUrl + "&singleResult=true" );

      participantsMenu.addMenuOption( searchPax );

    }
    return participantsMenu.isEmpty() ? null : participantsMenu;
  }

  /**
   * Builds the Promotions menu for a User
   *
   * @return promotionsMenu
   */
  private Menu buildPromotionsMenuTab( UserInfo userInfo, SystemInfo sysInfo )
  {
    Menu promotionsMenu = new Menu( "home.navmenu.user.promotions", "PROMOTIONS", null, false );

    if ( userInfo.isCsr() || userInfo.isCsrMgr() || userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() || userInfo.isStandardClientAdmin()
        || userInfo.isStandardClientAdminPromo() )
    {
      if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() )
      {
        MenuOption addNewPromotion = new MenuOption( "ADD_NEW_PROMOTION", "/promotion/startPromotionWizard.do" );
        promotionsMenu.addMenuOption( addNewPromotion );
      }

      if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() )
      {
        MenuOption viewAwardGenList = new MenuOption( "VIEW_AWARD_FILE_GEN", "/admin/awardGeneratorListDisplay.do" );
        promotionsMenu.addMenuOption( viewAwardGenList );

        if ( sysInfo.hasChallengepoint() )
        {
          MenuOption challengepointCalc = new MenuOption( "CHALLENGEPOINT_CALC", "/challengepoint/awardListDisplay.do" );
          promotionsMenu.addMenuOption( challengepointCalc );
        }

        if ( sysInfo.hasGoalQuest() )
        {
          MenuOption goalQuest = new MenuOption( "GOAL_QUEST", "/goalquest/goalQuestListDisplay.do" );
          promotionsMenu.addMenuOption( goalQuest );
        }
      }

      if ( userInfo.isBIAdmin() || userInfo.isClientAdmin() )
      {
        MenuOption instantPollLibrary = new MenuOption( "INSTANT_POLLS", "/instantPollsList.do" );
        promotionsMenu.addMenuOption( instantPollLibrary );
      }

      if ( userInfo.isBIAdmin() && sysVarService.getPropertyByName( SystemVariableService.AWARDBANQ_CONVERTCERT_IS_USED ).getBooleanVal() )
      {
        MenuOption otsAdministration = new MenuOption( "OTS_ADMINISTRATION", "/ots/otsAdministration.do" );
        promotionsMenu.addMenuOption( otsAdministration );
      }

      if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() )
      {
        if ( sysInfo.hasProductClaims() )
        {
          MenuOption stackRank = new MenuOption( "STACK_RANK", "/promotion/stackRankListDisplay.do" );
          promotionsMenu.addMenuOption( stackRank );
        }
      }

      if ( userInfo.isBIAdmin() )
      {
        if ( sysInfo.hasSsi() )
        {
          if ( promotionService.isSSILivePromotionAvailable() )
          {
            MenuOption adminSSI = new MenuOption( "SSI_ADMINISTRATION", "/promotion/ssiContestSearchResults.do?method=searchContest" );
            promotionsMenu.addMenuOption( adminSSI );
          }
        }
      }

      if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() )
      {
        if ( sysInfo.hasThrowdown() )
        {
          MenuOption throwdownCalc = new MenuOption( "THROWDOWN_CALC", "/promotionThrowDown/throwdownListDisplay.do" );
          promotionsMenu.addMenuOption( throwdownCalc );
        }
      }

      if ( sysInfo.hasBadges() )
      {
        MenuOption viewBadgeList = new MenuOption( "VIEW_BADGE_LIST", "/promotion/badgeList.do" );
        promotionsMenu.addMenuOption( viewBadgeList );
      }

      if ( sysInfo.hasLeaderBoard() )
      {
        MenuOption viewLeaderBoardList = new MenuOption( "VIEW_LEADERBOARD_LIST", "/leaderBoardsList.do" );
        promotionsMenu.addMenuOption( viewLeaderBoardList );
      }

      if ( sysInfo.hasQuizzes() )
      {
        MenuOption stackRank = new MenuOption( "VIEW_PARTICIPANT_DIY_QUIZ", "/promotion/diyQuizList.do" );
        promotionsMenu.addMenuOption( stackRank );
      }

      MenuOption viewPromotionsList = new MenuOption( "VIEW_PROMOTIONS_LIST", "/promotion/promotionListDisplay.do" );
      promotionsMenu.addMenuOption( viewPromotionsList );

      MenuOption viewPromotionsApproversList = new MenuOption( "VIEW_NOMINATION_APPROVALS_LIST", "/promotion/nominationApprovalsListDisplay.do" );
      promotionsMenu.addMenuOption( viewPromotionsApproversList );

      MenuOption viewSweepstakes = new MenuOption( "VIEW_SWEEPSTAKES", "/promotion/promotionSweepstakesListDisplay.do" );
      promotionsMenu.addMenuOption( viewSweepstakes );
    }

    return promotionsMenu.isEmpty() ? null : promotionsMenu;
  }

  /**
   * Builds the Budgets menu for Users
   *
   * @return budgetsMenu
   */
  private Menu buildBudgetsMenuTab( UserInfo userInfo, SystemInfo sysInfo )
  {
    Menu budgetsMenu = new Menu( "home.navmenu.user.budgets", "BUDGETS", null, false );

    if ( userInfo.isCsr() || userInfo.isCsrMgr() || userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() || userInfo.isStandardClientAdmin()
        || userInfo.isStandardClientAdminPromo() )
    {
      if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() )
      {
        MenuOption addNewBudget = new MenuOption( "ADD_NEW_BUDGET_MASTER", "/admin/budgetMasterMaintainDisplay.do?method=prepareCreate" );
        budgetsMenu.addMenuOption( addNewBudget );
      }

      MenuOption viewBudgetMasterList = new MenuOption( "VIEW_BUDGET_MASTER_LIST", "/admin/budgetMasterListDisplay.do" );
      budgetsMenu.addMenuOption( viewBudgetMasterList );
    }

    if ( userInfo.isBIAdmin() )
    {
      MenuOption inactiveBudget = new MenuOption( "EXTRACT_INACTIVE_BUDGETS", "/admin/extractInactiveBudgetsDisplay.do?method=display" );
      budgetsMenu.addMenuOption( inactiveBudget );
    }

    return budgetsMenu.isEmpty() ? null : budgetsMenu;
  }

  /**
   * Builds the Communications menu
   *
   * @return communicationsMenu
   */
  private Menu buildCommunicationsMenuTab( UserInfo userInfo, SystemInfo sysInfo )
  {
    if ( !userInfo.isBIAdmin() && !userInfo.isProjMgr() && !userInfo.isClientAdmin() && !userInfo.isStandardClientAdmin() && !userInfo.isStandardClientAdminPax()
        && !userInfo.isStandardClientAdminComm() )
    {
      return null;
    }

    Menu communicationsMenu = new Menu( "home.navmenu.user.communications", "COMMUNICATIONS", null, false );

    MenuOption diyCommunication = new MenuOption( "DIY_COMMUNICATION", "/admin/diyCommunication.do" );
    communicationsMenu.addMenuOption( diyCommunication );

    MenuOption sendAdHocMessage = new MenuOption( "SEND_AD_HOC_MESSAGE", "/admin/sendAdHocMessageDisplay.do?method=prepareSend" );
    communicationsMenu.addMenuOption( sendAdHocMessage );

    MenuOption messageLibrary = new MenuOption( "VIEW_MESSAGE_LIBRARY", "/admin/messageList.do" );
    communicationsMenu.addMenuOption( messageLibrary );

    return communicationsMenu;
  }

  /**
   * Builds the System menu for Users
   *
   * @return systemMenu
   */
  private Menu buildSystemMenuTab( UserInfo userInfo, SystemInfo sysInfo )
  {
    Menu systemMenu = new Menu( "home.navmenu.user.system", "SYSTEM", null, false );

    if ( userInfo.isProjMgr() )
    {
      MenuOption acls = new MenuOption( "ACLS", "/admin/aclListDisplay.do" );
      systemMenu.addMenuOption( acls );
    }

    if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() )
    {
      MenuOption participantActiviation = new MenuOption( "ACTIVATION_SETUP", "/participant/activation.do?method=display" );
      systemMenu.addMenuOption( participantActiviation );
    }

    if ( userInfo.isBIAdmin() || userInfo.isProcessTeam() || userInfo.isAnyClientAdminButReports() || userInfo.isProjMgr() )
    {
      MenuOption audienceDef = new MenuOption( "AUDIENCE_DEFINITIONS", "/participant/audienceListDisplay.do" );
      systemMenu.addMenuOption( audienceDef );
    }

    if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() || userInfo.isStandardClientAdmin() || userInfo.isStandardClientAdminPromo() )
    {
      if ( !sysInfo.isSalesMaker() )
      {
        MenuOption calculatorLibrary = new MenuOption( "CALCULATOR_LIBRARY", "/calculator/calculatorLibraryListDisplay.do" );
        systemMenu.addMenuOption( calculatorLibrary );
      }
    }

    if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() || userInfo.isStandardClientAdmin() || userInfo.isStandardClientAdminPax()
        || userInfo.isStandardClientAdminComm() || userInfo.isTranslator() )
    {
      MenuOption contentManager = new MenuOption( "CONTENT_MANAGER", "/admin/cmSso.do?method=sso", "_blank" );
      systemMenu.addMenuOption( contentManager );
    }

    if ( userInfo.isBIAdmin() || userInfo.isProjMgr() )
    {
      MenuOption counrtyAndSuppliers = new MenuOption( "COUNTRIES_AND_SUPPLIERS", "/admin/countryListDisplay.do" );
      systemMenu.addMenuOption( counrtyAndSuppliers );
    }

    if ( userInfo.isBIAdmin() || userInfo.isProjMgr() )
    {
      MenuOption currency = new MenuOption( "CURRENCY", "/admin/currencyList.do?method=display" );
      systemMenu.addMenuOption( currency );
    }

    if ( userInfo.isBIAdmin() )
    {
      MenuOption displayCache = new MenuOption( "DISPLAY_CACHE", "/sysadmin/cacheStatsDisplay.do", "_blank" );
      systemMenu.addMenuOption( displayCache );

      MenuOption displayPerformance = new MenuOption( "DISPLAY_PERFORMANCE", "/sysadmin/performanceStatsDisplay.do?d-49653-s=3&d-49653-o=1&d-49653-p=1", "_blank" );
      systemMenu.addMenuOption( displayPerformance );
    }

    if ( userInfo.isCsrMgr() || userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() )
    {
      MenuOption employersSetup = new MenuOption( "EMPLOYERS_SETUP", "/admin/employerListDisplay.do" );
      systemMenu.addMenuOption( employersSetup );
    }

    if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() )
    {
      MenuOption fileLoads = new MenuOption( "FILE_LOADS", "/admin/displayImportFileList.do" );
      systemMenu.addMenuOption( fileLoads );
    }

    if ( userInfo.isBIAdmin() || userInfo.isProjMgr() )
    {
      MenuOption filterPageSetup = new MenuOption( "FILTER_PAGE_SETUP", "/filterSetup.do?method=display" );
      systemMenu.addMenuOption( filterPageSetup );
    }

    if ( userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() )
    {
      MenuOption formLibrary = new MenuOption( "FORM_LIBRARY", "/claim/claimFormList.do" );
      systemMenu.addMenuOption( formLibrary );
    }

    if ( userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() )
    {
      if ( !sysInfo.isSalesMaker() )
      {
        MenuOption forumLibrary = new MenuOption( "FORUM_LIBRARY", "/forum/forumTopicList.do" );
        systemMenu.addMenuOption( forumLibrary );
      }
    }

    if ( userInfo.isBIAdmin() )
    {
      if ( !sysInfo.isSalesMaker() )
      {
        MenuOption giftCodeLookup = new MenuOption( "GIFT_CODE_LOOKUP", "/admin/giftCodeLookupStart.do" );
        systemMenu.addMenuOption( giftCodeLookup );
      }
    }

    if ( userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() || userInfo.isStandardClientAdmin() || userInfo.isStandardClientAdminPax() || userInfo.isStandardClientAdminPromo() )
    {
      MenuOption hierarchyManagement = new MenuOption( "HIERARCHY_MANAGEMENT", "/hierarchy/hierarchyListDisplay.do" );
      systemMenu.addMenuOption( hierarchyManagement );
    }

    if ( userInfo.isBIAdmin() || userInfo.isProjMgr() )
    {
      MenuOption nodeTypes = new MenuOption( "HIERARCHY_NODE_TYPES", "/hierarchy/nodeTypeListDisplay.do" );
      systemMenu.addMenuOption( nodeTypes );
    }

    if ( userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() )
    {
      MenuOption participantCharacteristics = new MenuOption( "PAX_CHARACTERISTICS", "/participant/characteristicListDisplayUser.do?method=displayList" );
      systemMenu.addMenuOption( participantCharacteristics );
    }

    if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() || userInfo.isStandardClientAdminPromo() || userInfo.isStandardClientAdminPax()
        || userInfo.isStandardClientAdmin() )
    {
      MenuOption processes = new MenuOption( "PROCESSES", "/process/processList.do" );
      systemMenu.addMenuOption( processes );
    }

    if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() || userInfo.isStandardClientAdminPromo() || userInfo.isStandardClientAdminPax()
        || userInfo.isStandardClientAdmin() )
    {
      MenuOption productCategories = new MenuOption( "PRODUCT_CATEGORIES", "/product/productCategoryListDisplay.do" );
      systemMenu.addMenuOption( productCategories );

      MenuOption productLibrary = new MenuOption( "PRODUCT_LIBRARY", "/product/productLibrary.do" );
      systemMenu.addMenuOption( productLibrary );
    }

    if ( sysInfo.hasProductClaims() && ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() ) )
    {
      MenuOption productCharacteristics = new MenuOption( "PRODUCT_CHARACTERISTICS", "/product/characteristicListDisplayProduct.do?method=displayList" );
      systemMenu.addMenuOption( productCharacteristics );
    }

    if ( sysInfo.hasQuizzes()
        && ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() || userInfo.isClientAdmin() || userInfo.isStandardClientAdmin() || userInfo.isStandardClientAdminPromo() ) )
    {
      MenuOption quizLibrary = new MenuOption( "QUIZ_LIBRARY", "/quiz/quizFormListDisplay.do" );
      systemMenu.addMenuOption( quizLibrary );
    }

    if ( userInfo.isBIAdmin() && ( sysInfo.hasGoalQuest() || sysInfo.hasRecognition() ) )
    {
      if ( !sysInfo.isSalesMaker() )
      {
        MenuOption replaceGiftCode = new MenuOption( "REPLACE_GIFT_CODE", "/admin/replaceGiftCodeDisplay.do" );
        systemMenu.addMenuOption( replaceGiftCode );
      }
    }

    if ( userInfo.isBIAdmin() || userInfo.isProjMgr() )
    {
      MenuOption option = new MenuOption( "RESEND_WELCOME_EMAIL", "/admin/resendWelcomeEmail.do?method=display" );
      systemMenu.addMenuOption( option );
    }

    if ( userInfo.isProjMgr() )
    {
      MenuOption rolesAndACLs = new MenuOption( "ROLES", "/admin/roleListDisplay.do" );
      systemMenu.addMenuOption( rolesAndACLs );
    }

    if ( userInfo.isBIAdmin() || userInfo.isProjMgr() )
    {
      if ( sysInfo.isSelfEnroll() )
      {
        MenuOption formLibrary = new MenuOption( "SELF_ENROLLMENT", PageConstants.SELF_ENROLLMENT_DISPLAY );
        systemMenu.addMenuOption( formLibrary );
      }
    }

    if ( ( sysInfo.hasGoalQuest() || sysInfo.hasChallengepoint() || sysInfo.hasSurveys() ) && ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() ) )
    {
      MenuOption surveyLibrary = new MenuOption( "SURVEY_LIBRARY", "/surveyFormListDisplay.do" );
      systemMenu.addMenuOption( surveyLibrary );
    }

    if ( userInfo.isProjMgr() )
    {
      MenuOption systemVariables = new MenuOption( "SYSTEM_VARIABLES", "/admin/systemVariableListDisplay.do" );
      systemMenu.addMenuOption( systemVariables );
    }

    if ( userInfo.isBIAdmin() || userInfo.isProjMgr() )
    {
      MenuOption tilePageSetup = new MenuOption( "TILE_PAGE_SETUP", "/tileSetup.do?method=display" );
      systemMenu.addMenuOption( tilePageSetup );
    }

    if ( userInfo.isCsrMgr() || userInfo.isBIAdmin() || userInfo.isProjMgr() )
    {
      MenuOption users = new MenuOption( "USERS", "/userList.do" );
      systemMenu.addMenuOption( users );
    }

    if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() )
    {
      MenuOption welcomeMessage = new MenuOption( "WELCOME_MESSAGE", "/admin/welcomeMessageList.do" );
      systemMenu.addMenuOption( welcomeMessage );
    }

    if ( userInfo.isBIAdmin() )
    {
      MenuOption welcomeMessage = new MenuOption( "ELASTIC_INDEX_ADMIN", "/admin/elasticIndexAdminView.do" );
      systemMenu.addMenuOption( welcomeMessage );
    }

    if ( userInfo.isBIAdmin() && NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      MenuOption welcomeMessage = new MenuOption( "EVENT_ADMIN", "/admin/eventAdminView.do" );
      systemMenu.addMenuOption( welcomeMessage );
    }
    return systemMenu.isEmpty() ? null : systemMenu;
  }

  /**
   * Builds the User Help menu (available for all users)
   *
   * @return helpMenu
   */
  private Menu buildUserHelpMenuTab( UserInfo userInfo, SystemInfo sysInfo )
  {
    Menu helpMenu = new Menu( "home.navmenu.user.help", "HELP", null, false );

    MenuOption contactSupport = new MenuOption( "CONTACT_SUPPORT", "/contactUs.do?method=view&isFullPage=true" );
    helpMenu.addMenuOption( contactSupport );

    MenuOption systemDocumentation = new MenuOption( "SYSTEM_DOCUMENTATION", "/docs/Platform_Administration_Guide.pdf", "_blank" );
    helpMenu.addMenuOption( systemDocumentation );

    MenuOption changeAdmin = new MenuOption( "VERSION_INFO", "/sysadmin/version.do", "_blank" );
    helpMenu.addMenuOption( changeAdmin );

    return helpMenu;
  }

  /**
   * Builds the User Reports menu for Users
   *
   * @return reportsMenu
   */
  private Menu buildUserReportsMenuTab( UserInfo userInfo, SystemInfo sysInfo )
  {
    if ( !userInfo.isProcessTeam() && !userInfo.isBIAdmin() && !userInfo.isProjMgr() && !userInfo.isViewReports() )
    {
      return null;
    }

    Menu reportsMenu = new Menu( "home.navmenu.user.reports", "REPORTS", null, false );

    MenuOption viewDownloads = new MenuOption( "DOWNLOAD_EXTRACTS", "/admin/reportDownload.do?method=fetchAlrts" );
    reportsMenu.addMenuOption( viewDownloads );

    if ( userInfo.isProcessTeam() || userInfo.isBIAdmin() || userInfo.isProjMgr() )
    {
      MenuOption manageReports = new MenuOption( "MANAGE_REPORTS", "/reports/manageReports.do" );
      reportsMenu.addMenuOption( manageReports );
    }

    MenuOption allReports = new MenuOption( "REPORTS", "/reports/allReports.do" );
    reportsMenu.addMenuOption( allReports );

    return reportsMenu;
  }

  public List getReportsAccessibleToUser()
  {
    UserInfo userInfo = new UserInfo( this );
    SystemInfo sysInfo = new SystemInfo( this );
    List reports = new ArrayList( reportsDAO.getReports() );
    return reports;
  }

  public List getAllReports()
  {
    UserInfo userInfo = new UserInfo( this );
    SystemInfo sysInfo = new SystemInfo( this );
    List reports = new ArrayList( reportsDAO.getAllReports() );

    return reports;
  }

  public List<DailyTipValueBean> getDailyTips()
  {
    List<DailyTipValueBean> tips = null;

    ContentReader contentReader = ContentReaderManager.getContentReader();
    List allDailyTips = (List)contentReader.getContent( "home.dailyTips" );
    tips = new ArrayList<DailyTipValueBean>();
    if ( allDailyTips == null )
    {
      return tips;
    }
    for ( int i = 0; i < allDailyTips.size(); i++ )
    {
      Content content = (Content)allDailyTips.get( i );
      String text = (String)content.getContentDataMap().get( "TEXT" );
      DailyTipValueBean dailyTip = new DailyTipValueBean();
      dailyTip.setText( text );
      dailyTip.setCode( "home.dailyTips" );
      dailyTip.setId( new Long( i + 1 ) );
      tips.add( dailyTip );
    }

    return tips;
  }

  /*
   * public String getWellnessEncodedUrl() { StringBuilder wellnessUrl = new StringBuilder();
   * wellnessUrl.append( sysVarService.getPropertyByNameAndEnvironment(
   * SystemVariableService.WELLNESS_URL_PREFIX ).getStringVal() ); wellnessUrl.append( "?auth=" );
   * wellnessUrl.append( getWellnessEncodedKey() ); return wellnessUrl.toString(); }
   */

  private String getWellnessEncodedKey()
  {
    Long userId = UserManager.getUserId();
    Map paramMap = new HashMap();
    String encodedKey = "";

    Participant pax = participantService.getParticipantById( userId );
    String path = sysVarService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    String relativeUrlToHomePage = getUrlPath( path ) + "/homePage.do";
    String relativeUrlToKeepAlive = getUrlPath( path ) + "/wellnessKeepAlive.do";

    paramMap.put( "firstName", pax.getFirstName() );
    paramMap.put( "lastName", pax.getLastName() );
    paramMap.put( "username", pax.getUserName() );

    paramMap.put( "emailAddresses", getAllEmailAddresses( pax ) );
    paramMap.put( "phones", getAllPhoneNumbers( pax ) );
    paramMap.put( "participantId", pax.getId() );
    paramMap.put( "css", getFullPathToSkin() );
    paramMap.put( "tid", new Long( System.currentTimeMillis() ) );

    paramMap.put( "G4_domain", getDomainName( path.toLowerCase() ) );
    paramMap.put( "G4_homeUrl", relativeUrlToHomePage );
    paramMap.put( "G4_keepAlive", relativeUrlToKeepAlive );
    paramMap.put( "str_pax_id", pax.getId().toString() );

    boolean encrypt = true;
    boolean compress = false;
    boolean get = true;

    String key = com.biperf.clientstate.ClientStateUtils.deconstructMap( paramMap, encrypt, compress, get );

    try
    {
      encodedKey = URLEncoder.encode( key, "UTF-8" );
    }
    catch( Exception e )
    {
      log.error( e.getMessage(), e );
    }
    return encodedKey;
  }

  private String getDomainName( String path )
  {
    String serverName = null;
    try
    {
      URL url = new URL( path );
      serverName = url.getHost();
    }
    catch( MalformedURLException e )
    {
      log.error( "Malformed URL [" + path + "]", e );
    }
    return serverName;
  }

  private String getUrlPath( String path )
  {
    String pathName = null;

    try
    {
      URL url = new URL( path );
      pathName = url.getPath();
    }
    catch( MalformedURLException e )
    {
      log.error( "Malformed URL [" + path + "]", e );
    }

    return pathName;
  }

  private String[] getAllEmailAddresses( Participant pax )
  {
    String[] primaryEmail = new String[pax.getUserEmailAddresses().size()];

    Set<UserEmailAddress> allEmailAddresses = pax.getUserEmailAddresses();
    int i = 0;
    for ( UserEmailAddress email : allEmailAddresses )
    {
      if ( email.isPrimary() )
      {
        primaryEmail[0] = email.getEmailAddr();
      }
      else
      {
        i++;
        primaryEmail[i] = email.getEmailAddr();
      }
    }
    return primaryEmail;
  }

  private String[] getAllPhoneNumbers( Participant pax )
  {
    String[] primaryPhone = new String[pax.getUserPhones().size()];

    Set<UserPhone> allPhoneNumbers = pax.getUserPhones();
    int i = 0;
    for ( UserPhone phone : allPhoneNumbers )
    {
      if ( phone.isPrimary() )
      {
        primaryPhone[0] = phone.getPhoneNbr();
      }
      else
      {
        i++;
        primaryPhone[i] = phone.getPhoneNbr();
      }
    }
    return primaryPhone;
  }

  private String getFullPathToSkin()
  {
    String designTheme = designThemeService.getDefaultDesignTheme();

    String domain = sysVarService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    StringBuffer styleSheetPath = new StringBuffer();
    styleSheetPath.append( domain );
    styleSheetPath.append( DesignThemeService.SKINS_FOLDER );
    styleSheetPath.append( designTheme );
    styleSheetPath.append( DesignThemeService.STYLES_FOLDER );

    return styleSheetPath.toString();

  }

  public boolean checkShowShopORTravel()
  {
    AwardBanqResponseView awardBanqResponseView = new AwardBanqResponseView();
    Participant participant = participantService.getParticipantById( UserManager.getUserId() );
    if ( participant.isParticipant() )
    {
      if ( participant.getAwardBanqNumber() != null )
      {
        awardBanqResponseView = getAwardBanQService().buildAwardBanqResponseView( participant.getId(), participant.getAwardBanqNumber() );
        String balanceAvailable = awardBanqResponseView != null ? awardBanqResponseView.getBalanceAvailable() : null;
        Long balance = org.apache.commons.lang3.StringUtils.isNotBlank( balanceAvailable ) ? Long.valueOf( balanceAvailable ) : -1L;

        // Check the balance
        // Show the Shop tile/Account Statement if PAX has non-zero balance
        if ( balance > new Long( 0 ) )
        {
          return true;
        }
        if ( balance.equals( new Long( 0 ) ) )
        {

          // When the user locale is fr_CA, date pattern will be yyyy-MM-dd but not MM/dd/yyyy
          // So the startDate and endDate for the getAccountSummaryByParticipantIdAndDateRange is
          // passing as empty values and the service is throwing the exception.
          // Bug #72690 Start
          DateFormat dateFormat = new SimpleDateFormat( UserManager.getUserDatePattern() );
          // Bug #72690 End

          // get current date with Calendar()
          Calendar cal = Calendar.getInstance();
          String endDate = dateFormat.format( cal.getTime() );

          // get Two years ago date with Calendar()
          cal.add( Calendar.YEAR, -2 );
          Date twoYearsAgo = cal.getTime();
          String startDate = dateFormat.format( twoYearsAgo.getTime() );

          // Get the Account Summary from AwardBanQ Services
          AccountSummary accountSummary;
          try
          {
            accountSummary = getAwardBanQService().getAccountSummaryByParticipantIdAndDateRange( participant.getId(), DateUtils.toDate( startDate ), DateUtils.toDate( endDate ) );

            // check for any transactions
            if ( accountSummary != null && accountSummary.getAccountTransactions() != null && accountSummary.getAccountTransactions().size() > 0 )
            {
              return true;
            }
            // to check the account status is in hold or not
            if ( accountSummary.getErrCode() == -81 )
            {
              return false;
            }
            
          }
          catch( ServiceErrorException e )
          {
            e.printStackTrace();
          }
        }
      }

      List<PromotionMenuBean> eligiblePromotions = (List<PromotionMenuBean>)buildEligiblePromoList( UserManager.getUser() );
      // If PAX tied to any of the points promotion as a eligible receiver then show the Shop tile
      for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
      {
        if ( promotionMenuBean.getPromotion().getAwardType() != null && promotionMenuBean.getPromotion().getAwardType().equals( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) )
            && promotionMenuBean.isCanReceive() )
        {
          if ( promotionMenuBean.getPromotion().isRecognitionPromotion() && ( (RecognitionPromotion)promotionMenuBean.getPromotion() ).isAwardActive() )
          {
            return true;
          }
          else if ( promotionMenuBean.getPromotion().isQuizPromotion() && ( (QuizPromotion)promotionMenuBean.getPromotion() ).isAwardActive() )
          {
            return true;
          }
          else if ( promotionMenuBean.getPromotion().isGoalQuestOrChallengePointPromotion() || promotionMenuBean.getPromotion().isThrowdownPromotion()
              || promotionMenuBean.getPromotion().isProductClaimPromotion() )
          {
            return true;
          }
        }
      }

      for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
      {
        if ( promotionMenuBean.isCanReceive() )
        {
          if ( promotionMenuBean.getPromotion().isNominationPromotion() && ( (NominationPromotion)promotionMenuBean.getPromotion() ).isAwardActive() )
          {
            AssociationRequestCollection arc = new AssociationRequestCollection();
            arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOMINATION_PROMOTION_LEVEL ) );

            NominationPromotion promotion = (NominationPromotion)promotionService.getPromotionByIdWithAssociations( promotionMenuBean.getPromotion().getId(), arc );
            for ( NominationPromotionLevel nominationPromotionLevel : promotion.getNominationLevels() )
            {

              if ( nominationPromotionLevel.getAwardPayoutType() != null && nominationPromotionLevel.getAwardPayoutType().isPointsAwardType() )
              {
                return true;
              }
            }
          }
        }
      }

      // If PAX is tied to a promotion as a eligible receiver where awards are not active and
      // manager discretionary award is enabled & award is points.
      for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
      {
        if ( promotionMenuBean.getPromotion().isRecognitionPromotion() )
        {
          if ( promotionMenuBean.getPromotion().getAwardType().equals( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) ) && promotionMenuBean.isCanReceive() )
          {
            if ( ! ( (RecognitionPromotion)promotionMenuBean.getPromotion() ).isAwardActive() )
            {
              if ( ( (RecognitionPromotion)promotionMenuBean.getPromotion() ).isAllowManagerAward() )
              {
                Promotion mgrAwardPromotion = promotionService.getPromotionById( ( (RecognitionPromotion)promotionMenuBean.getPromotion() ).getMgrAwardPromotionId() );
                if ( mgrAwardPromotion.getAwardType().equals( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) ) && ( (RecognitionPromotion)mgrAwardPromotion ).isAwardActive() )
                {
                  if ( checkIfReceivable( mgrAwardPromotion, participantService.getParticipantById( participant.getId() ) ) )
                  {
                    return true;
                  }
                }
              }
            }
          }
        }
      }

      // If PAX is tied to a promotion as a eligible receiver where awards are not active and
      // public recognition award is enabled & award is points.
      for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
      {
        if ( promotionMenuBean.getPromotion().isRecognitionPromotion() )
        {
          if ( promotionMenuBean.getPromotion().getAwardType().equals( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) ) && promotionMenuBean.isCanReceive() )
          {
            if ( ! ( (RecognitionPromotion)promotionMenuBean.getPromotion() ).isAwardActive() )
            {
              if ( ( (RecognitionPromotion)promotionMenuBean.getPromotion() ).isAllowPublicRecognition()
                  && ( (RecognitionPromotion)promotionMenuBean.getPromotion() ).isAllowPublicRecognitionPoints() )
              {
                return true;
              }
            }
          }
        }
      }

      // If PAX has tied to a promotion where awards are not active and Sweepstakes are enabled
      for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
      {
        if ( promotionMenuBean.getPromotion().isSweepstakesActive() )
        {
          if ( promotionMenuBean.getPromotion().isRecognitionPromotion() && ! ( (RecognitionPromotion)promotionMenuBean.getPromotion() ).isAwardActive() )
          {
            if ( promotionMenuBean.getPromotion().getAwardType().equals( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) ) )
            {
              if ( promotionMenuBean.getPromotion().getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.GIVERS_ONLY_CODE ) )
              {
                if ( promotionMenuBean.isCanSubmit() )
                {
                  return true;
                }
              }
              else
              {
                if ( promotionMenuBean.isCanReceive() )
                {
                  return true;
                }
              }
            }
          }
          if ( promotionMenuBean.getPromotion().isNominationPromotion() && ! ( (NominationPromotion)promotionMenuBean.getPromotion() ).isAwardActive() )
          {
            if ( promotionMenuBean.getPromotion().getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.NOMINATORS_ONLY_CODE ) )
            {
              if ( promotionMenuBean.isCanSubmit() )
              {
                return true;
              }
            }
            else
            {
              if ( promotionMenuBean.isCanReceive() )
              {
                return true;
              }
            }
          }
          if ( promotionMenuBean.getPromotion().isSurveyPromotion() )
          {
            if ( promotionMenuBean.getPromotion().getAwardType().equals( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) ) )
            {
              if ( promotionMenuBean.getPromotion().getSweepstakesWinnerEligibilityType().getCode().equals( SweepstakesWinnerEligibilityType.PAX_SELECTED_SURVEY_ONLY_CODE ) )
              {
                return true;
              }
            }
          }
        }
      }
      // If any SSI Contests with points
      if ( promotionService.checkIfAnyPointsContestsByPaxId( UserManager.getUserId() ) )
      {
        return true;
      }
    }
    return false;

  }

  public PaxGoalService getPaxGoalService()
  {
    return paxGoalService;
  }

  public void setPaxGoalService( PaxGoalService paxGoalService )
  {
    this.paxGoalService = paxGoalService;
  }

  public PlatformTransactionManager getTransactionManager()
  {
    return transactionManager;
  }

  public void setTransactionManager( PlatformTransactionManager transactionManager )
  {
    this.transactionManager = transactionManager;
  }

  public MerchLevelService getMerchLevelService()
  {
    return merchLevelService;
  }

  public void setMerchLevelService( MerchLevelService merchLevelService )
  {
    this.merchLevelService = merchLevelService;
  }

  public MerchOrderService getMerchOrderService()
  {
    return merchOrderService;
  }

  public void setMerchOrderService( MerchOrderService merchOrderService )
  {
    this.merchOrderService = merchOrderService;
  }

}

/**
 * UserInfo is a single-threaded access point for User/Pax information.
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
 * <td>Brian Repko</td>
 * <td>Dec 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 *
 */
class UserInfo
{
  private MainContentServiceImpl service;
  private AuthenticatedUser authUser;
  private User user;
  private Participant pax;
  private Boolean processTeam;
  private Boolean biAdmin;
  private Boolean projMgr;
  private Boolean viewReports;
  private Boolean viewParticipants;
  private Boolean hasProxies;
  private Boolean loginAs;

  UserInfo( MainContentServiceImpl service )
  {
    this.service = service;
    this.authUser = UserManager.getUser();
  }

  public Long getId()
  {
    return authUser.getUserId();
  }

  public boolean isParticipant()
  {
    return authUser.isParticipant();
  }

  public Participant getParticipant()
  {
    if ( !authUser.isParticipant() )
    {
      return null;
    }
    return pax != null ? pax : loadParticipant();
  }

  private Participant loadParticipant()
  {
    pax = service.participantService.getParticipantById( authUser.getUserId() );
    return pax;
  }

  public boolean isClientAdmin()
  {
    return false;
  }

  public boolean isLoginAs()
  {
    return loginAs != null ? loginAs.booleanValue() : loadRole( loginAs, AuthorizationService.ROLE_CODE_LOGIN_AS );
  }

  public boolean isStandardClientAdmin()
  {
    return false;
  }

  public boolean isTranslator()
  {
    return false;
  }

  public boolean isStandardClientAdminReports()
  {
    return false;
  }

  public boolean isStandardClientAdminPax()
  {
    return false;
  }

  public boolean isStandardClientAdminPromo()
  {
    return false;
  }

  public boolean isStandardClientAdminComm()
  {
    return false;
  }

  public boolean isCsr()
  {
    return false;
  }

  public boolean isCsrMgr()
  {
    return false;
  }

  public boolean isProcessTeam()
  {
    return processTeam != null ? processTeam.booleanValue() : loadRole( processTeam, AuthorizationService.ROLE_CODE_PROCESS_TEAM );
  }

  public boolean isBIAdmin()
  {
    return biAdmin != null ? biAdmin.booleanValue() : loadRole( biAdmin, AuthorizationService.ROLE_CODE_BI_ADMIN );
  }

  public boolean isProjMgr()
  {
    return projMgr != null ? projMgr.booleanValue() : loadRole( projMgr, AuthorizationService.ROLE_CODE_PROJ_MGR );
  }

  public boolean isViewReports()
  {
    return viewReports != null ? viewReports.booleanValue() : loadRole( viewReports, AuthorizationService.ROLE_CODE_VIEW_REPORTS );
  }

  public boolean isViewParticipants()
  {
    return viewParticipants != null ? viewParticipants.booleanValue() : loadRole( viewParticipants, AuthorizationService.ROLE_CODE_VIEW_PARTICIPANTS );
  }

  public boolean isAnyClientAdminButReports()
  {
    return isClientAdmin() || isStandardClientAdmin() || isStandardClientAdminPax() || isStandardClientAdminPromo() || isStandardClientAdminComm();
  }

  public boolean isManager()
  {
    return getUser().isManager();
  }

  public boolean isOwner()
  {
    return getUser().isOwner();
  }

  public boolean isSpecialReportAccess()
  {
    return false;
  }

  public boolean hasAdminMenuAccess()
  {
    return isViewParticipants();
  }

  public boolean hasProxies()
  {
    return hasProxies != null ? hasProxies.booleanValue() : loadHasProxies();
  }

  public boolean loadHasProxies()
  {
    List proxies = service.proxyService.getUsersByProxyUserId( authUser.getUserId() );
    hasProxies = new Boolean( !proxies.isEmpty() );
    return hasProxies.booleanValue();
  }

  public boolean approvesProductClaims()
  {
    if ( service.claimService.getClaimsForApprovalByUserCount( authUser.getUserId(), PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) ) > 0 )
    {
      return true;
    }
    return false;
  }

  public boolean approvesRecognition()
  {
    PromotionType recognitionPromotionType = PromotionType.lookup( PromotionType.RECOGNITION );
    if ( service.claimService.getClaimsForApprovalByUserCount( authUser.getUserId(), recognitionPromotionType ) > 0 )
    {
      return true;
    }
    ApproverSeekingClaimQueryConstraint claimQueryConstraint = new ApproverSeekingClaimQueryConstraint();
    String timeZoneID = service.userService.getUserTimeZone( authUser.getUserId() );
    String datePattern = DateFormatterUtil.getOracleDatePattern( UserManager.getLocale().toString() );
    Date toDay = DateUtils.applyTimeZone( new Date(), timeZoneID );
    claimQueryConstraint.setToDate( DateUtils.toDisplayString( toDay ) );
    claimQueryConstraint.setDatePattern( datePattern );
    claimQueryConstraint.setApprovedUserId( authUser.getUserId() );
    claimQueryConstraint.setClaimPromotionType( recognitionPromotionType );

    int claimsApprovedCount = service.claimService.getClaimListCount( claimQueryConstraint );
    if ( claimsApprovedCount > 0 )
    {
      return true;
    }

    return false;
  }

  public boolean approvesSixSigma()
  {
    return false;
  }

  public boolean approvesIdeas()
  {
    return false;
  }

  /**
   * Returns true if user has any current nomination approvals to do, or has done any nomination
   * approvals. The seconds part is done because for nom approvals, previous approvalls can be
   * viewed, so we need to show the link if the users has ever approved.
   *
   * @return boolean
   */
  public boolean approvesNominations()
  {
    // Check claims and claim groups since they are each approvable
    // // Claims
    String timeZoneID = service.userService.getUserTimeZone( authUser.getUserId() );
    String datePattern = DateFormatterUtil.getOracleDatePattern( UserManager.getLocale().toString() );
    Date toDay = DateUtils.applyTimeZone( new Date(), timeZoneID );
    PromotionType nominationPromotionType = PromotionType.lookup( PromotionType.NOMINATION );
    int claimsForApprovalCount = service.claimService.getClaimsForApprovalByUserCount( authUser.getUserId(), nominationPromotionType );
    if ( claimsForApprovalCount > 0 )
    {
      return true;
    }

    ApproverSeekingClaimQueryConstraint claimQueryConstraint = new ApproverSeekingClaimQueryConstraint();
    claimQueryConstraint.setToDate( DateUtils.toDisplayString( toDay ) );
    claimQueryConstraint.setDatePattern( datePattern );
    claimQueryConstraint.setApprovedUserId( authUser.getUserId() );
    claimQueryConstraint.setClaimPromotionType( nominationPromotionType );

    int claimsApprovedCount = service.claimService.getClaimListCount( claimQueryConstraint );
    if ( claimsApprovedCount > 0 )
    {
      return true;
    }

    // // Claim groups
    int claimGroupsForApprovalCount = service.claimGroupService.getClaimGroupsForApprovalByUserCount( authUser.getUserId(), nominationPromotionType );
    if ( claimGroupsForApprovalCount > 0 )
    {
      return true;
    }

    ApproverSeekingClaimGroupQueryConstraint claimGroupQueryConstraint = new ApproverSeekingClaimGroupQueryConstraint();
    claimGroupQueryConstraint.setApprovedUserId( authUser.getUserId() );
    claimGroupQueryConstraint.setClaimGroupPromotionType( nominationPromotionType );

    int claimGroupsApprovedCount = service.claimGroupService.getClaimGroupListCount( claimGroupQueryConstraint );
    if ( claimGroupsApprovedCount > 0 )
    {
      return true;
    }

    return false;
  }

  private User getUser()
  {
    return user != null ? user : loadUser();
  }

  private User loadUser()
  {
    user = service.userService.getUserById( authUser.getUserId() );
    return user;
  }

  private boolean loadRole( Boolean holder, String roleCode )
  {
    holder = new Boolean( service.aznService.isUserInRole( roleCode ) );
    return holder.booleanValue();
  }

  public String getSessionId()
  {
    return authUser.getSessionId();
  }

  public String getRouteId()
  {
    return authUser.getRouteId();
  }
}

class SystemInfo
{
  private MainContentServiceImpl service;
  private Boolean productClaims;
  private Boolean recognition;
  private Boolean quizzes;
  private Boolean nominations;
  private Boolean surveys;
  private Boolean goalQuest;
  private Boolean challengepoint;
  private Boolean leaderBoard;
  private Boolean badges;
  private Boolean throwdown;
  private Boolean ssi;
  private Boolean salesMaker;
  private Boolean selfEnroll;

  SystemInfo( MainContentServiceImpl service )
  {
    this.service = service;
  }

  public boolean hasBadges()
  {
    return badges != null ? badges.booleanValue() : loadModule( badges, SystemVariableService.INSTALL_BADGES );
  }

  public boolean hasProductClaims()
  {
    return productClaims != null ? productClaims.booleanValue() : loadModule( productClaims, SystemVariableService.INSTALL_PRODUCTCLAIMS );
  }

  public boolean hasRecognition()
  {
    return recognition != null ? recognition.booleanValue() : loadModule( recognition, SystemVariableService.INSTALL_RECOGNITION );
  }

  public boolean hasQuizzes()
  {
    return quizzes != null ? quizzes.booleanValue() : loadModule( quizzes, SystemVariableService.INSTALL_QUIZZES );
  }

  public boolean hasNominations()
  {
    return nominations != null ? nominations.booleanValue() : loadModule( nominations, SystemVariableService.INSTALL_NOMINATIONS );
  }

  public boolean hasSurveys()
  {
    return surveys != null ? surveys.booleanValue() : loadModule( surveys, SystemVariableService.INSTALL_SURVEYS );
  }

  public boolean hasGoalQuest()
  {
    return goalQuest != null ? goalQuest.booleanValue() : loadModule( goalQuest, SystemVariableService.INSTALL_GOAL_QUEST );
  }

  public boolean hasChallengepoint()
  {
    return challengepoint != null ? challengepoint.booleanValue() : loadModule( challengepoint, SystemVariableService.INSTALL_CHALLENGEPOINT );
  }

  public boolean hasLeaderBoard()
  {
    return leaderBoard != null ? leaderBoard.booleanValue() : loadModule( leaderBoard, SystemVariableService.LEADERBOARD_SHOW_HIDE );
  }

  public boolean hasThrowdown()
  {
    return throwdown != null ? throwdown.booleanValue() : loadModule( throwdown, SystemVariableService.INSTALL_THROWDOWN );
  }

  public boolean hasSsi()
  {
    return ssi != null ? ssi.booleanValue() : loadModule( ssi, SystemVariableService.INSTALL_SSI );
  }

  public boolean isSalesMaker()
  {
    return salesMaker != null ? salesMaker.booleanValue() : loadModule( salesMaker, SystemVariableService.SALES_MAKER );
  }

  public boolean isSelfEnroll()
  {
    return selfEnroll != null ? selfEnroll.booleanValue() : loadModule( selfEnroll, SystemVariableService.SELF_ENROLL_ALLOWED );
  }

  private boolean loadModule( Boolean holder, String sysVarName )
  {
    PropertySetItem sysVar = service.sysVarService.getPropertyByName( sysVarName );
    boolean sysVarValue = sysVar != null && sysVar.getBooleanVal();
    holder = new Boolean( sysVarValue );
    return holder.booleanValue();
  }
  
}
