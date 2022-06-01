
package com.biperf.core.value.ssi;

import java.util.List;

/**
 * @author dudam
 * @since Nov 11, 2014
 * @version 1.0
 */
public class SSIContestContentValueBean
{

  private List<SSIContestNameValueBean> names;
  private List<SSIContestMessageValueBean> messages;
  private List<SSIContestDescriptionValueBean> descriptions;
  private List<SSIContestDocumentValueBean> documents;

  public List<SSIContestNameValueBean> getNames()
  {
    return names;
  }

  public void setNames( List<SSIContestNameValueBean> names )
  {
    this.names = names;
  }

  public List<SSIContestMessageValueBean> getMessages()
  {
    return messages;
  }

  public void setMessages( List<SSIContestMessageValueBean> messages )
  {
    this.messages = messages;
  }

  public List<SSIContestDescriptionValueBean> getDescriptions()
  {
    return descriptions;
  }

  public void setDescriptions( List<SSIContestDescriptionValueBean> descriptions )
  {
    this.descriptions = descriptions;
  }

  public List<SSIContestDocumentValueBean> getDocuments()
  {
    return documents;
  }

  public void setDocuments( List<SSIContestDocumentValueBean> documents )
  {
    this.documents = documents;
  }

}
