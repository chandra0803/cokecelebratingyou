
package com.biperf.core.ui.diycommunication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.diycommunication.AudienceResults;
import com.biperf.core.value.diycommunication.CommunicationsManageNewsTable;
import com.biperf.core.value.diycommunication.CommunicationsNewsData;
import com.biperf.core.value.diycommunication.MessageUploadBean;
import com.biperf.core.value.diycommunication.ResourceList;
import com.biperf.core.value.diycommunication.SelectAudienceParticipantData;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;

public class DIYNewsAction extends BaseDIYCommunicationsAction
{
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( mapping.getParameter() != null )
    {
      return super.execute( mapping, form, request, response );
    }
    else
    {
      return this.display( mapping, form, request, response );
    }
  }

  // populates the news list as a table on the news maintenance page.
  public ActionForward populateNewsMaintenanceTable( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String statusType = "";

    // removing the newsId from session, as that may be carried along when a back icon is clicked
    // on the edit page to list page.
    request.getSession().removeAttribute( "newsId" );
    request.getSession().removeAttribute( "audienceResultsList" );

    if ( request.getParameter( "statusType" ) != null )
    {
      statusType = RequestUtils.getRequiredParamString( request, "statusType" );
    }
    CommunicationsManageNewsTable newsList = new CommunicationsManageNewsTable();

    List<DIYCommunications> communicationList = new ArrayList<DIYCommunications>();

    if ( !statusType.isEmpty() && statusType.equals( "archived" ) )
    {
      communicationList = getParticipantDIYCommunicationsService().getArchievedByCommunicationType( UserManager.getUserId(), DIYCommunications.COMMUNICATION_TYPE_NEWS );
    }
    else
    {
      communicationList = getParticipantDIYCommunicationsService().getActiveByCommunicationType( UserManager.getUserId(), DIYCommunications.COMMUNICATION_TYPE_NEWS );
    }

    if ( communicationList != null && !communicationList.isEmpty() )
    {
      newsList = new CommunicationsManageNewsTable( request, communicationList, statusType.isEmpty() || statusType.equals( "active" ) ? true : false );
    }
    super.writeAsJsonToResponse( newsList, response );
    return null;
  }

  // to display the news create/edit page
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    String forward = ActionConstants.SUCCESS_FORWARD;
    Long value = buildCommunicationsTypeId( request, "newsId" );
    request.setAttribute( "newsId", value );
    if ( value != null )
    {
      DIYCommunications communications = getCommunicationById( new Long( value ) );

      if ( DateUtils.toStartDate( communications.getStartDate() ).before( DateUtils.getCurrentDateTrimmed() ) )
      {
        request.getSession().setAttribute( "disableDate", Boolean.TRUE );
      }
    }

    return mapping.findForward( forward );
  }

  // returns the create or edit JSON for a news.
  public ActionForward populateNewsContent( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {

    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }

    List<ResourceList> newsContents = new ArrayList<ResourceList>();
    CommunicationsNewsData news = new CommunicationsNewsData( siteUrlPrefix, newsContents );
    DIYCommunications diyCommunication = null;
    request.getSession().removeAttribute( "disableDate" );

    String newsId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "newsId" );

    // fetching the form from session because when an ajax is called in a jsp with form errors, the
    // form is
    // not in reach for this ajax call, so form is set in the controller when there are serve errors
    // and fetched here.
    DIYCommunicationsNewsForm formData = (DIYCommunicationsNewsForm)request.getSession().getAttribute( DIYCommunications.SESSION_NEWS_FORM );

    if ( formData != null && formData.getNewsStartDate() != null )
    {
      diyCommunication = new DIYCommunications();
      diyCommunication.setStartDate( DateUtils.toDate( formData.getNewsStartDate() ) );
      diyCommunication.setEndDate( DateUtils.toDate( formData.getNewsEndDate() ) );
      diyCommunication.setId( request.getSession().getAttribute( "newsId" ) != null ? (Long)request.getSession().getAttribute( "newsId" ) : null );

      if ( formData.getDefaultLanguage() == null )
      {
        String lang = request.getSession().getAttribute( "userLang" ) != null ? (String)request.getSession().getAttribute( "userLang" ) : null;
        formData.setDefaultLanguage( lang );
      }

      newsContents = prepareNewsContents( formData.getNewsContentList(), formData.getDefaultLanguage() );
      news = new CommunicationsNewsData( diyCommunication, newsContents, siteUrlPrefix );

      request.getSession().setAttribute( "communicationsAudienceList", formData.getAudienceSelGroupList() );
      request.getSession().removeAttribute( DIYCommunications.SESSION_NEWS_FORM );
    }
    else if ( !StringUtils.isEmpty( newsId ) )
    {
      request.getSession().removeAttribute( "newsId" );

      diyCommunication = getParticipantDIYCommunicationsService().getDIYCommunicationsById( new Long( newsId ) );

      if ( DateUtils.toEndDate( diyCommunication.getEndDate() ).before( DateUtils.getCurrentDateTrimmed() ) )
      {
        diyCommunication.setStartDate( null );
        diyCommunication.setEndDate( null );
      }

      newsContents = getContentFromCM( diyCommunication.getCmAssetCode(), newsId, form );

      news = new CommunicationsNewsData( diyCommunication, newsContents, siteUrlPrefix );

      DIYCommunicationsNewsForm updatedForm = (DIYCommunicationsNewsForm)form;
      request.getSession().setAttribute( "newsId", new Long( newsId ) );
      request.getSession().setAttribute( "userLang", updatedForm.getDefaultLanguage() );
    }

    super.writeAsJsonToResponse( news, response );
    return null;

  }

  // returns the audienceList JSON.
  public ActionForward populatePublicAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SelectAudienceParticipantData audienceList = generateAudience( "newsId", request );
    super.writeAsJsonToResponse( audienceList, response );
    return null;
  }

  // returns the uploadStories JSON.
  public ActionForward uploadStoryImage( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    DIYCommunicationsNewsForm form = (DIYCommunicationsNewsForm)actionForm;

    String sizes = form.getSizes();
    List<String> sizesList = new ArrayList<String>();
    if ( sizes != null )
    {
      sizesList = Arrays.asList( sizes.split( "," ) );
    }

    FormFile uploadAnImage = null;

    if ( form.getImage() != null )
    {
      uploadAnImage = form.getImage();
    }
    else
    {
      uploadAnImage = form.getReplacementImage();
    }

    // this action should only allow for image uploads...
    if ( uploadAnImage != null && !ImageUtils.isImageTypeValid( getFileExtension( uploadAnImage.getFileName() ) ) )
    {
      MessageUploadBean message = createWebErrorMessage( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.INVALID_EXT" ), "error" );
      message.setName( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.IMAGE_UPLOAD_FAILURE" ) );
      writeUploadJsonToResponse( new DIYOwnImageUploadBean( message ), response );
      return null;
    }

    // go ahead and attempt the upload!
    DIYOwnImageUploadBean status = super.uploadImage( uploadAnImage, sizesList, DIYCommunications.COMMUNICATION_TYPE_NEWS );
    writeUploadJsonToResponse( status, response );
    return null;
  }

  // Saves the News Content to CM and then saves the News object.
  public ActionForward saveNews( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ActionForward forward = new ActionForward( mapping.findForward( ActionConstants.SUCCESS_FORWARD ) );
    ActionMessages errors = new ActionMessages();
    Locale systemDefaultLocale = getCMSDefaultLocale();

    // save the news assests in cm with the news upload image saved in cmdam3.
    DIYCommunicationsNewsForm diyNewsForm = (DIYCommunicationsNewsForm)form;

    Long communicationId = null;
    List<String> audienceNames = new ArrayList<String>();

    if ( request.getSession() != null )
    {
      communicationId = request.getSession().getAttribute( "newsId" ) != null ? (Long)request.getSession().getAttribute( "newsId" ) : null;
    }

    DIYCommunications diyCommunication = communicationId != null ? getParticipantDIYCommunicationsService().getDIYCommunicationsById( communicationId ) : null;

    if ( diyCommunication == null )
    {
      diyCommunication = new DIYCommunications();
      User managerUser = getUserService().getUserById( UserManager.getUserId() );
      diyCommunication.setManagerUser( managerUser );
    }

    diyNewsForm.toDomain( diyCommunication, diyNewsForm.getDefaultLanguage() );

    for ( Iterator<AudienceResults> audienceIter = diyNewsForm.getAudienceSelGroupList().iterator(); audienceIter.hasNext(); )
    {
      AudienceResults results = audienceIter.next();

      audienceNames.add( getAudienceService().getAudienceNameById( new Long( results.getId() ) ) );
    }
    if ( communicationId != null )
    {
      getCMAssetService().expireContents( diyCommunication.getCmAssetCode(), diyNewsForm.getNewsContentList(), systemDefaultLocale.toString() );
    }

    List<ResourceList> newsContentList = diyNewsForm.getNewsContentList();

    if ( diyNewsForm.getDefaultLanguage() == null )
    {
      String lang = request.getSession().getAttribute( "userLang" ) != null ? (String)request.getSession().getAttribute( "userLang" ) : null;
      diyNewsForm.setDefaultLanguage( lang );
      request.getSession().removeAttribute( "userLang" );
    }

    // boolean insertDefaultLocale = insertDefaultLocaleContent( newsContentList );
    /*
     * boolean updateDefaultLocale = false; if ( diyNewsForm.getDefaultLanguage() != null ) {
     * updateDefaultLocale = ! ( diyNewsForm.getDefaultLanguage().equals(
     * systemDefaultLocale.toString() ) ); if ( updateDefaultLocale ) {
     * removeExistingDefaultContent( newsContentList ); } }
     */

    for ( Iterator<ResourceList> newsContentIter = newsContentList.iterator(); newsContentIter.hasNext(); )
    {
      ResourceList newsContent = newsContentIter.next();

      Map<String, Object> deafultLangMap = new HashMap<String, Object>();
      deafultLangMap.put( "sourceLocale", diyNewsForm.getDefaultLanguage() );
      deafultLangMap.put( "locale", CmsUtil.getLocale( newsContent.getLanguage() ) );

      diyCommunication = getParticipantDIYCommunicationsService().saveNewsContents( diyCommunication, newsContent, audienceNames, deafultLangMap );

      // if ( ( insertDefaultLocale || updateDefaultLocale ) && newsContent.getLanguage().equals(
      // diyNewsForm.getDefaultLanguage() ) )

      /*
       * if ( ( updateDefaultLocale ) && newsContent.getLanguage().equals(
       * diyNewsForm.getDefaultLanguage() ) ) { deafultLangMap.put( "locale", systemDefaultLocale );
       * diyCommunication = getParticipantDIYCommunicationsService().saveNewsContents(
       * diyCommunication, newsContent, audienceNames, deafultLangMap ); }
       */
    }

    try
    {
      getParticipantDIYCommunicationsService().saveDIYCommunications( diyCommunication );
    }
    catch( UniqueConstraintViolationException ex )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "diyCommunications.errors.TITLE_EXISTS" ) );
      request.setAttribute( "serverReturnedErrorForNews", Boolean.TRUE );
    }
    catch( ServiceErrorException e )
    {
      log.debug( e );
      List serviceErrors = e.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
      saveErrors( request, errors );
      forward = new ActionForward( mapping.findForward( ActionConstants.FAIL_FORWARD ) );
      return forward;
    }

    if ( errors.size() > 0 )
    {
      saveErrors( request, errors );
      forward = new ActionForward( mapping.findForward( ActionConstants.FAIL_FORWARD ) );
    }

    request.setAttribute( "displayPopup", true );

    return forward;

  }

  @SuppressWarnings( "rawtypes" )
  private List<ResourceList> getContentFromCM( String code, String commNewsId, ActionForm form )
  {
    List<ResourceList> returnContent = new ArrayList<ResourceList>();
    List contents = getCMAssetService().getContentsForAsset( code );

    int index = 0;
    Long id = new Long( 1 );

    for ( Iterator contentIter = contents.iterator(); contentIter.hasNext(); )
    {
      Content content = (Content)contentIter.next();
      if ( content.getContentStatus().getName().equals( "Live" ) )
      {
        Map m = content.getContentDataMapList();
        String image = "";
        String imageMax = "";
        String imageMobile = "";

        String headline = "";
        String storyContent = "";
        String referenceImageKey = "";
        String defaultLanguage = "";

        Translations imageObject = (Translations)m.get( DIYCommunications.DIY_NEWS_CMASSET_IMAGESIZE );
        Translations imageMaxObject = (Translations)m.get( DIYCommunications.DIY_NEWS_CMASSET_IMAGESIZE_MAX );
        Translations imageMobileObject = (Translations)m.get( DIYCommunications.DIY_NEWS_CMASSET_IMAGESIZE_MOBILE );

        Translations headlineObject = (Translations)m.get( DIYCommunications.DIY_NEWS_CMASSET_HEADLINE );
        Translations storyContentObject = (Translations)m.get( DIYCommunications.DIY_NEWS_CMASSET_STORY_CONTENT );
        Translations referenceImage = (Translations)m.get( DIYCommunications.DIY_NEWS_CMASSET_REFERENCE_CONTENTKEY );
        Translations defaultLanguageObject = (Translations)m.get( DIYCommunications.SOURCE_LOCALE );

        if ( imageObject != null )
        {
          image = imageObject.getValue();
        }
        if ( imageMaxObject != null )
        {
          imageMax = imageMaxObject.getValue();
        }
        if ( imageMobileObject != null )
        {
          imageMobile = imageMobileObject.getValue();
        }
        if ( headlineObject != null )
        {
          headline = headlineObject.getValue();
        }
        if ( storyContentObject != null )
        {
          storyContent = storyContentObject.getValue();
        }
        if ( referenceImage != null )
        {
          referenceImageKey = referenceImage.getValue();
        }
        if ( defaultLanguageObject != null )
        {
          defaultLanguage = defaultLanguageObject.getValue();
        }

        ResourceList newsContent = new ResourceList();
        newsContent.setImageSize( image );
        newsContent.setImageSize_max( imageMax );
        newsContent.setImageSize_mobile( imageMobile );

        newsContent.setHeadline( headline );
        newsContent.setStory( storyContent );
        newsContent.setIndex( index++ );
        newsContent.setImageId( referenceImageKey );
        newsContent.setId( id.toString() );
        newsContent.setLanguage( content.getLocale().toString() );
        LanguageType languageItem = LanguageType.lookup( content.getLocale().toString() );
        newsContent.setLanguageDisplay( languageItem.getName() );
        newsContent.setIsDefaultLang( defaultLanguage.toString().equalsIgnoreCase( content.getLocale().toString() ) );
        newsContent.setIsSystemLanguage( defaultLanguage.toString().equalsIgnoreCase( getCMSDefaultLocale().toString() ) );
        newsContent.setSystemLocale( content.getLocale().toString().equalsIgnoreCase( getCMSDefaultLocale().toString() ) );

        id++;
        if ( newsContent.isIsDefaultLang() && newsContent.isIsSystemLanguage() )
        {
          DIYCommunicationsNewsForm formData = (DIYCommunicationsNewsForm)form;
          formData.setDefaultLanguage( defaultLanguage.toString() );
        }

        returnContent.add( newsContent );
      }
    }
    return returnContent;
  }

  private List<ResourceList> prepareNewsContents( List<ResourceList> resourceContents, String defaultLanguage )
  {
    int index = 0;

    for ( Iterator<ResourceList> resourceContentIter = resourceContents.iterator(); resourceContentIter.hasNext(); )
    {
      ResourceList resourceContent = resourceContentIter.next();
      resourceContent.setIndex( index++ );
      if ( !StringUtils.isEmpty( defaultLanguage ) )
      {
        resourceContent.setIsDefaultLang( defaultLanguage.toString().equalsIgnoreCase( resourceContent.getLanguage().toString() ) );
        resourceContent.setIsSystemLanguage( defaultLanguage.toString().equalsIgnoreCase( getCMSDefaultLocale().toString() ) );
      }
    }
    return resourceContents;
  }

}
