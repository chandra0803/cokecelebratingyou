
package com.biperf.core.ui.promotion;

import java.io.Serializable;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings( "serial" )
public class DivisionFormBean implements Serializable, Cloneable
{
  private Long divisionId;
  private String guid;

  private String divisionName;
  private String minimumQualifier;

  private String removePayout;
  private String createdBy = "";
  private long dateCreated;
  private Long version;
  private List<DivisionPayoutFormBean> divisionPayoutValueList = new ArrayList<DivisionPayoutFormBean>();

  public DivisionFormBean()
  {

    this.guid = new VMID().toString();
  }

  @SuppressWarnings( "unchecked" )
  public Object clone()
  {
    DivisionFormBean clonedObject = null;

    try
    {
      clonedObject = (DivisionFormBean)super.clone();
      clonedObject.setDivisionPayoutValueList( (List<DivisionPayoutFormBean>) ( (ArrayList<DivisionPayoutFormBean>)divisionPayoutValueList ).clone() );
    }
    catch( CloneNotSupportedException e )
    {
      // This exception will never be throw because this class implements the interface
      // "Cloneable."
    }

    return clonedObject;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public String getGuid()
  {
    return guid;
  }

  public void setGuid( String guid )
  {
    this.guid = guid;
  }

  public String getDivisionName()
  {
    return divisionName;
  }

  public void setDivisionName( String divisionName )
  {
    this.divisionName = divisionName;
  }

  public String getMinimumQualifier()
  {
    return minimumQualifier;
  }

  public void setMinimumQualifier( String minimumQualifier )
  {
    this.minimumQualifier = minimumQualifier;
  }

  public Long getDivisionId()
  {
    return divisionId;
  }

  public void setDivisionId( Long divisionId )
  {
    this.divisionId = divisionId;
  }

  public String getRemovePayout()
  {
    return removePayout;
  }

  public void setRemovePayout( String removePayout )
  {
    this.removePayout = removePayout;
  }

  public List<DivisionPayoutFormBean> getDivisionPayoutValueList()
  {
    return divisionPayoutValueList;
  }

  public void setDivisionPayoutValueList( List<DivisionPayoutFormBean> divisionPayoutValueList )
  {
    this.divisionPayoutValueList = divisionPayoutValueList;
  }

  public int getDivisionPayoutValueListCount()
  {
    if ( divisionPayoutValueList == null )
    {
      return 0;
    }

    return divisionPayoutValueList.size();
  }

  public DivisionPayoutFormBean getDivisionPayoutValue( int index )
  {
    try
    {
      return (DivisionPayoutFormBean)divisionPayoutValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

}
