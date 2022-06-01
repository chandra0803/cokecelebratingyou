
package com.biperf.core.ui.mobilerecogapp;

import java.io.Serializable;
import java.net.URL;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.PurlContributorState;
import com.biperf.core.domain.enums.PurlContributorVideoType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlContributorComment;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.mtc.MTCService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.purl.PurlContributorCommentValidationException;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.serviceanniversary.ServiceAnniversaryService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.mobilerecogapp.view.ContributionsTAndCView;
import com.biperf.core.ui.mobilerecogapp.view.ContributorView;
import com.biperf.core.ui.recognition.EcardUploadBean;
import com.biperf.core.ui.recognition.EcardUploadForm;
import com.biperf.core.ui.recognition.purl.ContributorMediaUploadAction;
import com.biperf.core.ui.utils.JsonResponse;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.mtc.v1.upload.UploadResponse;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

public class ContributionsListAction extends ContributorMediaUploadAction
{

  private static final Log logger = LogFactory.getLog( ContributionsListAction.class );

  public ActionForward fetchContributionsList( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    Long loggedinUserId = UserManager.getUserId();

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "LoggedinUserId : " + loggedinUserId );
    }
    if ( loggedinUserId == null && ( request.getParameter( "activityId" ) == null || "".equals( request.getParameter( "activityId" ) ) ) )
    {
      WebErrorMessageList messages = new WebErrorMessageList();
      WebErrorMessage message = new WebErrorMessage();
      message.setCommand( WebResponseConstants.RESPONSE_COMMAND_REDIRECT );
      message.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.LOGIN_PAGE_URL );
      messages.getMessages().add( message );
      super.writeAsJsonToResponse( messages, response );
      return null;
    }
    int pageSize = 20;
    int startIndex = 1;
    String commentsOrderBy = "asc";
    Boolean isCommentOrderDescending = Boolean.TRUE;
    Long purlRecipientId = Long.valueOf( "0" );

    if ( (String)request.getParameter( "commentsOrderBy" ) != null )
    {
      commentsOrderBy = request.getParameter( "commentsOrderBy" );
      if ( "asc".equalsIgnoreCase( commentsOrderBy ) )
      {
        isCommentOrderDescending = Boolean.FALSE;
      }
    }

    if ( (String)request.getParameter( "pageSize" ) != null )
    {
      String PAGE_SIZE = request.getParameter( "pageSize" );
      if ( !StringUtil.isEmpty( PAGE_SIZE ) )
      {
        pageSize = Integer.parseInt( PAGE_SIZE );
      }
    }

    if ( (String)request.getParameter( "startIndex" ) != null )
    {
      String START_INDEX = request.getParameter( "startIndex" );
      if ( !StringUtil.isEmpty( START_INDEX ) )
      {
        startIndex = Integer.parseInt( START_INDEX );
      }
    }

    if ( (String)request.getParameter( "activityId" ) != null )
    {
      String ACTIVITY_ID = request.getParameter( "activityId" );
      if ( !StringUtil.isEmpty( ACTIVITY_ID ) )
      {
        purlRecipientId = Long.parseLong( ACTIVITY_ID );
      }
    }

    Participant participant = null;
    // Get the participant logged in
    ContributorView contributorView = new ContributorView();
    if ( null != loggedinUserId )
    {
      participant = getParticipantService().getParticipantById( loggedinUserId );
      if ( participant != null )
      {
        contributorView.setContributor( getPurlService().updateParticipant( participant ) );
      }
    }

    int rowNumStart = ( startIndex - 1 ) * pageSize;
    int rowNumEnd = rowNumStart + pageSize;

    List<PurlContributorComment> comments = getPurlService().getComments( purlRecipientId, isCommentOrderDescending, rowNumStart, rowNumEnd );
    contributorView.setContributions( getPurlService().contributorCommentsProcess( comments, getFinalImagePrefixUrl() ) );

    writeAsJsonToResponse( contributorView, response );

    // Need to set locale and primaryCountryCode for UserManager object.
    // These are
    // consumed by some service methods down the chain.
    Locale locale = UserManager.getLocale();
    return null;

  }

  public ActionForward termsAndConditions( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      fetchSAContributeUrl( mapping, actionForm, request, response );
    }
    else
    {
      fetchTermsAndConditions( mapping, actionForm, request, response );
    }
    return null;
  }

  private ActionForward fetchSAContributeUrl( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ContributionsTAndCView contributionsTAndCView = new ContributionsTAndCView();
    String url = "";
    String celebrationId = request.getParameter( "celebrationId" );

    if ( !StringUtil.isEmpty( celebrationId ) )
    {
      url = getServiceAnniversaryService().getContributePageUrl( celebrationId, null );
    }
    else
    {
      logger.error( "Celebration Id from input param : " + celebrationId );
    }

    contributionsTAndCView.setCelebrationLink( url );
    writeAsJsonToResponse( contributionsTAndCView, response );
    return null;
  }

  private ActionForward fetchTermsAndConditions( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    StringBuffer termsAndConditions = new StringBuffer();
    DateTimeFormatter formatter = null;
    if ( request.getSession().getAttribute( "loggedInUserDate" ) != null )
    {
      formatter = DateTimeFormatter.ofPattern( request.getSession().getAttribute( "loggedInUserDate" ).toString() );
    }
    else
    {
      formatter = DateTimeFormatter.ofPattern( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
    }
    String buffer = MessageFormat.format( StringEscapeUtils.escapeXml( ContentReaderManager.getText( "purl.terms.text", "TERMS_AND_COND_TEXT" ) ),
                                          new Object[] { getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal(),
                                                         LocalDateTime.now().toLocalDate().format( formatter ),
                                                         new Integer( LocalDateTime.now().getYear() ).toString() } );

    String[] parseBuffer = StringEscapeUtils.unescapeXml( buffer ).split( "<p>|</p>" );
    ContributionsTAndCView contributionsTAndCView = new ContributionsTAndCView();
    for ( int i = 0; i < parseBuffer.length; i++ )
    {
      termsAndConditions.append( parseBuffer[i] );
    }
    contributionsTAndCView.setTermsAndConditions( termsAndConditions );
    writeAsJsonToResponse( contributionsTAndCView, response );
    return null;
  }

  public ActionForward uploadMedia( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    Long loggedinUserId = UserManager.getUserId();
    EcardUploadForm form = (EcardUploadForm)actionForm;
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "LoggedinUserId : " + loggedinUserId );
    }
    if ( loggedinUserId == null )
    {
      WebErrorMessageList messages = new WebErrorMessageList();
      WebErrorMessage message = new WebErrorMessage();
      message.setCommand( WebResponseConstants.RESPONSE_COMMAND_REDIRECT );
      message.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.LOGIN_PAGE_URL );
      messages.getMessages().add( message );
      super.writeAsJsonToResponse( messages, response );
      return null;
    }

    EcardUploadBean status = super.uploadEcard( form.getUploadAnImage() );

    writeAsJsonToResponse( status, response );
    return null;
  }

  public ActionForward postComment( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    String message = null;
    Long purlRecipientId = Long.valueOf( "0" );
    String mediaUrl = null;
    String thumbnailUrl = null;

    Long loggedinUserId = UserManager.getUserId();
    JsonResult jsonResult = new JsonResult();
    if ( loggedinUserId == null && ( request.getParameter( "activityId" ) == null || "".equals( request.getParameter( "activityId" ) ) )
        && ( request.getParameter( "message" ) == null || "".equals( request.getParameter( "message" ) ) ) )
    {
      WebErrorMessageList messages = new WebErrorMessageList();
      WebErrorMessage messageError = new WebErrorMessage();
      messageError.setCommand( WebResponseConstants.RESPONSE_COMMAND_REDIRECT );
      messageError.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.LOGIN_PAGE_URL );
      messages.getMessages().add( messageError );
      super.writeAsJsonToResponse( messages, response );
      return null;
    }
    try
    {
      if ( (String)request.getParameter( "activityId" ) != null )
      {
        String ACTIVITY_ID = request.getParameter( "activityId" );
        if ( !StringUtil.isEmpty( ACTIVITY_ID ) )
        {
          purlRecipientId = Long.parseLong( ACTIVITY_ID );
        }
      }
      message = (String)request.getParameter( "message" );

      if ( (String)request.getParameter( "mediaUrl" ) != null )
      {
        mediaUrl = request.getParameter( "mediaUrl" );
      }
      if ( (String)request.getParameter( "thumbnailUrl" ) != null && !request.getParameter( "thumbnailUrl" ).equals( "null" ) )
      {
        thumbnailUrl = request.getParameter( "thumbnailUrl" );
      }
      PurlContributor managerContributor = getPurlService().getPurlContributorByUserId( loggedinUserId, purlRecipientId );
      if ( managerContributor == null )
      {
        managerContributor = new PurlContributor();
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
        User user = getUserService().getUserByIdWithAssociations( loggedinUserId, associationRequestCollection );

        managerContributor.setPurlRecipient( getPurlService().getPurlRecipientById( purlRecipientId ) );
        managerContributor.setUser( user );
        managerContributor.setFirstName( user.getFirstName() );
        managerContributor.setLastName( user.getLastName() );
        managerContributor.setEmailAddr( user.getPrimaryEmailAddress().getEmailAddr() );
        managerContributor.setAvatarUrl( getParticipantService().getParticipantById( user.getId() ).getAvatarOriginal() );
        managerContributor.setState( PurlContributorState.lookup( PurlContributorState.INVITATION ) );
        managerContributor = getPurlService().savePurlContributor( managerContributor );
      }
      PurlContributorComment commentObject = createPurlContributorComment( purlRecipientId, message, mediaUrl, thumbnailUrl );
      if ( commentObject.getVideoUrl() != null )
      {
        commentObject.setImageUrl( null );
        commentObject.setImageUrlThumb( null );
        UploadResponse uploadResponse = null;

        uploadResponse = getMTCService().uploadVideo( new URL( commentObject.getVideoUrl() ) );
        logger.error( "getRequestId" + uploadResponse.getRequestId() );
        commentObject.setVideoUrl( commentObject.getVideoUrl() + ActionConstants.REQUEST_ID + uploadResponse.getRequestId() );

      }
      PurlContributorComment purlComment = getPurlService().postCommentForContributor( managerContributor.getId(), commentObject );

      jsonResult.setStatus( JsonResponse.STATUS_SUCCESS );
      super.writeAsJsonToResponse( jsonResult, response );

    }
    catch( JSONException e )
    {
      jsonResult.setStatus( JsonResponse.STATUS_FAILED );
      super.writeAsJsonToResponse( jsonResult, response );
      logger.error( "Error during json comment population" );
    }
    catch( PurlContributorCommentValidationException e )
    {
      jsonResult.setStatus( JsonResponse.STATUS_FAILED );
      request.setAttribute( "errorText", CmsResourceBundle.getCmsBundle().getString( ServiceErrorMessageKeys.PURL_COMMENT_LENGTH_ERROR, getPurlService().getMaxCommentLength() + "" ) );
      super.writeAsJsonToResponse( jsonResult, response );
    }
    catch( ServiceErrorException e )
    {
      jsonResult.setStatus( JsonResponse.STATUS_FAILED );
      super.writeAsJsonToResponse( jsonResult, response );
      logger.error( "Error during conributor upload avatar" );
    }
    catch( Exception e )
    {
      jsonResult.setStatus( JsonResponse.STATUS_FAILED );
      super.writeAsJsonToResponse( jsonResult, response );
      logger.error( "Error during conributor upload avatar" );
    }

    return null;
  }

  private PurlContributorComment createPurlContributorComment( Long activityId, String comments, String mediaUrl, String thumbnailUrl ) throws JSONException
  {

    PurlContributor purlContributor = new PurlContributor();
    purlContributor.setId( activityId );

    if ( UserManager.isUserLoggedIn() )
    {
      Participant participant = getParticipantService().getParticipantById( UserManager.getUserId() );
      if ( purlContributor.getAvatarUrl() == null )
      {
        if ( participant.getAvatarSmall() != null )
        {
          purlContributor.setAvatarUrl( participant.getAvatarSmall() );
        }
      }
      else
      {
        purlContributor.setAvatarUrl( purlContributor.getAvatarUrl() );
      }
    }
    else
    {
      if ( purlContributor.getAvatarUrl() != null )
      {
        purlContributor.setAvatarUrl( purlContributor.getAvatarUrl() );
      }
    }
    PurlContributorComment purlComment = new PurlContributorComment();
    purlComment.setPurlContributor( purlContributor );
    purlComment.setComments( comments );
    if ( mediaUrl != null && StringUtil.isEmpty( thumbnailUrl ) && thumbnailUrl == null )
    {
      String[] ImageUrl = mediaUrl.split( "-cm/cm3dam/" );
      if ( ImageUrl != null && ImageUrl.length >= 2 )
      {
        purlComment.setImageUrl( ImageUrl[1] );
      }
      if ( ImageUrl.length >= 1 )
      {
        purlComment.setImageUrl( mediaUrl );
      }
    }

    // Thumbnail URL implies video upload is being posted.
    else if ( !StringUtil.isEmpty( thumbnailUrl ) && thumbnailUrl != null )
    {
      String videoExtension = FilenameUtils.getExtension( mediaUrl );
      purlComment.setVideoUrl( mediaUrl );
      purlComment.setVideoUrlThumb( thumbnailUrl );
      purlComment.setVideoUrlExtension( videoExtension );
      purlComment.setVideoType( PurlContributorVideoType.lookup( PurlContributorVideoType.DIRECT ) );
    }

    /*
     * purlComment.setImageUrl(photoJson.getString("src"));
     * purlComment.setImageUrlThumb(photoJson.getString("thumbSrc"));
     */
    return purlComment;
  }

  private static PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

  private static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private String getFinalImagePrefixUrl()
  {
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    return siteUrlPrefix + "-cm/cm3dam" + '/';
  }

  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  @SuppressWarnings( "serial" )
  private class JsonResult implements Serializable
  {
    String status;

    public String getStatus()
    {
      return status;
    }

    public void setStatus( String status )
    {
      this.status = status;
    }

  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private ServiceAnniversaryService getServiceAnniversaryService()
  {
    return (ServiceAnniversaryService)getService( ServiceAnniversaryService.BEAN_NAME );
  }

  public MTCService getMTCService()
  {
    return (MTCService)getService( MTCService.BEAN_NAME );
  }
}
