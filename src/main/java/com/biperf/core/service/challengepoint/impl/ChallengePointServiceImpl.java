
package com.biperf.core.service.challengepoint.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.biperf.awardslinqDataRetriever.util.AwardslinqDataRetrieverException;
import com.biperf.core.dao.challengepoint.ChallengepointAwardDAO;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.domain.challengepoint.ChallengepointAward;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.AchievementRuleType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.RoundingMethod;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingAttachmentInfo;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.ChallengePointAchievementStrategyFactory;
import com.biperf.core.service.promotion.engine.ChallengePointCalculationResult;
import com.biperf.core.service.promotion.engine.ChallengePointIncrementStrategyFactory;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.FileExtractUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.MerchLinqProductDataUtils;
import com.biperf.core.utils.ServiceLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ChallengepointPaxAwardValueBean;
import com.biperf.core.value.ChallengepointPaxValueBean;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * 
 * ChallengePointServiceImpl.
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
 * <td>Satish & Todd</td>
 * <td>Jan 15, 2007</td>
 * <td>1.0</td>
 * <td>Handles Challengepoint Calculation and processes Calculation results</td>
 * </tr>
 * </table>
 * 
 * 
 */
public class ChallengePointServiceImpl implements ChallengePointService
{
  private PaxGoalService paxGoalService;
  private MerchLevelService merchLevelService;
  private PromotionDAO promotionDAO;
  private ChallengepointAwardDAO challengepointAwardDAO;

  private AudienceService audienceService;
  private SystemVariableService systemVariableService;
  private MessageService messageService;
  private MailingService mailingService;
  private UserService userService;
  private ParticipantService participantService;

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  private static final String DETAIL_REPORT_HEADER = "Promotion,Login Id,PAX First Name,PAX Last Name,Email Address,"
      + "Phone,Address,City,State,Zip Code,Country,Node Role,Node Owner,Base,CP Level,CP Value,CP Description,"
      + "Total Progress, CP Award,Calculated starting point, Calculated increment,Basic earned," + "Basic Pending, Basic deposited,Total Award,Total Award pending";

  private static final String omDateFormat = "dd-MMM-yy";
  private static final String UnixFileSeparator = "/";
  private static final String WindowsFileSeparator = "\\";

  private static final Logger log = Logger.getLogger( ChallengePointServiceImpl.class );

  /**
   * @param userId
   * @return List of all ChallengePointPromotionObjects with PaxLevels present
   *         for selected promotions
   * @throws ServiceErrorException 
   */
  public List<ChallengePointPromotion> getAllLiveChallengePointPromotionsByUserId( Long userId ) throws ServiceErrorException
  {
    List<ChallengePointPromotion> cpPromotions = promotionDAO.getAllLiveByType( PromotionType.CHALLENGE_POINT );

    return cpPromotions;
  }

  /**
   * @param userId
   * @return List of all ChallengePointPromotionObjects with PaxLevels present
   *         for selected promotions
   */
  public List<PaxGoal> getAllLivePromotionsWithPaxGoalsByUserId( Long userId )
  {
    List<ChallengePointPromotion> cpPromotions = promotionDAO.getAllLiveByType( PromotionType.CHALLENGE_POINT );
    List<PaxGoal> paxGoals = new ArrayList<PaxGoal>();

    for ( Iterator iter = cpPromotions.iterator(); iter.hasNext(); )
    {
      ChallengePointPromotion cpPromotion = (ChallengePointPromotion)iter.next();
      if ( cpPromotion != null )
      {
        PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( cpPromotion.getId(), userId );
        if ( paxGoal == null )
        {
          paxGoal = new PaxGoal();
          paxGoal.setGoalQuestPromotion( cpPromotion );

        }
        paxGoals.add( paxGoal );
      }
    }
    return paxGoals;
  }

  /**
     * @param promotion
     * @param base
     * @param value
     * @return BigDecimal
     */
  public BigDecimal computePercentOfBaseAmount( ChallengePointPromotion promotion, BigDecimal base, BigDecimal value )
  {
    BigDecimal calculatedValue = null;
    if ( base != null && value != null )
    {
      calculatedValue = value.multiply( base ).divide( new BigDecimal( 100 ), 2, BigDecimal.ROUND_DOWN );
      return promotion.roundValue( calculatedValue );
    }
    else
    {
      return new BigDecimal( 0 );
    }

  }

  /**
   * @param promotion
   * @param base
   * @param value
   * @return BigDecimal
   */
  public BigDecimal computePercent( ChallengePointPromotion promotion, BigDecimal base, BigDecimal value )
  {
    BigDecimal calculatedValue = null;
    if ( base != null && value != null )
    {
      calculatedValue = value.divide( base, 2, BigDecimal.ROUND_DOWN ).multiply( new BigDecimal( 100 ) );
      return promotion.roundValue( calculatedValue );
    }
    else
    {
      return new BigDecimal( 0 );
    }

  }

  public List<ChallengepointPaxValueBean> getParticipantChallengePointLevelBeans( ChallengePointPromotion promotion, PaxGoal paxGoal, boolean productsFlag ) throws ServiceErrorException
  {
    List<ChallengepointPaxValueBean> challengePointLevelBeans = new ArrayList<ChallengepointPaxValueBean>();
    HashMap productMap = null;
    if ( promotion.getGoalLevels() != null )
    {
      log.debug( "product levels size" + promotion.getGoalLevels().size() );
      if ( productsFlag )
      {
        try
        {
          productMap = (HashMap)MerchLinqProductDataUtils.getProductMap( promotion.getProgramId() );
        }
        catch( AwardslinqDataRetrieverException e )
        {
          throw new ServiceErrorException( e.getNestedExceptionErrorList() );
        }
      }
      for ( Iterator<AbstractGoalLevel> iter = promotion.getGoalLevels().iterator(); iter.hasNext(); )
      {
        GoalLevel goalLevel = (GoalLevel)iter.next();

        ChallengepointPaxValueBean challengepointPaxValueBean = populateChallengepointPaxValueBean( paxGoal, promotion, goalLevel );
        if ( productsFlag && productMap != null && goalLevel != null )
        {

          challengepointPaxValueBean.setProducts( (List)productMap.get( new Integer( goalLevel.getAward().intValue() ) ) );

        }
        challengePointLevelBeans.add( challengepointPaxValueBean );
      }
    }
    return challengePointLevelBeans;
  }

