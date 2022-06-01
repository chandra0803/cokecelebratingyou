
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
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.ssi.view.SSIContestParticipantView;
import com.biperf.core.ui.ssi.view.SSIContestPayoutObjectivesTotalsView;
import com.biperf.core.ui.ssi.view.SSIContestPayoutObjectivesView;
import com.biperf.core.ui.ssi.view.SSIContestResponseView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestPayoutObjectivesAction.
 * 
 * @author Patelp
 * @since Dec 2, 2014
 * @version 1.0
 */

public class SSIContestPayoutObjectivesAction extends SSIContestCreateBaseAction
{

  private static final String BONUS_FOR_EVERY = "bonusForEvery";
  private static final String OBJECTIVE_BONUS_INCREMENT = "objectiveBonusIncrement";
  private static final String BONUS_PAYOUT = "bonusPayout";
  private static final String OBJECTIVE_BONUS_PAYOUT = "objectiveBonusPayout";
  private static final String BONUS_PAYOUT_CAP = "bonusPayoutCap";
  private static final String OBJECTIVE_BONUS_CAP = "objectiveBonusCap";

  /**
    * Method to load the contest when the page is loaded
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return
    * @throws Exception
    */
  public ActionForward populateContestPayoutsObjective( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContest contest = getSSIContestService().getContestById( getContestIdFromClientStateMap( request ) );
    Badge ssiBadge = contest.getPromotion().getBadge();
    ssiBadge.setBadgeRules( getSortedBadgeRulesById( ssiBadge.getId() ) );
    List<Currency> currencies = getCurrencyService().getAllActiveCurrency();
    SSIContestPayoutObjectivesView contestPayoutObjectivesView = new SSIContestPayoutObjectivesView( contest,
                                                                                                     RequestUtils.getRequiredParamString( request, "ssiContestClientState" ),
                                                                                                     currencies,
                                                                                                     getSysUrl(),
                                                                                                     getBillCodeViewByContest( contest ) );

    writeAsJsonToResponse( contestPayoutObjectivesView, response );
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
    List<SSIContestParticipant> participants = getSSIContestParticipantService()
        .getContestParticipants( contestId, SSIContestUtil.FIRST_PAGE_NUMBER, SSIContestUtil.SORT_BY_LAST_NAME, SSIContestUtil.DEFAULT_SORT_BY );

    Integer participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contestId );
    SSIContestParticipantView responseView = new SSIContestParticipantView( participants,
                                                                            SSIContestUtil.PAX_RECORDS_PER_PAGE,
                                                                            participantsCount.intValue(),
                                                                            SSIContestUtil.DEFAULT_SORT_BY,
                                                                            SSIContestUtil.SORT_BY_LAST_NAME,
                                                                            SSIContestUtil.FIRST_PAGE_NUMBER );
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * To save the current page of Contest Participants data, apply necessary validations for the data filled-in and lookup the data for the requested page; 
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
    SSIContestPayoutObjectivesForm ssiContestPayoutObjectivesForm = (SSIContestPayoutObjectivesForm)form;
    Long contestId = getContestIdFromClientStateMap( request );
    int pageNumber = getPageNumber( request );
    String sortedOn = getMappedSortedOn( request );
    String sortedBy = getSortedBy( request );
    List<SSIContestParticipantValueBean> formParticipants = ssiContestPayoutObjectivesForm.getParticipantsAsList();
    SSIContest ssiContest = ssiContestPayoutObjectivesForm.toDomain( contestId );
    Long badgeId = ssiContestPayoutObjectivesForm.getBadgeId();
    List<SSIContestParticipant> participants = null;

