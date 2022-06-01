/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.ecard;

import java.io.Serializable;

public class MigratedCardInfo implements Serializable
{

  private static final long serialVersionUID = 1L;

  private Long claimId;
  private String migratedUrl;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getMigratedUrl()
  {
    return migratedUrl;
  }

  public void setMigratedUrl( String migratedUrl )
  {
    this.migratedUrl = migratedUrl;
  }

}
