
package com.biperf.core.ui.engagement;

/**
 * 
 * EngagementTeamMetaView.
 * 
 * @author kandhi
 * @since May 29, 2014
 * @version 1.0
 */
public class EngagementTeamMetaView
{
  private static final int TEAM_MEMBERS_PER_PAGE = 10;

  private int count;
  private int perPage;
  private int page;
  private String sortedOn;
  private String sortedBy;

  public EngagementTeamMetaView( int count, int page, String sortedOn, String sortedBy )
  {
    super();
    this.count = count;
    this.perPage = TEAM_MEMBERS_PER_PAGE;
    this.page = page;
    this.sortedOn = sortedOn;
    this.sortedBy = sortedBy;
  }

  public int getCount()
  {
    return count;
  }

  public void setCount( int count )
  {
    this.count = count;
  }

  public int getPerPage()
  {
    return perPage;
  }

  public void setPerPage( int perPage )
  {
    this.perPage = perPage;
  }

  public int getPage()
  {
    return page;
  }

  public void setPage( int page )
  {
    this.page = page;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

}
