
package com.biperf.core.ui.survey;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.SurveyResponseType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.domain.survey.ParticipantSurvey;
import com.biperf.core.domain.survey.ParticipantSurveyResponse;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participantsurvey.ParticipantSurveyService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.survey.ParticipantSurveyAssociationRequest;
import com.biperf.core.service.survey.SurveyAssociationRequest;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;

public class ParticipantTakeSurveyAction extends BaseDispatchAction
{

  @SuppressWarnings( "unused" )
  private static final Log logger = LogFactory.getLog( ParticipantTakeSurveyAction.class );
  private static final String SAVE_FOR_LATER = "saveForLater";
  private static final String SUBMIT = "submit";

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    if ( mapping.getParameter() != null )
    {
      return super.execute( mapping, form, request, response );
    }
    else
    {
      return display( mapping, form, request, response );
    }
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward displayInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SurveyPageDetailsView surveyPageDetailsView = new SurveyPageDetailsView();

    Participant user = getParticipantService().getParticipantById( UserManager.getUserId() );

    ParticipantTakeSurveyForm participantTakeSurveyForm = (ParticipantTakeSurveyForm)form;
    buildPromotionInfo( participantTakeSurveyForm, request );
    Survey survey = getSurveyFromPromotion( request, participantTakeSurveyForm.getPromotion() );

    AssociationRequestCollection ascReqColl = new AssociationRequestCollection();
    ascReqColl.add( new ParticipantSurveyAssociationRequest( ParticipantSurveyAssociationRequest.ALL ) );
    ParticipantSurvey participantSurvey = getParticipantSurveyService().getParticipantSurveyByPromotionAndSurveyIdAndUserIdWithAssociations( new Long( participantTakeSurveyForm.getPromotionId() ),
                                                                                                                                             survey != null ? survey.getId() : null,
                                                                                                                                             user.getId(),
                                                                                                                                             ascReqColl );
    if ( survey != null )
    {
      SurveyPageTakeView surveyPageTakeView = new SurveyPageTakeView( survey, (Promotion)participantTakeSurveyForm.getPromotion(), user, participantSurvey );
      surveyPageDetailsView.setSurveyJson( surveyPageTakeView );
    }

