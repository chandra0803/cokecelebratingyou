
package com.biperf.core.service.participant;

import java.util.Collection;
import java.util.List;

import com.biperf.core.domain.user.UserCountryChanges;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.CampaignTransferValueBean;

public interface UserCountryChangesService extends SAO
{
  public static final String BEAN_NAME = "userCountryChangesService";

  public boolean processCampaignTransfers( UserCountryChanges userCountryChanges ) throws ServiceErrorException;

  public List<UserCountryChanges> getUsersToMoveBalance() throws ServiceErrorException;

  public CampaignTransferValueBean buildValueBean( UserCountryChanges userCountryChanges, CampaignTransferValueBean valueBean ) throws ServiceErrorException;

  public void emailExtract( Collection valueBeanList );

  /**
   * @return List of userCountryChanges IDs
   */
  public List<Long> getUCCsWithAccountBalancesToTranfer();

  /**
   * @param uccId
   * @return boolean
   * @throws ServiceErrorException
   */
  public boolean processAccountBalanceTransfer( Long uccId ) throws ServiceErrorException;
}
