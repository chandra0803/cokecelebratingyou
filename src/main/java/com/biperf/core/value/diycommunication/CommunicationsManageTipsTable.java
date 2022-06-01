
package com.biperf.core.value.diycommunication;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.diycommunications.DIYCommunications;

public class CommunicationsManageTipsTable extends CommunicationsManageTable
{
  private static final long serialVersionUID = 1L;

  private DIYTipsListTabularData tabularData;
  private int tipsPerPage;

  public CommunicationsManageTipsTable()
  {
    tabularData = new DIYTipsListTabularData();
  }

  public CommunicationsManageTipsTable( List<DIYCommunications> communicationList, HttpServletRequest request, boolean isActive )
  {
    this.tabularData = new DIYTipsListTabularData( communicationList, request, isActive );
    setTotal( communicationList.size() );
    setTipsPerPage( 15 );
    setCurrentPage( 1 );
    setSortedOn( "1" );
    setSortedBy( "asc" );
  }

  public DIYTipsListTabularData getTabularData()
  {
    return tabularData;
  }

  public void setTabularData( DIYTipsListTabularData tabularData )
  {
    this.tabularData = tabularData;
  }

  public int getTipsPerPage()
  {
    return tipsPerPage;
  }

  public void setTipsPerPage( int tipsPerPage )
  {
    this.tipsPerPage = tipsPerPage;
  }

}
