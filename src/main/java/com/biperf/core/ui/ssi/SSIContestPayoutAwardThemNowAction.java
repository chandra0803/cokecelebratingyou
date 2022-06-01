
package com.biperf.core.ui.ssi;

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
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.ssi.view.SSIContestParticipantView;
import com.biperf.core.ui.ssi.view.SSIContestPayoutAwardThemNowView;
import com.biperf.core.ui.ssi.view.SSIContestPayoutObjectivesTotalsView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestPayoutAwardThemNowAction.
 * 
 * @author Patelp
 * @since Feb 05, 2015
 * @version 1.0
 */

public class SSIContestPayoutAwardThemNowAction extends SSIContestCreateBaseAction
{

  /**
   * Method to load the Award them Now contest when the page is loaded
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward populateContestPayoutAwardThemNow( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContest contest = getSSIContestService().getContestById( getContestIdFromClientStateMap( request ) );
    List<Currency> currencies = getCurrencyService().getAllActiveCurrency();
    SSIContestPayoutAwardThemNowView contestPayoutAwardThemNowView = new SSIContestPayoutAwardThemNowView( contest,
                                                                                                           RequestUtils.getRequiredParamString( request, "ssiContestClientState" ),
                                                                                                           currencies,
                                                                                                           getSysUrl(),
                                                                                                           getBillCodeViewByContest( contest ) );
    writeAsJsonToResponse( contestPayoutAwardThemNowView, response );
    return null;
  }

  /**
   * To save the current data on the step, with all the validations for the required and fields filled-in; 
   * No data to return except error messages. 
   * This method will be used for the following events - 'Tab header' click, 'Next' button click, 'Back' button click
   * @param mapping
   * @param form
   * @param request
   * @param response 
   * @return - null
   * @throws Exception
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    SSIContestPayoutAwardThemNowForm ssiContestPayoutAwardThemNowForm = (SSIContestPayoutAwardThemNowForm)form;
    SSIContestAwardThemNow contestAtn = ssiContestPayoutAwardThemNowForm.toAwardThemNowDomain( getContestIdFromClientStateMap( request ), getAwardIssuanceNumberFromClientState( request ) );
    getSSIContestAwardThemNowService().savePayoutAwardThemNow( contestAtn, ssiContestPayoutAwardThemNowForm.getParticipantsAsList() );

    // Temporary changes to bypass the UI validation
    load( mapping, form, request, response );
    return null;
  }

  /**
   * This saves the contest data and the data on the current page of the Contest Participant table, apply necessary 
   * validations for the data filled-in. Method will return the forward URL to invoker and error messages (if any).
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return - NULL
   * @throws Exception
   */
  public ActionForward saveAsDraft( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    save( mapping, form, request, response );
    return null;
  }

