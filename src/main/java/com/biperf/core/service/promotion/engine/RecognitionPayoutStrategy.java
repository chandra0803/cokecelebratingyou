/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/RecognitionPayoutStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.activity.RecognitionActivity;
import com.biperf.core.domain.audit.ClaimBasedPayoutCalculationAudit;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.SAO;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.utils.BeanLocator;

/**
 * RecognitionPayoutStrategy.
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
 * <td>Oct 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RecognitionPayoutStrategy extends AbstractPayoutStrategy
{
 private ParticipantService participantService; //coke customization
 
  protected Set processActivitiesInternal( Set activities, Promotion promotion, PayoutCalculationFacts payoutCalculationFacts )
  {
    RecognitionFacts recognitionFacts = (RecognitionFacts)payoutCalculationFacts;

    Set payoutCalculationResults = new LinkedHashSet();

    Iterator activitiesIterator = activities.iterator();
    while ( activitiesIterator.hasNext() )
    {
      RecognitionActivity activity = (RecognitionActivity)activitiesIterator.next();

      PayoutCalculationResult payoutCalculationResult = new PayoutCalculationResult();

      // Only submitters get a payout, and only when awards are active.
      if ( !activity.isSubmitter() && ( (AbstractRecognitionPromotion)promotion ).isAwardActive() || ( (RecognitionPromotion)promotion ).isAllowPublicRecognitionPoints() )
      {
        if ( recognitionFacts.getParticipant().getOptOutAwards() )
        {
          payoutCalculationResult.setCalculatedPayout( 0L );
        }
        else
        {
          payoutCalculationResult.setCalculatedPayout( activity.getAwardQuantity() );
        }
        // -------------------------------
        // coke customization start
        // -------------------------------
        if ( promotion.getAdihCashOption() || !getParticipantService().isOptedOut( activity.getParticipant().getId() ) )
        {
        payoutCalculationResult.addContributingActivity( activity );

        ClaimBasedPayoutCalculationAudit audit = new ClaimBasedPayoutCalculationAudit();
        audit.setClaim( recognitionFacts.getClaim() );
        audit.setParticipant( recognitionFacts.getParticipant() );
        audit.setReason( PayoutCalculationAuditReasonType.RECOGNITION_SUCCESS, new String[] { String.valueOf( activity.getAwardQuantity() ) } );
        payoutCalculationResult.setPayoutCalculationAudit( audit );
        if ( recognitionFacts.getClaim().isFileLoadDeposit() ) // bug 73458
        {
          payoutCalculationResult.setFileLoadDeposit( Boolean.TRUE );
        }
        payoutCalculationResults.add( payoutCalculationResult );
        } // coke customization
        
      }

    }

    return payoutCalculationResults;
  }
  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }
  

  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }


  public ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }
}
