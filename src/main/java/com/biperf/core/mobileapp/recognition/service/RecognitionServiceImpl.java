
package com.biperf.core.mobileapp.recognition.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.claim.hibernate.RecognitionClaimQueryConstraint;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.BadgeType;
import com.biperf.core.domain.gamification.GamificationBadgeTileView;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.RecognitionBean;
import com.biperf.core.value.promotion.RecognitionAdvisorPromotionValueBean;

/**
 *
 * @author ryan
 */
public class RecognitionServiceImpl implements RecognitionService
{
  private PromotionService promotionService;
  private CountryService countryService;
  private UserService userService;
  private ClaimService claimService;
  private GamificationService gamificationService;
  private ParticipantService participantService;
  private ClaimDAO claimDAO;

  @Override
  public List<EligibleRecognitionPromotion> getMobileRecognitionSubmissionList( Long userId, List<PromotionMenuBean> eligiblePromotions, boolean isUserAParticipant, Long recipientId )
  {

    List<PromotionMenuBean> promoMenuBeanLst = new ArrayList<>();
    // Added for Recognition Advisor
    if ( null != recipientId )
    {
      List<RecognitionAdvisorPromotionValueBean> recAdvisorPromoBeanLst = promotionService.getPromotionListForRA( UserManager.getUser().getUserId(), recipientId );
      List<PromotionMenuBean> eligiblePromoList = eligiblePromotions;
      recAdvisorPromoBeanLst.stream().forEach( p -> promoMenuBeanLst.add( getEligiblePromotionListForRA( eligiblePromoList, p ) ) );
      eligiblePromotions = promoMenuBeanLst;
    }

    List<PromotionMenuBean> validPromotions = new ArrayList<PromotionMenuBean>();
    for ( int i = 0; i < eligiblePromotions.size(); i++ )
    {
      PromotionMenuBean promoBean = (PromotionMenuBean)eligiblePromotions.get( i );
      if ( promoBean.isCanSubmit() )
      {
        validPromotions.add( promoBean );
      }
    }
    List<RecognitionBean> recognitionPromotions = promotionService.getRecognitionSubmissionList( userId, validPromotions, isUserAParticipant );

    Set<UserNode> userNodes = userService.getUserNodes( userId );
    UserNode primaryUserNode = getPrimaryNodeFrom( userNodes );

    List<EligibleRecognitionPromotion> promotions = new ArrayList<>();
    for ( RecognitionBean recognitionBean : recognitionPromotions )
    {
      if ( !recognitionBean.isMobileAppEnabled() )
      {
        continue;
      }

      EligibleRecognitionPromotion erp = new EligibleRecognitionPromotion( recognitionBean.getPromotion(), recognitionBean.getId(), recognitionBean.getName(), recognitionBean.getDaysRemaining() );
      erp.setBehaviors( recognitionBean.getBehaviors() );
      erp.setBehaviorRequired( recognitionBean.isBehaviorRequired() );
      erp.setRecognitionPrivateActive( recognitionBean.isRecognitionPrivateActive() );
      erp.setAllowYourOwnCard( recognitionBean.isAllowYourOwnCard() );

      if ( recognitionBean.isPointsAwardActive() )
      {
        erp.setAwardAvailable( true );

        // set the award type (fixed vs. range) and values
        erp.setAwardType( recognitionBean.getAwardType() );
        erp.setAwardFixed( recognitionBean.getAwardFixed() );
        erp.setAwardMin( recognitionBean.getAwardMin() );
        erp.setAwardMax( recognitionBean.getAwardMax() );

        Promotion promotion = erp.getPromotion();
        if ( promotion.getBudgetMaster() != null && promotion.getBudgetMaster().getBudgetType().isNodeBudgetType() )
        {
          // Org based (Node based) budgets...
          for ( UserNode un : userNodes )
          {
            setNodeAndBudgetInfoOn( erp, un, promotion, userId );
            erp.setHasMultipleOrgBasedBudgets( userNodes.size() > 1 );

            // add one copy of the promotion for each user node for which the user has a budget
            promotions.add( erp.shallowCopy() );
          }
        }
        else
        {
          // Central and Participant based budgets, or no budget...
          setNodeAndBudgetInfoOn( erp, primaryUserNode, promotion, userId );
          promotions.add( erp );
        }
      }
      else
      {
        // use the user's primary node as the node ID
        erp.setNodeId( primaryUserNode.getNode().getId() );

        // set the last sent and total number sent
        setHistoryInfo( erp, userId );

        // badges (we don't need no stinkin' badges)
        setBadgeProgressionInfo( erp, userId );

        // be sure to add it to the promotions list!
        promotions.add( erp );
      }
    }

    return promotions;
  }

