/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.value.nomination.CumulativeApprovalNominatorInfoValueBean;

/**
 * 
 * @author poddutur
 * @since Sep 12, 2016
 */
public class CumulativeInfoTableDataValueBean
{
  private List<CumulativeApprovalNominatorInfoValueBean> cumulativeApprovalNominatorInfoList = new ArrayList<CumulativeApprovalNominatorInfoValueBean>();
  private List<NominationsApprovalCustomValueBean> nominationsApprovalCustomList = new ArrayList<NominationsApprovalCustomValueBean>();

  public List<CumulativeApprovalNominatorInfoValueBean> getCumulativeApprovalNominatorInfoList()
  {
    return cumulativeApprovalNominatorInfoList;
  }

  public void setCumulativeApprovalNominatorInfoList( List<CumulativeApprovalNominatorInfoValueBean> cumulativeApprovalNominatorInfoList )
  {
    this.cumulativeApprovalNominatorInfoList = cumulativeApprovalNominatorInfoList;
  }

  public List<NominationsApprovalCustomValueBean> getNominationsApprovalCustomList()
  {
    return nominationsApprovalCustomList;
  }

  public void setNominationsApprovalCustomList( List<NominationsApprovalCustomValueBean> nominationsApprovalCustomList )
  {
    this.nominationsApprovalCustomList = nominationsApprovalCustomList;
  }

}
