
package com.biperf.core.value.cmx.v1.cmx;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CMXTranslateResponse implements Serializable
{

  private String success;
  private Map<String, Map<String, String>> bundles;

  public String getSuccess()
  {
    return success;
  }

  public void setSuccess( String success )
  {
    this.success = success;
  }

  public Map<String, Map<String, String>> getBundles()
  {
    return bundles;
  }

  public void setBundles( Map<String, Map<String, String>> bundles )
  {
    this.bundles = bundles;
  }

}
