
package com.biperf.core.ui.survey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.InvalidClientStateException;

public class SurveyQuestionResponseAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( SurveyQuestionResponseAction.class );

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "display";
    logger.debug( ">>> " + METHOD_NAME );

    SurveyQuestionResponseForm surveyQuestionResponseForm = (SurveyQuestionResponseForm)form;

    String forward = ActionConstants.SUCCESS_FORWARD;

    String surveyFormId = null;
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
        surveyFormId = (String)clientStateMap.get( "surveyFormId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "surveyFormId" );
        surveyFormId = id.toString();
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    if ( surveyFormId != null )
    {
      Survey survey = getSurveyService().getSurveyById( new Long( surveyFormId ) );

      if ( survey.getPromotionModuleType().isSurveyPromotion() )
      {
        surveyQuestionResponseForm.setSurveyPromotion( true );
      }
    }

    surveyQuestionResponseForm.setMethod( "save" );
    Long surveyQuestionResponseId = surveyQuestionResponseForm.getSurveyQuestionResponseId();

    if ( surveyQuestionResponseId != null && surveyQuestionResponseId.longValue() > 0 )
    {
      surveyQuestionResponseForm.load( getSurveyService().getSurveyQuestionResponseById( surveyQuestionResponseId ) );
    }
    logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "save";

    String forwardAction = ActionConstants.SUCCESS_FORWARD;

    ActionMessages errors = new ActionMessages();
    SurveyQuestionResponseForm surveyQuestionResponseForm = (SurveyQuestionResponseForm)form;

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      forwardAction = ActionConstants.CANCEL_FORWARD;
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "surveyQuestionId", surveyQuestionResponseForm.getSurveyQuestionId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( mapping, forwardAction, new String[] { queryString, "method=display" } );
    }

    SurveyQuestionResponse questionresponse = null;
    try
    {
      questionresponse = getSurveyService().saveSurveyQuestionResponse( surveyQuestionResponseForm.getSurveyQuestionId(),
                                                                        surveyQuestionResponseForm.toDomainObject(),
                                                                        surveyQuestionResponseForm.getSurveyQuestionResponseText() );
      surveyQuestionResponseForm.setSurveyQuestionResponseId( questionresponse.getId() );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardAction = ActionConstants.FAIL_FORWARD;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "surveyQuestionId", surveyQuestionResponseForm.getSurveyQuestionId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, forwardAction, new String[] { queryString, "method=display" } );
  } // end save

  private SurveyService getSurveyService()
  {
    return (SurveyService)getService( SurveyService.BEAN_NAME );
  }

  public ActionForward saveAndAddAnother( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    this.save( mapping, form, request, response );
    SurveyQuestionResponseForm surveyQuestionResponseForm = (SurveyQuestionResponseForm)form;
    surveyQuestionResponseForm.clearForNewQuestionResponse();

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "surveyQuestionId", surveyQuestionResponseForm.getSurveyQuestionId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_CREATE, new String[] { queryString, "method=display" } );
  }
}
