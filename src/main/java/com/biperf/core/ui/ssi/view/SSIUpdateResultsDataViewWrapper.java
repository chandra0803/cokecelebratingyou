
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

public class SSIUpdateResultsDataViewWrapper
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private SSIUpdateResultsDataView response;

  public SSIUpdateResultsDataViewWrapper()
  {
  }

  public SSIUpdateResultsDataViewWrapper( SSIUpdateResultsDataView response )
  {
    super();
    this.response = response;
  }

  public SSIUpdateResultsDataViewWrapper( SSIUpdateResultsDataView response, List<WebErrorMessage> messages )
  {
    super();
    this.messages = messages;
    this.response = response;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public SSIUpdateResultsDataView getResponse()
  {
    return response;
  }

  public void setResponse( SSIUpdateResultsDataView response )
  {
    this.response = response;
  }

}
