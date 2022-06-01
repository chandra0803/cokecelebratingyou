/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserAddressListForm.java,v $
 */

package com.biperf.core.ui.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.ui.BaseForm;

/**
 * UserAddressListForm.
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
 * <td>May 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserAddressListForm extends BaseForm
{
  private static final Log log = LogFactory.getLog( UserAddressListForm.class );
  private String userId;
  private String primary;
  private String addressType;
  private String method;
  private String[] delete;

  /**
   * Makes sure that at least one value is present in the delete[] attribute
   * 
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    log.debug( "UserAddressListForm.validate" );
    super.validate( mapping, request );

    ActionErrors actionErrors = super.validate( mapping, request );
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( "remove".equals( method ) )
    {
      if ( getDelete() == null || getDelete().length < 1 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.address.errors.NOTHING_SELECTED_TO_DELETE_ERROR" ) );
      }
    }
    return actionErrors;
  }

  public String getAddressType()
  {
    return addressType;
  }

  public void setAddressType( String addressType )
  {
    this.addressType = addressType;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getPrimary()
  {
    return primary;
  }

  public void setPrimary( String primary )
  {
    this.primary = primary;
  }

  public String getUserId()
  {
    return userId;
  }

  public void setUserId( String userId )
  {
    this.userId = userId;
  }

  /**
   * Overridden from
   * 
   * @return String
   */
  protected String getErrorMessageKeyWhenNoneSelected()
  {
    return "participant.address.errors.NOTHING_SELECTED_TO_DELETE_ERROR";
  }

  public String[] getDelete()
  {
    return delete;
  }

  public void setDelete( String[] delete )
  {
    this.delete = delete;
  }

} // end class UserAddressListForm
