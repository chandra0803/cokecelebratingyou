
package com.biperf.core.ui.diycommunication;

import java.util.ArrayList;
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
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.DIYCommunicationsFileUploadValue;
import com.biperf.core.value.diycommunication.AudienceResults;
import com.biperf.core.value.diycommunication.CommunicationsManageResourceCenterTable;
import com.biperf.core.value.diycommunication.CommunicationsResourceCenterData;
import com.biperf.core.value.diycommunication.CommunicationsUploadDoc;
import com.biperf.core.value.diycommunication.MessageUploadBean;
import com.biperf.core.value.diycommunication.ResourceList;
import com.biperf.core.value.diycommunication.SelectAudienceParticipantData;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.CmsUtil;

public class DIYCommunicationsResourceCenterAction extends BaseDIYCommunicationsAction
{
  private static final Log logger = LogFactory.getLog( DIYCommunicationsResourceCenterAction.class );

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

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forward = ActionConstants.SUCCESS_FORWARD;

    Long value = buildCommunicationsTypeId( request, "resourceId" );
    request.setAttribute( "resourceId", value );
    if ( value != null )
    {
      DIYCommunications communications = getCommunicationById( value );

      if ( DateUtils.toStartDate( communications.getStartDate() ).before( DateUtils.getCurrentDateTrimmed() ) )
      {
        request.getSession().setAttribute( "disableDate", Boolean.TRUE );
      }
    }

