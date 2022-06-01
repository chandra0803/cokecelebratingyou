/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/nodetype/ListValidatorFormWithDelete.java,v $
 */

package com.biperf.core.ui.nodetype;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.BaseForm;

/**
 * ListValidatorFormWithDelete This class can be used as a superclass for forms which have a list of
 * delete checkboxes (i.e., on most of our List display pages). <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>May 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public abstract class ListValidatorFormWithDelete extends BaseForm
{
  private static final Log log = LogFactory.getLog( ListValidatorFormWithDelete.class );
  private String[] delete;

  public String[] getDelete()
  {
    return delete;
  }

  public void setDelete( String[] delete )
  {
    this.delete = delete;
  }

  /**
   * Makes sure that at least one value is present in the delete[] attribute
   * 
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    log.debug( "ListValidatorFormWithDelete.validate" );
    super.validate( mapping, request );

    ActionErrors actionErrors = super.validate( mapping, request );
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    /*
     * if ( getDelete() == null || getDelete().length < 1 ) { actionErrors.add(
     * ActionErrors.GLOBAL_MESSAGE, new ActionMessage( getErrorMessageKeyWhenNoneSelected() ) ); }
     */
    return actionErrors;
  }

  /**
   * This needs to return the key for the error message which should be displayed when no 'delete'
   * checkboxes are set by the user (i.e., an empty form is submitted).
   * 
   * @return error message key
   */
  protected abstract String getErrorMessageKeyWhenNoneSelected();
}
