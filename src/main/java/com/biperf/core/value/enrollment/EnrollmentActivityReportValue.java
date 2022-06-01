
package com.biperf.core.value.enrollment;

import java.math.BigDecimal;

public class EnrollmentActivityReportValue
{
  // TABULAR DATA
  private String hierarchyNodeName;
  private Long hierarchyNodeId;
  private Long activeCnt;
  private Long inactiveCnt;
  private Long totalCnt;
  private Boolean isLeaf;
  private Boolean isTeam;

  // DETAIL TABULAR DATA
  private String paxName;
  private String enrollmentDate;
  private String status;
  private String jobPosition;
  private String role;
  private String department;
  private String nodeName;
  private String country;

  // CHARTS
  private Long statusCnt;
  private String month;
  private BigDecimal activePct;
  private BigDecimal inActivePct;

  public String getHierarchyNodeName()
  {
    return hierarchyNodeName;
  }

  public void setHierarchyNodeName( String hierarchyNodeName )
  {
    this.hierarchyNodeName = hierarchyNodeName;
  }

  public Long getHierarchyNodeId()
  {
    return hierarchyNodeId;
  }

  public void setHierarchyNodeId( Long hierarchyNodeId )
  {
    this.hierarchyNodeId = hierarchyNodeId;
  }

  public Long getActiveCnt()
  {
    return activeCnt;
  }

  public void setActiveCnt( Long activeCnt )
  {
    this.activeCnt = activeCnt;
  }

  public Long getInactiveCnt()
  {
    return inactiveCnt;
  }

  public void setInactiveCnt( Long inactiveCnt )
  {
    this.inactiveCnt = inactiveCnt;
  }

  public Long getTotalCnt()
  {
    return totalCnt;
  }

  public void setTotalCnt( Long totalCnt )
  {
    this.totalCnt = totalCnt;
  }

  public Boolean getIsLeaf()
  {
    return isLeaf;
  }

  public void setIsLeaf( Boolean isLeaf )
  {
    this.isLeaf = isLeaf;
  }

  public Boolean getIsTeam()
  {
    return isTeam;
  }

  public void setIsTeam( Boolean isTeam )
  {
    this.isTeam = isTeam;
  }

  public String getPaxName()
  {
    return paxName;
  }

  public void setPaxName( String paxName )
  {
    this.paxName = paxName;
  }

  public String getEnrollmentDate()
  {
    return enrollmentDate;
  }

  public void setEnrollmentDate( String enrollmentDate )
  {
    this.enrollmentDate = enrollmentDate;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getJobPosition()
  {
    return jobPosition;
  }

  public void setJobPosition( String jobPosition )
  {
    this.jobPosition = jobPosition;
  }

  public String getRole()
  {
    return role;
  }

  public void setRole( String role )
  {
    this.role = role;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

  public Long getStatusCnt()
  {
    return statusCnt;
  }

  public void setStatusCnt( Long statusCnt )
  {
    this.statusCnt = statusCnt;
  }

  public String getMonth()
  {
    return month;
  }

  public void setMonth( String month )
  {
    this.month = month;
  }

  public BigDecimal getActivePct()
  {
    return activePct;
  }

  public void setActivePct( BigDecimal activePct )
  {
    this.activePct = activePct;
  }

  public BigDecimal getInActivePct()
  {
    return inActivePct;
  }

  public void setInActivePct( BigDecimal inActivePct )
  {
    this.inActivePct = inActivePct;
  }

}
