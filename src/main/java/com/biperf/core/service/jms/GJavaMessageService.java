
package com.biperf.core.service.jms;

import com.biperf.core.service.SAO;
import com.biperf.core.utils.jms.JmsMessage;
import com.biperf.core.utils.jms.JsonJmsMessage;

public interface GJavaMessageService extends SAO
{
  String BEAN_NAME = "javaMessageService";

  public void sendToJmsQueue( JmsMessage message );

  public void sendToJmsTopic( JmsMessage message );

  public void sendToJmsTopic( JsonJmsMessage message );

}
