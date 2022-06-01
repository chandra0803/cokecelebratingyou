/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/participant/MyAccountForm.java,v $
 */

package com.biperf.core.ui.participant;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * MyAccountForm.
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
 * <td>May 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MyAccountForm extends BaseForm
{
  private String method;
  private String startDate = DateUtils.displayDateFormatMask;
  private String endDate = DateUtils.displayDateFormatMask;
  private boolean printerFriendly;

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Validation is being done inside the form because there
   * are dynamic fields that may or may not need validating.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */

  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    Date formatDate = null;
    if ( startDate != null && startDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( startDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( startDate ) )
      {
        actionErrors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "participant.myaccount.START_DATE" ) ) );

      }
    }
    if ( endDate != null && endDate.length() > 0 )
    {
      formatDate = DateUtils.toDate( endDate );
      if ( !DateUtils.toDisplayString( formatDate ).equals( endDate ) )
      {
        actionErrors.add( "endDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "participant.myaccount.END_DATE" ) ) );

      }
    }
    if ( DateUtils.toDate( startDate ) != null && DateUtils.toDate( endDate ) != null && DateUtils.toDate( startDate ).after( DateUtils.toDate( endDate ) ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "participant.myaccount.errors.START_DATE_AFTER_END_DATE" ) );
    }

    return actionErrors;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public boolean isPrinterFriendly()
  {
    return printerFriendly;
  }

  public void setPrinterFriendly( boolean printerFriendly )
  {
    this.printerFriendly = printerFriendly;
  }
}
