
package com.biperf.core.ui.ssi;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.ssi.SSIAdminContestActions;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.SSIContestStackRankUpdateProcess;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.ssi.SSIContestApproversUpdateAssociation;
import com.biperf.core.service.ssi.SSIContestFileUploadService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.ssi.view.SSIContestApproversView;
import com.biperf.core.ui.ssi.view.SSIContestBillCodeView;
import com.biperf.core.ui.ssi.view.SSIContestCheckNameView;
import com.biperf.core.ui.ssi.view.SSIContestDescriptionsView;
import com.biperf.core.ui.ssi.view.SSIContestDocumentUploadView;
import com.biperf.core.ui.ssi.view.SSIContestDocumentsView;
import com.biperf.core.ui.ssi.view.SSIContestFileUploadPropertiesView;
import com.biperf.core.ui.ssi.view.SSIContestGeneralInfoResponseView;
import com.biperf.core.ui.ssi.view.SSIContestMainView;
import com.biperf.core.ui.ssi.view.SSIContestMessagesView;
import com.biperf.core.ui.ssi.view.SSIContestNamesView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.ssi.SSIContestDescriptionValueBean;
import com.biperf.core.value.ssi.SSIContestDocumentValueBean;
import com.biperf.core.value.ssi.SSIContestFileUploadValue;
import com.biperf.core.value.ssi.SSIContestMessageValueBean;
import com.biperf.core.value.ssi.SSIContestNameValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Action class to create/edit an SSIContest SSIContestGeneralInfoAction section
 * SSIContestGeneralInfoAction.
 * 
 * @author kandhi
 * @since Nov 7, 2014
 * @version 1.0
 */
public class SSIContestGeneralInfoAction extends SSIContestCreateBaseAction
{

  protected static final String FORWARD_TO_GENERAL_INFO = "display_general_info";
  protected static final String FORWARD_TO_ATN_GENERAL_INFO = "display_atn_general_info";
  /**
   * Return code returned from the stored procedure
   */
  public static final String OUTPUT_RETURN_CODE = "p_out_return_code";

  /**
   * Result set returned from the stored procedure
   */
  public static final String OUTPUT_RESULT_SET = "p_out_ref_cursor";

  protected static final String NEW_LINE = "\n";

  /**
   * Stored proc returns this code when the stored procedure executed without errors
   */
  public static final String GOOD = "00";

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
    SSIContestGeneralInfoForm generalInfoForm = (SSIContestGeneralInfoForm)form;
    String forward = FORWARD_TO_GENERAL_INFO;

    if ( generalInfoForm.getContestType() != null && SSIContest.CONTEST_TYPE_AWARD_THEM_NOW.equals( generalInfoForm.getContestType() ) )
    {
      forward = FORWARD_TO_ATN_GENERAL_INFO;
    }
    else
    {
      SSIPromotion ssiPromotion = getSSIPromotionService().getLiveSSIPromotion();
      String contestClientState = createSSIContestClientState( null, ssiPromotion, generalInfoForm.getContestType() );
      List localeItems = getCMAssetService().getSupportedLocales( false );
      List<SSIContestBillCodeView> billCodes = new ArrayList<SSIContestBillCodeView>();

      if ( SSIContest.CONTEST_TYPE_AWARD_THEM_NOW.equals( generalInfoForm.getContestType() ) )
      {
        billCodes = getBillCodeViewByPromoId( ssiPromotion.getId() );
      }

      SSIContestMainView pageViewBean = new SSIContestMainView( new SSIContest(),
                                                                ssiPromotion,
                                                                contestClientState,
                                                                generalInfoForm.getContestType(),
                                                                getContestValueBean( new SSIContest(), 0 ),
                                                                localeItems,
                                                                getSysUrl(),
                                                                billCodes );

      // populate name if passed from the create module
      if ( generalInfoForm.getContestName() != null )
      {
        List<SSIContestNameValueBean> contestNames = new ArrayList<SSIContestNameValueBean>();
        contestNames.add( new SSIContestNameValueBean( pageViewBean.getDefaultLanguage(), generalInfoForm.getContestName() ) );
        pageViewBean.setNames( contestNames );
      }

      request.setAttribute( "promotionId", ssiPromotion.getId() );
      request.setAttribute( "initializationJson", toJson( pageViewBean ) );
    }
    if ( Objects.nonNull( generalInfoForm.getContestType() ) )
    {
      String contestName = SSIContest.getContestTypeFromName( generalInfoForm.getContestType() ).getName();
      request.setAttribute( "contestName", contestName );
    }

