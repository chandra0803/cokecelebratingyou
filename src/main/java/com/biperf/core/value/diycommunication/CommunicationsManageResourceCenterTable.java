
package com.biperf.core.value.diycommunication;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.diycommunications.DIYCommunications;

public class CommunicationsManageResourceCenterTable extends CommunicationsManageTable
{
  private static final long serialVersionUID = 1L;

  private DIYResourceCenterListTabularData tabularData;
  private int resourcesPerPage;

  public CommunicationsManageResourceCenterTable()
  {
    tabularData = new DIYResourceCenterListTabularData();
  }

  public CommunicationsManageResourceCenterTable( List<DIYCommunications> communicationList, HttpServletRequest request, boolean isActive )
  {
    this.tabularData = new DIYResourceCenterListTabularData( communicationList, request, isActive );
    setTotal( communicationList.size() );
    setResourcesPerPage( 15 );
    setCurrentPage( 1 );
    setSortedOn( "1" );
    setSortedBy( "asc" );
  }

  public DIYResourceCenterListTabularData getTabularData()
  {
    return tabularData;
  }

  public void setTabularData( DIYResourceCenterListTabularData tabularData )
  {
    this.tabularData = tabularData;
  }

  public int getResourcesPerPage()
  {
    return resourcesPerPage;
  }

  public void setResourcesPerPage( int resourcesPerPage )
  {
    this.resourcesPerPage = resourcesPerPage;
  }

}
