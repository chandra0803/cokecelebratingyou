
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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.DIYCommunicationsFileUploadValue;
import com.biperf.core.value.diycommunication.AudienceResults;
import com.biperf.core.value.diycommunication.CommunicationsBannerData;
import com.biperf.core.value.diycommunication.CommunicationsManageBannerTable;
import com.biperf.core.value.diycommunication.CommunicationsUploadDoc;
import com.biperf.core.value.diycommunication.MessageUploadBean;
import com.biperf.core.value.diycommunication.ResourceList;
import com.biperf.core.value.diycommunication.SelectAudienceParticipantData;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;

public class DIYBannerAction extends BaseDIYCommunicationsAction
{
  private static final Log logger = LogFactory.getLog( DIYBannerAction.class );

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

  // populates the banner list as a table on the banner maintenance page.
  public ActionForward populateBannerMaintenanceTable( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String statusType = "";
    // removing the bannerId from session, as that may be carried along when a back icon is clicked
    // on the edit page to list page.
    request.getSession().removeAttribute( "bannerId" );
    request.getSession().removeAttribute( "audienceResultsList" );

    if ( request.getParameter( "statusType" ) != null )
    {
      statusType = RequestUtils.getRequiredParamString( request, "statusType" );
    }
    CommunicationsManageBannerTable bannerList = new CommunicationsManageBannerTable();

    List<DIYCommunications> communicationList = new ArrayList<DIYCommunications>();

    if ( !statusType.isEmpty() && statusType.equals( "archived" ) )
    {
      communicationList = getParticipantDIYCommunicationsService().getArchievedByCommunicationType( UserManager.getUserId(), DIYCommunications.COMMUNICATION_TYPE_BANNER );
    }
    else
    {
      communicationList = getParticipantDIYCommunicationsService().getActiveByCommunicationType( UserManager.getUserId(), DIYCommunications.COMMUNICATION_TYPE_BANNER );
    }

    if ( communicationList != null && !communicationList.isEmpty() )
    {
      bannerList = new CommunicationsManageBannerTable( request, communicationList, statusType.isEmpty() || statusType.equals( "active" ) ? true : false );
    }
    super.writeAsJsonToResponse( bannerList, response );
    return null;
  }

  // to display the banner create/edit page
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    String forward = ActionConstants.SUCCESS_FORWARD;

    Long value = buildCommunicationsTypeId( request, "bannerId" );
    request.setAttribute( "bannerId", value );
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

  // returns the create or edit JSON for a banner.
  public ActionForward populateBannerContent( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }

    request.getSession().removeAttribute( "disableDate" );
    CommunicationsBannerData banner = new CommunicationsBannerData( siteUrlPrefix );
    List<ResourceList> bannerContents = new ArrayList<ResourceList>();
    DIYCommunications diyCommunication = null;

