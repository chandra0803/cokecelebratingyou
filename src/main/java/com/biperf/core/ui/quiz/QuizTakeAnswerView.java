
package com.biperf.core.ui.quiz;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.claim.QuizClaim;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.CmsResourceBundle;

public class QuizTakeAnswerView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private QuizTakeQuestion question = new QuizTakeQuestion();
  private UpdateBootstrapJson updateBootstrapJson = null;

  public QuizTakeAnswerView( QuizClaim quizClaim, boolean displayCorrect, QuizTakeCertificate certificate, Long awardAmount )
  {
    this.question = new QuizTakeQuestion( quizClaim, true, displayCorrect );
    this.updateBootstrapJson = new UpdateBootstrapJson( certificate, awardAmount );
  }

  public QuizTakeAnswerView( QuizTakeQuestion question )
  {
    super();
    this.question = question;
  }

  public QuizTakeAnswerView( List<WebErrorMessage> messages, QuizTakeQuestion question )
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

  @JsonProperty( "quizJson" )
  public UpdateBootstrapJson getUpdateBootstrapJson()
  {
    return updateBootstrapJson;
  }

  public void setUpdateBootstrapJson( UpdateBootstrapJson updateBootstrapJson )
  {
    this.updateBootstrapJson = updateBootstrapJson;
  }

  /**
   * Any properties that are in this JSON will replace the bootstrap JSON properties.
   * FE code will use these properties rather than the BootStrap.
   * UpdateBootstrapJson.
   * 
   * @author kandhi
   * @since May 6, 2014
   * @version 1.0
   */
  public class UpdateBootstrapJson
  {
    private QuizTakeCertificate certificate = new QuizTakeCertificate();
    private String quizAward = null;

    public UpdateBootstrapJson( QuizTakeCertificate certificate, Long awardAmount )
    {
      super();
      this.certificate = certificate;
      if ( awardAmount != null && awardAmount.longValue() > 0 )
      {
        quizAward = awardAmount + " " + CmsResourceBundle.getCmsBundle().getString( "claims.quiz.submission.review.POINTS" );
      }
      setQuizAward( quizAward );
    }

    public QuizTakeCertificate getCertificate()
    {
      return certificate;
    }

    public void setCertificate( QuizTakeCertificate certificate )
    {
      this.certificate = certificate;
    }

    public String getQuizAward()
    {
      return quizAward;
    }

    public void setQuizAward( String quizAward )
    {
      this.quizAward = quizAward;
    }

  }

}