  protected void setNodeAndBudgetInfoOn( EligibleRecognitionPromotion erp, UserNode userNode, Promotion promotion, Long userId )
  {
    erp.setNodeName( userNode.getNode().getName() );
    erp.setNodeId( userNode.getNode().getId() );

    Budget b = getAvailableBudget( promotion, userId, userNode.getNode().getId() );
    if ( b != null )
    {
      BudgetInfo budgetInfo = new BudgetInfo( userNode.getNode().getId(), b );
      erp.setBudgetInfo( budgetInfo );
    }
  }

  @Override
  public List<Participant> findRecentRecipientsFor( Long userId, Long promotionId )
  {
    List<Participant> mostRecent = new ArrayList<>();

    List<ClaimRecipient> mostRecentClaimRecipients = claimDAO.findMostRecentRecipientsFor( userId );

    if ( mostRecentClaimRecipients != null )
    {
      for ( ClaimRecipient cr : mostRecentClaimRecipients )
      {
        if ( !mostRecent.contains( cr.getRecipient() ) && cr.getRecipient().isActive() && cr.getRecipient().getStatus().isActive() )
        {
          mostRecent.add( cr.getRecipient() );
        }

        if ( mostRecent.size() >= 20 )
        {
          break;
        }
      }
    }

    mostRecent = filterUsers( mostRecent, promotionId );

    return mostRecent;
  }

  @Override
  public List<Participant> getTeamMemberRecipients( Long userId, Long promotionId )
  {
    // get the user
    User user = userService.getUserById( userId );

    // get the user's nodes
    Set<UserNode> userNodes = user.getUserNodes();

    // get all of the team members in the user's nodes, using a Map
    // to filter out any possible duplicates
    Map<Long, User> teamMembers = new HashMap<>();
    for ( UserNode un : userNodes )
    {
      List<User> users = userService.getAllUsersOnNode( un.getNode().getId() );
      for ( User u : users )
      {
        // be sure to exlude the user
        if ( !userId.equals( u.getId() ) && u.isParticipant() && u.isActive() )
        {
          teamMembers.put( u.getId(), u );
        }
      }
    }

    List<Participant> teamMemberRecipients = new ArrayList<>();
    for ( User u : teamMembers.values() )
    {
      Participant pax = participantService.getParticipantById( u.getId() );
      if ( pax.getStatus().isActive() )
      {
        teamMemberRecipients.add( pax );
      }
    }

    teamMemberRecipients = filterUsers( teamMemberRecipients, promotionId );

    return teamMemberRecipients;
  }

  protected List<Participant> filterUsers( List<Participant> participants, Long promotionId )
  {
    if ( participants != null && participants.size() > 0 )
    {
      Promotion promotion = getPromotion( promotionId );
      for ( Iterator paxIter = participants.iterator(); paxIter.hasNext(); )
      {
        Participant pax = (Participant)paxIter.next();
        if ( !promotionService.isParticipantMemberOfPromotionAudience( pax, promotion, false, null ) )
        {
          paxIter.remove();
        }
      }
    }
    return participants;
  }

