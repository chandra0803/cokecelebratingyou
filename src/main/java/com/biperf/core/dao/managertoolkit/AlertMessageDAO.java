
package com.biperf.core.dao.managertoolkit;

import java.util.List;

import com.biperf.core.domain.managertoolkit.AlertMessage;
import com.biperf.core.service.SAO;

public interface AlertMessageDAO extends SAO
{
  public static final String BEAN_NAME = "alertMessageDAO";

  public AlertMessage getAlertMessageById( Long id );

  public List getAlertMessageByUserId( Long userId );

  public List<AlertMessage> getAlertMessageByContestId( Long contestId );

}
