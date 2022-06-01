
package com.biperf.core.service.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionWizard;
import com.biperf.core.service.UpdateAssociationRequest;

public class PromotionWizardUpdateAssociation extends UpdateAssociationRequest
{

  public PromotionWizardUpdateAssociation( Promotion detachedNomPromotion )
  {
    super( detachedNomPromotion );
  }

  @Override
  public void execute( BaseDomain attachedDomain )
  {
    Promotion detachedNomPromo = (Promotion)getDetachedDomain();
    Promotion attachedNomPromo = (Promotion)attachedDomain;

    if ( detachedNomPromo.isNominationPromotion() )
    {
      if ( detachedNomPromo.getPromotionWizardOrder() != null && detachedNomPromo.getPromotionWizardOrder().size() > 0 )
      {
        Set<PromotionWizard> detachedWizards = detachedNomPromo.getPromotionWizardOrder();
        List<PromotionWizard> PromotionWizardsToAdd = new ArrayList<PromotionWizard>();
        for ( Iterator iterator = detachedWizards.iterator(); iterator.hasNext(); )
        {
          PromotionWizard promotionWizard = (PromotionWizard)iterator.next();
          PromotionWizardsToAdd.add( promotionWizard );
        }

        Set<PromotionWizard> attachPromotionWizards = attachedNomPromo.getPromotionWizardOrder();
        Iterator attachedNominationPromotionWizard = attachPromotionWizards.iterator();
        while ( attachedNominationPromotionWizard.hasNext() )
        {
          PromotionWizard promotionWizard = (PromotionWizard)attachedNominationPromotionWizard.next();
          if ( promotionWizard != null && !PromotionWizardsToAdd.contains( promotionWizard ) )
          {
            attachedNominationPromotionWizard.remove();
          }
        }

        Set<PromotionWizard> detachedNominationPromotionWizards = detachedNomPromo.getPromotionWizardOrder();

        if ( detachedNominationPromotionWizards != null && detachedNominationPromotionWizards.size() > 0 )
        {
          for ( PromotionWizard detachedPromotionWizard : detachedNominationPromotionWizards )
          {
            if ( attachedNomPromo.getPromotionWizardOrder().contains( detachedPromotionWizard ) )
            {
              Iterator attachedPromotionWizards = attachedNomPromo.getPromotionWizardOrder().iterator();
              while ( attachedPromotionWizards.hasNext() )
              {
                PromotionWizard attachedPromotionWizard = (PromotionWizard)attachedPromotionWizards.next();
                if ( attachedPromotionWizard != null && detachedPromotionWizard != null && attachedPromotionWizard.equals( detachedPromotionWizard ) )
                {
                  attachedPromotionWizard.setWizardOrderName( detachedPromotionWizard.getWizardOrderName() );
                  attachedPromotionWizard.setWizardOrder( detachedPromotionWizard.getWizardOrder() );
                }
              }
            }
            else
            {
              attachedNomPromo.addPromotionWizardOrder( detachedPromotionWizard );
            }
          }
        }

        /*
         * Iterator detachedNominationPromotionWizards =
         * detachedNomPromo.getPromotionWizardOrder().iterator(); while (
         * detachedNominationPromotionWizards.hasNext() ) { PromotionWizard detachedPromotionWizard
         * = (PromotionWizard)detachedNominationPromotionWizards.next(); if (
         * attachedNomPromo.getPromotionWizardOrder().contains( detachedPromotionWizard ) ) {
         * Iterator attachedPromotionWizards =
         * attachedNomPromo.getPromotionWizardOrder().iterator(); while (
         * attachedPromotionWizards.hasNext() ) { PromotionWizard attachedPromotionWizard =
         * (PromotionWizard)attachedPromotionWizards.next(); if ( attachedPromotionWizard!= null &&
         * detachedPromotionWizard!= null && attachedPromotionWizard.equals( detachedPromotionWizard
         * ) ) { attachedPromotionWizard.setWizardOrderName(
         * detachedPromotionWizard.getWizardOrderName() ); attachedPromotionWizard.setWizardOrder(
         * detachedPromotionWizard.getWizardOrder() ); } } } else {
         * attachedNomPromo.addPromotionWizardOrder( detachedPromotionWizard ); } }
         */
      }
    }
  }
}
