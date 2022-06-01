
package com.biperf.core.service.email;

import java.util.Map;

import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.service.SAO;

public interface PersonalizationService extends SAO
{

  public static String BEAN_NAME = "personalizationService";

  /**
   * Process mailing level data
   * 
   * @param mailing
   * @param objectMap This is the map of objects that will be used to populate the generic dynamic
   *          elements in the message. Generic in this context means that it is not user-specific.
   *          if a dynamic element is put in that references an object that is found here it will
   *          not work. Reflection is used to get the value off of the objects given. The code in
   *          the message looks like this: {promotion:madeUpMethod} - where promotion = object and
   *          madeUpMethod = method. Method should be in camel case. and will map to
   * @return Mailing
   */
  public Mailing preProcessMailing( Mailing mailing, Map objectMap );

  /**
   * Personalize input string (target) given a mailingRecipient which has recipient specific data.
   * 
   * @param mailingRecipient
   * @param target
   * @return String
   */
  public String personalize( MailingRecipient mailingRecipient, String target );

  public String personalize( MailingRecipient mailingRecipient, String target, boolean maskSecureFields );

  /**
   * Process message using velocity
   * 
   * @param objectMap Objects containing data to load from
   * @param logString
   * @param targetString
   * @return String
   */
  public String processMessage( Map objectMap, String logString, String targetString );

  public String processMessage( Map objectMap, String logString, String targetString, boolean maskSecureFields );
}
