/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/util/impl/BudgetCalculatorImpl.java,v $
 */

package com.biperf.core.service.util.impl;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.audit.PayoutCalculationAudit;
import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.enums.BudgetActionType;
import com.biperf.core.domain.enums.BudgetFinalPayoutRule;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.email.EmailNotificationService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.PayoutCalculationResult;
import com.biperf.core.service.util.BudgetCalculator;

/*
 * BudgetCalculatorImpl <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Jun
 * 27, 2006</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class BudgetCalculatorImpl implements BudgetCalculator
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private AwardBanQServiceFactory awardBanQServiceFactory;
  private EmailNotificationService emailNotificationService;
  private JournalService journalService;
  private PromotionService promotionService;
  private MailingService mailingService;

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  public void calculateBudget( Set payoutCalculationResults, Approvable approvable )
  {
    Set promotions = new LinkedHashSet();

    if ( payoutCalculationResults != null && !payoutCalculationResults.isEmpty() )
    {
      PayoutCalculationResultsAdapter payoutCalculationResultsAdapter = new PayoutCalculationResultsAdapter( payoutCalculationResults );

      if ( approvable != null )
      {
        ClaimAdapter claimAdapter = new ClaimAdapter( (Claim)approvable );

        promotions.addAll( claimAdapter.getPromotions() );

        for ( Iterator iter = promotions.iterator(); iter.hasNext(); )
        {
          Promotion promotion = (Promotion)iter.next();
          if ( promotion.isBudgetUsed() )
          {
            Budget budget = claimAdapter.getBudget( promotion );
            if ( budget != null )
            {
              if ( budget.getBudgetSegment().isExpired( claimAdapter.claim.getSubmissionDate() ) )
              {
                payoutCalculationResultsAdapter.cancelPayouts( promotion );

                // The budget date has expired before the promotion date and claim date
                String msg_body = "Budget for promotion " + promotion.getName() + " has expired ";
                mailingService.submitSystemMailing( "Break the Bank Budget Expired", msg_body, msg_body );
              }
              else
              // budget date is valid
              {

                int totalPayout = payoutCalculationResultsAdapter.calculateTotalPayout( promotion );

                BigDecimal newBudgetValue = budget.getCurrentValue().subtract( BigDecimal.valueOf( totalPayout ) );

                if ( newBudgetValue.intValue() >= 0 )
                {
                  budget.setCurrentValue( newBudgetValue );
                }
                else
                // newBudgetValue < 0
                {
                  if ( budget.getCurrentValueDisplay() > 0 )
                  {
                    // first time overdrawing the budget so send an email to system mailbox
                    // saying the budget is overdrawn
                    String body = "Budget for promotion " + promotion.getName() + " was overdrawn by claim " + approvable.getId();
                    mailingService.submitSystemMailing( "Break the Bank Budget Overdrawn", body, body );

                    BudgetMaster budgetMaster = budget.getBudgetSegment().getBudgetMaster();
                    BudgetFinalPayoutRule finalPayoutRule = budgetMaster.getFinalPayoutRule();
                    if ( finalPayoutRule.isFullPayout() )
                    {
                      budget.setCurrentValue( newBudgetValue );
                    }
                    else if ( finalPayoutRule.isPartialPayout() )
                    {
                      payoutCalculationResultsAdapter.processPartialPayouts( budget, promotion );
                    }
                    else if ( finalPayoutRule.isNoPayout() )
                    {
                      // BugFix 18807,Do not return here,as this loop might has to be repeated for
                      // other child promos
                      payoutCalculationResultsAdapter.cancelPayouts( promotion );
                    }
                  }
                  else
                  // oldBudgetValue <= 0
                  {
                    // BugFix 18807,Do not return here,as this loop might has to be repeated for
                    // other child promos
                    payoutCalculationResultsAdapter.cancelPayouts( promotion );
                  }
                }
                budget.setActionType( BudgetActionType.lookup( BudgetActionType.DEDUCT ) );
                payoutCalculationResultsAdapter.setBudget( budget, promotion );
              }
            }
          }
        }
      }
    }
  }

  // ---------------------------------------------------------------------------
  // Service Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the deployment-dependent implementation of the AwardBanQ service.
   * 
   * @return the deployment-dependent implementation of the AwardBanQ service.
   */
  public AwardBanQService getAwardBanQService()
  {
    return awardBanQServiceFactory.getAwardBanQService();
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public void setEmailNotificationService( EmailNotificationService emailNotificationService )
  {
    this.emailNotificationService = emailNotificationService;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  private class ClaimAdapter
  {
    /**
     * the claim to which this class adds behavior
     */
    private Claim claim;

    /**
     * Constructs an <code>ApprovableWrapper</code> object.
     * 
     * @param claim the claim to which behavior will be added.
     */
    public ClaimAdapter( Claim claim )
    {
      this.claim = claim;
    }

    /**
     * Returns the active budget for this claim.
     * 
     * @return the active budget for this claim.
     */
    public Budget getBudget()
    {
      Promotion promotion = claim.getPromotion();
      Participant participant = claim.getSubmitter();
      Node node = claim.getNode();

      return promotionService.getAvailableBudget( promotion.getId(), participant.getId(), node.getId() );
    }

    /**
     * Returns the active budget for this claim.
     * 
     * @return the active budget for this claim.
     */
    public Budget getBudget( Promotion promotion )
    {
      Participant participant = claim.getSubmitter();
      Node node = claim.getNode();

      return promotionService.getAvailableBudget( promotion.getId(), participant.getId(), node.getId() );
    }

    /**
     * Returns the promotion associated with this claim.
     * 
     * @return the promotion associated with this claim.
     */
    public Promotion getPromotion()
    {
      return claim.getPromotion();
    }

    public Set getPromotions()
    {
      Set promotions = new LinkedHashSet();

      if ( ! ( claim instanceof ProductClaim ) )
      {
        promotions.add( claim.getPromotion() );
        return promotions;
      }
      ProductClaim productClaim = (ProductClaim)claim;

      Set masterAndChildPromotions = new LinkedHashSet();
      Promotion masterPromotion = productClaim.getPromotion();
      masterAndChildPromotions.add( masterPromotion );
      if ( masterPromotion.isProductClaimPromotion() )
      {
        masterAndChildPromotions.addAll( ( (ProductClaimPromotion)masterPromotion ).getChildPromotions() );
      }

      for ( Iterator iter = masterAndChildPromotions.iterator(); iter.hasNext(); )
      {
        ProductClaimPromotion promotion = (ProductClaimPromotion)iter.next();
        if ( !promotion.isUnderConstruction() && promotionService.isPromotionClaimableByParticipant( promotion.getId(), productClaim.getSubmitter() ) )
        {
          promotions.add( promotion );
        }
      }
      return promotions;
    }

  }

  private class PayoutCalculationResultsAdapter
  {
    /**
     * the payout calculation results to which this class adds behavior. A <code>Set</code> of
     * {@link com.biperf.core.service.promotion.engine.PayoutCalculationResult} objects.
     */
    private Set payoutCalculationResults;

    /**
     * Constructs an <code>PayoutCalculationResultsAdapter</code> object.
     * 
     * @param payoutCalculationResults the pay calculation results to which behavior will be added.
     */
    public PayoutCalculationResultsAdapter( Set payoutCalculationResults )
    {
      this.payoutCalculationResults = payoutCalculationResults;
    }

    /**
     * Calculates the total payout for this set of payout calculation results.
     * 
     * @return the total payout for this set of payout calculation results.
     */
    public int calculateTotalPayout( Promotion promotion )
    {
      double totalPayout = 0;
      for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
      {
        PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();
        Promotion payoutPromotion = null;
        if ( promotion.getPromotionType().getCode().equals( PromotionType.PRODUCT_CLAIM ) )
        {
          if ( payoutCalculationResult.getPromotionPayoutGroup() != null )
          {
            payoutPromotion = payoutCalculationResult.getPromotionPayoutGroup().getPromotion();
          }
          else
          {
            continue;
          }
        }
        else
        {
          if ( payoutCalculationResult.getJournal() != null )
          {
            payoutPromotion = payoutCalculationResult.getJournal().getPromotion();
          }
          else
          {
            continue;
          }
        }
        if ( payoutPromotion.getId().equals( promotion.getId() ) )
        {
          Journal journal = payoutCalculationResult.getJournal();
          if ( journal != null )
          {
            totalPayout += journal.getTransactionAmount().doubleValue();
          }
        }
      }

      return (int)Math.ceil( totalPayout );
    }

    /**
     * Cancels the payouts for this set of payout calculation results.
     */
    public void cancelPayouts( Promotion promotion )
    {
      PayoutCalculationAuditReasonType reasonType = PayoutCalculationAuditReasonType.lookup( PayoutCalculationAuditReasonType.BUDGET_OVERDRAWN );

      for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
      {
        PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();
        Promotion payoutPromotion = null;
        if ( promotion.getPromotionType().getCode().equals( PromotionType.PRODUCT_CLAIM ) )
        {
          if ( payoutCalculationResult.getPromotionPayoutGroup() != null )
          {
            payoutPromotion = payoutCalculationResult.getPromotionPayoutGroup().getPromotion();
          }
          else
          {
            continue;
          }
        }
        else
        {
          if ( payoutCalculationResult.getJournal() != null )
          {
            payoutPromotion = payoutCalculationResult.getJournal().getPromotion();
          }
          else
          {
            continue;
          }
        }
        if ( payoutPromotion.getId().equals( promotion.getId() ) )
        {
          Journal journal = payoutCalculationResult.getJournal();
          if ( journal != null )
          {
            // Update the payout calculation result.
            payoutCalculationResult.setJournal( null );
            payoutCalculationResult.setCalculatedPayout( new Long( 0 ) );
            payoutCalculationResult.getPayoutCalculationAudit().setReasonType( reasonType );

            // Update the payout calcuation audit.
            PayoutCalculationAudit payoutCalculationAudit = payoutCalculationResult.getPayoutCalculationAudit();
            payoutCalculationAudit.setJournal( null );

            // Delete the journal.
            journalService.deleteJournal( journal );
          }
        }
      }
    }

    /**
     * Uses the "partial payout" final payout strategy to debit the given budget.
     * 
     * @param budget the budget to be debited.
     */
    public void processPartialPayouts( Budget budget, Promotion promotion )
    {
      PayoutCalculationAuditReasonType budgetOverdrawn = PayoutCalculationAuditReasonType.lookup( PayoutCalculationAuditReasonType.BUDGET_OVERDRAWN );

      int currentValue = budget.getCurrentValueDisplay();
      for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
      {
        PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();
        Promotion payoutPromotion = null;
        if ( promotion.getPromotionType().getCode().equals( PromotionType.PRODUCT_CLAIM ) )
        {
          if ( payoutCalculationResult.getPromotionPayoutGroup() != null )
          {
            payoutPromotion = payoutCalculationResult.getPromotionPayoutGroup().getPromotion();
          }
          else
          {
            continue;
          }
        }
        else
        {
          if ( payoutCalculationResult.getJournal() != null )
          {
            payoutPromotion = payoutCalculationResult.getJournal().getPromotion();
          }
          else
          {
            continue;
          }
        }
        if ( payoutPromotion.getId().equals( promotion.getId() ) )
        {
          Journal journal = payoutCalculationResult.getJournal();
          if ( journal != null )
          {
            if ( currentValue > 0 )
            {
              long oldPayout = payoutCalculationResult.getCalculatedPayout().longValue();
              long newPayout = Math.min( oldPayout, currentValue );

              payoutCalculationResult.setCalculatedPayout( new Long( newPayout ) );
              journal.setTransactionAmount( new Long( newPayout ) );

              currentValue -= newPayout;
            }
            else
            {
              payoutCalculationResult.setJournal( null );
              payoutCalculationResult.setCalculatedPayout( new Long( 0 ) );
              payoutCalculationResult.getPayoutCalculationAudit().setReasonType( budgetOverdrawn );
            }
          }
        }
      }

      budget.setCurrentValue( BigDecimal.valueOf( currentValue ) );
    }

    /**
     * Bind the given budget to the journals associated with this set of payout calculation results.
     * 
     * @param budget the budget to associate with the journals for this set of payout calculation
     *          results.
     */
    public void setBudget( Budget budget, Promotion promotion )
    {
      for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
      {
        PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();
        Promotion payoutPromotion = null;
        if ( promotion.getPromotionType().getCode().equals( PromotionType.PRODUCT_CLAIM ) )
        {
          if ( payoutCalculationResult.getPromotionPayoutGroup() != null )
          {
            payoutPromotion = payoutCalculationResult.getPromotionPayoutGroup().getPromotion();
          }
          else
          {
            continue;
          }
        }
        else
        {
          if ( payoutCalculationResult.getJournal() != null )
          {
            payoutPromotion = payoutCalculationResult.getJournal().getPromotion();
          }
          else
          {
            continue;
          }
        }
        if ( payoutPromotion.getId().equals( promotion.getId() ) )
        {
          Journal journal = payoutCalculationResult.getJournal();
          if ( journal != null )
          {
            journal.setBudget( budget );
          }
        }
      }
    }
  }
}
