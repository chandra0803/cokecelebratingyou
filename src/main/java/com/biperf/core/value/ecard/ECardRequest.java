
package com.biperf.core.value.ecard;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class ECardRequest implements Serializable
{

  private static final long serialVersionUID = -7145430082594207456L;
  private String companyId;
  private String visibility;

  public String getCompanyId()
  {
    return companyId;
  }

  public void setCompanyId( String companyId )
  {
    this.companyId = companyId;
  }

  public String getVisibility()
  {
    return visibility;
  }

  public void setVisibility( String visibility )
  {
    this.visibility = visibility;
  }

}
