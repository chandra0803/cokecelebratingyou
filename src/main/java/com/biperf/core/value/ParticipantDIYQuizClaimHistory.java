
package com.biperf.core.value;

import com.biperf.core.domain.diyquiz.DIYQuiz;
import com.biperf.core.domain.quiz.Quiz;
import com.objectpartners.cms.util.CmsResourceBundle;

public class ParticipantDIYQuizClaimHistory extends ParticipantQuizClaimHistory
{

  private Quiz quiz;

  public Quiz getQuiz()
  {
    return quiz;
  }

  public void setQuiz( Quiz quiz )
  {
    this.quiz = quiz;
  }

  /**
   * Returns true if promotion is not yet passed and number of attempts is less than max attempts
   */
  @Override
  public boolean isAttemptsRemaining()
  {
    if ( isPassed() )
    {
      return false;
    }
    else if ( ( (DIYQuiz)quiz ).isAllowUnlimitedAttempts() )
    {
      return true;
    }
    return getQuizClaimsBySubmissionDate().size() < ( (DIYQuiz)quiz ).getMaximumAttempts();
  }

  /**
   * Returns String value of attempts remaining, "unlimited" if unlimited
   */
  @Override
  public String getAttemptsRemaining()
  {
    if ( isPassed() )
    {
      return new Long( 0 ).toString();
    }
    else if ( ( (DIYQuiz)quiz ).isAllowUnlimitedAttempts() )
    {
      return CmsResourceBundle.getCmsBundle().getString( "report.quizzes.quizactivity.UNLIMITED" );
    }
    else if ( getQuizClaimsBySubmissionDate().size() < ( (DIYQuiz)quiz ).getMaximumAttempts() )
    {
      return new Long( ( (DIYQuiz)quiz ).getMaximumAttempts() - getQuizClaimsBySubmissionDate().size() ).toString();
    }
    else
    {
      return new Long( 0 ).toString();
    }
  }
}