  /**
   * @param paxChallengePoint
   * @param promotion
   * @param ChallengePointLevel
   * @return ChallengePointLevelValueBean
   */
  public ChallengepointPaxValueBean populateChallengepointPaxValueBean( PaxGoal paxGoal, ChallengePointPromotion promotion, GoalLevel goalLevel ) throws ServiceErrorException
  {
    Participant pax = null;
    ChallengepointPaxValueBean challengepointpaxValueBean = new ChallengepointPaxValueBean();
    challengepointpaxValueBean.setGoalLevel( goalLevel );
    challengepointpaxValueBean.setPromotion( promotion );

    Long userId = null;
    if ( paxGoal == null || paxGoal.getParticipant() == null )
    {
      userId = UserManager.getUserId();
    }
    else
    {
      userId = paxGoal.getParticipant().getId();
    }

    challengepointpaxValueBean.setCalculatedThreshold( getCalculatedThreshold( promotion.getId(), userId ) );
    ChallengePointIncrementStrategyFactory challengePointIncrementStrategyFactory = getChallengePointIncrementStrategyFactory();
    challengepointpaxValueBean
        .setCalculatedIncrementAmount( challengePointIncrementStrategyFactory.getChallengePointIncrementStrategy( promotion.getAwardIncrementType() ).processIncrement( promotion, userId ) );
    if ( paxGoal != null && paxGoal.getParticipant() != null )
    {
      pax = getParticipant( paxGoal.getParticipant().getId() );
    }
    else
    {
      pax = getParticipant( userId );
    }

    if ( goalLevel != null )
    {
      challengepointpaxValueBean.setAmountToAchieve( getCalculatedAchievementAmount( goalLevel, paxGoal ) );
      if ( paxGoal != null )
      {
        // pax=paxLevel.getParticipant();
        challengepointpaxValueBean.setBaseAmount( paxGoal.getBaseQuantity() );
        challengepointpaxValueBean.setPaxGoal( paxGoal );
        BigDecimal currentValueForAward = null;
        if ( challengepointpaxValueBean.getCalculatedThreshold() == null )
        {
          if ( paxGoal.getCurrentValue() != null )
          {
            currentValueForAward = paxGoal.getCurrentValue();
          }
          else
          {
            currentValueForAward = new BigDecimal( 0 );
          }
        }
        else
        {
          if ( paxGoal.getCurrentValue() != null )
          {
            currentValueForAward = paxGoal.getCurrentValue().subtract( challengepointpaxValueBean.getCalculatedThreshold() );
          }
          else
          {
            currentValueForAward = new BigDecimal( 0 );
          }
        }
        // If Current Value For Award is < 0 because of threshold, we make it zero
        if ( currentValueForAward.doubleValue() < 0 )
        {
          currentValueForAward = new BigDecimal( 0 );
        }
        BigDecimal awardPerIncrement = new BigDecimal( 0 );
        if ( promotion.getAwardPerIncrement() != null )
        {
          awardPerIncrement = new BigDecimal( promotion.getAwardPerIncrement().longValue() );
          // fix 21447 display points properly if we load progress in decimal
          // currentValueForAward = new BigDecimal( currentValueForAward.longValue()-(
          // currentValueForAward.longValue()% promotion.getAwardIncrementValue().longValue()));
        }
        BigDecimal calculatedIncrement = challengepointpaxValueBean.getCalculatedIncrementAmount();
        if ( calculatedIncrement == null )
        {
          calculatedIncrement = new BigDecimal( 0 );
        }
        if ( calculatedIncrement.doubleValue() > 0 )
        {
          int roundValue;
          if ( promotion.getRoundingMethod().getCode().equals( RoundingMethod.UP ) )
          {
            roundValue = 1;
          }
          else if ( promotion.getRoundingMethod().getCode().equals( RoundingMethod.DOWN ) )
          {
            roundValue = 0;
          }
          else
          {
            roundValue = 4;
          }
          if ( currentValueForAward.intValue() >= calculatedIncrement.intValue() )// For base value
                                                                                  // = 0 for the PAX
                                                                                  // who not achieve
                                                                                  // gaol
          {
            BigDecimal awardCount = currentValueForAward.divide( calculatedIncrement, 0, RoundingMode.DOWN );
            BigDecimal totalBasicAwardEarned = awardCount.multiply( awardPerIncrement );
            challengepointpaxValueBean.setTotalBasicAwardEarned( new Long( totalBasicAwardEarned.longValue() ) );
          }
        }
      }
    }
    boolean programRulesFlag = false;

    if ( pax != null && promotion != null )
    {
      programRulesFlag = checkIfShowPromoRules( promotion, pax );

    }
    challengepointpaxValueBean.setShowProgramRules( programRulesFlag );
    return challengepointpaxValueBean;

  }

  /**
   * @param paxChallengePoint
   * @param promotion
   * @param ChallengePointLevel
   * @return ChallengePointLevelValueBean
   */
  public ChallengepointPaxValueBean populateChallengepointPaxValueBean( PaxGoal paxGoal, ChallengePointPromotion promotion, GoalLevel goalLevel, boolean productsFlag ) throws ServiceErrorException
  {
    ChallengepointPaxValueBean challengepointpaxValueBean = new ChallengepointPaxValueBean();
    challengepointpaxValueBean = populateChallengepointPaxValueBean( paxGoal, promotion, goalLevel );
    if ( productsFlag )
    {
      HashMap productMap = null;
      try
      {
        productMap = (HashMap)MerchLinqProductDataUtils.getProductMap( promotion.getProgramId() );
      }
      catch( AwardslinqDataRetrieverException e )
      {
        throw new ServiceErrorException( e.getNestedExceptionErrorList() );
      }
      if ( productMap != null && goalLevel != null )
      {
        challengepointpaxValueBean.setProducts( (List)productMap.get( new Integer( goalLevel.getAward().intValue() ) ) );
      }
    }

    return challengepointpaxValueBean;
  }

