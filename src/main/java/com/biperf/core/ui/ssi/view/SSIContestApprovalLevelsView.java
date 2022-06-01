
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * SSIContestApprovalLevelsView.
 * 
 * @author kandhi
 * @since Dec 15, 2014
 * @version 1.0
 */
public class SSIContestApprovalLevelsView
{
  private Long id;
  private String name;
  private boolean approved;
  private String approvedBy;
  private List<SSIContestApprover> approverList = new ArrayList<SSIContestApprover>();

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public boolean isApproved()
  {
    return approved;
  }

  public void setApproved( boolean approved )
  {
    this.approved = approved;
  }

  public String getApprovedBy()
  {
    return approvedBy;
  }

  public List<SSIContestApprover> getApproverList()
  {
    return approverList;
  }

  public void setApproverList( List<SSIContestApprover> approverList )
  {
    this.approverList = approverList;
  }

  public void setApprovedBy( String approvedBy )
  {
    this.approvedBy = approvedBy;
  }

  public class SSIContestApprover
  {
    private Long id;
    private String name;

    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    public String getName()
    {
      return name;
    }

    public void setName( String name )
    {
      this.name = name;
    }
  }

}
