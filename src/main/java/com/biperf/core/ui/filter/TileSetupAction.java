
package com.biperf.core.ui.filter;

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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.ModuleAppAudienceType;
import com.biperf.core.domain.homepage.ModuleApp;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.promotion.PromotionAudienceFormBean;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

public class TileSetupAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( TileSetupAction.class );

  public static final String SESSION_TILE_SETUP_FORM = "sessionTileSetupForm";

  /**
   * Dispatcher.  Default to home page display.  Too much work to append 'method=display'
   * to all the paths that lead to the home page.  
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  public ActionForward addAudience( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    TileSetupForm setupForm = (TileSetupForm)actionForm;

    // If there was no audience selected, then return an error
    if ( setupForm.getPrimaryAudienceId() == null || setupForm.getPrimaryAudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );
      request.setAttribute( "showAudience", new Boolean( true ) );
      return actionMapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    Long audienceId = new Long( setupForm.getPrimaryAudienceId() );
    setupForm.setMethod( "save" );
    addAudienceToForm( audienceId, setupForm );
    request.setAttribute( "showAudience", new Boolean( true ) );
    return actionMapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void addAudienceToForm( Long audienceId, TileSetupForm setupForm )
  {
    // get the audience object
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    PromotionAudienceFormBean audienceFormBean = new PromotionAudienceFormBean();
    audienceFormBean.setAudienceId( audience.getId() );
    audienceFormBean.setName( audience.getName() );
    audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceFormBean.setSize( audience.getSize() );

    setupForm.getPrimaryAudienceListAsList().add( audienceFormBean );
  }

  public ActionForward populateTile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    TileSetupForm setupForm = (TileSetupForm)form;
    ModuleApp app = getFilterAppSetupService().getModuleAppById( new Long( setupForm.getModuleappId() ) );
    setupForm.setTileName( app.getName() );
    setupForm.setModuleappId( app.getId().toString() );
    setupForm.setPrimaryAudienceType( app.getAudienceType().getCode().toLowerCase() );

    if ( setupForm.getPrimaryAudienceType().equals( ModuleAppAudienceType.SPECIFY_AUDIENCE_CODE ) )
    {

      if ( !app.getAudiences().isEmpty() )
      {
        Set<Audience> audienceSet = app.getAudiences();
        for ( Audience audience : audienceSet )
        {
          PromotionAudienceFormBean audienceFormBean = new PromotionAudienceFormBean();
          audienceFormBean.setAudienceId( audience.getId() );
          audienceFormBean.setName( audience.getName() );
          audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
          audienceFormBean.setSize( audience.getSize() );
          setupForm.getPrimaryAudienceListAsList().add( audienceFormBean );
        }
      }
    }
    setupForm.setMethod( "save" );
    request.setAttribute( "showAudience", new Boolean( true ) );
    return forward;
  }

  public ActionForward removeAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    TileSetupForm setupForm = (TileSetupForm)form;
    for ( Iterator iter = setupForm.getPrimaryAudienceListAsList().iterator(); iter.hasNext(); )
    {
      PromotionAudienceFormBean audienceFormBean = (PromotionAudienceFormBean)iter.next();
      if ( audienceFormBean.isRemoved() )
      {
        iter.remove();
      }
    }
    setupForm.setMethod( "save" );
    request.setAttribute( "showAudience", new Boolean( true ) );
    return forward;
  }

  public ActionForward prepareAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    TileSetupForm setupForm = (TileSetupForm)form;

    ActionForward returnForward = mapping.findForward( "audienceLookup" );

    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { "method=returnAudienceLookup" } );

    String queryString = "saveAudienceReturnUrl=" + returnUrl;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_TILE_SETUP_FORM, setupForm );

    ActionForward forward = ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString } );

    return forward;
  }

  public ActionForward returnAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    TileSetupForm setupForm = (TileSetupForm)form;

    // Get the form back out of the Session to redisplay.
    TileSetupForm sessionsetupForm = (TileSetupForm)request.getSession().getAttribute( SESSION_TILE_SETUP_FORM );

    if ( sessionsetupForm != null )
    {
      try
      {
        BeanUtils.copyProperties( setupForm, sessionsetupForm );
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
            audienceId = (Long)clientStateMap.get( "audienceId" );
          }
          catch( ClassCastException cce )
          {
            String id = (String)clientStateMap.get( "audienceId" );
            if ( id != null && id.length() > 0 )
            {
              audienceId = new Long( id );
            }
          }
        }
        catch( InvalidClientStateException e )
        {
          throw new IllegalArgumentException( "request parameter clientState was missing" );
        }

        if ( audienceId != null )
        {
          addAudienceToForm( audienceId, setupForm );
        }
      }
      catch( Exception e )
      {
        logger.error( "returnAudienceLookup: Copy Properties failed." );
      }
    }
    request.setAttribute( "showAudience", new Boolean( true ) );
    request.getSession().removeAttribute( SESSION_TILE_SETUP_FORM );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD );
    TileSetupForm setupForm = (TileSetupForm)form;
    ActionMessages errors = new ActionMessages();
    if ( isCancelled( request ) )
    {
      forward = mapping.findForward( ActionConstants.CANCEL_FORWARD );
      return forward;
    }

    errors = setupForm.validate( mapping, request );
    if ( errors.isEmpty() )
    {
      try
      {
        ModuleApp moduleApp = getFilterAppSetupService().getModuleAppById( new Long( setupForm.getModuleappId() ) );
        moduleApp.setAudienceType( ModuleAppAudienceType.lookup( setupForm.getPrimaryAudienceType() ) );
        moduleApp.setName( setupForm.getTileName() );
        Set<Audience> audienceSet = new LinkedHashSet<Audience>();
        if ( setupForm.getPrimaryAudienceType().equals( ModuleAppAudienceType.SPECIFY_AUDIENCE_CODE ) )
        {
          List audiences = setupForm.getPrimaryAudienceListAsList();
          if ( audiences != null )
          {
            for ( Iterator iter = audiences.iterator(); iter.hasNext(); )
            {
              PromotionAudienceFormBean bean = (PromotionAudienceFormBean)iter.next();
              Audience audience = getAudienceService().getAudienceById( bean.getAudienceId(), null );
              audienceSet.add( audience );
            }
          }
        }
        moduleApp.setAudiences( audienceSet );
        getFilterAppSetupService().save( moduleApp );
      }
      catch( ServiceErrorException se )
      {
        List serviceErrors = se.getServiceErrors();
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );

      }
    }
    return forward;

  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  public boolean isModuleInstalled( String module )
  {
    return getSystemVariableService().getPropertyByName( module ).getBooleanVal();
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private FilterAppSetupService getFilterAppSetupService()
  {
    return (FilterAppSetupService)getService( FilterAppSetupService.BEAN_NAME );
  }

}
