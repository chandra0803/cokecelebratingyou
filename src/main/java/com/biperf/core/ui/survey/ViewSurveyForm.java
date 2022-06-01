
package com.biperf.core.ui.survey;

import java.util.List;

import com.biperf.core.domain.promotion.Survey;

public class ViewSurveyForm extends SurveyForm
{
  private String surveyQuestionId;
  private String newQuestionSequenceNum;
  private List questions;
  private String[] deleteIds;

  /**
   * Load the form
   * 
   * @param survey
   */
  public void load( Survey survey )
  {
    super.load( survey );
    this.questions = survey.getSurveyQuestions();

  }

  public List getQuestions()
  {
    return questions;
  }

  public void setQuestions( List questions )
  {
    this.questions = questions;
  }

  public int getQuestionsSize()
  {
    int size = 0;
    if ( questions != null )
    {
      size = questions.size();
    }
    return size;
  }

  public String getNewQuestionSequenceNum()
  {
    return newQuestionSequenceNum;
  }

  public void setNewQuestionSequenceNum( String newQuestionSequenceNum )
  {
    this.newQuestionSequenceNum = newQuestionSequenceNum;
  }

  public String getSurveyQuestionId()
  {
    return surveyQuestionId;
  }

  public void setSurveyQuestionId( String surveyQuestionId )
  {
    this.surveyQuestionId = surveyQuestionId;
  }

  public String[] getDeleteIds()
  {
    return deleteIds;
  }

  public void setDeleteIds( String[] deleteIds )
  {
    this.deleteIds = deleteIds;
  }

}
