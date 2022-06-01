
package com.biperf.core.ui.ssi.view;

import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

public class SSIPaxContestMasterModuleView
{

  private List<WebErrorMessage> messages;
  private int createListAfter;
  private List<SSIPaxContestDataView> masterModuleList;

  public SSIPaxContestMasterModuleView()
  {

  }

  public SSIPaxContestMasterModuleView( int createListAfter, List<SSIPaxContestDataView> masterModuleList )
  {
    this.createListAfter = createListAfter;
    this.masterModuleList = masterModuleList;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public int getCreateListAfter()
  {
    return createListAfter;
  }

  public void setCreateListAfter( int createListAfter )
  {
    this.createListAfter = createListAfter;
  }

  public List<SSIPaxContestDataView> getMasterModuleList()
  {
    return masterModuleList;
  }

  public void setMasterModuleList( List<SSIPaxContestDataView> masterModuleList )
  {
    this.masterModuleList = masterModuleList;
  }

}
