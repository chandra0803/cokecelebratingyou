
package com.biperf.core.ui.ssi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.currency.Currency;
import com.biperf.core.domain.gamification.Badge;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.ssi.SSIContestApproversUpdateAssociation;
import com.biperf.core.service.ssi.SSIContestAwardThemNowService;
import com.biperf.core.service.ssi.SSIContestBillCodesUpdateAssociation;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.ssi.view.SSIContestAwardThemNowMainView;
import com.biperf.core.ui.ssi.view.SSIContestAwardThemNowResponseView;
import com.biperf.core.ui.ssi.view.SSIContestGeneralInfoResponseView;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.ssi.SSIContestNameValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Step 1 of SSI create wizard for Award them now contest.
 * SSIContestGeneralInfoAwardThemNowAction.
 * 
 * @author kandhi
 * @since Feb 3, 2015
 * @version 1.0
 */
public class SSIContestGeneralInfoAwardThemNowAction extends SSIContestGeneralInfoAction
{
  // Step one be loaded only in the contest create mode hence defaulting to 1
  private static final Short DEFAULT_AWARD_ISSUANCE_NUMBER = 1;

  /**
   * Load an empty page to create SSIContest
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward prepareCreate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestGeneralInfoAwardThemNowForm generalInfoForm = (SSIContestGeneralInfoAwardThemNowForm)form;
    SSIPromotion ssiPromotion = getSSIPromotionService().getLiveSSIPromotion();

    // Because this is the first time the contest is being created we are defaulting the award
    // issuance number as 1
    String contestClientState = createSSIContestClientState( null, ssiPromotion, generalInfoForm.getContestType(), DEFAULT_AWARD_ISSUANCE_NUMBER );
    List localeItems = getCMAssetService().getSupportedLocales( false );
    Badge ssiBadge = ssiPromotion.getBadge();
    ssiBadge.setBadgeRules( getSortedBadgeRulesById( ssiBadge.getId() ) );
    SSIContestAwardThemNowMainView pageViewBean = new SSIContestAwardThemNowMainView( new SSIContest(),
                                                                                      ssiPromotion,
                                                                                      contestClientState,
                                                                                      generalInfoForm.getContestType(),
                                                                                      getContestValueBean( new SSIContest(), 0 ),
                                                                                      localeItems,
                                                                                      getSysUrl(),
                                                                                      getBillCodeViewByPromoId( ssiPromotion.getId() ) );

    List<Currency> currencies = getCurrencyService().getAllActiveCurrency();
    pageViewBean.setCurrencies( currencies );
    pageViewBean.setAwardThemNowStatus( SSIContestAwardThemNowMainView.ATN_CREATE );

    // populate name if passed from the create module
    if ( generalInfoForm.getContestName() != null )
    {
      List<SSIContestNameValueBean> contestNames = new ArrayList<SSIContestNameValueBean>();
      contestNames.add( new SSIContestNameValueBean( pageViewBean.getDefaultLanguage(), generalInfoForm.getContestName() ) );
      pageViewBean.setNames( contestNames );
    }
    request.setAttribute( "contestType", SSIContest.CONTEST_TYPE_AWARD_THEM_NOW );
    request.setAttribute( "promotionId", ssiPromotion.getId() );
    request.setAttribute( "initializationJson", toJson( pageViewBean ) );
    return mapping.findForward( FORWARD_TO_GENERAL_INFO );
  }

  /**
   * This method will load the bootstrap for award them now contest for issuing new awards.
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward editInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestGeneralInfoForm generalInfoForm = (SSIContestGeneralInfoForm)form;

    boolean decode = !request.getMethod().equalsIgnoreCase( SSIContestUtil.GET_METHOD );
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, generalInfoForm.getContestId(), decode );

    // Get the next issuance number for this contest
    SSIContest contest = getSSIContestAwardThemNowService().getContestById( contestId );

    // Using super method as we do not want to set the award sequence number in the client state.
    // This is to avoid incorrect sequence number being saved in the client state when some other
    // user is issuing awards for the same contest while the contest is being updated here.
    String contestClientState = super.createSSIContestClientState( contest, contest.getPromotion(), contest.getContestTypeName() );

    List localeItems = getCMAssetService().getSupportedLocales( false );

    // Sending participant count equal to 0 as the count is not valid at the contest level for this
    // award them now.
    int participantsCount = 0;

    SSIContestAwardThemNowMainView pageViewBean = new SSIContestAwardThemNowMainView( contest,
                                                                                      contest.getPromotion(),
                                                                                      contestClientState,
                                                                                      contest.getContestTypeName(),
                                                                                      getContestValueBean( contest, participantsCount ),
                                                                                      localeItems,
                                                                                      getSysUrl(),
                                                                                      getBillCodeViewByContest( contest ) );

    pageViewBean.setAwardThemNowStatus( SSIContestAwardThemNowMainView.ATN_EDIT_INFO );

    pageViewBean.setCurrentStep( generalInfoForm.getCurrentStep() );
    request.setAttribute( "contestType", SSIContest.CONTEST_TYPE_AWARD_THEM_NOW );
    request.setAttribute( "contestName", contest.getContestType().getName() );
    request.setAttribute( "contestId", generalInfoForm.getContestId() );
    request.setAttribute( "promotionId", contest.getPromotion() );
    request.setAttribute( "initializationJson", toJson( pageViewBean ) );
    return mapping.findForward( FORWARD_TO_GENERAL_INFO );
  }

  /**
   * This method will load the bootstrap for award them now contest for issuing new awards.
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward issueMoreAwards( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestGeneralInfoForm generalInfoForm = (SSIContestGeneralInfoForm)form;

    Long contestId = SSIContestUtil.getContestIdFromClientState( request, generalInfoForm.getContestId(), false );

    // Get the next issuance number for this contest
    SSIContest contest = getSSIContestAwardThemNowService().updateContestAwardIssuanceNumber( contestId );

    getSSIContestAwardThemNowService().saveAwardThemNowContest( contest.getAwardIssuanceNumber(), contest );

    String contestClientState = createSSIContestClientState( contest, contest.getPromotion(), contest.getContestTypeName(), contest.getAwardIssuanceNumber() );

    List localeItems = getCMAssetService().getSupportedLocales( false );

    // Get the participant count for this issuance. This will be zero for the current issuance
    // unless the user is toggling between tabs and front end is making a fresh request.
    int participantsCount = getSSIContestAwardThemNowService().getContestParticipantsCount( contest.getId(), contest.getAwardIssuanceNumber() );

    SSIContestAwardThemNowMainView pageViewBean = new SSIContestAwardThemNowMainView( contest,
                                                                                      contest.getPromotion(),
                                                                                      contestClientState,
                                                                                      contest.getContestTypeName(),
                                                                                      getContestValueBean( contest, participantsCount ),
                                                                                      localeItems,
                                                                                      getSysUrl(),
                                                                                      getBillCodeViewByContest( contest ) );

    pageViewBean.setAwardThemNowStatus( SSIContestAwardThemNowMainView.ATN_ISSUE_MORE_AWARDS );

    pageViewBean.setCurrentStep( generalInfoForm.getCurrentStep() );
    request.setAttribute( "contestType", SSIContest.CONTEST_TYPE_AWARD_THEM_NOW );
    request.setAttribute( "contestName", contest.getContestType().getName() );
    request.setAttribute( "contestId", generalInfoForm.getContestId() );
    request.setAttribute( "promotionId", contest.getPromotion() );
    request.setAttribute( "initializationJson", toJson( pageViewBean ) );

    return mapping.findForward( FORWARD_TO_GENERAL_INFO );
  }

  /**
   * Save SSIContest issuance. Called during the contest create.
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestGeneralInfoAwardThemNowForm generalInfoForm = (SSIContestGeneralInfoAwardThemNowForm)form;
    Map<String, Object> clientStateMap = getSSIContestClientStateMap( request );
    String contestType = (String)clientStateMap.get( "contestType" );
    SSIContest ssiContest = generalInfoForm.toDomain( clientStateMap );
    SSIContestGeneralInfoResponseView responseView = null;
    try
    {
      List<UpdateAssociationRequest> updateAssociations = new ArrayList<UpdateAssociationRequest>();
      updateAssociations.add( new SSIContestApproversUpdateAssociation( ssiContest ) );
      updateAssociations.add( new SSIContestBillCodesUpdateAssociation( ssiContest ) );
      // Save the contest with award issuance number as 1 since this is a new contest
      SSIContestAwardThemNow contestAtn = getSSIContestAwardThemNowService()
          .saveContest( ssiContest, generalInfoForm.getContentValueBean(), updateAssociations, generalInfoForm.getBadgeId(), DEFAULT_AWARD_ISSUANCE_NUMBER );

      // Update the client state with the award issuance number
      String contestClientState = createSSIContestClientState( contestAtn.getContest(), contestAtn.getContest().getPromotion(), contestType, contestAtn.getIssuanceNumber() );
      responseView = new SSIContestGeneralInfoResponseView( contestAtn.getContest().getId(), contestClientState );
      request.getSession().setAttribute( "ssiContestExportId", ssiContest.getId() );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      for ( Iterator iter = serviceErrors.iterator(); iter.hasNext(); )
      {
        ServiceError error = (ServiceError)iter.next();
        WebErrorMessage message = new WebErrorMessage();
        message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
        message.setSuccess( false );
        message.setText( CmsResourceBundle.getCmsBundle().getString( error.getKey() ) );
        responseView = new SSIContestGeneralInfoResponseView( message );
      }
    }
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * This method is called only when the 'save' button is clicked on the general info while editing an existing contest
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward saveAtn( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestGeneralInfoAwardThemNowForm generalInfoForm = (SSIContestGeneralInfoAwardThemNowForm)form;
    Map<String, Object> clientStateMap = getSSIContestClientStateMap( request );
    SSIContest ssiContest = generalInfoForm.toDomain( clientStateMap );
    try
    {
      List<UpdateAssociationRequest> updateAssociations = new ArrayList<UpdateAssociationRequest>();
      updateAssociations.add( new SSIContestApproversUpdateAssociation( ssiContest ) );
      updateAssociations.add( new SSIContestBillCodesUpdateAssociation( ssiContest ) );
      SSIContest contest = getSSIContestAwardThemNowService().saveContest( ssiContest, generalInfoForm.getContentValueBean(), updateAssociations );
      SSIContestAwardThemNowResponseView responseView = new SSIContestAwardThemNowResponseView();
      responseView.setForwardUrl( "displayContestSummaryAwardThemNow.do?method=load&contestId=" + SSIContestUtil.getClientState( request, contest.getId(), true ) );
      super.writeAsJsonToResponse( responseView, response );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      for ( Iterator iter = serviceErrors.iterator(); iter.hasNext(); )
      {
        ServiceError error = (ServiceError)iter.next();
        WebErrorMessage message = new WebErrorMessage();
        message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
        message.setSuccess( false );
        message.setText( CmsResourceBundle.getCmsBundle().getString( error.getKey() ) );
        SSIContestGeneralInfoResponseView responseView = new SSIContestGeneralInfoResponseView( message );
        super.writeAsJsonToResponse( responseView, response );
      }
    }
    return null;
  }

  private String createSSIContestClientState( SSIContest ssiContest, SSIPromotion ssiPromotion, String contestType, Short awardIssuanceNumber )
  {
    Map<String, Object> clientStateParamMap = new HashMap<String, Object>();
    if ( ssiContest != null )
    {
      clientStateParamMap.put( "contestId", ssiContest.getId() );
      clientStateParamMap.put( "cmAssetCode", ssiContest.getCmAssetCode() );
      clientStateParamMap.put( "contestVersion", ssiContest.getVersion() );
      clientStateParamMap.put( "createdBy", ssiContest.getAuditCreateInfo().getCreatedBy() );
      clientStateParamMap.put( "createdOn", ssiContest.getAuditCreateInfo().getDateCreated() );
    }
    clientStateParamMap.put( "promotionId", ssiPromotion.getId() );
    clientStateParamMap.put( "contestType", contestType );
    clientStateParamMap.put( AWARD_ISSUANCE_NUMBER, awardIssuanceNumber );
    String password = ClientStatePasswordManager.getPassword();
    return ClientStateSerializer.serialize( clientStateParamMap, password );
  }

  private String getClientState( Long id )
  {
    // put the client state instead of direct id
    Map<String, Object> clientStateParamMap = new HashMap<String, Object>();
    clientStateParamMap.put( "contestId", id );
    String password = ClientStatePasswordManager.getPassword();
    return ClientStateSerializer.serialize( clientStateParamMap, password );
  }

  protected SSIContestAwardThemNowService getSSIContestAwardThemNowService()
  {
    return (SSIContestAwardThemNowService)getService( SSIContestAwardThemNowService.BEAN_NAME );
  }
}
