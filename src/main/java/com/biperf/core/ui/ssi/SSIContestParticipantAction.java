
package com.biperf.core.ui.ssi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestClaimField;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.ssi.SSIContestPaxClaimAssociationRequest;
import com.biperf.core.service.ssi.SSIContestPaxClaimService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.ssi.view.SSIContestClaimDetailView;
import com.biperf.core.ui.ssi.view.SSIContestClaimSubmissionResponseView;
import com.biperf.core.ui.ssi.view.SSIContestDataCollectionDataView;
import com.biperf.core.ui.ssi.view.SSIContestDataCollectionFieldsView;
import com.biperf.core.ui.ssi.view.SSIContestResponseView;
import com.biperf.core.ui.ssi.view.SSIPaxContestActivityHistoryView;
import com.biperf.core.ui.ssi.view.SSIPaxContestDataView;
import com.biperf.core.ui.ssi.view.SSIPaxContestMasterModuleView;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.SSIFileUpload;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestPaginationValueBean;
import com.biperf.core.value.ssi.SSIContestPaxProgressDetailValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * @author dudam
 * @since Nov 19, 2014
 * @version 1.0
 */
public class SSIContestParticipantAction extends SSIAwardRedeemAction
{

  private static final Log logger = LogFactory.getLog( SSIContestParticipantAction.class );

