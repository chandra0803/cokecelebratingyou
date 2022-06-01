/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claimform/ClaimFormForm.java,v $
 */

package com.biperf.core.ui.claimform;

import java.sql.Timestamp;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.enums.ClaimFormModuleType;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.ui.BaseActionForm;

/**
 * ClaimFormForm.
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
 * <td>Jun 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormForm extends BaseActionForm
{
  private String formName;
  private String newFormName;
  private String cmAssetCode;
  private String description;
  private String claimFormModuleType;
  private String claimFormModuleTypeDesc;
  private String claimFormStatusType;
  private String claimFormStatusTypeDesc;

  private long claimFormId;
  private long version;
  private long dateCreated;
  private String createdBy;
  private String method;
  private String[] delete;
  private boolean editable;

  /**
   * Load the Claim Form to the form
   * 
   * @param claimForm
   */
  public void load( ClaimForm claimForm )
  {
    this.formName = claimForm.getName();
    this.cmAssetCode = claimForm.getCmAssetCode();
    this.description = claimForm.getDescription();
    this.claimFormModuleType = claimForm.getClaimFormModuleType().getCode();
    this.claimFormModuleTypeDesc = claimForm.getClaimFormModuleType().getName();
    this.claimFormStatusType = claimForm.getClaimFormStatusType().getCode();
    this.claimFormStatusTypeDesc = claimForm.getClaimFormStatusType().getName();
    this.claimFormId = claimForm.getId().longValue();
    this.createdBy = claimForm.getAuditCreateInfo().getCreatedBy().toString();
    this.dateCreated = claimForm.getAuditCreateInfo().getDateCreated().getTime();
    this.version = claimForm.getVersion().longValue();
    this.editable = claimForm.isEditable();
  } // end load

  /**
   * Builds a domain object from the form.
   * 
   * @return ClaimForm
   */
  public ClaimForm toInsertedDomainObject()
  {
    ClaimForm claimForm = new ClaimForm();

    claimForm.setName( this.formName );
    claimForm.setCmAssetCode( this.cmAssetCode );
    claimForm.setDescription( this.description );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( this.claimFormModuleType ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( this.claimFormStatusType ) );

    return claimForm;
  } // end toInsertedDomainObject

  /**
   * Builds a full domain object from the form.
   * 
   * @return ClaimForm
   */
  public ClaimForm toFullDomainObject()
  {
    ClaimForm claimForm = new ClaimForm();

    claimForm.setId( new Long( this.claimFormId ) );
    claimForm.setName( this.formName );
    claimForm.setCmAssetCode( this.cmAssetCode );
    claimForm.setDescription( this.description );
    claimForm.setClaimFormModuleType( ClaimFormModuleType.lookup( this.claimFormModuleType ) );
    claimForm.setClaimFormStatusType( ClaimFormStatusType.lookup( this.claimFormStatusType ) );
    claimForm.setVersion( new Long( this.version ) );
    claimForm.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    claimForm.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );
    return claimForm;

  } // end toInsertedDomainObject

  public long getClaimFormId()
  {
    return claimFormId;
  }

  public void setClaimFormId( long claimFormId )
  {
    this.claimFormId = claimFormId;
  }

  public String getClaimFormModuleType()
  {
    return claimFormModuleType;
  }

  public void setClaimFormModuleType( String claimFormModuleType )
  {
    this.claimFormModuleType = claimFormModuleType;
  }

  public String getClaimFormModuleTypeDesc()
  {
    return claimFormModuleTypeDesc;
  }

  public void setClaimFormModuleTypeDesc( String claimFormModuleTypeDesc )
  {
    this.claimFormModuleTypeDesc = claimFormModuleTypeDesc;
  }

  public String getClaimFormStatusType()
  {
    return claimFormStatusType;
  }

  public void setClaimFormStatusType( String claimFormStatusType )
  {
    this.claimFormStatusType = claimFormStatusType;
  }

  public String getClaimFormStatusTypeDesc()
  {
    return claimFormStatusTypeDesc;
  }

  public void setClaimFormStatusTypeDesc( String claimFormStatusTypeDesc )
  {
    this.claimFormStatusTypeDesc = claimFormStatusTypeDesc;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
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

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getFormName()
  {
    return formName;
  }

  public void setFormName( String formName )
  {
    this.formName = formName;
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

  public String[] getDelete()
  {
    return delete;
  }

  public void setDelete( String[] delete )
  {
    this.delete = delete;
  }

  public String getNewFormName()
  {
    return newFormName;
  }

  public void setNewFormName( String newFormName )
  {
    this.newFormName = newFormName;
  }

  public boolean isEditable()
  {
    return editable;
  }

  public void setEditable( boolean editable )
  {
    this.editable = editable;
  }

}
// end class ClaimFormForm
