
package com.biperf.core.ui.diycommunication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.common.util.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.diycommunication.AudienceResults;
import com.biperf.core.value.diycommunication.CommunicationsManageTipsTable;
import com.biperf.core.value.diycommunication.CommunicationsTipsData;
import com.biperf.core.value.diycommunication.ResourceList;
import com.biperf.core.value.diycommunication.SelectAudienceParticipantData;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.util.CmsUtil;

public class DIYCommunicationsTipsAction extends BaseDIYCommunicationsAction
{

  private static final Log logger = LogFactory.getLog( DIYCommunicationsTipsAction.class );

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

    Long value = buildCommunicationsTypeId( request, "tipsId" );
    request.setAttribute( "tipsId", value );
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

  public ActionForward populateTipsTable( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String statusType = "";

    // removing the tipsId from session, as that may be carried along when a back icon is clicked
    // on the edit page to list page.
    request.getSession().removeAttribute( "tipsId" );
    request.getSession().removeAttribute( "audienceResultsList" );

    if ( request.getParameter( "statusType" ) != null )
    {
      statusType = RequestUtils.getRequiredParamString( request, "statusType" );
    }
    CommunicationsManageTipsTable matchList = new CommunicationsManageTipsTable();

    List<DIYCommunications> communicationList = new ArrayList<DIYCommunications>();

    if ( !statusType.isEmpty() && statusType.equals( "archived" ) )
    {
      communicationList = getParticipantDIYCommunicationsService().getArchievedByCommunicationType( UserManager.getUserId(), DIYCommunications.COMMUNICATION_TYPE_TIPS );
    }
    else
    {
      communicationList = getParticipantDIYCommunicationsService().getActiveByCommunicationType( UserManager.getUserId(), DIYCommunications.COMMUNICATION_TYPE_TIPS );
    }

    if ( communicationList != null && !communicationList.isEmpty() )
    {
      matchList = new CommunicationsManageTipsTable( communicationList, request, statusType.isEmpty() || statusType.equals( "active" ) ? true : false );
    }
    super.writeAsJsonToResponse( matchList, response );
    return null;
  }

  public ActionForward populateTipsDataEdit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    CommunicationsTipsData matchList = new CommunicationsTipsData();
    List<ResourceList> tipsContentList = new ArrayList<ResourceList>();
    Long communicationId = buildCommunicationsTypeId( request, "tipsId" );
    request.getSession().removeAttribute( "disableDate" );

    DIYCommunications communications = null;

    // fetching the form from session because when an ajax is called in a jsp with form errors, the
    // form is
    // not in reach for this ajax call, so form is set in the controller when there are serve errors
    // and fetched here.
    DIYCommunicationsTipsForm formData = (DIYCommunicationsTipsForm)request.getSession().getAttribute( DIYCommunications.SESSION_TIPS_FORM );

    if ( formData != null && formData.getTipTitle() != null )
    {
      communications = new DIYCommunications();
      communications.setContentTitle( formData.getTipTitle() );
      communications.setStartDate( DateUtils.toDate( formData.getTipStartDate() ) );
      communications.setEndDate( DateUtils.toDate( formData.getTipEndDate() ) );
      communications.setId( request.getSession().getAttribute( "tipsId" ) != null ? (Long)request.getSession().getAttribute( "tipsId" ) : null );

      if ( formData.getDefaultLanguage() == null )
      {
        String lang = request.getSession().getAttribute( "userLang" ) != null ? (String)request.getSession().getAttribute( "userLang" ) : null;
        formData.setDefaultLanguage( lang );
      }

      tipsContentList = prepareTipsContents( formData.getTipContentList(), formData.getDefaultLanguage() );

      matchList = new CommunicationsTipsData( communications, tipsContentList );

      request.getSession().setAttribute( "communicationsAudienceList", formData.getAudienceSelGroupList() );
      request.getSession().removeAttribute( DIYCommunications.SESSION_TIPS_FORM );
    }
    else if ( communicationId != null && communicationId > 0 )
    {
      communications = getCommunicationById( communicationId );
      if ( DateUtils.toEndDate( communications.getEndDate() ).before( DateUtils.getCurrentDateTrimmed() ) )
      {
        communications.setStartDate( null );
        communications.setEndDate( null );
      }
      tipsContentList = getContentFromCM( communications.getCmAssetCode(), form );
      matchList = new CommunicationsTipsData( communications, tipsContentList );

      DIYCommunicationsTipsForm updatedForm = (DIYCommunicationsTipsForm)form;
      request.getSession().setAttribute( "tipsId", communicationId );
      request.getSession().setAttribute( "userLang", updatedForm.getDefaultLanguage() );
    }

