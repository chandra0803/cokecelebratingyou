
package com.biperf.core.ui.survey;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.SurveyResponseType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.InstantPoll;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.domain.survey.ParticipantSurvey;
import com.biperf.core.domain.survey.ParticipantSurveyResponse;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.instantpoll.InstantPollService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participantsurvey.ParticipantSurveyService;
import com.biperf.core.service.survey.ParticipantSurveyAssociationRequest;
import com.biperf.core.service.survey.SurveyAssociationRequest;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

public class ParticipantTakeInstantPollAction extends BaseDispatchAction
{

  /**
   * Display the polls the user is a member of
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws Exception
   */
  @SuppressWarnings( { "unchecked" } )
  public void fetchQuestions( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SurveyAssociationRequest( SurveyAssociationRequest.ALL ) );

    List<InstantPoll> instantPolls = getInstallPollService().getInstantPollsForTileDisplay( UserManager.getUserId(), associationRequestCollection );

    InstantPollCollectionView instantPollCollectionView = new InstantPollCollectionView( instantPolls );

    AssociationRequestCollection ascReqColl = new AssociationRequestCollection();
    ascReqColl.add( new ParticipantSurveyAssociationRequest( ParticipantSurveyAssociationRequest.SURVEY_RESPONSE_QUESTIONS ) );

    setPollCountsToView( instantPolls, instantPollCollectionView, ascReqColl );

    for ( InstantPollValueBean instantPollValueBean : instantPollCollectionView.getPolls() )
    {
      if ( instantPollValueBean.isComplete() )
      {
        calculateResponsePercentage( instantPollValueBean.getQuestions() );
      }
    }

