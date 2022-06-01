
package com.biperf.core.ui.diycommunication;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceRole;
import com.biperf.core.domain.user.Role;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.promotion.PromotionAudienceFormBean;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

public class DIYCommunicationsAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( DIYCommunicationsAction.class );

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( mapping, actionForm, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;

    List<Audience> bannerAudienceList = getAudienceService().getAudienceListFromAudienceRole( diyForm.getBannerAudienceName() );
    if ( !bannerAudienceList.isEmpty() )
    {
      diyForm.setDiyBannersAvailable( true );
    }
    else
    {
      diyForm.setDiyBannersAvailable( false );
    }
    diyForm.setDiyBannersAudienceListAsList( addAudience( bannerAudienceList, diyForm ) );

    List<Audience> newsStoriesAudienceList = getAudienceService().getAudienceListFromAudienceRole( diyForm.getNewsAudienceName() );
    if ( !newsStoriesAudienceList.isEmpty() )
    {
      diyForm.setDiyNewsStoriesAvailable( true );
    }
    else
    {
      diyForm.setDiyNewsStoriesAvailable( false );
    }
    diyForm.setDiyNewsStoriesAudienceListAsList( addAudience( newsStoriesAudienceList, diyForm ) );

    List<Audience> resourceCenterAudienceList = getAudienceService().getAudienceListFromAudienceRole( diyForm.getResourceAudienceName() );
    if ( !resourceCenterAudienceList.isEmpty() )
    {
      diyForm.setDiyResourceCenterAvailable( true );
    }
    else
    {
      diyForm.setDiyResourceCenterAvailable( false );
    }
    diyForm.setDiyResourceCenterAudienceListAsList( addAudience( resourceCenterAudienceList, diyForm ) );

    List<Audience> tipsAudienceList = getAudienceService().getAudienceListFromAudienceRole( diyForm.getTipsAudienceName() );
    if ( !tipsAudienceList.isEmpty() )
    {
      diyForm.setDiyTipsAvailable( true );
    }
    else
    {
      diyForm.setDiyTipsAvailable( false );
    }
    diyForm.setDiyTipsAudienceListAsList( addAudience( tipsAudienceList, diyForm ) );

    return mapping.findForward( forwardTo );
  }

  public ActionForward cancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;

    getAudienceService().deleteAllAudienceRoles();

    if ( diyForm.isDiyBannersAvailable() )
    {
      Role bannerAudienceRole = getRoleService().getRoleByCode( diyForm.getBannerAudienceName() );

      Iterator bannerAudienceIterator = diyForm.getDiyBannersAudienceListAsList().iterator();
      while ( bannerAudienceIterator.hasNext() )
      {
        PromotionAudienceFormBean formBean = (PromotionAudienceFormBean)bannerAudienceIterator.next();
        Audience audience = getAudienceService().getAudienceById( formBean.getAudienceId(), null );

        AudienceRole audienceRole = new AudienceRole();

        audienceRole.setAudience( audience );
        audienceRole.setRole( bannerAudienceRole );
        getAudienceService().updateAudienceRole( audienceRole );
      }
    }

    if ( diyForm.isDiyNewsStoriesAvailable() )
    {
      Role newsStoriesAudienceRole = getRoleService().getRoleByCode( diyForm.getNewsAudienceName() );

      Iterator newsAudienceIterator = diyForm.getDiyNewsStoriesAudienceListAsList().iterator();
      while ( newsAudienceIterator.hasNext() )
      {
        PromotionAudienceFormBean formBean = (PromotionAudienceFormBean)newsAudienceIterator.next();
        Audience audience = getAudienceService().getAudienceById( formBean.getAudienceId(), null );

        AudienceRole audienceRole = new AudienceRole();
        audienceRole.setAudience( audience );
        audienceRole.setRole( newsStoriesAudienceRole );
        getAudienceService().updateAudienceRole( audienceRole );
      }
    }

    if ( diyForm.isDiyResourceCenterAvailable() )
    {
      Role resourceCenterAudienceRole = getRoleService().getRoleByCode( diyForm.getResourceAudienceName() );

      Iterator resourceCenterAudienceIterator = diyForm.getDiyResourceCenterAudienceListAsList().iterator();
      while ( resourceCenterAudienceIterator.hasNext() )
      {
        PromotionAudienceFormBean formBean = (PromotionAudienceFormBean)resourceCenterAudienceIterator.next();
        Audience audience = getAudienceService().getAudienceById( formBean.getAudienceId(), null );

        AudienceRole audienceRole = new AudienceRole();
        audienceRole.setAudience( audience );
        audienceRole.setRole( resourceCenterAudienceRole );
        getAudienceService().updateAudienceRole( audienceRole );
      }
    }

    if ( diyForm.isDiyTipsAvailable() )
    {
      Role tipsAudienceRole = getRoleService().getRoleByCode( diyForm.getTipsAudienceName() );

      Iterator tipsAudienceIterator = diyForm.getDiyTipsAudienceListAsList().iterator();
      while ( tipsAudienceIterator.hasNext() )
      {
        PromotionAudienceFormBean formBean = (PromotionAudienceFormBean)tipsAudienceIterator.next();
        Audience audience = getAudienceService().getAudienceById( formBean.getAudienceId(), null );

        AudienceRole audienceRole = new AudienceRole();
        audienceRole.setAudience( audience );
        audienceRole.setRole( tipsAudienceRole );
        getAudienceService().updateAudienceRole( audienceRole );
      }
    }

    request.setAttribute( "success", "Successfully Saved" );

    return mapping.findForward( forwardTo );
  }

  public ActionForward returnBannersAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;
    // Get the diyCommunicationsForm from the session as it was placed there before we called out to
    // build
    // the audience.
    DIYCommunicationsForm sessionDIYForm = (DIYCommunicationsForm)request.getSession().getAttribute( "sessiondiyCommunicationsForm" );

    if ( sessionDIYForm != null )
    {
      try
      {
        BeanUtils.copyProperties( diyForm, sessionDIYForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }

    // Get the newly created audienceId from the request.
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
        String audienceIdString = (String)clientStateMap.get( "audienceId" );
        if ( audienceIdString != null && !audienceIdString.equals( "" ) )
        {
          audienceId = new Long( audienceIdString );
        }
      }
      catch( ClassCastException cce )
      {
        audienceId = (Long)clientStateMap.get( "audienceId" );
      }

    }
    catch( InvalidClientStateException e )
    {
      // audienceId is optional - do nothing
      // throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( audienceId != null && audienceId.longValue() != 0 )
    {
      addDIYBannersAudience( audienceId, diyForm );
    }

    return forward;
  }

  public ActionForward addBannersAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;

    // If there was no audience selected, then return an error
    if ( diyForm.getDiyBannersAudienceId() == null || diyForm.getDiyBannersAudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );

      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    Long audienceId = new Long( diyForm.getDiyBannersAudienceId() );

    addDIYBannersAudience( audienceId, diyForm );
    diyForm.setMethod( "success" );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void addDIYBannersAudience( Long audienceId, DIYCommunicationsForm diyForm )
  {
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    PromotionAudienceFormBean audienceFormBean = new PromotionAudienceFormBean();
    audienceFormBean.setAudienceId( audience.getId() );
    audienceFormBean.setName( audience.getName() );
    audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceFormBean.setSize( audience.getSize() );

    diyForm.getDiyBannersAudienceListAsList().add( audienceFormBean );
  }

  public ActionForward prepareBannersAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;

    ActionForward returnForward = mapping.findForward( "bannersAudienceLookup" );

    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { "method=returnBannersAudienceLookup" } );

    String queryString = "saveAudienceReturnUrl=" + returnUrl;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( "sessiondiyCommunicationsForm", diyForm );

    ActionForward forward = ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString } );

    return forward;
  }

  public ActionForward removeBannersAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( "success" );

    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;

    for ( Iterator iter = diyForm.getDiyBannersAudienceListAsList().iterator(); iter.hasNext(); )
    {
      PromotionAudienceFormBean audienceFormBean = (PromotionAudienceFormBean)iter.next();
      if ( audienceFormBean.isRemoved() )
      {
        iter.remove();
      }
    }
    return forward;
  }

  public ActionForward returnNewsStoriesAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;
    // Get the diyCommunicationsForm from the session as it was placed there before we called out to
    // build
    // the audience.
    DIYCommunicationsForm sessionDIYForm = (DIYCommunicationsForm)request.getSession().getAttribute( "sessiondiyCommunicationsForm" );

    if ( sessionDIYForm != null )
    {
      try
      {
        BeanUtils.copyProperties( diyForm, sessionDIYForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }

    // Get the newly created audienceId from the request.
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
        String audienceIdString = (String)clientStateMap.get( "audienceId" );
        if ( audienceIdString != null && !audienceIdString.equals( "" ) )
        {
          audienceId = new Long( audienceIdString );
        }
      }
      catch( ClassCastException cce )
      {
        audienceId = (Long)clientStateMap.get( "audienceId" );
      }

    }
    catch( InvalidClientStateException e )
    {
      // audienceId is optional - do nothing
      // throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( audienceId != null && audienceId.longValue() != 0 )
    {
      addDIYNewsStoriesAudience( audienceId, diyForm );
    }

    return forward;
  }

  public ActionForward addNewsStoriesAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;

    // If there was no audience selected, then return an error
    if ( diyForm.getDiyNewsStoriesAudienceId() == null || diyForm.getDiyNewsStoriesAudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );

      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    Long audienceId = new Long( diyForm.getDiyNewsStoriesAudienceId() );

    addDIYNewsStoriesAudience( audienceId, diyForm );
    diyForm.setMethod( "success" );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void addDIYNewsStoriesAudience( Long audienceId, DIYCommunicationsForm diyForm )
  {
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    PromotionAudienceFormBean audienceFormBean = new PromotionAudienceFormBean();
    audienceFormBean.setAudienceId( audience.getId() );
    audienceFormBean.setName( audience.getName() );
    audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceFormBean.setSize( audience.getSize() );

    diyForm.getDiyNewsStoriesAudienceListAsList().add( audienceFormBean );
  }

  public ActionForward prepareNewsStoriesAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;

    ActionForward returnForward = mapping.findForward( "newsStoriesAudienceLookup" );

    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { "method=returnNewsStoriesAudienceLookup" } );

    String queryString = "saveAudienceReturnUrl=" + returnUrl;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( "sessiondiyCommunicationsForm", diyForm );

    ActionForward forward = ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString } );

    return forward;
  }

  public ActionForward removeNewsStoriesAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( "success" );

    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;

    for ( Iterator iter = diyForm.getDiyNewsStoriesAudienceListAsList().iterator(); iter.hasNext(); )
    {
      PromotionAudienceFormBean audienceFormBean = (PromotionAudienceFormBean)iter.next();
      if ( audienceFormBean.isRemoved() )
      {
        iter.remove();
      }
    }
    return forward;
  }

  public ActionForward returnResourceCenterAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;
    // Get the diyCommunicationsForm from the session as it was placed there before we called out to
    // build
    // the audience.
    DIYCommunicationsForm sessionDIYForm = (DIYCommunicationsForm)request.getSession().getAttribute( "sessiondiyCommunicationsForm" );

    if ( sessionDIYForm != null )
    {
      try
      {
        BeanUtils.copyProperties( diyForm, sessionDIYForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }

    // Get the newly created audienceId from the request.
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
        String audienceIdString = (String)clientStateMap.get( "audienceId" );
        if ( audienceIdString != null && !audienceIdString.equals( "" ) )
        {
          audienceId = new Long( audienceIdString );
        }
      }
      catch( ClassCastException cce )
      {
        audienceId = (Long)clientStateMap.get( "audienceId" );
      }

    }
    catch( InvalidClientStateException e )
    {
      // audienceId is optional - do nothing
      // throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( audienceId != null && audienceId.longValue() != 0 )
    {
      addDIYResourceCenterAudience( audienceId, diyForm );
    }

    return forward;
  }

  public ActionForward addResourceCenterAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;

    // If there was no audience selected, then return an error
    if ( diyForm.getDiyResourceCenterAudienceId() == null || diyForm.getDiyResourceCenterAudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );

      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    Long audienceId = new Long( diyForm.getDiyResourceCenterAudienceId() );

    addDIYResourceCenterAudience( audienceId, diyForm );
    diyForm.setMethod( "success" );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void addDIYResourceCenterAudience( Long audienceId, DIYCommunicationsForm diyForm )
  {
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    PromotionAudienceFormBean audienceFormBean = new PromotionAudienceFormBean();
    audienceFormBean.setAudienceId( audience.getId() );
    audienceFormBean.setName( audience.getName() );
    audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceFormBean.setSize( audience.getSize() );

    diyForm.getDiyResourceCenterAudienceListAsList().add( audienceFormBean );
  }

  public ActionForward prepareResourceCenterAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;

    ActionForward returnForward = mapping.findForward( "resourceCenterAudienceLookup" );

    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { "method=returnResourceCenterAudienceLookup" } );

    String queryString = "saveAudienceReturnUrl=" + returnUrl;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( "sessiondiyCommunicationsForm", diyForm );

    ActionForward forward = ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString } );

    return forward;
  }

  public ActionForward removeResourceCenterAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( "success" );

    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;

    for ( Iterator iter = diyForm.getDiyResourceCenterAudienceListAsList().iterator(); iter.hasNext(); )
    {
      PromotionAudienceFormBean audienceFormBean = (PromotionAudienceFormBean)iter.next();
      if ( audienceFormBean.isRemoved() )
      {
        iter.remove();
      }
    }
    return forward;
  }

  public ActionForward returnTipsAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;
    // Get the diyCommunicationsForm from the session as it was placed there before we called out to
    // build
    // the audience.
    DIYCommunicationsForm sessionDIYForm = (DIYCommunicationsForm)request.getSession().getAttribute( "sessiondiyCommunicationsForm" );

    if ( sessionDIYForm != null )
    {
      try
      {
        BeanUtils.copyProperties( diyForm, sessionDIYForm );
      }
      catch( Exception e )
      {
        logger.info( "Copy Properties failed." );
      }
    }

    // Get the newly created audienceId from the request.
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
        String audienceIdString = (String)clientStateMap.get( "audienceId" );
        if ( audienceIdString != null && !audienceIdString.equals( "" ) )
        {
          audienceId = new Long( audienceIdString );
        }
      }
      catch( ClassCastException cce )
      {
        audienceId = (Long)clientStateMap.get( "audienceId" );
      }

    }
    catch( InvalidClientStateException e )
    {
      // audienceId is optional - do nothing
      // throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    if ( audienceId != null && audienceId.longValue() != 0 )
    {
      addDIYTipsAudience( audienceId, diyForm );
    }

    return forward;
  }

  public ActionForward addTipsAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;

    // If there was no audience selected, then return an error
    if ( diyForm.getDiyTipsAudienceId() == null || diyForm.getDiyTipsAudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );

      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    Long audienceId = new Long( diyForm.getDiyTipsAudienceId() );

    addDIYTipsAudience( audienceId, diyForm );
    diyForm.setMethod( "success" );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void addDIYTipsAudience( Long audienceId, DIYCommunicationsForm diyForm )
  {
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    PromotionAudienceFormBean audienceFormBean = new PromotionAudienceFormBean();
    audienceFormBean.setAudienceId( audience.getId() );
    audienceFormBean.setName( audience.getName() );
    audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceFormBean.setSize( audience.getSize() );

    diyForm.getDiyTipsAudienceListAsList().add( audienceFormBean );
  }

  public ActionForward prepareTipsAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;

    ActionForward returnForward = mapping.findForward( "tipsAudienceLookup" );

    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { "method=returnTipsAudienceLookup" } );

    String queryString = "saveAudienceReturnUrl=" + returnUrl;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( "sessiondiyCommunicationsForm", diyForm );

    ActionForward forward = ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString } );

    return forward;
  }

  public ActionForward removeTipsAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( "success" );

    DIYCommunicationsForm diyForm = (DIYCommunicationsForm)form;

    for ( Iterator iter = diyForm.getDiyTipsAudienceListAsList().iterator(); iter.hasNext(); )
    {
      PromotionAudienceFormBean audienceFormBean = (PromotionAudienceFormBean)iter.next();
      if ( audienceFormBean.isRemoved() )
      {
        iter.remove();
      }
    }
    return forward;
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private RoleService getRoleService()
  {
    return (RoleService)getService( RoleService.BEAN_NAME );
  }

  private List<PromotionAudienceFormBean> addAudience( List<Audience> audienceList, DIYCommunicationsForm diyForm )
  {
    List<PromotionAudienceFormBean> audienceBeanList = new ArrayList<PromotionAudienceFormBean>();
    for ( Iterator audienceIter = audienceList.iterator(); audienceIter.hasNext(); )
    {
      Audience audience = (Audience)audienceIter.next();

      PromotionAudienceFormBean audienceFormBean = new PromotionAudienceFormBean();
      audienceFormBean.setAudienceId( audience.getId() );
      audienceFormBean.setName( audience.getName() );
      audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
      audienceFormBean.setSize( audience.getSize() );

      audienceBeanList.add( audienceFormBean );
    }
    return audienceBeanList;

  }

}
