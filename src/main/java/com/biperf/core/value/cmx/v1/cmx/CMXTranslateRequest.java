
package com.biperf.core.value.cmx.v1.cmx;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CMXTranslateRequest implements Serializable
{
  private List<String> locales;
  private List<String> keys;

  public List<String> getLocales()
  {
    return locales;
  }

  public void setLocales( List<String> locales )
  {
    this.locales = locales;
  }

  public List<String> getKeys()
  {
    return keys;
  }

  public void setKeys( List<String> keys )
  {
    this.keys = keys;
  }

  @Override
  public String toString()
  {
    return "CMXTranslateRequest [keys=" + keys + "]";
  }

}