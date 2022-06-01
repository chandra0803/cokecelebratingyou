
package com.biperf.core.ui.gamification;

/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/gamification/GamificationAdminAction.java,v $
 */

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.json.JSONObject;
import org.ujac.util.StringUtils;

import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.BadgeCountType;
import com.biperf.core.domain.enums.BadgeLevelType;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.enums.MessageModuleType;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.SweepstakesWinnerEligibilityType;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.gamification.BadgeBehaviorPromotion;
import com.biperf.core.domain.gamification.BadgeBehaviorView;
import com.biperf.core.domain.gamification.BadgeLevelsView;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.gamification.BadgePromotion;
import com.biperf.core.domain.gamification.BadgePromotionLevels;
import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.domain.gamification.GamificationBadgeProfileView;
import com.biperf.core.domain.gamification.GamificationBadgeTileView;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.gamification.ParticipantType;
import com.biperf.core.domain.goalquest.GoalBadgeRule;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.StackStandingBadgeRule;
import com.biperf.core.domain.promotion.StackStandingPayout;
import com.biperf.core.domain.promotion.StackStandingPayoutGroup;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionBadgeRulesUpdateAssociation;
import com.biperf.core.service.promotion.PromotionBillCodeUpdateAssociation;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;

/**
 * GamificationAdminAction.
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
 * <td>sharafud</td>
 * <td>August 23, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GamificationAdminAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( GamificationAdminAction.class );
  private static final String SPLIT_TOKEN = ",";
  private static final String REPLACEMENT_TOKEN = "&#664;";
  private static String BILLCODE_RECOGNITION_TYPE_RECEIVER = "Receiver";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Dispatcher.  Default to home page display.  Too much work to append 'method=display'
   * to all the paths that lead to the home page.  
   */
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( mapping.getParameter() != null )
    {
      return super.execute( mapping, form, request, response );
    }
    else
    {
      return this.display( mapping, form, request, response );
    }
  }

  /**
   * Method to list the active badges in the system.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forward = "showBadgeList";
    String successMessage = "";
    String fromPage = "";

    successMessage = request.getParameter( "successMessage" );
    fromPage = request.getParameter( "fromPage" );
    if ( !StringUtil.isEmpty( successMessage ) )
    {
      request.setAttribute( "successMessage", successMessage );
    }
    if ( !StringUtil.isEmpty( fromPage ) )
    {
      request.setAttribute( "fromPage", fromPage );
    }
    List<Promotion> badgeList = new ArrayList<Promotion>();
    List<Badge> liveBadgeList = new ArrayList<Badge>();
    List<Badge> underConstructionBadgeList = new ArrayList<Badge>();
    List<Badge> completedBadgeList = new ArrayList<Badge>();

    badgeList = getPromotionService().getAllBadges();

    Iterator it = badgeList.iterator();

    while ( it.hasNext() )
    {
      Promotion promotion = (Promotion)it.next();

      if ( ( (Badge)promotion ).getStatus().equals( Badge.BADGE_UNDER_CONSTRUCTION ) )
      {
        underConstructionBadgeList.add( (Badge)promotion );
      }

      else if ( promotion.isLive() && ( (Badge)promotion ).getStatus().equals( Badge.BADGE_ACTIVE ) )
      {
        liveBadgeList.add( (Badge)promotion );
      }

      else if ( promotion.isComplete() && ( (Badge)promotion ).getStatus().equals( Badge.BADGE_ACTIVE ) )
      {
        completedBadgeList.add( (Badge)promotion );
      }
    }

    request.setAttribute( "liveBadgeList", liveBadgeList );
    request.setAttribute( "savedBadgeList", underConstructionBadgeList );
    request.setAttribute( "completedBadgeList", completedBadgeList );
    return mapping.findForward( forward );
  }

  /**
   * Method to list the active badges in the system.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward showExpiredBadges( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forward = "showExpiredBadgeList";
    String successMessage = "";
    String fromPage = "";

    successMessage = request.getParameter( "successMessage" );
    fromPage = request.getParameter( "fromPage" );
    if ( !StringUtil.isEmpty( successMessage ) )
    {
      request.setAttribute( "successMessage", successMessage );
    }
    if ( !StringUtil.isEmpty( fromPage ) )
    {
      request.setAttribute( "fromPage", fromPage );
    }
    List<Badge> badgeList = new ArrayList<Badge>();
    badgeList = getGamificationService().getBadgeByStatus( Badge.BADGE_INACTIVE );
    request.setAttribute( "badgeList", badgeList );
    return mapping.findForward( forward );
  }

  /**
   * Method to populate the form while adding a new badge.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */

  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forward = "createBadge";
    BadgeForm badgeForm = (BadgeForm)form;
    List promotionList = new ArrayList();
    promotionList = getGamificationService().getLiveCompletedPromotionList();
    List badgeTypeFileLoadList = BadgeType.getList().subList( 2, 3 );
    request.setAttribute( "badgeTypeFileLoadList", badgeTypeFileLoadList );
    request.setAttribute( "promotionList", promotionList );
    request.setAttribute( "badgeTypeList", BadgeType.getList() );
    List badgeLevelList = BadgeLevelType.getList();

    List userCharList = getUserCharacteristicService().getAllCharacteristics();
    request.setAttribute( "userCharList", userCharList );

    request.setAttribute( "badgeEarnedList", BadgeCountType.getList() );
    badgeForm.setBadgeLibraryList( getGamificationService().buildBadgeLibraryList() );
    request.setAttribute( "badgeLibraryList", getGamificationService().buildBadgeLibraryList() );
    request.setAttribute( "fileLoadTableList", getGamificationService().buildFileLoadTableList( 3, null ) );
    request.setAttribute( "progressTableList", getGamificationService().buildProgressTableList( 3, null ) );
    request.setAttribute( "pointRangeTableList", getGamificationService().buildPointRangeTableList( 3, null ) );
    request.setAttribute( "currentFileLoadTableSize", "3" );
    request.setAttribute( "currentProgressTableSize", "3" );
    request.setAttribute( "currentPointRangeTableSize", "3" );
    List activeMessages = new ArrayList();
    activeMessages = getMessageService().getAllActiveMessagesByModuleType( MessageModuleType.GENERAL );
    request.setAttribute( "notificationMessageMap", getActiveMessageMap( activeMessages ) );
    return mapping.findForward( forward );
  }

  public PromotionStatusType generatePromotionStatus( Date badgeStartDate )
  {
    Date currentDate = new Date();
    // if the submission start date is has passed, automatically set the promotion to live
    if ( badgeStartDate.before( currentDate ) )
    {
      return PromotionStatusType.lookup( PromotionStatusType.LIVE );
    }
    else
    {
      return PromotionStatusType.lookup( PromotionStatusType.COMPLETE );
    }
  }

  /**
   * Method to add a new badge
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */

  public ActionForward createBadge( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    String forwardTo = "showBadgeList";
    List<BadgeRule> badgeRulesList = new ArrayList<BadgeRule>();
    BadgeForm badgeForm = (BadgeForm)form;
    BadgePromotion badgePromoReturned;
    String[] promotions = badgeForm.getPromotionIds();
    Promotion promotion = new Badge();
    if ( badgeForm.getBadgeId() != null && !badgeForm.getBadgeId().isEmpty() )
    {
      promotion = getPromotionService().getPromotionById( new Long( badgeForm.getBadgeId() ) );
    }
    if ( badgeForm.getBadgeType() != null && !badgeForm.getBadgeType().isEmpty() )
    {
      ( (Badge)promotion ).setBadgeType( BadgeType.lookup( badgeForm.getBadgeType() ) );
    }
    ( (Badge)promotion ).setBadgeType( BadgeType.lookup( badgeForm.getBadgeType() ) );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.BADGE ) );
    promotion.setPromotionName( badgeForm.getBadgeSetupName() );
    promotion = getPromotionService().savePromoNameCmText( promotion, badgeForm.getBadgeSetupName() );
    if ( badgeForm.getDisplayDays() != null )
    {
      ( (Badge)promotion ).setDisplayEndDays( Long.parseLong( badgeForm.getDisplayDays() ) );
    }
    else
    {
      ( (Badge)promotion ).setDisplayEndDays( new Long( 0 ) );
    }
    if ( badgeForm.getTileHighlightPeriod() != null )
    {
      ( (Badge)promotion ).setTileHighlightPeriod( Long.parseLong( badgeForm.getTileHighlightPeriod() ) );
    }
    else
    {
      ( (Badge)promotion ).setTileHighlightPeriod( new Long( 0 ) );
    }

    ( (Badge)promotion ).setStatus( Badge.BADGE_ACTIVE );
    promotion.setPromotionStatus( generatePromotionStatus( DateUtils.toDate( badgeForm.getStartDate() ) ) );
    ( (Badge)promotion ).setNotificationMessageId( badgeForm.getNotificationMessageId() );
    ( (Badge)promotion ).setBadgeCountType( BadgeCountType.lookup( badgeForm.getBadgeCountType() ) );
    if ( badgeForm.getStartDate() != null && badgeForm.getStartDate().length() > 0 )
    {
      promotion.setSubmissionStartDate( DateUtils.toDate( badgeForm.getStartDate() ) );
    }
    promotion.setTaxable( badgeForm.isTaxable() );
    promotion.setBillCodesActive( badgeForm.isBillCodesActive() );
    promotion.setPromotionBillCodes( badgeForm.getPromoBillCodeList( promotion ) );
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    promotion.setSweepstakesWinnerEligibilityType( (SweepstakesWinnerEligibilityType)PickListItem.getDefaultItem( SweepstakesWinnerEligibilityType.class ) );
    if ( badgeForm.isIncludeAllBehaviorPoints() )
    {
      ( (Badge)promotion ).setAllBehaviorPoints( badgeForm.getAllBehaviorPoints() );
    }
    else
    {
      ( (Badge)promotion ).setAllBehaviorPoints( null );
    }

    Badge badgeReturned = (Badge)getPromotionService().savePromotion( promotion );

    if ( ( (Badge)promotion ).getBadgePromotions() != null && ( (Badge)promotion ).getBadgePromotions().size() > 0 )
    {
      getGamificationService().deleteBadgePromotion( ( (Badge)promotion ).getBadgePromotions() );
    }

    if ( ( (Badge)promotion ).getBadgeRules() != null && ( (Badge)promotion ).getBadgeRules().size() > 0 )
    {
      getGamificationService().deleteBadgeRule( ( (Badge)promotion ).getBadgeRules() );
    }

    if ( promotions != null )
    {
      for ( int i = 0; i < promotions.length; i++ )
      {
        if ( !promotions[i].equalsIgnoreCase( "-1" ) )
        {
          Promotion promo = getPromotionService().getPromotionById( Long.parseLong( promotions[i] ) );

          BadgePromotion badgePromo = new BadgePromotion();
          badgePromo.setBadgePromotion( badgeReturned );
          badgePromo.setEligiblePromotion( promo );
          badgePromoReturned = getGamificationService().saveBadgePromotion( badgePromo );
        }
      }
      badgeRulesList = parseBadgeRuleDetails( badgeForm, badgeReturned, promotions[0], "add" );
    }
    else
    {
      String promotionId = badgeReturned.getPromotionIds( badgeReturned.getBadgePromotions() );
      badgeRulesList = parseBadgeRuleDetails( badgeForm, badgeReturned, promotionId, "update" );
    }

    BadgeRule badgeRuleLastRowReturned = getGamificationService().saveBadgeRules( badgeRulesList );
    String successMessage = "";
    if ( badgeRuleLastRowReturned != null && badgeRuleLastRowReturned.getId() > 0 )
    {
      request.setAttribute( "successMessage", "Y" );
      successMessage = "Y";

    }
    else
    {
      request.setAttribute( "successMessage", "N" );
      successMessage = "N";
    }
    return new ActionForward( mapping.findForward( forwardTo ).getPath() + "?fromPage=create&successMessage=" + successMessage, false );

  }

  public ActionForward saveDraft( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    String forwardTo = "showBadgeList";
    List<BadgeRule> badgeRulesList = new ArrayList<BadgeRule>();
    BadgeForm badgeForm = (BadgeForm)form;
    BadgePromotion badgePromoReturned;
    String[] promotions = badgeForm.getPromotionIds();
    List<ParticipantBadge> paxList = new ArrayList<ParticipantBadge>();

    // add-update a badge start
    Promotion promotion = new Badge();
    if ( badgeForm.getBadgeId() != null && !badgeForm.getBadgeId().isEmpty() )
    {
      promotion = getPromotionService().getPromotionById( new Long( badgeForm.getBadgeId() ) );
    }
    if ( badgeForm.getBadgeType() != null && !badgeForm.getBadgeType().isEmpty() )
    {
      ( (Badge)promotion ).setBadgeType( BadgeType.lookup( badgeForm.getBadgeType() ) );
    }
    promotion.setPromotionType( PromotionType.lookup( PromotionType.BADGE ) );
    promotion.setPromotionName( badgeForm.getBadgeSetupName() );
    promotion = getPromotionService().savePromoNameCmText( promotion, badgeForm.getBadgeSetupName() );
    if ( badgeForm.getDisplayDays() != null )
    {
      ( (Badge)promotion ).setDisplayEndDays( Long.parseLong( badgeForm.getDisplayDays() ) );
    }
    else
    {
      ( (Badge)promotion ).setDisplayEndDays( new Long( 0 ) );
    }
    if ( badgeForm.getTileHighlightPeriod() != null )
    {
      ( (Badge)promotion ).setTileHighlightPeriod( Long.parseLong( badgeForm.getTileHighlightPeriod() ) );
    }
    else
    {
      ( (Badge)promotion ).setTileHighlightPeriod( new Long( 0 ) );
    }

    ( (Badge)promotion ).setStatus( Badge.BADGE_UNDER_CONSTRUCTION );
    if ( badgeForm.getStartDate() != null && badgeForm.getStartDate().length() > 0 )
    {
      promotion.setSubmissionStartDate( DateUtils.toDate( badgeForm.getStartDate() ) );
    }
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.UNDER_CONSTRUCTION ) );
    ( (Badge)promotion ).setNotificationMessageId( badgeForm.getNotificationMessageId() );
    ( (Badge)promotion ).setBadgeCountType( BadgeCountType.lookup( badgeForm.getBadgeCountType() ) );

    promotion.setTaxable( badgeForm.isTaxable() );
    promotion.setBillCodesActive( badgeForm.isBillCodesActive() );
    promotion.setPromotionBillCodes( badgeForm.getPromoBillCodeList( promotion ) );
    promotion.setAwardType( PromotionAwardsType.lookup( PromotionAwardsType.POINTS ) );
    if ( badgeForm.isIncludeAllBehaviorPoints() )
    {
      ( (Badge)promotion ).setAllBehaviorPoints( badgeForm.getAllBehaviorPoints() );
    }
    else
    {
      ( (Badge)promotion ).setAllBehaviorPoints( null );
    }

    Badge badgeReturned = (Badge)getPromotionService().savePromotion( promotion );

    // add-update a badge end

    if ( ( (Badge)promotion ).getBadgePromotions() != null && ( (Badge)promotion ).getBadgePromotions().size() > 0 )
    {
      getGamificationService().deleteBadgePromotion( ( (Badge)promotion ).getBadgePromotions() );
    }

    if ( ( (Badge)promotion ).getBadgeRules() != null && ( (Badge)promotion ).getBadgeRules().size() > 0 )
    {
      getGamificationService().deleteBadgeRule( ( (Badge)promotion ).getBadgeRules() );
    }

    if ( promotions != null )
    {
      for ( int i = 0; i < promotions.length; i++ )
      {
        if ( !promotions[i].equalsIgnoreCase( "-1" ) )
        {
          Promotion promo = getPromotionService().getPromotionById( Long.parseLong( promotions[i] ) );

          BadgePromotion badgePromo = new BadgePromotion();
          badgePromo.setBadgePromotion( badgeReturned );
          badgePromo.setEligiblePromotion( promo );
          badgePromoReturned = getGamificationService().saveBadgePromotion( badgePromo );
        }
      }
      badgeRulesList = parseBadgeRuleDetails( badgeForm, badgeReturned, promotions[0], "add" );
    }
    else
    {
      String promotionId = badgeReturned.getPromotionIds( badgeReturned.getBadgePromotions() );
      badgeRulesList = parseBadgeRuleDetails( badgeForm, badgeReturned, promotionId, "update" );
    }

    int currentBadgeRuleSize = ( (Badge)promotion ).getBadgeRules().size();
    int newBadgeRuleSize = badgeRulesList.size();

    // If any new badge rule added for progress badge type, then get the pax who already earned
    // highest level and need to insert a in progress record
    if ( newBadgeRuleSize != currentBadgeRuleSize && ( (Badge)promotion ).getBadgeType().getCode().equalsIgnoreCase( BadgeType.PROGRESS ) )
    {
      paxList = getGamificationService().getParticipansEarnedHighestLevel( promotion.getId() );

    }
    BadgeRule badgeRuleLastRowReturned = getGamificationService().saveBadgeRules( badgeRulesList );
    if ( paxList != null && paxList.size() > 0 )
    {
      Iterator paxItr = paxList.iterator();
      while ( paxItr.hasNext() )
      {
        ParticipantBadge paxBadge = (ParticipantBadge)paxItr.next();
        ParticipantBadge participantBadge = new ParticipantBadge();
        participantBadge.setParticipant( paxBadge.getParticipant() );
        participantBadge.setBadgePromotion( paxBadge.getBadgePromotion() );
        participantBadge.setBadgeRule( (BadgeRule)badgeRulesList.get( currentBadgeRuleSize ) );
        participantBadge.setIsEarned( false );
        participantBadge.setStatus( "A" );
        participantBadge.setSentCount( paxBadge.getSentCount() );
        participantBadge.setReceivedCount( paxBadge.getReceivedCount() );
        getGamificationService().saveParticipantBadge( participantBadge );
      }
    }
    String successMessage = "";
    if ( badgeRuleLastRowReturned != null && badgeRuleLastRowReturned.getId() > 0 )
    {
      request.setAttribute( "successMessage", "Y" );
      successMessage = "Y";

    }
    else
    {
      request.setAttribute( "successMessage", "N" );
      successMessage = "N";
    }
    return new ActionForward( mapping.findForward( forwardTo ).getPath() + "?fromPage=saveDraft&successMessage=" + successMessage, false );

  }

  /**
   * Method to show the badge details in edit mode
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */

  public ActionForward prepareUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forwardTo = ActionConstants.UPDATE_FORWARD;
    ActionMessages errors = new ActionMessages();
    BadgeForm badgeForm = (BadgeForm)form;

    String badgeId = null;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      try
      {
        badgeId = (String)clientStateMap.get( "badgeId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "badgeId" );
        badgeId = id.toString();
      }

      if ( badgeId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "badgeId as part of clientState" ) );
        saveErrors( request, errors );
        mapping.findForward( ActionConstants.FAIL_UPDATE );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NONSWEEP_PROMO_BILLCODES ) );
    Badge badgeToUpdate = getGamificationService().getBadgeByIdWithAssociations( new Long( badgeId ), associationRequestCollection );
    Set badgeRules = badgeToUpdate.getBadgeRules();

    List<BadgeRule> badgeRulesList = new ArrayList<BadgeRule>();
    badgeRulesList.addAll( badgeRules );
    Collections.sort( badgeRulesList, new BadgeRuleComparator() );

    Iterator it = badgeRulesList.iterator();
    String levelName = "";
    BadgeLevelType levelType = null;
    String behaviorCode = "";
    Long countryId;
    String countryCode = "";
    String promotionNamesBehavior = "";
    List badgeRuleModifiedList = new ArrayList();
    String badgeLibId = "";
    String badgeLibDesc = "";
    String badgeName = "";
    Long badgePoints = null;
    boolean eligibleForSweepstake = false;
    String badgeDescription = "";
    List<BadgeRule> stackStandBadges;
    List<BadgeRule> overallBadges;
    List<BadgeRule> undefeatedBadges;
    List behaviors = null;
    List<BadgeBehaviorPromotion> badgebehaviorPromoList = new ArrayList<BadgeBehaviorPromotion>();

    while ( it.hasNext() )
    {
      BadgeRule badgeRule = (BadgeRule)it.next();
      levelName = badgeRule.getLevelName();
      levelType = badgeRule.getLevelType();
      countryId = badgeRule.getCountryId();
      behaviorCode = badgeRule.getBehaviorName();
      badgeLibId = badgeRule.getBadgeLibraryCMKey();
      badgeLibDesc = getGamificationService().getBadgeLibDescription( badgeLibId );
      badgeRule.setBadgeLibDisplayName( badgeLibDesc );
      badgeName = badgeRule.getBadgeNameTextFromCM();
      badgeRule.setBadgeName( badgeName );
      badgePoints = badgeRule.getBadgePoints();
      badgeRule.setBadgePoints( badgePoints );
      eligibleForSweepstake = badgeRule.isEligibleForSweepstake();
      badgeRule.setEligibleForSweepstake( eligibleForSweepstake );
      badgeDescription = badgeRule.getBadgeDescriptionTextFromCM();
      badgeRule.setBadgeDescription( badgeDescription );
      if ( !StringUtil.isEmpty( behaviorCode ) )
      {
        String promotionIds = badgeToUpdate.getPromotionIds( badgeToUpdate.getBadgePromotions() );
        promotionNamesBehavior = getGamificationService().getPromotionByBehavior( promotionIds, behaviorCode );
        badgeRule.setPromotionNames( promotionNamesBehavior );
      }
      if ( !StringUtil.isEmpty( levelName ) && countryId != null && countryId > 0 )
      {
        Country country = getCountryService().getCountryById( countryId );
        countryCode = country.getCountryCode();
        badgeRule.setCountryCode( countryCode.toUpperCase() );
      }
      badgeRuleModifiedList.add( badgeRule );

    }
    badgeToUpdate.setBadgeRules( new HashSet( badgeRuleModifiedList ) );
    badgeForm.load( badgeToUpdate );

    if ( badgeToUpdate.getBadgeType().getCode().equalsIgnoreCase( "fileload" ) )
    {
      String isShowFileloadNoPromoDiv = "N";
      for ( Iterator<BadgePromotion> badgePromotionIter = badgeToUpdate.getBadgePromotions().iterator(); badgePromotionIter.hasNext(); )
      {

        BadgePromotion bp = badgePromotionIter.next();
        Promotion promotion = getPromotionService().getPromotionById( bp.getEligiblePromotion().getId() );
        if ( promotion.getPromotionType().getCode().equals( PromotionType.RECOGNITION ) || promotion.getPromotionType().getCode().equals( PromotionType.NOMINATION ) )
        {
          isShowFileloadNoPromoDiv = "Y";
          break;
        }
      }
      request.setAttribute( "isShowFileloadNoPromoDiv", isShowFileloadNoPromoDiv );
    }

    if ( badgeToUpdate.getBadgeType().getCode().equalsIgnoreCase( "earned" ) && StringUtil.isEmpty( levelName ) && levelType == null )
    {
      request.setAttribute( "isPointRange", "Y" );
    }
    else if ( badgeToUpdate.getBadgeType().getCode().equalsIgnoreCase( "earned" ) && !StringUtil.isEmpty( levelName ) && levelType == null )
    {
      request.setAttribute( "isPointRange", "N" );
      String promotionIds = badgeToUpdate.getPromotionIds( badgeToUpdate.getBadgePromotions() );
      Promotion promotion = getPromotionService().getPromotionById( Long.parseLong( promotionIds ) );
      if ( promotion.isGoalQuestOrChallengePointPromotion() )
      {
        request.setAttribute( "isGoalQuest", "Y" );
        request.setAttribute( "hasPartners", promotion.isPartnersEnabled() ? "Y" : "N" );
      }
      else
      {
        request.setAttribute( "isGoalQuest", "N" );
      }
      if ( promotion.isPartnersEnabled() )
      {
        request.setAttribute( "isPartners", "Y" );
      }
      else
      {
        request.setAttribute( "isPartners", "N" );
      }
    }
    else if ( badgeToUpdate.getBadgeType().getCode().equalsIgnoreCase( "earned" ) && levelType != null )
    {
      request.setAttribute( "isPointRange", "N" );
      String promotionIds = badgeToUpdate.getPromotionIds( badgeToUpdate.getBadgePromotions() );
      Promotion promotion = getPromotionService().getPromotionById( Long.parseLong( promotionIds ) );
      if ( promotion.getPromotionType().getCode().equalsIgnoreCase( PromotionType.THROWDOWN ) )
      {
        AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_PAYOUTS ) );
        promotion = getPromotionService().getPromotionByIdWithAssociations( Long.parseLong( promotionIds ), promoAssociationRequestCollection );
        request.setAttribute( "isThrowDown", "Y" );
        if ( promotion.isThrowdownPromotion() && ! ( (ThrowdownPromotion)promotion ).getStackStandingPayoutGroups().isEmpty() )
        {
          request.setAttribute( "hasStackStandingPayouts", "Y" );
        }
        else
        {
          request.setAttribute( "hasStackStandingPayouts", "N" );
        }
        // Splitting the badge rules according to level type for throwdown-earned
        List<BadgeRule> tdBadgeRules = badgeForm.getBadgeRuleList();

        stackStandBadges = badgeToUpdate.getBadgeRulesByLevelType( BadgeLevelType.lookup( BadgeLevelType.STACK_STAND ), tdBadgeRules );
        overallBadges = badgeToUpdate.getBadgeRulesByLevelType( BadgeLevelType.lookup( BadgeLevelType.OVERALL ), tdBadgeRules );
        undefeatedBadges = badgeToUpdate.getBadgeRulesByLevelType( BadgeLevelType.lookup( BadgeLevelType.UNDEFEATED ), tdBadgeRules );

        if ( stackStandBadges != null && !stackStandBadges.isEmpty() )
        {
          request.setAttribute( "stackStandBadges", stackStandBadges );
        }
        if ( overallBadges != null && !overallBadges.isEmpty() )
        {
          request.setAttribute( "overallBadges", overallBadges );
        }
        if ( undefeatedBadges != null && !undefeatedBadges.isEmpty() )
        {
          request.setAttribute( "undefeatedBadges", undefeatedBadges );
          badgeForm.setUndefeatedTdBadgeRuleListSize( undefeatedBadges.size() );
        }
      }
      else
      {
        request.setAttribute( "isThrowDown", "N" );
        request.setAttribute( "hasStackStandingPayouts", "N" );
      }
    }

    if ( badgeToUpdate.isUnderConstruction() )
    {
      request.setAttribute( "badgeCompleted", Boolean.FALSE );
    }
    else
    {
      request.setAttribute( "badgeCompleted", Boolean.TRUE );
    }

    List promotionList = new ArrayList();
    promotionList = getGamificationService().getLiveCompletedPromotionList();
    request.setAttribute( "promotionList", promotionList );
    request.setAttribute( "badgeTypeList", BadgeType.getList() );
    List badgeTypeFileLoadList = BadgeType.getList().subList( 2, 3 );
    request.setAttribute( "badgeTypeFileLoadList", badgeTypeFileLoadList );

    String isEndDateNull = "N";
    isEndDateNull = getGamificationService().validatePromotionEndDate( badgeToUpdate.getPromotionIds( badgeToUpdate.getBadgePromotions() ) );
    request.setAttribute( "isEndDateNull", isEndDateNull );
    request.setAttribute( "badgeType", badgeToUpdate.getBadgeType().getCode() );
    request.setAttribute( "badgeForm", badgeForm );
    if ( badgeToUpdate.getBadgeType().getCode().equalsIgnoreCase( BadgeType.BEHAVIOR ) )
    {
      String promotionId = badgeToUpdate.getPromotionIds( badgeToUpdate.getBadgePromotions() );
      int i = promotionId.indexOf( ',' );
      String firstPromotion = "";

      if ( i == -1 )
      {
        firstPromotion = promotionId;
      }
      else
      {
        firstPromotion = promotionId.substring( 0, i );
      }

      Promotion promotion = getPromotionService().getPromotionById( Long.parseLong( firstPromotion ) );
      behaviors = getGamificationService().getBehaviorForSelectedPromotions( promotionId, null, promotion.getPromotionType().getCode() );
      badgebehaviorPromoList = getGamificationService().getBadgeBehaviorPromotions( badgeToUpdate.getPromotionIds( badgeToUpdate.getBadgePromotions() ), behaviors );

      request.setAttribute( "behaviorListSize", badgebehaviorPromoList.size() );

      for ( Iterator<BadgeBehaviorPromotion> badgebehaviorPromoListIter = badgebehaviorPromoList.iterator(); badgebehaviorPromoListIter.hasNext(); )
      {
        BadgeBehaviorPromotion badgeBehaviorPromotion = badgebehaviorPromoListIter.next();
        for ( Iterator<BadgeRule> badgeRuleIter = badgeForm.getBadgeRuleList().iterator(); badgeRuleIter.hasNext(); )
        {
          BadgeRule badgeRule = badgeRuleIter.next();
          if ( badgeBehaviorPromotion.getBehaviorCode().equals( badgeRule.getBehaviorName() ) )
          {
            badgebehaviorPromoListIter.remove();
            break;
          }
        }
      }

      request.setAttribute( "behaviorList", badgebehaviorPromoList );
      List<BadgeRule> tempBadgeRuleslist = badgeForm.getBadgeRuleList();
      for ( BadgeBehaviorPromotion badgeBehaviorPromotion : badgebehaviorPromoList )
      {
        BadgeRule badgeRule = new BadgeRule();
        badgeRule.setBehaviorName( badgeBehaviorPromotion.getBehaviorCode() );
        badgeRule.setPromotionNames( badgeBehaviorPromotion.getPromotionNames() );
        tempBadgeRuleslist.add( badgeRule );
      }
      request.setAttribute( "badgeRuleList", tempBadgeRuleslist );
      badgeForm.setBadgeRuleListSize( tempBadgeRuleslist.size() );
    }
    else
    {
      request.setAttribute( "badgeRuleList", badgeForm.getBadgeRuleList() );
      badgeForm.setBadgeRuleListSize( badgeForm.getBadgeRuleList().size() );
    }
    request.setAttribute( "badgeLibraryList", getGamificationService().buildBadgeLibraryList() );
    request.setAttribute( "currentFileLoadTableSize", badgeForm.getBadgeRuleList().size() );
    request.setAttribute( "currentProgressTableSize", badgeForm.getBadgeRuleList().size() );
    request.setAttribute( "currentPointRangeTableSize", badgeForm.getBadgeRuleList().size() );

    request.setAttribute( "badgeEarnedList", BadgeCountType.getList() );
    badgeForm.setBadgeLibraryList( getGamificationService().buildBadgeLibraryList() );

    List userCharList = getUserCharacteristicService().getAllCharacteristics();
    request.setAttribute( "userCharList", userCharList );
    request.setAttribute( "badgeLibraryList", getGamificationService().buildBadgeLibraryList() );
    List activeMessages = new ArrayList();
    activeMessages = getMessageService().getAllActiveMessagesByModuleType( MessageModuleType.GENERAL );
    request.setAttribute( "notificationMessageMap", getActiveMessageMap( activeMessages ) );
    request.setAttribute( "promotionStatus", badgeToUpdate.getPromotionStatus().getCode() );
    if ( badgeToUpdate.isLive() )
    {
      request.setAttribute( "fileLoadTableList", badgeForm.getBadgeRuleList().size() );
      request.setAttribute( "progressTableList", badgeForm.getBadgeRuleList().size() );
      request.setAttribute( "pointRangeTableList", badgeForm.getBadgeRuleList().size() );
    }
    else
    {
      request.setAttribute( "fileLoadTableList", getGamificationService().buildFileLoadTableList( badgeForm.getBadgeRuleList().size(), badgeForm.getBadgeRuleList() ) );
      request.setAttribute( "progressTableList", getGamificationService().buildProgressTableList( badgeForm.getBadgeRuleList().size(), badgeForm.getBadgeRuleList() ) );
      request.setAttribute( "pointRangeTableList", getGamificationService().buildPointRangeTableList( badgeForm.getBadgeRuleList().size(), badgeForm.getBadgeRuleList() ) );
      forwardTo = "createBadge";
      // bug fix
      request.setAttribute( "editing", Boolean.TRUE );
      List<String> ids = new ArrayList<String>();
      for ( BadgePromotion bp : badgeToUpdate.getBadgePromotions() )
      {
        ids.add( String.valueOf( bp.getEligiblePromotion().getId() ) );
      }
      request.setAttribute( "promoIds", org.apache.commons.lang3.StringUtils.join( ids, "," ) );
    }

    return mapping.findForward( forwardTo );
  }

  /**
   * Method to update badge details
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */

  public ActionForward updateBadge( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    String forwardTo = "showBadgeList";
    List<BadgeRule> badgeRulesList = new ArrayList<BadgeRule>();
    List<ParticipantBadge> paxList = new ArrayList<ParticipantBadge>();
    BadgeForm badgeForm = (BadgeForm)form;
    Promotion promotion = getPromotionService().getPromotionById( new Long( badgeForm.getBadgeId() ) );
    if ( badgeForm.getDisplayDays() != null )
    {
      ( (Badge)promotion ).setDisplayEndDays( Long.parseLong( badgeForm.getDisplayDays() ) );
    }
    else
    {
      ( (Badge)promotion ).setDisplayEndDays( new Long( 0 ) );
    }
    if ( badgeForm.getTileHighlightPeriod() != null )
    {
      ( (Badge)promotion ).setTileHighlightPeriod( Long.parseLong( badgeForm.getTileHighlightPeriod() ) );
    }
    else
    {
      ( (Badge)promotion ).setTileHighlightPeriod( new Long( 0 ) );
    }
    promotion = getPromotionService().savePromoNameCmText( promotion, badgeForm.getBadgeSetupName() );
    promotion.setName( badgeForm.getBadgeSetupName() );
    ( (Badge)promotion ).setNotificationMessageId( badgeForm.getNotificationMessageId() );
    promotion.setTaxable( badgeForm.isTaxable() );
    promotion.setBillCodesActive( badgeForm.isBillCodesActive() );
    promotion.setPromotionBillCodes( badgeForm.getPromoBillCodeList( promotion ) );
    promotion.setPromotionStatus( generatePromotionStatus( promotion.getSubmissionStartDate() ) );
    ( (Badge)promotion ).setStatus( Badge.BADGE_ACTIVE );
    if ( badgeForm.isIncludeAllBehaviorPoints() )
    {
      ( (Badge)promotion ).setAllBehaviorPoints( badgeForm.getAllBehaviorPoints() );
    }
    else
    {
      ( (Badge)promotion ).setAllBehaviorPoints( null );
    }

    if ( badgeForm.getStartDate() != null && badgeForm.getStartDate().length() > 0 )
    {
      promotion.setSubmissionStartDate( DateUtils.toDate( badgeForm.getStartDate() ) );
    }

    Badge badgeReturned = (Badge)getPromotionService().savePromotion( promotion );
    PromotionBillCodeUpdateAssociation billCodeUpdateAssociation = new PromotionBillCodeUpdateAssociation( promotion );
    List updateAssociations = new ArrayList();
    updateAssociations.add( billCodeUpdateAssociation );
    promotion = getPromotionService().savePromotion( new Long( badgeForm.getBadgeId() ), updateAssociations );
    String promotions = badgeReturned.getPromotionIds( badgeReturned.getBadgePromotions() );
    String[] promotionsArray = promotions.split( SPLIT_TOKEN );
    badgeRulesList = parseBadgeRuleDetails( badgeForm, badgeReturned, promotionsArray[0], "update" );
    int currentBadgeRuleSize = ( (Badge)promotion ).getBadgeRules().size();
    int newBadgeRuleSize = badgeRulesList.size();

    // If any new badge rule added for progress badge type, then get the pax who already earned
    // highest level and need to insert a in progress record
    if ( newBadgeRuleSize != currentBadgeRuleSize && ( (Badge)promotion ).getBadgeType().getCode().equalsIgnoreCase( BadgeType.PROGRESS ) )
    {
      paxList = getGamificationService().getParticipansEarnedHighestLevel( promotion.getId() );

    }

    Set<BadgeRule> badgeRulesSet = new HashSet<BadgeRule>();
    badgeRulesSet.addAll( badgeRulesList );
    badgeReturned.setBadgeRules( badgeRulesSet );

    int badgeRuleLastRowReturned = 0;
    if ( badgeReturned != null )
    {
      // existing badge
      PromotionBadgeRulesUpdateAssociation pbrua = new PromotionBadgeRulesUpdateAssociation( badgeReturned );
      badgeReturned = getGamificationService().saveBadge( badgeReturned.getId(), pbrua );
      badgeRuleLastRowReturned = badgeReturned.getBadgeRules().size();
    }
    // badge rules ends

    if ( paxList != null && paxList.size() > 0 )
    {
      Iterator paxItr = paxList.iterator();
      while ( paxItr.hasNext() )
      {
        ParticipantBadge paxBadge = (ParticipantBadge)paxItr.next();
        ParticipantBadge participantBadge = new ParticipantBadge();
        participantBadge.setParticipant( paxBadge.getParticipant() );
        participantBadge.setBadgePromotion( paxBadge.getBadgePromotion() );
        participantBadge.setBadgeRule( (BadgeRule)badgeRulesList.get( currentBadgeRuleSize ) );
        participantBadge.setIsEarned( false );
        participantBadge.setStatus( "A" );
        participantBadge.setSentCount( paxBadge.getSentCount() );
        participantBadge.setReceivedCount( paxBadge.getReceivedCount() );
        getGamificationService().saveParticipantBadge( participantBadge );
      }
    }
    String successMessage = "";
    if ( badgeRuleLastRowReturned > 0 )
    {
      request.setAttribute( "successMessage", "Y" );
      successMessage = "Y";

    }
    else
    {
      request.setAttribute( "successMessage", "N" );
      successMessage = "N";
    }
    return new ActionForward( mapping.findForward( forwardTo ).getPath() + "?fromPage=update&successMessage=" + successMessage, false );

  }

  /**
   * Method to update badge details
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */

  public ActionForward doExpireBadge( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    String forwardTo = "showBadgeList";
    ActionMessages errors = new ActionMessages();
    List<BadgeRule> badgeRulesList = new ArrayList<BadgeRule>();
    BadgeForm badgeForm = (BadgeForm)form;
    String successMessage = "";
    String[] badgeIds = badgeForm.getDeleteBadges();
    if ( badgeIds != null )
    {
      for ( int i = 0; i < badgeIds.length; i++ )
      {
        Promotion promotion = getPromotionService().getPromotionById( Long.parseLong( badgeIds[i] ) );
        ( (Badge)promotion ).setStatus( Badge.BADGE_INACTIVE );
        ( (Badge)promotion ).setSubmissionEndDate( DateUtils.getCurrentDate() );
        promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) );
        Badge badgeReturned = (Badge)getPromotionService().savePromotion( promotion );
        if ( badgeReturned != null && badgeReturned.getId() > 0 )
        {
          request.setAttribute( "successMessage", "Y" );
          successMessage = "Y";

        }
        else
        {
          request.setAttribute( "successMessage", "N" );
          successMessage = "N";
        }
      }
    }
    else if ( badgeForm.getBadgeId() != null )
    {
      Promotion promotion = getPromotionService().getPromotionById( Long.parseLong( badgeForm.getBadgeId() ) );
      ( (Badge)promotion ).setStatus( Badge.BADGE_INACTIVE );
      ( (Badge)promotion ).setSubmissionEndDate( DateUtils.getCurrentDate() );
      promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) );
      Badge badgeReturned = (Badge)getPromotionService().savePromotion( promotion );
      if ( badgeReturned != null && badgeReturned.getId() > 0 )
      {
        request.setAttribute( "successMessage", "Y" );
        successMessage = "Y";

      }
      else
      {
        request.setAttribute( "successMessage", "N" );
        successMessage = "N";
      }
    }
    return new ActionForward( mapping.findForward( forwardTo ).getPath() + "?fromPage=update&successMessage=" + successMessage, false );

  }

  /**
   * Method to get the participant badge tile Json 
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward(response put to stream for the ajax call)
   */
  public ActionForward fetchBadgesForTile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    // List<ParticipantBadge> partcipantBadgeList=getGamificationService().getBadgeByParticipantId(
    // UserManager.getUserId() );
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
    List<ParticipantBadge> partcipantBadgeList = getGamificationService().getBadgeByParticipantSorted( eligiblePromotions, UserManager.getUserId() );
    // Comparator comparator = Collections.reverseOrder();
    // Collections.sort(partcipantBadgeList,comparator);
    GamificationBadgeTileView gamficationTileView = getGamificationService().getTileViewJson( partcipantBadgeList );
    super.writeAsJsonToResponse( gamficationTileView, response );
    return null;
  }

  /**
   * Method to get the participant badge tile Json 
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward(response put to stream for the ajax call)
   */
  public ActionForward fetchBadgesForProfile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String paxId = request.getParameter( "participantId" );
    String isFromMinProfile = request.getParameter( "isFromMiniProfile" );
    boolean isFromMiniProfile = false;
    Long userId;
    if ( StringUtil.isEmpty( paxId ) )
    {
      userId = UserManager.getUserId();
    }
    else
    {
      userId = Long.parseLong( paxId );
    }
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
    List<ParticipantBadge> partcipantBadgeList = getGamificationService().getBadgeByParticipantProfileSorted( eligiblePromotions, userId, 0 );
    if ( isFromMinProfile != null && isFromMinProfile.equals( "true" ) )
    {
      isFromMiniProfile = true;
    }
    GamificationBadgeProfileView gamficationTileView = getGamificationService().getProfileViewJson( partcipantBadgeList, isFromMiniProfile );
    super.writeAsJsonToResponse( gamficationTileView, response );
    return null;
  }

  /**
   * Method to get the behaviors Json for the selected promotions
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward(response put to stream for the ajax call)
   */

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public ActionForward populateBehaviors( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PrintWriter out = response.getWriter();
    List behaviors = null;
    List<BadgeBehaviorPromotion> badgebehaviorPromoList = new ArrayList<BadgeBehaviorPromotion>();
    List badgeLibraryList = new ArrayList();
    String promoIds = RequestUtils.getRequiredParamString( request, "promotionIds" );
    String mode = request.getParameter( "mode" );
    BadgeBehaviorView badgeBehJson = new BadgeBehaviorView();
    boolean editMode = false;
    String alteredPromoIds = null;
    List<String> existingPromosId = null;
    if ( !StringUtil.isEmpty( mode ) && "edit".equals( mode ) )
    {
      editMode = true;
      String[] ids = promoIds.split( "," );
      List<String> alteredList = new ArrayList<String>();
      String badgeId = request.getParameter( "badgeId" );
      Badge badge = getGamificationService().getBadgeById( Long.parseLong( badgeId ) );
      // find out newly selected promos for behavioural badges
      for ( String id : ids )
      {
        if ( ids.length == 1 )
        {
          alteredList.add( id );
        }
        else
        {
          boolean add = true;
          for ( BadgePromotion bp : badge.getBadgePromotions() )
          {
            if ( bp.getEligiblePromotion().getId() == Long.parseLong( id ) )
            {
              if ( existingPromosId == null )
              {
                existingPromosId = new ArrayList<String>();
              }
              existingPromosId.add( String.valueOf( bp.getEligiblePromotion().getId() ) );
              add = false;
            }
            if ( add )
            {
              alteredList.add( id );
            }
          }
        }
      }
      alteredPromoIds = org.apache.commons.lang3.StringUtils.join( alteredList, "," );
    }
    try
    {
      // Get list of IDs
      String[] ids = promoIds.split( "," );
      Long[] idLongs = new Long[ids.length];
      for ( int i = 0; i < ids.length; ++i )
      {
        idLongs[i] = Long.parseLong( ids[i] );
      }

      // Grab the promotions that are selected. Validate that they are all the same promotion type.
      PromotionQueryConstraint listQuery = new PromotionQueryConstraint();
      listQuery.setPromotionIds( idLongs );
      List<Promotion> promotionList = getPromotionService().getPromotionList( listQuery );
      Set<PromotionType> promotionTypes = promotionList.stream().map( ( promotion ) -> promotion.getPromotionType() ).collect( Collectors.toSet() );
      if ( promotionTypes.size() > 1 || promotionTypes.isEmpty() )
      {
        throw new Exception( "Heterogenous promotion types" );
      }

      String promotionTypeCode = promotionTypes.iterator().next().getCode();
      behaviors = getGamificationService().getBehaviorForSelectedPromotions( editMode ? alteredPromoIds : promoIds, existingPromosId, promotionTypeCode );
      badgebehaviorPromoList = getGamificationService().getBadgeBehaviorPromotions( promoIds, behaviors );
      request.setAttribute( "behaviorList", badgebehaviorPromoList );
      badgeLibraryList = getGamificationService().buildBadgeLibraryList();
      badgeBehJson.setBehaviors( badgebehaviorPromoList );
      badgeBehJson.setBadgeLibrary( badgeLibraryList );
      super.writeAsJsonToResponse( badgeBehJson, response );
    }
    catch( Exception e )
    {
      logger.error( "Error during populate behaviors", e );
      out.println( "0" );
    }
    return null;
  }

  /**
   * Method to get the badge library list Json while add a new row
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward(response put to stream for the ajax call)
   */
  public ActionForward populateBadgeLibraryAddRow( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PrintWriter out = response.getWriter();
    List<BadgeLibrary> badgeLibraryList = new ArrayList<BadgeLibrary>();
    BadgePromotionValueBean badgePromotionValueBean = new BadgePromotionValueBean();

    String promoIds = RequestUtils.getRequiredParamString( request, "promotionIds" );
    String badgeType = RequestUtils.getRequiredParamString( request, "badgeType" );

    try
    {
      if ( badgeType != null && badgeType.equalsIgnoreCase( "progress" ) )
      {
        badgeLibraryList = getGamificationService().buildBadgeLibraryListProgress();
        badgePromotionValueBean.setBadgeLibraryList( badgeLibraryList );
      }
      else if ( badgeType != null && badgeType.equalsIgnoreCase( "fileload" ) )
      {
        badgeLibraryList = getGamificationService().buildBadgeLibraryList();
        String isRecognitionType = getGamificationService().validatePromotionTypeOthers( promoIds );
        badgePromotionValueBean.setBadgeLibraryList( badgeLibraryList );
        badgePromotionValueBean.setShowFileLoadNoPromoDiv( isRecognitionType );
      }
      else
      {
        badgeLibraryList = getGamificationService().buildBadgeLibraryList();
        badgePromotionValueBean.setBadgeLibraryList( badgeLibraryList );
      }
      super.writeAsJsonToResponse( badgePromotionValueBean, response );
    }
    catch( Exception e )
    {
      logger.error( "Error during getting badge library", e );
      out.println( "0" );
    }

    return null;
  }

  /**
   * Method to validate the promotions type
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward(response put to stream for the ajax call)
   */

  public ActionForward validatePromotionType( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PrintWriter out = response.getWriter();
    String isRecognitionType = "N";

    try
    {
      String promoIds = RequestUtils.getRequiredParamString( request, "promotionIds" );
      String badgeType = request.getParameter( "badgeType" ) != null ? request.getParameter( "badgeType" ) : null;

      if ( promoIds.contains( "-1" ) )
      {
        List badgeTypeList = BadgeType.getList().subList( 2, 2 );
        request.setAttribute( "badgeTypeList", badgeTypeList );
      }
      if ( badgeType != null && "fileload".equals( badgeType ) )
      {
        isRecognitionType = getGamificationService().validatePromotionTypeOthers( promoIds );
      }
      else
      {
        isRecognitionType = getGamificationService().validatePromotionType( promoIds );
      }

      out.println( isRecognitionType );

      BadgeForm badgeForm = (BadgeForm)form;

      String[] promotionArray = null;
      promotionArray = com.biperf.core.service.util.StringUtil.parseCommaDelimitedList( promoIds );
      Long promotionId;
      for ( int i = 0; i < promotionArray.length; i++ )
      {
        promotionId = Long.parseLong( promotionArray[i] );
        Promotion promotion = getPromotionService().getPromotionById( promotionId );
        if ( promotion.isRecognitionPromotion() || promotion.isNominationPromotion() )
        {
          badgeForm.setPromotionTypeCode( PromotionType.RECOGNITION );
        }
      }
    }
    catch( Exception e )
    {
      logger.error( "Error during validate promotion type", e );
      out.println( "0" );
    }
    return null;
  }

  /**
   * Method to validate the promotion end date
   * 
   * @param mapping
   * @param form
   * @param request
   * @param responsef
   * @return ActionForward(response put to stream for the ajax call)
   */

  public ActionForward validatePromotionEndDate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PrintWriter out = response.getWriter();
    String isEndDateNull = "N";

    try
    {
      String promoIds = RequestUtils.getRequiredParamString( request, "promotionIds" );

      if ( !promoIds.equalsIgnoreCase( "-1" ) )
      {
        isEndDateNull = getGamificationService().validatePromotionEndDate( promoIds );
      }
      else
      {
        isEndDateNull = "Y";
      }

      out.println( isEndDateNull );

    }
    catch( Exception e )
    {
      logger.error( "Error during validate promotion type", e );
      out.println( "0" );
    }
    return null;
  }

  /**
   * Method to validate the badge setup name
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward(response put to stream for the ajax call)
   */

  public ActionForward validateBadgeSetupName( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PrintWriter out = response.getWriter();
    String isBadgeExists = "N";
    String isRecognitionOrNomination = "N";

    try
    {
      String badgeSetupName = RequestUtils.getRequiredParamString( request, "badgeName" ).trim();
      String badgeId = RequestUtils.getRequiredParamString( request, "badgeId" );
      String promoIds = request.getParameter( "promotionIds" ) != null ? request.getParameter( "promotionIds" ) : null;

      // isBadgeExists = getGamificationService().isBadgeNameExists( badgeSetupName, badgeId );
      isBadgeExists = getPromotionService().isPromotionNameUnique( badgeSetupName, badgeId != null && !badgeId.isEmpty() ? new Long( badgeId ) : new Long( 0 ) ) ? "N" : "Y";

      if ( promoIds != null )
      {
        String[] promotionArray = null;
        promotionArray = com.biperf.core.service.util.StringUtil.parseCommaDelimitedList( promoIds );
        Long promotionId;
        if ( promotionArray.length == 1 )
        {
          promotionId = Long.parseLong( promotionArray[0] );
          Promotion promotion = getPromotionService().getPromotionById( promotionId );
          if ( promotion != null && ( promotion.isRecognitionPromotion() || promotion.isNominationPromotion() ) )
          {
            isRecognitionOrNomination = "Y";
          }
        }
        else if ( promotionArray.length > 1 )
        {
          isRecognitionOrNomination = "Y";
        } // usually if the promotionArray greater than one, it is recognition only, as we allow
          // only
          // recognition promotions to be chosen in multiple option
      }

      JSONObject json = new JSONObject();
      json.put( "isBadgeExists", isBadgeExists );
      json.put( "isRecognitionOrNomination", isRecognitionOrNomination );

      out.println( json );

    }
    catch( Exception e )
    {
      logger.error( "Error during validate promotion type", e );
      out.println( "0" );
    }
    return null;
  }

  /**
   * Method to populate the levels if the promotion is merchandise type
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward(response put to stream for the ajax call)
   */
  public ActionForward populateLevels( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PrintWriter out = response.getWriter();
    String awardType = "";

    request.setAttribute( "isGoalQuest", "N" );
    request.setAttribute( "hasPartners", "N" );
    request.setAttribute( "isThrowDown", "N" );
    request.setAttribute( "stackStandingPayouts", "N" );
    try
    {
      String promoIds = RequestUtils.getRequiredParamString( request, "promotionIds" );
      if ( getGamificationService().getPromotionTypeById( promoIds ).equalsIgnoreCase( "goalQuest" ) )
      {
        request.setAttribute( "isGoalQuest", "Y" );

        AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PARTNER_GOAL_LEVELS ) );
        Promotion abstractPromotion = getPromotionService().getPromotionByIdWithAssociations( Long.parseLong( promoIds ), promoAssociationRequestCollection );
        GoalQuestPromotion promotion = null;
        if ( abstractPromotion instanceof GoalQuestPromotion )
        {
          promotion = (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( Long.parseLong( promoIds ), promoAssociationRequestCollection );
        }
        GoalBadgeRule goalLevelJson = new GoalBadgeRule();
        if ( promotion != null )
        {
          if ( promotion.isPartnersEnabled() )
          {
            request.setAttribute( "hasPartners", "Y" );
          }
          List<String> levelNames = new ArrayList<String>();
          for ( AbstractGoalLevel level : promotion.getGoalLevels() )
          {
            levelNames.add( level.getGoalLevelName() );
          }
          goalLevelJson.setLevelNames( levelNames );
          goalLevelJson.setPromotionName( promotion.getName() );
          goalLevelJson.setGoalQuest( true );
          goalLevelJson.setPartners( promotion.isPartnersEnabled() );
          goalLevelJson.setBadgeLibraryList( getGamificationService().buildBadgeLibraryListProgress() );
        }
        super.writeAsJsonToResponse( goalLevelJson, response );
        return null;
      }
      else if ( getGamificationService().getPromotionTypeById( promoIds ).equalsIgnoreCase( "throwDown" ) )
      {
        request.setAttribute( "isThrowDown", "Y" );

        AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.THROWDOWN_PAYOUTS ) );

        ThrowdownPromotion promotion = (ThrowdownPromotion)getPromotionService().getPromotionByIdWithAssociations( Long.parseLong( promoIds ), promoAssociationRequestCollection );

        StackStandingBadgeRule stackLevelJson = new StackStandingBadgeRule();
        if ( promotion != null )
        {
          Map<String, List<String>> nodePayouts = new HashMap<String, List<String>>();
          for ( StackStandingPayoutGroup stackStanding : promotion.getStackStandingPayoutGroups() )
          {
            List<String> payouts = new ArrayList<String>();

            List<StackStandingPayout> stackStandingPayoutList = new ArrayList<StackStandingPayout>();
            Set<StackStandingPayout> stackStandingPayoutSet = stackStanding.getStackStandingPayouts();
            stackStandingPayoutList.addAll( stackStandingPayoutSet );
            Collections.sort( stackStandingPayoutList, StackStandingPayout.PayoutIdComparator );

            for ( StackStandingPayout payout : stackStandingPayoutList )
            {
              payouts.add( payout.getStartStanding() + "-" + payout.getEndStanding() );
            }
            if ( !stackStanding.isHierarchyPayoutGroup() )
            {
              nodePayouts.put( stackStanding.getNodeType().getI18nName(), payouts );
            }
            else
            {
              nodePayouts.put( "All", payouts );
            }
          }
          stackLevelJson.setNodePayouts( nodePayouts );
          stackLevelJson.setPromotionName( promotion.getName() );
          stackLevelJson.setThrowDown( true );
          stackLevelJson.setStackStandingPayouts( !nodePayouts.isEmpty() );
          stackLevelJson.setBadgeLibraryList( getGamificationService().buildBadgeLibraryListProgress() );
        }
        super.writeAsJsonToResponse( stackLevelJson, response );
        return null;
      }

      awardType = getGamificationService().getAwardType( promoIds );
      if ( awardType.equalsIgnoreCase( "points" ) )
      {
        awardType = "A";
        out.println( awardType );
        return null;
      }
      else
      {
        awardType = "M";
        List badgeLibraryList = getGamificationService().buildBadgeLibraryList();

        List<BadgePromotionLevels> levelsList = getGamificationService().getPromotionLevels( promoIds );
        BadgeLevelsView badgeLevelJson = new BadgeLevelsView();
        badgeLevelJson.setLevels( levelsList );
        badgeLevelJson.setBadgeLibrary( badgeLibraryList );
        super.writeAsJsonToResponse( badgeLevelJson, response );
        return null;
      }

    }
    catch( Exception e )
    {
      logger.error( "Error during getting populate levels", e );
      out.println( "0" );
    }
    return null;
  }

  /**
   * Method to parse the form values while add/update a badge
   * 
   * @param BadgeForm
   * @param Badge
   * @param promotionId
   * @param fromFunction(either add or update)
   * @return ActionForward(response put to stream for the ajax call)
   */

  @SuppressWarnings( "unused" )
  public List<BadgeRule> parseBadgeRuleDetails( BadgeForm badgeForm, Badge badgeReturned, String promotionId, String fromFunction )
  {
    List<BadgeRule> badgeRulesList = new ArrayList<BadgeRule>();
    String badgeLibId;
    String badgeName;
    String badgeDescription;
    String partnerBadgeLibId;
    String partnerBadgeName;
    String partnerBadgeDescription;
    Long minQualifier;
    Long maxQualifier;
    String levelName;
    String stackLevelNodeName;
    String lastStackLevelNodeName = "";
    String stackLevelName;
    String overallLevelNodeName;
    String lastOverallLevelNodeName = "";
    String overallLevelName;
    Long countryId;
    String behaviorName;
    String behaviorCode;
    Long badgeRuleId;
    BadgeRule badgeRules = null;
    BadgeRule partnerBadgeRules = null;
    String badgeType = "";

    Long badgePoints = null;
    boolean eligibleForSweepstake = false;

    if ( fromFunction.equalsIgnoreCase( "update" ) )
    {
      badgeType = badgeReturned.getBadgeType().getCode();
    }
    else
    {
      badgeType = badgeForm.getBadgeType();
    }

    Promotion promo = null;
    if ( !StringUtils.isEmpty( promotionId ) )
    {
      promo = getPromotionService().getPromotionById( Long.parseLong( promotionId ) );
    }

    try
    {

      if ( badgeType.equalsIgnoreCase( "progress" ) )
      {
        String[] progressStringRows = badgeForm.getProgressStringRow();
        for ( String progressRow : progressStringRows )
        {
          StringTokenizer progressRowTokens = new StringTokenizer( progressRow, SPLIT_TOKEN );
          while ( progressRowTokens.hasMoreElements() )
          {

            String badgeRuleIdString = progressRowTokens.nextElement().toString();
            if ( fromFunction.equalsIgnoreCase( "update" ) )
            {
              badgeRuleId = Long.parseLong( badgeRuleIdString );
              if ( badgeRuleId > 0 )
              {
                badgeRules = getGamificationService().getBadgeRuleById( badgeRuleId );
              }
              else
              {
                badgeRules = new BadgeRule();
              }
            }
            else
            {
              badgeRules = new BadgeRule();
            }
            maxQualifier = Long.parseLong( progressRowTokens.nextElement().toString() );
            badgeLibId = progressRowTokens.nextElement().toString();

            Object badgeNameValue = progressRowTokens.nextElement();
            if ( badgeNameValue != null )
            {
              badgeName = badgeNameValue.toString();
            }
            else
            {
              badgeName = "";
            }
            Object badgePointsValue = progressRowTokens.nextElement();
            if ( badgePointsValue != null && !badgePointsValue.equals( "null" ) && new Long( badgePointsValue.toString() ) > 0 )
            {
              badgePoints = Long.parseLong( badgePointsValue.toString() );
            }
            else
            {
              badgePoints = null;
            }
            Object badgeSweepValue = progressRowTokens.nextElement();
            if ( badgeSweepValue.equals( "on" ) || badgeSweepValue.equals( "true" ) )
            {
              eligibleForSweepstake = true;
              promo.setSweepstakesActive( true );
            }
            else
            {
              eligibleForSweepstake = false;
            }
            Object badgeDescValue = progressRowTokens.nextElement();
            if ( badgeDescValue != null )
            {
              badgeDescription = badgeDescValue.toString();
            }
            else
            {
              badgeDescription = "";
            }

            while ( progressRowTokens.hasMoreElements() )
            {
              badgeDescription += SPLIT_TOKEN + progressRowTokens.nextElement();
            }
            if ( badgeLibId.indexOf( REPLACEMENT_TOKEN ) > 0 )
            {
              badgeLibId = badgeLibId.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
            }
            if ( badgeName.indexOf( REPLACEMENT_TOKEN ) > 0 )
            {
              badgeName = badgeName.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
            }
            if ( !badgeDescription.equalsIgnoreCase( "0" ) )
            {
              if ( badgeDescription.indexOf( REPLACEMENT_TOKEN ) > 0 )
              {
                badgeDescription = badgeDescription.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
              }
            }
            else
            {
              badgeDescription = "";
            }
            String badgeNameCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeName(), badgeName );
            if ( !badgeDescription.isEmpty() )
            {
              String badgeDescCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeDescription(), badgeDescription );
              badgeRules.setBadgeDescription( badgeDescCmAsset );
            }
            else
            {
              badgeRules.setBadgeDescription( badgeDescription );
            }

            badgeRules.setBadgePromotion( badgeReturned );
            badgeRules.setBadgeName( badgeNameCmAsset );
            badgeRules.setMaximumQualifier( maxQualifier );
            badgeRules.setBadgePoints( badgePoints );
            badgeRules.setEligibleForSweepstake( eligibleForSweepstake );
            badgeRules.setBadgeLibraryCMKey( badgeLibId );
          }

          badgeRulesList.add( badgeRules );

        }

      }
      else if ( badgeType.equalsIgnoreCase( "behavior" ) )
      {
        String[] behaviorStringRows = badgeForm.getBehaviorStringRow();
        for ( String behaviorRow : behaviorStringRows )
        {
          StringTokenizer behaviorRowTokens = new StringTokenizer( behaviorRow, SPLIT_TOKEN );
          while ( behaviorRowTokens.hasMoreElements() )
          {
            String badgeRuleIdString = behaviorRowTokens.nextElement().toString();
            if ( fromFunction.equalsIgnoreCase( "update" ) )
            {
              badgeRuleId = Long.parseLong( badgeRuleIdString );
              if ( badgeRuleId > 0 )
              {
                badgeRules = getGamificationService().getBadgeRuleById( badgeRuleId );
              }
              else
              {
                badgeRules = new BadgeRule();
              }
            }
            else
            {
              badgeRules = new BadgeRule();
            }
            behaviorCode = behaviorRowTokens.nextElement().toString();
            behaviorName = behaviorRowTokens.nextElement().toString();
            badgeLibId = behaviorRowTokens.nextElement().toString();
            Object badgeNameValue = behaviorRowTokens.nextElement();
            if ( badgeNameValue != null )
            {
              badgeName = badgeNameValue.toString();
            }
            else
            {
              badgeName = "";
            }
            Object badgePointsValue = behaviorRowTokens.nextElement();
            if ( badgePointsValue != null && !badgePointsValue.equals( "null" ) && new Long( badgePointsValue.toString() ) > 0 )
            {
              badgePoints = Long.parseLong( badgePointsValue.toString() );
            }
            else
            {
              badgePoints = null;
            }
            Object badgeSweepValue = behaviorRowTokens.nextElement();
            if ( badgeSweepValue.equals( "on" ) || badgeSweepValue.equals( "true" ) )
            {
              eligibleForSweepstake = true;
              promo.setSweepstakesActive( true );
            }
            else
            {
              eligibleForSweepstake = false;
            }
            Object badgeDescValue = behaviorRowTokens.nextElement();
            if ( badgeDescValue != null )
            {
              badgeDescription = badgeDescValue.toString();
            }
            else
            {
              badgeDescription = "";
            }

            while ( behaviorRowTokens.hasMoreElements() )
            {
              badgeDescription += SPLIT_TOKEN + behaviorRowTokens.nextElement();
            }
            if ( badgeLibId.indexOf( REPLACEMENT_TOKEN ) > 0 )
            {
              badgeLibId = badgeLibId.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
            }
            if ( badgeName.indexOf( REPLACEMENT_TOKEN ) > 0 )
            {
              badgeName = badgeName.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
            }
            if ( !badgeDescription.equalsIgnoreCase( "0" ) )
            {
              if ( badgeDescription.indexOf( REPLACEMENT_TOKEN ) > 0 )
              {
                badgeDescription = badgeDescription.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
              }
            }
            else
            {
              badgeDescription = "";
            }
            String badgeNameCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeName(), badgeName );
            if ( !badgeDescription.isEmpty() )
            {
              String badgeDescCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeDescription(), badgeDescription );
              badgeRules.setBadgeDescription( badgeDescCmAsset );
            }
            else
            {
              badgeRules.setBadgeDescription( badgeDescription );
            }

            badgeRules.setBadgePromotion( badgeReturned );
            badgeRules.setBadgeName( badgeNameCmAsset );
            badgeRules.setBehaviorName( behaviorCode );
            badgeRules.setBadgePoints( badgePoints );
            badgeRules.setEligibleForSweepstake( eligibleForSweepstake );
            badgeRules.setBadgeLibraryCMKey( badgeLibId );
          }

          badgeRulesList.add( badgeRules );
        }

      }
      else if ( badgeType.equalsIgnoreCase( "earned" ) && promo != null && !promo.isGoalQuestOrChallengePointPromotion() && !promo.isThrowdownPromotion() )
      {
        String awardType = getGamificationService().getAwardType( promotionId );
        if ( awardType.equalsIgnoreCase( "points" ) )
        {
          // code for point range parsing
          String[] pointRangeStringRows = badgeForm.getPointRangeStringRow();
          for ( String pointRangeRow : pointRangeStringRows )
          {
            StringTokenizer pointRangeRowTokens = new StringTokenizer( pointRangeRow, SPLIT_TOKEN );
            while ( pointRangeRowTokens.hasMoreElements() )
            {

              String badgeRuleIdString = pointRangeRowTokens.nextElement().toString();
              if ( fromFunction.equalsIgnoreCase( "update" ) )
              {
                badgeRuleId = Long.parseLong( badgeRuleIdString );
                if ( badgeRuleId > 0 )
                {
                  badgeRules = getGamificationService().getBadgeRuleById( badgeRuleId );
                }
                else
                {
                  badgeRules = new BadgeRule();
                }
              }
              else
              {
                badgeRules = new BadgeRule();
              }
              minQualifier = Long.parseLong( pointRangeRowTokens.nextElement().toString() );
              maxQualifier = Long.parseLong( pointRangeRowTokens.nextElement().toString() );
              badgeLibId = pointRangeRowTokens.nextElement().toString();

              Object badgeNameValue = pointRangeRowTokens.nextElement();
              if ( badgeNameValue != null )
              {
                badgeName = badgeNameValue.toString();
              }
              else
              {
                badgeName = "";
              }
              Object badgeDescValue = pointRangeRowTokens.nextElement();
              if ( badgeDescValue != null )
              {
                badgeDescription = badgeDescValue.toString();
              }
              else
              {
                badgeDescription = "";
              }

              while ( pointRangeRowTokens.hasMoreElements() )
              {
                badgeDescription += SPLIT_TOKEN + pointRangeRowTokens.nextElement();
              }
              if ( badgeLibId.indexOf( REPLACEMENT_TOKEN ) > 0 )
              {
                badgeLibId = badgeLibId.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
              }
              if ( badgeName.indexOf( REPLACEMENT_TOKEN ) > 0 )
              {
                badgeName = badgeName.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
              }
              if ( !badgeDescription.equalsIgnoreCase( "0" ) )
              {
                if ( badgeDescription.indexOf( REPLACEMENT_TOKEN ) > 0 )
                {
                  badgeDescription = badgeDescription.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                }
              }
              else
              {
                badgeDescription = "";
              }
              String badgeNameCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeName(), badgeName );
              if ( !badgeDescription.isEmpty() )
              {
                String badgeDescCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeDescription(), badgeDescription );
                badgeRules.setBadgeDescription( badgeDescCmAsset );
              }
              else
              {
                badgeRules.setBadgeDescription( badgeDescription );
              }

              badgeRules.setBadgePromotion( badgeReturned );
              badgeRules.setBadgeName( badgeNameCmAsset );
              badgeRules.setMinimumQualifier( minQualifier );
              badgeRules.setMaximumQualifier( maxQualifier );
              badgeRules.setBadgeLibraryCMKey( badgeLibId );
            }

            badgeRulesList.add( badgeRules );
          }

        }
        else
        {
          // code for level parsing

          String[] levelStringRows = badgeForm.getLevelStringRow();
          for ( String levelRow : levelStringRows )
          {
            StringTokenizer levelRowTokens = new StringTokenizer( levelRow, SPLIT_TOKEN );
            while ( levelRowTokens.hasMoreElements() )
            {
              String badgeRuleIdString = levelRowTokens.nextElement().toString();
              if ( fromFunction.equalsIgnoreCase( "update" ) )
              {
                badgeRuleId = Long.parseLong( badgeRuleIdString );
                if ( badgeRuleId > 0 )
                {
                  badgeRules = getGamificationService().getBadgeRuleById( badgeRuleId );
                }
                else
                {
                  badgeRules = new BadgeRule();
                }
              }
              else
              {
                badgeRules = new BadgeRule();
              }
              countryId = Long.parseLong( levelRowTokens.nextElement().toString() );
              levelName = levelRowTokens.nextElement().toString();
              badgeLibId = levelRowTokens.nextElement().toString();
              Object badgeNameValue = levelRowTokens.nextElement();
              if ( badgeNameValue != null )
              {
                badgeName = badgeNameValue.toString();
              }
              else
              {
                badgeName = "";
              }
              Object badgeDescValue = levelRowTokens.nextElement();
              if ( badgeDescValue != null )
              {
                badgeDescription = badgeDescValue.toString();
              }
              else
              {
                badgeDescription = "";
              }

              while ( levelRowTokens.hasMoreElements() )
              {
                badgeDescription += SPLIT_TOKEN + levelRowTokens.nextElement();
              }
              if ( badgeLibId.indexOf( REPLACEMENT_TOKEN ) > 0 )
              {
                badgeLibId = badgeLibId.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
              }
              if ( badgeName.indexOf( REPLACEMENT_TOKEN ) > 0 )
              {
                badgeName = badgeName.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
              }
              if ( !badgeDescription.equalsIgnoreCase( "0" ) )
              {
                if ( badgeDescription.indexOf( REPLACEMENT_TOKEN ) > 0 )
                {
                  badgeDescription = badgeDescription.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                }
              }
              else
              {
                badgeDescription = "";
              }
              String badgeNameCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeName(), badgeName );
              if ( !badgeDescription.isEmpty() )
              {
                String badgeDescCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeDescription(), badgeDescription );
                badgeRules.setBadgeDescription( badgeDescCmAsset );
              }
              else
              {
                badgeRules.setBadgeDescription( badgeDescription );
              }

              badgeRules.setBadgePromotion( badgeReturned );
              badgeRules.setBadgeName( badgeNameCmAsset );
              badgeRules.setCountryId( countryId );
              badgeRules.setLevelName( levelName );
              badgeRules.setBadgeLibraryCMKey( badgeLibId );
            }

            badgeRulesList.add( badgeRules );
          }
        }
      }
      else if ( badgeType.equalsIgnoreCase( "earned" ) && promo != null && promo.isGoalQuestOrChallengePointPromotion() )
      {
        String[] levelStringRows = badgeForm.getLevelStringRow();
        if ( fromFunction.equalsIgnoreCase( "update" ) )
        {
          for ( String levelRow : levelStringRows )
          {
            StringTokenizer levelRowTokens = new StringTokenizer( levelRow, SPLIT_TOKEN );
            while ( levelRowTokens.hasMoreElements() )
            {
              // get badge rule id, new badge name and new badge desc
              String badgeRuleIdString = levelRowTokens.nextElement().toString();
              Object badgeNameValue = levelRowTokens.nextElement();
              Object badgeDescValue = levelRowTokens.nextElement();
              badgeRules = getGamificationService().getBadgeRuleById( Long.parseLong( badgeRuleIdString ) );

              // get badge name and description
              if ( badgeNameValue != null )
              {
                badgeName = badgeNameValue.toString();
              }
              else
              {
                badgeName = "";
              }
              if ( badgeDescValue != null )
              {
                badgeDescription = badgeDescValue.toString();
              }
              else
              {
                badgeDescription = "";
              }
              if ( badgeName.indexOf( REPLACEMENT_TOKEN ) > 0 )
              {
                badgeName = badgeName.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
              }
              if ( !badgeDescription.equalsIgnoreCase( "0" ) )
              {
                if ( badgeDescription.indexOf( REPLACEMENT_TOKEN ) > 0 )
                {
                  badgeDescription = badgeDescription.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                }
              }
              else
              {
                badgeDescription = "";
              }
              String badgeNameCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeName(), badgeName );
              if ( !badgeDescription.isEmpty() )
              {
                String badgeDescCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeDescription(), badgeDescription );
                badgeRules.setBadgeDescription( badgeDescCmAsset );
              }
              else
              {
                badgeRules.setBadgeDescription( badgeDescription );
              }
              badgeRules.setBadgePromotion( badgeReturned );
              badgeRules.setBadgeName( badgeNameCmAsset );

              if ( promo.isPartnersEnabled() )
              {
                // get partner badge ruleid, new partner badgename and new partner description
                String partnerBadgeRuleIdString = levelRowTokens.nextElement().toString();
                Object partnerBadgeNameValue = levelRowTokens.nextElement();
                Object partnerBadgeDescValue = levelRowTokens.nextElement();
                partnerBadgeRules = getGamificationService().getBadgeRuleById( Long.parseLong( partnerBadgeRuleIdString ) );

                // update partner badge name and description
                if ( partnerBadgeNameValue != null )
                {
                  partnerBadgeName = partnerBadgeNameValue.toString();
                }
                else
                {
                  partnerBadgeName = "";
                }
                if ( partnerBadgeDescValue != null )
                {
                  partnerBadgeDescription = partnerBadgeDescValue.toString();
                }
                else
                {
                  partnerBadgeDescription = "";
                }

                if ( partnerBadgeName.indexOf( REPLACEMENT_TOKEN ) > 0 )
                {
                  partnerBadgeName = partnerBadgeName.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                }
                if ( !partnerBadgeDescription.equalsIgnoreCase( "0" ) )
                {
                  if ( partnerBadgeDescription.indexOf( REPLACEMENT_TOKEN ) > 0 )
                  {
                    partnerBadgeDescription = partnerBadgeDescription.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                  }
                }
                else
                {
                  partnerBadgeDescription = "";
                }

                String partnerBadgeNameCmAsset = getGamificationService().saveRulesCmText( partnerBadgeRules.getBadgeName(), partnerBadgeName );
                if ( !partnerBadgeDescription.isEmpty() )
                {
                  String partnerBadgeDescCmAsset = getGamificationService().saveRulesCmText( partnerBadgeRules.getBadgeDescription(), partnerBadgeDescription );
                  partnerBadgeRules.setBadgeDescription( partnerBadgeDescCmAsset );
                }
                else
                {
                  partnerBadgeRules.setBadgeDescription( partnerBadgeDescription );
                }
                partnerBadgeRules.setBadgePromotion( badgeReturned );
                partnerBadgeRules.setBadgeName( partnerBadgeNameCmAsset );
                badgeRulesList.add( partnerBadgeRules );
              }
              else
              {
                // skip the empty partner values
                while ( levelRowTokens.hasMoreElements() )
                {
                  Object partnerBadgeLibIdValue = levelRowTokens.nextElement();
                  Object partnerBadgeNameValue = levelRowTokens.nextElement();
                  Object partnerBadgeDescValue = levelRowTokens.nextElement();
                }
              }
              badgeRulesList.add( badgeRules );
            }
          }
        }
        else
        {
          // create new badge rules
          for ( String levelRow : levelStringRows )
          {
            StringTokenizer levelRowTokens = new StringTokenizer( levelRow, SPLIT_TOKEN );
            while ( levelRowTokens.hasMoreElements() )
            {
              String badgeRuleIdString = levelRowTokens.nextElement().toString();
              countryId = Long.parseLong( levelRowTokens.nextElement().toString() );
              levelName = levelRowTokens.nextElement().toString();
              if ( badgeRuleIdString != null && badgeRuleIdString.equals( "new" ) )
              {
                badgeRules = new BadgeRule();
                partnerBadgeRules = new BadgeRule();
                badgeLibId = levelRowTokens.nextElement().toString();
                Object badgeNameValue = levelRowTokens.nextElement();
                if ( badgeNameValue != null )
                {
                  badgeName = badgeNameValue.toString();
                }
                else
                {
                  badgeName = "";
                }
                Object badgeDescValue = levelRowTokens.nextElement();
                if ( badgeDescValue != null )
                {
                  badgeDescription = badgeDescValue.toString();
                }
                else
                {
                  badgeDescription = "";
                }

                if ( badgeLibId.indexOf( REPLACEMENT_TOKEN ) > 0 )
                {
                  badgeLibId = badgeLibId.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                }
                if ( badgeName.indexOf( REPLACEMENT_TOKEN ) > 0 )
                {
                  badgeName = badgeName.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                }
                if ( !badgeDescription.equalsIgnoreCase( "0" ) )
                {
                  if ( badgeDescription.indexOf( REPLACEMENT_TOKEN ) > 0 )
                  {
                    badgeDescription = badgeDescription.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                  }
                }
                else
                {
                  badgeDescription = "";
                }
                String badgeNameCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeName(), badgeName );
                if ( !badgeDescription.isEmpty() )
                {
                  String badgeDescCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeDescription(), badgeDescription );
                  badgeRules.setBadgeDescription( badgeDescCmAsset );
                }
                else
                {
                  badgeRules.setBadgeDescription( badgeDescription );
                }
                badgeRules.setBadgePromotion( badgeReturned );
                badgeRules.setBadgeName( badgeNameCmAsset );
                badgeRules.setCountryId( countryId );
                badgeRules.setLevelName( levelName );
                badgeRules.setBadgeLibraryCMKey( badgeLibId );

                if ( promo != null && promo.isPartnersEnabled() )
                {
                  partnerBadgeLibId = levelRowTokens.nextElement().toString();
                  Object partnerBadgeNameValue = levelRowTokens.nextElement();
                  if ( partnerBadgeNameValue != null )
                  {
                    partnerBadgeName = partnerBadgeNameValue.toString();
                  }
                  else
                  {
                    partnerBadgeName = "";
                  }
                  Object partnerBadgeDescValue = levelRowTokens.nextElement();
                  if ( partnerBadgeDescValue != null )
                  {
                    partnerBadgeDescription = partnerBadgeDescValue.toString();
                  }
                  else
                  {
                    partnerBadgeDescription = "";
                  }
                  if ( partnerBadgeLibId.indexOf( REPLACEMENT_TOKEN ) > 0 )
                  {
                    partnerBadgeLibId = partnerBadgeLibId.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                  }
                  if ( partnerBadgeName.indexOf( REPLACEMENT_TOKEN ) > 0 )
                  {
                    partnerBadgeName = partnerBadgeName.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                  }
                  if ( !partnerBadgeDescription.equalsIgnoreCase( "0" ) )
                  {
                    if ( partnerBadgeDescription.indexOf( REPLACEMENT_TOKEN ) > 0 )
                    {
                      partnerBadgeDescription = partnerBadgeDescription.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                    }
                  }
                  else
                  {
                    partnerBadgeDescription = "";
                  }
                  String partnerBadgeNameCmAsset = getGamificationService().saveRulesCmText( partnerBadgeRules.getBadgeName(), partnerBadgeName );
                  if ( !partnerBadgeDescription.isEmpty() )
                  {
                    String partnerBadgeDescCmAsset = getGamificationService().saveRulesCmText( partnerBadgeRules.getBadgeDescription(), partnerBadgeDescription );
                    partnerBadgeRules.setBadgeDescription( partnerBadgeDescCmAsset );
                  }
                  else
                  {
                    partnerBadgeRules.setBadgeDescription( partnerBadgeDescription );
                  }
                  partnerBadgeRules.setBadgePromotion( badgeReturned );
                  partnerBadgeRules.setLevelName( levelName );
                  partnerBadgeRules.setBadgeLibraryCMKey( partnerBadgeLibId );
                  partnerBadgeRules.setBadgeName( partnerBadgeNameCmAsset );
                  partnerBadgeRules.setParticipantType( ParticipantType.PARTNER );
                  badgeRulesList.add( partnerBadgeRules );
                }
                else
                {
                  // skip the empty partner values
                  while ( levelRowTokens.hasMoreElements() )
                  {
                    Object partnerBadgeLibIdValue = levelRowTokens.nextElement();
                    Object partnerBadgeNameValue = levelRowTokens.nextElement();
                    Object partnerBadgeDescValue = levelRowTokens.nextElement();
                  }
                }
                badgeRulesList.add( badgeRules );
              }
              else if ( badgeRuleIdString != null && badgeRuleIdString.equals( "edit" ) )
              {
                String goalOrPartner = levelRowTokens.nextElement().toString();
                badgeRules = new BadgeRule();
                partnerBadgeRules = new BadgeRule();

                if ( goalOrPartner.equals( "none" ) )
                {
                  badgeLibId = levelRowTokens.nextElement().toString();
                  Object badgeNameValue = levelRowTokens.nextElement();
                  if ( badgeNameValue != null )
                  {
                    badgeName = badgeNameValue.toString();
                  }
                  else
                  {
                    badgeName = "";
                  }
                  Object badgeDescValue = levelRowTokens.nextElement();
                  if ( badgeDescValue != null )
                  {
                    badgeDescription = badgeDescValue.toString();
                  }
                  else
                  {
                    badgeDescription = "";
                  }

                  if ( badgeLibId.indexOf( REPLACEMENT_TOKEN ) > 0 )
                  {
                    badgeLibId = badgeLibId.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                  }
                  if ( badgeName.indexOf( REPLACEMENT_TOKEN ) > 0 )
                  {
                    badgeName = badgeName.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                  }
                  if ( !badgeDescription.equalsIgnoreCase( "0" ) )
                  {
                    if ( badgeDescription.indexOf( REPLACEMENT_TOKEN ) > 0 )
                    {
                      badgeDescription = badgeDescription.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                    }
                  }
                  else
                  {
                    badgeDescription = "";
                  }
                  String badgeNameCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeName(), badgeName );
                  if ( !badgeDescription.isEmpty() )
                  {
                    String badgeDescCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeDescription(), badgeDescription );
                    badgeRules.setBadgeDescription( badgeDescCmAsset );
                  }
                  else
                  {
                    badgeRules.setBadgeDescription( badgeDescription );
                  }
                  badgeRules.setBadgePromotion( badgeReturned );
                  badgeRules.setBadgeName( badgeNameCmAsset );
                  badgeRules.setCountryId( countryId );
                  badgeRules.setLevelName( levelName );
                  badgeRules.setBadgeLibraryCMKey( badgeLibId );

                  badgeRulesList.add( badgeRules );

                  // skip the empty partner values
                  while ( levelRowTokens.hasMoreElements() )
                  {
                    levelRowTokens.nextElement();
                    levelRowTokens.nextElement();
                    levelRowTokens.nextElement();
                  }

                }
                else if ( goalOrPartner.equals( "partner" ) )
                {
                  levelRowTokens.nextElement();
                  levelRowTokens.nextElement();
                  levelRowTokens.nextElement();

                  partnerBadgeLibId = levelRowTokens.nextElement().toString();
                  Object partnerBadgeNameValue = levelRowTokens.nextElement();
                  if ( partnerBadgeNameValue != null )
                  {
                    partnerBadgeName = partnerBadgeNameValue.toString();
                  }
                  else
                  {
                    partnerBadgeName = "";
                  }
                  Object partnerBadgeDescValue = levelRowTokens.nextElement();
                  if ( partnerBadgeDescValue != null )
                  {
                    partnerBadgeDescription = partnerBadgeDescValue.toString();
                  }
                  else
                  {
                    partnerBadgeDescription = "";
                  }
                  if ( partnerBadgeLibId.indexOf( REPLACEMENT_TOKEN ) > 0 )
                  {
                    partnerBadgeLibId = partnerBadgeLibId.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                  }
                  if ( partnerBadgeName.indexOf( REPLACEMENT_TOKEN ) > 0 )
                  {
                    partnerBadgeName = partnerBadgeName.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                  }
                  if ( !partnerBadgeDescription.equalsIgnoreCase( "0" ) )
                  {
                    if ( partnerBadgeDescription.indexOf( REPLACEMENT_TOKEN ) > 0 )
                    {
                      partnerBadgeDescription = partnerBadgeDescription.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                    }
                  }
                  else
                  {
                    partnerBadgeDescription = "";
                  }
                  String partnerBadgeNameCmAsset = getGamificationService().saveRulesCmText( partnerBadgeRules.getBadgeName(), partnerBadgeName );
                  if ( !partnerBadgeDescription.isEmpty() )
                  {
                    String partnerBadgeDescCmAsset = getGamificationService().saveRulesCmText( partnerBadgeRules.getBadgeDescription(), partnerBadgeDescription );
                    partnerBadgeRules.setBadgeDescription( partnerBadgeDescCmAsset );
                  }
                  else
                  {
                    partnerBadgeRules.setBadgeDescription( partnerBadgeDescription );
                  }
                  partnerBadgeRules.setBadgePromotion( badgeReturned );
                  partnerBadgeRules.setLevelName( levelName );
                  partnerBadgeRules.setBadgeLibraryCMKey( partnerBadgeLibId );
                  partnerBadgeRules.setBadgeName( partnerBadgeNameCmAsset );
                  partnerBadgeRules.setParticipantType( ParticipantType.PARTNER );
                  badgeRulesList.add( partnerBadgeRules );

                }
              }
            }
          }
        }
      }
      else if ( badgeType.equalsIgnoreCase( "earned" ) && promo != null && promo.isThrowdownPromotion() )
      {
        String[] stackLevelStringRows = badgeForm.getStackLevelStringRow();
        if ( stackLevelStringRows != null && stackLevelStringRows.length > 0 )
        {
          for ( String stackLevelRow : stackLevelStringRows )
          {
            StringTokenizer stackLevelRowTokens = new StringTokenizer( stackLevelRow, SPLIT_TOKEN );
            while ( stackLevelRowTokens.hasMoreElements() )
            {
              String badgeRuleIdString = stackLevelRowTokens.nextElement().toString();
              if ( fromFunction.equalsIgnoreCase( "update" ) )
              {
                badgeRuleId = Long.parseLong( badgeRuleIdString );
                if ( badgeRuleId > 0 )
                {
                  badgeRules = getGamificationService().getBadgeRuleById( badgeRuleId );
                }
                else
                {
                  badgeRules = new BadgeRule();
                }
              }
              else
              {
                badgeRules = new BadgeRule();
              }
              countryId = Long.parseLong( stackLevelRowTokens.nextElement().toString() );
              stackLevelNodeName = stackLevelRowTokens.nextElement().toString();
              if ( stackLevelNodeName.trim().length() < 1 )
              {
                stackLevelNodeName = lastStackLevelNodeName;
              }
              else
              {
                lastStackLevelNodeName = stackLevelNodeName;
              }
              stackLevelName = stackLevelRowTokens.nextElement().toString();
              StringTokenizer rankLevelRowTokens = new StringTokenizer( stackLevelName, "-" );
              minQualifier = Long.parseLong( rankLevelRowTokens.nextElement().toString() );
              maxQualifier = Long.parseLong( rankLevelRowTokens.nextElement().toString() );

              badgeLibId = stackLevelRowTokens.nextElement().toString();
              Object badgeNameValue = stackLevelRowTokens.nextElement();
              if ( badgeNameValue != null )
              {
                badgeName = badgeNameValue.toString();
              }
              else
              {
                badgeName = "";
              }
              Object badgeDescValue = stackLevelRowTokens.nextElement();
              if ( badgeDescValue != null )
              {
                badgeDescription = badgeDescValue.toString();
              }
              else
              {
                badgeDescription = "";
              }

              // while ( levelRowTokens.hasMoreElements() )
              // {
              // badgeDescription += SPLIT_TOKEN + levelRowTokens.nextElement();
              // }
              if ( badgeLibId.indexOf( REPLACEMENT_TOKEN ) > 0 )
              {
                badgeLibId = badgeLibId.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
              }
              if ( badgeName.indexOf( REPLACEMENT_TOKEN ) > 0 )
              {
                badgeName = badgeName.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
              }
              if ( !badgeDescription.equalsIgnoreCase( "0" ) )
              {
                if ( badgeDescription.indexOf( REPLACEMENT_TOKEN ) > 0 )
                {
                  badgeDescription = badgeDescription.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                }
              }
              else
              {
                badgeDescription = "";
              }
              String badgeNameCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeName(), badgeName );
              if ( !badgeDescription.isEmpty() )
              {
                String badgeDescCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeDescription(), badgeDescription );
                badgeRules.setBadgeDescription( badgeDescCmAsset );
              }
              else
              {
                badgeRules.setBadgeDescription( badgeDescription );
              }

              badgeRules.setBadgePromotion( badgeReturned );
              badgeRules.setBadgeName( badgeNameCmAsset );
              badgeRules.setCountryId( countryId );
              badgeRules.setLevelName( stackLevelNodeName );
              badgeRules.setMinimumQualifier( minQualifier );
              badgeRules.setMaximumQualifier( maxQualifier );
              badgeRules.setBadgeLibraryCMKey( badgeLibId );
              badgeRules.setLevelType( BadgeLevelType.lookup( BadgeLevelType.STACK_STAND ) );

              badgeRulesList.add( badgeRules );
            }

          }
        }
        // Add BadgeRule for OverallBadge
        String[] overallLevelStringRows = badgeForm.getOverallLevelStringRow();
        if ( overallLevelStringRows != null && overallLevelStringRows.length > 0 )
        {
          for ( String overallLevelRow : overallLevelStringRows )
          {

            StringTokenizer overallLevelRowTokens = new StringTokenizer( overallLevelRow, SPLIT_TOKEN );
            while ( overallLevelRowTokens.hasMoreElements() )
            {

              String badgeRuleIdString = overallLevelRowTokens.nextElement().toString();
              if ( fromFunction.equalsIgnoreCase( "update" ) )
              {
                badgeRuleId = Long.parseLong( badgeRuleIdString );
                if ( badgeRuleId > 0 )
                {
                  badgeRules = getGamificationService().getBadgeRuleById( badgeRuleId );
                }
                else
                {
                  badgeRules = new BadgeRule();
                }
              }
              else
              {
                badgeRules = new BadgeRule();
              }
              countryId = Long.parseLong( overallLevelRowTokens.nextElement().toString() );
              overallLevelNodeName = overallLevelRowTokens.nextElement().toString();
              if ( overallLevelNodeName.trim().length() < 1 )
              {
                overallLevelNodeName = lastOverallLevelNodeName;
              }
              else
              {
                lastOverallLevelNodeName = overallLevelNodeName;
              }
              overallLevelName = overallLevelRowTokens.nextElement().toString();
              StringTokenizer overallRankLevelRowTokens = new StringTokenizer( overallLevelName, "-" );
              minQualifier = Long.parseLong( overallRankLevelRowTokens.nextElement().toString() );
              maxQualifier = Long.parseLong( overallRankLevelRowTokens.nextElement().toString() );

              badgeLibId = overallLevelRowTokens.nextElement().toString();
              Object badgeNameValue = overallLevelRowTokens.nextElement();
              if ( badgeNameValue != null )
              {
                badgeName = badgeNameValue.toString();
              }
              else
              {
                badgeName = "";
              }
              Object badgeDescValue = overallLevelRowTokens.nextElement();
              if ( badgeDescValue != null )
              {
                badgeDescription = badgeDescValue.toString();
              }
              else
              {
                badgeDescription = "";
              }

              // while ( levelRowTokens.hasMoreElements() )
              // {
              // badgeDescription += SPLIT_TOKEN + levelRowTokens.nextElement();
              // }
              if ( badgeLibId.indexOf( REPLACEMENT_TOKEN ) > 0 )
              {
                badgeLibId = badgeLibId.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
              }
              if ( badgeName.indexOf( REPLACEMENT_TOKEN ) > 0 )
              {
                badgeName = badgeName.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
              }
              if ( !badgeDescription.equalsIgnoreCase( "0" ) )
              {
                if ( badgeDescription.indexOf( REPLACEMENT_TOKEN ) > 0 )
                {
                  badgeDescription = badgeDescription.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
                }
              }
              else
              {
                badgeDescription = "";
              }
              String badgeNameCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeName(), badgeName );
              if ( !badgeDescription.isEmpty() )
              {
                String badgeDescCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeDescription(), badgeDescription );
                badgeRules.setBadgeDescription( badgeDescCmAsset );
              }
              else
              {
                badgeRules.setBadgeDescription( badgeDescription );
              }

              badgeRules.setBadgePromotion( badgeReturned );
              badgeRules.setBadgeName( badgeNameCmAsset );
              badgeRules.setCountryId( countryId );
              badgeRules.setLevelName( overallLevelNodeName );
              badgeRules.setMinimumQualifier( minQualifier );
              badgeRules.setMaximumQualifier( maxQualifier );
              badgeRules.setBadgeLibraryCMKey( badgeLibId );
              badgeRules.setLevelType( BadgeLevelType.lookup( BadgeLevelType.OVERALL ) );

              badgeRulesList.add( badgeRules );

            }

          }
        }
        // Add badge Rule for Undefeated Badge
        String undefeatedBadge = badgeForm.getUndefeatedBadgeStringRow();

        if ( undefeatedBadge != null && !undefeatedBadge.equals( "" ) )
        {
          StringTokenizer undefeatedBadgeTokens = new StringTokenizer( undefeatedBadge, SPLIT_TOKEN );
          while ( undefeatedBadgeTokens.hasMoreElements() )
          {

            String badgeRuleIdString = undefeatedBadgeTokens.nextElement().toString();
            if ( fromFunction.equalsIgnoreCase( "update" ) )
            {
              badgeRuleId = Long.parseLong( badgeRuleIdString );
              if ( badgeRuleId > 0 )
              {
                badgeRules = getGamificationService().getBadgeRuleById( badgeRuleId );
              }
              else
              {
                badgeRules = new BadgeRule();
              }
            }
            else
            {
              badgeRules = new BadgeRule();
            }
            countryId = Long.parseLong( undefeatedBadgeTokens.nextElement().toString() );

            // String undefeatedBadgeLevelName = undefeatedBadgeTokens.nextElement().toString();

            badgeLibId = undefeatedBadgeTokens.nextElement().toString();

            Object badgeNameValue = undefeatedBadgeTokens.nextElement();
            if ( badgeNameValue != null )
            {
              badgeName = badgeNameValue.toString();
            }
            else
            {
              badgeName = "";
            }
            Object badgeDescValue = undefeatedBadgeTokens.nextElement();
            if ( badgeDescValue != null )
            {
              badgeDescription = badgeDescValue.toString();
            }
            else
            {
              badgeDescription = "";
            }
            if ( badgeLibId.indexOf( REPLACEMENT_TOKEN ) > 0 )
            {
              badgeLibId = badgeLibId.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
            }
            if ( badgeName.indexOf( REPLACEMENT_TOKEN ) > 0 )
            {
              badgeName = badgeName.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
            }
            if ( !badgeDescription.equalsIgnoreCase( "0" ) )
            {
              if ( badgeDescription.indexOf( REPLACEMENT_TOKEN ) > 0 )
              {
                badgeDescription = badgeDescription.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
              }
            }
            else
            {
              badgeDescription = "";
            }
            String badgeNameCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeName(), badgeName );
            if ( !badgeDescription.isEmpty() )
            {
              String badgeDescCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeDescription(), badgeDescription );
              badgeRules.setBadgeDescription( badgeDescCmAsset );
            }
            else
            {
              badgeRules.setBadgeDescription( badgeDescription );
            }

            badgeRules.setBadgePromotion( badgeReturned );
            badgeRules.setBadgeName( badgeNameCmAsset );
            badgeRules.setCountryId( countryId );
            // badgeRules.setLevelName( overallBadgeLevelName );
            badgeRules.setBadgeLibraryCMKey( badgeLibId );
            badgeRules.setLevelType( BadgeLevelType.lookup( BadgeLevelType.UNDEFEATED ) );

            badgeRulesList.add( badgeRules );
          }

        }

      }
      // check badgeType is file load
      else if ( badgeType.equalsIgnoreCase( "fileload" ) )
      {
        String[] fileLoadStringRows = badgeForm.getFileLoadStringRow();
        for ( String fileLoadRow : fileLoadStringRows )
        {
          StringTokenizer fileLoadRowTokens = new StringTokenizer( fileLoadRow, SPLIT_TOKEN );

          while ( fileLoadRowTokens.hasMoreElements() )
          {
            String badgeRuleIdString = fileLoadRowTokens.nextElement().toString();
            if ( fromFunction.equalsIgnoreCase( "update" ) )
            {
              badgeRuleId = Long.parseLong( badgeRuleIdString );
              if ( badgeRuleId > 0 )
              {
                badgeRules = getGamificationService().getBadgeRuleById( badgeRuleId );
              }
              else
              {
                badgeRules = new BadgeRule();
              }
            }
            else
            {
              badgeRules = new BadgeRule();
            }
            badgeLibId = fileLoadRowTokens.nextElement().toString();

            Object badgeNameValue = fileLoadRowTokens.nextElement();
            if ( badgeNameValue != null )
            {
              badgeName = badgeNameValue.toString();
            }
            else
            {
              badgeName = "";
            }
            if ( promo == null || promo != null && ( promo.isRecognitionPromotion() || promo.isNominationPromotion() ) )
            {
              Object badgePointsValue = fileLoadRowTokens.nextElement();
              if ( badgePointsValue != null && !badgePointsValue.equals( "null" ) && new Long( badgePointsValue.toString() ) > 0 )
              {
                badgePoints = Long.parseLong( badgePointsValue.toString() );
              }
              else
              {
                badgePoints = null;
              }
              Object badgeSweepValue = fileLoadRowTokens.nextElement();
              if ( badgeSweepValue.equals( "on" ) || badgeSweepValue.equals( "true" ) )
              {
                eligibleForSweepstake = true;
              }
              else
              {
                eligibleForSweepstake = false;
              }
            }
            Object badgeDescValue = fileLoadRowTokens.nextElement();
            if ( badgeDescValue != null )
            {
              badgeDescription = badgeDescValue.toString();
            }
            else
            {
              badgeDescription = "";
            }

            while ( fileLoadRowTokens.hasMoreElements() )
            {
              badgeDescription += SPLIT_TOKEN + fileLoadRowTokens.nextElement();
            }
            if ( badgeLibId.indexOf( REPLACEMENT_TOKEN ) > 0 )
            {
              badgeLibId = badgeLibId.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
            }
            if ( badgeName.indexOf( REPLACEMENT_TOKEN ) > 0 )
            {
              badgeName = badgeName.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
            }

            if ( !badgeDescription.equalsIgnoreCase( "0" ) )
            {
              if ( badgeDescription.indexOf( REPLACEMENT_TOKEN ) > 0 )
              {
                badgeDescription = badgeDescription.replaceAll( REPLACEMENT_TOKEN, SPLIT_TOKEN );
              }
              // badgeRules.setBadgeDescription( badgeDescription );
            }
            else
            {
              // badgeRules.setBadgeDescription("");
              badgeDescription = "";
            }
            String badgeNameCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeName(), badgeName );
            if ( !badgeDescription.isEmpty() )
            {
              String badgeDescCmAsset = getGamificationService().saveRulesCmText( badgeRules.getBadgeDescription(), badgeDescription );
              badgeRules.setBadgeDescription( badgeDescCmAsset );
            }
            else
            {
              badgeRules.setBadgeDescription( badgeDescription );
            }

            badgeRules.setBadgePromotion( badgeReturned );
            badgeRules.setBadgeName( badgeNameCmAsset );
            badgeRules.setBadgePoints( badgePoints );
            badgeRules.setEligibleForSweepstake( eligibleForSweepstake );
            badgeRules.setBadgeLibraryCMKey( badgeLibId );
          }

          badgeRulesList.add( badgeRules );

        }

      }
    }
    catch( Exception e )
    {
      logger.error( e );
    }

    return badgeRulesList;

  }

  private Map getActiveMessageMap( List activeMessages )
  {
    Map activeMessageMap = new HashMap();

    for ( Iterator iter = activeMessages.iterator(); iter.hasNext(); )
    {
      Message message = (Message)iter.next();
      String messageTypeCode = message.getMessageTypeCode().getCode();

      List activeMessageListByTypeCode = (List)activeMessageMap.get( messageTypeCode );

      if ( null == activeMessageListByTypeCode )
      {
        activeMessageListByTypeCode = new ArrayList();
        activeMessageMap.put( messageTypeCode, activeMessageListByTypeCode );
      }

      activeMessageListByTypeCode.add( message );
    }

    Set<String> keys = activeMessageMap.keySet();
    for ( String messageTypeCode : keys )
    {
      List messages = (List)activeMessageMap.get( messageTypeCode );

      // Add the "Select One" message
      Message selectMessage = new Message();
      selectMessage.setId( new Long( 0 ) );
      selectMessage.setName( "Select One" );
      messages.add( 0, selectMessage );

      if ( !messageTypeCode.equals( "recognition_received" ) )
      {
        // Add the "No Notification" message
        Message noMessage = new Message();
        noMessage.setId( new Long( -1 ) );
        noMessage.setName( "No Notification" );
        messages.add( noMessage );
      }
    }

    return activeMessageMap;
  }

  public List getEligiblePromotions( HttpServletRequest request )
  {
    if ( UserManager.getUser().isParticipant() )
    {
      List eligiblePromotions = (List)request.getSession().getAttribute( "eligiblePromotions" );
      if ( null != eligiblePromotions )
      {
        return eligiblePromotions;
      }
      else
      {
        eligiblePromotions = getMainContentService().buildEligiblePromoList( UserManager.getUser() );
        request.getSession().setAttribute( "eligiblePromotions", eligiblePromotions );
        return eligiblePromotions;
      }
    }
    return null;
  }

  /**
   * Get the GamificationService from the beanFactory locator.
   * 
   * @return GamificationService
   */
  private GamificationService getGamificationService()
  {
    return (GamificationService)getService( GamificationService.BEAN_NAME );
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }

  /**
   * Get the PromotionService from the beanFactory locator.
   * 
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  /**
   * Get the MessageService from the beanFactory locator.
   * 
   * @return MessageService
   */

  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

  /**
   * Get the CountryService from the beanFactory locator.
   * 
   * @return CountryService
   */
  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

  private MainContentService getMainContentService()
  {
    return (MainContentService)(SAO)BeanLocator.getBean( MainContentService.BEAN_NAME );
  }

}