  protected Promotion getPromotion( Long promotionId )
  {
    AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
    promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    Promotion promotion = promotionService.getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );
    return promotion;
  }

  private void setBadgeProgressionInfo( EligibleRecognitionPromotion erp, Long userId )
  {
    List<ParticipantBadge> participantBadges = gamificationService.getBadgeByParticipantPromotionSorted( erp.getId(), userId, 0 );

    // we only want progression badges that are for total or sent, we don't care about received
    // also on mobile we only want to display the current progress badge, if there are no
    // current progress badges, then display the last badge earned.
    List<ParticipantBadge> progressionBadges = new ArrayList<>();
    for ( ParticipantBadge pb : participantBadges )
    {
      if ( BadgeType.PROGRESS.equals( pb.getBadgePromotion().getBadgeType().getCode() ) && !pb.getBadgePromotion().getBadgeCountType().getCode().equals( "received" ) )
      {
        if ( pb.getIsEarned() != null && pb.getIsEarned().booleanValue() && participantBadges.size() == 1 )
        {
          progressionBadges.add( pb );
          break;
        }
        if ( pb.getIsEarned() != null && !pb.getIsEarned().booleanValue() && participantBadges.size() > 1 && pb.getSentCount() > 0 )
        {
          progressionBadges.add( pb );
          break;
        }
      }
    }

    // convert to BadgeDetails for the UI
    // List<BadgeDetails> allBadgeDetails = gamificationService.toBadgeDetails(progressionBadges);
    GamificationBadgeTileView tile = gamificationService.getTileViewJson( progressionBadges );

    erp.setBadgeDetails( tile.getBadgeInfo().getBadgeDetails() );
  }

  private void setHistoryInfo( EligibleRecognitionPromotion erp, Long userId )
  {
    // get the last time one was sent....
    Date startDate = DateUtils.toDate( "01/01/1900" );
    Date endDate = DateUtils.toDate( "12/31/2500" );
    List<Claim> claims = getClaimList( userId, erp.getId(), startDate, endDate );

    // sort by descending date
    Comparator<Claim> comparator = new Comparator<Claim>()
    {
      @Override
      public int compare( Claim one, Claim two )
      {
        return two.getSubmissionDate().compareTo( one.getSubmissionDate() );
      }
    };
    Collections.sort( claims, comparator );

    if ( claims != null && !claims.isEmpty() )
    {
      Claim mostRecent = claims.get( 0 );
      erp.setLastSent( DateUtils.getElapsedDays( mostRecent.getSubmissionDate(), new Date() ) );
    }

    // set the total sent
    if ( claims != null )
    {
      erp.setTotalSent( claims.size() );
    }
  }

  private List<Claim> getClaimList( Long userId, Long promotionId, Date startDate, Date endDate )
  {
    // Setup the query constraint.
    RecognitionClaimQueryConstraint queryConstraint = new RecognitionClaimQueryConstraint();

    queryConstraint.setSubmitterId( userId );
    queryConstraint.setProxyUserId( null );

    if ( promotionId != null && promotionId.longValue() != 0 )
    {
      queryConstraint.setIncludedPromotionIds( new Long[] { promotionId } );
    }

    queryConstraint.setStartDate( startDate );
    queryConstraint.setEndDate( endDate );

    // Setup the association request collection.
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

    // Get the claim list.
    return claimService.getClaimListWithAssociations( queryConstraint, associationRequestCollection );
  }

  private Budget getAvailableBudget( Promotion promotion, Long participantId, Long nodeId )
  {
    Budget budget = null;

    if ( promotion.isBudgetUsed() )
    {
      Budget persistedBudget = promotionService.getAvailableBudget( promotion.getId(), participantId, nodeId );

      budget = new Budget();
      budget.setId( persistedBudget.getId() );
      budget.setBudgetSegment( persistedBudget.getBudgetSegment() );
      budget.setOriginalValue( persistedBudget.getOriginalValue() );
      budget.setCurrentValue( persistedBudget.getCurrentValue() );

      if ( !promotion.getBudgetMaster().isCentralBudget() )
      {
        final BigDecimal US_MEDIA_VALUE = countryService.getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
        final BigDecimal USER_MEDIA_VALUE = userService.getBudgetMediaValueForUser( participantId );

        budget.setOriginalValue( BudgetUtils.applyMediaConversion( persistedBudget.getOriginalValue(), US_MEDIA_VALUE, USER_MEDIA_VALUE ) );
        budget.setCurrentValue( BudgetUtils.applyMediaConversion( persistedBudget.getCurrentValue(), US_MEDIA_VALUE, USER_MEDIA_VALUE ) );
      }
    }

    return budget;
  }

  private UserNode getPrimaryNodeFrom( Set<UserNode> userNodes )
  {
    UserNode primary = null;
    for ( UserNode un : userNodes )
    {
      if ( un.getIsPrimary() )
      {
        primary = un;
        break;
      }
    }
    return primary;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setCountryService( CountryService countryService )
  {
    this.countryService = countryService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setClaimDAO( ClaimDAO claimDAO )
  {
    this.claimDAO = claimDAO;
  }

  private PromotionMenuBean getEligiblePromotionListForRA( List<PromotionMenuBean> eligiblePromotionList, RecognitionAdvisorPromotionValueBean raPromoValueBean )
  {
    return eligiblePromotionList.stream().filter( p -> p.getPromotion().getId().equals( raPromoValueBean.getPromotionId() ) ).findFirst().get();
  }
}
