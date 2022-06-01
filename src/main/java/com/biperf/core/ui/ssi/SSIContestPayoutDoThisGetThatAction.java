
package com.biperf.core.ui.ssi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.ssi.view.SSIContestActivityResponseView;
import com.biperf.core.ui.ssi.view.SSIContestGeneralInfoResponseView;
import com.biperf.core.ui.ssi.view.SSIContestPayoutDoThisGetThatView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestPayoutDoThisGetThatAction.
 * 
 * @author kandhi
 * @since Dec 22, 2014
 * @version 1.0
 */
public class SSIContestPayoutDoThisGetThatAction extends SSIContestCreateBaseAction
{

  /**
   * Load the existing payouts on loading tab 3
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward populateContestPayoutsDoThisGetThat( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long contestId = getContestIdFromClientStateMap( request );

    SSIContest contest = getSSIContestService().getContestById( contestId );

    addContestActivities( contest );

    Integer participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contestId );
    List<Currency> currencies = getCurrencyService().getAllActiveCurrency();
    SSIContestPayoutDoThisGetThatView contestPayoutObjectivesView = new SSIContestPayoutDoThisGetThatView( contest,
                                                                                                           RequestUtils.getRequiredParamString( request, "ssiContestClientState" ),
                                                                                                           participantsCount,
                                                                                                           currencies,
                                                                                                           getSysUrl(),
                                                                                                           getBillCodeViewByContest( contest ) );
    writeAsJsonToResponse( contestPayoutObjectivesView, response );
    return null;
  }

  /**
   * Save contest payout activity
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException 
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    SSIContestPayoutDoThisGetThatForm ssiContestPayoutDoThisGetThatForm = (SSIContestPayoutDoThisGetThatForm)form;
    List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
    Long contestId = getContestIdFromClientStateMap( request );
    SSIContest contest = ssiContestPayoutDoThisGetThatForm.toDomain( contestId );

    try
    {
      getSSIContestService().savePayoutDoThisGetThat( contest );
    }
    catch( ServiceErrorException se )
    {
      addServiceException( messages, se );
    }

    // Temporary change to bypass the UI validation
    String clientState = RequestUtils.getRequiredParamString( request, "ssiContestClientState" );
    SSIContestGeneralInfoResponseView responseView = new SSIContestGeneralInfoResponseView( contestId, clientState );
    writeAsJsonToResponse( responseView, response );
    return null;

  }

  /**
   * Save a draft of SSIContest with SSIContestActivity records
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

  /**
   * Create a new SSIContestActivity record
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public ActionForward create( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPayoutDoThisGetThatForm ssiContestPayoutDoThisGetThatForm = (SSIContestPayoutDoThisGetThatForm)form;
    Long contestId = getContestIdFromClientStateMap( request );

    SSIContestActivity ssiContestActivity = ssiContestPayoutDoThisGetThatForm.toActivityDomain();
    getSSIContestService().saveContestActivity( contestId, ssiContestActivity );
    SSIContestActivityResponseView contestActivityResponseView = new SSIContestActivityResponseView( ssiContestActivity );

    writeAsJsonToResponse( contestActivityResponseView, response );
    return null;
  }

  /**
   * Update and existing SSIContestActivity record
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPayoutDoThisGetThatForm ssiContestPayoutDoThisGetThatForm = (SSIContestPayoutDoThisGetThatForm)form;

    SSIContestActivity ssiContestActivity = ssiContestPayoutDoThisGetThatForm.toActivityDomain();
    getSSIContestService().updateContestActivity( ssiContestActivity );
    SSIContestActivityResponseView contestActivityResponseView = new SSIContestActivityResponseView( ssiContestActivity );

    writeAsJsonToResponse( contestActivityResponseView, response );
    return null;
  }

  /**
   * Delete SSIContestActivity record
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public ActionForward remove( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPayoutDoThisGetThatForm ssiContestPayoutDoThisGetThatForm = (SSIContestPayoutDoThisGetThatForm)form;
    SSIContestActivity ssiContestActivity = ssiContestPayoutDoThisGetThatForm.toActivityDomain();

    Long contestId = getContestIdFromClientStateMap( request );
    getSSIContestService().deleteContestActivity( contestId, ssiContestActivity.getId(), UserManager.getUserId() );
    SSIContestActivityResponseView contestActivityResponseView = new SSIContestActivityResponseView( ssiContestActivity );

    writeAsJsonToResponse( contestActivityResponseView, response );
    return null;
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

  private void addContestActivities( SSIContest contest )
  {
    List<SSIContestActivity> contestActivities = getSSIContestService().getContestActivitiesByContestId( contest.getId() );
    contest.setContestActivities( new HashSet( contestActivities ) );

  }

}
