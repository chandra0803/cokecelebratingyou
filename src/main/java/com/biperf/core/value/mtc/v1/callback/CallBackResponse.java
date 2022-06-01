
package com.biperf.core.value.mtc.v1.callback;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CallBackResponse implements Serializable
{

  private static final long serialVersionUID = 1L;

  private String state;
  private String companyId;
  private String profile;
  private String requestId;
  private List<CallBackResponseOutput> outputs;

  public String getState()
  {
    return state;
  }

  public void setState( String state )
  {
    this.state = state;
  }

  public String getCompanyId()
  {
    return companyId;
  }

  public void setCompanyId( String companyId )
  {
    this.companyId = companyId;
  }

  public String getProfile()
  {
    return profile;
  }

  public void setProfile( String profile )
  {
    this.profile = profile;
  }

  public String getRequestId()
  {
    return requestId;
  }

  public void setRequestId( String requestId )
  {
    this.requestId = requestId;
  }

  public List<CallBackResponseOutput> getOutputs()
  {
    return outputs;
  }

  public void setOutputs( List<CallBackResponseOutput> outputs )
  {
    this.outputs = outputs;
  }

}