  /** Fetch my live contest for detail page, based on the participant id
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forwardTo = ActionConstants.DISPLAY_FORWARD;
    boolean isCreatorOrManager = false;
    SSIContestListForm ssiContestListForm = (SSIContestListForm)form;
    Long userId = getUserId( ssiContestListForm, request );
    boolean isReportDrillDown = getBooleanValueFromClientSate( ssiContestListForm, request, "isReportDrillDown" );
    boolean isParticipantDrillDown = getBooleanValueFromClientSate( ssiContestListForm, request, "isParticipantDrillDown" );
    forwardTo = isReportDrillDown ? "fromReport" : forwardTo;

    if ( userId == null )
    {
      userId = UserManager.getUserId();
    }
    else
    {
      isCreatorOrManager = true;
      isParticipantDrillDown = true;
    }
    List<SSIContestListValueBean> view = getSSIContestParticipantService().getParticipantLiveContestsValueBean( userId );
    ssiContestListForm.setInitializationJson( toJson( view ) );

    // load the details for the passed contest id
    if ( ssiContestListForm.getId() != null )
    {
      Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestListForm.getId(), false );
      SSIContest contest = getSSIContestService().getContestById( contestId );
      request.setAttribute( "isSuperViewer", getSSIContestParticipantService().isSuperViewer( contest, UserManager.getUser().getUserId() ) );
      SSIPaxContestDataView ssiPaxContestDataView = null;
      if ( isCreatorOrManager )
      {
        ssiPaxContestDataView = populatePaxContestDataView( contest, request, false, userId );
      }
      else
      {
        ssiPaxContestDataView = populatePaxContestDataView( contest, request, false, null );
      }

      ssiPaxContestDataView.setIsReportDrillDown( isReportDrillDown );
      ssiPaxContestDataView.setIsParticipantDrillDown( isParticipantDrillDown );
      ssiContestListForm.setContestJson( toJson( ssiPaxContestDataView ) );
      request.setAttribute( "isCreator", contest.getContestOwnerId().equals( UserManager.getUserId() ) );
    }
    moveToRequest( request );
    return mapping.findForward( forwardTo );
  }

  private boolean getBooleanValueFromClientSate( SSIContestListForm ssiContestListForm, HttpServletRequest request, String key )
  {
    String booleanStringVal = null;
    boolean booleanVal = false;
    try
    {
      String clientState = ssiContestListForm.getId();
      String cryptoPass = request.getParameter( "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();
      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }

      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      booleanStringVal = (String)clientStateMap.get( key );

      booleanVal = !StringUtils.isEmpty( booleanStringVal ) ? Boolean.valueOf( booleanStringVal ) : Boolean.valueOf( false );
    }
    catch( Exception e )
    {
      booleanVal = false;
    }
    return booleanVal;
  }

  private Long getUserId( SSIContestListForm ssiContestListForm, HttpServletRequest request )
  {
    Long userId = null;
    try
    {
      String clientState = ssiContestListForm.getId();
      String cryptoPass = request.getParameter( "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();
      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      userId = Long.valueOf( clientStateMap.get( "userId" ).toString() );
    }
    catch( Exception e )
    {
      userId = null;
    }
    return userId;
  }

  /** Fetch archived contest based on the participant id
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchArchivedContest( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<SSIContestListValueBean> view = getSSIContestParticipantService().getParticipantArchivedContests( UserManager.getUserId() );
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  /** Fetch my live contest for tile, based on the participant id
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchContests( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<SSIContest> contests = getSSIContestParticipantService().getParticipantLiveContests( UserManager.getUserId() );
    SSIPaxContestMasterModuleView masterView = new SSIPaxContestMasterModuleView();
    List<SSIPaxContestDataView> masterModuleList = new ArrayList<SSIPaxContestDataView>();
    for ( SSIContest contest : contests )
    {
      // no tile to display for award them now contest
      if ( !SSIContestType.AWARD_THEM_NOW.equals( contest.getContestType().getCode() ) )
      {
        SSIPaxContestDataView view = populatePaxContestDataView( contest, request, true, null );
        masterModuleList.add( view );
      }
    }
    Collections.sort( masterModuleList );
    masterView.setMasterModuleList( masterModuleList );
    super.writeAsJsonToResponse( masterView, response );
    return null;
  }

  /** Fetches contest details based on contest id
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchContestDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestListForm ssiContestListForm = (SSIContestListForm)form;
    Long userId = SSIContestUtil.getUserIdFromClientState( request, ssiContestListForm.getId(), true );
    if ( displayDetail( userId ) )
    {
      Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestListForm.getId(), true );
      SSIContest contest = getSSIContestService().getContestById( contestId );
      SSIPaxContestDataView responseView = populatePaxContestDataView( contest, request, false, null );
      super.writeAsJsonToResponse( responseView, response );
    }
    return null;
  }

  /**
   * 
   * @param contest
   * @param request
   * @return
   */
  private SSIPaxContestDataView populatePaxContestDataView( SSIContest contest, HttpServletRequest request, boolean fromTile, Long userId )
  {
    Long paxId = null;
    String paxName = "";
    boolean isCreatorOrManager = false;
    if ( userId == null )
    {
      paxId = UserManager.getUserId();
    }
    else
    {
      isCreatorOrManager = true;
      paxId = userId;
      paxName = getFullName( paxId );
    }

    SSIContestParticipant contestParticipant = getSSIContestService().getContestParticipantByContestIdAndPaxId( contest.getId(), paxId );
    SSIPaxContestDataView responseView = null;
    SSIContestPaxProgressDetailValueBean paxProgressValueBean = null;
    try
    {
      paxProgressValueBean = getSSIContestParticipantService().getContestParticipantProgress( contest.getId(), paxId );
      String shoppingUrl = buildShopUrl( contest, request, contestParticipant.getId() );
      List<SSIContestStackRankPaxValueBean> stackRanks = null;
      if ( fromTile && contest.getContestType().isStackRank() )
      {
        stackRanks = getSSIContestParticipantService().getContestStackRank( contest.getId(), null, null, 1, 20, false, true );
      }
      String creatorName = getFullName( contest.getCreatorId() );
      responseView = new SSIPaxContestDataView( contest,
                                                paxProgressValueBean,
                                                shoppingUrl,
                                                getContestValueBean( contest, 0 ),
                                                stackRanks,
                                                paxId,
                                                contestParticipant,
                                                creatorName,
                                                getSysUrl(),
                                                isCreatorOrManager,
                                                paxName );
    }
    catch( ServiceErrorException see )
    {
      responseView = new SSIPaxContestDataView( addServiceException( see ) );
    }
    return responseView;
  }