  public PaxGoal getLevelSpecificChallengePoint( PaxGoal paxGoal, ChallengePointPromotion promotion, GoalLevel goalLevel )
  {
    PaxGoal levelSpecificChallengePoint = new PaxGoal();
    if ( null == paxGoal )
    {
      levelSpecificChallengePoint.setGoalLevel( goalLevel );
      levelSpecificChallengePoint.setGoalQuestPromotion( promotion );

    }
    else
    {
      levelSpecificChallengePoint.setGoalLevel( goalLevel );
      levelSpecificChallengePoint.setGoalQuestPromotion( (ChallengePointPromotion)paxGoal.getGoalQuestPromotion() );
      levelSpecificChallengePoint.setCurrentValue( paxGoal.getCurrentValue() );
      levelSpecificChallengePoint.setOverrideQuantity( paxGoal.getOverrideQuantity() );
      levelSpecificChallengePoint.setBaseQuantity( paxGoal.getBaseQuantity() );
    }
    return levelSpecificChallengePoint;
  }

  public ChallengepointPaxValueBean populateChallengepointPaxValueBean( PaxGoal paxGoal, ChallengePointPromotion promotion, GoalLevel goalLevel, Long userId ) throws ServiceErrorException
  {
    Participant pax = null;
    ChallengepointPaxValueBean challengepointpaxValueBean = new ChallengepointPaxValueBean();
    challengepointpaxValueBean.setGoalLevel( goalLevel );
    challengepointpaxValueBean.setPromotion( promotion );

    if ( paxGoal != null && paxGoal.getParticipant() != null )
    {
      userId = paxGoal.getParticipant().getId();
    }

    challengepointpaxValueBean.setCalculatedThreshold( getCalculatedThreshold( promotion.getId(), userId ) );
    ChallengePointIncrementStrategyFactory challengePointIncrementStrategyFactory = getChallengePointIncrementStrategyFactory();
    challengepointpaxValueBean
        .setCalculatedIncrementAmount( challengePointIncrementStrategyFactory.getChallengePointIncrementStrategy( promotion.getAwardIncrementType() ).processIncrement( promotion, userId ) );
    if ( paxGoal != null && paxGoal.getParticipant() != null )
    {
      pax = getParticipant( paxGoal.getParticipant().getId() );
    }
    else
    {
      pax = getParticipant( userId );
    }

    if ( goalLevel != null )
    {
      challengepointpaxValueBean.setAmountToAchieve( getCalculatedAchievementAmount( goalLevel, paxGoal ) );
      if ( paxGoal != null )
      {
        // pax=paxLevel.getParticipant();
        challengepointpaxValueBean.setBaseAmount( paxGoal.getBaseQuantity() );
        challengepointpaxValueBean.setPaxGoal( paxGoal );
        BigDecimal currentValueForAward = null;
        if ( challengepointpaxValueBean.getCalculatedThreshold() == null )
        {
          if ( paxGoal.getCurrentValue() != null )
          {
            currentValueForAward = paxGoal.getCurrentValue();
          }
          else
          {
            currentValueForAward = new BigDecimal( 0 );
          }
        }
        else
        {
          if ( paxGoal.getCurrentValue() != null )
          {
            currentValueForAward = paxGoal.getCurrentValue().subtract( challengepointpaxValueBean.getCalculatedThreshold() );
          }
          else
          {
            currentValueForAward = new BigDecimal( 0 );
          }
        }
        // If Current Value For Award is < 0 because of threshold, we make it zero
        if ( currentValueForAward.doubleValue() < 0 )
        {
          currentValueForAward = new BigDecimal( 0 );
        }
        BigDecimal awardPerIncrement = new BigDecimal( 0 );
        if ( promotion.getAwardPerIncrement() != null )
        {
          awardPerIncrement = new BigDecimal( promotion.getAwardPerIncrement().longValue() );
          // fix 21447 display points properly if we load progress in decimal
          // currentValueForAward = new BigDecimal( currentValueForAward.longValue()-(
          // currentValueForAward.longValue()% promotion.getAwardIncrementValue().longValue()));
        }
        BigDecimal calculatedIncrement = challengepointpaxValueBean.getCalculatedIncrementAmount();
        if ( calculatedIncrement == null )
        {
          calculatedIncrement = new BigDecimal( 0 );
        }
        if ( calculatedIncrement.doubleValue() > 0 )
        {
          int awardCount = currentValueForAward.divide( calculatedIncrement, BigDecimal.ROUND_DOWN ).intValue();
          challengepointpaxValueBean.setTotalBasicAwardEarned( new Long( new BigDecimal( awardCount ).multiply( awardPerIncrement ).longValue() ) );

        }
      }
    }
    boolean programRulesFlag = false;

    if ( pax != null && promotion != null )
    {
      programRulesFlag = checkIfShowPromoRules( promotion, pax );

    }
    challengepointpaxValueBean.setShowProgramRules( programRulesFlag );
    return challengepointpaxValueBean;

  }

  /**
   * @param abstractChallengePointLevel
   * @param paxChallengePoint
   * @return BigDecimal
   */
  public BigDecimal getCalculatedAchievementAmount( GoalLevel goalLevel, PaxGoal paxGoal )
  {
    if ( goalLevel != null )
    {
      ChallengePointPromotion challengePointPromotion = (ChallengePointPromotion)goalLevel.getPromotion();

      ChallengePointCalculationResult challengePointCalculationResult = null;
      if ( challengePointPromotion != null )
      {
        /*
         * bug fix note paxGoal is representing challengepoint level here with participant being
         * null, if that level is not selected
         */

        paxGoal = getLevelSpecificChallengePoint( paxGoal, challengePointPromotion, goalLevel );
        ChallengePointAchievementStrategyFactory challengePointPayoutStrategyFactory = getChallengePointAchievementStrategyFactory();

        AchievementRuleType ruleType = challengePointPromotion.getAchievementRule();

        if ( ruleType != null && paxGoal != null )
        {
          challengePointCalculationResult = challengePointPayoutStrategyFactory.getChallengePointAchievementStrategy( ruleType.getCode() ).processChallengePoint( paxGoal );

          if ( challengePointPromotion.getAchievementPrecision().getCode().equals( "two" ) && goalLevel != null && goalLevel.getAchievementAmount() != null
              && challengePointCalculationResult.getAmountToAchieve() != null )
          {
            return challengePointCalculationResult.getAmountToAchieve().setScale( 2 );
          }
          else if ( challengePointCalculationResult.getAmountToAchieve() == null && goalLevel.getAchievementAmount() != null )
          {
            return goalLevel.getAchievementAmount();
          }

          return challengePointCalculationResult.getAmountToAchieve();
        }
      }
    }
    return null;
  }

