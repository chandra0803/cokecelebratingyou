
package com.biperf.core.value.survey;

import java.math.BigDecimal;

public class StandardDeviationReportValue
{
  private String nodeName;
  private BigDecimal meanValue;
  private BigDecimal standardDeviation;

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public BigDecimal getMeanValue()
  {
    return meanValue;
  }

  public void setMeanValue( BigDecimal meanValue )
  {
    this.meanValue = meanValue;
  }

  public BigDecimal getStandardDeviation()
  {
    return standardDeviation;
  }

  public void setStandardDeviation( BigDecimal standardDeviation )
  {
    this.standardDeviation = standardDeviation;
  }

}
