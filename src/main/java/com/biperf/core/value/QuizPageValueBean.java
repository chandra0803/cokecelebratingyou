
package com.biperf.core.value;

import java.io.Serializable;

import com.biperf.core.domain.promotion.Promotion;

public class QuizPageValueBean implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 2L;
  private Promotion promotion;
  private long claimId;
  private boolean takeQuiz = false;
  private boolean resumeQuiz = false;
  private boolean quizCompleted = false;
  private boolean retakeQuiz = false;
  private int timeRemaining;
  private Long awardAmount;
  private Long attemptsRemaining;
  private Long diyQuizId;
  private String diyQuizName;

  public QuizPageValueBean()
  {
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( long claimId )
  {
    this.claimId = claimId;
  }

  public boolean isTakeQuiz()
  {
    return takeQuiz;
  }

  public void setTakeQuiz( boolean takeQuiz )
  {
    this.takeQuiz = takeQuiz;
  }

  public boolean isResumeQuiz()
  {
    return resumeQuiz;
  }

  public void setResumeQuiz( boolean resumeQuiz )
  {
    this.resumeQuiz = resumeQuiz;
  }

  public boolean isQuizCompleted()
  {
    return quizCompleted;
  }

  public void setQuizCompleted( boolean quizCompleted )
  {
    this.quizCompleted = quizCompleted;
  }

  public boolean isRetakeQuiz()
  {
    return retakeQuiz;
  }

  public void setRetakeQuiz( boolean retakeQuiz )
  {
    this.retakeQuiz = retakeQuiz;
  }

  public int getTimeRemaining()
  {
    return timeRemaining;
  }

  public void setTimeRemaining( int timeRemaining )
  {
    this.timeRemaining = timeRemaining;
  }

  public Long getAwardAmount()
  {
    return awardAmount;
  }

  public void setAwardAmount( Long awardAmount )
  {
    this.awardAmount = awardAmount;
  }

  public Long getAttemptsRemaining()
  {
    return attemptsRemaining;
  }

  public void setAttemptsRemaining( Long attemptsRemaining )
  {
    this.attemptsRemaining = attemptsRemaining;
  }

  public Long getDiyQuizId()
  {
    return diyQuizId;
  }

  public void setDiyQuizId( Long diyQuizId )
  {
    this.diyQuizId = diyQuizId;
  }

  public String getDiyQuizName()
  {
    return diyQuizName;
  }

  public void setDiyQuizName( String diyQuizName )
  {
    this.diyQuizName = diyQuizName;
  }

}