    super.writeAsJsonToResponse( matchList, response );
    return null;
  }

  public ActionForward populatePublicAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SelectAudienceParticipantData audienceList = generateAudience( "tipsId", request );
    super.writeAsJsonToResponse( audienceList, response );
    return null;
  }

  public ActionForward saveTipsDetails( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ActionForward forward = new ActionForward( mapping.findForward( ActionConstants.SUCCESS_FORWARD ) );
    ActionMessages errors = new ActionMessages();
    Locale systemDefaultLocale = getCMSDefaultLocale();

    if ( isCancelled( request ) )
    {
      forward = new ActionForward( mapping.findForward( ActionConstants.CANCEL_FORWARD ) );
    }
    else
    {
      DIYCommunicationsTipsForm communicationsForm = (DIYCommunicationsTipsForm)form;
      Long communicationId = (Long)request.getSession().getAttribute( "tipsId" );
      if ( communicationId == null )
      {
        communicationId = request.getSession().getAttribute( "tipsId" ) != null ? (Long)request.getSession().getAttribute( "tipsId" ) : null;
      }
      DIYCommunications diyCommunications = communicationId != null ? getCommunicationById( communicationId ) : null;
      List<String> audienceNames = new ArrayList<String>();

      if ( diyCommunications == null )
      {
        diyCommunications = new DIYCommunications();
        diyCommunications.setManagerUser( getParticipantService().getParticipantById( UserManager.getUserId() ) );
        diyCommunications.setCommunicationType( DIYCommunications.COMMUNICATION_TYPE_TIPS );
      }
      communicationsForm.toDomain( diyCommunications );

      for ( Iterator<AudienceResults> audienceIter = communicationsForm.getAudienceSelGroupList().iterator(); audienceIter.hasNext(); )
      {
        AudienceResults results = audienceIter.next();
        audienceNames.add( getAudienceService().getAudienceNameById( new Long( results.getId() ) ) );
      }
      if ( communicationId != null )
      {
        getCMAssetService().expireContents( diyCommunications.getCmAssetCode(), communicationsForm.getTipContentList(), systemDefaultLocale.toString() );
      }

      List<ResourceList> tipContentList = communicationsForm.getTipContentList();

      if ( communicationsForm.getDefaultLanguage() == null )
      {
        String lang = request.getSession().getAttribute( "userLang" ) != null ? (String)request.getSession().getAttribute( "userLang" ) : null;
        communicationsForm.setDefaultLanguage( lang );
        request.getSession().removeAttribute( "userLang" );
      }
      boolean updateDefaultLocale = false;
      boolean insertDefaultLocale = insertDefaultLocaleContent( tipContentList );

      if ( communicationsForm.getDefaultLanguage() != null )
      {
        updateDefaultLocale = !communicationsForm.getDefaultLanguage().equals( systemDefaultLocale.toString() );
        if ( updateDefaultLocale )
        {
          removeExistingDefaultContent( tipContentList );
        }
      }

      for ( Iterator<ResourceList> tipsIter = tipContentList.iterator(); tipsIter.hasNext(); )
      {
        ResourceList tipsContent = tipsIter.next();

        Map<String, Object> deafultLangMap = new HashMap<String, Object>();
        deafultLangMap.put( "sourceLocale", communicationsForm.getDefaultLanguage() );
        deafultLangMap.put( "locale", CmsUtil.getLocale( tipsContent.getLanguage() ) );

        diyCommunications = getParticipantDIYCommunicationsService().saveTipsResources( diyCommunications, tipsContent, audienceNames, deafultLangMap );

        if ( ( insertDefaultLocale || updateDefaultLocale ) && tipsContent.getLanguage().equals( communicationsForm.getDefaultLanguage() ) )
        {
          deafultLangMap.put( "locale", systemDefaultLocale );
          diyCommunications = getParticipantDIYCommunicationsService().saveTipsResources( diyCommunications, tipsContent, audienceNames, deafultLangMap );
        }
      }

      try
      {
        getParticipantDIYCommunicationsService().saveDIYCommunications( diyCommunications );
      }
      catch( UniqueConstraintViolationException ex )
      {
        errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "diyCommunications.errors.TITLE_EXISTS" ) );
        request.setAttribute( "serverReturnedErrorForTips", Boolean.TRUE );
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
    }

    return forward;
  }

  private List<ResourceList> getContentFromCM( String code, ActionForm form )
  {
    List<ResourceList> returnContentList = new ArrayList<ResourceList>();
    List<Content> contents = getCMAssetService().getContentsForAsset( code );

    int index = 0;
    Long id = new Long( 1 );

    for ( Content content : contents )
    {
      if ( content.getContentStatus().getName().equals( "Live" ) )
      {
        Map m = content.getContentDataMapList();
        String tipContent = "";
        String tipTitle = "";
        String defaultLanguage = "";

        Translations tipTitleObject = (Translations)m.get( DIYCommunications.DIY_TIPS_CMASSET_TITLE );
        Translations tipContentObject = (Translations)m.get( DIYCommunications.DIY_RESOURCE_CMASSET_LINK_TITLE );
        Translations defaultLanguageObject = (Translations)m.get( DIYCommunications.SOURCE_LOCALE );

        if ( tipContentObject != null )
        {
          tipContent = tipContentObject.getValue();
        }
        if ( tipTitleObject != null )
        {
          tipTitle = tipTitleObject.getValue();
        }
        if ( defaultLanguageObject != null )
        {
          defaultLanguage = defaultLanguageObject.getValue();
        }

        ResourceList resourceContent = new ResourceList();
        resourceContent.setContent( tipContent );
        resourceContent.setIndex( index++ );
        resourceContent.setId( id.toString() );
        resourceContent.setLanguage( content.getLocale().toString() );
        LanguageType languageItem = LanguageType.lookup( content.getLocale().toString() );
        resourceContent.setLanguageDisplay( languageItem.getName() );
        resourceContent.setTitle( tipTitle );
        resourceContent.setIsDefaultLang( defaultLanguage.toString().equalsIgnoreCase( content.getLocale().toString() ) );
        resourceContent.setIsSystemLanguage( defaultLanguage.toString().equalsIgnoreCase( getCMSDefaultLocale().toString() ) );
        resourceContent.setSystemLocale( content.getLocale().toString().equalsIgnoreCase( getCMSDefaultLocale().toString() ) );

        id++;
        if ( resourceContent.isIsDefaultLang() && resourceContent.isIsSystemLanguage() )
        {
          DIYCommunicationsTipsForm formData = (DIYCommunicationsTipsForm)form;
          formData.setDefaultLanguage( defaultLanguage.toString() );
        }
        returnContentList.add( resourceContent );
      }

    }
    return returnContentList;
  }

  private List<ResourceList> prepareTipsContents( List<ResourceList> resourceContents, String defaultLanguage )
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
