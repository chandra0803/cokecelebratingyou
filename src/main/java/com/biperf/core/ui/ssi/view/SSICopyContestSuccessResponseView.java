
package com.biperf.core.ui.ssi.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SSICopyContestSuccessResponseView
{
  private List<SSICopyContestSuccessMessage> messages = new ArrayList<SSICopyContestSuccessMessage>();

  public SSICopyContestSuccessResponseView( Long contestId )
  {
    Map<String, Object> clientStateParamMap = new HashMap<String, Object>();
    clientStateParamMap.put( "contestId", contestId.toString() );
    String password = ClientStatePasswordManager.getPassword();
    try
    {
      messages.add( new SSICopyContestSuccessMessage( URLEncoder.encode( ClientStateSerializer.serialize( clientStateParamMap, password ), "UTF-8" ) ) );
    }
    catch( UnsupportedEncodingException e )
    {
      throw new RuntimeException( "Unable Encode Client State: " + e );
    }
  }

  public List<SSICopyContestSuccessMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<SSICopyContestSuccessMessage> messages )
  {
    this.messages = messages;
  }

  @JsonInclude( value = Include.NON_EMPTY )
  private class SSICopyContestSuccessMessage
  {

    private String id;
    private String type = "success";
    private Boolean success = Boolean.TRUE;
    private String url;

    public String getUrl()
    {
      return url;
    }

    public void setUrl( String url )
    {
      this.url = url;
    }

    public SSICopyContestSuccessMessage( String id )
    {
      this.id = id;
      url = "createGeneralInfo.do?method=prepareEdit&contestId=" + id;
    }

    public String getId()
    {
      return id;
    }

    public void setId( String id )
    {
      this.id = id;
    }

    public String getType()
    {
      return type;
    }

    public void setType( String type )
    {
      this.type = type;
    }

    @JsonProperty( "isSuccess" )
    public Boolean getSuccess()
    {
      return success;
    }

    public void setSuccess( Boolean success )
    {
      this.success = success;
    }
  }

}
