/**
 * 
 */

package com.biperf.core.ui.productclaim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.ProductClaimTileView;
import com.biperf.core.domain.claim.ProductClaimTileView.ClaimApprovals;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ProductClaimPromotionsValueBean;
import com.biperf.core.value.PromotionMenuBean;

/**
 * @author poddutur
 *
 */
public class ProductClaimModuleAction extends BaseDispatchAction
{
  public ActionForward fetchProductClaimByIdForTile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<ProductClaimPromotionsValueBean> pcPromotionsTileList = new ArrayList<ProductClaimPromotionsValueBean>();
    List<ClaimApprovals> claimApprovalsList = new ArrayList<ClaimApprovals>();
    ProductClaimTileView productClaimTileView = new ProductClaimTileView();
    Long userId = UserManager.getUser().getUserId();

    Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );

    pcPromotionsTileList = getClaimService().getEligibleProductClaimPromotions( userId );

    for ( ProductClaimPromotionsValueBean productClaimPromotionsValueBean : pcPromotionsTileList )
    {
      boolean isPCPromoInCahce = false;
      for ( Iterator iter = eligiblePromotions.iterator(); iter.hasNext(); )
      {
        PromotionMenuBean promoMenuBean = (PromotionMenuBean)iter.next();
        if ( promoMenuBean.getPromotion().isProductClaimPromotion() && promoMenuBean.isCanSubmit() )
        {
          if ( promoMenuBean.getPromotion().getId().equals( productClaimPromotionsValueBean.getPromotionId() ) )
          {
            isPCPromoInCahce = true;
            break;
          }
        }
      }
      if ( isPCPromoInCahce )
      {
        ClaimApprovals claimApprovals = new ClaimApprovals();
        claimApprovals.setPromotionId( productClaimPromotionsValueBean.getPromotionId() );
        claimApprovals.setPromotionName( productClaimPromotionsValueBean.getPromotionName() );
        claimApprovals.setClaimId( null );
        claimApprovals.setNumberSubmitted( productClaimPromotionsValueBean.getNumberSubmitted() );
        claimApprovals.setNumberOfApprovals( productClaimPromotionsValueBean.getNumberOfApprovals() );
        claimApprovals.setPromotionEndDate( DateUtils.toDisplayString( productClaimPromotionsValueBean.getPromotionEndDate() ) );
        claimApprovals.setPromotionStartDate( DateUtils.toDisplayString( productClaimPromotionsValueBean.getPromotionStartDate() ) );
        claimApprovalsList.add( claimApprovals );
      }
    }

    productClaimTileView.setClaimApprovals( claimApprovalsList );

    super.writeAsJsonToResponse( productClaimTileView, response );
    return null;
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  protected ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

}
