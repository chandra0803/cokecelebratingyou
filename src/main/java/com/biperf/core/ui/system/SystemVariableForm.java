/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/system/SystemVariableForm.java,v $
 */

package com.biperf.core.ui.system;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.SystemVariableType;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.opensymphony.module.propertyset.PropertySet;

public class SystemVariableForm extends BaseForm
{
  private long entityId;
  private String entityName;
  private String typeCode;
  private String key;
  private String stringVal;
  private String method;
  private String[] deleteValues;
  private boolean editable;
  private boolean viewable;
  private String groupName;

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
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

    /**
     * Validate string value matches it's type.
     */
    switch ( Integer.parseInt( getTypeCode() ) )
    {
      case PropertySet.BOOLEAN:
        if ( !getStringVal().equalsIgnoreCase( "true" ) && !getStringVal().equalsIgnoreCase( "false" ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.sys.var.errors.BOOLEAN" ) );
        }
        break;
      case PropertySet.DOUBLE:
        try
        {
          Double.parseDouble( getStringVal() );
        }
        catch( NumberFormatException nfe )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.sys.var.errors.DOUBLE" ) );
        }
        break;
      case PropertySet.STRING:
        // no validation needed for string
        break;
      case PropertySet.LONG:
        try
        {
          Long.parseLong( getStringVal() );
        }
        catch( NumberFormatException nfe )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.sys.var.errors.LONG" ) );
        }
        break;
      case PropertySet.INT:
        try
        {
          Integer.parseInt( getStringVal() );
        }
        catch( NumberFormatException nfe )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.sys.var.errors.INT" ) );
        }
        break;
      case PropertySet.DATE:
        // Validate the date
        try
        {
          SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
          sdf.parse( getStringVal() );
        }
        catch( ParseException e )
        {
          actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "admin.sys.var.errors.DATE" ) );
        }
        break;
      default:
        break;
    }

    return actionErrors;
  }

  /**
   * Load Property Set Item
   * 
   * @param prop
   */
  public void load( PropertySetItem prop )
  {
    this.entityId = prop.getEntityId();
    this.entityName = prop.getEntityName();
    this.key = prop.getKey();
    this.typeCode = prop.getType().getCode();
    this.editable = prop.isEditable();
    this.viewable = prop.isViewable();
    this.groupName = prop.getGroupName();

    // The form will only load one stringVal so determin what value to pull
    // out of the PropertySetItem based on the typeCode.
    switch ( Integer.parseInt( typeCode ) )
    {
      case PropertySet.BOOLEAN:
        this.stringVal = String.valueOf( prop.getBooleanVal() );
        break;
      case PropertySet.DOUBLE:
        this.stringVal = String.valueOf( prop.getDoubleVal() );
        break;
      case PropertySet.STRING:
        this.stringVal = prop.getStringVal();
        break;
      case PropertySet.LONG:
        if ( prop.getEntityName().equals( SystemVariableService.PASSWORD_EXPIRED_PERIOD ) )
        {
          // Stored in millis but displayed in days
          this.stringVal = String.valueOf( prop.getLongVal() / org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
        }
        else
        {
          this.stringVal = String.valueOf( prop.getLongVal() );
        }
        break;
      case PropertySet.INT:
        this.stringVal = String.valueOf( prop.getIntVal() );
        break;
      case PropertySet.DATE:
        this.stringVal = DateUtils.toDisplayString( prop.getDateVal() );
        break;
      default:
        break;
    }
  }

  /**
   * Sets the propertySetItem in the Domain Object
   * 
   * @return prop
   */
  public PropertySetItem toDomainObject()
  {
    PropertySetItem prop = new PropertySetItem();

    prop.setEntityId( this.entityId );
    prop.setEntityName( this.entityName );
    prop.setKey( this.key );
    prop.setType( SystemVariableType.lookup( this.typeCode ) );
    // If we create/update any system variable from the screen, currently we don't have an option to set the editable and viewable fields
    // so from the screen editable and viewable always coming as false value.
    //but currently we are display the system variables, only if viewable is true, due to that reason, here I'm setting viewable and editable value as true.
    prop.setEditable( Boolean.TRUE ); 
    prop.setViewable( Boolean.TRUE );
    prop.setGroupName( this.groupName );

    // All that is on the form is a string value so set the appropriate PropertySetItem
    // value based on the type.
    switch ( Integer.parseInt( typeCode ) )
    {
      case PropertySet.BOOLEAN:
        prop.setBooleanVal( Boolean.valueOf( this.stringVal ) );
        break;
      case PropertySet.DOUBLE:
        prop.setDoubleVal( new Double( this.stringVal ) );
        break;
      case PropertySet.STRING:
        prop.setStringVal( this.stringVal );
        break;
      case PropertySet.LONG:
        if ( prop.getEntityName().equals( SystemVariableService.PASSWORD_EXPIRED_PERIOD ) )
        {
          // Stored in millis but displayed in days
          prop.setLongVal( new Long( Long.parseLong( this.stringVal ) * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY ) );
        }
        else
        {
          prop.setLongVal( new Long( this.stringVal ) );
        }
        break;
      case PropertySet.INT:
        prop.setIntVal( new Integer( this.stringVal ) );
        break;
      case PropertySet.DATE:
        prop.setDateVal( DateUtils.toDate( this.stringVal ) );
        break;
      default:
        break;
    }

    return prop;
  }

  public long getEntityId()
  {
    return entityId;
  }

  public void setEntityId( long entityId )
  {
    this.entityId = entityId;
  }

  public String getEntityName()
  {
    return entityName;
  }

  public void setEntityName( String entityName )
  {
    this.entityName = entityName;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey( String key )
  {
    this.key = key;
  }

  public String getStringVal()
  {
    return stringVal;
  }

  public void setStringVal( String stringVal )
  {
    this.stringVal = stringVal;
  }

  public String[] getDeleteValues()
  {
    return deleteValues;
  }

  public void setDeleteValues( String[] deleteValues )
  {
    this.deleteValues = deleteValues;
  }

  public String getTypeCode()
  {
    return typeCode;
  }

  public void setTypeCode( String typeCode )
  {
    this.typeCode = typeCode;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public boolean isEditable()
  {
    return editable;
  }

  public void setEditable( boolean editable )
  {
    this.editable = editable;
  }

  public boolean isViewable()
  {
    return viewable;
  }

  public void setViewable( boolean viewable )
  {
    this.viewable = viewable;
  }

  public String getGroupName()
  {
    return groupName;
  }

  public void setGroupName( String groupName )
  {
    this.groupName = groupName;
  }

}
