/**
 * 
 */

package com.biperf.core.domain.promotion;

import java.sql.Timestamp;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;

/**
 * PromotionSweepstakeWinner.
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
 * <td>asondgeroth</td>
 * <td>Nov 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionSweepstakeWinner extends BaseDomain
{
  public static final String GIVER_TYPE = "Giver";
  public static final String RECEIVER_TYPE = "Receiver";

  public static final String SUBMITTERS_TYPE = "Submitter";
  public static final String TEAM_MEMBERS_TYPE = "Teammember";

  public static final String NOMINEE_TYPE = "Nominee";
  public static final String NOMINATOR_TYPE = "Nominator";

  public static final String SURVEY_TAKER_TYPE = "Survey";

  public static final String BADGE_WINNER = "Badge";

  private PromotionSweepstake promotionSweepstake;
  private Participant participant;
  private String winnerType;
  private boolean removed;
  private String guid;
  private Long consumerId;
  private ParticipantBadge participantBadge = null;

  /**
   * public constructor that sets the createdBy and DateCreated properties. We do this since
   * Hibernate does not treat this as an entity - thus the HibernateAuditInterceptor is not used.
   */
  public PromotionSweepstakeWinner()
  {
    this.guid = GuidUtils.generateGuid();
    getAuditCreateInfo().setCreatedBy( UserManager.getUserId() );
    getAuditCreateInfo().setDateCreated( new Timestamp( System.currentTimeMillis() ) );
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public PromotionSweepstake getPromotionSweepstake()
  {
    return promotionSweepstake;
  }

  public void setPromotionSweepstake( PromotionSweepstake promotionSweepstake )
  {
    this.promotionSweepstake = promotionSweepstake;
  }

  public boolean isRemoved()
  {
    return removed;
  }

  public void setRemoved( boolean removed )
  {
    this.removed = removed;
  }

  public String getWinnerType()
  {
    return winnerType;
  }

  public void setWinnerType( String winnerType )
  {
    this.winnerType = winnerType;
  }

  /**
   * Checks equality of the object parameter to this. Overridden from
   * 
   * @param object
   * @return boolean
   * @see com.biperf.core.domain.BaseDomain#equals(Object)
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof PromotionSweepstakeWinner ) )
    {
      return false;
    }

    final PromotionSweepstakeWinner winner = (PromotionSweepstakeWinner)object;

    if ( getGuid() != null ? !getGuid().equals( winner.getGuid() ) : winner.getGuid() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @return int
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   */
  public int hashCode()
  {
    return 29 * ( getGuid() != null ? getGuid().hashCode() : 0 );
  }

  public String getGuid()
  {
    return guid;
  }

  public void setGuid( String guid )
  {
    this.guid = guid;
  }

  public Long getConsumerId()
  {
    return consumerId;
  }

  public void setConsumerId( Long consumerId )
  {
    this.consumerId = consumerId;
  }

  public ParticipantBadge getParticipantBadge()
  {
    return participantBadge;
  }

  public void setParticipantBadge( ParticipantBadge participantBadge )
  {
    this.participantBadge = participantBadge;
  }
}
