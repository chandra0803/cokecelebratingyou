/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/cms/CMDataElement.java,v $
 *
 */

package com.biperf.core.service.cms;

import com.objectpartners.cms.domain.enums.DataTypeEnum;

/**
 * CMDataElement <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Jul 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class CMDataElement
{
  private String key;
  private String label;
  private String data;
  private boolean unique = false;
  private boolean required = false;
  private DataTypeEnum dataTypeEnum;

  public CMDataElement( String label, String key, String data )
  {
    this.key = key;
    this.label = label;
    this.data = data;
    this.dataTypeEnum = DataTypeEnum.STRING;
  }

  public CMDataElement( String label, String key, String data, boolean unique )
  {
    this.key = key;
    this.label = label;
    this.data = data;
    this.unique = unique;
    this.dataTypeEnum = DataTypeEnum.STRING;
  }

  public CMDataElement( String label, String key, String data, boolean unique, DataTypeEnum dataTypeEnum )
  {
    this.key = key;
    this.label = label;
    this.data = data;
    this.unique = unique;
    this.dataTypeEnum = dataTypeEnum;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey( String key )
  {
    this.key = key;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel( String label )
  {
    this.label = label;
  }

  public String getData()
  {
    return data;
  }

  public void setData( String data )
  {
    this.data = data;
  }

  public boolean isUnique()
  {
    return unique;
  }

  public void setUnique( boolean unique )
  {
    this.unique = unique;
  }

  public boolean isRequired()
  {
    return required;
  }

  public void setRequired( boolean required )
  {
    this.required = required;
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof CMDataElement ) )
    {
      return false;
    }

    final CMDataElement cmDataElement = (CMDataElement)o;

    if ( key != null ? !key.equals( cmDataElement.key ) : cmDataElement.key != null )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    return key != null ? key.hashCode() : 0;
  }

  public DataTypeEnum getDataTypeEnum()
  {
    return dataTypeEnum;
  }

  public void setDataTypeEnum( DataTypeEnum dataTypeEnum )
  {
    this.dataTypeEnum = dataTypeEnum;
  }

}
