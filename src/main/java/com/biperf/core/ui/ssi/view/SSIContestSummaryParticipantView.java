
package com.biperf.core.ui.ssi.view;

/**
 * 
 * SSIContestAdminDetailOrgUnitView.
 * orgUnits or participants View
 * @author chowdhur
 * @since Jan 13, 2015
 */
public class SSIContestSummaryParticipantView
{

  private String contestType;
  private String payoutType;
  private boolean includeBonus;
  private boolean includeBaseline;
  private SSIContestAdminDetailTabularDataView tabularData;
  private int total;
  private int perPage;
  private int current;
  private String sortedBy; // asc/desc
  private String sortedOn;
  private String role;

  public static final String SORTED_BY_ASC = "asc";
  public static final String SORTED_BY_DESC = "desc";

  public SSIContestSummaryParticipantView()
  {

  }

  /**
   * @param contestType
   * @param payoutType
   * @param includeBonus
   * @param tabularData
   * @param total
   * @param perPage
   * @param current
   * @param sortedBy
   * @param sortedOn
   */
  public SSIContestSummaryParticipantView( String contestType,
                                           String payoutType,
                                           boolean includeBonus,
                                           boolean includeBaseline,
                                           SSIContestAdminDetailTabularDataView tabularData,
                                           int total,
                                           int perPage,
                                           int current,
                                           String sortedBy,
                                           String sortedOn )
  {
    super();
    this.contestType = contestType;
    this.payoutType = payoutType;
    this.includeBonus = includeBonus;
    this.includeBaseline = includeBaseline;
    this.tabularData = tabularData;
    this.total = total;
    this.perPage = perPage;
    this.current = current;
    this.sortedBy = sortedBy;
    this.sortedOn = sortedOn;
  }

  public String getContestType()
  {
    return contestType;
  }

  public void setContestType( String contestType )
  {
    this.contestType = contestType;
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public boolean isIncludeBonus()
  {
    return includeBonus;
  }

  public void setIncludeBonus( boolean includeBonus )
  {
    this.includeBonus = includeBonus;
  }

  public boolean isIncludeBaseline()
  {
    return includeBaseline;
  }

  public void setIncludeBaseline( boolean includeBaseline )
  {
    this.includeBaseline = includeBaseline;
  }

  public SSIContestAdminDetailTabularDataView getTabularData()
  {
    return tabularData;
  }

  public void setTabularData( SSIContestAdminDetailTabularDataView tabularData )
  {
    this.tabularData = tabularData;
  }

  public int getTotal()
  {
    return total;
  }

  public void setTotal( int total )
  {
    this.total = total;
  }

  public int getPerPage()
  {
    return perPage;
  }

  public void setPerPage( int perPage )
  {
    this.perPage = perPage;
  }

  public int getCurrent()
  {
    return current;
  }

  public void setCurrent( int current )
  {
    this.current = current;
  }

  public String getSortedBy()
  {
    return sortedBy;
  }

  public void setSortedBy( String sortedBy )
  {
    this.sortedBy = sortedBy;
  }

  public String getSortedOn()
  {
    return sortedOn;
  }

  public void setSortedOn( String sortedOn )
  {
    this.sortedOn = sortedOn;
  }

  public String getRole()
  {
    return role;
  }

  public void setRole( String role )
  {
    this.role = role;
  }

}
