/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/ParticipantQuizClaimHistory.java,v $
 */

package com.biperf.core.value;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.domain.claim.QuizClaim;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>wadzinsk</td>
 * <td>Nov 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantQuizClaimHistory
{

  private QuizPromotion promotion;

  private List quizClaimsBySubmissionDate = new ArrayList();

  public QuizClaim getMostRecentClaim()
  {
    if ( quizClaimsBySubmissionDate == null || quizClaimsBySubmissionDate.isEmpty() )
    {
      throw new BeaconRuntimeException( "ParticipantQuizClaimHistory must always have at least one quiz claim, " + "check code where this ParticipantQuizClaimHistory instance was created" );
    }

    // last list entry is most recent claim
    return (QuizClaim)quizClaimsBySubmissionDate.get( quizClaimsBySubmissionDate.size() - 1 );
  }

  public boolean isRetakeable( String timeZoneId )
  {
    boolean retakeable = false;

    if ( isPromotionSubmittable( timeZoneId ) && !isInProgress() && !isPassed() && isAttemptsRemaining() )
    {
      retakeable = true;
    }

    return retakeable;
  }

  public boolean isPassed()
  {
    boolean passed = false;

    QuizClaim mostRecentClaim = getMostRecentClaim();

    if ( BooleanUtils.isTrue( mostRecentClaim.getPass() ) )
    {
      passed = true;
    }

    return passed;
  }

  public boolean isSavedForLater()
  {
    boolean saved = false;

    QuizClaim mostRecentClaim = getMostRecentClaim();

    if ( Objects.isNull( mostRecentClaim.getPass() ) )
    {
      saved = true;
    }

    return saved;
  }

  public boolean isInProgress()
  {
    boolean inProgress = false;

    QuizClaim mostRecentClaim = getMostRecentClaim();

    if ( !mostRecentClaim.isQuizComplete() )
    {
      inProgress = true;
    }

    return inProgress;
  }

  private boolean isPromotionSubmittable( String timeZoneId )
  {
    return DateUtils.isDateBetween( new Date(), promotion.getSubmissionStartDate(), promotion.getSubmissionEndDate(), timeZoneId );
  }

  /**
   * Returns true if promotion is not yet passed and number of attempts is less than max attempts
   */
  public boolean isAttemptsRemaining()
  {
    if ( isPassed() )
    {
      return false;
    }
    else if ( promotion.isAllowUnlimitedAttempts() )
    {
      return true;
    }
    return quizClaimsBySubmissionDate.size() < promotion.getMaximumAttempts();
  }

  /**
   * Returns String value of attempts remaining, "unlimited" if unlimited
   */
  public String getAttemptsRemaining()
  {
    if ( isPassed() )
    {
      return new Long( 0 ).toString();
    }
    else if ( promotion.isAllowUnlimitedAttempts() )
    {
      return CmsResourceBundle.getCmsBundle().getString( "report.quizzes.quizactivity.UNLIMITED" );
    }
    else if ( quizClaimsBySubmissionDate.size() < promotion.getMaximumAttempts() )
    {
      return new Long( promotion.getMaximumAttempts() - quizClaimsBySubmissionDate.size() ).toString();

    }
    else
    {
      return new Long( 0 ).toString();
    }
  }

  /**
   * @return value of quizClaimsBySubmissionDate property
   */
  public List getQuizClaimsBySubmissionDate()
  {
    return quizClaimsBySubmissionDate;
  }

  /**
   * @param quizClaimsBySubmissionDate value for quizClaimsBySubmissionDate property
   */
  public void setQuizClaimsBySubmissionDate( List quizClaimsBySubmissionDate )
  {
    this.quizClaimsBySubmissionDate = quizClaimsBySubmissionDate;
  }

  /**
   * Add a quiz claim, and resort quizClaimsBySubmissionDate by submission date ascending.
   */
  public void addQuizClaim( QuizClaim quizClaim )
  {
    quizClaimsBySubmissionDate.add( quizClaim );

    // sort by submission date asc
    PropertyComparator.sort( quizClaimsBySubmissionDate, new MutableSortDefinition( "submissionDate", false, true ) );
  }

  /**
   * @return value of promotion property
   */
  public QuizPromotion getPromotion()
  {
    return promotion;
  }

  /**
   * @param promotion value for promotion property
   */
  public void setPromotion( QuizPromotion promotion )
  {
    this.promotion = promotion;
  }

}
