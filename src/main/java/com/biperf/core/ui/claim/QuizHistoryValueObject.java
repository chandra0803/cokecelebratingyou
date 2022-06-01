/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/QuizHistoryValueObject.java,v $
 */

package com.biperf.core.ui.claim;

import java.util.Date;

import com.biperf.core.utils.DateUtils;

/*
 * RecognitionHistoryForm <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Jul
 * 15, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class QuizHistoryValueObject extends Object
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private Long id;

  private Long promotionId;

  private String promotionName = null;

  private int quizAttempt = 0;

  private String quizAttemptsRemaining;

  private boolean passed;

  private String diyQuizName;

  private boolean quizComplete;

  private boolean attemptsRemaining;

  private Date dateCompleted = null;

  private boolean resumeQuiz;

  private boolean retakeQuiz;

  private boolean unlimitedAttempts;

  // For deposits such as file load, discretionary awards which have no quiz details
  // but must be shown in the Quiz History and Quiz Transaction History list pages
  // Below attributes capture the type of deposits (journalType) and the award qty
  private boolean nonClaimRelatedDeposits = false;
  private String journalType;
  private int awardQuantity;
  private String awardTypeName;
  private String badgeImgUrl;

  /**
   * True if this value object represents a non-claim award that is a result
   * of a Discretionary Award; false otherwise.
   */
  private boolean isDiscretionary;

  private boolean isSweepstakes;

  private String reversalDescription;

  private String regSymbol = "&#174;";

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public boolean isAttemptsRemaining()
  {
    return attemptsRemaining;
  }

  public void setAttemptsRemaining( boolean attemptsRemaining )
  {
    this.attemptsRemaining = attemptsRemaining;
  }

  public int getAwardQuantity()
  {
    return awardQuantity;
  }

  public void setAwardQuantity( int awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

  public String getAwardTypeName()
  {
    return awardTypeName;
  }

  public void setAwardTypeName( String awardTypeName )
  {
    this.awardTypeName = awardTypeName;
  }

  public Date getDateCompleted()
  {
    return dateCompleted;
  }

  public String getDisplayDateCompleted()
  {
    if ( this.dateCompleted != null )
    {
      return DateUtils.toDisplayString( this.dateCompleted );
    }
    else
    {
      return "";
    }
  }

  public void setDateCompleted( Date dateCompleted )
  {
    this.dateCompleted = dateCompleted;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getJournalType()
  {
    return journalType;
  }

  public void setJournalType( String journalType )
  {
    this.journalType = journalType;
  }

  public boolean isNonClaimRelatedDeposits()
  {
    return nonClaimRelatedDeposits;
  }

  public void setNonClaimRelatedDeposits( boolean nonClaimRelatedDeposits )
  {
    this.nonClaimRelatedDeposits = nonClaimRelatedDeposits;
  }

  public boolean isPassed()
  {
    return passed;
  }

  public void setPassed( boolean passed )
  {
    this.passed = passed;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public int getQuizAttempt()
  {
    return quizAttempt;
  }

  public void setQuizAttempt( int quizAttempt )
  {
    this.quizAttempt = quizAttempt;
  }

  public String getQuizAttemptsRemaining()
  {
    return quizAttemptsRemaining;
  }

  public void setQuizAttemptsRemaining( String quizAttemptsRemaining )
  {
    this.quizAttemptsRemaining = quizAttemptsRemaining;
  }

  public boolean isQuizComplete()
  {
    return quizComplete;
  }

  public void setQuizComplete( boolean quizComplete )
  {
    this.quizComplete = quizComplete;
  }

  public boolean isResumeQuiz()
  {
    return resumeQuiz;
  }

  public void setResumeQuiz( boolean resumeQuiz )
  {
    this.resumeQuiz = resumeQuiz;
  }

  public boolean isRetakeQuiz()
  {
    return retakeQuiz;
  }

  public void setRetakeQuiz( boolean retakeQuiz )
  {
    this.retakeQuiz = retakeQuiz;
  }

  public boolean isUnlimitedAttempts()
  {
    return unlimitedAttempts;
  }

  public void setUnlimitedAttempts( boolean unlimitedAttempts )
  {
    this.unlimitedAttempts = unlimitedAttempts;
  }

  public boolean isDiscretionary()
  {
    return isDiscretionary;
  }

  public void setDiscretionary( boolean isDiscretionary )
  {
    this.isDiscretionary = isDiscretionary;
  }

  public boolean isSweepstakes()
  {
    return isSweepstakes;
  }

  public void setSweepstakes( boolean isSweepstakes )
  {
    this.isSweepstakes = isSweepstakes;
  }

  public String getReversalDescription()
  {
    return reversalDescription;
  }

  public void setReversalDescription( String reversalDescription )
  {
    this.reversalDescription = reversalDescription;
  }

  public String getRegSymbol()
  {
    return regSymbol;
  }

  public void setRegSymbol( String regSymbol )
  {
    this.regSymbol = regSymbol;
  }

  public String getDiyQuizName()
  {
    return diyQuizName;
  }

  public void setDiyQuizName( String diyQuizName )
  {
    this.diyQuizName = diyQuizName;
  }

  public String getBadgeImgUrl()
  {
    return badgeImgUrl;
  }

  public void setBadgeImgUrl( String badgeImgUrl )
  {
    this.badgeImgUrl = badgeImgUrl;
  }

}
