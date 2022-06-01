/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.claim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionErrors;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.CustomerInformationBlock;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.ui.BaseForm;

/**
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
 * <td>wadzinsk</td>
 * <td>Nov 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BaseClaimElementSubmissionForm extends BaseForm
{

  private Long claimFormStepId;
  private List claimElementForms = new ArrayList();
  private String claimFormAsset;

  protected void loadClaimElements( ClaimFormStep claimFormStep, Claim claim )
  {
    claimFormAsset = claimFormStep.getClaimForm().getCmAssetCode();

    claimFormStepId = claimFormStep.getId();
    // -----------------------
    // Load the claimElements
    // -----------------------
    for ( Iterator iterator = claimFormStep.getClaimFormStepElements().iterator(); iterator.hasNext(); )
    {
      ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)iterator.next();
      ClaimElement claimElement = createOrGetClaimElement( claim, claimFormStepElement );

      ClaimElementForm claimElementForm = new ClaimElementForm();
      claimElementForm.load( claimElement );

      claimElementForms.add( claimElementForm );
    }
  }

  private ClaimElement createOrGetClaimElement( Claim claim, ClaimFormStepElement claimFormStepElement )
  {
    ClaimElement claimElement = null;

    if ( claim == null )
    {
      // Hack until I understand recog submission reset() method
      claimElement = new ClaimElement();
      claimElement.setClaimFormStepElement( claimFormStepElement );
      return claimElement;
    }

    for ( Iterator iter = claim.getClaimElements().iterator(); iter.hasNext(); )
    {
      ClaimElement potentialMatchingClaimElement = (ClaimElement)iter.next();
      if ( claimFormStepElement.equals( potentialMatchingClaimElement.getClaimFormStepElement() ) )
      {
        claimElement = potentialMatchingClaimElement;
      }
    }
    if ( claimElement == null )
    {
      claimElement = new ClaimElement();
      claimElement.setClaimFormStepElement( claimFormStepElement );
    }
    return claimElement;
  }

  protected void toDomainObjectClaimElements( Claim claim )
  {
    claim.getClaimElements().clear();

    for ( int i = 0; i < claimElementForms.size(); i++ )
    {
      // Add ClaimElement objects to the Claim object.
      ClaimElementForm claimElementForm = (ClaimElementForm)claimElementForms.get( i );
      ClaimElement claimElement = claimElementForm.toDomainObject();
      claim.addClaimElement( claimElement );
    }
  }

  public ClaimElementForm getClaimElement( int index )
  {
    return (ClaimElementForm)claimElementForms.get( index );
  }

  public void setClaimElementForms( List claimElementForms )
  {
    this.claimElementForms = claimElementForms;
  }

  public List getClaimElementForms()
  {
    return claimElementForms;
  }

  public void setClaimElement( int index, ClaimElementForm claimElementForm )
  {
    claimElementForms.set( index, claimElementForm );
  }

  public void addClaimElementForm( ClaimElementForm claimElementForm )
  {
    claimElementForms.add( claimElementForm );
  }

  public Long getClaimFormStepId()
  {
    return claimFormStepId;
  }

  public void setClaimFormStepId( Long claimFormStepId )
  {
    this.claimFormStepId = claimFormStepId;
  }

  public String getClaimFormAsset()
  {
    return claimFormAsset;
  }

  public void setClaimFormAsset( String claimFormAsset )
  {
    this.claimFormAsset = claimFormAsset;
  }

  protected void validateClaimElements( ActionErrors actionErrors )
  {
    for ( Iterator elementsIterator = claimElementForms.iterator(); elementsIterator.hasNext(); )
    {
      ClaimElementForm claimElementForm = (ClaimElementForm)elementsIterator.next();
      ClaimFormStepElement claimFormStepElement = claimElementForm.getClaimFormStepElement();

      // validate address fields
      if ( claimFormStepElement.getClaimFormElementType().isAddressBlock() )
      {
        if ( CustomerInformationBlock.MAIN_ADDRESS_1_CFSE_ID.equals( claimFormStepElement.getCustomerInformationBlockId() )
            || CustomerInformationBlock.ADDITIONAL_ADDRESS_2_CFSE_ID.equals( claimFormStepElement.getCustomerInformationBlockId() ) )
        {

          if ( !StringUtils.isEmpty( claimElementForm.getValue() ) )
          {
            // this is to take care of address1
            claimElementForm.getMainAddressFormBean().setAddr1( claimElementForm.getValue() );
          }

          claimElementForm.getMainAddressFormBean().validateAddress( actionErrors );
        }
      }

      // Rest of validations now done inside claimService.validateClaim() to allow for file uploads
      // to use the validation
    }
  }

  public void populateClaimElementPickLists()
  {
    Iterator iterator = getClaimElementForms().iterator();
    while ( iterator.hasNext() )
    {
      ClaimElementForm claimElementForm = (ClaimElementForm)iterator.next();
      ClaimFormStepElement claimFormStepElement = claimElementForm.getClaimFormStepElement();
      ClaimFormElementType claimFormElementType = claimFormStepElement.getClaimFormElementType();

      if ( claimFormElementType.isSelectField() || claimFormElementType.isMultiSelectField() )
      {
        claimElementForm.setPickList( DynaPickListType.getList( claimFormStepElement.getSelectionPickListName() ) );
      }
    }
  }

}
