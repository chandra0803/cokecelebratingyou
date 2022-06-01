
package com.biperf.core.value;

/**
 * EngagementChartValueBean.
 * 
 * @author kandhi
 * @since May 23, 2014
 * @version 1.0
 */
public class EngagementChartValueBean
{
  private String label;
  private int value;
  private int companyAverage;

  public String getLabel()
  {
    return label;
  }

  public void setLabel( String label )
  {
    this.label = label;
  }

  public int getValue()
  {
    return value;
  }

  public void setValue( int value )
  {
    this.value = value;
  }

  public int getCompanyAverage()
  {
    return companyAverage;
  }

  public void setCompanyAverage( int companyAverage )
  {
    this.companyAverage = companyAverage;
  }

}
