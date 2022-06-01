/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserAddressForm.java,v $
 */

package com.biperf.core.ui.user;

import java.sql.Timestamp;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.AddressType;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * UserAddressForm.
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
 * <td>May 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class UserAddressForm extends BaseForm
{

  private static final long serialVersionUID = 3618416025444299317L;

  public static final String FORM_NAME = "userAddressForm";
  private static final Log log = LogFactory.getLog( UserAddressForm.class );

  private String userId;
  private String method;
  private String addressType;
  private String addressTypeDesc;
  private AddressFormBean addressFormBean = new AddressFormBean();

  private long version;
  private long id;
  private long dateCreated;
  private String createdBy;
  private boolean primary;
  private String rosterAddressId;

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param anActionMapping
   * @param aRequest
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping anActionMapping, HttpServletRequest aRequest )
  {
    final String METHOD_NAME = "validate";
    log.info( ">>> " + METHOD_NAME );

    ActionErrors actionErrors = super.validate( anActionMapping, aRequest );
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( "create".equals( method ) || "update".equals( method ) )
    {
      if ( addressType == null || addressType.equals( "" ) )
      {
        actionErrors.add( "addressType", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "participant.address.ADDR_TYPE_LABEL" ) ) );
      }
      actionErrors = addressFormBean.validateAddress( actionErrors );
    } // end if create

    log.info( "<<< " + METHOD_NAME );
    return actionErrors;
  } // end validate

  /**
   * Load the values from the UserAddress to the form
   * 
   * @param userAddress
   */
  public void load( UserAddress userAddress )
  {
    this.userId = String.valueOf( userAddress.getUser().getId() );
    this.addressType = userAddress.getAddressType().getCode();
    this.addressTypeDesc = userAddress.getAddressType().getName();
    this.addressFormBean.load( userAddress.getAddress() );
    this.primary = userAddress.isPrimary();
    this.createdBy = userAddress.getAuditCreateInfo().getCreatedBy().toString();
    this.dateCreated = userAddress.getAuditCreateInfo().getDateCreated().getTime();
    this.id = userAddress.getId().longValue();
    this.version = userAddress.getVersion().longValue();
    if ( userAddress.getId() != null )
    {
      this.rosterAddressId = userAddress.getRosterAddressId().toString();
    }
  } // end load

  /**
   * Builds a full domain object for update from the form.
   * 
   * @return UserAddress
   */
  public UserAddress toFullDomainObject()
  {

    UserAddress userAddress = new UserAddress();
    userAddress.setAddressType( AddressType.lookup( this.addressType ) );
    userAddress.setAddress( this.addressFormBean.toDomainObject() );
    userAddress.setIsPrimary( Boolean.valueOf( primary ) );

    userAddress.setId( new Long( this.id ) );
    userAddress.setVersion( new Long( this.version ) );
    userAddress.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    userAddress.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );

    if ( this.rosterAddressId != null && !StringUtils.isEmpty( this.rosterAddressId ) )
    {
      userAddress.setRosterAddressId( UUID.fromString( this.rosterAddressId ) );
    }

    return userAddress;
  } // end toFullDomainObject

  /**
   * Builds a domain object from the form.
   * 
   * @return UserAddress
   */
  public UserAddress toInsertedDomainObject()
  {
    UserAddress userAddress = new UserAddress();
    userAddress.setAddressType( AddressType.lookup( this.addressType ) );
    userAddress.setAddress( this.addressFormBean.toDomainObject() );
    userAddress.setIsPrimary( Boolean.valueOf( primary ) );
    return userAddress;
  } // end toInsertedDomainObject

  public String getRosterAddressId()
  {
    return rosterAddressId;
  }

  public void setRosterAddressId( String rosterAddressId )
  {
    this.rosterAddressId = rosterAddressId;
  }

  public String getAddressType()
  {
    return addressType;
  }

  public void setAddressType( String addressType )
  {
    this.addressType = addressType;
  }

  public String getAddressTypeDesc()
  {
    return addressTypeDesc;
  }

  public void setAddressTypeDesc( String addressTypeDesc )
  {
    this.addressTypeDesc = addressTypeDesc;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public long getId()
  {
    return id;
  }

  public void setId( long id )
  {
    this.id = id;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public boolean isPrimary()
  {
    return primary;
  }

  public void setPrimary( boolean primary )
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

  public long getVersion()
  {
    return version;
  }

  public void setVersion( long version )
  {
    this.version = version;
  }

  public AddressFormBean getAddressFormBean()
  {
    return addressFormBean;
  }

  public void setAddressFormBean( AddressFormBean addressFormBean )
  {
    this.addressFormBean = addressFormBean;
  }

} // end class UserAddressForm
