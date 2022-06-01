
package com.biperf.core.value.plateauawards;

import java.math.BigDecimal;

public class PlateauAwardItemSelectionReportValue
{

  private String levelName;
  private String itemName;
  private String itemNum;
  private Long selectionCnt;
  private BigDecimal selectionPct;

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public String getItemName()
  {
    return itemName;
  }

  public void setItemName( String itemName )
  {
    this.itemName = itemName;
  }

  public String getItemNum()
  {
    return itemNum;
  }

  public void setItemNum( String itemNum )
  {
    this.itemNum = itemNum;
  }

  public Long getSelectionCnt()
  {
    return selectionCnt;
  }

  public void setSelectionCnt( Long selectionCnt )
  {
    this.selectionCnt = selectionCnt;
  }

  public BigDecimal getSelectionPct()
  {
    return selectionPct;
  }

  public void setSelectionPct( BigDecimal selectionPct )
  {
    this.selectionPct = selectionPct;
  }

}
