
package com.biperf.core.domain.challengepoint;

import java.math.BigDecimal;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;

/**
 * ChallengepointAward domain object.
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
public class ChallengepointAward extends BaseDomain implements Cloneable
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
   * The Result calculated at the time of issuance.
   */
  private BigDecimal result;

  /*
   * Basic Award earned
   */
  private Long awardEarned;

  /*
   * Award Issued
   */
  private Long awardIssued;

  /*
   * Total Award with Challengepoint earned
   */
  private Long totalAwardIssued;

  /*
   * Corresponding Journal Entry
   */
  private Journal journal;

  /*
   * award Type
   */
  private String awardType;

  public ChallengepointAward()
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

    if ( ! ( object instanceof ChallengepointAward ) )
    {
      return false;
    }

    ChallengepointAward otherChallengepointAward = (ChallengepointAward)object;

    if ( otherChallengepointAward.getChallengePointPromotion() != null && !otherChallengepointAward.getChallengePointPromotion().equals( this.getChallengePointPromotion() ) )
    {

      equals = false;
    }

    if ( otherChallengepointAward.getParticipant() != null && !otherChallengepointAward.getParticipant().equals( this.getParticipant() ) )
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
    resultHS = 29 * resultHS + ( getTotalAwardIssued() != null ? getTotalAwardIssued().hashCode() : 0 );

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

  public Long getAwardEarned()
  {
    return awardEarned;
  }

  public void setAwardEarned( Long awardEarned )
  {
    this.awardEarned = awardEarned;
  }

  public Long getAwardIssued()
  {
    return awardIssued;
  }

  public void setAwardIssued( Long awardIssued )
  {
    this.awardIssued = awardIssued;
  }

  public BigDecimal getResult()
  {
    return result;
  }

  public void setResult( BigDecimal result )
  {
    this.result = result;
  }

  public Long getTotalAwardIssued()
  {
    return totalAwardIssued;
  }

  public void setTotalAwardIssued( Long totalAwardIssued )
  {
    this.totalAwardIssued = totalAwardIssued;
  }

  public Journal getJournal()
  {
    return journal;
  }

  public void setJournal( Journal journal )
  {
    this.journal = journal;
  }

  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

}
