
package com.biperf.core.service.process;

import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.AdminTestProcessParmBean;

/**
 * 
 * AdminTestProcessService.
 * 
 * @author bethke
 * @since Mar 4, 2016
 */
public interface AdminTestProcessService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "adminTestProcessService";

  public MailingRecipient buildRecognitionMailingRecipient( Participant recipient, AdminTestProcessParmBean parmBean ) throws ServiceErrorException;

  public MailingRecipient buildCelebrationManagerMailingRecipient( Participant sendToUser, AdminTestProcessParmBean parmBean ) throws ServiceErrorException;

}
