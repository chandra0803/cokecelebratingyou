
package com.biperf.core.ui.filter;

import java.io.Serializable;

public class FilterSetupBean implements Serializable, Comparable<FilterSetupBean>
{
  String filterAppSetupId;
  String tileId;
  String rank;
  private String removeTile;
  private int sequenceNumber;

  public String getRank()
  {
    return rank;
  }

  public void setRank( String rank )
  {
    this.rank = rank;
  }

  public String getRemoveTile()
  {
    return removeTile;
  }

  public void setRemoveTile( String removeTile )
  {
    this.removeTile = removeTile;
  }

  public int getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( int sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  @Override
  public int compareTo( FilterSetupBean fileFilterSetupBean )
  {
    return this.sequenceNumber - fileFilterSetupBean.getSequenceNumber();
  }

  public String getFilterAppSetupId()
  {
    return filterAppSetupId;
  }

  public void setFilterAppSetupId( String filterAppSetupId )
  {
    this.filterAppSetupId = filterAppSetupId;
  }

  public String getTileId()
  {
    return tileId;
  }

  public void setTileId( String tileId )
  {
    this.tileId = tileId;
  }

}
