
package com.biperf.core.value.ecard;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class UploadEcardRequest implements Serializable
{
  private static final long serialVersionUID = -1326957226165924918L;
  private String name;
  private String file;
  private Long claimId;

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getFile()
  {
    return file;
  }

  public void setFile( String file )
  {
    this.file = file;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }
}
