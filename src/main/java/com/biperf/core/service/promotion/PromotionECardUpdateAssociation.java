/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromotionECardUpdateAssociation.java,v $
 */

package com.biperf.core.service.promotion;

import java.util.Iterator;

import com.biperf.core.dao.multimedia.MultimediaDAO;
import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionECard;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.utils.BeanLocator;

/**
 * PromotionECardUpdateAssociation.
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
 * <td>sedey</td>
 * <td>Oct 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionECardUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructor
   * 
   * @param promotion
   */
  public PromotionECardUpdateAssociation( Promotion promotion )
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

    updateRecognitionPromotion( (AbstractRecognitionPromotion)attachedPromo );
  }

  /**
   * Get the MultimediaDAO
   * 
   * @return MultimediaDAO
   */
  private MultimediaDAO getMultimediaDAO()
  {
    return (MultimediaDAO)BeanLocator.getBean( MultimediaDAO.BEAN_NAME );
  }

  private void updateRecognitionPromotion( AbstractRecognitionPromotion attachedPromo )
  {
    AbstractRecognitionPromotion detachedPromo = (AbstractRecognitionPromotion)getDetachedDomain();

    attachedPromo.setCardActive( detachedPromo.isCardActive() );
    attachedPromo.setCardClientEmailAddress( detachedPromo.getCardClientEmailAddress() );
    attachedPromo.setCardClientSetupDone( detachedPromo.isCardClientSetupDone() );

    attachedPromo.setAllowYourOwnCard( detachedPromo.isAllowYourOwnCard() );
    attachedPromo.setDrawYourOwnCard( detachedPromo.isDrawYourOwnCard() );
    
    //client customization start
    attachedPromo.setAllowMeme( detachedPromo.isAllowMeme() );
    attachedPromo.setAllowSticker( detachedPromo.isAllowSticker() );
    attachedPromo.setAllowUploadOwnMeme( detachedPromo.isAllowUploadOwnMeme() );
    //client customization end

    // Remove any eCards from the attached promotion object that are not present in the
    // detached promotion object
    if ( attachedPromo.getPromotionECard() != null && attachedPromo.getPromotionECard().size() > 0 )
    {
      Iterator attachedECardIt = attachedPromo.getPromotionECard().iterator();
      while ( attachedECardIt.hasNext() )
      {
        PromotionECard attachedPromoECard = (PromotionECard)attachedECardIt.next();
        if ( !detachedPromo.getPromotionECard().contains( attachedPromoECard ) )
        {
          attachedECardIt.remove();
        }
      }
    }

    // Add all eCards from the detached Promotion Object to the attached Promotion Object
    if ( detachedPromo.getPromotionECard() != null && detachedPromo.getPromotionECard().size() > 0 )
    {
      Iterator detachedECardIt = detachedPromo.getPromotionECard().iterator();
      while ( detachedECardIt.hasNext() )
      {
        PromotionECard detachedPromoECard = (PromotionECard)detachedECardIt.next();
        ECard eCard = (ECard)getMultimediaDAO().getCardById( detachedPromoECard.geteCard().getId() );
        detachedPromoECard.seteCard( eCard );
        attachedPromo.addECard( detachedPromoECard );
      }
    }
    else
    {
      attachedPromo.getPromotionECard().clear();
    }

  }
}
