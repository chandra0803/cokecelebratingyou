
package com.biperf.core.ui.survey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.survey.ParticipantSurvey;
import com.biperf.core.domain.survey.ParticipantSurveyResponse;
import com.biperf.core.domain.user.UserNode;

public class SurveyPageTakeView
{
  private List<SurveyQuestionBean> questions = new ArrayList<SurveyQuestionBean>();
  private String id;
  private String name;
  private String startDate;
  private String endDate;
  private String introText;
  private boolean isComplete;
  private List<SurveyNodesBean> nodes = new ArrayList<SurveyNodesBean>();

  public SurveyPageTakeView()
  {

  }

  public SurveyPageTakeView( Survey survey, Promotion promotion, Participant pax, ParticipantSurvey participantSurvey )
  {
    List<Long> paxResponseIds = new ArrayList<Long>();
    if ( participantSurvey != null && participantSurvey.getParticipantSurveyResponse() != null && !participantSurvey.getParticipantSurveyResponse().isEmpty() )
    {
      for ( Iterator iter1 = survey.getActiveQuestions().iterator(); iter1.hasNext(); )
      {
        SurveyQuestion surveyQuestion = (SurveyQuestion)iter1.next();
        for ( Iterator iter = participantSurvey.getParticipantSurveyResponse().iterator(); iter.hasNext(); )
        {
          ParticipantSurveyResponse participantSurveyResponse = (ParticipantSurveyResponse)iter.next();
          if ( !CollectionUtils.isEmpty( survey.getActiveQuestions() ) )
          {
            if ( surveyQuestion.getId().equals( participantSurveyResponse.getSurveyQuestion().getId() ) )
            {
              SurveyQuestionBean formBean = new SurveyQuestionBean( participantSurveyResponse, surveyQuestion );
              questions.add( formBean );
              paxResponseIds.add( surveyQuestion.getId() );
              break;
            }
          }
        }
      }
      this.setNodes( getNodesAsList( new ArrayList<UserNode>( pax.getUserNodes() ), participantSurvey.getNodeId() ) );
      if ( paxResponseIds.size() != survey.getActiveQuestions().size() )
      {
        int index = 0;
        for ( Iterator iter = survey.getActiveQuestions().iterator(); iter.hasNext(); )
        {
          SurveyQuestion surveyQuestion = (SurveyQuestion)iter.next();
          boolean hasQuestion = false;
          for ( Iterator iter1 = paxResponseIds.iterator(); iter1.hasNext(); )
          {
            Long ids = (Long)iter1.next();
            if ( ids.equals( surveyQuestion.getId() ) )
            {
              hasQuestion = true;
              break;
            }
          }
          if ( !hasQuestion )
          {
            SurveyQuestionBean formBean = new SurveyQuestionBean( surveyQuestion );
            questions.add( index, formBean );
          }
          index++;
        }
      }
    }
    else
    {
      if ( !CollectionUtils.isEmpty( survey.getActiveQuestions() ) )
      {
        for ( Iterator iter = survey.getActiveQuestions().iterator(); iter.hasNext(); )
        {
          SurveyQuestion surveyQuestion = (SurveyQuestion)iter.next();
          SurveyQuestionBean formBean = new SurveyQuestionBean( surveyQuestion );
          questions.add( formBean );
        }
      }
      this.setNodes( getNodesAsList( new ArrayList<UserNode>( pax.getUserNodes() ), null ) );
    }

    this.setQuestions( questions );
    this.setId( survey.getId().toString() );
    if ( promotion != null )
    {
      this.setName( promotion.getName() );
      this.setStartDate( promotion.getSubmissionStartDate().toString() );
      if ( promotion.getSubmissionEndDate() != null )
      {
        this.setEndDate( promotion.getSubmissionEndDate().toString() );
      }
    }
  }

  public void setNodes( List<SurveyNodesBean> nodes )
  {
    this.nodes = nodes;
  }

  public List<SurveyNodesBean> getNodes()
  {
    return nodes;
  }

  public List<SurveyQuestionBean> getQuestions()
  {
    return questions;
  }

  public void setQuestions( List<SurveyQuestionBean> questions )
  {
    this.questions = questions;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getIntroText()
  {
    return introText;
  }

  public void setIntroText( String introText )
  {
    this.introText = introText;
  }

  public boolean isIsComplete()
  {
    return isComplete;
  }

  public void setIsComplete( boolean isComplete )
  {
    this.isComplete = isComplete;
  }

  public static List<SurveyNodesBean> getNodesAsList( List<UserNode> userNodes, Long nodeId )
  {
    List<SurveyNodesBean> nodeList = new ArrayList<SurveyNodesBean>();
    Collections.sort( userNodes, new Comparator<UserNode>()
    {
      public int compare( UserNode un1, UserNode un2 )
      {
        if ( un1.getIsPrimary() )
        {
          return -1;
        }
        else if ( un2.getIsPrimary() )
        {
          return 1;
        }
        else
        {
          return un1.getNode().getName().compareTo( un1.getNode().getName() );
        }
      }
    } );

    for ( Iterator<UserNode> it = userNodes.iterator(); it.hasNext(); )
    {
      SurveyNodesBean bean = new SurveyNodesBean();
      UserNode userNode = it.next();
      if ( nodeId != null && userNode.getNode().getId().equals( nodeId ) )
      {
        bean = new SurveyNodesBean( userNode.getNode().getId(), userNode.getNode().getName(), true );
      }
      else
      {
        bean = new SurveyNodesBean( userNode.getNode().getId(), userNode.getNode().getName(), false );
      }

      nodeList.add( bean );
    }

    return nodeList;
  }

}
