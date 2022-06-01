/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claimform/CustomerInformationBlockForm.java,v $
 */

package com.biperf.core.ui.claimform;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;

/**
 * CustomerInformationBlockForm.
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
 * <td>Jun 15, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CustomerInformationBlockForm extends BaseForm
{
  public static final String FORM_NAME = "customerInformationBlockForm";

  private String claimFormId;
  private String claimFormStepId;
  private String method;
  private List cibFormBeans;

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
    for ( java.util.Iterator iter = cibFormBeans.iterator(); iter.hasNext(); )
    {
      CustomerInformationBlockFormBean bean = (CustomerInformationBlockFormBean)iter.next();
      if ( bean.isRequired() && !bean.isDisplay() )
      {
        // an element cannot be required if it is not displayed
        actionErrors.add( "cibFormBean", new ActionMessage( "claims.form.step.cib.ELEMENT_REQUIRED_BUT_NOT_DISPLAYED", bean.getName() ) );
      }
    }

    return actionErrors;
  }

  /**
   * Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    cibFormBeans = new ArrayList();

    int count = RequestUtils.getOptionalParamInt( request, "cibFormBeansCount" );

    for ( int i = 0; i < count; i++ )
    {
      cibFormBeans.add( new CustomerInformationBlockFormBean() );
    }
  } // end reset

  public List getCibFormBeans()
  {
    return cibFormBeans;
  }

  public void setCibFormBeans( List cibFormBeans )
  {
    this.cibFormBeans = cibFormBeans;
  }

  public int getCibFormBeansCount()
  {
    if ( cibFormBeans != null )
    {
      return cibFormBeans.size();
    }
    return 0;
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of CustomerInformationBlockFormBean from the value list
   */
  public CustomerInformationBlockFormBean getCibFormBean( int index )
  {
    try
    {
      return (CustomerInformationBlockFormBean)cibFormBeans.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getClaimFormStepId()
  {
    return claimFormStepId;
  }

  public void setClaimFormStepId( String claimFormStepId )
  {
    this.claimFormStepId = claimFormStepId;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getClaimFormId()
  {
    return claimFormId;
  }

  public void setClaimFormId( String claimFormId )
  {
    this.claimFormId = claimFormId;
  }
}
