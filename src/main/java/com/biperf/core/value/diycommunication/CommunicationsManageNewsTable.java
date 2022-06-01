
package com.biperf.core.value.diycommunication;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.diycommunications.DIYCommunications;

public class CommunicationsManageNewsTable extends CommunicationsManageTable
{
  private static final long serialVersionUID = 1L;

  private DIYNewsListTabularData tabularData;
  private int storiesPerPage;

  public CommunicationsManageNewsTable()
  {
    tabularData = new DIYNewsListTabularData();
  }

  public CommunicationsManageNewsTable( HttpServletRequest request, List<DIYCommunications> communicationList, boolean isActive )
  {
    this.tabularData = new DIYNewsListTabularData( request, communicationList, isActive );
    setTotal( communicationList.size() );
    this.setStoriesPerPage( 15 );
    setCurrentPage( 1 );
    setSortedOn( "1" );
    setSortedBy( "asc" );
  }

  public DIYNewsListTabularData getTabularData()
  {
    return tabularData;
  }

  public void setTabularData( DIYNewsListTabularData tabularData )
  {
    this.tabularData = tabularData;
  }

  public void setStoriesPerPage( int storiesPerPage )
  {
    this.storiesPerPage = storiesPerPage;
  }

  public int getStoriesPerPage()
  {
    return storiesPerPage;
  }

}
