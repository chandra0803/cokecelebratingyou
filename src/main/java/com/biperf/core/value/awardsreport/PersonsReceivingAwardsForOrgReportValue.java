/**
 * 
 */

package com.biperf.core.value.awardsreport;

/**
 * @author poddutur
 *
 */
public class PersonsReceivingAwardsForOrgReportValue
{
  private String orgName;
  private Long personsCnt;

  public PersonsReceivingAwardsForOrgReportValue()
  {
    super();
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public Long getPersonsCnt()
  {
    return personsCnt;
  }

  public void setPersonsCnt( Long personsCnt )
  {
    this.personsCnt = personsCnt;
  }

}
