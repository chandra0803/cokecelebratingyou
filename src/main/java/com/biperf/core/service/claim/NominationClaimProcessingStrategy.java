/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/NominationClaimProcessingStrategy.java,v $
 */

package com.biperf.core.service.claim;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.activity.NominationActivity;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.ApprovableItemApprover;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.claim.ClaimGroupApprover;
import com.biperf.core.domain.claim.ClaimItemApprover;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.NominationPromotionLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.NominationFacts;
import com.biperf.core.service.promotion.engine.PayoutCalculationFacts;
import com.biperf.core.service.promotion.engine.PayoutCalculationResult;
import com.biperf.core.utils.ClaimApproveUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;

/**
 * NominationClaimProcessingStrategy.
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
 * <td>zahler</td>
 * <td>Oct 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NominationClaimProcessingStrategy extends AbstractClaimProcessingStrategy
{
  private PromotionService promotionService;
  private MailingService mailingService;
  private static final Log log = LogFactory.getLog( RecognitionClaimProcessingStrategy.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#getPromotionPayoutType(com.biperf.core.domain.promotion.Promotion)
   * @param promotion
   * @return String promotion payout type
   */
  public String getPromotionPayoutType( Promotion promotion )
  {
    return PromotionType.NOMINATION;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#getPayoutCalculationFacts(Approvable,
   *      com.biperf.core.domain.promotion.Promotion,
   *      com.biperf.core.domain.participant.Participant)
   * @param approvable
   * @param promotion
   * @param participant
   * @return PayoutCalculationFacts
   */
  protected PayoutCalculationFacts getPayoutCalculationFacts( Approvable approvable, Promotion promotion, Participant participant )
  {

    return new NominationFacts( approvable, participant );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#createActivitiesForApprovable(Approvable)
   * @param approvable
   * @return List
   */
  protected List createActivitiesForApprovable( Approvable approvable )
  {
    List activities = new ArrayList();

    if ( isWinning( approvable ) )
    {
      if ( approvable instanceof ClaimGroup )
      {
        ClaimGroup claimGroup = (ClaimGroup)approvable;

        Participant nominee = claimGroup.getParticipant();

        // Add nominee activity
        NominationActivity activity = createNomineeActivity( claimGroup, nominee, claimGroup.getNode(), claimGroup.getAwardQuantity(), claimGroup.getCashAwardQuantity() );
        activities.add( activity );

        // Add nominator activities for sweepstakes purposes for claim group's claims.
        Set alreadyAddedNominators = new LinkedHashSet();
        for ( Iterator iter = claimGroup.getClaims().iterator(); iter.hasNext(); )
        {
          Claim claim = (Claim)iter.next();
          // Only make nominator activity if nominator isn't nominee (nominator can't be entry in
          // both nominee and nominator pool, only nominator).
          // Also, if duplicate nominator only create one activity.
          Participant nominator = claim.getSubmitter();
          if ( !nominee.equals( nominator ) && !alreadyAddedNominators.contains( nominator ) )
          {
            activities.add( createNominatorActivity( claimGroup, claim.getSubmissionDate(), nominator, claim.getNode() ) );
            alreadyAddedNominators.add( nominator );
          }
        }

      }
      else if ( approvable instanceof NominationClaim )
      {
          NominationClaim nominationClaim = (NominationClaim)approvable;

          boolean isNominatorNominee = false;
          Participant nominator = nominationClaim.getSubmitter();

          // Add Nominee activity(s) - team treated differently than individual
          if ( nominationClaim.isTeam() )
          {
            // Get award quantity from single team claimRecipient
            ClaimRecipient claimRecipient = (ClaimRecipient)nominationClaim.getClaimRecipients()
                .iterator().next();
            Long awardQuantity = claimRecipient.getAwardQuantity();

            // Add Nominee activities from team members
            for ( Iterator iter = nominationClaim.getTeamMembers().iterator(); iter.hasNext(); )
            {
              ProductClaimParticipant teamMember = (ProductClaimParticipant)iter.next();
              Participant nominee = teamMember.getParticipant();
              NominationActivity activity = createNomineeActivity( nominationClaim,
                                                                   nominee,
                                                                   teamMember.getNode(),
                                                                   awardQuantity ,claimRecipient.getCashAwardQuantity());
              activities.add( activity );

              if ( nominee.equals( nominator ) )
              {
                isNominatorNominee = true;
              }
            }
          }
          else
          {
            // Individual

            // Add Nominee activity from Claim Recipient
            ClaimRecipient claimRecipient = (ClaimRecipient)nominationClaim.getClaimRecipients()
                .iterator().next();

            Participant nominee = claimRecipient.getRecipient();
            NominationActivity activity = createNomineeActivity( nominationClaim,
                                                                 nominee,
                                                                 claimRecipient.getNode(),
                                                                 claimRecipient.getAwardQuantity(),claimRecipient.getCashAwardQuantity());
            activities.add( activity );

            if ( nominee.equals( nominator ) )
            {
              isNominatorNominee = true;
            }
          }

          // Add nominator activity for sweepstakes purposes.
          if ( !isNominatorNominee )
          {
            // Only make nominator activity if nominator isn't nominee (nominator can't be entry in
            // both nominee and nominator pool, only nominator).
            activities.add( createNominatorActivity( nominationClaim, nominationClaim
                .getSubmissionDate(), nominator, nominationClaim.getNode() ) );
          }
        }
      else
      {
        throw new BeaconRuntimeException( "Unhandled approvable type: " + approvable.getClass().getName() );
      }
    }

    return activities;
  }

  private boolean isWinning( Approvable approvable )
  {
    Set<ApprovableItem> approvableItems = approvable.getApprovableItems();
    for ( ApprovableItem approvableItem : approvableItems )
    {
      if ( approvableItem instanceof ClaimRecipient )
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)approvableItem;

        Set<ClaimItemApprover> claimItemApprovers = claimRecipient.getApprovableItemApprovers();

        for ( ClaimItemApprover claimItemApprover : claimItemApprovers )
        {
          if ( claimItemApprovers.size() == claimItemApprover.getApprovalRound().intValue() )
          {
            return claimItemApprover.getApprovalStatusType().getCode().equals( ApprovalStatusType.WINNER );
          }
        }
      }
      else if ( approvableItem instanceof ClaimGroup )
      {
        ClaimGroup claimGroup = (ClaimGroup)approvableItem;
        Set<ClaimGroupApprover> claimGroupApprovers = claimGroup.getApprovableItemApprovers();
        for ( ClaimGroupApprover claimGroupApprover : claimGroupApprovers )
        {
          if ( claimGroupApprovers.size() == claimGroupApprover.getApprovalRound().intValue() )
          {
            return claimGroupApprover.getApprovalStatusType().getCode().equals( ApprovalStatusType.WINNER );
          }
        }
      }
    }
    return false;
  }

  private NominationActivity createNomineeActivity( Approvable approvable, Participant participant, Node node, Long awardQuantity, BigDecimal cashAwardQuantity )
  {
    NominationActivity activity = new NominationActivity( GuidUtils.generateGuid() );
    ApprovableItem approvableItem = (ApprovableItem)approvable.getApprovableItems().iterator().next();

    if ( approvable instanceof ClaimGroup )
    {
      activity.setClaimGroup( (ClaimGroup)approvable );
      activity.setSubmissionDate( new Date() );
    }
    else
    {
      Claim claim = (Claim)approvable;
      activity.setClaim( claim );
      activity.setSubmissionDate( claim.getSubmissionDate() );
    }

    activity.setPromotion( approvable.getPromotion() );
    activity.setParticipant( participant );
    activity.setNode( node );
    activity.setApprovalRound( (long)approvableItem.getApprovableItemApprovers().size() );

    if ( ( (AbstractRecognitionPromotion)approvable.getPromotion() ).isAwardActive() )
    {
      NominationPromotion nominationPromotion = (NominationPromotion)approvable.getPromotion();
      for ( NominationPromotionLevel nominationPromotionLevel : nominationPromotion.getNominationLevels() )
      {
        if ( nominationPromotionLevel.getLevelIndex().intValue() == approvableItem.getApprovableItemApprovers().size() )
        {
          if ( nominationPromotionLevel.getAwardPayoutType() != null && nominationPromotionLevel.getAwardPayoutType().isPointsAwardType() )
          {
            if ( participant.getOptOutAwards() )
            {
              activity.setAwardQuantity( new Long( 0 ) );
            }
            else
            {
              activity.setAwardQuantity( awardQuantity );
            }
          }
          else if ( nominationPromotionLevel.getAwardPayoutType() != null && nominationPromotionLevel.getAwardPayoutType().isCashAwardType() )
          {
            activity.setCashAwardQuantity( cashAwardQuantity );
          }
          break;
        }
      }
    }
    else
    {
      activity.setAwardQuantity( new Long( 0 ) );
      activity.setCashAwardQuantity( new BigDecimal( 0 ) );
    }

    activity.setSubmitter( false );
    return activity;
  }

  private NominationActivity createNominatorActivity( Approvable approvable, Date submissionDate, Participant nominator, Node nominatorNode )
  {
    NominationActivity activity = new NominationActivity( GuidUtils.generateGuid() );
    ApprovableItem approvableItem = (ApprovableItem)approvable.getApprovableItems().iterator().next();

    if ( approvable instanceof ClaimGroup )
    {
      activity.setClaimGroup( (ClaimGroup)approvable );
    }
    else
    {
      Claim claim = (Claim)approvable;
      activity.setClaim( claim );
    }

    activity.setSubmissionDate( submissionDate );
    activity.setPromotion( approvable.getPromotion() );
    activity.setParticipant( nominator );
    activity.setNode( nominatorNode );
    activity.setApprovalRound( (long)approvableItem.getApprovableItemApprovers().size() );

    activity.setAwardQuantity( new Long( 0 ) );
    activity.setCashAwardQuantity( new BigDecimal( 0 ) );

    activity.setSubmitter( true );
    return activity;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#getValidParticipantsForApprovable(Approvable,
   *      com.biperf.core.domain.promotion.Promotion)
   * @param approvable
   * @param promotion
   * @return List
   */
  protected List getValidParticipantsForApprovable( Approvable approvable, Promotion promotion )
  {
    // Return all activity participants
    List validParticipants = new ArrayList();

    List activities = createActivitiesForApprovable( approvable );
    for ( Iterator iter = activities.iterator(); iter.hasNext(); )
    {
      NominationActivity nominationActivity = (NominationActivity)iter.next();
      validParticipants.add( nominationActivity.getParticipant() );
    }

    return validParticipants;
  }

  /**
   * Just set Budget on Journal, don't change budget, that was done at approval time for nomination.
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#calculateBudget(java.util.Set,
   *      Approvable)
   * @param payoutCalculationResults
   */
  protected void calculateBudget( Set payoutCalculationResults, Approvable approvable )
  {
    NominationPromotion promotion = (NominationPromotion)approvable.getPromotion();

    // If promotion doesn't use budget, no calculation is necessary.
    if ( !promotion.isBudgetUsed() && !promotion.isCashBudgetUsed() )
    {
      return;
    }

    // if no payout for this claim, no calculation is necessary.
    if ( payoutCalculationResults.isEmpty() )
    {
      return;
    }

    Budget budget = null;

    if ( approvable instanceof NominationClaim )
    {
      NominationClaim nominationClaim = (NominationClaim)approvable;
      String awardPayoutType = null;
      int approvalLevel = nominationClaim.getApprovableItemApproversSize();
      for ( NominationPromotionLevel nominationPromotionLevel : promotion.getNominationLevels() )
      {
        if ( nominationPromotionLevel.getLevelIndex() != null && nominationPromotionLevel.getLevelIndex().intValue() == approvalLevel )
        {
          awardPayoutType = nominationPromotionLevel.getAwardPayoutType().getCode();
          break;
        }
      }
      if ( PromotionAwardsType.POINTS.equals( awardPayoutType ) && promotion.isBudgetUsed() || PromotionAwardsType.CASH.equals( awardPayoutType ) && promotion.isCashBudgetUsed() )
      {
        Participant submitter = nominationClaim.getSubmitter();
        budget = promotionService.getNominationPromotionAvailableBudgetByAwardType( promotion, submitter, null, awardPayoutType );
        /*
         * This ReferenceVariableForClaimId which we set in budget is just a reference variable for
         * claimId which is used in GenericPostUpdateEventListener to save it in BudgetHistory table
         */
        budget.setReferenceVariableForClaimId( nominationClaim.getId() );
      }
      else
      {
        return;
      }
    }
    if ( approvable instanceof ClaimGroup )
    {
      ClaimGroup claimGroup = (ClaimGroup)approvable;
      String awardPayoutType = null;
      // As nomination promotion will have only central budget, we can check the first claim
      // submitter for budget availability
      NominationClaim firstClaim = (NominationClaim)claimGroup.getClaims().iterator().next();
      int approvalLevel = claimGroup.getApprovableItemApproversSize();
      for ( NominationPromotionLevel nominationPromotionLevel : promotion.getNominationLevels() )
      {
        if ( nominationPromotionLevel.getLevelIndex() != null && nominationPromotionLevel.getLevelIndex().intValue() == approvalLevel )
        {
          awardPayoutType = nominationPromotionLevel.getAwardPayoutType().getCode();
          break;
        }
      }
      if ( PromotionAwardsType.POINTS.equals( awardPayoutType ) && promotion.isBudgetUsed() || PromotionAwardsType.CASH.equals( awardPayoutType ) && promotion.isCashBudgetUsed() )
      {
        Participant submitter = firstClaim.getSubmitter();
        budget = promotionService.getNominationPromotionAvailableBudgetByAwardType( promotion, submitter, null, awardPayoutType );
        /*
         * This ReferenceVariableForClaimId which we set in budget is just a reference variable for
         * claimId which is used in GenericPostUpdateEventListener to save it in BudgetHistory table
         */
        budget.setReferenceVariableForClaimId( firstClaim.getId() );
      }
      else
      {
        return;
      }
    }
    if ( budget == null )
    {
      throw new BeaconRuntimeException( "No budget found for Claim " + approvable.getId() );
    }

    budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEDUCT ) );

    for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
    {
      PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();

      Journal journal = payoutCalculationResult.getJournal();

      // set budget on journal records as we go
      if ( journal != null )
      {
        journal.setBudget( budget );
      }
    }
  }

  /**
   * This postProcess implementation for Nomination will check to see if any point awards were given for
   * this claim. If point awards are not active on this promotion, need to send nomination emails here,
   * otherwise JournalServiceImpl sends the nomination email AFTER the award has been deposited.
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#postProcess(java.util.Set,
   *      Approvable)
   * @param payoutCalculationResults
   * @param approvable
   * @throws ServiceErrorException 
   */
  public void postProcess( Set payoutCalculationResults, Approvable approvable ) throws ServiceErrorException
  {
    try
    {
      NominationPromotion promotion = (NominationPromotion)approvable.getPromotion();

      NominationClaim nominationClaim = null;
      if ( promotion.isCumulative() )
      {
        nominationClaim = (NominationClaim)approvable;

        ClaimGroup claimGroup = nominationClaim.getClaimGroup();
        if ( claimGroup != null )
        {
          Set claims = claimGroup.getClaims();
          for ( Iterator cliamsIterator = claims.iterator(); cliamsIterator.hasNext(); )
          {
            nominationClaim = (NominationClaim)cliamsIterator.next();
            NominationPromotionLevel currentLevel = getWinningLevel( promotion, claimGroup.getApprovableItemApproversSize() );
            if ( shouldSendNominationMailing( promotion, currentLevel ) )
            {
              ClaimRecipient claimRecipient = (ClaimRecipient)nominationClaim.getClaimRecipients().iterator().next();
              ApprovableItemApprover approverItem = getApproverForLevel( nominationClaim, claimRecipient, currentLevel, NominationEvaluationType.lookup( NominationEvaluationType.CUMULATIVE ) );
              if ( approverItem != null && ApprovalStatusType.WINNER.equals( approverItem.getApprovalStatusType().getCode() ) && ClaimApproveUtils.isApprovalRoundOver( approvable ) )
              {
                sendNominationMailing( nominationClaim );                
              }
            }
            break;
          }
        }
      }
      else if ( approvable instanceof Claim )
      {
        nominationClaim = (NominationClaim)approvable;
        NominationPromotionLevel currentLevel = getWinningLevel( promotion, nominationClaim.getApprovableItemApproversSize() );

        if ( shouldSendNominationMailing( promotion, currentLevel ) )
        {
          /* Bug # 36806 START */
          Set<ClaimRecipient> recipient = nominationClaim.getClaimRecipients();
          // ClaimRecipient claimRecipient =
          // (ClaimRecipient)nominationClaim.getClaimRecipients().iterator().next();

          for ( ClaimRecipient claimRecipient2 : recipient )
          {
            ApprovableItemApprover approverItem = getApproverForLevel( nominationClaim, claimRecipient2, currentLevel, NominationEvaluationType.lookup( NominationEvaluationType.INDEPENDENT ) );
            if ( approverItem != null && ApprovalStatusType.WINNER.equals( approverItem.getApprovalStatusType().getCode() ) )
            {
              sendNominationMailing( nominationClaim );
            }
            break;
          }
          /* Bug # 36806 end */
        } else {
            nominationClaim = (NominationClaim)approvable;
            
            /*Bug # 36806 START */
            ClaimRecipient claimRecipient = (ClaimRecipient)nominationClaim.getClaimRecipients()
            .iterator().next();
            String approvalStatusTypeCode = claimRecipient.getApprovalStatusType().getCode(); 
            if (approvalStatusTypeCode != null && !approvalStatusTypeCode.equals( ApprovalStatusType.NON_WINNER))
            {
              sendNominationMailing(nominationClaim);
            }
            /* Bug # 36806  end */
          }
      }

    }
    catch( Exception ex )
    {
      log.error( "Error in postProcess ", ex );
      throw new ServiceErrorException( ex.getMessage(), ex );
    }

  }

  /**
   * @return True if awards are not active, or if the award is cash or other. (False only for points award)
   */
  private boolean shouldSendNominationMailing( NominationPromotion nominationPromotion, NominationPromotionLevel currentLevel )
  {
    if ( !nominationPromotion.isAwardActive() )
    {
      return true;
    }

    if ( currentLevel != null && ( currentLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.CASH ) || currentLevel.getAwardPayoutType().getCode().equals( PromotionAwardsType.OTHER ) ) )
    {
      return true;
    }

    return false;
  }

  /**
   * Find which level a win was at for the winner emails. Needs to account for multiple level scenarios where 
   * the current level might already have been advanced.
   */
  private NominationPromotionLevel getWinningLevel( NominationPromotion promotion, int approvalRound )
  {
    NominationPromotionLevel winningLevel = null;

    Long winningRound = (long)approvalRound;
    winningLevel = promotion.getNominationLevels().stream().filter( ( level ) -> level.getLevelIndex().equals( winningRound ) ).findAny().orElse( null );

    return winningLevel;
  }

  /**
   * Return the approver which marked a claim as a winner at the given level, or null
   */
  @SuppressWarnings( "unchecked" )
  private ApprovableItemApprover getApproverForLevel( NominationClaim claim, ClaimRecipient claimRecipient, NominationPromotionLevel currentLevel, NominationEvaluationType evaluationType )
  {
    ApprovableItemApprover approverItem = null;
    if ( evaluationType.equals( NominationEvaluationType.lookup( NominationEvaluationType.CUMULATIVE ) ) )
    {
      if ( claim.getClaimGroup().getApprovableItemApprovers() != null && currentLevel != null )
      {
        approverItem = (ApprovableItemApprover)claim.getClaimGroup().getApprovableItemApprovers().stream()
            .filter( ( aia ) -> ( (ApprovableItemApprover)aia ).getApprovalRound().equals( currentLevel.getLevelIndex() ) ).findAny().orElse( null );
      }
    }
    else
    {
      if ( claimRecipient.getApprovableItemApprovers() != null && currentLevel != null )
      {
        approverItem = (ApprovableItemApprover)claimRecipient.getApprovableItemApprovers().stream()
            .filter( ( aia ) -> ( (ApprovableItemApprover)aia ).getApprovalRound().equals( currentLevel.getLevelIndex() ) ).findAny().orElse( null );
      }
    }
    return approverItem;
  }

  protected void sendNominationMailing( NominationClaim nominationClaim )
  {   
    // Bug # 37498 - added userId
    List mailings = mailingService.buildNominationMailing( nominationClaim, null );
    for ( Iterator iter = mailings.iterator(); iter.hasNext(); )
    {
      Mailing mailing = (Mailing)iter.next();
      mailingService.submitMailing( mailing, null );
    }
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }
}
