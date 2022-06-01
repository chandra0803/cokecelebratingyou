/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/hierarchy/HierarchyListForm.java,v $
 */

package com.biperf.core.ui.hierarchy;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.BaseForm;

/**
 * HierarchyListForm.
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
 * <td>zahler</td>
 * <td>Apr 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class HierarchyListForm extends BaseForm
{
  private static final Log logger = LogFactory.getLog( HierarchyListForm.class );

  private String primary;
  private String[] delete;
  private String method;

  public String[] getDelete()
  {
    return delete;
  }

  public void setDelete( String[] delete )
  {
    this.delete = delete;
  }

  public String getPrimary()
  {
    return primary;
  }

  public void setPrimary( String primary )
  {
    this.primary = primary;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.validator.ValidatorForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    logger.debug( "HierarchyListForm.validate" );

    ActionErrors errors = super.validate( mapping, request );

    /*
     * if ( getDelete() == null || getDelete().length < 1 ) { errors.add( "deleted", new
     * ActionMessage( "hierarchy.delete.SELECTONE" ) ); }
     */

    return errors;

  }

}