    Collections.sort( instantPollCollectionView.getPolls(), InstantPollComparator );
    super.writeAsJsonToResponse( instantPollCollectionView, response );
    return;
  }

  public static Comparator<InstantPollValueBean> InstantPollComparator = new Comparator<InstantPollValueBean>()
  {
    public int compare( InstantPollValueBean instantPollValueBean1, InstantPollValueBean instantPollValueBean2 )
    {
      boolean flag1 = instantPollValueBean1.isComplete();
      boolean flag2 = instantPollValueBean2.isComplete();
      Date bean1StartDate = DateUtils.toDate( instantPollValueBean1.getStartDate() );
      Date bean2StartDate = DateUtils.toDate( instantPollValueBean2.getStartDate() );

      if ( !flag1 && !flag2 )
      {
        return bean1StartDate.compareTo( bean2StartDate );
      }
      else if ( flag1 && flag2 )
      {
        return bean1StartDate.compareTo( bean2StartDate );
      }
      else if ( !flag1 )
      {
        return -1;
      }
      else if ( !flag2 )
      {
        return 1;
      }
      return 0;
    }
  };

  /**
   * This would set the chosen answer and count for the question
   * @param instantPolls
   * @param instantPollCollectionView
   * @param ascReqColl
   */
  private void setPollCountsToView( List<InstantPoll> instantPolls, InstantPollCollectionView instantPollCollectionView, AssociationRequestCollection ascReqColl )
  {
    for ( InstantPollValueBean viewInstantPollView : instantPollCollectionView.getPolls() )
    {
      for ( InstantPoll instantPoll : instantPolls )
      {
        if ( instantPoll.getId().equals( Long.valueOf( viewInstantPollView.getId() ) ) )
        {
          ParticipantSurvey participantSurvey = getParticipantSurveyService().getParticipantSurveyBySurveyIdAndUserIdWithAssociations( instantPoll.getId(), UserManager.getUserId(), ascReqColl );

          if ( participantSurvey != null )
          {
            viewInstantPollView.setComplete( participantSurvey.isCompleted() );

            Set<ParticipantSurveyResponse> participantSurveyResponseSet = participantSurvey.getParticipantSurveyResponse();
            Iterator<ParticipantSurveyResponse> itr = participantSurveyResponseSet.iterator();
            while ( itr.hasNext() )
            {
              ParticipantSurveyResponse participantSurveyResponse = itr.next();
              SurveyQuestion surveyQuestion = participantSurveyResponse.getSurveyQuestion();
              for ( SurveyQuestionBean viewSurveyQuestionBean : viewInstantPollView.getQuestions() )
              {
                if ( surveyQuestion.getId().equals( Long.valueOf( viewSurveyQuestionBean.getId() ) ) )
                {
                  viewSurveyQuestionBean.setIsAnswered( true );
                  for ( SurveyQuestionResponse surveyQuestionResponse : surveyQuestion.getSurveyQuestionResponses() )
                  {
                    for ( SurveyAnswerValueBean viewSurveyAnswerValueBean : viewSurveyQuestionBean.getAnswers() )
                    {
                      if ( surveyQuestionResponse.getId().equals( new Long( viewSurveyAnswerValueBean.getId() ) ) )
                      {
                        if ( participantSurveyResponse.getSurveyQuestionResponse().getId().equals( surveyQuestionResponse.getId() ) )
                        {
                          viewSurveyAnswerValueBean.setIsChosen( true );
                        }
                        viewSurveyAnswerValueBean.setCount( String.valueOf( surveyQuestionResponse.getResponseCount() ) );
                        break;
                      }
                    }
                  }
                  break;
                }
              }
            }
          }
          else
          {
            viewInstantPollView.setComplete( false );
          }
          break;
        }
      }
    }
  }

  /**
   * Saves the poll response of the user
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @throws Exception
   */
  public void saveInstantPoll( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SurveyPageDetailsView surveyPageDetailsView = new SurveyPageDetailsView();

    // set the particiapntSurvey
    User user = (User)getParticipantService().getParticipantById( UserManager.getUserId() );

    ParticipantSurvey participantSurvey = new ParticipantSurvey();
    participantSurvey.setParticipant( user );
    participantSurvey.setCompleted( true );
    participantSurvey.setSurveyDate( new Date() );

    Long instantPollId = new Long( request.getParameter( "id" ) );
    participantSurvey.setSurveyId( instantPollId );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SurveyAssociationRequest( SurveyAssociationRequest.ALL ) );
    InstantPoll instantPoll = getInstallPollService().getInstantPollByIdWithAssociations( new Long( instantPollId ), associationRequestCollection );

    Integer sequenceNum = 0;
    Map m = request.getParameterMap();
    Set s = m.entrySet();

    Set<ParticipantSurveyResponse> participantSurveyResponse = new LinkedHashSet<ParticipantSurveyResponse>();
    if ( instantPoll != null )
    {
      if ( instantPoll.getActiveQuestions() != null )
      {
        for ( Iterator iter = instantPoll.getActiveQuestions().iterator(); iter.hasNext(); )
        {
          ParticipantSurveyResponse paxSurveyResponse = new ParticipantSurveyResponse();
          SurveyQuestion surveyQuestion = (SurveyQuestion)iter.next();
          surveyQuestion.setResponseType( SurveyResponseType.lookup( SurveyResponseType.STANDARD_RESPONSE ) );
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
              for ( SurveyAnswerValueBean surveyAnswerBean : queBean.getAnswers() )
              {
                if ( surveyAnswerBean != null )
                {
                  if ( surveyAnswerBean.getId() != null )
                  {
                    if ( surveyAnswerBean.getId().toString().equals( answer ) )
                    {
                      answerIndex = Integer.valueOf( surveyAnswerBean.getNumber() ).intValue();
                    }
                  }
                }
              }
              if ( answerIndex >= surveyQuestion.getSurveyQuestionResponses().size() )
              {
                throw new BeaconRuntimeException( "selected answer index does not exist: " + answerIndex );
              }
              SurveyQuestionResponse questionResponse = (SurveyQuestionResponse)surveyQuestion.getSurveyQuestionResponses().get( answerIndex );
              if ( surveyQuestion.getResponseType().getCode().equals( SurveyResponseType.OPEN_ENDED ) )
              {
                questionResponse.setText( answer );
                paxSurveyResponse.setOpenEndedResponse( answer );
              }
              paxSurveyResponse.setSurveyQuestion( surveyQuestion );

              paxSurveyResponse.setSurveyQuestionResponse( questionResponse );
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

    if ( participantSurvey != null )
    {
      Set<ParticipantSurveyResponse> participantSurveyResponseSet = participantSurvey.getParticipantSurveyResponse();
      Iterator<ParticipantSurveyResponse> itr = participantSurveyResponseSet.iterator();
      while ( itr.hasNext() )
      {
        ParticipantSurveyResponse surveyResponse = itr.next();
        SurveyQuestionResponse surveyQuestionResponse = surveyResponse.getSurveyQuestionResponse();
        surveyQuestionResponse.setResponseCount( surveyQuestionResponse.getResponseCount() + 1 );
        getParticipantSurveyService().saveSurveyQuestionResponse( surveyQuestionResponse );
      }
    }

    if ( instantPoll != null )
    {
      SurveyPageTakeView surveyPageTakeView = new SurveyPageTakeView( instantPoll, null, (Participant)user, participantSurvey );
      surveyPageTakeView.setIsComplete( true );
      calculateResponsePercentage( surveyPageTakeView.getQuestions() );
      surveyPageDetailsView.setSurveyJson( surveyPageTakeView );

      super.writeAsJsonToResponse( surveyPageDetailsView, response );
      return;
    }
  }

  private void calculateResponsePercentage( List<SurveyQuestionBean> surveyQuestionBeans )
  {
    // Calculate totals
    Long total = 0L;
    for ( SurveyQuestionBean surveyQuestionBean : surveyQuestionBeans )
    {
      List<SurveyAnswerValueBean> surveyAnswerValueBeans = surveyQuestionBean.getAnswers();
      for ( SurveyAnswerValueBean surveyAnswerValueBean : surveyAnswerValueBeans )
      {
        if ( surveyAnswerValueBean.getCount() != null )
        {
          total = total + Long.valueOf( surveyAnswerValueBean.getCount() );
        }
      }
    }

    // Calculate percentage
    for ( SurveyQuestionBean surveyQuestionBean : surveyQuestionBeans )
    {
      List<SurveyAnswerValueBean> surveyAnswerValueBeans = surveyQuestionBean.getAnswers();
      for ( SurveyAnswerValueBean surveyAnswerValueBean : surveyAnswerValueBeans )
      {
        if ( surveyAnswerValueBean.getCount() != null )
        {
          Long percentage = Long.parseLong( surveyAnswerValueBean.getCount() ) * 100 / total;
          surveyAnswerValueBean.setPercent( String.valueOf( percentage ) );
        }
        else
        {
          surveyAnswerValueBean.setPercent( "0" );
        }
      }
    }
  }

  private InstantPollService getInstallPollService()
  {
    return (InstantPollService)getService( InstantPollService.BEAN_NAME );
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
