
package com.biperf.core.ui.mobilerecogapp;

import com.biperf.core.ui.recognition.PromotionNodeCheckBean;
import com.biperf.core.value.RecognitionBean;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude( JsonInclude.Include.NON_NULL )
public class PromotionBean
{
  private final Long id;
  private Long nodeId;
  private final String name;
  private final int daysRemaining;

  private boolean awardAvailable;

  private BudgetInfo budgetInfo;

  private int totalSent;
  private int lastSent;

  public PromotionBean( RecognitionBean recognitionBean, Long nodeId, boolean isAwardAvailable )
  {
    id = recognitionBean.getId();
    name = recognitionBean.getName();
    daysRemaining = recognitionBean.getDaysRemaining();
    awardAvailable = isAwardAvailable;
  }

  public PromotionBean( RecognitionBean recognitionBean, PromotionNodeCheckBean pncb )
  {
    this( recognitionBean, pncb.getNode().getId(), true );
    this.budgetInfo = new BudgetInfo( pncb );
  }

  public Long getId()
  {
    return id;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public String getName()
  {
    return name;
  }

  public int getDaysRemaining()
  {
    return daysRemaining;
  }

  public boolean isAwardAvailable()
  {
    return awardAvailable;
  }

  public BudgetInfo getBudgetInfo()
  {
    return budgetInfo;
  }

  public int getTotalSent()
  {
    return totalSent;
  }

  public int getLastSent()
  {
    return lastSent;
  }

  public static class BudgetInfo
  {
    private final int amount;
    private final int pointsRemaining;

    public BudgetInfo( PromotionNodeCheckBean pncb )
    {
      this.amount = pncb.getNode().getAmount();
      this.pointsRemaining = pncb.getNode().getAmount() - pncb.getNode().getAmountUsed();
    }

    public int getAmount()
    {
      return amount;
    }

    public int getPointsRemaining()
    {
      return pointsRemaining;
    }
  }
}
