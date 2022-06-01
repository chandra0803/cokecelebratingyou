/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/CommLogValueBean.java,v $
 */

package com.biperf.core.value;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * CommLogValueBean.
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
 * <td>arasi</td>
 * <td>Jan 15, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CommLogValueBean implements Serializable
{
  private Long rowNum;
  private Long commLogId;
  private String message;
  private String subject;
  private Timestamp dateCreated;
  private String plainMessage;
  private Long createdBy;

  public Long getRowNum()
  {
    return rowNum;
  }

  public void setRowNum( Long rowNum )
  {
    this.rowNum = rowNum;
  }

  public Long getCommLogId()
  {
    return commLogId;
  }

  public void setCommLogId( Long commLogId )
  {
    this.commLogId = commLogId;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message;
  }

  public String getSubject()
  {
    return subject;
  }

  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  public Timestamp getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( Timestamp dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public String getPlainMessage()
  {
    return plainMessage;
  }

  public void setPlainMessage( String plainMessage )
  {
    this.plainMessage = plainMessage;
  }

  public Long getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( Long createdBy )
  {
    this.createdBy = createdBy;
  }

}
