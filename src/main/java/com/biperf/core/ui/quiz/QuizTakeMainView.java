
package com.biperf.core.ui.quiz;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

public class QuizTakeMainView
{

  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private QuizTakeQuizView quizJson = new QuizTakeQuizView();

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public QuizTakeQuizView getQuizJson()
  {
    return quizJson;
  }

  public void setQuizJson( QuizTakeQuizView quizJson )
  {
    this.quizJson = quizJson;
  }

}
