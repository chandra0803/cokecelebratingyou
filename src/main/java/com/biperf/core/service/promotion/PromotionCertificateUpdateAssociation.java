/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromotionCertificateUpdateAssociation.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.Iterator;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionCert;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.UpdateAssociationRequest;

/**
 * PromotionCertificateUpdateAssociation.
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
 * <td>meadows</td>
 * <td>Sep 07, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionCertificateUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructor
   * 
   * @param promotion
   */
  public PromotionCertificateUpdateAssociation( Promotion promotion )
  {
    super( promotion );
  }

  /**
   * execute the association request and update the specified promotion object into the associated
   * promotion object
   * 
   * @param attachedDomain
   */
  public void execute( BaseDomain attachedDomain )
  {
    Promotion attachedPromo = (Promotion)attachedDomain;

    if ( attachedPromo.isDIYQuizPromotion() )
    {
      updateDIYQuizPromotion( (QuizPromotion)attachedPromo );
    }
    else if ( attachedPromo.isNominationPromotion() )
    {
      updateNominationPromotion( (NominationPromotion)attachedPromo );
    }
    else
    {
      updateRecognitionPromotion( (RecognitionPromotion)attachedPromo );
    }
  }

  private void updateDIYQuizPromotion( QuizPromotion attachedPromo )
  {
    QuizPromotion detachedPromo = (QuizPromotion)getDetachedDomain();
    attachedPromo.setSubmissionStartDate( detachedPromo.getSubmissionStartDate() );
    attachedPromo.setSubmissionEndDate( detachedPromo.getSubmissionEndDate() );

    // Remove any certificates from the attached promotion that are not present in the detached
    if ( attachedPromo.getPromotionCertificates() != null && !attachedPromo.getPromotionCertificates().isEmpty() )
    {
      Iterator attachedCertificateIter = attachedPromo.getPromotionCertificates().iterator();
      while ( attachedCertificateIter.hasNext() )
      {
        PromotionCert attachedPromoCert = (PromotionCert)attachedCertificateIter.next();
        Iterator detachedCertificateIter = detachedPromo.getPromotionCertificates().iterator();
        boolean found = false;
        while ( detachedCertificateIter.hasNext() )
        {
          PromotionCert detachedPromoCert = (PromotionCert)detachedCertificateIter.next();
          if ( detachedPromoCert.getId() != null && attachedPromoCert.getId().equals( detachedPromoCert.getId() ) )
          {
            found = true;
            break;
          }
        }
        if ( !found )
        {
          attachedCertificateIter.remove();
        }
      }
    }

    // Add all the newly added certificates from the detached Promotion Object to the attached
    if ( detachedPromo.getPromotionCertificates() != null && detachedPromo.getPromotionCertificates().size() > 0 )
    {
      Iterator detachedCertificateIter = detachedPromo.getPromotionCertificates().iterator();
      while ( detachedCertificateIter.hasNext() )
      {
        PromotionCert detachedPromoCert = (PromotionCert)detachedCertificateIter.next();
        Iterator attachedCertificateIter = attachedPromo.getPromotionCertificates().iterator();
        boolean found = false;
        while ( attachedCertificateIter.hasNext() )
        {
          PromotionCert attachedPromoCert = (PromotionCert)attachedCertificateIter.next();
          if ( detachedPromoCert.getId() != null && detachedPromoCert.getId().equals( attachedPromoCert.getId() ) )
          {
            found = true;
            break;
          }
        }
        if ( !found )
        {
          attachedPromo.addCertificate( detachedPromoCert );
        }
      }
    }
    else
    {
      attachedPromo.getPromotionCertificates().clear();
    }
  }

  private void updateNominationPromotion( NominationPromotion attachedPromo )
  {
    NominationPromotion detachedPromo = (NominationPromotion)getDetachedDomain();

    attachedPromo.setCertificateActive( detachedPromo.isCertificateActive() );
    attachedPromo.setPaxDisplayOrder( detachedPromo.getPaxDisplayOrder() );

    // If cumulative, then one cert per promotion must be yes
    if ( attachedPromo.isCumulative() )
    {
      attachedPromo.setOneCertPerPromotion( true );
    }
    else
    {
      attachedPromo.setOneCertPerPromotion( detachedPromo.isOneCertPerPromotion() );
    }

    // Remove any certificates from the attached promotion object that are not present in the
    // detached promotion object
    if ( attachedPromo.getPromotionCertificates() != null && !attachedPromo.getPromotionCertificates().isEmpty() )
    {
      Iterator<PromotionCert> attachedCertificateIter = attachedPromo.getPromotionCertificates().iterator();
      while ( attachedCertificateIter.hasNext() )
      {
        PromotionCert attachedPromoCert = attachedCertificateIter.next();
        if ( !detachedPromo.getPromotionCertificates().contains( attachedPromoCert ) )
        {
          attachedCertificateIter.remove();
        }
      }
    }

    // Add all certificates from the detached Promotion Object to the attached Promotion Object
    if ( detachedPromo.getPromotionCertificates() != null && detachedPromo.getPromotionCertificates().size() > 0 )
    {
      Iterator<PromotionCert> detachedCertificateIter = detachedPromo.getPromotionCertificates().iterator();
      while ( detachedCertificateIter.hasNext() )
      {
        PromotionCert detachedPromoCert = detachedCertificateIter.next();
        // attachedPromo.addCertificate( detachedPromoCert );
        attachedPromo.addPromotionCertificate( detachedPromoCert.getCertificateId(), detachedPromoCert.getOrderNumber() );
      }
    }
    else
    {
      attachedPromo.getPromotionCertificates().clear();
    }
  }

  private void updateRecognitionPromotion( RecognitionPromotion attachedPromo )
  {
    RecognitionPromotion detachedPromo = (RecognitionPromotion)getDetachedDomain();

    attachedPromo.setPaxDisplayOrder( detachedPromo.getPaxDisplayOrder() );

    attachedPromo.setCardActive( detachedPromo.isCardActive() );
    attachedPromo.setCardClientEmailAddress( detachedPromo.getCardClientEmailAddress() );
    attachedPromo.setCardClientSetupDone( detachedPromo.isCardClientSetupDone() );

    attachedPromo.setAllowYourOwnCard( detachedPromo.isAllowYourOwnCard() );
    attachedPromo.setDrawYourOwnCard( detachedPromo.isDrawYourOwnCard() );

    // Remove any certificates from the attached promotion object that are not present in the
    // detached promotion object
    if ( attachedPromo.getPromotionCertificates() != null && !attachedPromo.getPromotionCertificates().isEmpty() )
    {
      Iterator attachedCertificateIter = attachedPromo.getPromotionCertificates().iterator();
      while ( attachedCertificateIter.hasNext() )
      {
        PromotionCert attachedPromoCert = (PromotionCert)attachedCertificateIter.next();
        if ( !detachedPromo.getPromotionCertificates().contains( attachedPromoCert ) )
        {
          attachedCertificateIter.remove();
        }
      }
    }

    // Add all certificates from the detached Promotion Object to the attached Promotion Object
    if ( detachedPromo.getPromotionCertificates() != null && detachedPromo.getPromotionCertificates().size() > 0 )
    {
      Iterator detachedCertificateIter = detachedPromo.getPromotionCertificates().iterator();
      while ( detachedCertificateIter.hasNext() )
      {
        PromotionCert detachedPromoCert = (PromotionCert)detachedCertificateIter.next();
        attachedPromo.addCertificate( detachedPromoCert );
      }
    }
    else
    {
      attachedPromo.getPromotionCertificates().clear();
    }

  }
}
