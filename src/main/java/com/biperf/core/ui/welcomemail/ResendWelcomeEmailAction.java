
package com.biperf.core.ui.welcomemail;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
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

import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.process.ResendWelcomeEmailProcess;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.promotion.PromotionAudienceFormBean;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;

public class ResendWelcomeEmailAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( ResendWelcomeEmailAction.class );

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( "display" );
  }

  public ActionForward addAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ResendWelcomeEmailForm resendWelcomeEmailForm = (ResendWelcomeEmailForm)form;

    Long audienceId = new Long( resendWelcomeEmailForm.getSelectedAudienceId() );
    addAudienceToForm( audienceId, resendWelcomeEmailForm );

    return mapping.findForward( "display" );
  }

  public ActionForward removeAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ResendWelcomeEmailForm resendWelcomeEmailForm = (ResendWelcomeEmailForm)form;

    for ( Iterator<PromotionAudienceFormBean> iter = resendWelcomeEmailForm.getAudienceList().iterator(); iter.hasNext(); )
    {
      PromotionAudienceFormBean audienceFormBean = (PromotionAudienceFormBean)iter.next();
      if ( audienceFormBean.isRemoved() )
      {
        iter.remove();
      }
    }

    return mapping.findForward( "display" );
  }

  public ActionForward prepareAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ResendWelcomeEmailForm resendWelcomeEmailForm = (ResendWelcomeEmailForm)form;

    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( ResendWelcomeEmailForm.FORM_NAME, resendWelcomeEmailForm );

    ActionForward returnForward = mapping.findForward( "audience_lookup_return" );
    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { "method=returnAudienceLookup" } );
    String queryString = "saveAudienceReturnUrl=" + returnUrl;
    return ActionUtils.forwardWithParameters( mapping, "list_builder", new String[] { queryString } );
  }

  public ActionForward returnAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ResendWelcomeEmailForm resendWelcomeEmailForm = (ResendWelcomeEmailForm)form;

    // Get the form back out of the Session to redisplay.
    ResendWelcomeEmailForm sessionResendWelcomeEmailForm = (ResendWelcomeEmailForm)request.getSession().getAttribute( ResendWelcomeEmailForm.FORM_NAME );
    if ( sessionResendWelcomeEmailForm != null )
    {
      try
      {
        BeanUtils.copyProperties( resendWelcomeEmailForm, sessionResendWelcomeEmailForm );
        Long audienceId = getLongValueFromClientState( request );
        if ( audienceId != null )
        {
          addAudienceToForm( audienceId, resendWelcomeEmailForm );
        }
      }
      catch( Exception e )
      {
        logger.error( e );
      }
    }

    // Remove form from session
    request.getSession().removeAttribute( ResendWelcomeEmailForm.FORM_NAME );

    return mapping.findForward( "display" );
  }

  public ActionForward count( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ResendWelcomeEmailForm resendWelcomeEmailForm = (ResendWelcomeEmailForm)form;

    launchResendWelcomeEmailProcess( true, resendWelcomeEmailForm );

    resendWelcomeEmailForm.setCountSuccess( true );

    return mapping.findForward( "display" );
  }

  public ActionForward send( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ResendWelcomeEmailForm resendWelcomeEmailForm = (ResendWelcomeEmailForm)form;

    launchResendWelcomeEmailProcess( false, resendWelcomeEmailForm );

    resendWelcomeEmailForm.setMailSuccess( true );

    return mapping.findForward( "display" );
  }

  private String getAudienceIds( List<PromotionAudienceFormBean> audienceList )
  {
    StringBuilder audienceIds = new StringBuilder();
    boolean first = true;
    for ( PromotionAudienceFormBean audience : audienceList )
    {
      if ( first )
      {
        first = false;
      }
      else
      {
        audienceIds.append( "|" );
      }
      audienceIds.append( audience.getAudienceId() );
    }
    return audienceIds.toString();
  }

  private void launchResendWelcomeEmailProcess( boolean emailCount, ResendWelcomeEmailForm resendWelcomeEmailForm )
  {
    String audienceIds = null;
    String importFileId = null;
    String importFileDate = null;

    if ( ResendWelcomeEmailForm.OPTION_AUDIENCE_DEF.equals( resendWelcomeEmailForm.getOption() ) )
    {
      audienceIds = getAudienceIds( resendWelcomeEmailForm.getAudienceList() );
    }
    else if ( ResendWelcomeEmailForm.OPTION_FILE_LOAD_DATE.equals( resendWelcomeEmailForm.getOption() ) )
    {
      importFileDate = resendWelcomeEmailForm.getFileLoadDate();
    }
    else if ( ResendWelcomeEmailForm.OPTION_FILE_LOAD_ID.equals( resendWelcomeEmailForm.getOption() ) )
    {
      importFileId = resendWelcomeEmailForm.getFileLoadId();
    }

    // Launch process
    launchResendWelcomeEmailProcess( emailCount, audienceIds, importFileId, importFileDate );

  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private void launchResendWelcomeEmailProcess( boolean emailCount, String audienceIds, String importFileId, String importFileDate )
  {
    Process process = getProcessService().createOrLoadSystemProcess( ResendWelcomeEmailProcess.PROCESS_NAME, ResendWelcomeEmailProcess.BEAN_NAME );

    LinkedHashMap parameterValueMap = new LinkedHashMap();
    parameterValueMap.put( "emailCount", new String[] { String.valueOf( emailCount ) } );
    if ( null != audienceIds )
    {
      parameterValueMap.put( "audienceIdStr", new String[] { audienceIds } );
    }
    if ( null != importFileId )
    {
      parameterValueMap.put( "importFileId", new String[] { importFileId } );
    }
    if ( null != importFileDate )
    {
      parameterValueMap.put( "importFileDate", new String[] { importFileDate } );
    }

    ProcessSchedule processSchedule = new ProcessSchedule();
    processSchedule.setStartDate( new Date() );
    processSchedule.setTimeOfDayMillis( new Long( 0 ) );
    processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

    getProcessService().scheduleProcess( process, processSchedule, parameterValueMap, UserManager.getUserId() );
  }

  private void addAudienceToForm( Long audienceId, ResendWelcomeEmailForm resendWelcomeEmailForm )
  {
    // get the audience object
    Audience audience = getAudienceService().getAudienceById( audienceId, null );

    PromotionAudienceFormBean audienceFormBean = new PromotionAudienceFormBean();
    audienceFormBean.setAudienceId( audience.getId() );
    audienceFormBean.setName( audience.getName() );
    audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceFormBean.setSize( audience.getSize() );

    if ( audience instanceof CriteriaAudience )
    {
      audienceFormBean.setSize( getNbrOfPaxsInCriteriaAudience( (CriteriaAudience)audience ) );
    }
    resendWelcomeEmailForm.getAudienceList().add( audienceFormBean );
  }

  @SuppressWarnings( "rawtypes" )
  private Long getLongValueFromClientState( HttpServletRequest request )
  {
    Long value = null;
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
        value = (Long)clientStateMap.get( "audienceId" );
      }
      catch( ClassCastException cce )
      {
        String id = (String)clientStateMap.get( "audienceId" );
        if ( id != null && id.length() > 0 )
        {
          value = new Long( id );
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    return value;
  }

  private int getNbrOfPaxsInCriteriaAudience( Audience audience )
  {
    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();
    Set audiences = new LinkedHashSet();
    audiences.add( audience );
    return getListBuilderService().searchParticipants( audiences, primaryHierarchyId, true, null, true ).size();
  }

  private List getPaxsInCriteriaAudience( Audience audience )
  {
    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();
    Set audiences = new LinkedHashSet();
    audiences.add( audience );
    return getListBuilderService().searchParticipants( audiences, primaryHierarchyId, false, null, true );
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

  private HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  private ListBuilderService getListBuilderService()
  {
    return (ListBuilderService)getService( ListBuilderService.BEAN_NAME );
  }

}
