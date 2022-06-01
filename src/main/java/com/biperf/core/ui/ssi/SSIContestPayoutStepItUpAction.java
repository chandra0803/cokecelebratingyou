
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
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.ssi.view.SSIContestBaseLineTotalsStepItUpResponseView;
import com.biperf.core.ui.ssi.view.SSIContestGeneralInfoResponseView;
import com.biperf.core.ui.ssi.view.SSIContestLevelResponseView;
import com.biperf.core.ui.ssi.view.SSIContestParticipantView;
import com.biperf.core.ui.ssi.view.SSIContestPayoutStepItUpView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.ssi.SSIContestBaseLineTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestStepItUpAction.
 * 
 * @author Patelp
 * @since jan 08, 2015
 * @version 1.0
 */

public class SSIContestPayoutStepItUpAction extends SSIContestCreateBaseAction
{

  /**
   * Method to load the Step it Up contest when the page is loaded
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward populateContestPayoutsStepItUp( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String clientState = RequestUtils.getRequiredParamString( request, "ssiContestClientState" );
    Long contestId = getContestIdFromClientStateMap( request );
    List<Currency> currencies = getCurrencyService().getAllActiveCurrency();

    SSIContest contest = getSSIContestService().getContestByIdWithAssociations( contestId, getAssociationRequestCollection() );
    flushContestLevels( contest );
    Integer participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contestId );
    Badge ssiBadge = contest.getPromotion().getBadge();
    ssiBadge.setBadgeRules( getSortedBadgeRulesById( ssiBadge.getId() ) );
    SSIContestPayoutStepItUpView contestPayoutStepItUpView = new SSIContestPayoutStepItUpView( contest, clientState, currencies, getSysUrl(), getBillCodeViewByContest( contest ) );
    contestPayoutStepItUpView.getContestJson().setParticipantCount( participantsCount );

    writeAsJsonToResponse( contestPayoutStepItUpView, response );
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
                                                                            1 );
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * Implement the same for all feature. This will update the baseline amount for all the participants in the contest with the same amount.
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward sameForAll( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPayoutStepItUpForm ssiContestStepItUpForm = (SSIContestPayoutStepItUpForm)form;
    Long contestId = getContestIdFromClientStateMap( request );

    getSSIContestParticipantService().updateSameValueForAllPax( contestId, ssiContestStepItUpForm.getKey(), ssiContestStepItUpForm.getParticipants( 0 ) );

    SSIContestBaseLineTotalsValueBean ssiContestBaseLineTotalsValueBean = getSSIContestService().calculateBaseLineTotalsForStepItUp( contestId );

    getSSIContestBaseLineTotalsStepItUpResponseView( response, ssiContestBaseLineTotalsValueBean.getBaselineTotal() );
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
    SSIContestPayoutStepItUpForm ssiContestStepItUpForm = (SSIContestPayoutStepItUpForm)form;
    Long contestId = getContestIdFromClientStateMap( request );
    int pageNumber = getPageNumber( request );
    String sortedOn = getSortedOn( request );
    String sortedBy = getSortedBy( request );
    List<SSIContestParticipantValueBean> formParticipants = ssiContestStepItUpForm.getParticipantsAsList();
    SSIContest ssiContest = ssiContestStepItUpForm.toDomain( contestId );
    List<SSIContestParticipant> participants = null;
    try
    {
      participants = getSSIContestService().saveContestAndFetchNextPageResults( ssiContest, formParticipants, sortedBy, sortedOn, pageNumber );
    }
    catch( ServiceErrorException se )
    {
      addServiceException( messages, se );
    }
    Integer participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contestId );
    SSIContestParticipantView responseView = new SSIContestParticipantView( participants, SSIContestUtil.PAX_RECORDS_PER_PAGE, participantsCount, sortedBy, sortedOn, pageNumber );
    super.writeAsJsonToResponse( responseView, response );
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
    List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
    SSIContestPayoutStepItUpForm ssiContestStepItUpForm = (SSIContestPayoutStepItUpForm)form;
    Long contestId = getContestIdFromClientStateMap( request );
    SSIContest contest = ssiContestStepItUpForm.toDomain( contestId );
    try
    {
      getSSIContestService().savePayoutStepItUp( contest, ssiContestStepItUpForm.getParticipantsAsList() );
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

  /**
   * Update the individual base line amount based on the type and returns the 1st page of participants information
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException 
   */
  public ActionForward updateBaseline( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    SSIContestPayoutStepItUpForm stepItUpForm = (SSIContestPayoutStepItUpForm)form;
    Long contestId = getContestIdFromClientStateMap( request );
    SSIContestParticipantView responseView = null;
    try
    {
      List<SSIContestParticipant> contestParticipants = getSSIContestService().resetAllParticipantsBaseLineAmount( stepItUpForm.getMeasureOverBaseline(), contestId );
      Integer participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contestId );
      responseView = new SSIContestParticipantView( contestParticipants,
                                                    SSIContestUtil.PAX_RECORDS_PER_PAGE,
                                                    participantsCount,
                                                    SSIContestUtil.DEFAULT_SORT_BY,
                                                    SSIContestUtil.SORT_BY_LAST_NAME,
                                                    SSIContestUtil.FIRST_PAGE_NUMBER );
    }
    catch( ServiceErrorException see )
    {
      responseView = new SSIContestParticipantView( addServiceException( see ) );
    }
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
    SSIContestPayoutStepItUpForm stepItUpForm = (SSIContestPayoutStepItUpForm)form;
    List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
    Long contestId = getContestIdFromClientStateMap( request );

