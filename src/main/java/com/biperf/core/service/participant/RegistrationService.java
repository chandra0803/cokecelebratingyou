
package com.biperf.core.service.participant;

import java.util.Map;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;

public interface RegistrationService extends SAO
{
  /**
   * BEAN_NAME
   */
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "registrationService";

  /**
   * Ensures the registration code entered is valid.
   * 
   * @param registrationCode
   * @throws ServiceErrorException
   */
  public void validateRegistrationCode( String registrationCode ) throws ServiceErrorException;

  /**
   * Ensures the password entered is valid.
   * 
   * @param password
   * @throws ServiceErrorException
   */
  public void validatePassword( String password ) throws ServiceErrorException;

  /**
  * This method will return a map of promotion id, audience id and node id for the specified registration code.
  * @param code
  * @return Map
  */
  public Map getRegistrationInfoByRegistrationCode( String code );

  /**
   * Enrolls a participant, sends welcome email and logs that person in.
   * 
   * @param participant
   * @param promotionId
   * @param audienceId
   * @throws ServiceErrorException
   */
  public void enroll( Participant participant ) throws ServiceErrorException;

  public void sendWelcomeEamiltoRosterMgmtPax( Participant participant ) throws ServiceErrorException;

}