  /** Fetches contest activity history based on contest id
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchActivityHistory( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestActivityHistoryForm ssiContestActivityHistoryForm = (SSIContestActivityHistoryForm)form;

    // collecting input params
    boolean decode = !request.getMethod().equalsIgnoreCase( SSIContestUtil.GET_METHOD );
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestActivityHistoryForm.getId(), decode );
    Long participantId = getParticipantId( request, ssiContestActivityHistoryForm, decode );
    boolean fromDrilldown = isFromDrillDown( request, ssiContestActivityHistoryForm, decode );

    SSIContestPaginationValueBean paginationParams = getPaginationValueBean( ssiContestActivityHistoryForm );

    SSIContestParticipant contestParticipant = null;
    SSIContest contest = null;
    List<SSIContestPaxClaim> myClaims = null;
    String totalAmount = null;
    int precision = 0;
    int totalCount = getSSIContestPaxClaimService().getPaxClaimsCountBySubmitterId( contestId, participantId );

    if ( totalCount > 0 )
    {
      myClaims = getSSIContestPaxClaimService().getPaxClaimsBySubmitterId( contestId, participantId, paginationParams );
      // to get activity description for obj contest type
      if ( myClaims != null && myClaims.size() > 0 )
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_ACTIVITIES ) );
        contest = getSSIContestService().getContestByIdWithAssociations( contestId, associationRequestCollection );
        if ( contest.getContestType().isObjectives() && !contest.getSameObjectiveDescription() )
        {
          contestParticipant = getSSIContestService().getContestParticipantByContestIdAndPaxId( contestId, participantId );
        }
      }
      Double totalActivityAmt = getSSIContestPaxClaimService().getClaimsActivityAmount( contestId, participantId );
      totalAmount = getActivityPrefix( contest ) + SSIContestUtil.getFormattedValue( totalActivityAmt, SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() ) );
      precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
    }
    SSIPaxContestActivityHistoryView responseView = new SSIPaxContestActivityHistoryView( myClaims, totalAmount, contestParticipant, getSysUrl(), contest, participantId, fromDrilldown );
    responseView.addPaginationParams( paginationParams, totalCount );
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  private Long getParticipantId( HttpServletRequest request, SSIContestActivityHistoryForm ssiContestActivityHistoryForm, boolean decode )
  {
    Long userId = 0L;
    try
    {
      userId = SSIContestUtil.getUserIdFromClientState( request, ssiContestActivityHistoryForm.getId(), decode );
    }
    catch( Exception e )
    {
      userId = UserManager.getUserId();
    }
    return userId;
  }

  private boolean isFromDrillDown( HttpServletRequest request, SSIContestActivityHistoryForm ssiContestActivityHistoryForm, boolean decode )
  {
    boolean fromDrillDown = true;
    try
    {
      SSIContestUtil.getUserIdFromClientState( request, ssiContestActivityHistoryForm.getId(), decode );
    }
    catch( Exception e )
    {
      fromDrillDown = false;
    }
    return fromDrillDown;
  }

  private SSIContestPaginationValueBean getPaginationValueBean( SSIContestActivityHistoryForm form )
  {
    SSIContestPaginationValueBean valueBean = new SSIContestPaginationValueBean();
    valueBean.setCurrentPage( form.getPage() == 0 ? SSIContestUtil.FIRST_PAGE_NUMBER : form.getPage() );
    valueBean.setPageSize( SSIContestUtil.CLAIM_HISTORY_PER_PAGE );
    valueBean.setSortedBy( StringUtil.isNullOrEmpty( form.getSortedBy() ) ? SSIContestUtil.DEFAULT_SORT_BY : form.getSortedBy() );
    valueBean.setSortedOn( StringUtil.isNullOrEmpty( form.getSortedOn() ) ? SSIContestUtil.CLAIMS_DEFAULT_SORT_ON : form.getSortedOn() );
    return valueBean;
  }

  public ActionForward fetchStackRank( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestListForm ssiContestListForm = (SSIContestListForm)form;
    ssiContestListForm.setRole( SSIContest.CONTEST_ROLE_PAX );
    ssiContestListForm.setUserId( null );
    return super.fetchStackRankTable( mapping, ssiContestListForm, request, response );
  }

  public ActionForward displayClaimForm( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long contestId = getClientStateParameterValueAsLong( request, SSIContestUtil.CONTEST_ID );
    Set<Long> contestIds = new HashSet<Long>();
    contestIds.add( contestId );
    List<NameIdBean> contestNames = getSSIContestService().getContestNames( contestIds, "en_US" );
    request.setAttribute( "contestId", SSIContestUtil.getClientState( contestId ) );
    request.setAttribute( "contestName", contestNames.get( 0 ).getName() );
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  public ActionForward populateClaimFormFields( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContest contest = getContest( request );
    List<Country> activeCountryList = getCountryService().getAllActive();
    SSIContestParticipant contestParticipant = null;
    if ( contest.getContestType().isObjectives() && !contest.getSameObjectiveDescription() )
    {
      contestParticipant = getSSIContestService().getContestParticipantByContestIdAndPaxId( contest.getId(), UserManager.getUserId() );
    }
    SSIContestDataCollectionDataView responseView = new SSIContestDataCollectionDataView( contest, activeCountryList, contestParticipant );
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  private SSIContest getContest( HttpServletRequest request ) throws InvalidClientStateException
  {
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, request.getParameter( "id" ), false );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_CLAIM_FIELDS ) );
    associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_ACTIVITIES ) );
    return getSSIContestService().getContestByIdWithAssociations( contestId, associationRequestCollection );
  }

  public ActionForward uploadDocument( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException, IOException
  {
    SSIContestResponseView responseView = null;
    SSIContestClaimSubmissionForm claimSubmissionForm = (SSIContestClaimSubmissionForm)form;
    SSIFileUpload data = getFileUploadData( claimSubmissionForm );
    SSIContestClaimField claimField = getSSIContestService().getContestClaimFieldById( data.getId() );
    if ( claimField == null )
    {
      responseView = new SSIContestResponseView( new WebErrorMessage( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR,
                                                                      false,
                                                                      CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.FILE_UPLOAD_FAILED" ) ) );
    }
    else if ( !isValidFileSize( claimField, data ) )
    {
      responseView = new SSIContestResponseView( new WebErrorMessage( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR,
                                                                      false,
                                                                      MessageFormat.format( ContentReaderManager.getText( "ssi_contest.claims", "INVALID_FILE_SIZE" ),
                                                                                            new Object[] { claimField.getFormElement().getFileSize() } ) ) );
    }
    else if ( !isValidFileType( claimField, data ) )
    {
      responseView = new SSIContestResponseView( new WebErrorMessage( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR,
                                                                      false,
                                                                      MessageFormat.format( ContentReaderManager.getText( "ssi_contest.claims", "INVALID_FILE_TYPE" ),
                                                                                            new Object[] { claimField.getFormElement().getFileType() } ) ) );
    }
    else
    {
      try
      {
        data = getSSIContestPaxClaimService().uploadClaimDocument( data );
        responseView = new SSIContestResponseView( getMessage( data ) );
      }
      catch( ServiceErrorException see )
      {
        logger.error( "Error while uploading docuemnt for claim submission: " + see );
        responseView = new SSIContestResponseView( addServiceException( see ) );
      }
    }
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  private SSIFileUpload getFileUploadData( SSIContestClaimSubmissionForm claimSubmissionForm ) throws FileNotFoundException, IOException
  {
    SSIFileUpload data = new SSIFileUpload();
    String filename = claimSubmissionForm.getSsiClaimDoc().getFileName();
    int filesize = claimSubmissionForm.getSsiClaimDoc().getFileSize();
    byte[] imageInByte = claimSubmissionForm.getSsiClaimDoc().getFileData();
    String fileExtension = filename.substring( filename.lastIndexOf( "." ) + 1, filename.length() );
    Long fieldId = 0L;
    for ( SSIContestDataCollectionFieldsView field : claimSubmissionForm.getFieldsAsList() )
    {
      if ( field.getId() != null && field.getId().longValue() > 0 )
      {
        fieldId = field.getId();
        break;
      }
    }
    data.setId( fieldId );
    data.setData( imageInByte );
    data.setType( fileExtension );
    data.setName( filename );
    data.setSize( filesize );
    return data;
  }

  private boolean isValidFileSize( SSIContestClaimField claimField, SSIFileUpload data )
  {
    boolean valid = false;
    int acceptedFileSizeInMB = claimField.getFormElement().getFileSize();
    int acceptedFileSizeInBytes = SSIContestUtil.MEGABYTES_TO_BYTES_MULTIPLIER * acceptedFileSizeInMB;
    if ( data.getSize() <= acceptedFileSizeInBytes )
    {
      valid = true;
    }
    return valid;
  }

  private boolean isValidFileType( SSIContestClaimField claimField, SSIFileUpload data )
  {
    return StringUtils.contains( claimField.getFormElement().getFileType().toLowerCase(), data.getType().toLowerCase() );
  }

  private WebErrorMessage getMessage( SSIFileUpload data )
  {
    String docUrl = SSIContestUtil.getCm3damBaseUrl() + data.getFull();
    String fileName = data.getName();
    String extension = "." + FilenameUtils.getExtension( fileName );
    return new WebErrorMessage( WebResponseConstants.RESPONSE_TYPE_SUCCESS, "", true, docUrl, fileName.substring( 0, fileName.length() - extension.length() ) );
  }

  public ActionForward saveClaim( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestClaimSubmissionResponseView responseView = null;
    SSIContestClaimSubmissionForm claimSubmissionForm = (SSIContestClaimSubmissionForm)form;
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_CLAIM_FIELDS ) );
    SSIContest contest = getSSIContestService().getContestByIdWithAssociations( claimSubmissionForm.getContestId(), associationRequestCollection );
    SSIContestPaxClaim paxClaim = claimSubmissionForm.toDomain( contest, UserManager.getUserId() );
    try
    {
      paxClaim = getSSIContestPaxClaimService().savePaxClaim( paxClaim );
      String successMessage = MessageFormat.format( ContentReaderManager.getText( "ssi_contest.participant", "CLAIM_SUCCESS" ),
                                                    new Object[] { paxClaim.getClaimNumber(), DateUtils.toDisplayString( new Date() ) } );
      request.getSession().setAttribute( SSIContestUtil.SHOW_MODAL, Boolean.TRUE );
      request.getSession().setAttribute( SSIContestUtil.MODAL_MESSAGE, successMessage );
      responseView = new SSIContestClaimSubmissionResponseView( getNextUrl( SSIContestUtil.getClientState( claimSubmissionForm.getContestId() ) ) );
    }
    catch( ServiceErrorException se )
    {
      responseView = new SSIContestClaimSubmissionResponseView( addServiceException( se ) );
    }
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  public ActionForward viewClaim( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long claimId = getClientStateParameterValueAsLong( request, SSIContestUtil.CLAIM_ID );
    Long userId = null;
    try
    {
      userId = getClientStateParameterValueAsLong( request, SSIContestUtil.USER_ID );
    }
    catch( Exception e )
    {
      // do nothing
    }
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SSIContestPaxClaimAssociationRequest( SSIContestPaxClaimAssociationRequest.CLAIM_FIELDS ) );
    SSIContestPaxClaim paxClaim = getSSIContestPaxClaimService().getPaxClaimByIdWithAssociations( claimId, associationRequestCollection );

    SSIContestClaimDetailForm ssiContestClaimDetailForm = (SSIContestClaimDetailForm)form;
    SSIContestClaimDetailView contestJson = popluateClaimDetailValueBean( paxClaim, ssiContestClaimDetailForm.getClientState() );
    ssiContestClaimDetailForm.setInitializationJson( toJson( contestJson ) );

    // userId will be null if pax is viewing claim details or else creator/manager pax drilldown
    if ( userId == null )
    {
      ssiContestClaimDetailForm.setBackButtonUrl( getNextUrl( SSIContestUtil.getClientState( paxClaim.getContestId() ) ) );
    }
    else
    {
      ssiContestClaimDetailForm.setBackButtonUrl( SSIContestUtil.populateParticipantDetailPageUrl( paxClaim.getContestId(), userId ) );
    }
    request.setAttribute( "pageTitle", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.CLAIM_DETAIL_TITLE" ) );
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  private String getNextUrl( String encryptedId )
  {
    return getSysUrl() + PageConstants.SSI_PARTICIPANT_LIST_PAGE + encryptedId;
  }

  protected SSIContestPaxClaimService getSSIContestPaxClaimService()
  {
    return (SSIContestPaxClaimService)getService( SSIContestPaxClaimService.BEAN_NAME );
  }

  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }

}
