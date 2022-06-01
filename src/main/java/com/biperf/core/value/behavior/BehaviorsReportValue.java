
package com.biperf.core.value.behavior;

/**
 * Value bean for Behaviors report
 */
public class BehaviorsReportValue
{
  private String summaryType;
  private String behavior;
  private Long bCnt;
  private Boolean isLeaf;
  private Long headerNodeId;
  private Long parentNodeId;
  private Long detailNodeId;
  private Long hierarchyLevel;
  private String nodeName;
  private String nodeTypeName;

  private Long totalRecords;

  public String getSummaryType()
  {
    return summaryType;
  }

  public void setSummaryType( String summaryType )
  {
    this.summaryType = summaryType;
  }

  public String getBehavior()
  {
    return behavior;
  }

  public void setBehavior( String behavior )
  {
    this.behavior = behavior;
  }

  public Long getbCnt()
  {
    return bCnt;
  }

  public void setbCnt( Long bCnt )
  {
    this.bCnt = bCnt;
  }

  public Boolean getIsLeaf()
  {
    return isLeaf;
  }

  public void setIsLeaf( Boolean isLeaf )
  {
    this.isLeaf = isLeaf;
  }

  public Long getHeaderNodeId()
  {
    return headerNodeId;
  }

  public void setHeaderNodeId( Long headerNodeId )
  {
    this.headerNodeId = headerNodeId;
  }

  public Long getParentNodeId()
  {
    return parentNodeId;
  }

  public void setParentNodeId( Long parentNodeId )
  {
    this.parentNodeId = parentNodeId;
  }

  public Long getDetailNodeId()
  {
    return detailNodeId;
  }

  public void setDetailNodeId( Long detailNodeId )
  {
    this.detailNodeId = detailNodeId;
  }

  public Long getHierarchyLevel()
  {
    return hierarchyLevel;
  }

  public void setHierarchyLevel( Long hierarchyLevel )
  {
    this.hierarchyLevel = hierarchyLevel;
  }

  public String getNodeName()
  {
    return nodeName;
  }

  public void setNodeName( String nodeName )
  {
    this.nodeName = nodeName;
  }

  public String getNodeTypeName()
  {
    return nodeTypeName;
  }

  public void setNodeTypeName( String nodeTypeName )
  {
    this.nodeTypeName = nodeTypeName;
  }

  public Long getTotalRecords()
  {
    return totalRecords;
  }

  public void setTotalRecords( Long totalRecords )
  {
    this.totalRecords = totalRecords;
  }

}
