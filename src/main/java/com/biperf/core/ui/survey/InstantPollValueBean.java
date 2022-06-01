
package com.biperf.core.ui.survey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.enums.SurveyResponseType;
import com.biperf.core.domain.promotion.InstantPoll;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InstantPollValueBean
{

  private String id;
  private String promotionId;
  private String name;
  private String startDate;
  private String endDate;
  private boolean complete;
  private boolean showResults;
  private List<SurveyQuestionBean> questions = new ArrayList<SurveyQuestionBean>();

  public InstantPollValueBean()
  {
  }

  public InstantPollValueBean( InstantPoll instantPoll )
  {
    setId( instantPoll.getId().toString() );
    setName( instantPoll.getName() );
    setStartDate( DateUtils.toDisplayString( instantPoll.getSubmissionStartDate() ) );
    setEndDate( DateUtils.toDisplayString( instantPoll.getSubmissionEndDate() ) );
    setComplete( false ); // TODO: check in participant_survey to see if user already taken the
                          // instantPoll
    setShowResults( true );

    List<SurveyQuestion> instantPollQuestions = instantPoll.getActiveQuestions();
    for ( Iterator iter1 = instantPollQuestions.iterator(); iter1.hasNext(); )
    {
      SurveyQuestion surveyQuestion = (SurveyQuestion)iter1.next();
      surveyQuestion.setResponseType( SurveyResponseType.lookup( SurveyResponseType.STANDARD_RESPONSE ) );
      SurveyQuestionBean formBean = new SurveyQuestionBean( surveyQuestion );
      questions.add( formBean );
    }
    setQuestions( questions );
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
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

  @JsonProperty( "isComplete" )
  public boolean isComplete()
  {
    return complete;
  }

  public void setComplete( boolean complete )
  {
    this.complete = complete;
  }

  public boolean isShowResults()
  {
    return showResults;
  }

  public void setShowResults( boolean showResults )
  {
    this.showResults = showResults;
  }

  public List<SurveyQuestionBean> getQuestions()
  {
    return questions;
  }

  public void setQuestions( List<SurveyQuestionBean> questions )
  {
    this.questions = questions;
  }

}
