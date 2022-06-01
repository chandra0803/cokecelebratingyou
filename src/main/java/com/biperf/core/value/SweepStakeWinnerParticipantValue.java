
package com.biperf.core.value;

import com.biperf.core.domain.participant.Participant;

/**
 * SweepStakeWinnerParticipantValue.
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
 * <td>attada</td>
 * <td>Feb 10, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SweepStakeWinnerParticipantValue
{
  private Participant participant;
  private String eligibilityType;

  /**
   * @return String
   */
  public String getEligibilityType()
  {
    return eligibilityType;
  }

  /**
   * @param eligibilityType
   */
  public void setEligibilityType( String eligibilityType )
  {
    this.eligibilityType = eligibilityType;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

}
