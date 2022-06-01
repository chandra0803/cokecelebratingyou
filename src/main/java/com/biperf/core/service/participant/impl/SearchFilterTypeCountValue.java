
package com.biperf.core.service.participant.impl;

import com.biperf.core.domain.enums.ParticipantSearchFilterEnum;

public class SearchFilterTypeCountValue
{
  private ParticipantSearchFilterEnum paxSearchFilterType;
  private int count = 0;

  public SearchFilterTypeCountValue()
  {
  }

  public SearchFilterTypeCountValue( ParticipantSearchFilterEnum type, int count )
  {
    this.paxSearchFilterType = type;
    this.count = count;
  }

  public ParticipantSearchFilterEnum getPaxSearchFilterType()
  {
    return paxSearchFilterType;
  }

  public void setPaxSearchFilterType( ParticipantSearchFilterEnum paxSearchFilterType )
  {
    this.paxSearchFilterType = paxSearchFilterType;
  }

  public int getCount()
  {
    return count;
  }

  public void setCount( int count )
  {
    this.count = count;
  }

  @Override
  public String toString()
  {
    return "SearchFilterTypeCountValue [paxSearchFilterType=" + ( null != paxSearchFilterType ? paxSearchFilterType.getCode() : "null" ) + ", count=" + count + "]";
  }

}
