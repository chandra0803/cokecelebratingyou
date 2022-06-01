
package com.biperf.core.domain.challengepoint;

import java.math.BigDecimal;
import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;

/**
 * ChallengepointProgress domain object.
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
 * <td>Prabu Babu</td>
 * <td>Jul 16, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * 
 */
public class ChallengepointProgress extends BaseDomain implements Cloneable
{
  /**
   * The participant for whom the cp is set for.
   */
  private Participant participant;

  /**
   * The challengepoint promotion for which the cp is set for.
   */
  private ChallengePointPromotion challengePointPromotion;

  /**
   * Type of load, add/replace
   */
  private String type;

  /**
   * The progress quantity.
   */
  private BigDecimal quantity;

  /*
   * as of date for quantity
   */
  private Date submissionDate;

  public ChallengepointProgress()
  {
    super();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( ! ( object instanceof ChallengepointProgress ) )
    {
      return false;
    }

    ChallengepointProgress otherChallengepointAward = (ChallengepointProgress)object;

    if ( otherChallengepointAward.getChallengePointPromotion() != null && !otherChallengepointAward.getChallengePointPromotion().equals( this.getChallengePointPromotion() ) )
    {

      equals = false;
    }

    if ( otherChallengepointAward.getParticipant() != null && !otherChallengepointAward.getParticipant().equals( this.getParticipant() ) )
    {

      equals = false;
    }

    if ( otherChallengepointAward.getSubmissionDate() != null && !otherChallengepointAward.getSubmissionDate().equals( this.getSubmissionDate() ) )
    {

      equals = false;
    }

    return equals;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int resultHS;
    resultHS = this.getChallengePointPromotion() != null ? this.getChallengePointPromotion().hashCode() : 0;
    resultHS = 29 * resultHS + ( getParticipant() != null ? getParticipant().hashCode() : 0 );
    resultHS = 29 * resultHS + ( getSubmissionDate() != null ? getSubmissionDate().hashCode() : 0 );

    return resultHS;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public ChallengePointPromotion getChallengePointPromotion()
  {
    return challengePointPromotion;
  }

  public void setChallengePointPromotion( ChallengePointPromotion challengePointPromotion )
  {
    this.challengePointPromotion = challengePointPromotion;
  }

  public BigDecimal getQuantity()
  {
    return quantity;
  }

  public void setQuantity( BigDecimal quantity )
  {
    this.quantity = quantity;
  }

  public Date getSubmissionDate()
  {
    return submissionDate;
  }

  public void setSubmissionDate( Date submissionDate )
  {
    this.submissionDate = submissionDate;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

}
