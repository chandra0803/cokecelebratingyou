/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/message/MessageListForm.java,v $
 */

package com.biperf.core.ui.message;

import com.biperf.core.ui.BaseActionForm;

/**
 * MessageListForm.
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
 * <td>robinsra</td>
 * <td>Sep 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MessageListForm extends BaseActionForm
{
  private String statusCode;
  private String method;
  private String messageId;
  private String moduleCode;

  /**
   * Get Message Status. Code from pick list. E.G. "act" for active
   * 
   * @return String
   */
  public String getStatusCode()
  {
    return statusCode;
  }

  /**
   * Set Message Status. Code from pick list. E.G. "act" for active
   * 
   * @param statusCode
   */
  public void setStatusCode( String statusCode )
  {
    this.statusCode = statusCode;
  }

  /**
   * Get the Action Method for this form
   * 
   * @return String
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * Set the Action Method for this form
   * 
   * @param method
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * Get Message ID (Database PK)
   * 
   * @return long
   */
  public String getMessageId()
  {
    return messageId;
  }

  /**
   * Set Message ID (Database PK)
   * 
   * @param messageId
   */
  public void setMessageId( String messageId )
  {
    this.messageId = messageId;
  }

  public String getModuleCode()
  {
    return moduleCode;
  }

  public void setModuleCode( String moduleCode )
  {
    this.moduleCode = moduleCode;
  }

}
