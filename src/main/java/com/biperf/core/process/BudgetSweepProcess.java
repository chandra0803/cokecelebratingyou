/*
 * (c) 2008 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/BudgetSweepProcess.java,v $
 */

package com.biperf.core.process;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.promotion.hibernate.RecognitionPromotionQueryConstraint;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.budget.BudgetSegment;
import com.biperf.core.domain.budget.PromotionBudgetSweep;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.BudgetStatusType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.utils.BudgetUtils;
import com.biperf.core.utils.DateUtils;

/**
 * BudgetSweepProcess.
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
 * <td>dy</td>
 * <td>Jul 18, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class BudgetSweepProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( BudgetSweepProcess.class );

  public static final String BEAN_NAME = "budgetSweepProcess";

  private PromotionService promotionService;
  private BudgetMasterService budgetMasterService;
  private ClaimService claimService;
  private ParticipantService participantService;

  /**
   * Overridden from @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  protected void onExecute()
  {
    // List to keep track of the Promotion Budget Details for Email Processing
    List promoBudgetDetailList = new ArrayList();

    // Get the List of Promotions Eligible for Budget Sweep
    List recognitionList = getAllPromotionsEligibleForBudgetSweep();

    // For each promotion
    Iterator promoIterator = recognitionList.iterator();
    while ( promoIterator.hasNext() )
    {
      int promotionEvaluatedBudgetCount = 0;
      int promotionSweptCount = 0;
      double promotionPoints = 0;
      int promotionZeroBalance = 0;

      RecognitionPromotion promoRecognition = (RecognitionPromotion)promoIterator.next();
      BudgetMaster budgetMaster = promoRecognition.getBudgetMaster();

      // 1. Budget Master is ACTIVE
      // 2. Budget Master is of type 'PAX'
      if ( budgetMaster.isActive() && budgetMaster.getBudgetType().isPaxBudgetType() )
      {
        Set<BudgetSegment> budgetSegments = budgetMaster.getBudgetSegments();
        Date currentDateEndOfDay = DateUtils.toEndDate( new Date() );

        for ( BudgetSegment budgetSegment : budgetSegments )
        {
          if ( budgetSegment.getPromotionBudgetSweeps() != null && budgetSegment.getPromotionBudgetSweeps().size() > 0 )
          {
            PromotionBudgetSweep promoBudgetSweep = budgetSegment.getPromotionBudgetSweeps().iterator().next();
            if ( !promoBudgetSweep.isBudgetSweepRun() && promoBudgetSweep.getBudgetSweepRunDate() == null && promoBudgetSweep.getBudgetSweepDate().before( currentDateEndOfDay ) )
            {
              Set<Budget> budgets = budgetSegment.getBudgets();
              if ( null != budgets )
              {
                // Get the list of all eligible givers for the promotion in process
                Set eligibleGiverList = participantService.getAllEligiblePaxForPromotion( promoRecognition.getId(), true );

                // For each budget
                Iterator budgetIterator = budgets.iterator();
                while ( budgetIterator.hasNext() )
                {
                  Budget budget = (Budget)budgetIterator.next();
                  // 1. Budget should be ACTIVE
                  // 2. Budget User should be an Eligible Giver
                  if ( budget.getStatus().equals( BudgetStatusType.lookup( BudgetStatusType.ACTIVE ) ) && eligibleGiverList.contains( budget.getUser() ) )
                  {
                    // Only Budget with Original Value assigned are considered for sweep
                    if ( budget.getOriginalValue().doubleValue() > 0 )
                    {
                      // Keep count of the budgets being evaluated
                      promotionEvaluatedBudgetCount++;

                      // Budget with Current Value > 0 will be actually swept
                      if ( budget.getCurrentValue().doubleValue() > 0 )
                      {

                        final BigDecimal US_MEDIA_VALUE = countryService.getBudgetMediaValueByCountryCode( Country.UNITED_STATES );
                        final BigDecimal RECIPIENT_MEDIA_VALUE = ( (Participant)budget.getUser() ).getPrimaryAddress().getAddress().getCountry().getBudgetMediaValue();
                        final BigDecimal CONVERTED_BUDGET_AMOUNT = BudgetUtils.applyMediaConversion( budget.getCurrentValue(), US_MEDIA_VALUE, RECIPIENT_MEDIA_VALUE );

                        // Build the Claim
                        Claim claim = buildClaim( budget, promoRecognition, CONVERTED_BUDGET_AMOUNT );
                        try
                        {
                          // Keep track of the Points being deposited
                          promotionPoints = promotionPoints + CONVERTED_BUDGET_AMOUNT.doubleValue();

                          // Submit the Claim
                          claimService.saveClaim( claim, null, null, false, true, budgetSegment );

                          // Keep count of the budgets being swept
                          promotionSweptCount++;
                        }
                        catch( ServiceErrorException e )
                        {
                          log.error( "An exception occurred while processing claim " + BEAN_NAME + " (process invocation ID = " + getProcessInvocationId() + ")", e );

                          addComment( "An exception occurred while processing claim " + BEAN_NAME + ". See the log file for additional information.  " + "(process invocation ID = "
                              + getProcessInvocationId() + ")" );

                          throw new BeaconRuntimeException( e );
                        }
                      }
                      // All amount assigned to the budget has been used
                      else
                      {
                        // Keep count of completely used budgets
                        promotionZeroBalance++;
                      }
                    }
                  }
                }
              }

              // Update the Sweep Run details on the promo_budget_sweep
              PromotionBudgetSweep promotionBudgetSweep = budgetSegment.getPromotionBudgetSweeps().iterator().next();
              promotionBudgetSweep.setBudgetSweepRun( true );
              promotionBudgetSweep.setBudgetSweepRunDate( new Date() );
              budgetSegment.getPromotionBudgetSweeps().add( promotionBudgetSweep );
            }
          }
        }

        // Save the promotion budget Sweep, with updated details
        try
        {
          budgetMasterService.saveBudgetMaster( promoRecognition.getBudgetMaster() );
        }
        catch( ServiceErrorException e )
        {
          log.error( "An exception occurred while saving promotion Budget Sweep " + BEAN_NAME + " (process invocation ID = " + getProcessInvocationId() + ")", e );

          addComment( "An exception occurred while saving promotion Budget Sweep " + BEAN_NAME + ". See the log file for additional information.  " + "(process invocation ID = "
              + getProcessInvocationId() + ")" );

          throw new BeaconRuntimeException( e );
        }

        // Build and keep track of Promotion Budget details for Email processing
        PromotionBudgetSweepDetail details = buildPromotionBudgetSweepDetails( promoRecognition.getName(),
                                                                               promotionEvaluatedBudgetCount,
                                                                               promotionSweptCount,
                                                                               BudgetUtils.getBudgetDisplayValue( promotionPoints ),
                                                                               promotionZeroBalance );
        promoBudgetDetailList.add( details );
      }
    }

    // Send email on successful completion
    if ( promoBudgetDetailList.size() > 0 )
    {
      sendEmailToUser( promoBudgetDetailList );
    }
    else
    {
      sendEmailToUser();
    }
  }

  private List getAllPromotionsEligibleForBudgetSweep()
  {
    RecognitionPromotionQueryConstraint recQueryConstraint = new RecognitionPromotionQueryConstraint();
    recQueryConstraint.setHasBudgets( Boolean.TRUE );
    recQueryConstraint.setBudgetSweepEnabled( Boolean.TRUE );
    recQueryConstraint.setBudgetSweepRun( Boolean.FALSE );
    recQueryConstraint.setStatus( Boolean.TRUE );
    recQueryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ) } );
    return promotionService.getPromotionList( recQueryConstraint );
  }

  private PromotionBudgetSweepDetail buildPromotionBudgetSweepDetails( String promotionName, int promotionEvaluatedBudgetCount, int promotionSweptCount, int promotionPoints, int promotionZeroBalance )
  {
    PromotionBudgetSweepDetail details = new PromotionBudgetSweepDetail();

    details.setPromotionName( promotionName );
    details.setPromotionEvaluatedBudgetCount( promotionEvaluatedBudgetCount );
    details.setPromotionSweptCount( promotionSweptCount );
    details.setPromotionPoints( promotionPoints );
    details.setPromotionZeroBalance( promotionZeroBalance );

    return details;
  }

  private Node getUserNode( Participant user )
  {
    Node selectedRecipientNode = null;

    // Get all nodes the recipient is attached to
    for ( Iterator iter = user.getUserNodes().iterator(); iter.hasNext(); )
    {
      UserNode userNode = (UserNode)iter.next();
      // Pick initial active node
      if ( selectedRecipientNode == null && userNode.isActive().booleanValue() )
      {
        selectedRecipientNode = userNode.getNode();
      }
      // Compare and select the first created active recipient node
      else if ( userNode.isActive().booleanValue() && userNode.getNode().getAuditCreateInfo().getDateCreated().before( selectedRecipientNode.getAuditCreateInfo().getDateCreated() ) )
      {
        selectedRecipientNode = userNode.getNode();
      }
    }

    return selectedRecipientNode;
  }

  private Claim buildClaim( Budget budget, RecognitionPromotion promoRecognition, BigDecimal convertedBudgetAmount )
  {
    RecognitionClaim claim = new RecognitionClaim();

    Participant budgetUser = (Participant)budget.getUser();
    Node budgetUserNode = getUserNode( budgetUser );

    ClaimRecipient claimRecipient = new ClaimRecipient();
    claimRecipient.setAwardQuantity( new Long( BudgetUtils.getBudgetDisplayValue( convertedBudgetAmount ) ) );
    claimRecipient.setRecipient( budgetUser );
    claimRecipient.setNode( budgetUserNode );
    claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );

    claim.addClaimRecipient( claimRecipient );
    claim.setCopyManager( promoRecognition.isCopyRecipientManager() );
    claim.setSubmissionDate( new Timestamp( DateUtils.getCurrentDateAsLong() ) );
    claim.setPromotion( promoRecognition );
    claim.setOpen( true );
    claim.setCopySender( false );
    claim.setSubmitter( budgetUser );
    claim.setNode( budgetUserNode );
    claim.setSource( RecognitionClaimSource.WEB );
    claim.setTeamId( claimService.getNextTeamId() );

    return claim;
  }

  private void sendEmailToUser( List promoBudgetDetailList )
  {
    Iterator iter = promoBudgetDetailList.iterator();
    User recipientUser = getRunByUser();

    while ( iter.hasNext() )
    {
      PromotionBudgetSweepDetail detail = (PromotionBudgetSweepDetail)iter.next();

      Map objectMap = new HashMap();
      objectMap.put( "promotionName", detail.getPromotionName() );
      objectMap.put( "totalEvaluated", String.valueOf( detail.getPromotionEvaluatedBudgetCount() ) );
      objectMap.put( "totalSwept", String.valueOf( detail.getPromotionSweptCount() ) );
      objectMap.put( "totalZeroBalance", String.valueOf( detail.getPromotionZeroBalance() ) );
      objectMap.put( "totalPoints", String.valueOf( detail.getPromotionPoints() ) );

      // Compose the mailing
      Message m = messageService.getMessageByCMAssetCode( MessageService.BUDGET_SWEEP_PROMOTION_MESSAGE_CM_ASSETCODE );
      Mailing mailing = composeMail( m.getId(), MailingType.PROMOTION );

      // Add the recipient
      MailingRecipient mr = addRecipient( recipientUser );
      mailing.addMailingRecipient( mr );

      // Send the e-mail message with personalization
      try
      {
        mailingService.submitMailing( mailing, objectMap );

        log.debug( "--------------------------------------------------------------------------------" );
        log.debug( "process: " + BEAN_NAME + " has been sent to:" );
        log.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
        log.debug( "Total number of budgets swept: " + detail.getPromotionSweptCount() + " for promotion " + detail.getPromotionName() );
        log.debug( "--------------------------------------------------------------------------------" );

        addComment( "process: " + BEAN_NAME + " has been sent to the process scheduler " + recipientUser.getFirstName() + " " + recipientUser.getLastName() + ". Total number of budgets swept: "
            + detail.getPromotionSweptCount() + " for promotion " + detail.getPromotionName() );
      }
      catch( Exception e )
      {
        log.error( "An exception occurred while sending " + BEAN_NAME + " (process invocation ID = " + getProcessInvocationId() + ") for promotion " + detail.getPromotionName(), e );
        addComment( "An exception occurred while sending " + BEAN_NAME + ". See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId()
            + ") for promotion " + detail.getPromotionName() );
      }
    }

  }

  private void sendEmailToUser()
  {
    User recipientUser = getRunByUser();

    // Compose the mailing
    Message m = messageService.getMessageByCMAssetCode( MessageService.BUDGET_SWEEP_NO_PROMOTIONS_MESSAGE_CM_ASSETCODE );
    Mailing mailing = composeMail( m.getId(), MailingType.PROMOTION );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    // Send the e-mail message with personalization
    try
    {
      mailingService.submitMailing( mailing, null );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "process: " + BEAN_NAME + " has been sent to:" );
      log.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( "process: " + BEAN_NAME + " has been sent to the process scheduler " + recipientUser.getFirstName() + " " + recipientUser.getLastName() + ". Total number of promotions swept: 0" );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " + BEAN_NAME + " (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + BEAN_NAME + ". See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public BudgetMasterService getBudgetMasterService()
  {
    return budgetMasterService;
  }

  public void setBudgetMasterService( BudgetMasterService budgetMasterService )
  {
    this.budgetMasterService = budgetMasterService;
  }

  private class PromotionBudgetSweepDetail
  {
    private String promotionName;
    private int promotionEvaluatedBudgetCount;
    private int promotionSweptCount;
    private int promotionPoints;
    private int promotionZeroBalance;

    public int getPromotionPoints()
    {
      return promotionPoints;
    }

    public void setPromotionPoints( int promotionPoints )
    {
      this.promotionPoints = promotionPoints;
    }

    public int getPromotionEvaluatedBudgetCount()
    {
      return promotionEvaluatedBudgetCount;
    }

    public void setPromotionEvaluatedBudgetCount( int promotionEvaluatedBudgetCount )
    {
      this.promotionEvaluatedBudgetCount = promotionEvaluatedBudgetCount;
    }

    public String getPromotionName()
    {
      return promotionName;
    }

    public void setPromotionName( String promotionName )
    {
      this.promotionName = promotionName;
    }

    public int getPromotionSweptCount()
    {
      return promotionSweptCount;
    }

    public void setPromotionSweptCount( int promotionSweptCount )
    {
      this.promotionSweptCount = promotionSweptCount;
    }

    public int getPromotionZeroBalance()
    {
      return promotionZeroBalance;
    }

    public void setPromotionZeroBalance( int promotionZeroBalance )
    {
      this.promotionZeroBalance = promotionZeroBalance;
    }
  }

}
