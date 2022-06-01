
package com.biperf.core.service.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionBillCode;
import com.biperf.core.service.UpdateAssociationRequest;

public class PromotionBillCodeUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructor
   * 
   * @param promotion
   */
  public PromotionBillCodeUpdateAssociation( Promotion promotion )
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
    Promotion detachedPromo = (Promotion)getDetachedDomain();

    Promotion attachedPromo = (Promotion)attachedDomain;

    attachedPromo.setBillCodesActive( detachedPromo.isBillCodesActive() );

    if ( detachedPromo.isBillCodesActive() && detachedPromo.getPromotionBillCodes() != null && detachedPromo.getPromotionBillCodes().size() > 0 )
    {
      List<PromotionBillCode> detachedBillCodes = detachedPromo.getPromotionBillCodes();
      List<PromotionBillCode> promotionBillCodesToAdd = new ArrayList<PromotionBillCode>();
      for ( Iterator iterator = detachedBillCodes.iterator(); iterator.hasNext(); )
      {
        PromotionBillCode promoBillCode = (PromotionBillCode)iterator.next();
        promotionBillCodesToAdd.add( promoBillCode );
      }

      List<PromotionBillCode> attachBillCodes = attachedPromo.getPromotionBillCodes();
      Iterator attachedPromoBillCode = attachBillCodes.iterator();
      while ( attachedPromoBillCode.hasNext() )
      {
        PromotionBillCode promoBillCode = (PromotionBillCode)attachedPromoBillCode.next();
        if ( promoBillCode != null && !promotionBillCodesToAdd.contains( promoBillCode ) )
        {
          attachedPromoBillCode.remove();
        }
      }

      Iterator detachedPromoBillCodes = detachedPromo.getPromotionBillCodes().iterator();
      while ( detachedPromoBillCodes.hasNext() )
      {
        PromotionBillCode detachedPromoBillCode = (PromotionBillCode)detachedPromoBillCodes.next();

        if ( attachedPromo.getPromotionBillCodes().contains( detachedPromoBillCode ) )
        {
          Iterator attachedPromotionBillCodes = attachedPromo.getPromotionBillCodes().iterator();
          while ( attachedPromotionBillCodes.hasNext() )
          {
            PromotionBillCode attachedPromotionBillCode = (PromotionBillCode)attachedPromotionBillCodes.next();
            if ( attachedPromotionBillCode != null && detachedPromoBillCode != null && attachedPromotionBillCode.equals( detachedPromoBillCode ) )
            {
              attachedPromotionBillCode.setBillCode( detachedPromoBillCode.getBillCode() );
              attachedPromotionBillCode.setCustomValue( detachedPromoBillCode.getCustomValue() );
              attachedPromotionBillCode.setSortOrder( detachedPromoBillCode.getSortOrder() );
              attachedPromotionBillCode.setTrackBillCodeBy( detachedPromoBillCode.getTrackBillCodeBy() );
            }

          }
        }
        else
        {
          attachedPromo.addPromotionBillCodes( detachedPromoBillCode );
        }
      }
    }
  }
}
