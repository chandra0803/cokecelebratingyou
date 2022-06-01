
package com.biperf.core.ui.diyquiz;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

public class DIYQuizResponseView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private String forwardUrl = "";
  private DIYQuizIdView quiz = new DIYQuizIdView();

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public String getForwardUrl()
  {
    return forwardUrl;
  }

  public void setForwardUrl( String forwardUrl )
  {
    this.forwardUrl = forwardUrl;
  }

  public DIYQuizIdView getQuiz()
  {
    return quiz;
  }

  public void setQuiz( DIYQuizIdView quiz )
  {
    this.quiz = quiz;
  }

}
