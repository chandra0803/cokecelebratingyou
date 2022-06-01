
package com.biperf.core.ui.ssi;

import static com.biperf.core.utils.SSIContestUtil.getClientState;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.ssi.SSIAdminContestActions;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.process.SSIContestProgressLoadNotificationProcess;
import com.biperf.core.process.SSIContestStackRankUpdateProcess;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.fileprocessing.GlobalFileProcessingService;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.ssi.SSIContestFileUploadService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.fileload.FileStageStrategyFactory;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.ssi.view.SSIContestResponseView;
import com.biperf.core.ui.ssi.view.SSIUpdateFinalResultsView;
import com.biperf.core.ui.ssi.view.SSIUpdateResultsDataView;
import com.biperf.core.ui.ssi.view.SSIUpdateResultsDataViewWrapper;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.AwsUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.fileprocessing.OperationResultInfo;
import com.biperf.core.value.ssi.SSIContestFileUploadValue;
import com.biperf.core.value.ssi.SSIContestParticipantProgressValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SSIContestUpdateResultsAction extends SSIContestBaseAction
{

  private static final String CONTEST_DETAIL_PAGE = "creatorContestList.do?method=display&id=";

  // maps FE sort params to db specific values
  private static final Map<String, String> contestResultsSortedOnMap = new HashMap<String, String>();
  private static final String dummyString = "dummy";

  static
  {
    contestResultsSortedOnMap.put( "lastName", "participantName" ); // default sort on option
    contestResultsSortedOnMap.put( "activity", "activityName" );
    contestResultsSortedOnMap.put( "activityAsOfDate", "activityAmount" );
  }

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return fetchContestResults( mapping, actionForm, request, response );
  }

  public ActionForward populateContestResults( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    // TODO; optimize; re-set the contest id in scope
    SSIContestUpdateResultsForm ssiContestUpdateResultsForm = (SSIContestUpdateResultsForm)form;
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestUpdateResultsForm.getId(), false );
    request.setAttribute( "id", getClientState( request, contestId, true ) );
    SSIContest contest = getSSIContestService().getContestById( contestId );
    String contestName = contest.getContestNameFromCM();
    String name = contestName.length() > 50 ? contestName.substring( 0, 50 ) : contestName;
    request.setAttribute( "name", name );
    SSIUpdateFinalResultsView finalResults = getFinalResultsView( contest );
    ssiContestUpdateResultsForm.setInitializationJson( toJson( finalResults ) );
    /* Bug# 61243, commenting out feature for large audience, moved to next phase */
    /*
     * if ( isLargeAudienceDownload() ) { request.setAttribute( "largeAudience", Boolean.TRUE ); }
     */
    request.setAttribute( "canShowOnlineForm", ! ( UserManager.getUser().isSSIAdmin() || UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_LOGIN_AS" ) ) ) );
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  private SSIUpdateFinalResultsView getFinalResultsView( SSIContest contest ) throws ServiceErrorException
  {
    SSIUpdateFinalResultsView finalResults = new SSIUpdateFinalResultsView();
    if ( !contest.getStatus().isFinalizeResults() )
    {
      finalResults.setDaysToEnd( DateUtils.getDaysToGoFromToday( contest.getEndDate() ) );
      if ( finalResults.getDaysToEnd() == 0 )
      {
        finalResults.setId( SSIContestUtil.getClientState( contest.getId() ) );
        if ( hasPayouts( contest ) )
        {
          finalResults.setHasApprovePayout( true );
        }
        else
        {
          finalResults.setAllowFinalizeResults( true );
        }
      }
    }
    return finalResults;
  }

  public ActionForward finalizeResults( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestUpdateResultsForm ssiContestUpdateResultsForm = (SSIContestUpdateResultsForm)form;
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestUpdateResultsForm.getId(), false );
    if ( ssiContestUpdateResultsForm.getFinalizeResults().equals( "yes" ) )
    {
      getSSIContestService().finalizeContest( contestId );
    }
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { "method=display", "id=" + SSIContestUtil.getClientState( contestId ) } );
  }

  public ActionForward fetchContestResults( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      SSIContestUpdateResultsForm ssiContestUpdateResultsForm = (SSIContestUpdateResultsForm)form;
      Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestUpdateResultsForm.getId(), false );

      Integer currentPage = ssiContestUpdateResultsForm.getPage() == 0 ? SSIContestUtil.FIRST_PAGE_NUMBER : ssiContestUpdateResultsForm.getPage();
      Integer perPage = ssiContestUpdateResultsForm.getPerPage() == 0 ? SSIContestUtil.CONTEST_DETAIL_PER_PAGE : ssiContestUpdateResultsForm.getPerPage();

      String sortedOn = contestResultsSortedOnMap.get( ssiContestUpdateResultsForm.getSortedOn() );
      if ( sortedOn == null )
      {
        sortedOn = contestResultsSortedOnMap.get( "lastName" ); // default sortedOn
        ssiContestUpdateResultsForm.setSortedOn( "lastName" );
      }
      if ( StringUtil.isNullOrEmpty( ssiContestUpdateResultsForm.getSortedBy() ) )
      {
        ssiContestUpdateResultsForm.setSortedBy( SSIContestUtil.DEFAULT_SORT_BY );
      }
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_ACTIVITIES ) );
      SSIContest contest = getSSIContestService().getContestByIdWithAssociations( contestId, associationRequestCollection );

      Map<String, Double> contestActivityTotals = getSSIContestService().getContestActivityTotals( contestId );

      Integer participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contestId );
      List<SSIContestParticipantProgressValueBean> paticipantProgressValueBeans = getSSIContestParticipantService()
          .getContestParticipantsProgresses( contestId, currentPage, perPage, sortedOn, ssiContestUpdateResultsForm.getSortedBy(), contest.getContestType().isDoThisGetThat() );
      SSIUpdateResultsDataView view = new SSIUpdateResultsDataView( contest,
                                                                    contestActivityTotals,
                                                                    paticipantProgressValueBeans,
                                                                    participantsCount,
                                                                    currentPage,
                                                                    perPage,
                                                                    ssiContestUpdateResultsForm.getSortedOn(),
                                                                    ssiContestUpdateResultsForm.getSortedBy() );
      super.writeAsJsonToResponse( new SSIUpdateResultsDataViewWrapper( view ), response );
      return null;
    }
    catch( Exception e )
    {
      return sendErrorResponse( response );
    }
  }

  private ActionForward sendSuccessResponse( Long contestId, HttpServletResponse response, HttpServletRequest request, String redirectUrl ) throws Exception
  {
    SSIUpdateResultsDataViewWrapper view = new SSIUpdateResultsDataViewWrapper();
    WebErrorMessage message = new WebErrorMessage();
    message.setSuccess( true );
    message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
    message.setCommand( WebResponseConstants.RESPONSE_COMMAND_REDIRECT );
    message.setUrl( redirectUrl + SSIContestUtil.getClientState( request, contestId, true ) );
    view.getMessages().add( message );
    writeAsJsonToResponse( view, response, ContentType.HTML );
    return null;
  }

  private ActionForward sendErrorResponse( HttpServletResponse response ) throws Exception
  {
    WebErrorMessage errorMessage = new WebErrorMessage();
    errorMessage.setSuccess( false );
    errorMessage.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
    errorMessage.setText( CmsResourceBundle.getCmsBundle().getString( "system.errors.USER_FRIENDLY_SYSTEM_ERROR_MESSAGE" ) );
    SSIUpdateResultsDataViewWrapper view = new SSIUpdateResultsDataViewWrapper();
    view.getMessages().add( errorMessage );
    writeAsJsonToResponse( view, response );
    return null;
  }

  private ActionForward sendErrorResponse( HttpServletResponse response, List<String> validationErrors ) throws Exception
  {
    SSIUpdateResultsDataViewWrapper view = new SSIUpdateResultsDataViewWrapper();

    for ( String error : validationErrors )
    {
      WebErrorMessage errorMessage = new WebErrorMessage();
      errorMessage.setSuccess( false );
      errorMessage.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
      errorMessage.setText( error );
      view.getMessages().add( errorMessage );
    }
    writeAsJsonToResponse( view, response );
    return null;
  }

  public ActionForward saveAndSort( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      if ( saveContestResults( form, request, response ) )
      {
        return fetchContestResults( mapping, form, request, response );
      }
      else
      {
        return null;
      }
    }
    catch( Exception e )
    {
      return sendErrorResponse( response );
    }
  }

  public ActionForward saveAndNavigate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      if ( saveContestResults( form, request, response ) )
      {
        return fetchContestResults( mapping, form, request, response );
      }
      else
      {
        return null;
      }
    }
    catch( Exception e )
    {
      return sendErrorResponse( response );
    }
  }

  public ActionForward sort( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      return fetchContestResults( mapping, form, request, response );
    }
    catch( Exception e )
    {
      return sendErrorResponse( response );
    }
  }

  public ActionForward navigate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      return fetchContestResults( mapping, form, request, response );
    }
    catch( Exception e )
    {
      return sendErrorResponse( response );
    }
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      if ( saveContestResults( form, request, response ) )
      {
        Long contestId = SSIContestUtil.getContestIdFromClientState( request, ( (SSIContestUpdateResultsForm)form ).getId(), false );
        return sendSuccessResponse( contestId, response, request, CONTEST_DETAIL_PAGE );
      }
      else
      {
        return null;
      }
    }
    catch( Exception e )
    {
      return sendErrorResponse( response );
    }

  }

  public ActionForward cancelUpdate( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      SSIContestUpdateResultsForm ssiContestUpdateResultsForm = (SSIContestUpdateResultsForm)form;
      Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestUpdateResultsForm.getId(), false );
      if ( SSIContestUtil.SELECTION_YES.equals( ssiContestUpdateResultsForm.getCancelAndSendProgress() ) )
      {
        launchProgressNotificationProcess( contestId );
        return sendSuccessResponse( contestId, response, request, CONTEST_DETAIL_PAGE );
      }
      else
      {
        return sendSuccessResponse( contestId, response, request, CONTEST_DETAIL_PAGE );
      }
    }
    catch( Exception e )
    {
      return sendErrorResponse( response );
    }
  }

  private boolean saveContestResults( ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestUpdateResultsForm ssiContestUpdateResultsForm = (SSIContestUpdateResultsForm)form;
    List<String> validationErrors = ssiContestUpdateResultsForm.validate();
    if ( validationErrors.size() == 0 )
    {
      Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestUpdateResultsForm.getId(), false );
      getSSIContestParticipantService().saveContestParticipantProgress( contestId,
                                                                        DateUtils.toDate( ssiContestUpdateResultsForm.getSsiEnterActivityDate() ),
                                                                        ssiContestUpdateResultsForm.getParticipantAsList() );
      // launch process to update stackRank
      launchStackRankUpdateProcess( contestId );

      if ( SSIContestUtil.SELECTION_YES.equals( ssiContestUpdateResultsForm.getSaveAndSendProgressUpdate() ) )
      {
        launchProgressNotificationProcess( contestId );
      }

      return true;
    }
    else
    {
      sendErrorResponse( response, validationErrors );
      return false;
    }
  }

  /**
   * Launch Stack Rank Update Process on update of pax progress via screen( individual one or via spreadsheet )
   */
  private void launchStackRankUpdateProcess( Long contestId ) throws ServiceErrorException
  {
    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "contestId", new String[] { contestId.toString() } );

    Process process = getProcessService().createOrLoadSystemProcess( SSIContestStackRankUpdateProcess.PROCESS_NAME, SSIContestStackRankUpdateProcess.BEAN_NAME );
    getProcessService().launchProcess( process, parameterValueMap, UserManager.getUserId() );
  }

  public ActionForward uploadContestResults( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      SSIContestUploadResultsForm uploadResultsForm = (SSIContestUploadResultsForm)form;
      List<String> validationErrors = uploadResultsForm.validate();
      if ( validationErrors.size() == 0 )
      {
        FormFile formFile = uploadResultsForm.getSsiHiddenUpload();
        Long contestId = SSIContestUtil.getContestIdFromClientState( request, request.getParameter( "id" ), false );

        String orginalfilename = formFile.getFileName();
        String fileFormat = FilenameUtils.getExtension( orginalfilename );
        String extension = "." + fileFormat;
        String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
        if ( filename != null )
        {
          filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );
        }
        filename = filename + extension;
        int filesize = formFile.getFileSize();
        byte[] imageInByte = formFile.getFileData();

        SSIContestFileUploadValue data = new SSIContestFileUploadValue();
        data.setData( imageInByte );
        data.setName( filename );
        data.setAdcFileName( ImageUtils.getSSIContestProgressADCFilePath( contestId, extension ) );
        data.setSize( filesize );
        data.setType( fileFormat );
        data.setId( contestId );

        String environment = Environment.getEnvironment();
        if ( !Environment.ENV_DEV.equals( environment ) )
        {
          OperationResultInfo result = null;
          if ( AwsUtils.isAws() )
          {
            InputStream input = new ByteArrayInputStream( data.getData() );
            result = getFileProcessingService().process( ImportFileTypeType.SSI_PROGRESS_DATA_LOAD, data.getName(), input, String.valueOf( UserManager.getUserId() ) );
          }
          else
          {
            getSSIContestFileUploadService().moveFileToWevdavADC( data );
          }

          // turn on the upload in progress flag and add the saveAndSendProgressUpdate column value
          getSSIContestService().setUploadInProgress( contestId, true, uploadResultsForm.getSaveAndSendProgressUpdate() );

          if ( AwsUtils.isAws() )
          {
            String fileName = getValidFileOutURL( result.getOutputFileName() );
            BigDecimal returnCode = new BigDecimal( "999" );
            try
            {
              if ( result.getRowCountBad() == 0 )
              {
                Map outParams = getFileStageStrategyFactory().getStrategy( ImportFileTypeType.SSI_PROGRESS_DATA_LOAD ).stage( fileName );
                returnCode = new BigDecimal( outParams.get( "p_out_returncode" ).toString() );
                if ( returnCode.compareTo( new BigDecimal( "0" ) ) == 0 )
                {
                  log.debug( "File Stagged Successfully!" );
                }
                else
                {
                  log.error( "File " + fileName + " Stagged Failed!" );
                }
              }
            }
            catch( Throwable t )
            {
              log.error( "File " + fileName + " Stagged Failed!" + t );
            }
          }

        }
        request.getSession().setAttribute( SSIContestUtil.SHOW_MODAL, Boolean.TRUE );
        request.getSession().setAttribute( SSIContestUtil.MODAL_MESSAGE, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.UPDATE_CALC_IN_PROGRESS" ) );
        sendSuccessResponse( contestId, response, request, CONTEST_DETAIL_PAGE );
        // Track SSI Admin Action
        if ( UserManager.getUser().isSSIAdmin() || UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_LOGIN_AS" ) ) )
        {
          getSSIContestService().saveAdminAction( contestId,
                                                  UserManager.getUser().getOriginalAuthenticatedUser().getUserId(),
                                                  SSIAdminContestActions.UPLOAD_CONTEST_PROGRESS_SPREADSHEET,
                                                  "Progress Spreadsheet Loaded for Contest ID: " + contestId + " by Admin ID: " + UserManager.getUser().getOriginalAuthenticatedUser().getUserId() );
        }
        return null;
      }
      else
      {
        return sendErrorResponse( response, validationErrors );
      }
    }
    catch( Exception e )
    {
      log.error( "Error occured moving the file to ADC WebDAV: ", e );
      e.printStackTrace();
      return sendErrorResponse( response );
    }

  }

  public ActionForward uploadContestDetailsValid( ActionMapping mapping, ActionForm form, HttpServletRequest aRequest, HttpServletResponse aResponse ) throws Exception
  {
    SSIContestResponseView responseView = null;
    SSIContestUploadResultsForm t_fileForm = (SSIContestUploadResultsForm)form;
    List<String> validationErrors = t_fileForm.validate();
    if ( validationErrors.size() == 0 )
    {
      FormFile myFile = t_fileForm.getSsiHiddenUpload();

      Long contestId = new Long( aRequest.getSession().getAttribute( "ssiContestExportId" ).toString() );
      SSIContest ssiContest = getSSIContestService().getContestById( contestId );
      String filetype = null;
      if ( SSIContestType.OBJECTIVES.equals( ssiContest.getContestType().getCode() ) )
      {
        filetype = "ssicontestobjimport";
      }
      else if ( SSIContestType.AWARD_THEM_NOW.equals( ssiContest.getContestType().getCode() ) )
      {
        filetype = "ssicontestatnimport";
      }
      else if ( SSIContestType.DO_THIS_GET_THAT.equals( ssiContest.getContestType().getCode() ) )
      {
        filetype = "ssicontestdtgtimport";
      }
      else if ( SSIContestType.STACK_RANK.equals( ssiContest.getContestType().getCode() ) )
      {
        filetype = "ssicontestsrimport";
      }
      else if ( SSIContestType.STEP_IT_UP.equals( ssiContest.getContestType().getCode() ) )
      {
        filetype = "ssicontestsiuimport";
      }

      String fileName = myFile.getFileName();
      String userId = UserManager.getUserId().toString();
      InputStream inputFile = myFile.getInputStream();

      OperationResultInfo result = null;
      if ( Environment.isCtech() )
      {
        result = getFileProcessingService().process( filetype, fileName, inputFile, userId );
      }

      String ifileName = null;
      String outputFileName = null;
      if ( t_fileForm != null )
      {
        outputFileName = result.getOutputFileName();
        ifileName = getValidFileOutURL( result.getOutputFileName() ) + "@" + contestId;
      }

      PrintWriter pw = aResponse.getWriter();
      BigDecimal returnCode = new BigDecimal( "999" );
      BigDecimal errorCount = new BigDecimal( 0 );
      BigDecimal fileId = new BigDecimal( 0 );
      try
      {
        if ( result.getRowCountBad() == 0 )
        {
          Map outParams = getFileStageStrategyFactory().getStrategy( filetype ).stage( ifileName );
          returnCode = new BigDecimal( outParams.get( "p_out_returncode" ).toString() );
          if ( outParams.get( "p_total_error_rec" ).toString() != null )
          {
            errorCount = new BigDecimal( outParams.get( "p_total_error_rec" ).toString() );
          }
          if ( returnCode.compareTo( new BigDecimal( "0" ) ) == 0 )
          {
            if ( errorCount.compareTo( new BigDecimal( "0" ) ) > 0 )
            {
              fileId = new BigDecimal( outParams.get( "p_out_import_file_id" ).toString() );
              SSIUpdateResultsDataView view = new SSIUpdateResultsDataView( "createGeneralInfo.do?method=extractErrorReport&importFileId=" + fileId, errorCount.toString() );

              WebErrorMessage message = new WebErrorMessage( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR, false, dummyString );
              List<WebErrorMessage> webErrorMessage = new ArrayList<WebErrorMessage>();
              webErrorMessage.add( message );
              super.writeAsJsonToResponse( new SSIUpdateResultsDataViewWrapper( view, webErrorMessage ), aResponse );
            }
            else
            {

              SSIUpdateResultsDataView view = new SSIUpdateResultsDataView( "", "" );

              WebErrorMessage message = new WebErrorMessage( WebResponseConstants.RESPONSE_TYPE_SUCCESS, true, dummyString );
              List<WebErrorMessage> webSuccessMessage = new ArrayList<WebErrorMessage>();
              webSuccessMessage.add( message );
              super.writeAsJsonToResponse( new SSIUpdateResultsDataViewWrapper( view, webSuccessMessage ), aResponse );
            }
          }
          else
          {

            SSIUpdateResultsDataView view = new SSIUpdateResultsDataView( "", "" );

            WebErrorMessage message = new WebErrorMessage( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR,
                                                           false,
                                                           CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.FILE_UPLOAD_FAILED" ) );
            List<WebErrorMessage> webErrorMessage = new ArrayList<WebErrorMessage>();
            webErrorMessage.add( message );
            super.writeAsJsonToResponse( new SSIUpdateResultsDataViewWrapper( view, webErrorMessage ), aResponse );
          }
        }
        else
        {

          SSIUpdateResultsDataView view = new SSIUpdateResultsDataView( "", "" );

          WebErrorMessage message = new WebErrorMessage( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR,
                                                         false,
                                                         CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.FILE_UPLOAD_FAILED" ) );
          List<WebErrorMessage> webErrorMessage = new ArrayList<WebErrorMessage>();
          webErrorMessage.add( message );
          super.writeAsJsonToResponse( new SSIUpdateResultsDataViewWrapper( view, webErrorMessage ), aResponse );
        }

      }
      catch( Throwable t )
      {

      }
    }
    return null;
  }

  private String getStackTraceAsString( Throwable e )
  {
    StringWriter stackTrace = new StringWriter();
    e.printStackTrace( new PrintWriter( stackTrace ) );
    return stackTrace.toString();
  }

  private GlobalFileProcessingService getFileProcessingService()
  {
    return (GlobalFileProcessingService)getService( GlobalFileProcessingService.BEAN_NAME );
  }

  private FileStageStrategyFactory getFileStageStrategyFactory()
  {
    return (FileStageStrategyFactory)BeanLocator.getBean( FileStageStrategyFactory.BEAN_NAME );
  }

  private String getValidFileOutURL( String outputFile )
  {
    StringBuilder builder = new StringBuilder();
    builder.append( getWorkWipPath() );
    builder.append( "/" );
    builder.append( getPrefix() );
    builder.append( "/" );
    builder.append( getSubFolderName() );
    if ( !AwsUtils.isAws() )
    {
      builder.append( "/valid" );
    }
    builder.append( "/" );
    builder.append( outputFile );
    return builder.toString();
  }

  private String getWorkWipPath()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.GLOBAL_FILE_PROCESSING_WORKWIP ).getStringVal();
  }

  private String getPrefix()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.GLOBAL_FILE_PROCESSING_PREFIX ).getStringVal();
  }

  private String getSubFolderName()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.GLOBAL_FILE_PROCESSING_SUBFOLDER ).getStringVal();
  }

  private void launchProgressNotificationProcess( Long contestId )
  {
    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "contestId", new String[] { contestId.toString() } );
    parameterValueMap.put( "isSSIAdmin", new String[] { String.valueOf( UserManager.getUser().isSSIAdmin() ) } );
    Process process = getProcessService().createOrLoadSystemProcess( SSIContestProgressLoadNotificationProcess.PROCESS_NAME, SSIContestProgressLoadNotificationProcess.BEAN_NAME );
    getProcessService().launchProcess( process, parameterValueMap, UserManager.getUserId() );
  }

  private SSIContestFileUploadService getSSIContestFileUploadService()
  {
    return (SSIContestFileUploadService)getService( SSIContestFileUploadService.BEAN_NAME );
  }

}
