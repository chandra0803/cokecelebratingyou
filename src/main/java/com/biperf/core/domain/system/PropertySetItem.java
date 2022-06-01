/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/system/PropertySetItem.java,v $
 */

package com.biperf.core.domain.system;

import java.io.Serializable;
import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.SystemVariableType;

public class PropertySetItem extends BaseDomain implements Serializable
{
  private Date dateVal;
  private String entityName;
  private String key;
  private String stringVal;
  private boolean booleanVal;
  private double doubleVal;
  private int intVal;
  private SystemVariableType type;
  private long entityId;
  private long longVal;
  private boolean editable;
  private boolean viewable;
  private String groupName;

  /**
   * Default Constructor
   */
  public PropertySetItem()
  {
  }

  public void setBooleanVal( Boolean booleanVal )
  {
    if ( booleanVal == null )
    {
      this.booleanVal = true;
    }
    else
    {
      this.booleanVal = booleanVal.booleanValue();
    }
  }

  public boolean getBooleanVal()
  {
    return booleanVal;
  }

  public void setDateVal( Date dateVal )
  {
    this.dateVal = dateVal;
  }

  public Date getDateVal()
  {
    return dateVal;
  }

  public void setDoubleVal( Double doubleVal )
  {
    if ( doubleVal == null )
    {
      this.doubleVal = 0.0;
    }
    else
    {
      this.doubleVal = doubleVal.doubleValue();
    }

  }

  public double getDoubleVal()
  {
    return doubleVal;
  }

  public void setEntityId( long entityId )
  {
    this.entityId = entityId;
  }

  public long getEntityId()
  {
    return entityId;
  }

  public void setEntityName( String entityName )
  {
    this.entityName = entityName;
  }

  public String getEntityName()
  {
    return entityName;
  }

  public void setIntVal( Integer intVal )
  {
    if ( intVal == null )
    {
      this.intVal = 0;
    }
    else
    {
      this.intVal = intVal.intValue();
    }
  }

  public int getIntVal()
  {
    return intVal;
  }

  public void setKey( String key )
  {
    this.key = key;
  }

  public String getKey()
  {
    return key;
  }

  public void setLongVal( Long longVal )
  {
    if ( longVal == null )
    {
      this.longVal = 0;
    }
    else
    {
      this.longVal = longVal.longValue();
    }
  }

  public long getLongVal()
  {
    return longVal;
  }

  public void setStringVal( String stringVal )
  {
    this.stringVal = stringVal;
  }

  public String getStringVal()
  {
    return stringVal;
  }

  public void setType( SystemVariableType type )
  {
    this.type = type;
  }

  public SystemVariableType getType()
  {
    return type;
  }

  public boolean equals( Object obj )
  {
    if ( ! ( obj instanceof PropertySetItem ) )
    {
      return false;
    }

    PropertySetItem item = (PropertySetItem)obj;

    return item.getEntityId() == entityId && item.getEntityName().equals( entityName ) && item.getKey().equals( key );
  }

  public int hashCode()
  {
    return (int) ( entityId + entityName.hashCode() + key.hashCode() );
  }

  public boolean isEditable()
  {
    return editable;
  }

  public void setEditable( boolean editable )
  {
    this.editable = editable;
  }

  public boolean isViewable()
  {
    return viewable;
  }

  public void setViewable( boolean viewable )
  {
    this.viewable = viewable;
  }

  public String getGroupName()
  {
    return groupName;
  }

  public void setGroupName( String groupName )
  {
    this.groupName = groupName;
  }

}
