
package com.biperf.core.ui.quiz;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.claim.QuizClaim;

public class QuizTakeQuestionView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private QuizTakeQuestion question = new QuizTakeQuestion();

  public QuizTakeQuestionView( QuizClaim quizClaim, boolean displayCorrect )
  {
    this.question = new QuizTakeQuestion( quizClaim, displayCorrect );
  }

  public QuizTakeQuestionView( QuizTakeQuestion question )
  {
    super();
    this.question = question;
  }

  public QuizTakeQuestionView( List<WebErrorMessage> messages, QuizTakeQuestion question )
  {
    super();
    this.messages = messages;
    this.question = question;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public QuizTakeQuestion getQuestion()
  {
    return question;
  }

  public void setQuestion( QuizTakeQuestion question )
  {
    this.question = question;
  }

}