    String bannerId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "bannerId" );

    // fetching the form from session because when an ajax is called in a jsp with form errors, the
    // form is
    // not in reach for this ajax call, so form is set in the controller when there are serve errors
    // and fetched here.
    DIYCommunicationsBannerForm formData = (DIYCommunicationsBannerForm)request.getSession().getAttribute( DIYCommunications.SESSION_BANNER_FORM );

    if ( formData != null && formData.getBannerTitle() != null )
    {
      diyCommunication = new DIYCommunications();
      diyCommunication.setContentTitle( formData.getBannerTitle() );
      diyCommunication.setStartDate( DateUtils.toDate( formData.getBannerStartDate() ) );
      diyCommunication.setEndDate( DateUtils.toDate( formData.getBannerEndDate() ) );
      diyCommunication.setId( request.getSession().getAttribute( "bannerId" ) != null ? (Long)request.getSession().getAttribute( "bannerId" ) : null );

      if ( formData.getDefaultLanguage() == null )
      {
        String lang = request.getSession().getAttribute( "userLang" ) != null ? (String)request.getSession().getAttribute( "userLang" ) : null;
        formData.setDefaultLanguage( lang );
      }

      bannerContents = prepareBannerContents( formData.getBannerContentList(), formData.getDefaultLanguage() );
      banner = new CommunicationsBannerData( diyCommunication, bannerContents, siteUrlPrefix );

      request.getSession().setAttribute( "communicationsAudienceList", formData.getAudienceSelGroupList() );
      request.getSession().removeAttribute( DIYCommunications.SESSION_BANNER_FORM );
    }
    else if ( !StringUtils.isEmpty( bannerId ) )
    {
      request.getSession().removeAttribute( "bannerId" );

      diyCommunication = getParticipantDIYCommunicationsService().getDIYCommunicationsById( new Long( bannerId ) );

      if ( DateUtils.toEndDate( diyCommunication.getEndDate() ).before( DateUtils.getCurrentDateTrimmed() ) )
      {
        diyCommunication.setStartDate( null );
        diyCommunication.setEndDate( null );
      }

      bannerContents = getContentFromCM( diyCommunication.getCmAssetCode(), bannerId, form );

      banner = new CommunicationsBannerData( diyCommunication, bannerContents, siteUrlPrefix );

      DIYCommunicationsBannerForm updatedForm = (DIYCommunicationsBannerForm)form;
      request.getSession().setAttribute( "bannerId", new Long( bannerId ) );
      request.getSession().setAttribute( "userLang", updatedForm.getDefaultLanguage() );
    }

    super.writeAsJsonToResponse( banner, response );
    return null;
  }

  // returns the audienceList JSON.
  public ActionForward populatePublicAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SelectAudienceParticipantData audienceList = generateAudience( "bannerId", request );
    super.writeAsJsonToResponse( audienceList, response );
    return null;
  }

  // returns the uploadBanner JSON.
  public ActionForward uploadBanner( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    DIYCommunicationsBannerForm form = (DIYCommunicationsBannerForm)actionForm;

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
    DIYOwnImageUploadBean status = super.uploadImage( uploadAnImage, sizesList, DIYCommunications.COMMUNICATION_TYPE_BANNER );

    writeUploadJsonToResponse( status, response );
    return null;
  }

  public ActionForward uploadDocument( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    DIYCommunicationsBannerForm communicationsForm = (DIYCommunicationsBannerForm)form;
    FormFile formFile = communicationsForm.getBannerContentDoc();

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

    DIYCommunicationsFileUploadValue data = new DIYCommunicationsFileUploadValue();
    data.setData( imageInByte );
    data.setName( filename );
    data.setSize( filesize );
    data.setType( fileFormat );

    if ( DIYCommunicationsFileUploadValue.TYPE_PDF.equalsIgnoreCase( fileFormat ) || DIYCommunicationsFileUploadValue.TYPE_XLS.equalsIgnoreCase( fileFormat )
        || DIYCommunicationsFileUploadValue.TYPE_XLSX.equalsIgnoreCase( fileFormat ) || DIYCommunicationsFileUploadValue.TYPE_DOC.equalsIgnoreCase( fileFormat )
        || DIYCommunicationsFileUploadValue.TYPE_DOCX.equalsIgnoreCase( fileFormat ) || DIYCommunicationsFileUploadValue.TYPE_PPT.equalsIgnoreCase( fileFormat )
        || DIYCommunicationsFileUploadValue.TYPE_PPTX.equalsIgnoreCase( fileFormat ) )
    {
      uploadFile( response, filename, data, orginalfilename, DIYCommunications.COMMUNICATION_TYPE_BANNER );
    }
    else if ( getParticipantDIYCommunicationsService().isValidImageFormat( fileFormat ) )
    {
      uploadImage( response, filename, data, orginalfilename, DIYCommunications.COMMUNICATION_TYPE_BANNER );
    }
    else
    {
      MessageUploadBean uploadBean = new MessageUploadBean();
      uploadBean.setSuccess( false );
      uploadBean.setType( "Failed" );
      uploadBean.setName( orginalfilename );
      uploadBean.setText( CmsResourceBundle.getCmsBundle().getString( "diyCommunications.errors.INVALID_FORMAT" ) );
      CommunicationsUploadDoc fileUploadView = new CommunicationsUploadDoc( uploadBean );
      writeUploadJsonToResponse( fileUploadView, response );
      logger.error( "Invalid file format supplied : " + fileFormat );
    }

    return null;
  }

  // Saves the Banner Content to CM and then saves the Banner object.
  public ActionForward saveBanner( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    ActionForward forward = new ActionForward( mapping.findForward( ActionConstants.SUCCESS_FORWARD ) );
    ActionMessages errors = new ActionMessages();
    Locale systemDefaultLocale = getCMSDefaultLocale();

    // save the banner assests in cm with the banner upload image saved in cmdam3.
    DIYCommunicationsBannerForm diyForm = (DIYCommunicationsBannerForm)form;

    Long communicationId = null;
    List<String> audienceNames = new ArrayList<String>();

    if ( request.getSession() != null )
    {
      communicationId = request.getSession().getAttribute( "bannerId" ) != null ? (Long)request.getSession().getAttribute( "bannerId" ) : null;
    }

    DIYCommunications diyCommunication = communicationId != null ? getParticipantDIYCommunicationsService().getDIYCommunicationsById( communicationId ) : null;

    if ( diyCommunication == null )
    {
      diyCommunication = new DIYCommunications();
      User managerUser = getUserService().getUserById( UserManager.getUserId() );
      diyCommunication.setManagerUser( managerUser );
    }

    diyForm.toDomain( diyCommunication );

    for ( Iterator<AudienceResults> audienceIter = diyForm.getAudienceSelGroupList().iterator(); audienceIter.hasNext(); )
    {
      AudienceResults results = audienceIter.next();

      audienceNames.add( getAudienceService().getAudienceNameById( new Long( results.getId() ) ) );
    }
    if ( communicationId != null )
    {
      getCMAssetService().expireContents( diyCommunication.getCmAssetCode(), diyForm.getBannerContentList(), systemDefaultLocale.toString() );
    }

    List<ResourceList> bannerConntentList = diyForm.getBannerContentList();

    if ( diyForm.getDefaultLanguage() == null )
    {
      String lang = request.getSession().getAttribute( "userLang" ) != null ? (String)request.getSession().getAttribute( "userLang" ) : null;
      diyForm.setDefaultLanguage( lang );
      request.getSession().removeAttribute( "userLang" );
    }

    boolean updateDefaultLocale = false;
    boolean insertDefaultLocale = insertDefaultLocaleContent( bannerConntentList );

    if ( diyForm.getDefaultLanguage() != null )
    {
      updateDefaultLocale = !diyForm.getDefaultLanguage().equals( systemDefaultLocale.toString() );
      if ( updateDefaultLocale )
      {
        removeExistingDefaultContent( bannerConntentList );
      }
    }

    for ( Iterator<ResourceList> bannerConntentIter = bannerConntentList.iterator(); bannerConntentIter.hasNext(); )
    {
      ResourceList bannerResource = bannerConntentIter.next();

      Map<String, Object> deafultLangMap = new HashMap<String, Object>();
      deafultLangMap.put( "sourceLocale", diyForm.getDefaultLanguage() );
      deafultLangMap.put( "locale", CmsUtil.getLocale( bannerResource.getLanguage() ) );

      diyCommunication = getParticipantDIYCommunicationsService().saveBannerContents( diyCommunication, bannerResource, audienceNames, deafultLangMap );

      if ( ( insertDefaultLocale || updateDefaultLocale ) && bannerResource.getLanguage().equals( diyForm.getDefaultLanguage() ) )
      {
        deafultLangMap.put( "locale", systemDefaultLocale );
        diyCommunication = getParticipantDIYCommunicationsService().saveBannerContents( diyCommunication, bannerResource, audienceNames, deafultLangMap );
      }
    }

    try
    {
      getParticipantDIYCommunicationsService().saveDIYCommunications( diyCommunication );
    }
    catch( UniqueConstraintViolationException ex )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "diyCommunications.errors.TITLE_EXISTS" ) );
      request.setAttribute( "serverReturnedErrorForBanner", Boolean.TRUE );
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
  private List<ResourceList> getContentFromCM( String code, String commBannerId, ActionForm form )
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

        String linkUrl = "";
        String referenceImageKey = "";
        String defaultLanguage = "";

        Translations imageObject = (Translations)m.get( DIYCommunications.DIY_BANNER_CMASSET_IMAGESIZE );
        Translations imageMaxObject = (Translations)m.get( DIYCommunications.DIY_BANNER_CMASSET_IMAGESIZE_MAX );
        Translations imageMobileObject = (Translations)m.get( DIYCommunications.DIY_BANNER_CMASSET_IMAGESIZE_MOBILE );

        Translations linkUrlObject = (Translations)m.get( DIYCommunications.DIY_BANNER_CMASSET_LINK_URL );
        Translations referenceImage = (Translations)m.get( DIYCommunications.DIY_BANNER_CMASSET_REFERENCE_CONTENTKEY );
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

        if ( linkUrlObject != null )
        {
          linkUrl = linkUrlObject.getValue();
        }
        if ( referenceImage != null )
        {
          referenceImageKey = referenceImage.getValue();
        }
        if ( defaultLanguageObject != null )
        {
          defaultLanguage = defaultLanguageObject.getValue();
        }

        ResourceList bannerContent = new ResourceList();
        bannerContent.setImageSize( image );
        bannerContent.setImageSize_max( imageMax );
        bannerContent.setImageSize_mobile( imageMobile );

        bannerContent.setLink( linkUrl );
        bannerContent.setIndex( index++ );
        bannerContent.setImageId( referenceImageKey );
        bannerContent.setId( id.toString() );
        bannerContent.setLanguage( content.getLocale().toString() );
        LanguageType languageItem = LanguageType.lookup( content.getLocale().toString() );
        bannerContent.setLanguageDisplay( languageItem.getName() );
        bannerContent.setIsDefaultLang( defaultLanguage.toString().equalsIgnoreCase( content.getLocale().toString() ) );
        bannerContent.setIsSystemLanguage( defaultLanguage.toString().equalsIgnoreCase( getCMSDefaultLocale().toString() ) );
        bannerContent.setSystemLocale( content.getLocale().toString().equalsIgnoreCase( getCMSDefaultLocale().toString() ) );

        id++;
        if ( bannerContent.isIsDefaultLang() && bannerContent.isIsSystemLanguage() )
        {
          DIYCommunicationsBannerForm formData = (DIYCommunicationsBannerForm)form;
          formData.setDefaultLanguage( defaultLanguage.toString() );
        }
        returnContent.add( bannerContent );
      }
    }
    return returnContent;
  }

  private List<ResourceList> prepareBannerContents( List<ResourceList> resourceContents, String defaultLanguage )
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
