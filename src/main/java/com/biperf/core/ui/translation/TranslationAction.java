
package com.biperf.core.ui.translation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.nomination.TranslateCommentViewBean;

public class TranslationAction extends BaseDispatchAction
{
  public ActionForward translate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long claimId = RequestUtils.getRequiredParamLong( request, "claimId" );
    TranslateCommentViewBean translateCommentViewBean = getClaimService().getTranslatedComments( claimId, UserManager.getUserId() );

    writeAsJsonToResponse( translateCommentViewBean, response );
    return null;
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }
}
