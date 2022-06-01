/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserCharacteristicForm.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.CharacteristicUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.value.CharacteristicValueBean;

/**
 * UserCharacteristicForm.
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
 * <td>sedey</td>
 * <td>Apr 25, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserCharacteristicForm extends BaseForm
{
  private Long userId;
  private String method;
  private List userCharacteristicValueList;
  public static final String DELETE_VALUE = "delete_option";

  /**
   * Reset all properties to their default values.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // UserCharacteristicFormBeans. If this is not done, the form wont initialize
    // properly.
    userCharacteristicValueList = CharacteristicUtils.getEmptyValueList( RequestUtils.getOptionalParamInt( request, "userCharacteristicValueListCount" ) );
  }

  public String getDeleteOption()
  {
    return DELETE_VALUE;
  }

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

    CharacteristicUtils.validateCharacteristicValueList( userCharacteristicValueList, actionErrors );

    return actionErrors;
  }

  /**
   * @return List of CharacteristicFormBean objects
   */
  public List getUserCharacteristicValueList()
  {
    return userCharacteristicValueList;
  }

  /**
   * Accessor for the number of CharacteristicFormBean objects in the list.
   * 
   * @return int
   */
  public int getUserCharacteristicValueListCount()
  {
    if ( userCharacteristicValueList == null )
    {
      return 0;
    }

    return userCharacteristicValueList.size();
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of CharacteristicFormBean from the value list
   */
  public CharacteristicValueBean getUserCharacteristicValueInfo( int index )
  {
    try
    {
      return (CharacteristicValueBean)userCharacteristicValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  /**
   * @return userId
   */
  public Long getUserId()
  {
    return userId;
  }

  /**
   * @param userId
   */
  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  /**
   * @return method
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * @param method
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  public void setUserCharacteristicValueList( List valueList )
  {
    this.userCharacteristicValueList = valueList;
  }

}
