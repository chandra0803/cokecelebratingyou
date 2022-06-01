/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author poddutur
 * @since Apr 21, 2016
 */
public class NominationsApprovalPageDataValueBean
{
  private List<NominationsApprovalPagePromotionLevelsValueBean> promotionLevelsList = new ArrayList<NominationsApprovalPagePromotionLevelsValueBean>();
  private List<NominationsApprovalPageTimePeriodsValueBean> timePeriodsList = new ArrayList<NominationsApprovalPageTimePeriodsValueBean>();
  private List<NominationsApprovalPageDetailsValueBean> detailsList = new ArrayList<NominationsApprovalPageDetailsValueBean>();

  public List<NominationsApprovalPageDetailsValueBean> getDetailsList()
  {
    return detailsList;
  }

  public void setDetailsList( List<NominationsApprovalPageDetailsValueBean> detailsList )
  {
    this.detailsList = detailsList;
  }

  public List<NominationsApprovalPageTimePeriodsValueBean> getTimePeriodsList()
  {
    return timePeriodsList;
  }

  public void setTimePeriodsList( List<NominationsApprovalPageTimePeriodsValueBean> timePeriodsList )
  {
    this.timePeriodsList = timePeriodsList;
  }

  public List<NominationsApprovalPagePromotionLevelsValueBean> getPromotionLevelsList()
  {
    return promotionLevelsList;
  }

  public void setPromotionLevelsList( List<NominationsApprovalPagePromotionLevelsValueBean> promotionLevelsList )
  {
    this.promotionLevelsList = promotionLevelsList;
  }

}