  /**
   * @param ChallengePointPromotion
   * @param ChallengePointLevelSequence
   * @param isOwner
   * @return GoalLevel
   */
  public GoalLevel getChallengePointLevelForOwnerOrMember( ChallengePointPromotion ChallengePointPromotion, Integer ChallengePointLevelSequence, boolean isOwner )
  {
    // if owner get manager override goal levels else get
    // GoalLevels
    for ( Iterator<AbstractGoalLevel> iter = ChallengePointPromotion.getGoalLevels().iterator(); iter.hasNext(); )
    {
      GoalLevel goalLevel = (GoalLevel)iter.next();
      // if passed-in goal level sequence matches goal
      // level sequence from db return that GoalLevel
      if ( ChallengePointLevelSequence.intValue() == goalLevel.getSequenceNumber() )
      {
        return goalLevel;
      }
    }
    return null;
  }

  /**
   * @param promotion
   * @return boolean
   */
  public boolean isValidChallengePointPromotionForParticipant( Promotion promotion )
  {
    return promotion.isChallengePointPromotion() && promotion.isLive();
  }

  /**
   * @param pax
   * @param promotion
   * @return boolean
   */
  public boolean isChallengepointSelected( Participant pax, Promotion promotion )
  {
    boolean isLevelSelected = false;
    PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotion.getId(), pax.getId() );
    if ( paxGoal != null && paxGoal.getGoalLevel() != null )
    {
      isLevelSelected = true;
    }
    return isLevelSelected;
  }

  /**
   * @param promotionId
   * @return
   */
  public ChallengePointPromotion getPromotion( Long promotionId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CP_LEVELS ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( promotionId, associationRequestCollection );
    return (ChallengePointPromotion)promotion;
  }

  /**
   * @param userId
   * @return
   */
  public Participant getParticipant( Long userId )
  {
    return getParticipantService().getParticipantById( userId );
  }

  public boolean isParticipantOwner( Long userId )
  {
    Participant pax = getParticipant( userId );
    List<Node> ownedNodes = pax.getNodes( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
    return ownedNodes.size() > 0;
  }

  public boolean isParticipantOwner( Participant pax )
  {
    List<Node> ownedNodes = pax.getNodes( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
    return ownedNodes.size() > 0;
  }

  public boolean isParticipantManager( Participant pax )
  {
    List<Node> ownedNodes = pax.getNodes( HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ) );
    return ownedNodes.size() > 0;
  }

  /**
   * @param promotionId
   * @param userId
   * @return
   */
  public PaxGoal getPaxChallengePoint( Long promotionId, Long userId )
  {
    return getPaxGoalService().getPaxGoalByPromotionIdAndUserId( promotionId, userId );
  }

  /**
   * Get the PromotionService from the beanLocator.
   * 
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)ServiceLocator.getService( PromotionService.BEAN_NAME );
  }

  /**
   * Get the ParticipantService from the beanLocator.
   * 
   * @return ParticipantService
   */
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)ServiceLocator.getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Get the ChallengePointPayoutStrategyFactory from the beanLocator.
   * 
   * @return ChallengePointPayoutStrategyFactory
   */
  private ChallengePointAchievementStrategyFactory getChallengePointAchievementStrategyFactory()
  {
    return (ChallengePointAchievementStrategyFactory)BeanLocator.getBean( ChallengePointAchievementStrategyFactory.BEAN_NAME );
  }

  /**
   * Get the ChallengePointIncrementStrategyFactory from the beanLocator.
   * 
   * @return ChallengePointIncrementStrategyFactory
   */
  private ChallengePointIncrementStrategyFactory getChallengePointIncrementStrategyFactory()
  {
    return (ChallengePointIncrementStrategyFactory)BeanLocator.getBean( ChallengePointIncrementStrategyFactory.BEAN_NAME );
  }

  /**
   * @param promotionId
   * @param userId
   * @param ascReqCollection
   * @return PaxChallengePoint With Initialized Associations
   */
  public PaxGoal getPaxChallengePoint( Long promotionId, Long userId, AssociationRequestCollection ascReqCollection )
  {
    PaxGoalService paxChallengePointService = (PaxGoalService)ServiceLocator.getService( PaxGoalService.BEAN_NAME );
    return paxChallengePointService.getPaxGoalByPromotionIdAndUserId( promotionId, userId, ascReqCollection );
  }

  public MerchLevelService getMerchLevelService()
  {
    return merchLevelService;
  }

  public void setMerchLevelService( MerchLevelService merchLevelService )
  {
    this.merchLevelService = merchLevelService;
  }

  /**
     * This method calculates the threshold for a user for a given promotion
     * @param promotionId Long
     * @param userId Long
     * @return calculatedThreshold BigDecimal
     */
  public BigDecimal getCalculatedThreshold( Long promotionId, Long userId )
  {

    ChallengePointPromotion cpPromotion = getPromotion( promotionId );

    if ( cpPromotion.getAwardThresholdType() == null || cpPromotion.getAwardThresholdType().equals( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_NONE ) )
    {
      return new BigDecimal( 0 );
    }
    else if ( cpPromotion.getAwardThresholdType().equals( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_FIXED ) )
    {
      log.debug( "type is fixed--->" + cpPromotion.getAwardThresholdValue().longValue() );
      return new BigDecimal( cpPromotion.getAwardThresholdValue().longValue() );
    }
    else if ( cpPromotion.getAwardThresholdType().equals( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_PCT_BASE ) )
    {
      PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( promotionId, userId );
      if ( paxGoal == null || paxGoal.getGoalQuestPromotion() == null || !paxGoal.getGoalQuestPromotion().isLive() )
      {
        log.debug( "promotion id-->" + promotionId + "user id-->" + userId + " " + " selected paxLevel is null" );
        return null;
      }
      else
      {
        return computePercentOfBaseAmount( (ChallengePointPromotion)paxGoal.getGoalQuestPromotion(),
                                           paxGoal.getBaseQuantity(),
                                           new BigDecimal( ( (ChallengePointPromotion)paxGoal.getGoalQuestPromotion() ).getAwardThresholdValue().longValue() ) );

      }
    }
    return new BigDecimal( 0 );
  }

  /**
   * @param userId
   * @param ascReqCol 
   * @return List of all ChallengePointPromotionObjects with PaxGoals present
   *         for selected promotions
   */
  public List<PaxGoal> getAllLivePromotionsWithPaxGoalsByUserIdWithAssociations( Long userId, AssociationRequestCollection ascReqCol )
  {
    List<ChallengePointPromotion> cpPromotions = promotionDAO.getAllLiveByType( PromotionType.CHALLENGE_POINT );
    List<PaxGoal> paxGoals = new ArrayList<PaxGoal>();

    for ( Iterator iter = cpPromotions.iterator(); iter.hasNext(); )
    {
      ChallengePointPromotion cpPromotion = (ChallengePointPromotion)iter.next();
      if ( cpPromotion != null )
      {
        ascReqCol.process( cpPromotion );
        PaxGoal paxGoal = paxGoalService.getPaxGoalByPromotionIdAndUserId( cpPromotion.getId(), userId );
        if ( paxGoal == null )
        {
          paxGoal = new PaxGoal();
          paxGoal.setGoalQuestPromotion( cpPromotion );

        }
        paxGoals.add( paxGoal );
      }
    }
    return paxGoals;
  }

  private boolean checkIfShowPromoRules( Promotion promotion, Participant participant )
  {
    String timeZoneID = UserManager.getUser().getTimeZoneId();
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.WEB_RULES_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    promotion = getPromotionService().getPromotionByIdWithAssociations( promotion.getId(), promoAssociationRequestCollection );
    return promotion.isWebRulesActive() && audienceService.isParticipantInWebRulesAudience( promotion, participant );
  }

  /**
   * Returns the AudienceService
   * 
   * @return a reference to the AudienceService.
   */
  private AudienceService getAudienceService()
  {
    return audienceService;
  }

  public ChallengepointAward saveChallengepointAward( ChallengepointAward challengepointAward )
  {
    return challengepointAwardDAO.saveChallengepointAward( challengepointAward );
  }

  public List<ChallengepointAward> getParticipantChallengePointAwards( Long promotionId, Long userId )
  {
    return getChallengepointAwardDAO().getAllChallengepointAwardsByPromotionIdAndUserId( promotionId, userId );
  }

  public ChallengepointAwardDAO getChallengepointAwardDAO()
  {
    return challengepointAwardDAO;
  }

  public PaxGoalService getPaxGoalService()
  {
    return paxGoalService;
  }

  public void setPaxGoalService( PaxGoalService paxGoalService )
  {
    this.paxGoalService = paxGoalService;
  }

  public PromotionDAO getPromotionDAO()
  {
    return promotionDAO;
  }

  public void setPromotionDAO( PromotionDAO promotionDAO )
  {
    this.promotionDAO = promotionDAO;
  }

  public void setAudienceService( AudienceService audienceService )
  {
    this.audienceService = audienceService;
  }

  public void setChallengepointAwardDAO( ChallengepointAwardDAO challengepointAwardDAO )
  {
    this.challengepointAwardDAO = challengepointAwardDAO;
  }

  /**
   * This method returns user defined path where the csv file should be saved. i.e. /tmp/ on Unix.
   * It makes sure the file separator (On Windows this is \  On Unix this is /) works with the
   * current operating system
   * 
   * @return a system variable driven path where the extract will be saved
   */
  private String getExtractLocation()
  {
    String extractLocation = null;

    // On Windows this is \ On Unix this is /
    String currentSystemFileSeparator = File.separator;

    // user defined path where the csv file should be saved. i.e. /tmp/ on Unix
    extractLocation = System.getProperty( "appdatadir" ); // 28946

    // make sure the user defined directory works with the current system
    if ( !StringUtils.isBlank( extractLocation ) )
    {
      // e.g. Developers running on localhosts on Windows
      // but the system variable specifies an Unix file separator
      if ( extractLocation.indexOf( UnixFileSeparator ) >= 0 && currentSystemFileSeparator.equals( WindowsFileSeparator ) )
      {
        extractLocation = extractLocation.replace( '/', '\\' );
      }
      // e.g. QA, PPRD, PROD - CTECH environments running on Unix
      // but the system variable specifies a Windows file separator
      if ( extractLocation.indexOf( WindowsFileSeparator ) >= 0 && currentSystemFileSeparator.equals( UnixFileSeparator ) )
      {
        extractLocation = extractLocation.replace( '\\', '/' );
      }
    }

    return extractLocation;
  }

  public boolean generateAndMailExtractReport( List<ChallengepointPaxAwardValueBean> cpCalculationList, ChallengePointPromotion promotion, List cpManagerOverrideList )
  {
    return generateAndMailExtractReport( cpCalculationList, promotion, cpManagerOverrideList, null );
  }

  /**
   * Generate and mail a detail extract from the challengepoint CalculationList
   * 
   * @param cpCalculationList
   * @return boolean 
   */
  public boolean generateAndMailExtractReport( List<ChallengepointPaxAwardValueBean> cpCalculationList, ChallengePointPromotion promotion, List cpManagerOverrideList, String batchComments )
  {
    try
    {
      String extractLocation = getExtractLocation();
      String fileName = generateUniqueFileName();

      FileExtractUtils.createDirIfNeeded( extractLocation );

      File extractFile = new File( extractLocation, fileName ); // Fix 28946
      List<ChallengepointPaxAwardValueBean> consolidatedList = new ArrayList<ChallengepointPaxAwardValueBean>();

      // Getting out Challengepoint object first into the hash map
      // will make sure that Challengepoint object will be the primary and basic award will
      // be added to the challengepoint objects.
      if ( cpCalculationList != null )
      {
        for ( Iterator cpCalculationResultsIter = cpCalculationList.iterator(); cpCalculationResultsIter.hasNext(); )
        {
          ChallengepointPaxAwardValueBean cpPaxAwardValueBean = (ChallengepointPaxAwardValueBean)cpCalculationResultsIter.next();
          if ( cpPaxAwardValueBean.getAwardType().equals( "challengepoint" ) || cpPaxAwardValueBean.getAwardType().equals( "partner" ) )
          {
            consolidatedList.add( cpPaxAwardValueBean );
            // consolidatedMap.put( cpPaxAwardValueBean.getParticipant(), cpPaxAwardValueBean );
          }
        }
        int index = 0;
        for ( Iterator cpCalculationResultsIter = cpCalculationList.iterator(); cpCalculationResultsIter.hasNext(); )
        {
          ChallengepointPaxAwardValueBean cpPaxAwardValueBean = (ChallengepointPaxAwardValueBean)cpCalculationResultsIter.next();
          ChallengepointPaxAwardValueBean cpPaxAwardConsolidated = null;
          if ( index < consolidatedList.size() )
          {
            cpPaxAwardConsolidated = (ChallengepointPaxAwardValueBean)consolidatedList.get( index );

            if ( cpPaxAwardConsolidated == null )
            {
              cpPaxAwardConsolidated = cpPaxAwardValueBean;
            }
          }
          if ( cpPaxAwardValueBean.getAwardType().equals( "challengepoint" ) || cpPaxAwardValueBean.getAwardType().equals( "partner" ) )
          {
            if ( cpPaxAwardValueBean.getCalculatedAchievement() != null )
            {
              cpPaxAwardConsolidated.setExtractCPAwardDeposited( new Long( cpPaxAwardValueBean.getCalculatedAchievement().longValue() ) );
            }
            cpPaxAwardConsolidated.setCalculatedAchievement( cpPaxAwardValueBean.getCalculatedAchievement() );
          }
          else if ( cpPaxAwardValueBean.getAwardType().equals( "basic" ) )
          {
            cpPaxAwardConsolidated.setExtractBasicAwardEarned( cpPaxAwardValueBean.getAwardEarned() );
            cpPaxAwardConsolidated.setExtractBasicAwardPending( cpPaxAwardValueBean.getAwardIssued() );
            long totalDeposit = 0;
            long depositNow = 0;
            if ( cpPaxAwardValueBean.getTotalBasicAwardIssued() != null )
            {
              totalDeposit = cpPaxAwardValueBean.getTotalBasicAwardIssued().longValue();
            }
            if ( cpPaxAwardValueBean.getAwardIssued() != null )
            {
              depositNow = cpPaxAwardValueBean.getAwardIssued().longValue();
            }
            cpPaxAwardConsolidated.setExtractBasicAwardDeposited( new Long( totalDeposit - depositNow < 0 ? 0 : totalDeposit - depositNow ) );
            index++;
          }
          else if ( cpPaxAwardValueBean.getAwardType().equals( "manageroverride" ) )
          {
            consolidatedList.add( cpPaxAwardValueBean );
          }
          // consolidatedMap.put( cpPaxAwardConsolidated.getParticipant(), cpPaxAwardConsolidated );

        }
      }
      /*
       * if ( cpManagerOverrideList != null && cpManagerOverrideList.size() > 0 ) { for ( Iterator
       * cpCalculationResultsIter = cpManagerOverrideList.iterator();
       * cpCalculationResultsIter.hasNext(); ) { ChallengepointPaxAwardValueBean cpPaxAwardValueBean
       * = (ChallengepointPaxAwardValueBean)cpCalculationResultsIter.next(); consolidatedList.add(
       * cpPaxAwardValueBean ); //consolidatedMap.put( cpPaxAwardValueBean.getParticipant(),
       * cpPaxAwardValueBean ); } }
       */

      // Collection consolidatedList = consolidatedList1.values();
      if ( extractFile.createNewFile() )
      {
        BufferedWriter writer = new BufferedWriter( new FileWriter( extractFile ) );
        writer.write( DETAIL_REPORT_HEADER );
        writer.newLine();
        if ( cpCalculationList != null )
        {
          for ( Iterator cpCalculationResultsIter = consolidatedList.iterator(); cpCalculationResultsIter.hasNext(); )
          {
            ChallengepointPaxAwardValueBean cpPaxAwardValueBean = (ChallengepointPaxAwardValueBean)cpCalculationResultsIter.next();
            String csvRow = createDetailLine( cpPaxAwardValueBean, promotion );
            writer.write( csvRow );
            writer.newLine();
          }
        }
        writer.close();
        if ( extractFile != null && extractFile.exists() )
        {
          // Email the file to the user.
          sendMessage( extractFile.getAbsolutePath(), extractFile.getName(), promotion, batchComments );
        }
      }
      else
      {
        log.error( "Error creating temporary file for extract" );
        throw new BeaconRuntimeException( "Error creating temporary file for extract" );

      }
      return false;
    }
    catch( Exception e )
    {
      log.error( "Error generating extract file", e );
      throw new BeaconRuntimeException( "Error generating extract file", e );
    }
  }

  private String createDetailLine( ChallengepointPaxAwardValueBean cpCalculationResult, ChallengePointPromotion promotion ) throws ServiceErrorException
  {
    UserAssociationRequest userAssociationRequest = new UserAssociationRequest( UserAssociationRequest.ALL );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( userAssociationRequest );
    User user = userService.getUserByIdWithAssociations( cpCalculationResult.getParticipant().getId(), associationRequestCollection );
    StringBuffer detailLine = new StringBuffer();
    detailLine.append( promotion.getName() );
    detailLine.append( ",=\"" );
    detailLine.append( user.getUserName() );
    detailLine.append( "\"," );
    detailLine.append( user.getFirstName() );
    detailLine.append( ',' );
    detailLine.append( user.getLastName() );
    detailLine.append( ',' );
    if ( user.getPrimaryEmailAddress() != null )
    {
      detailLine.append( user.getPrimaryEmailAddress().getEmailAddr() );
    }
    detailLine.append( ',' );
    if ( user.getPrimaryPhone() != null )
    {
      detailLine.append( user.getPrimaryPhone().getPhoneNbr() );
    }
    detailLine.append( ',' );
    if ( user.getPrimaryAddress() != null && user.getPrimaryAddress().getAddress() != null )
    {
      detailLine.append( '"' );
      detailLine.append( user.getPrimaryAddress().getAddress().getAddr1() );
      detailLine.append( "\",\"" );
      detailLine.append( user.getPrimaryAddress().getAddress().getCity() );
      detailLine.append( "\",\"" );
      if ( user.getPrimaryAddress().getAddress().getStateType() != null )
      {
        detailLine.append( user.getPrimaryAddress().getAddress().getStateType().getName() );
      }
      detailLine.append( "\"," );
      detailLine.append( user.getPrimaryAddress().getAddress().getPostalCode() );
      detailLine.append( ',' );
      Country country = user.getPrimaryAddress().getAddress().getCountry();
      if ( country != null )
      {
        if ( country.getCmAssetCode() != null && country.getNameCmKey() != null )
        {
          detailLine.append( ContentReaderManager.getText( country.getCmAssetCode(), country.getNameCmKey() ) );
        }
      }
      detailLine.append( ',' );
    }
    else
    {
      detailLine.append( ",,,,," );
    }
    Set<UserNode> userNodes = user.getUserNodesWithinPrimaryHierarchy();
    if ( userNodes == null || userNodes.size() == 0 )
    {
      detailLine.append( ",," );
    }
    else
    {
      Iterator iter = userNodes.iterator();
      iter.hasNext();
      UserNode userNode = (UserNode)iter.next();
      if ( userNode != null && userNode.getHierarchyRoleType() != null )
      {
        detailLine.append( userNode.getHierarchyRoleType().getName() );
      }
      detailLine.append( ',' );
      detailLine.append( "\"" );
      if ( userNode != null && userNode.getNode() != null && participantService.getNodeOwner( userNode.getNode().getId() ) != null )
      {
        detailLine.append( participantService.getNodeOwner( userNode.getNode().getId() ).getNameLFMWithComma() );
      }
      detailLine.append( "\"," );
    }
    if ( cpCalculationResult.getBaseQuantity() != null )
    {
      detailLine.append( cpCalculationResult.getBaseQuantity() );
    }
    detailLine.append( ',' );
    if ( cpCalculationResult.getPaxGoal() != null )
    {
      detailLine.append( cpCalculationResult.getPaxGoal().getGoalLevel().getSequenceNumber() );
      detailLine.append( ',' );
      if ( cpCalculationResult.getAmountToAchieve() != null )
      {
        detailLine.append( cpCalculationResult.getAmountToAchieve() );
      }
      detailLine.append( ',' );
      detailLine.append( cpCalculationResult.getPaxGoal().getGoalLevel().getGoalLevelName() );
      detailLine.append( ',' );
      detailLine.append( cpCalculationResult.getPaxGoal().getCurrentValue() );
      detailLine.append( ',' );
    }
    else
    {
      detailLine.append( ",,,," );
    }

    if ( cpCalculationResult.getCalculatedAchievement() != null )
    {
      detailLine.append( cpCalculationResult.getCalculatedAchievement() );
    }
    detailLine.append( ',' );
    if ( !cpCalculationResult.getAwardType().equalsIgnoreCase( "manageroverride" ) )
    {
      BigDecimal threshold = getCalculatedThreshold( promotion.getId(), cpCalculationResult.getParticipant().getId() );
      detailLine.append( threshold == null ? new BigDecimal( 0 ) : threshold );
    }
    detailLine.append( ',' );
    if ( !cpCalculationResult.getAwardType().equalsIgnoreCase( "manageroverride" ) )
    {
      GoalLevel goalLevel = null;
      if ( cpCalculationResult.getPaxGoal() != null )
      {
        goalLevel = (GoalLevel)cpCalculationResult.getPaxGoal().getGoalLevel();
      }
      else if ( cpCalculationResult.getGoalLevel() != null )
      {
        goalLevel = cpCalculationResult.getGoalLevel();
      }
      ChallengepointPaxValueBean cpPaxValueBean = populateChallengepointPaxValueBean( cpCalculationResult.getPaxGoal(), promotion, goalLevel );
      if ( cpPaxValueBean != null )
      {
        detailLine.append( cpPaxValueBean.getCalculatedIncrementAmount() );
      }
    }
    detailLine.append( ',' );
    detailLine.append( cpCalculationResult.getExtractBasicAwardEarned() == null ? new Long( 0 ) : cpCalculationResult.getExtractBasicAwardEarned() );
    detailLine.append( ',' );
    detailLine.append( cpCalculationResult.getExtractBasicAwardPending() == null ? new Long( 0 ) : cpCalculationResult.getExtractBasicAwardPending() );
    detailLine.append( ',' );
    detailLine.append( cpCalculationResult.getExtractBasicAwardDeposited() == null ? new Long( 0 ) : cpCalculationResult.getExtractBasicAwardDeposited() );
    detailLine.append( ',' );

    if ( !cpCalculationResult.getAwardType().equalsIgnoreCase( "manageroverride" ) )
    {
      if ( cpCalculationResult.getCalculatedAchievement() != null && cpCalculationResult.getExtractBasicAwardEarned() != null )
      {
        detailLine.append( cpCalculationResult.getExtractBasicAwardEarned().longValue() + cpCalculationResult.getCalculatedAchievement().longValue() );
      }
      else if ( cpCalculationResult.getCalculatedAchievement() != null )
      {
        detailLine.append( cpCalculationResult.getCalculatedAchievement().longValue() );

      }
      else if ( cpCalculationResult.getExtractBasicAwardEarned() != null )
      {
        detailLine.append( cpCalculationResult.getExtractBasicAwardEarned().longValue() );
      }
    }
    else
    {
      detailLine.append( cpCalculationResult.getAwardEarned() );
    }
    detailLine.append( ',' );
    if ( promotion.isIssueAwardsRun() )
    {
      detailLine.append( new Long( 0 ) );
    }
    else
    {
      long totalPending = 0;
      if ( cpCalculationResult.getExtractBasicAwardPending() != null )
      {
        totalPending = cpCalculationResult.getExtractBasicAwardPending().longValue();
      }
      if ( cpCalculationResult.getExtractCPAwardDeposited() != null )
      {
        totalPending += cpCalculationResult.getExtractCPAwardDeposited().longValue();
      }
      detailLine.append( totalPending );

    }
    detailLine.append( ',' );
    return detailLine.toString();

  }

  /**
   * Creates a .csv file name that is unique to: - the client name - the report requested - the current datetime.
   * @return an unique file name
   * 
   */
  private String generateUniqueFileName()
  {
    StringBuffer fileName = new StringBuffer();
    fileName.append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    fileName.append( "_CPCalcExtract_" );
    fileName.append( DateUtils.getCurrentDate().getTime() );
    fileName.append( ".csv" );
    return fileName.toString();
  }

  /**
   * Sends an e-mail message to the user  with the file attachment of the dataextract
   */
  private void sendMessage( String fullFileName, String attachmentFileName, ChallengePointPromotion promotion, String batchComments )
  {
    User currentUser = userService.getUserByUserName( UserManager.getUserName() );
    // Set up mailing-level personalization data.
    Map objectMap = new HashMap();
    if ( promotion != null )
    {
      objectMap.put( "promoName", promotion.getName() );
    }
    if ( !StringUtils.isEmpty( batchComments ) )
    {
      objectMap.put( "batchComments", batchComments );
    }

    // Compose the e-mail message.

    Mailing mailing = composeMail( MessageService.CHALLENGEPOINT_DETAIL_EXTRACT_MESSAGE_CM_ASSET_CODE, MailingType.PROMOTION );

    mailing.addMailingRecipient( createRecipient( currentUser ) );

    // Is there a file to attach?
    if ( fullFileName != null )
    {
      // Attach the file to the e-mail.
      mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, fullFileName, attachmentFileName ) );
    }

    // Send the e-mail message.
    try
    {
      // mailingService.submitMailing( mailing, objectMap );
      mailingService.submitMailingWithoutScheduling( mailing, objectMap );
      // process mailing
      mailingService.processMailing( mailing.getId() );
      log.info( "Successfully sent email to  " + currentUser.getFirstName() + " " + currentUser.getLastName() + "." + " (mailing ID = " + mailing.getId() + ")" );

    }
    catch( Exception e )
    {
      String msg = new String( "An exception occurred while sending challengepoint detail extract. " + " (mailing ID = " + mailing.getId() + ")" );
      log.error( msg, e );
      throw new BeaconRuntimeException( "Error during submission of mail. The exception caused by: " + e.getCause().getMessage() );
    }
  }

  /**
   * @param mailing
   * @param fullFileName
   * @return MailingAttachmentInfo
   */
  protected MailingAttachmentInfo addMailingAttachmentInfo( Mailing mailing, String fullFileName, String attachmentFileName )
  {
    MailingAttachmentInfo mailingAttachmentInfo = new MailingAttachmentInfo();
    mailingAttachmentInfo.setFullFileName( fullFileName );
    mailingAttachmentInfo.setMailing( mailing );
    mailingAttachmentInfo.setAttachmentFileName( attachmentFileName );
    return mailingAttachmentInfo;
  }

  /**
   * Takes in a user and returns a mailing recipient object suitable for mailing service
   * 
   * @param recipient
   * @return a mailingRecipient object
   */
  private MailingRecipient createRecipient( User recipient )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    String localeCode = getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    // String localeCode = "en";
    if ( recipient.getLanguageType() != null )
    {
      localeCode = recipient.getLanguageType().getCode();
    }
    mailingRecipient.setLocale( localeCode );
    mailingRecipient.setUser( recipient );

    return mailingRecipient;
  }

  /**
   * Adds the message by name and mailing type to a mailing object
   * 
   * @param cmAssetCode
   * @param mailingType
   * @return a mailing object that is mostly assembled, except for the mailingRecipient(s)
   */
  private Mailing composeMail( String cmAssetCode, String mailingType )
  {
    Mailing mailing = composeMail();

    mailing.setMailingType( MailingType.lookup( mailingType ) );

    Message message = messageService.getMessageByCMAssetCode( cmAssetCode );
    mailing.setMessage( message );

    return mailing;
  }

  /**
   * Creates a new mailing object and add Guid, Sender and Delivery Date to it
   * 
   * @return a partially assembled mailing object
   */
  private Mailing composeMail()
  {
    Mailing mailing = new Mailing();

    // Needs Guid due to lack of a business key
    mailing.setGuid( GuidUtils.generateGuid() );

    // Sender
    String sender = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();
    mailing.setSender( sender );

    // Delivery Date - Assumes Now (i.e. immediate delivery)
    Timestamp deliveryDate = new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDate().getTime() );
    mailing.setDeliveryDate( deliveryDate );

    return mailing;
  }

  /**
   * Get the max GoalLevel sequence for all active promotions
   * 
   * @return int
   */
  public int getMaxSequence()
  {
    return this.challengepointAwardDAO.getMaxSequence();
  }

  /**
   * Get the max GoalLevel sequence for all active promotions where goal selection start
   * date has passed
   * 
   * @return int
   */
  public int getMaxSequenceWhereCpSelectionStarted()
  {
    return this.challengepointAwardDAO.getMaxSequenceWhereCpSelectionStarted();
  }

  /**
   * Get the max GoalLevel sequence for all active promotions where issue awards
   * has been run
   * 
   * @return int
   */
  public int getMaxSequenceWhereIssueAwardsRun()
  {
    return this.challengepointAwardDAO.getMaxSequenceWhereIssueAwardsRun();
  }

  public MailingService getMailingService()
  {
    return mailingService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public MessageService getMessageService()
  {
    return messageService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public SystemVariableService getSystemVariableService()
  {
    return systemVariableService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public UserService getUserService()
  {
    return userService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }
}
