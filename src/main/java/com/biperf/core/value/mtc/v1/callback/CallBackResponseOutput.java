
package com.biperf.core.value.mtc.v1.callback;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CallBackResponseOutput implements Serializable
{

  private static final long serialVersionUID = 1L;

  private String descriptor;
  private String url;

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getDescriptor()
  {
    return descriptor;
  }

  public void setDescriptor( String descriptor )
  {
    this.descriptor = descriptor;
  }

 

}