    try
    {
      participants = getSSIContestService().saveContestAndFetchNextPageResults( ssiContest, badgeId, formParticipants, sortedBy, sortedOn, pageNumber );
    }
    catch( ServiceErrorException se )
    {
      addServiceException( messages, se );
    }
    Integer participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contestId );
    SSIContestParticipantView responseView = new SSIContestParticipantView( participants, SSIContestUtil.PAX_RECORDS_PER_PAGE, participantsCount, sortedBy, getSortedOn( request ), pageNumber );
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  private String getMappedSortedOn( HttpServletRequest request )
  {

    String sortedOn = getSortedOn( request );
    return BONUS_FOR_EVERY.equalsIgnoreCase( sortedOn )
        ? OBJECTIVE_BONUS_INCREMENT
        : BONUS_PAYOUT.equalsIgnoreCase( sortedOn ) ? OBJECTIVE_BONUS_PAYOUT : BONUS_PAYOUT_CAP.equalsIgnoreCase( sortedOn ) ? OBJECTIVE_BONUS_CAP : sortedOn;

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
    List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
    SSIContestPayoutObjectivesForm ssiContestPayoutObjectivesForm = (SSIContestPayoutObjectivesForm)form;
    Long badgeId = ssiContestPayoutObjectivesForm.getBadgeId();

    SSIContest ssiContest = ssiContestPayoutObjectivesForm.toDomain( getContestIdFromClientStateMap( request ) );
    try
    {
      getSSIContestService().savePayoutObjectives( ssiContest, badgeId, ssiContestPayoutObjectivesForm.getParticipantsAsList() );
    }
    catch( ServiceErrorException se )
    {
      addServiceException( messages, se );
    }
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
    SSIContestPayoutObjectivesForm ssiContestPayoutObjectivesForm = (SSIContestPayoutObjectivesForm)form;
    Long contestId = getContestIdFromClientStateMap( request );

    getSSIContestParticipantService().updateSameValueForAllPax( contestId, ssiContestPayoutObjectivesForm.getKey(), ssiContestPayoutObjectivesForm.getParticipants( 0 ) );

    SSIContestResponseView responseView = new SSIContestResponseView();
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
    SSIContestPayoutObjectivesForm ssiContestPayoutObjectivesForm = (SSIContestPayoutObjectivesForm)form;
    List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
    Long contestId = getContestIdFromClientStateMap( request );
    SSIContestPayoutObjectivesTotalsValueBean payoutObjectivesTotalsValueBean = new SSIContestPayoutObjectivesTotalsValueBean();

    // Save the current page
    SSIContest ssiContest = ssiContestPayoutObjectivesForm.toDomain( contestId );
    ssiContest.getContestBillCodes().clear(); //
    try
    {
      // Call the stored procedure to validate the participant data and fetch the totals
      getSSIContestService().savePayoutObjectives( ssiContest, ssiContestPayoutObjectivesForm.getBadgeId(), ssiContestPayoutObjectivesForm.getParticipantsAsList() );
      payoutObjectivesTotalsValueBean = getSSIContestService().calculatePayoutObjectivesTotals( contestId );
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

  /**
   * This method will be used when Creator changes any of these fields on the page - Measure Type (currency, units), 
   * Payout Type (points, other), Include Stack Rack, Include Bonus. And NO data from the Contest Participants table gets posted. 
   * After the data is updated (clear all applicable fields) on the server, the URL will return the 
   * data for the first page of the Contest Participants in the default order. 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward updatePayout( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPayoutObjectivesForm ssiContestPayoutObjectivesForm = (SSIContestPayoutObjectivesForm)form;
    Long contestId = getContestIdFromClientStateMap( request );
    SSIContest ssiContest = ssiContestPayoutObjectivesForm.toDomain( contestId );
    List<SSIContestParticipant> participants = getSSIContestService().updatePayout( ssiContest, contestId );
    Integer participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contestId );
    SSIContestParticipantView responseView = new SSIContestParticipantView( participants,
                                                                            SSIContestUtil.PAX_RECORDS_PER_PAGE,
                                                                            participantsCount,
                                                                            SSIContestUtil.DEFAULT_SORT_BY,
                                                                            SSIContestUtil.SORT_BY_LAST_NAME,
                                                                            SSIContestUtil.FIRST_PAGE_NUMBER );
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }
}
