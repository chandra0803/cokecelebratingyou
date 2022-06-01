/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.roster.value;

public class HierarchyDetails
{
  private String companyId;
  private String nodeUUID;
  private String role;
  private String name;
  private String hierarchyUUID;
  private String parentNodeUUID;

  public String getCompanyId()
  {
    return companyId;
  }

  public void setCompanyId( String companyId )
  {
    this.companyId = companyId;
  }

  public String getNodeUUID()
  {
    return nodeUUID;
  }

  public void setNodeUUID( String nodeUUID )
  {
    this.nodeUUID = nodeUUID;
  }

  public String getRole()
  {
    return role;
  }

  public void setRole( String role )
  {
    this.role = role;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getHierarchyUUID()
  {
    return hierarchyUUID;
  }

  public void setHierarchyUUID( String hierarchyUUID )
  {
    this.hierarchyUUID = hierarchyUUID;
  }

  public String getParentNodeUUID()
  {
    return parentNodeUUID;
  }

  public void setParentNodeUUID( String parentNodeUUID )
  {
    this.parentNodeUUID = parentNodeUUID;
  }

}