  /**
   * Load the first page of the Contest Participants table in the default sort order; with NO save
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward load( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long contestId = getContestIdFromClientStateMap( request );
    short awardIssuanceNumber = getAwardIssuanceNumberFromClientState( request );
    List<SSIContestParticipant> participants = getSSIContestAwardThemNowService()
        .getContestParticipants( contestId, awardIssuanceNumber, SSIContestUtil.FIRST_PAGE_NUMBER, SSIContestUtil.SORT_BY_LAST_NAME, SSIContestUtil.DEFAULT_SORT_BY, null );
    Integer participantsCount = getSSIContestAwardThemNowService().getContestParticipantsCount( contestId, awardIssuanceNumber );
    SSIContestParticipantView responseView = new SSIContestParticipantView( participants,
                                                                            SSIContestUtil.PAX_RECORDS_PER_PAGE,
                                                                            participantsCount.intValue(),
                                                                            SSIContestUtil.DEFAULT_SORT_BY,
                                                                            SSIContestUtil.SORT_BY_LAST_NAME,
                                                                            1 );
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * To save the current page of Contest Participants data, apply necessary validations for the data filled-in and lookup the data for the requested page. 
   * This method will be used for the following events - Sort, Page Navigation on the Participant table. 
   * @param mapping
   * @param form
   * @param request
   * @param response 
   * @return - Next set of participant records to display - JSON
   * @throws Exception
   */
  public ActionForward paxNav( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
    SSIContestPayoutAwardThemNowForm ssiContestPayoutAwardThemNowForm = (SSIContestPayoutAwardThemNowForm)form;
    Long contestId = getContestIdFromClientStateMap( request );
    short awardIssuanceNumber = getAwardIssuanceNumberFromClientState( request );
    int pageNumber = getPageNumber( request );
    String sortedOn = getSortedOn( request );
    String sortedBy = getSortedBy( request );
    List<SSIContestParticipantValueBean> formParticipants = ssiContestPayoutAwardThemNowForm.getParticipantsAsList();
    SSIContest ssiContest = ssiContestPayoutAwardThemNowForm.toDomain( contestId );
    List<SSIContestParticipant> participants = null;

    try
    {
      participants = getSSIContestAwardThemNowService().saveParticipantsAndFetchNextPageResults( ssiContest, awardIssuanceNumber, formParticipants, sortedBy, sortedOn, pageNumber );
    }
    catch( ServiceErrorException se )
    {
      addServiceException( messages, se );
    }
    Integer participantsCount = getSSIContestAwardThemNowService().getContestParticipantsCount( contestId, awardIssuanceNumber );
    SSIContestParticipantView responseView = new SSIContestParticipantView( participants, SSIContestUtil.PAX_RECORDS_PER_PAGE, participantsCount, sortedBy, sortedOn, pageNumber );
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * Implement the same for all feature. This will update the database column for all the participants in the contest with the same value based on the input key.
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward sameForAll( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPayoutAwardThemNowForm ssiContestPayoutAwardThemNowForm = (SSIContestPayoutAwardThemNowForm)form;
    Long contestId = getContestIdFromClientStateMap( request );
    short awardIssuanceNumber = getAwardIssuanceNumberFromClientState( request );
    getSSIContestAwardThemNowService().updateSameValueForAllPax( contestId, awardIssuanceNumber, ssiContestPayoutAwardThemNowForm.getKey(), ssiContestPayoutAwardThemNowForm.getParticipants( 0 ) );

    SSIContestPayoutAwardThemNowView responseView = new SSIContestPayoutAwardThemNowView();
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * Save the Contest data and the data on the current page of Contest Participants table, with all the validations for the required and fields filled-in; 
   * calculate the totals for all the participants in the contest and return the Total row back to the front end. 
   * This method will be used when the Contest Creator clicks the 'Calculate Totals' button.
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward calcTotal( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPayoutAwardThemNowForm ssiContestPayoutAwardThemNowForm = (SSIContestPayoutAwardThemNowForm)form;
    List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
    Long contestId = getContestIdFromClientStateMap( request );
    Short awardIssuanceNumber = getAwardIssuanceNumberFromClientState( request );
    SSIContestPayoutObjectivesTotalsValueBean payoutObjectivesTotalsValueBean = new SSIContestPayoutObjectivesTotalsValueBean();
    getSSIContestAwardThemNowService().updateContestParticipant( ssiContestPayoutAwardThemNowForm.getParticipantsAsList(), awardIssuanceNumber );
    try
    {
      // Call the stored procedure to validate the participant data and fetch the totals
      payoutObjectivesTotalsValueBean = getSSIContestAwardThemNowService().calculatePayoutObjectivesTotals( contestId, awardIssuanceNumber );
    }
    catch( ServiceErrorException se )
    {
      addServiceException( messages, se );
    }
    SSIContestPayoutObjectivesTotalsView responseView = new SSIContestPayoutObjectivesTotalsView( payoutObjectivesTotalsValueBean );
    responseView.setMessages( messages );
    super.writeAsJsonToResponse( responseView, response );
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

}