    super.writeAsJsonToResponse( surveyPageDetailsView, response );
    return null;
  }

  // to display list of Survey Question and Responses.
  private Survey getSurveyFromPromotion( HttpServletRequest request, Promotion promotion )
  {
    List<Survey> surveyList = new ArrayList<Survey>();

    surveyList.add( ( (SurveyPromotion)promotion ).getSurvey() );

    if ( !surveyList.isEmpty() )
    {
      Survey randomSurvey = getRandomSurvey( surveyList );
      if ( randomSurvey != null )
      {
        AssociationRequestCollection assocReqs = new AssociationRequestCollection();
        assocReqs.add( new SurveyAssociationRequest( SurveyAssociationRequest.ALL ) );
        Survey rndSurvey = getSurveyService().getSurveyByIdWithAssociations( randomSurvey.getId(), assocReqs );
        return rndSurvey;
      }
    }

    return null;
  }

  public ActionForward saveForLater( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    saveParticipantSurvey( mapping, form, request, response, SAVE_FOR_LATER );

    return null;
  }

  public ActionForward submit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    saveParticipantSurvey( mapping, form, request, response, SUBMIT );

    request.getSession().removeAttribute( "alertsList" );
    return null;
  }

  private void saveParticipantSurvey( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, String method ) throws Exception
  {
    WebErrorMessage message = new WebErrorMessage();
    SurveyPageDetailsView surveyPageDetailsView = new SurveyPageDetailsView();

    ParticipantTakeSurveyForm paxsurveyresform = (ParticipantTakeSurveyForm)form;

    // set the particiapntSurvey
    buildPromotionInfo( paxsurveyresform, request );
    Survey rndSurvey = getSurveyFromPromotion( request, paxsurveyresform.getPromotion() );

    User user = (User)getParticipantService().getParticipantById( UserManager.getUserId() );

    AssociationRequestCollection ascReqColl = new AssociationRequestCollection();
    ascReqColl.add( new ParticipantSurveyAssociationRequest( ParticipantSurveyAssociationRequest.ALL ) );
    ParticipantSurvey participantSurvey = getParticipantSurveyService()
        .getParticipantSurveyByPromotionAndSurveyIdAndUserIdWithAssociations( new Long( paxsurveyresform.getPromotionId() ), rndSurvey != null ? rndSurvey.getId() : null, user.getId(), ascReqColl );
    boolean newSurvey = false;
    if ( participantSurvey == null )
    {
      participantSurvey = new ParticipantSurvey();
      participantSurvey.setParticipant( user );
      participantSurvey.setPromotionId( new Long( paxsurveyresform.getPromotionId() ) );
      participantSurvey.setSurveyId( new Long( paxsurveyresform.getId() ) );
      newSurvey = true;
    }

    if ( method.equals( SAVE_FOR_LATER ) )
    {
      participantSurvey.setCompleted( false );
    }
    else if ( method.equals( SUBMIT ) )
    {
      participantSurvey.setCompleted( true );
    }

    participantSurvey.setSurveyDate( new Date() );

    Map m = request.getParameterMap();
    Set s = m.entrySet();

    Integer sequenceNum = 0;

    Set<ParticipantSurveyResponse> participantSurveyResponse = new LinkedHashSet<ParticipantSurveyResponse>();
    if ( rndSurvey != null )
    {
      if ( rndSurvey.getActiveQuestions() != null )
      {
        for ( Iterator iter = rndSurvey.getActiveQuestions().iterator(); iter.hasNext(); )
        {
          ParticipantSurveyResponse paxSurveyResponse = new ParticipantSurveyResponse();
          SurveyQuestion surveyQuestion = (SurveyQuestion)iter.next();
          SurveyQuestionBean queBean = new SurveyQuestionBean( surveyQuestion );
          for ( Iterator iterator = s.iterator(); iterator.hasNext(); )
          {
            Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>)iterator.next();
            String key = entry.getKey();
            String[] value = null;
            String answer = null;

            if ( key.equals( "nodes" ) )
            {
              value = entry.getValue();
              participantSurvey.setNodeId( new Long( value[0] ) );
            }

            if ( queBean.getId().equals( key ) )
            {
              value = entry.getValue();
              for ( int i = 0; i < value.length; i++ )
              {
                answer = value[0].toString();
              }
            }
            else
            {
              continue;
            }
            int answerIndex = 0;
            if ( surveyQuestion.getId().equals( new Long( queBean.getId() ) ) )
            {
              if ( surveyQuestion.getResponseType().getCode().equals( SurveyResponseType.STANDARD_RESPONSE ) )
              {
                for ( SurveyAnswerValueBean surveyAnswerBean : queBean.getAnswers() )
                {
                  if ( surveyAnswerBean.getId().toString().equals( answer ) )
                  {
                    answerIndex = Integer.valueOf( surveyAnswerBean.getNumber() ).intValue();
                  }
                }
                if ( answerIndex >= surveyQuestion.getSurveyQuestionResponses().size() )
                {
                  throw new BeaconRuntimeException( "selected answer index does not exist: " + answerIndex );
                }
                SurveyQuestionResponse questionResponse = (SurveyQuestionResponse)surveyQuestion.getSurveyQuestionResponses().get( answerIndex );
                questionResponse.setText( answer );
                paxSurveyResponse.setSurveyQuestionResponse( questionResponse );
              }
              else if ( surveyQuestion.getResponseType().getCode().equals( SurveyResponseType.OPEN_ENDED ) )
              {
                paxSurveyResponse.setOpenEndedResponse( answer );
              }
              else if ( surveyQuestion.getResponseType().getCode().equals( SurveyResponseType.SLIDER_SELECTION ) )
              {
                if ( answer != null && !answer.isEmpty() )
                {
                  paxSurveyResponse.setSliderResponse( Double.parseDouble( answer ) );
                }
              }
              paxSurveyResponse.setSurveyQuestion( surveyQuestion );

              if ( !newSurvey )
              {
                if ( participantSurvey.getParticipantSurveyResponse() != null && participantSurvey.getParticipantSurveyResponse().size() > 0 )
                {
                  for ( Iterator responseIter = participantSurvey.getParticipantSurveyResponse().iterator(); responseIter.hasNext(); )
                  {
                    ParticipantSurveyResponse paxResponse = (ParticipantSurveyResponse)responseIter.next();
                    if ( paxSurveyResponse.getSurveyQuestion().getId().equals( paxResponse.getSurveyQuestion().getId() ) )
                    {
                      paxSurveyResponse.setId( paxResponse.getId() );
                      paxSurveyResponse.setVersion( paxResponse.getVersion() );
                      paxSurveyResponse.setAuditCreateInfo( paxResponse.getAuditCreateInfo() );
                    }
                  }
                }
              }

              paxSurveyResponse.setSequenceNum( sequenceNum );
              participantSurveyResponse.add( paxSurveyResponse );
              sequenceNum++;
            }
          }

        }
      }
    }
    participantSurvey.setParticipantSurveyResponse( participantSurveyResponse );
    participantSurvey = getParticipantSurveyService().save( participantSurvey );

    message.setCommand( WebResponseConstants.RESPONSE_COMMAND_REDIRECT );
    message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
    message.setUrl( RequestUtils.getBaseURI( request ) + PageConstants.HOME_PAGE_G5_REDIRECT_URL );

    surveyPageDetailsView.getMessages().add( message );

    request.getSession().setAttribute( "isSurveyCompleted", participantSurvey.isCompleted() );

    if ( message.getCommand().equals( WebResponseConstants.RESPONSE_COMMAND_REDIRECT ) )
    {
      request.getSession().setAttribute( "surveySubmitConfirmation", true );
    }
    else
    {
      request.setAttribute( "surveySubmitConfirmation", true );
    }

    if ( rndSurvey != null )
    {
      SurveyPageTakeView surveyPageTakeView = new SurveyPageTakeView( rndSurvey, paxsurveyresform.getPromotion(), (Participant)user, participantSurvey );
      surveyPageDetailsView.setSurveyJson( surveyPageTakeView );

      super.writeAsJsonToResponse( surveyPageDetailsView, response );
      return;
    }

  }

  protected void buildPromotionInfo( ParticipantTakeSurveyForm participantTakeSurveyForm, HttpServletRequest request )
  {
    Long promotionId = buildPromotionId( request );
    SurveyPromotion surveyPromotion = (SurveyPromotion)getPromotionService().getPromotionById( promotionId );
    participantTakeSurveyForm.setPromotion( surveyPromotion );
    participantTakeSurveyForm.setPromotionId( promotionId.toString() );
    request.setAttribute( "promoTypeName", surveyPromotion.getPromotionType().getName() );
    request.getSession().setAttribute( "isGqCpSurvey", Boolean.FALSE );
  }

  protected Long buildPromotionId( HttpServletRequest request )
  {
    String value = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" );
    // check the request, just in case
    if ( null != value )
    {
      return new Long( value );
    }
    else
    {
      return (Long)request.getAttribute( "promotionId" );
    }
  }

  private Survey getRandomSurvey( List<Survey> listSurvey )
  {
    Survey randomSurvey = null;

    java.util.Collections.shuffle( listSurvey );
    randomSurvey = (Survey)listSurvey.get( 0 );

    return randomSurvey;
  }

  public static void moveToRequest( HttpServletRequest request )
  {
    // get it from session....
    Boolean modelAvailable = (Boolean)request.getSession().getAttribute( "surveySubmitConfirmation" );
    if ( modelAvailable != null )
    {
      // add it to the request object...
      request.setAttribute( "surveySubmitConfirmation", true );

      // remove it from session....
      request.getSession().removeAttribute( "surveySubmitConfirmation" );
    }
    else
    {
      // add it to the request object...
      request.setAttribute( "surveySubmitConfirmation", false );
    }

  }

  private SurveyService getSurveyService()
  {
    return (SurveyService)getService( SurveyService.BEAN_NAME );
  }

  protected PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private ParticipantSurveyService getParticipantSurveyService()
  {
    return (ParticipantSurveyService)getService( ParticipantSurveyService.BEAN_NAME );
  }

}
