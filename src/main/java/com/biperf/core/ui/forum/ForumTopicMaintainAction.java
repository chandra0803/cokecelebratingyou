/**
 * 
 */

package com.biperf.core.ui.forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.AudienceType;
import com.biperf.core.domain.forum.ForumTopic;
import com.biperf.core.domain.forum.ForumTopicAudience;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.forum.ForumTopicService;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.participant.ListBuilderAction;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.forum.ForumAudienceFormBean;

/**
 * @author poddutur
 *
 */
public class ForumTopicMaintainAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( ForumTopicMaintainAction.class );

  public static final String FORUM_TOPIC_FORM = "forumTopicForm";

  /**
   * RETURN_ACTION_URL_PARAM
   */
  public static String RETURN_ACTION_URL_PARAM = "returnActionUrl";

  /**
   * SESSION_AUDIENCE_FORM
   */
  public static String SESSION_AUDIENCE_FORM = "sessionAudienceForm";

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward prepareCreate( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ForumTopicForm forumTopicForm = (ForumTopicForm)form;

    ForumTopic forumTopic = new ForumTopic();
    forumTopicForm.loadDefaultParameters( forumTopic );
    request.setAttribute( "forumTopic", forumTopic );

    // get the actionForward to display the create pages.
    return actionMapping.findForward( ActionConstants.CREATE_FORWARD );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward create( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward actionForward = null;
    ActionMessages errors = new ActionMessages();

    if ( !isTokenValid( request, true ) )
    {
      errors.add( "tokenFailure", new ActionMessage( "system.errors.TOKEN_EXCEPTION" ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_CREATE ); // EARLY EXIT
    }

    try
    {
      ForumTopicForm forumTopicForm = (ForumTopicForm)form;

      String topicTitle = forumTopicForm.getTopicCmAssetCode();
      boolean isTitleExists = getForumTopicService().isTopicNameExists( topicTitle );
      if ( isTitleExists )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "forum.topic.details.TOPIC_NAME_EXISTS" ) );
        throw new Exception();
      }

      List<ForumAudienceFormBean> primaryAudienceList = new ArrayList<ForumAudienceFormBean>();
      primaryAudienceList = forumTopicForm.getPrimaryAudienceListAsList();
      ForumTopic forumTopic = forumTopicForm.toDomainObject();
      forumTopic = getForumTopicService().saveTopicCmText( forumTopic, forumTopic.getTopicCmAssetCode() );
      forumTopic.setTopicCmAssetCode( forumTopic.getTopicscmAsset() );
      forumTopic = getForumTopicService().save( forumTopic );
      if ( !primaryAudienceList.isEmpty() )
      {
        for ( ForumAudienceFormBean forumAudienceFormBean : primaryAudienceList )
        {
          if ( forumAudienceFormBean.getAudienceId() != null )
          {
            Audience audience = getAudienceService().getAudienceById( forumAudienceFormBean.getAudienceId(), null );
            ForumTopicAudience forumTopicAudience = new ForumTopicAudience();
            forumTopicAudience.setForumTopic( forumTopic );
            forumTopicAudience.setAudience( audience );
            forumTopicAudience = getForumTopicService().save( forumTopicAudience );
          }
        }
      }
      request.setAttribute( "forumTopic", forumTopic );
      request.setAttribute( "forumTopicForm", forumTopicForm );
      request.setAttribute( "primaryAudienceList", primaryAudienceList );
      actionForward = actionMapping.findForward( ActionConstants.SUCCESS_CREATE );
    }
    catch( Exception e )
    {
      log.debug( e );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_CREATE );
    }

    return actionForward;
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   * @throws InvalidClientStateException 
   */
  public ActionForward prepareUpdate( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ForumTopicForm forumTopicForm = (ForumTopicForm)form;
    List<ForumAudienceFormBean> primaryAudienceList = new ArrayList<ForumAudienceFormBean>();

    Long topicId = forumTopicForm.getId();
    ForumTopic forumTopic = getForumTopicService().getTopicById( topicId );
    forumTopic.setTopicscmAsset( forumTopic.getTopicCmAssetCode() );
    forumTopicForm.loadDomainObject( forumTopic );
    String audienceType = forumTopicForm.getAudienceType();
    if ( !audienceType.equals( "all active participants" ) )
    {
      primaryAudienceList = getForumTopicService().getAudienceByTopicId( topicId );
      forumTopicForm.loadDomainAudienceObject( primaryAudienceList );
    }
    request.setAttribute( "forumTopic", forumTopic );
    request.setAttribute( "forumTopicForm", forumTopicForm );

    return actionMapping.findForward( ActionConstants.UPDATE_FORWARD );
  }

  /**
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  @SuppressWarnings( "unchecked" )
  public ActionForward update( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException
  {
    ActionForward actionForward = null;

    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( "tokenFailure", new ActionMessage( "system.errors.TOKEN_EXCEPTION" ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY
      // EXIT
    }
    try
    {
      ForumTopicForm forumTopicForm = (ForumTopicForm)actionForm;
      Long topicId = forumTopicForm.getId();
      List<ForumAudienceFormBean> primaryAudienceList = new ArrayList<ForumAudienceFormBean>();
      primaryAudienceList = forumTopicForm.getPrimaryAudienceListAsList();

      ForumTopic forumTopic = getForumTopicService().getTopicById( topicId );
      forumTopic.setTopicscmAsset( forumTopic.getTopicCmAssetCode() );
      String originalTitle = forumTopic.getTopicNameFromCM();

      String topicTitletoUpdate = forumTopicForm.getTopicCmAssetCode();
      if ( !topicTitletoUpdate.equalsIgnoreCase( originalTitle ) )
      {
        boolean isTitleExists = getForumTopicService().isTopicNameExists( topicTitletoUpdate );
        if ( isTitleExists )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "forum.topic.details.TOPIC_NAME_EXISTS" ) );
          throw new Exception();
        }
      }

      forumTopicForm.populateDomainObject( forumTopic );
      forumTopic = getForumTopicService().saveTopicCmText( forumTopic, forumTopic.getTopicCmAssetCode() );
      forumTopic.setTopicCmAssetCode( forumTopic.getTopicscmAsset() );

      String audienceType = forumTopicForm.getAudienceType();
      Set<ForumTopicAudience> audienceSet = new HashSet<ForumTopicAudience>();
      if ( !audienceType.equals( "all active participants" ) )
      {
        if ( !primaryAudienceList.isEmpty() )
        {
          List audienceIdList = new ArrayList();

          for ( ForumAudienceFormBean forumAudienceFormBean : primaryAudienceList )
          {
            audienceIdList.add( forumAudienceFormBean.getAudienceId() );
          }

          for ( Iterator iter = forumTopic.getAudience().iterator(); iter.hasNext(); )
          {
            ForumTopicAudience forumTopicAudience1 = (ForumTopicAudience)iter.next();
            if ( !audienceIdList.contains( forumTopicAudience1.getAudience().getId() ) )
            {
              try
              {
                getForumTopicService().deleteTopicAudience( forumTopicAudience1.getAudience().getId(), forumTopicForm.getId() );
              }
              catch( Exception e )
              {
                logger.error( e.getMessage(), e );
              }
            }
          }

          for ( ForumAudienceFormBean forumAudienceFormBean : primaryAudienceList )
          {
            if ( forumAudienceFormBean.getAudienceId() != null )
            {
              Audience audience = getAudienceService().getAudienceById( forumAudienceFormBean.getAudienceId(), null );
              ForumTopicAudience forumTopicAudience = new ForumTopicAudience();
              forumTopicAudience.setForumTopic( forumTopic );
              forumTopicAudience.setAudience( audience );
              audienceSet.add( forumTopicAudience );
            }
          }

          forumTopic.setAudience( audienceSet );
          getForumTopicService().save( forumTopic );
        }
      }
      else
      {
        if ( !primaryAudienceList.isEmpty() )
        {
          for ( ForumAudienceFormBean forumAudienceFormBean : primaryAudienceList )
          {
            try
            {
              getForumTopicService().deleteTopicAudience( forumAudienceFormBean.getAudienceId(), forumTopicForm.getId() );
            }
            catch( Exception e )
            {
              logger.error( e.getMessage(), e );
            }
          }
          getForumTopicService().save( forumTopic );
        }
        else
        {
          for ( Iterator iter = forumTopic.getAudience().iterator(); iter.hasNext(); )
          {
            ForumTopicAudience forumTopicAudience = (ForumTopicAudience)iter.next();
            try
            {
              getForumTopicService().deleteTopicAudience( forumTopicAudience.getAudience().getId(), forumTopicForm.getId() );
            }
            catch( Exception e )
            {
              logger.error( e.getMessage(), e );
            }
          }
          getForumTopicService().save( forumTopic );
        }
      }

      forumTopicForm.setId( forumTopic.getId() );
      if ( request.getAttribute( "forumTopicForm" ) != null )
      {
        request.removeAttribute( "forumTopicForm" );
        request.setAttribute( "forumTopicForm", forumTopicForm );
      }
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "id", forumTopicForm.getId() );
      clientStateParameterMap.put( "topicCmAssetCode", forumTopicForm.getTopicCmAssetCode() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      actionForward = ActionUtils.forwardWithParameters( actionMapping, ActionConstants.SUCCESS_UPDATE, new String[] { queryString, "method=display" } );
    }
    catch( Exception e )
    {
      log.debug( e );
    }
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      actionForward = actionMapping.findForward( ActionConstants.FAIL_UPDATE );
    }
    return actionForward;
  }

  public ActionForward addSubmitterAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ForumTopicForm forumTopicForm = (ForumTopicForm)form;

    // If there was no audience selected, then return an error
    if ( forumTopicForm.getPrimaryAudienceId() == null || forumTopicForm.getPrimaryAudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    addAudience( forumTopicForm, request, ForumTopicForm.PRIMARY, new Long( forumTopicForm.getPrimaryAudienceId() ) );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void addAudience( ForumTopicForm form, HttpServletRequest request, String audienceType, Long audienceId )
  {

    // get the audience object
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    if ( AudienceType.SPECIFIC_PAX_TYPE.equals( form.getAudienceType() ) || audience instanceof CriteriaAudience )
    {
      Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
      Long primaryHierarchyId = primaryHierarchy.getId();

      Set audiences = new LinkedHashSet();
      audiences.add( audience );
      List paxFormattedValueList = getListBuilderService().searchParticipants( audiences, primaryHierarchyId, true, null, true );
      audience.setSize( paxFormattedValueList.size() );
    }

    // add the audience to the list
    form.addAudience( audience, audienceType );
  }

  /**
   * Removes submitter audiences.
   * 
   */
  public ActionForward removeSubmitterAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorExceptionWithRollback
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    ActionMessages errors = new ActionMessages();

    ForumTopicForm forumTopicForm = (ForumTopicForm)form;

    List<ForumAudienceFormBean> primaryAudienceList = new ArrayList<ForumAudienceFormBean>();
    primaryAudienceList.addAll( forumTopicForm.getPrimaryAudienceListAsList() );

    // Remove audiences from the primary audience list.
    forumTopicForm.removeItems( ForumTopicForm.PRIMARY );

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  /**
   * display participant list popup
   * 
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward displayPaxListPopup( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    Long audienceId = null;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      try
      {
        audienceId = new Long( (String)clientStateMap.get( "audienceId" ) );
      }
      catch( ClassCastException cce )
      {
        audienceId = (Long)clientStateMap.get( "audienceId" );
      }

      if ( audienceId == null )
      {
        errors.add( "errorMessage", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, "audienceId as part of clientState" ) );
        saveErrors( request, errors );
        return mapping.findForward( "paxListPopup" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    Audience audience = getAudienceService().getAudienceById( audienceId, null );
    List paxFormattedValueList = getPaxsInCriteriaAudience( audience );

    request.setAttribute( "paxFormattedValueList", paxFormattedValueList );
    request.setAttribute( "paxFormattedValueListSize", String.valueOf( paxFormattedValueList.size() ) );
    return mapping.findForward( "paxListPopup" );
  }

  private List getPaxsInCriteriaAudience( Audience audience )
  {
    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();

    Set audiences = new LinkedHashSet();
    audiences.add( audience );
    return getListBuilderService().searchParticipants( audiences, primaryHierarchyId, false, null, true );
  }

  /**
   * Makes a request to the Audience builder sending it a redirect URL which will be used to forward
   * back information built in the audience.
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws Exception
   */
  public ActionForward prepareSubmitterAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ForumTopicForm forumTopicForm = (ForumTopicForm)form;

    ActionForward returnForward = mapping.findForward( "submitterAudienceLookup" );

    Map clientStateParameterMap = new HashMap();
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { queryString } );
    Map parmMap = new HashMap();
    parmMap.put( "saveAudienceReturnUrl", returnUrl );
    String queryString2 = "saveAudienceReturnUrl=" + returnUrl;
    queryString2 = ClientStateUtils.generateEncodedLink( "", "", parmMap );
    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_AUDIENCE_FORM, forumTopicForm );

    return ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString2 } );
  }

  public ActionForward returnSubmitterAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ForumTopicForm forumTopicForm = (ForumTopicForm)form;

    // Get the form back out of the Session to redisplay.
    ForumTopicForm sessionAudienceForm = (ForumTopicForm)request.getSession().getAttribute( SESSION_AUDIENCE_FORM );

    if ( sessionAudienceForm != null )
    {
      String formAudienceType = ForumTopicForm.PRIMARY;
      extractAndAddAudience( request, forumTopicForm, sessionAudienceForm, formAudienceType );
    }

    request.getSession().removeAttribute( SESSION_AUDIENCE_FORM );
    request.getSession().removeAttribute( ListBuilderAction.SESSION_CRITERIA_AUDIENCE_VALUE );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void extractAndAddAudience( HttpServletRequest request, ForumTopicForm forumTopicForm, ForumTopicForm sessionAudienceForm, String formAudienceType )
  {
    try
    {
      BeanUtils.copyProperties( forumTopicForm, sessionAudienceForm );
    }
    catch( Exception e )
    {
      logger.info( "returnCategoryLookup: Copy Properties failed." );
    }
    Long audienceId = null;
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      audienceId = (Long)clientStateMap.get( "audienceId" );

    }
    catch( InvalidClientStateException e )
    {
      // do nothing since this is an option parameter
    }

    if ( audienceId != null && audienceId.longValue() != 0 )
    {
      addAudience( forumTopicForm, request, formAudienceType, audienceId );
    }
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  private ForumTopicService getForumTopicService()
  {
    return (ForumTopicService)getService( ForumTopicService.BEAN_NAME );
  }

  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  /**
   * Gets an AudienceService
   * 
   * @return AudienceService
   */
  private ListBuilderService getListBuilderService()
  {
    return (ListBuilderService)getService( ListBuilderService.BEAN_NAME );
  }

}
