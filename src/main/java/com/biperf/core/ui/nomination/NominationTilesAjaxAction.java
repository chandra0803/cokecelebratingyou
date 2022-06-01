
package com.biperf.core.ui.nomination;

import static com.biperf.core.domain.enums.nomination.NominationsInProgressConstants.NOM_INPROGRESS_COUNT;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.promotion.NominationPromotionService;
import com.biperf.core.ui.recognition.BaseRecognitionAction;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.nomination.EligibleNominationPromotionValueObject;

public class NominationTilesAjaxAction extends BaseRecognitionAction
{

  public ActionForward eligiblePromo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    EligibleNominationPromotionListViewObject jsonResponse = new EligibleNominationPromotionListViewObject();

    List<EligibleNominationPromotionValueObject> nomPromos = getNomPromoService().getEligibleNomPromotions( UserManager.getUser() );

    for ( EligibleNominationPromotionValueObject vo : nomPromos )
    {
      jsonResponse.addPromotionView( new EligibleNominationPromotionViewObject( vo.getPromoId(), vo.getName(), vo.getMaxSubmissionAllowed(), vo.getUsedSubmission(), vo.getMessage() ) );
    }

    Map<String, Object> prcResult = getNomPromoService().nominationsWinnersModule( UserManager.getUserId() );
    int pastWinners = ( (BigDecimal)prcResult.get( "p_out_all_elig_winner_flg" ) ).intValue();
    int returnCode = ( (BigDecimal)prcResult.get( "p_out_returncode" ) ).intValue();

    if ( returnCode == 99 || pastWinners == 0 )
    {
      jsonResponse.setShowPastWinnersLink( false );
    }

    if ( pastWinners == 1 )
    {
      jsonResponse.setShowPastWinnersLink( true );
    }

    writeAsJsonToResponse( jsonResponse, response, ContentType.JSON );

    return null;
  }

  public ActionForward inProgressCount( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    int inProgressCount = getNomClaimService().getInProgressNominationClaimsCount( UserManager.getUserId() );
    JSONObject json = new JSONObject();
    json.put( NOM_INPROGRESS_COUNT, inProgressCount );
    response.getWriter().write( json.toString() );
    return null;
  }

  private NominationClaimService getNomClaimService()
  {
    return (NominationClaimService)getService( NominationClaimService.BEAN_NAME );
  }

  private NominationPromotionService getNomPromoService()
  {
    return (NominationPromotionService)getService( NominationPromotionService.BEAN_NAME );
  }
}
