/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value.approvals;

import java.math.BigDecimal;

/**
 * 
 * @author poddutur
 * @since Jul 25, 2016
 */
public class NominationsApprovalTeamAwardBean
{
  private Long paxId;
  private String value;

  public Long getPaxId()
  {
    return paxId;
  }

  public void setPaxId( Long paxId )
  {
    this.paxId = paxId;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  public BigDecimal getConvertedTeamValue()
  {
    BigDecimal b = new BigDecimal( this.value );
    return b;
  }

}
