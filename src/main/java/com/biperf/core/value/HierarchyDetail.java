/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.util.UUID;

public class HierarchyDetail
{
  private String companyId;
  private UUID rosterNodeId;
  private String role;
  private String name;
  private UUID rosterHierarchyId;
  private UUID parentRosterNodeId;

  public String getCompanyId()
  {
    return companyId;
  }

  public void setCompanyId( String companyId )
  {
    this.companyId = companyId;
  }

  public UUID getRosterNodeId()
  {
    return rosterNodeId;
  }

  public void setRosterNodeId( UUID rosterNodeId )
  {
    this.rosterNodeId = rosterNodeId;
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

  public UUID getRosterHierarchyId()
  {
    return rosterHierarchyId;
  }

  public void setRosterHierarchyId( UUID rosterHierarchyId )
  {
    this.rosterHierarchyId = rosterHierarchyId;
  }

  public UUID getParentRosterNodeId()
  {
    return parentRosterNodeId;
  }

  public void setParentRosterNodeId( UUID parentRosterNodeId )
  {
    this.parentRosterNodeId = parentRosterNodeId;
  }

}
