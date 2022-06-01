/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/BaseFormBean.java,v $
 */

package com.biperf.core.ui;

import com.biperf.core.domain.BaseDomain;

/**
 * This class is the base class for all Struts Form "Bean" objects. That is, these are the composite
 * objects on a Struts form.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>Apr 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class BaseFormBean
{
  private String id;
  private String version;

  public String getId()
  {
    return id;
  }

  public Long getIdAsLong()
  {
    return new Long( getId() );
  }

  public Long getVersionAsLong()
  {
    return new Long( getVersion() );
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getVersion()
  {
    return version;
  }

  public void setVersion( String version )
  {
    this.version = version;
  }

  public void load( BaseDomain baseObject )
  {
    if ( baseObject.getId() != null )
    {
      setId( "" + baseObject.getId() );
    }
    if ( baseObject.getVersion() != null )
    {
      setVersion( "" + baseObject.getVersion() );
    }
  }

}
