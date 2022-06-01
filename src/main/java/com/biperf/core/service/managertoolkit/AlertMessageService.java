
package com.biperf.core.service.managertoolkit;

import java.util.List;

import com.biperf.core.domain.managertoolkit.AlertMessage;
import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.service.SAO;

public interface AlertMessageService extends SAO
{
  public static final String BEAN_NAME = "alertMessageService";

  public List<ParticipantAlert> getActiveAlertMessagesByUserId( Long userId );

  public void createManagerSendAlert( AlertMessage alertMessage, Long managerId, Long proxyId, boolean includeChildNode );

  public List<AlertMessage> getAlertMessageByContestId( Long contestId );

}
