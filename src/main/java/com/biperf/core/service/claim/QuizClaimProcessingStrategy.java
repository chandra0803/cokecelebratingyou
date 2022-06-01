/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/QuizClaimProcessingStrategy.java,v $
 */

package com.biperf.core.service.claim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.activity.QuizActivity;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.promotion.engine.PayoutCalculationFacts;
import com.biperf.core.service.promotion.engine.PayoutCalculationResult;
import com.biperf.core.service.promotion.engine.QuizFacts;
import com.biperf.core.service.util.BudgetCalculator;
import com.biperf.core.utils.GuidUtils;

/**
 * QuizClaimProcessingStrategy.
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
public class QuizClaimProcessingStrategy extends AbstractClaimProcessingStrategy
{
  private static final Log log = LogFactory.getLog( RecognitionClaimProcessingStrategy.class );

  private AwardBanQServiceFactory awardBanQServiceFactory;
  private BudgetCalculator budgetCalculator;
  private MailingService mailingService;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#getPromotionPayoutType(com.biperf.core.domain.promotion.Promotion)
   * @param promotion
   * @return String promotion payout type
   */
  public String getPromotionPayoutType( Promotion promotion )
  {
    return PromotionType.QUIZ;
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
    return new QuizFacts( (QuizClaim)approvable, participant );
  }

  /**
   * Withdraw from the promotion's budget the amount paid out for given approvable.
   * 
   * @param payoutCalculationResults
   * @param approvable
   */
  protected void calculateBudget( Set payoutCalculationResults, Approvable approvable )
  {
    budgetCalculator.calculateBudget( payoutCalculationResults, approvable );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.claim.AbstractClaimProcessingStrategy#createActivitiesForApprovable(Approvable)
   * @param claim
   * @return List
   */
  protected List createActivitiesForApprovable( Approvable claim )
  {
    List activities = new ArrayList();

    QuizClaim quizClaim = (QuizClaim)claim;
    Promotion promotion = claim.getPromotion();

    // Add submitter activity
    for ( Iterator iter = getValidParticipantsForApprovable( claim, promotion ).iterator(); iter.hasNext(); )
    {
      Participant validParticipant = (Participant)iter.next();

      QuizActivity activity = new QuizActivity( GuidUtils.generateGuid() );

      activity.setClaim( quizClaim );
      activity.setPromotion( promotion );
      activity.setParticipant( validParticipant );
      activity.setNode( quizClaim.getNode() );
      activity.setSubmissionDate( quizClaim.getSubmissionDate() );

      activities.add( activity );
    }

    return activities;
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
    List validParticipants = new ArrayList();

    // Submitter is only valid partcipant for quiz claims
    validParticipants.add( ( (Claim)approvable ).getSubmitter() );

    return validParticipants;
  }

  /**
   * This postProcess implementation for Quiz will check to see if any awards were given for
   * this claim. If awards are not active on this promotion, need to send quiz pass email here,
   * otherwise JournalServiceImpl sends the email AFTER the award has been deposited.
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
      QuizPromotion promotion = (QuizPromotion)approvable.getPromotion();
      QuizClaim quizClaim = (QuizClaim)approvable;
      if ( !promotion.isAwardActive() )
      {
        buildMailingforPassedQuizClaim( quizClaim, false );
      }
      else
      {
        for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
        {
          PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();

          boolean isCalculationSuccessful = payoutCalculationResult.isCalculationSuccessful();
          if ( !isCalculationSuccessful )
          {
            buildMailingforPassedQuizClaim( quizClaim, isCalculationSuccessful );
          }
        }
      }
    }
    catch( Exception ex )
    {
      log.error( "Error in postProcess ", ex );
      throw new ServiceErrorException( ex.getMessage(), ex );
    }
  }

  public void buildMailingforPassedQuizClaim( QuizClaim quizClaim, boolean isCalculationSuccessful )
  {
    // Bug# 15262 - do not sent pass email to users if they fail the quiz
    if ( quizClaim.getPass().booleanValue() )
    {
      Mailing mailing = mailingService.buildQuizMailing( quizClaim, isCalculationSuccessful, null );
      mailingService.submitMailing( mailing, null );
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

  public void setBudgetCalculator( BudgetCalculator budgetCalculator )
  {
    this.budgetCalculator = budgetCalculator;
  }

  public void setJournalService( JournalService journalService )
  {
    this.journalService = journalService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }
}
