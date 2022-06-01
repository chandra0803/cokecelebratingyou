
package com.biperf.core.value.diycommunication;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.diycommunications.DIYCommunications;

public class CommunicationsManageBannerTable extends CommunicationsManageTable
{
  private static final long serialVersionUID = 1L;

  private DIYBannerListTabularData tabularData;
  private int bannersPerPage;

  public CommunicationsManageBannerTable()
  {
    tabularData = new DIYBannerListTabularData();
  }

  public CommunicationsManageBannerTable( HttpServletRequest request, List<DIYCommunications> communicationList, boolean isActive )
  {
    this.tabularData = new DIYBannerListTabularData( request, communicationList, isActive );
    setTotal( communicationList.size() );
    this.bannersPerPage = 15;
    setCurrentPage( 1 );
    setSortedOn( "1" );
    setSortedBy( "asc" );
  }

  public DIYBannerListTabularData getTabularData()
  {
    return tabularData;
  }

  public void setTabularData( DIYBannerListTabularData tabularData )
  {
    this.tabularData = tabularData;
  }

  public int getBannersPerPage()
  {
    return bannersPerPage;
  }

  public void setBannersPerPage( int bannersPerPage )
  {
    this.bannersPerPage = bannersPerPage;
  }

}
