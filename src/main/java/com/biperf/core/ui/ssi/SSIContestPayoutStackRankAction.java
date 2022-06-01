
package com.biperf.core.ui.ssi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.ssi.view.SSIContestGeneralInfoResponseView;
import com.biperf.core.ui.ssi.view.SSIContestPayoutStackRankView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.WebResponseConstants;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * SSIContestPayoutStackRankAction.
 * 
 * @author dudam
 * @since Feb 16, 2015
 * @version 1.0
 */
public class SSIContestPayoutStackRankAction extends SSIContestCreateBaseAction
{

  /**
   * Method to load the Stack Rank contest when the page is loaded
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward populateContestPayoutsStackRank( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String clientState = RequestUtils.getRequiredParamString( request, "ssiContestClientState" );
    Long contestId = getContestIdFromClientStateMap( request );
    List<Currency> currencies = getCurrencyService().getAllActiveCurrency();

    SSIContest contest = getSSIContestService().getContestByIdWithAssociations( contestId, getAssociationRequestCollection() );
    Badge ssiBadge = contest.getPromotion().getBadge();
    if ( null != ssiBadge )
    {
      ssiBadge.setBadgeRules( getSortedBadgeRulesById( ssiBadge.getId() ) );
    }
    Integer participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contestId );

    SSIContestPayoutStackRankView contestPayoutStackRankView = new SSIContestPayoutStackRankView( contest, clientState, currencies, getSysUrl(), getBillCodeViewByContest( contest ) );
    contestPayoutStackRankView.getContestJson().setParticipantCount( participantsCount );

    writeAsJsonToResponse( contestPayoutStackRankView, response );
    return null;
  }

  /**
   * Save SSIContest with SSIContestLevels information
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException 
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    SSIContestPayoutStackRankForm stackRankForm = (SSIContestPayoutStackRankForm)form;
    Long contestId = getContestIdFromClientStateMap( request );
    SSIContest contest = stackRankForm.toDomain( contestId );
    try
    {
      getSSIContestService().savePayoutStackRank( contest, stackRankForm.getRanksAsList(), new ArrayList<UpdateAssociationRequest>() );
    }
    catch( ServiceErrorException se )
    {
      List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
      addServiceException( messages, se );
    }
    // Temporary change to bypass the UI validation
    String clientState = RequestUtils.getRequiredParamString( request, "ssiContestClientState" );
    SSIContestGeneralInfoResponseView responseView = new SSIContestGeneralInfoResponseView( contestId, clientState );
    writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
  * Save a draft of SSIContest with SSIContestLevels information
  * @param mapping
  * @param form
  * @param request
  * @param response
  * @return
  * @throws IOException 
  */
  public ActionForward saveAsDraft( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    this.save( mapping, form, request, response );
    return null;
  }

  /*
   * Get the Association Request Collection
   */
  private AssociationRequestCollection getAssociationRequestCollection()
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_STACK_RANK_PAYOUTS ) );
    return associationRequestCollection;
  }

  private void addServiceException( List<WebErrorMessage> messages, ServiceErrorException se )
  {
    List serviceErrors = se.getServiceErrors();
    for ( Iterator iter = serviceErrors.iterator(); iter.hasNext(); )
    {
      ServiceError error = (ServiceError)iter.next();
      WebErrorMessage message = new WebErrorMessage();
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setSuccess( false );
      message.setText( CmsResourceBundle.getCmsBundle().getString( error.getKey() ) );
      messages.add( message );
    }
  }

}
