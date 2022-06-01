
package com.biperf.core.service.strongmail;

import java.util.Map;

import com.biperf.core.domain.message.Message;
import com.biperf.core.service.SAO;

public interface StrongMailService extends SAO
{

  public boolean sendWelcomeEmail( Message message, String processName );

  public void truncateStrongMailUserTable();

  public Map<String, Object> executeWelcomeEmailProcedure();
}
