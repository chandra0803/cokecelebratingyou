/*
 File: ClaimFormStepForm.java
 (c) 2005 BI, Inc.  All rights reserved.
 
 Change History:

 Author       Date      Version  Comments
 -----------  --------  -------  -----------------------------
 crosenquest      Jun 3, 2005   1.0      Created
 
 */

package com.biperf.core.ui.claimform;

import java.sql.Timestamp;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepEmailNotification;
import com.biperf.core.domain.enums.ClaimFormStepEmailNotificationType;
import com.biperf.core.ui.BaseForm;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author crosenquest
 */
public class ClaimFormStepForm extends BaseForm
{
  /**
   * Defines the name of the form to allow easy access whilst the form is in the request.
   */
  public static final String FORM_NAME = "claimFormStepForm";
  private Long id = new Long( 0 );
  private Long version;
  private String cmKeyFragment;
  private String name = "";
  private int sequence;
  private boolean salesRequired = false;
  private boolean deleted = false;
  private Long claimFormId;
  private String dateCreated = "0";
  private String createdBy = "";
  private String method = "";
  private String[] emailNotificationTypes;

  /** Define the Auto Delay Form Fields */
  private int numberOfDays;

  /** Define the Nth claim to force review claim */
  private int nthClaim;

  public ActionErrors validate( ActionMapping anActionMapping, HttpServletRequest aRequest )
  {
    ActionErrors actionErrors = super.validate( anActionMapping, aRequest );
    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
      /* -- Bug # 12725: need to remove below section, since validation works -- */

      /* I have added to validation.xml but I cant get it to work */
      /*
       * if ( this.name == null || this.name.length() <= 0 ) { actionErrors.add(
       * ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "claims.claimformstep.NAME_REQUIRED_ERROR"
       * ) ); }
       */
      /* -- end bug fix : Bug # 12725 */
    }

    aRequest.setAttribute( "claimFormId", claimFormId );

