/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/employer/EmployerForm.java,v $
 */

package com.biperf.core.ui.employer;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.employer.Employer;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.user.AddressFormBean;

/**
 * EmployerForm.
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
 * <td>crosenquest</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class EmployerForm extends BaseActionForm
{

  private static final long serialVersionUID = 3257009869159414067L;

  public static final String FORM_NAME = "employerForm";
  private static final Log logger = LogFactory.getLog( EmployerForm.class );

  private String employerName;
  private boolean active;
  private String statusReason;
  private String federalTaxId;
  private String stateTaxId;
  private String phoneNumber;
  private String phoneExtension;
  private String countryPhoneCode;
  private AddressFormBean addressFormBean = new AddressFormBean();

  /** method */
  private String method;

  /** version */
  private long version;

  /** id */
  private long id;

  /** dateCreated */
  private long dateCreated;

  /** createdBy */
  private String createdBy;

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
    logger.info( ">>> " + METHOD_NAME );

    ActionErrors actionErrors = super.validate( anActionMapping, aRequest );
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( "create".equals( method ) || "update".equals( method ) )
    {
      if ( addressFormBean.getCountryCode() == null || addressFormBean.getCountryCode().equals( "" ) )
      {
        addressFormBean.resetForm();
      }
      else
      {
        actionErrors = addressFormBean.validateAddress( actionErrors );
      }
    }

    logger.info( "<<< " + METHOD_NAME );
    return actionErrors;
  } // end validate

  /**
   * Load the form with the domain object value;
   * 
   * @param employer
   */
  public void load( Employer employer )
  {
    this.employerName = employer.getName();
    this.active = employer.isActive();
    this.statusReason = employer.getStatusReason();
    this.federalTaxId = employer.getFederalTaxId();
    this.stateTaxId = employer.getStateTaxId();
    this.phoneNumber = employer.getPhoneNumber();
    this.phoneExtension = employer.getPhoneExtension();
    this.countryPhoneCode = employer.getCountryPhoneCode();
    this.addressFormBean.load( employer.getAddress() );

    this.createdBy = employer.getAuditCreateInfo().getCreatedBy().toString();
    this.dateCreated = employer.getAuditCreateInfo().getDateCreated().getTime();
    this.id = employer.getId().longValue();
    this.version = employer.getVersion().longValue();
  } // end load

  /**
   * Builds a domain object from the form.
   * 
   * @return Employer
   */
  public Employer toDomainObject()
  {

    Employer employer = new Employer();
    employer.setName( this.employerName );
    employer.setActive( this.active );
    employer.setStatusReason( this.statusReason );
    employer.setFederalTaxId( this.federalTaxId );
    employer.setStateTaxId( this.stateTaxId );
    employer.setPhoneNumber( this.phoneNumber );
    employer.setPhoneExtension( this.phoneExtension );
    employer.setCountryPhoneCode( this.countryPhoneCode );
    employer.setAddress( this.addressFormBean.toDomainObject() );

    employer.setId( new Long( this.id ) );
    employer.setVersion( new Long( this.version ) );
    employer.setVersion( new Long( this.version ) );

    if ( this.createdBy != null )
    {
      employer.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
      employer.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );
    }

    return employer;
  } // end toDomainObject

  public AddressFormBean getAddressFormBean()
  {
    return addressFormBean;
  }

  public void setAddressFormBean( AddressFormBean addressFormBean )
  {
    this.addressFormBean = addressFormBean;
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

  public long getVersion()
  {
    return version;
  }

  public void setVersion( long version )
  {
    this.version = version;
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getFederalTaxId()
  {
    return federalTaxId;
  }

  public void setFederalTaxId( String federalTaxId )
  {
    this.federalTaxId = federalTaxId;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }

  public void setPhoneNumber( String phoneNumber )
  {
    this.phoneNumber = phoneNumber;
  }

  public String getStateTaxId()
  {
    return stateTaxId;
  }

  public void setStateTaxId( String stateTaxId )
  {
    this.stateTaxId = stateTaxId;
  }

  public String getStatusReason()
  {
    return statusReason;
  }

  public void setStatusReason( String statusReason )
  {
    this.statusReason = statusReason;
  }

  public String getEmployerName()
  {
    return employerName;
  }

  public void setEmployerName( String employerName )
  {
    this.employerName = employerName;
  }

  public void setCountryPhoneCode( String countryPhoneCode )
  {
    this.countryPhoneCode = countryPhoneCode;
  }

  public String getCountryPhoneCode()
  {
    return countryPhoneCode;
  }

  public void setPhoneExtension( String phoneExtension )
  {
    this.phoneExtension = phoneExtension;
  }

  public String getPhoneExtension()
  {
    return phoneExtension;
  }

}
