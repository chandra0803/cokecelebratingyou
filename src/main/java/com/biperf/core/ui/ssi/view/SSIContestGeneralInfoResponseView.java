
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;

/**
 * @author dudam
 * @since Nov 18, 2014
 * @version 1.0
 */
public class SSIContestGeneralInfoResponseView
{
  private List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
  private SSIContestGeneralInfoInnerResponse contest;

  public SSIContestGeneralInfoResponseView()
  {
    super();
  }

  public SSIContestGeneralInfoResponseView( Long contestId, String ssiContestClientState )
  {
    contest = new SSIContestGeneralInfoInnerResponse( contestId, ssiContestClientState );
  }

  public SSIContestGeneralInfoResponseView( WebErrorMessage message )
  {
    this.getMessages().add( message );
  }

  public SSIContestGeneralInfoInnerResponse getContest()
  {
    return contest;
  }

  public void setContest( SSIContestGeneralInfoInnerResponse contest )
  {
    this.contest = contest;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  class SSIContestGeneralInfoInnerResponse
  {
    private Long id;
    private String ssiContestClientState;

    public SSIContestGeneralInfoInnerResponse()
    {

    }

    public SSIContestGeneralInfoInnerResponse( Long contestId, String ssiContestClientState )
    {
      this.id = contestId;
      this.ssiContestClientState = ssiContestClientState;
    }

    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    public String getSsiContestClientState()
    {
      return ssiContestClientState;
    }

    public void setSsiContestClientState( String ssiContestClientState )
    {
      this.ssiContestClientState = ssiContestClientState;
    }

  }

}
