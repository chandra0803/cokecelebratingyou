/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/fileload/PaxByNodeResult.java,v $
 */

package com.biperf.core.domain.fileload;

import java.io.Serializable;

/**
 * This class is used as a result set holder object for the pax import service- in order to detect
 * if there are multiple owners per node set.
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
 * <td>Sep 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class PaxByNodeResult implements Serializable
{

  private Long importFileId;
  private Long importRecordId;
  private String username;
  private Long userId;
  private Long nodeId;
  private String nodeRole;

  public Long getImportFileId()
  {
    return importFileId;
  }

  public void setImportFileId( Long importFileId )
  {
    this.importFileId = importFileId;
  }

  public Long getImportRecordId()
  {
    return importRecordId;
  }

  public void setImportRecordId( Long importRecordId )
  {
    this.importRecordId = importRecordId;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername( String username )
  {
    this.username = username;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public String getNodeRole()
  {
    return nodeRole;
  }

  public void setNodeRole( String nodeRole )
  {
    this.nodeRole = nodeRole;
  }
}
