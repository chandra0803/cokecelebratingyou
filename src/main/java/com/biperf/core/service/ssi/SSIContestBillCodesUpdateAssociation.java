
package com.biperf.core.service.ssi;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.service.UpdateAssociationRequest;
import com.biperf.core.utils.HibernateSessionManager;

public class SSIContestBillCodesUpdateAssociation extends UpdateAssociationRequest
{

  public SSIContestBillCodesUpdateAssociation( SSIContest detachedContest )
  {
    super( detachedContest );
  }

  public void execute( BaseDomain attachedDomain )
  {
    SSIContest attachedContest = (SSIContest)attachedDomain;
    updateBillCodes( attachedContest );
  }

  private void updateBillCodes( SSIContest attachedContest )
  {
    SSIContest detachedContest = (SSIContest)getDetachedDomain();
    attachedContest.getContestBillCodes().clear();
    flushHibernateSession();
    detachedContest.getContestBillCodes().stream().forEach( e -> e.setSsiContest( attachedContest ) );
    attachedContest.getContestBillCodes().addAll( detachedContest.getContestBillCodes() );
  }
  
  protected void flushHibernateSession()
  {
    HibernateSessionManager.getSession().flush();
  }

}
