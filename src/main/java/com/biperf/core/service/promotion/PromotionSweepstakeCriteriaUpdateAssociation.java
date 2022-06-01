/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromotionSweepstakeCriteriaUpdateAssociation.java,v $
 *
 */

package com.biperf.core.service.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.SweepstakesBillCode;
import com.biperf.core.service.UpdateAssociationRequest;

/**
 * PromotionSweepstakeCriteriaUpdateAssociation <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>jenniget</td>
 * <td>Nov 9, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author jenniget
 *
 *          jenniget Exp $
 */
public class PromotionSweepstakeCriteriaUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructor
   * 
   * @param promotion
   */
  public PromotionSweepstakeCriteriaUpdateAssociation( Promotion promotion )
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

    updatePromotion( attachedPromo );

  }

  private void updatePromotion( Promotion attachedPromo )
  {
    Promotion detachedPromo = (Promotion)getDetachedDomain();

    attachedPromo.setSweepstakesActive( detachedPromo.isSweepstakesActive() );
    attachedPromo.setSweepstakesClaimEligibilityType( detachedPromo.getSweepstakesClaimEligibilityType() );
    attachedPromo.setSweepstakesWinnerEligibilityType( detachedPromo.getSweepstakesWinnerEligibilityType() );
    attachedPromo.setSweepstakesMultipleAwardType( detachedPromo.getSweepstakesMultipleAwardType() );

    attachedPromo.setSweepstakesPrimaryAwardLevel( detachedPromo.getSweepstakesPrimaryAwardLevel() );
    attachedPromo.setSweepstakesPrimaryAwardAmount( detachedPromo.getSweepstakesPrimaryAwardAmount() );
    attachedPromo.setSweepstakesPrimaryBasisType( detachedPromo.getSweepstakesPrimaryBasisType() );
    attachedPromo.setSweepstakesPrimaryWinners( detachedPromo.getSweepstakesPrimaryWinners() );
    attachedPromo.setSweepstakesSecondaryAwardAmount( detachedPromo.getSweepstakesSecondaryAwardAmount() );
    attachedPromo.setSweepstakesSecondaryBasisType( detachedPromo.getSweepstakesSecondaryBasisType() );
    attachedPromo.setSweepstakesSecondaryWinners( detachedPromo.getSweepstakesSecondaryWinners() );

    attachedPromo.setSwpBillCodesActive( detachedPromo.isSwpBillCodesActive() );

    if ( detachedPromo.isSwpBillCodesActive() && detachedPromo.getSweepstakesBillCodes() != null && detachedPromo.getSweepstakesBillCodes().size() > 0 )
    {
      List<SweepstakesBillCode> detachedBillCodes = detachedPromo.getSweepstakesBillCodes();
      List<SweepstakesBillCode> promotionBillCodesToAdd = new ArrayList<SweepstakesBillCode>();
      for ( Iterator iterator = detachedBillCodes.iterator(); iterator.hasNext(); )
      {
        SweepstakesBillCode promoBillCode = (SweepstakesBillCode)iterator.next();
        promotionBillCodesToAdd.add( promoBillCode );
      }

      List<SweepstakesBillCode> attachBillCodes = attachedPromo.getSweepstakesBillCodes();
      Iterator attachedPromoBillCode = attachBillCodes.iterator();
      while ( attachedPromoBillCode.hasNext() )
      {
        SweepstakesBillCode promoBillCode = (SweepstakesBillCode)attachedPromoBillCode.next();
        if ( promoBillCode != null && !promotionBillCodesToAdd.contains( promoBillCode ) )
        {
          attachedPromoBillCode.remove();
        }
      }

      Iterator detachedPromoBillCodes = detachedPromo.getSweepstakesBillCodes().iterator();
      while ( detachedPromoBillCodes.hasNext() )
      {
        SweepstakesBillCode detachedPromoBillCode = (SweepstakesBillCode)detachedPromoBillCodes.next();

        if ( attachedPromo.getSweepstakesBillCodes().contains( detachedPromoBillCode ) )
        {
          Iterator attachedPromotionBillCodes = attachedPromo.getSweepstakesBillCodes().iterator();
          while ( attachedPromotionBillCodes.hasNext() )
          {
            SweepstakesBillCode attachedPromotionBillCode = (SweepstakesBillCode)attachedPromotionBillCodes.next();
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
          attachedPromo.addSweepstakesBillCodes( detachedPromoBillCode );
        }
      }
    }

  }
}