    return actionErrors;
  }

  /**
   * Load the form with the domain object value;
   * 
   * @param claimFormStep
   */
  public void load( ClaimFormStep claimFormStep )
  {
    this.salesRequired = claimFormStep.isSalesRequired();
    this.claimFormId = claimFormStep.getClaimForm().getId();
    this.name = "";
    this.cmKeyFragment = claimFormStep.getCmKeyFragment();

    if ( claimFormStep.getId() != null )
    {
      this.name = CmsResourceBundle.getCmsBundle().getString( claimFormStep.getClaimForm().getCmAssetCode(), claimFormStep.getCmKeyForName() );
      this.id = claimFormStep.getId();
      this.version = claimFormStep.getVersion();
    }

    if ( claimFormStep.getAuditCreateInfo().getDateCreated() != null )
    {
      this.createdBy = claimFormStep.getAuditCreateInfo().getCreatedBy().toString();
      this.dateCreated = String.valueOf( claimFormStep.getAuditCreateInfo().getDateCreated().getTime() );
    }

    if ( claimFormStep.getClaimFormStepEmailNotifications().size() > 0 )
    {

      this.emailNotificationTypes = new String[claimFormStep.getClaimFormStepEmailNotifications().size()];

      Iterator claimFormStepEmailNotificationIter = claimFormStep.getClaimFormStepEmailNotifications().iterator();

      int position = 0;

      while ( claimFormStepEmailNotificationIter.hasNext() )
      {
        ClaimFormStepEmailNotification claimFormStepEmailNotification = (ClaimFormStepEmailNotification)claimFormStepEmailNotificationIter.next();

        this.emailNotificationTypes[position] = claimFormStepEmailNotification.getClaimFormStepEmailNotificationType().getCode();

        position++;

      }
    }
  }

  /**
   * Create the domain object with the values populated on this form.
   * 
   * @return ClaimFormStep
   */
  public ClaimFormStep toDomain()
  {
    ClaimFormStep claimFormStep = new ClaimFormStep();

    if ( this.id.longValue() == 0 )
    {
      claimFormStep.setId( null );
      claimFormStep.setVersion( new Long( 0 ) );
      claimFormStep.getAuditCreateInfo().setCreatedBy( null );
      claimFormStep.getAuditCreateInfo().setDateCreated( null );

    }
    else
    {
      claimFormStep.setId( this.id );
      claimFormStep.setVersion( this.version );
      claimFormStep.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
      claimFormStep.getAuditCreateInfo().setDateCreated( new Timestamp( new Long( this.dateCreated ).longValue() ) );
      claimFormStep.setCmKeyFragment( this.cmKeyFragment );
    }

    claimFormStep.setSalesRequired( this.salesRequired );

    ClaimForm claimForm = new ClaimForm();
    claimForm.setId( this.claimFormId );

    claimFormStep.setClaimForm( claimForm );

    if ( this.emailNotificationTypes != null && this.emailNotificationTypes.length > 0 )
    {

      // Load the EmailNotificationTypes onto the ClaimFormStep
      for ( int i = 0; i < this.emailNotificationTypes.length; i++ )
      {
        claimFormStep.addClaimFormStepEmailNotification( buildClaimFormStepEmailNotification( this.emailNotificationTypes[i] ) );
      }
    }

    return claimFormStep;
  }

  /**
   * Builds a ClaimFormStepEmailNotification from the user's selected email notifications.
   * 
   * @param notificationTypeCode
   * @return ClaimFormStepEmailNotification
   */
  private ClaimFormStepEmailNotification buildClaimFormStepEmailNotification( String notificationTypeCode )
  {

    ClaimFormStepEmailNotification claimFormStepEmailNotification = new ClaimFormStepEmailNotification();
    claimFormStepEmailNotification.setClaimFormStepEmailNotificationType( ClaimFormStepEmailNotificationType.lookup( notificationTypeCode ) );

    return claimFormStepEmailNotification;
  }

  /**
   * Get the claimFormId from this form.
   * 
   * @return Long
   */
  public Long getClaimFormId()
  {
    return claimFormId;
  }

  /**
   * Set the claimFormId onto this form.
   * 
   * @param claimFormId
   */
  public void setClaimFormId( Long claimFormId )
  {
    this.claimFormId = claimFormId;
  }

  /**
   * Get the createdBy from this.
   * 
   * @return String
   */
  public String getCreatedBy()
  {
    return createdBy;
  }

  /**
   * Set the createdBy value onto this.
   * 
   * @param createdBy
   */
  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  /**
   * Get the dateCreated from this.
   * 
   * @return String
   */
  public String getDateCreated()
  {
    return dateCreated;
  }

  /**
   * Set the dateCreated onto this.
   * 
   * @param dateCreated
   */
  public void setDateCreated( String dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  /**
   * Get the isDeleted from this.
   * 
   * @return boolean
   */
  public boolean isDeleted()
  {
    return deleted;
  }

  /**
   * Set the isDeleted onto this.
   * 
   * @param deleted
   */
  public void setDeleted( boolean deleted )
  {
    this.deleted = deleted;
  }

  /**
   * Get the id from this.
   * 
   * @return Long
   */
  public Long getId()
  {
    return id;
  }

  /**
   * Set the id onto this.
   * 
   * @param id
   */
  public void setId( Long id )
  {
    this.id = id;
  }

  /**
   * Get the method from this.
   * 
   * @return String
   */
  public String getMethod()
  {
    return method;
  }

  /**
   * Set the method onto this.
   * 
   * @param method
   */
  public void setMethod( String method )
  {
    this.method = method;
  }

  /**
   * Get the name from this.
   * 
   * @return String
   */
  public String getName()
  {
    return name;
  }

  /**
   * Set the name onto this.
   * 
   * @param name
   */
  public void setName( String name )
  {
    this.name = name;
  }

  /**
   * Get the isSalesRequired from from this.
   * 
   * @return boolean
   */
  public boolean isSalesRequired()
  {
    return salesRequired;
  }

  /**
   * Set the isSalesRequired onto this.
   * 
   * @param salesRequired
   */
  public void setSalesRequired( boolean salesRequired )
  {
    this.salesRequired = salesRequired;
  }

  /**
   * Get the sequence from this.
   * 
   * @return int
   */
  public int getSequence()
  {
    return sequence;
  }

  /**
   * Set the sequence onto this.
   * 
   * @param sequence
   */
  public void setSequence( int sequence )
  {
    this.sequence = sequence;
  }

  /**
   * Get the version from this.
   * 
   * @return Long
   */
  public Long getVersion()
  {
    return version;
  }

  /**
   * Set the version onto this.
   * 
   * @param version
   */
  public void setVersion( Long version )
  {
    this.version = version;
  }

  /**
   * Set the list of EmailNotificationTypes.
   * 
   * @param emailNotificationTypes
   */
  public void setEmailNotificationTypes( String[] emailNotificationTypes )
  {
    this.emailNotificationTypes = emailNotificationTypes;
  }

  /**
   * Get the emailNotificationTypes.
   * 
   * @return String[]
   */
  public String[] getEmailNotificationTypes()
  {
    return emailNotificationTypes;
  }

  /**
   * Set the numberOfDays which define the autoDelay time.
   * 
   * @return int
   */
  public int getNumberOfDays()
  {
    return numberOfDays;
  }

  /**
   * Set the numberOfDays value.
   * 
   * @param numberOfDays
   */
  public void setNumberOfDays( int numberOfDays )
  {
    this.numberOfDays = numberOfDays;
  }

  /**
   * Get the value to review after Nth number of claims.
   * 
   * @return int
   */
  public int getNthClaim()
  {
    return nthClaim;
  }

  /**
   * Set the value to review after every nth claim.
   * 
   * @param nthClaim
   */
  public void setNthClaim( int nthClaim )
  {
    this.nthClaim = nthClaim;
  }

  public String getCmKeyFragment()
  {
    return cmKeyFragment;
  }

  public void setCmKeyFragment( String cmKeyFragment )
  {
    this.cmKeyFragment = cmKeyFragment;
  }

}
