
package com.biperf.core.strategy;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.participant.Participant;

/**
 * This class will contain methods relating to management of participant
 * self-enrollment (registration) to use the site.
 * 
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * </table>

 */
public interface RegistrationStrategy extends Strategy
{

  /**
   * This method will check the given registration code to see if it fits the validation rules. If not, it
   * will return a List of validationErrors.
   * 
   * Overridden from @see com.biperf.core.strategy.RegistrationStrategy#getRegistrationCodeValidationErrors(java.lang.String)
   * 
   * @param registrationCode
   * @return the list of errors, or an empty list if no errors
   */
  public List getRegistrationCodeValidationErrors( String registrationCode );

  /**
   * This method will return promotion id, audience id and node id for the specified registration code.
   * @param code
   * @return Map
   */
  public Map getRegistrationInfoByRegistrationCode( String code );

  /**
   * This method will insert the participant and its relevant information to the database.
   * 
   * @param participant
   * @param promotionId
   * @param audienceId
   * @return the list of errors, or an empty list if no errors
   */
  public List enrollParticipant( Participant participant );

}