    return mapping.findForward( forward );
  }

  public ActionForward cancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  public ActionForward populateResourceCenterTable( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String statusType = "";

    // removing the resourceId from session, as that may be carried along when a back icon is
    // clicked
    // on the edit page to list page.
    request.getSession().removeAttribute( "resourceId" );
    request.getSession().removeAttribute( "audienceResultsList" );

    if ( request.getParameter( "statusType" ) != null )
    {
      statusType = RequestUtils.getRequiredParamString( request, "statusType" );
    }
    CommunicationsManageResourceCenterTable matchList = new CommunicationsManageResourceCenterTable();

    List<DIYCommunications> communicationList = new ArrayList<DIYCommunications>();

    if ( !statusType.isEmpty() && statusType.equals( "archived" ) )
    {
      communicationList = getParticipantDIYCommunicationsService().getArchievedByCommunicationType( UserManager.getUserId(), DIYCommunications.COMMUNICATION_TYPE_RESOURCE_CENTER );
    }
    else
    {
      communicationList = getParticipantDIYCommunicationsService().getActiveByCommunicationType( UserManager.getUserId(), DIYCommunications.COMMUNICATION_TYPE_RESOURCE_CENTER );
    }

    if ( communicationList != null && !communicationList.isEmpty() )
    {
      matchList = new CommunicationsManageResourceCenterTable( communicationList, request, statusType.isEmpty() || statusType.equals( "active" ) ? true : false );
    }
    super.writeAsJsonToResponse( matchList, response );
    return null;
  }

  public ActionForward populateResourceCenterDataEdit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    CommunicationsResourceCenterData matchList = new CommunicationsResourceCenterData();
    List<ResourceList> resourceContentList = new ArrayList<ResourceList>();
    Long communicationId = buildCommunicationsTypeId( request, "resourceId" );
    request.getSession().removeAttribute( "disableDate" );

    DIYCommunications communications = null;

    // fetching the form from session because when an ajax is called in a jsp with form errors, the
    // form is
    // not in reach for this ajax call, so form is set in the controller when there are serve errors
    // and fetched here.
    DIYCommunicationsResourceCenterForm formData = (DIYCommunicationsResourceCenterForm)request.getSession().getAttribute( DIYCommunications.SESSION_RESOURCE_CENTER_FORM );

    if ( formData != null && formData.getResourceTitle() != null )
    {
      communications = new DIYCommunications();
      communications.setContentTitle( formData.getResourceTitle() );
      communications.setStartDate( DateUtils.toDate( formData.getResourceStartDate() ) );
      communications.setEndDate( DateUtils.toDate( formData.getResourceEndDate() ) );
      communications.setId( request.getSession().getAttribute( "resourceId" ) != null ? (Long)request.getSession().getAttribute( "resourceId" ) : null );

      if ( formData.getDefaultLanguage() == null )
      {
        String lang = request.getSession().getAttribute( "userLang" ) != null ? (String)request.getSession().getAttribute( "userLang" ) : null;
        formData.setDefaultLanguage( lang );
      }

      resourceContentList = prepareResourceCenterContents( formData.getResourceContentList(), formData.getDefaultLanguage() );

      matchList = new CommunicationsResourceCenterData( communications, resourceContentList );
      request.getSession().setAttribute( "communicationsAudienceList", formData.getAudienceSelGroupList() );
      request.getSession().removeAttribute( DIYCommunications.SESSION_RESOURCE_CENTER_FORM );
    }
    else if ( communicationId != null && communicationId > 0 )
    {
      communications = getCommunicationById( communicationId );
      if ( DateUtils.toEndDate( communications.getEndDate() ).before( DateUtils.getCurrentDateTrimmed() ) )
      {
        communications.setStartDate( null );
        communications.setEndDate( null );
      }
      resourceContentList = getContentFromCM( communications.getCmAssetCode(), form );

      DIYCommunicationsResourceCenterForm updatedForm = (DIYCommunicationsResourceCenterForm)form;
      matchList = new CommunicationsResourceCenterData( communications, resourceContentList );
      request.getSession().setAttribute( "resourceId", communicationId );
      request.getSession().setAttribute( "userLang", updatedForm.getDefaultLanguage() );
    }

    super.writeAsJsonToResponse( matchList, response );
    return null;
  }

  public ActionForward populatePublicAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SelectAudienceParticipantData audienceList = generateAudience( "resourceId", request );
    super.writeAsJsonToResponse( audienceList, response );
    return null;
  }

  public ActionForward uploadDocument( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    DIYCommunicationsResourceCenterForm communicationsForm = (DIYCommunicationsResourceCenterForm)form;
    FormFile formFile = communicationsForm.getResourceContentDoc();

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
      uploadFile( response, filename, data, orginalfilename, "resourceCenter" );
    }
    else if ( getParticipantDIYCommunicationsService().isValidImageFormat( fileFormat ) )
    {
      uploadImage( response, filename, data, orginalfilename, "resourceCenter" );
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

  public ActionForward saveResourceCenterDetails( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ActionForward forward = new ActionForward( mapping.findForward( ActionConstants.SUCCESS_FORWARD ) );
    ActionMessages errors = new ActionMessages();
    Locale systemDefaultLocale = getCMSDefaultLocale();

    DIYCommunicationsResourceCenterForm communicationsForm = (DIYCommunicationsResourceCenterForm)form;
    Long communicationId = (Long)request.getSession().getAttribute( "resourceId" );
    DIYCommunications diyCommunications = communicationId != null ? getCommunicationById( communicationId ) : null;
    List<String> audienceNames = new ArrayList<String>();

    if ( diyCommunications == null )
    {
      diyCommunications = new DIYCommunications();
      diyCommunications.setManagerUser( getParticipantService().getParticipantById( UserManager.getUserId() ) );
      diyCommunications.setCommunicationType( DIYCommunications.COMMUNICATION_TYPE_RESOURCE_CENTER );
    }
    communicationsForm.toDomain( diyCommunications );

    for ( Iterator<AudienceResults> audienceIter = communicationsForm.getAudienceSelGroupList().iterator(); audienceIter.hasNext(); )
    {
      AudienceResults results = audienceIter.next();
      audienceNames.add( getAudienceService().getAudienceNameById( new Long( results.getId() ) ) );
    }
    if ( communicationId != null )
    {
      getCMAssetService().expireContents( diyCommunications.getCmAssetCode(), communicationsForm.getResourceContentList(), systemDefaultLocale.toString() );
    }

    List<ResourceList> resourceContentList = communicationsForm.getResourceContentList();

    if ( communicationsForm.getDefaultLanguage() == null )
    {
      String lang = request.getSession().getAttribute( "userLang" ) != null ? (String)request.getSession().getAttribute( "userLang" ) : null;
      communicationsForm.setDefaultLanguage( lang );
      request.getSession().removeAttribute( "userLang" );
    }

    boolean updateDefaultLocale = false;
    boolean insertDefaultLocale = insertDefaultLocaleContent( resourceContentList );

    if ( communicationsForm.getDefaultLanguage() != null )
    {
      updateDefaultLocale = !communicationsForm.getDefaultLanguage().equals( systemDefaultLocale.toString() );
      if ( updateDefaultLocale )
      {
        removeExistingDefaultContent( resourceContentList );
      }
    }

    for ( Iterator<ResourceList> resourceIter = resourceContentList.iterator(); resourceIter.hasNext(); )
    {
      ResourceList resourceCenterContent = resourceIter.next();

      Map<String, Object> deafultLangMap = new HashMap<String, Object>();
      deafultLangMap.put( "sourceLocale", communicationsForm.getDefaultLanguage() );
      deafultLangMap.put( "locale", CmsUtil.getLocale( resourceCenterContent.getLanguage() ) );

      diyCommunications = getParticipantDIYCommunicationsService().saveResourceCenterResources( diyCommunications, resourceCenterContent, audienceNames, deafultLangMap );

      if ( ( insertDefaultLocale || updateDefaultLocale ) && resourceCenterContent.getLanguage().equals( communicationsForm.getDefaultLanguage() ) )
      {
        deafultLangMap.put( "locale", systemDefaultLocale );
        diyCommunications = getParticipantDIYCommunicationsService().saveResourceCenterResources( diyCommunications, resourceCenterContent, audienceNames, deafultLangMap );
      }
    }

    try
    {
      getParticipantDIYCommunicationsService().saveDIYCommunications( diyCommunications );
    }
    catch( UniqueConstraintViolationException ex )
    {
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "diyCommunications.errors.TITLE_EXISTS" ) );
      request.setAttribute( "serverReturnedErrorForResourceCenter", Boolean.TRUE );
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
      request.setAttribute( "serverReturnedErrored", true );
      forward = new ActionForward( mapping.findForward( ActionConstants.FAIL_FORWARD ) );
    }

    request.setAttribute( "displayPopup", true );

    return forward;
  }

  @SuppressWarnings( "rawtypes" )
  private List<ResourceList> getContentFromCM( String code, ActionForm form )
  {
    List<ResourceList> returnContentList = new ArrayList<ResourceList>();
    List contents = getCMAssetService().getContentsForAsset( code );

    int index = 0;
    Long id = new Long( 1 );

    for ( Iterator contentIter = contents.iterator(); contentIter.hasNext(); )
    {
      Content content = (Content)contentIter.next();
      if ( content.getContentStatus().getName().equals( "Live" ) )
      {
        Map m = content.getContentDataMapList();
        String linkUrl = "";
        String linkTitle = "";
        String defaultLanguage = "";

        Translations linkTitleObject = (Translations)m.get( DIYCommunications.DIY_RESOURCE_CMASSET_LINK_TITLE );
        Translations linkUrlObject = (Translations)m.get( DIYCommunications.DIY_RESOURCE_CMASSET_LINK_URL );
        Translations defaultLanguageObject = (Translations)m.get( DIYCommunications.SOURCE_LOCALE );

        if ( linkUrlObject != null )
        {
          linkUrl = linkUrlObject.getValue();
        }
        if ( linkTitleObject != null )
        {
          linkTitle = linkTitleObject.getValue();
        }
        if ( defaultLanguageObject != null )
        {
          defaultLanguage = defaultLanguageObject.getValue();
        }

        ResourceList resourceContent = new ResourceList();
        resourceContent.setLink( linkUrl );
        resourceContent.setIndex( index++ );
        resourceContent.setId( id.toString() );
        resourceContent.setLanguage( content.getLocale().toString() );
        LanguageType languageItem = LanguageType.lookup( content.getLocale().toString() );
        resourceContent.setLanguageDisplay( languageItem.getName() );
        resourceContent.setTitle( linkTitle );
        resourceContent.setIsDefaultLang( defaultLanguage.toString().equalsIgnoreCase( content.getLocale().toString() ) );
        resourceContent.setIsSystemLanguage( defaultLanguage.toString().equalsIgnoreCase( getCMSDefaultLocale().toString() ) );
        resourceContent.setSystemLocale( content.getLocale().toString().equalsIgnoreCase( getCMSDefaultLocale().toString() ) );

        if ( resourceContent.isIsDefaultLang() && resourceContent.isIsSystemLanguage() )
        {
          DIYCommunicationsResourceCenterForm formData = (DIYCommunicationsResourceCenterForm)form;
          formData.setDefaultLanguage( defaultLanguage.toString() );
        }

        returnContentList.add( resourceContent );
      }

      id++;

    }
    return returnContentList;
  }

  private List<ResourceList> prepareResourceCenterContents( List<ResourceList> resourceContents, String defaultLanguage )
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
