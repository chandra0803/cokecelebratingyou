
package com.biperf.core.ui.mtc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CallBackEntity
{

  private String state;
  private String companyId;
  private String profile;
  private String requestId;
  private List<CallBackDetailEntity> outputs;
  private String inputKey;

  public String getState()
  {
    return state;
  }

  @Override
  public String toString()
  {
    return "CallBackEntity [state=" + state + ", companyId=" + companyId + ", profile=" + profile + ", requestId=" + requestId + ", outputs=" + outputs + ", inputKey=" + inputKey + "]";
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

  public List<CallBackDetailEntity> getOutputs()
  {
    return outputs;
  }

  public void setOutputs( List<CallBackDetailEntity> outputs )
  {
    this.outputs = outputs;
  }

  public String getInputKey()
  {
    return inputKey;
  }

  public void setInputKey( String inputKey )
  {
    this.inputKey = inputKey;
  }

}