    // if MeasureOverBaseline type is "NO" return the call
    if ( !"no".equalsIgnoreCase( stepItUpForm.getMeasureOverBaseline() ) )
    {
      // Save the current page
      SSIContest ssiContest = stepItUpForm.toDomain( contestId );

      try
      {
        getSSIContestService().savePayoutStepItUp( ssiContest, stepItUpForm.getParticipantsAsList() );
      }
      catch( ServiceErrorException se )
      {
        addServiceException( messages, se );
      }

      // Call the stored procedure to validate the participant data and fetch the totals
      SSIContestBaseLineTotalsValueBean ssiContestBaseLineTotalsValueBean = getSSIContestService().calculateBaseLineTotalsForStepItUp( contestId );
      if ( ssiContestBaseLineTotalsValueBean.isEnteredBaseLineForAllPax() )
      {
        getSSIContestBaseLineTotalsStepItUpResponseView( response, ssiContestBaseLineTotalsValueBean.getBaselineTotal() );
      }
      else
      {
        WebErrorMessage message = new WebErrorMessage();
        message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
        message.setSuccess( false );
        message.setText( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.INCOMPLETE_PAX_BASELINES" ) );
        messages.add( message );
        SSIContestBaseLineTotalsStepItUpResponseView responseView = new SSIContestBaseLineTotalsStepItUpResponseView( messages );
        super.writeAsJsonToResponse( responseView, response );
      }
    }
    else
    {
      getSSIContestBaseLineTotalsStepItUpResponseView( response, null );
    }
    return null;
  }

  private void getSSIContestBaseLineTotalsStepItUpResponseView( HttpServletResponse response, Double baselineTotal ) throws IOException
  {
    SSIContestBaseLineTotalsStepItUpResponseView responseView = new SSIContestBaseLineTotalsStepItUpResponseView( baselineTotal );
    super.writeAsJsonToResponse( responseView, response );
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

    SSIContestPayoutStepItUpForm ssiContestStepItUpForm = (SSIContestPayoutStepItUpForm)form;
    Long contestId = getContestIdFromClientStateMap( request );
    SSIContest ssiContest = ssiContestStepItUpForm.toDomain( contestId );
    List<SSIContestParticipant> participants = getSSIContestService().updateLevelPayout( ssiContest, contestId );
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

  /**
   * Create a new SSIContestLevel record
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public ActionForward create( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPayoutStepItUpForm ssiContestStepItUpForm = (SSIContestPayoutStepItUpForm)form;
    Long contestId = getContestIdFromClientStateMap( request );
    SSIContestLevel ssiContestLevel = ssiContestStepItUpForm.toLevelDomain();
    ssiContestLevel = getSSIContestService().saveContestLevel( contestId, ssiContestLevel, ssiContestStepItUpForm.getLevel().getBadgeId() );
    SSIContestLevelResponseView contestLevelResponseView = new SSIContestLevelResponseView( ssiContestLevel );
    writeAsJsonToResponse( contestLevelResponseView, response );
    return null;
  }

  /**
   * Update and existing SSIContestLevel record
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPayoutStepItUpForm ssiContestStepItUpForm = (SSIContestPayoutStepItUpForm)form;
    SSIContestLevel ssiContestLevel = ssiContestStepItUpForm.toLevelDomain();
    getSSIContestService().updateContestLevel( ssiContestLevel, ssiContestStepItUpForm.getLevel().getBadgeId() );
    SSIContestLevelResponseView contestLevelResponseView = new SSIContestLevelResponseView( ssiContestLevel );
    writeAsJsonToResponse( contestLevelResponseView, response );
    return null;
  }

  /**
   * Delete SSIContestLevel record
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */
  public ActionForward remove( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPayoutStepItUpForm ssiContestStepItUpForm = (SSIContestPayoutStepItUpForm)form;
    SSIContestLevel ssiContestLevel = ssiContestStepItUpForm.toLevelDomain();
    Long contestId = getContestIdFromClientStateMap( request );

    getSSIContestService().deleteContestLevel( contestId, ssiContestLevel.getId(), UserManager.getUserId() );

    SSIContestLevelResponseView contestLevelResponseView = new SSIContestLevelResponseView( ssiContestLevel );
    writeAsJsonToResponse( contestLevelResponseView, response );
    return null;
  }

  /*
   * Get the Association Request Collection
   */
  private AssociationRequestCollection getAssociationRequestCollection()
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_LEVELS ) );
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

  private void flushContestLevels( SSIContest contest )
  {
    List<SSIContestLevel> contestLevels = getSSIContestService().getContestLevelsByContestId( contest.getId() );
    contest.setContestLevels( new HashSet( contestLevels ) );

  }

}
