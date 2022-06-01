
package com.biperf.core.value;

import java.util.ArrayList;
import java.util.List;

public class PendingNominationsApprovalMainValueBean
{
  List<NominationsApprovalValueBean> pendingNominationsApprovalslist = new ArrayList<NominationsApprovalValueBean>();

  public List<NominationsApprovalValueBean> getPendingNominationsApprovalslist()
  {
    return pendingNominationsApprovalslist;
  }

  public void setPendingNominationsApprovalslist( List<NominationsApprovalValueBean> pendingNominationsApprovalslist )
  {
    this.pendingNominationsApprovalslist = pendingNominationsApprovalslist;
  }
}
