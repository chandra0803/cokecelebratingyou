/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/claim/ClaimForm.java,v $
 */

package com.biperf.core.domain.claim;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.domain.enums.ClaimFormModuleType;
import com.biperf.core.domain.promotion.Promotion;

/**
 * ClaimForm <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>robinsra</td>
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimForm extends Form
{

  /** cmConstants for Claim Form */
  public static final String CM_CLAIM_FORM_ASSET_PREFIX = "claim_form_data.claimform.";
  public static final String CM_CLAIM_FORM_SECTION = "claim_form_data";
  public static final String CM_CLAIM_FORM_NAME_KEY = "FORM_NAME";
  public static final String CM_CLAIM_FORM_NAME_KEY_DESC = "Form Name";

  private ClaimFormModuleType claimFormModuleType;
  private Set promotions = new LinkedHashSet();
  private int promotionCount;
  private List claimFormSteps = new ArrayList();
  private String cmAssetCode;

  /**
   * Empty Contructor.
   */
  public ClaimForm()
  {
    super();
  }

  /**
   * Copy Constructor.
   * 
   * @param claimFormToCopy
   * @throws CloneNotSupportedException
   */
  public ClaimForm( ClaimForm claimFormToCopy ) throws CloneNotSupportedException
  {
    this.setName( claimFormToCopy.getName() );
    this.setCmAssetCode( claimFormToCopy.getCmAssetCode() );
    this.setDescription( claimFormToCopy.getDescription() );
    this.setClaimFormModuleType( claimFormToCopy.getClaimFormModuleType() );
    this.setClaimFormStatusType( claimFormToCopy.getClaimFormStatusType() );

    Iterator iter = claimFormToCopy.getClaimFormSteps().iterator();
    while ( iter.hasNext() )
    {
      ClaimFormStep claimFormStepToCopy = (ClaimFormStep)iter.next();

      this.addClaimFormStep( new ClaimFormStep( claimFormStepToCopy ) );
    }
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  /**
   * Builds a String representation of this class.
   * 
   * @return String
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( "ClaimForm [" );
    buf.append( "{id=" + super.getId() + "}, " );
    buf.append( "{name=" + this.getName() + "}, " );
    buf.append( "{description=" + this.getDescription() + "}, " );
    buf.append( "{claimFormModuleCode=" + this.getClaimFormModuleType().getCode() + "}, " );
    buf.append( "{claimFormStatusCode=" + this.getClaimFormStatusType().getCode() + "}, " );
    buf.append( "]" );
    return buf.toString();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {

    return super.equals( object );

  } // end equals

  public Set getPromotions()
  {
    return promotions;
  }

  public void setPromotions( Set promotions )
  {
    this.promotions = promotions;
  }

  public void addPromotion( Promotion promotion )
  {
    promotion.setClaimForm( this );
    getPromotions().add( promotion );
  }

  public ClaimFormModuleType getClaimFormModuleType()
  {
    return claimFormModuleType;
  }

  public void setClaimFormModuleType( ClaimFormModuleType claimFormModuleType )
  {
    this.claimFormModuleType = claimFormModuleType;
  }

  /**
   * Get the set of claimFormSteps associated to this ClaimForm.
   * 
   * @return Set
   */
  public List<ClaimFormStep> getClaimFormSteps()
  {
    return claimFormSteps;
  }

  /**
   * Assign the Set of claimFormSteps onto this ClaimForm.
   * 
   * @param claimFormSteps
   */
  public void setClaimFormSteps( List claimFormSteps )
  {
    this.claimFormSteps = claimFormSteps;
  }

  /**
   * Add a claimFormStep to this ClaimForm's set of ClaimFormSteps.
   * 
   * @param claimFormStep
   */
  public void addClaimFormStep( ClaimFormStep claimFormStep )
  {
    claimFormStep.setClaimForm( this );
    this.claimFormSteps.add( claimFormStep );
  }

  /**
   * @return String date last updated
   */
  public Date getLastUpdatedDate()
  {
    Date lastUpdatedDate = null;

    if ( this.getAuditUpdateInfo() != null && this.getAuditUpdateInfo().getDateModified() != null )
    {
      lastUpdatedDate = this.getAuditUpdateInfo().getDateModified();
    }
    else
    {
      lastUpdatedDate = this.getAuditCreateInfo().getDateCreated();
    }
    return lastUpdatedDate;
  }

  /**
   * Checks if the form is at a status that can be edited.
   * 
   * @return boolean - Returns true if the form is "Under Construction" or "Completed" phase, and
   *         false otherwise.
   */
  public boolean isEditable()
  {

    // first make sure there is a status type available to check
    if ( getClaimFormStatusType() == null )
    {
      return false;
    }

    if ( getClaimFormStatusType().isUnderConstruction() || getClaimFormStatusType().isCompleted() )
    {
      return true;
    }

    return false;
  }

  /**
   * Checks if the form is at a status that can be deleted.
   * 
   * @return boolean - Returns true if the form is "Under Construction" or "Completed" with no links
   *         to expired promotions, and false otherwise.
   */
  public boolean isDeleteable()
  {
    // first make sure there is a status type available to check
    if ( getClaimFormStatusType() == null )
    {
      return false;
    }

    if ( getClaimFormStatusType().isUnderConstruction() || getClaimFormStatusType().isCompleted() && getPromotions().isEmpty() )
    {
      return true;
    }

    return false;
  }

  /**
   * Add the claimFormStep at a specific location in the list of claimFormSteps.
   * 
   * @param indexOfStep
   * @param claimFormStep
   */
  public void addClaimFormStep( int indexOfStep, ClaimFormStep claimFormStep )
  {
    claimFormStep.setClaimForm( this );
    this.claimFormSteps.add( indexOfStep, claimFormStep );
  }

  public int getPromotionCount()
  {
    return promotionCount;
  }

  public void setPromotionCount( int promotionCount )
  {
    this.promotionCount = promotionCount;
  }

  public boolean hasCustomFormElements()
  {
    if ( this.getClaimFormSteps() != null )
    {
      for ( ClaimFormStep step : this.getClaimFormSteps() )
      {
        if ( !step.getClaimFormStepElements().isEmpty() )
        {
          return true;
        }
      }
    }
    return false;
  }
} // end class ClaimForm
