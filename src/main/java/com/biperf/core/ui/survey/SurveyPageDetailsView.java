
package com.biperf.core.ui.survey;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

public class SurveyPageDetailsView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private SurveyPageTakeView surveyJson = new SurveyPageTakeView();

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public SurveyPageTakeView getSurveyJson()
  {
    return surveyJson;
  }

  public void setSurveyJson( SurveyPageTakeView surveyJson )
  {
    this.surveyJson = surveyJson;
  }

}