    return mapping.findForward( forward );
  }

  /**
   * Load the first page in edit mode
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward prepareEdit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestGeneralInfoForm generalInfoForm = (SSIContestGeneralInfoForm)form;
    SSIPromotion ssiPromotion = getSSIPromotionService().getLiveSSIPromotion();
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, generalInfoForm.getContestId(), false );
    request.getSession().setAttribute( "ssiContestExportId", contestId );
    SSIContest contest = getSSIContestService().getContestById( contestId );
    String contestClientState = createSSIContestClientState( contest, ssiPromotion, contest.getContestTypeName() );
    List localeItems = getCMAssetService().getSupportedLocales( false );
    int participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contest.getId() );

    SSIContestMainView pageViewBean = new SSIContestMainView( contest,
                                                              ssiPromotion,
                                                              contestClientState,
                                                              contest.getContestTypeName(),
                                                              getContestValueBean( contest, participantsCount ),
                                                              localeItems,
                                                              getSysUrl(),
                                                              getBillCodeViewByContest( contest ) );

    pageViewBean.setCurrentStep( generalInfoForm.getCurrentStep() );
    request.setAttribute( "contestId", generalInfoForm.getContestId() );
    request.setAttribute( "promotionId", ssiPromotion.getId() );
    request.setAttribute( "initializationJson", toJson( pageViewBean ) );

    Map map = new HashMap();
    map.put( "contestId", contest.getId() );
    request.setAttribute( "contestName", contest.getContestType().getName() );
    String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    request.setAttribute( "notifyUrl", ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.SSI_NOTIFIY_APPROVERS, map ) );
    if ( UserManager.getUser().isSSIAdmin() )
    {
      return mapping.findForward( "display_general_info_ssiadmin" );
    }
    else
    {
      return mapping.findForward( FORWARD_TO_GENERAL_INFO );
    }

  }

  /**
   * Save SSIContest
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestGeneralInfoForm generalInfoForm = (SSIContestGeneralInfoForm)form;
    Map<String, Object> clientStateMap = getSSIContestClientStateMap( request );
    String contestType = (String)clientStateMap.get( "contestType" );
    SSIContest ssiContest = generalInfoForm.toDomain( clientStateMap );
    SSIContestGeneralInfoResponseView responseView = null;

    try
    {
      List<UpdateAssociationRequest> updateAssociations = new ArrayList<UpdateAssociationRequest>();
      updateAssociations.add( new SSIContestApproversUpdateAssociation( ssiContest ) );
      // updateAssociations.add( new SSIContestBillCodesUpdateAssociation( ssiContest ) );
      ssiContest = getSSIContestService().saveContest( ssiContest, generalInfoForm.getContentValueBean(), updateAssociations );
      String contestClientState = createSSIContestClientState( ssiContest, ssiContest.getPromotion(), contestType );
      responseView = new SSIContestGeneralInfoResponseView( ssiContest.getId(), contestClientState );
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

    // Track SSI Admin Action
    if ( UserManager.getUser().isSSIAdmin() )
    {
      getSSIContestService().saveAdminAction( ssiContest.getId(),
                                              UserManager.getUser().getOriginalAuthenticatedUser().getUserId(),
                                              SSIAdminContestActions.EDIT_CONTEST,
                                              "Contest Edited for Contest ID: " + ssiContest.getId() + " by Admin ID: " + UserManager.getUser().getOriginalAuthenticatedUser().getUserId() );

    }
    else if ( UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_LOGIN_AS" ) ) )
    {
      getSSIContestService().saveAdminAction( ssiContest.getId(),
                                              UserManager.getUser().getOriginalAuthenticatedUser().getUserId(),
                                              SSIAdminContestActions.CREATE_CONTEST,
                                              "Contest Created for Contest ID: " + ssiContest.getId() + " by Admin ID: " + UserManager.getUser().getOriginalAuthenticatedUser().getUserId() );
    }
    else
    {
      if ( SSIContestUtil.canAddSSIAdminInEMailNotificxations( ssiContest.getId() ) != null )
      {
        Mailing createMailing = getMailingService()
            .buildSSIContestEditNotification( getSSIContestService().getContestById( ssiContest.getId() ),
                                              getUserService().getPrimaryUserEmailAddress( SSIContestUtil.canAddSSIAdminInEMailNotificxations( ssiContest.getId() ).getUserID() ).getEmailAddr() );
        getMailingService().submitMailing( createMailing, null );
      }
    }
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward notifyApprovers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long contestId = getContestId( request );
    getSSIContestService().sendContestUpdateNotificationToApprovers( getContestId( request ) );
    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "contestId", new String[] { contestId.toString() } );
    Process process = getProcessService().createOrLoadSystemProcess( "ssiContestStackRankUpdateProcess", SSIContestStackRankUpdateProcess.BEAN_NAME );
    // Track SSI Admin Action
    if ( UserManager.getUser().isSSIAdmin() || UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_LOGIN_AS" ) ) )
    {
      getSSIContestService().saveAdminAction( contestId,
                                              UserManager.getUser().getOriginalAuthenticatedUser().getUserId(),
                                              SSIAdminContestActions.EDIT_CONTEST,
                                              "Contest edited for Contest ID: " + contestId + " by Admin ID: " + UserManager.getUser().getOriginalAuthenticatedUser().getUserId() );

    }
    else
    {
      if ( SSIContestUtil.canAddSSIAdminInEMailNotificxations( contestId ) != null )
      {
        Mailing createMailing = getMailingService()
            .buildSSIContestEditNotification( getSSIContestService().getContestById( contestId ),
                                              getUserService().getPrimaryUserEmailAddress( SSIContestUtil.canAddSSIAdminInEMailNotificxations( contestId ).getUserID() ).getEmailAddr() );
        getMailingService().submitMailing( createMailing, null );
      }
    }

    getProcessService().launchProcess( process, parameterValueMap, UserManager.getUserId() );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Saves the contest and return to the list page
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward saveAsDraft( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestGeneralInfoForm generalInfoForm = (SSIContestGeneralInfoForm)form;
    Map<String, Object> clientStateMap = getSSIContestClientStateMap( request );
    String contestType = (String)clientStateMap.get( "contestType" );
    SSIContest ssiContest = generalInfoForm.toDomain( clientStateMap );
    SSIContestGeneralInfoResponseView responseView = null;
    try
    {
      List<UpdateAssociationRequest> updateAssociations = new ArrayList<UpdateAssociationRequest>();
      updateAssociations.add( new SSIContestApproversUpdateAssociation( ssiContest ) );
      // updateAssociations.add( new SSIContestBillCodesUpdateAssociation( ssiContest ) );
      ssiContest = getSSIContestService().saveContest( ssiContest, generalInfoForm.getContentValueBean(), updateAssociations );
      String contestClientState = createSSIContestClientState( ssiContest, ssiContest.getPromotion(), contestType );
      responseView = new SSIContestGeneralInfoResponseView( ssiContest.getId(), contestClientState );
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
    // Track SSI Admin Action
    if ( UserManager.getUser().isSSIAdmin() || UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_LOGIN_AS" ) ) )
    {
      getSSIContestService().saveAdminAction( ssiContest.getId(),
                                              UserManager.getUser().getOriginalAuthenticatedUser().getUserId(),
                                              SSIAdminContestActions.SAVE_AS_DRAFT,
                                              "Contest edited as 'Save As Draft' for Contest ID: " + ssiContest.getId() + " by Admin ID: "
                                                  + UserManager.getUser().getOriginalAuthenticatedUser().getUserId() );

    }

    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * Update an existing SSIContest
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward prepareUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    Long contestId = getContestIdFromClientState( clientState, cryptoPass );

    SSIContest contest = getSSIContestService().getContestById( contestId );

    String contestClientState = createSSIContestClientState( contest, contest.getPromotion(), contest.getContestTypeName() );
    int participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contest.getId() );
    List localeItems = getCMAssetService().getSupportedLocales( false );
    SSIContestMainView pageViewBean = new SSIContestMainView( contest,
                                                              contest.getPromotion(),
                                                              contestClientState,
                                                              contest.getContestTypeName(),
                                                              getContestValueBean( contest, participantsCount ),
                                                              localeItems,
                                                              getSysUrl(),
                                                              new ArrayList<SSIContestBillCodeView>() );

    request.setAttribute( "initializationJson", toJson( pageViewBean ) );
    return mapping.findForward( FORWARD_TO_GENERAL_INFO );
  }

  /**
   * Checks whether there is already a contest with the same name
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward validateContestName( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestCheckNameView contestCheckNameView = new SSIContestCheckNameView();
    WebErrorMessage message = new WebErrorMessage();
    String contestName = request.getParameter( "contestName" );
    String locale = request.getParameter( "language" );

    Map<String, Object> clientStateMap = getSSIContestClientStateMap( request );
    Long contestId = (Long)clientStateMap.get( SSIContestUtil.CONTEST_ID );

    boolean isContestNameUnique = getSSIContestService().isContestNameUnique( contestName, contestId != null && contestId > 0 ? contestId : null, locale );
    if ( isContestNameUnique )
    {
      message.setSuccess( true );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SUCCESS );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.CONTEST_NAME_UNIQUE" ) );
    }
    else
    {
      message.setSuccess( false );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.CONTEST_NAME_NOT_UNIQUE" ) );
    }
    contestCheckNameView.getMessages().add( message );
    writeAsJsonToResponse( contestCheckNameView, response );
    return null;
  }

  /**
   * Returns JSON of all the allowed contest approvers in the promotion and the selected contest approvers if the contest in existing one.
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward populateContestApprovers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> clientStateMap = getSSIContestClientStateMap( request );
    Long contestId = (Long)clientStateMap.get( SSIContestUtil.CONTEST_ID );
    Long promotionId = (Long)clientStateMap.get( "promotionId" );

    Map<String, Set<Participant>> allowedContestApprovers = getSSIPromotionService().getAllowedContestApprovers( promotionId );
    Map<String, Set<Participant>> selectedContestApprovers = getSSIContestService().getSelectedContestApprovers( contestId );

    SSIContestApproversView contestApproversView = new SSIContestApproversView( allowedContestApprovers, selectedContestApprovers );
    writeAsJsonToResponse( contestApproversView, response );
    return null;
  }

  public ActionForward populateContestTranslationNames( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> clientStateMap = getSSIContestClientStateMap( request );
    String cmAssetCode = (String)clientStateMap.get( "cmAssetCode" );

    SSIContestNamesView name = new SSIContestNamesView();
    List<SSIContestNameValueBean> contestNames = getSSIContestService().getTranslatedContestNames( cmAssetCode, SSIContest.CONTEST_CMASSET_NAME );
    name.setNames( contestNames );
    writeAsJsonToResponse( name, response );
    return null;
  }

  public ActionForward populateContestTranslationDescriptions( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> clientStateMap = getSSIContestClientStateMap( request );
    String cmAssetCode = (String)clientStateMap.get( "cmAssetCode" );

    SSIContestDescriptionsView description = new SSIContestDescriptionsView();
    List<SSIContestDescriptionValueBean> contestDescriptions = getSSIContestService().getTranslatedContestDescriptions( cmAssetCode, SSIContest.CONTEST_CMASSET_DESCRIPTION );
    description.setDescriptions( contestDescriptions );
    writeAsJsonToResponse( description, response );
    return null;
  }

  public ActionForward populateContestTranslationMessages( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> clientStateMap = getSSIContestClientStateMap( request );
    String cmAssetCode = (String)clientStateMap.get( "cmAssetCode" );

    SSIContestMessagesView message = new SSIContestMessagesView();
    List<SSIContestMessageValueBean> contestMessages = getSSIContestService().getTranslatedContestMessages( cmAssetCode, SSIContest.CONTEST_CMASSET_MESSAGE );
    message.setMessages( contestMessages );
    writeAsJsonToResponse( message, response );
    return null;
  }

  public ActionForward populateContestTranslationDocuments( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> clientStateMap = getSSIContestClientStateMap( request );
    String cmAssetCode = (String)clientStateMap.get( "cmAssetCode" );

    SSIContestDocumentsView document = new SSIContestDocumentsView();
    List<SSIContestDocumentValueBean> contestDocuments = getSSIContestService().getTranslatedContestDocuments( cmAssetCode );
    document.setDocuments( contestDocuments );
    writeAsJsonToResponse( document, response );
    return null;
  }

  protected String createSSIContestClientState( SSIContest ssiContest, SSIPromotion ssiPromotion, String contestType )
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
    String password = ClientStatePasswordManager.getPassword();
    return ClientStateSerializer.serialize( clientStateParamMap, password );
  }

  public ActionForward uploadContestDocuments( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestGeneralInfoForm generalInfoForm = (SSIContestGeneralInfoForm)form;

    // Creating json view objects
    SSIContestDocumentUploadView contestUpload = new SSIContestDocumentUploadView();
    SSIContestFileUploadPropertiesView propertiesView = new SSIContestFileUploadPropertiesView();

    FormFile contestUploadFile = generalInfoForm.getDocumentFile();

    String orginalfilename = contestUploadFile.getFileName();
    String fileFormat = getFileExtension( orginalfilename );
    String extension = "." + fileFormat;
    String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
    if ( filename != null )
    {
      filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );

    }
    filename = filename + extension;

    int filesize = contestUploadFile.getFileSize();
    byte[] imageInByte = contestUploadFile.getFileData();

    SSIContestFileUploadValue data = new SSIContestFileUploadValue();
    Random r = new Random();
    data.setId( Long.parseLong( String.valueOf( r.nextInt( 10000 ) ) ) );
    data.setData( imageInByte );
    data.setName( filename );
    data.setSize( filesize );
    setFileType( data, fileFormat );
    try
    {
      data = getSSIContestFileUploadService().uploadContestDocument( data );
      String fullImageUrlPath = ImageUtils.getImageUploadPath() + data.getFull();
      propertiesView.setFileUrl( fullImageUrlPath );
      propertiesView.setOriginalFilename( orginalfilename );
      propertiesView.setIsSuccess( true );
      /*
       * try { getSSIContestFileUploadService().deleteMediaFromWebdav( data.getFull() ); } catch(
       * Exception e ) { log.error( "<<<<<<<ERROR >>>>>>" + e ); }
       */
    }
    catch( Exception e )
    {
      log.error( "<<<<<<<ERROR >>>>>>" + e );
      propertiesView.setIsSuccess( false );
      propertiesView.setOriginalFilename( orginalfilename );
      propertiesView.setErrorText( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.FILE_UPLOAD_INVALID" ) );
    }

    contestUpload.setProperties( propertiesView );
    writeAsJsonToResponse( contestUpload, response );
    return null;
  }

  private void setFileType( SSIContestFileUploadValue data, String fileFormat )
  {
    if ( SSIContestFileUploadValue.TYPE_PDF.equalsIgnoreCase( fileFormat ) )
    {
      data.setType( SSIContestFileUploadValue.TYPE_PDF );
    }
    else if ( SSIContestFileUploadValue.TYPE_DOC.equalsIgnoreCase( fileFormat ) )
    {
      data.setType( SSIContestFileUploadValue.TYPE_DOC );
    }
    else if ( SSIContestFileUploadValue.TYPE_DOCX.equalsIgnoreCase( fileFormat ) )
    {
      data.setType( SSIContestFileUploadValue.TYPE_DOCX );
    }
  }

  public ActionForward extractReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestGeneralInfoForm generalInfoForm = (SSIContestGeneralInfoForm)form;
    Long contestId = new Long( request.getSession().getAttribute( "ssiContestExportId" ).toString() );
    SSIContest ssiContest = getSSIContestService().getContestById( contestId );
    String filename = getExtractFileName( ssiContest.getContestNameFromCM() );
    Map<String, Object> extractParameters = new HashMap<String, Object>();
    extractParameters.put( "contestId", contestId );
    processStandardExtract( response, filename, extractParameters, ssiContest.getContestType().getCode() );

    return null;
  }

  public ActionForward extractErrorReport( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestGeneralInfoForm generalInfoForm = (SSIContestGeneralInfoForm)form;
    String filename = getExtractFileName( "SSIErrors" );
    Map<String, Object> extractParameters = new HashMap<String, Object>();
    extractParameters.put( "importFileId", generalInfoForm.getImportFileId() );
    processStandardExtract( response, filename, extractParameters, null );

    return null;
  }

  protected void processStandardExtract( HttpServletResponse response, String filename, Map<String, Object> extractParameters, String ssiType ) throws Exception
  {
    writeHeader( response, filename );
    String content = getExtractReportData( extractParameters, ssiType );
    writeContent( content, response );
  }

  protected String getExtractReportData( Map<String, Object> reportParameters, String ssiType )
  {
    StringBuffer contentBuf = new StringBuffer();
    Map reportExtractData = null;
    if ( ssiType == null )
    {
      // Errors Download
      reportExtractData = getSSIContestService().getContsetErrorExtract( reportParameters );
      buildCSVErrorExtractContent( contentBuf, reportExtractData );
    }
    else
    {
      // File Download
      reportExtractData = getSSIContestService().getContsetPaxManagerSVExtract( reportParameters, ssiType );
      buildCSVExtractContent( contentBuf, reportExtractData, ssiType );
    }

    return contentBuf.toString();
  }

  protected void buildCSVErrorExtractContent( StringBuffer contentBuf, Map output )
  {
    List results = (List)output.get( OUTPUT_RESULT_SET );

    if ( new BigDecimal( 0 ).equals( output.get( OUTPUT_RETURN_CODE ) ) && null != results && results.size() > 0 )
    {
      for ( int i = 0; i < results.size(); i++ )
      {
        contentBuf.append( results.get( i ) ).append( NEW_LINE );
      }
    }
  }

  protected void buildCSVExtractContent( StringBuffer contentBuf, Map output, String ssiType )
  {
    List<SSIContestParticipantValueBean> results = (List<SSIContestParticipantValueBean>)output.get( OUTPUT_RESULT_SET );
    if ( results != null && !results.isEmpty() )
    {
      if ( SSIContestType.OBJECTIVES.equals( ssiType ) )
      {
        for ( int i = 0; i < results.size(); i++ )
        {
          SSIContestParticipantValueBean valueBean = results.get( i );
          contentBuf.append( valueBean.getLoginId() ).append( "," );
          contentBuf.append( valueBean.getFirstName() ).append( "," );
          contentBuf.append( valueBean.getLastName() ).append( "," );
          contentBuf.append( valueBean.getRole() ).append( "," );
          contentBuf.append( valueBean.getObjectivePayoutDescription() ).append( "," );
          contentBuf.append( valueBean.getObjectiveAmount() ).append( "," );
          contentBuf.append( valueBean.getObjectivePayout() ).append( "," );
          contentBuf.append( valueBean.getOtherPayoutDescription() ).append( "," );
          contentBuf.append( valueBean.getOtherValue() ).append( "," );
          contentBuf.append( valueBean.getBonusForEvery() ).append( "," );
          contentBuf.append( valueBean.getBonusPayout() ).append( "," );
          contentBuf.append( valueBean.getBonusPayoutCap() ).append( "," );
          contentBuf.append( NEW_LINE );
        }
      }
      else if ( SSIContestType.AWARD_THEM_NOW.equals( ssiType ) )
      {
        for ( int i = 0; i < results.size(); i++ )
        {
          SSIContestParticipantValueBean valueBean = results.get( i );
          contentBuf.append( valueBean.getLoginId() ).append( "," );
          contentBuf.append( valueBean.getFirstName() ).append( "," );
          contentBuf.append( valueBean.getLastName() ).append( "," );
          contentBuf.append( valueBean.getRole() ).append( "," );
          contentBuf.append( valueBean.getActivityDescription() ).append( "," );
          contentBuf.append( valueBean.getActivityAmount() ).append( "," );
          contentBuf.append( valueBean.getPayoutPoints() ).append( "," );
          contentBuf.append( valueBean.getPayoutDescription() ).append( "," );
          contentBuf.append( valueBean.getValue() ).append( "," );
          contentBuf.append( NEW_LINE );
        }
      }
      else if ( SSIContestType.DO_THIS_GET_THAT.equals( ssiType ) || SSIContestType.STACK_RANK.equals( ssiType ) )
      {
        for ( int i = 0; i < results.size(); i++ )
        {
          SSIContestParticipantValueBean valueBean = results.get( i );
          contentBuf.append( valueBean.getLoginId() ).append( "," );
          contentBuf.append( valueBean.getFirstName() ).append( "," );
          contentBuf.append( valueBean.getLastName() ).append( "," );
          contentBuf.append( valueBean.getRole() ).append( "," );
          contentBuf.append( NEW_LINE );
        }
      }
      else if ( SSIContestType.STEP_IT_UP.equals( ssiType ) )
      {
        for ( int i = 0; i < results.size(); i++ )
        {
          SSIContestParticipantValueBean valueBean = results.get( i );
          contentBuf.append( valueBean.getLoginId() ).append( "," );
          contentBuf.append( valueBean.getFirstName() ).append( "," );
          contentBuf.append( valueBean.getLastName() ).append( "," );
          contentBuf.append( valueBean.getRole() ).append( "," );
          contentBuf.append( valueBean.getBaselineAmount() ).append( "," );
          contentBuf.append( NEW_LINE );
        }
      }
    }
  }

  public void writeContent( String content, HttpServletResponse response ) throws Exception
  {
    response.getOutputStream().write( new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF } );
    response.getOutputStream().write( content.getBytes( Charset.forName( "UTF-8" ) ) );
  }

  public void writeHeader( HttpServletResponse response, String fileName )
  {
    response.setContentType( "text/csv; charset=UTF-8" );
    response.setCharacterEncoding( "UTF-8" );
    response.setHeader( "Pragma", "public" );
    response.setHeader( "Cache-Control", "max-age=0" );
    response.setHeader( "Content-disposition", "attachment; filename=\"" + fileName + "\"" );
    try
    {
      response.flushBuffer();
    }
    catch( Exception ex )
    {
      // Log Errors
    }
  }

  protected String getExtractFileName( String contestName )
  {
    return contestName + DateUtils.toDisplayString( DateUtils.getCurrentDate() ) + ".csv";
  }

  public Long getContestId( HttpServletRequest request ) throws InvalidClientStateException
  {
    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
    return (Long)clientStateMap.get( "contestId" );
  }

  private String getFileExtension( String filename )
  {
    return FilenameUtils.getExtension( filename );
  }

  private SSIContestFileUploadService getSSIContestFileUploadService()
  {
    return (SSIContestFileUploadService)getService( SSIContestFileUploadService.BEAN_NAME );
  }

  private MailingService getMailingService()
  {
    return (MailingService)getService( MailingService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

}
