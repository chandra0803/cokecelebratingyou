/**
 * 
 */

package com.biperf.core.ui.instantpoll;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.promotion.InstantPoll;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.instantpoll.InstantPollService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.survey.SurveyAssociationRequest;
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
import com.biperf.core.value.instantpoll.InstantPollAudienceFormBean;

/**
 * @author poddutur
 *
 */
public class InstantPollMaintainAction extends BaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( InstantPollMaintainAction.class );

  public static final String INSTANT_POLL_FORM = "instantPollForm";

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
    InstantPollForm instantPollForm = (InstantPollForm)form;

    instantPollForm.loadDefaultParameters();

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
    request.getSession().removeAttribute( "instantPollsList" );

    if ( !isTokenValid( request, true ) )
    {
      errors.add( "tokenFailure", new ActionMessage( "system.errors.TOKEN_EXCEPTION" ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_CREATE ); // EARLY EXIT
    }

    try
    {
      InstantPollForm instantPollForm = (InstantPollForm)form;
      List<InstantPollAudienceFormBean> primaryAudienceList = new ArrayList<InstantPollAudienceFormBean>();
      primaryAudienceList = instantPollForm.getPrimaryAudienceListAsList();
      List<String> answersList = populateInstantPollAnswers( instantPollForm );
      InstantPoll instantPoll = getInstantPollService().saveInstantPoll( instantPollForm.toDomainObject( null ),
                                                                         instantPollForm.toDomainSurveyQuestionObject( null ),
                                                                         instantPollForm.toDomainInstantPollAudienceSet(),
                                                                         answersList,
                                                                         instantPollForm.getQuestion() );
      request.setAttribute( "instantPollForm", instantPollForm );
      request.setAttribute( "instantPollForm", instantPoll );
      request.setAttribute( "primaryAudienceList", primaryAudienceList );
      actionForward = actionMapping.findForward( ActionConstants.SUCCESS_CREATE );
    }
    catch( Exception e )
    {
      log.debug( e );
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "instantpoll.library.CREATE_INSTANTPOLL_ERROR" ) );
      saveErrors( request, errors );
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
    InstantPollForm instantPollForm = (InstantPollForm)form;
    List<InstantPollAudienceFormBean> primaryAudienceList = new ArrayList<InstantPollAudienceFormBean>();

    Long instantPollId = instantPollForm.getId();
    InstantPoll instantPoll = getInstantPollService().getInstantPollById( instantPollId );
    List surveyQuestionresponseList = getInstantPollService().getSurveyQuestionResponseByInstantPollId( instantPollId );
    instantPollForm.loadDomainObject( instantPoll, surveyQuestionresponseList );
    String audienceType = instantPollForm.getAudienceType();
    if ( !audienceType.equals( "allactivepaxaudience" ) )
    {
      primaryAudienceList = getInstantPollService().getAudienceByInstantPollId( instantPollId );
      instantPollForm.loadDomainAudienceObject( primaryAudienceList );
    }
    request.setAttribute( "instantPoll", instantPoll );
    request.setAttribute( "instantPollForm", instantPollForm );

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
    ActionForward actionForward = actionMapping.findForward( ActionConstants.SUCCESS_CREATE );

    InstantPollForm instantPollForm = (InstantPollForm)actionForm;

    ActionMessages errors = new ActionMessages();
    if ( !isTokenValid( request, true ) )
    {
      errors.add( "tokenFailure", new ActionMessage( "system.errors.TOKEN_EXCEPTION" ) );
      saveErrors( request, errors );
      return actionMapping.findForward( ActionConstants.FAIL_UPDATE ); // EARLY
      // EXIT
    }
    Long instantPollId = instantPollForm.getId();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SurveyAssociationRequest( SurveyAssociationRequest.ALL ) );
    InstantPoll instantPoll = getInstantPollService().getInstantPollByIdWithAssociations( new Long( instantPollId ), associationRequestCollection );

    if ( instantPollForm.isDisabledFlag() )
    {
      instantPoll = getInstantPollService().saveInstantPoll( instantPollForm.toDomainObject( instantPoll ), instantPollForm.toDomainInstantPollAudienceSet() );
    }
    else
    {
      List<String> answersList = populateInstantPollAnswers( instantPollForm );
      SurveyQuestion surveyQuestion = instantPoll.getSurveyQuestions().get( 0 );
      instantPoll = getInstantPollService().saveInstantPoll( instantPollForm.toDomainObject( instantPoll ),
                                                             instantPollForm.toDomainSurveyQuestionObject( surveyQuestion ),
                                                             instantPollForm.toDomainInstantPollAudienceSet(),
                                                             answersList,
                                                             instantPollForm.getQuestion() );
    }
    return actionForward;
  }

  public ActionForward addSubmitterAudience( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    InstantPollForm instantPollForm = (InstantPollForm)form;

    // If there was no audience selected, then return an error
    if ( instantPollForm.getPrimaryAudienceId() == null || instantPollForm.getPrimaryAudienceId().length() == 0 )
    {
      ActionMessages errors = new ActionMessages();
      errors.add( ActionConstants.ERROR_MESSAGE_PROPERTY, new ActionMessage( "promotion.errors.NO_AUDIENCE" ) );
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    addAudience( instantPollForm, request, InstantPollForm.PRIMARY, new Long( instantPollForm.getPrimaryAudienceId() ) );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private void addAudience( InstantPollForm form, HttpServletRequest request, String audienceType, Long audienceId )
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

    InstantPollForm instantPollForm = (InstantPollForm)form;

    List<InstantPollAudienceFormBean> primaryAudienceList = new ArrayList<InstantPollAudienceFormBean>();
    primaryAudienceList.addAll( instantPollForm.getPrimaryAudienceListAsList() );

    // Remove audiences from the primary audience list.
    instantPollForm.removeItems( InstantPollForm.PRIMARY );

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
    InstantPollForm instantPollForm = (InstantPollForm)form;

    ActionForward returnForward = mapping.findForward( "submitterAudienceLookup" );

    Map clientStateParameterMap = new HashMap();
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    String returnUrl = ActionUtils.getForwardUriWithParameters( request, returnForward, new String[] { queryString } );
    Map parmMap = new HashMap();
    parmMap.put( "saveAudienceReturnUrl", returnUrl );
    String queryString2 = "saveAudienceReturnUrl=" + returnUrl;
    queryString2 = ClientStateUtils.generateEncodedLink( "", "", parmMap );
    // Put the form in the session to be reloaded when coming back from node lookup.
    request.getSession().setAttribute( SESSION_AUDIENCE_FORM, instantPollForm );

    return ActionUtils.forwardWithParameters( mapping, "listbuilder", new String[] { queryString2 } );
  }

  public ActionForward returnSubmitterAudienceLookup( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    InstantPollForm instantPollForm = (InstantPollForm)form;

    // Get the form back out of the Session to redisplay.
    InstantPollForm sessionAudienceForm = (InstantPollForm)request.getSession().getAttribute( SESSION_AUDIENCE_FORM );

    if ( sessionAudienceForm != null )
    {
      String formAudienceType = InstantPollForm.PRIMARY;
      extractAndAddAudience( request, instantPollForm, sessionAudienceForm, formAudienceType );
    }

    request.getSession().removeAttribute( SESSION_AUDIENCE_FORM );
    request.getSession().removeAttribute( ListBuilderAction.SESSION_CRITERIA_AUDIENCE_VALUE );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private List<String> populateInstantPollAnswers( InstantPollForm instantPollForm )
  {
    List<String> answersList = new ArrayList<String>();

    if ( !instantPollForm.getAnswer1().isEmpty() )
    {
      answersList.add( instantPollForm.getAnswer1() );
    }
    if ( !instantPollForm.getAnswer2().isEmpty() )
    {
      answersList.add( instantPollForm.getAnswer2() );
    }
    if ( !instantPollForm.getAnswer3().isEmpty() )
    {
      answersList.add( instantPollForm.getAnswer3() );
    }
    if ( !instantPollForm.getAnswer4().isEmpty() )
    {
      answersList.add( instantPollForm.getAnswer4() );
    }
    if ( !instantPollForm.getAnswer5().isEmpty() )
    {
      answersList.add( instantPollForm.getAnswer5() );
    }
    return answersList;
  }

  private List getPaxsInCriteriaAudience( Audience audience )
  {
    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    Long primaryHierarchyId = primaryHierarchy.getId();

    Set audiences = new LinkedHashSet();
    audiences.add( audience );
    return getListBuilderService().searchParticipants( audiences, primaryHierarchyId, false, null, true );
  }

  private void extractAndAddAudience( HttpServletRequest request, InstantPollForm instantPollForm, InstantPollForm sessionAudienceForm, String formAudienceType )
  {
    try
    {
      BeanUtils.copyProperties( instantPollForm, sessionAudienceForm );
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
      addAudience( instantPollForm, request, formAudienceType, audienceId );
    }
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
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

  private InstantPollService getInstantPollService()
  {
    return (InstantPollService)getService( InstantPollService.BEAN_NAME );
  }

}
