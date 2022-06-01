/*
 File: ClaimFormStepElement.java
 (c) 2005 BI, Inc.  All rights reserved.
 
 Change History:

 Author       Date      Version  Comments
 -----------  --------  -------  -----------------------------
 crosenquest      Jun 3, 2005   1.0      Created
 
 */

package com.biperf.core.domain.claim;

import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.NumberFieldInputFormatType;
import com.biperf.core.domain.enums.TextFieldInputFormatType;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * @author crosenquest
 */
public class ClaimFormStepElement extends FormStepElement implements Cloneable
{
  // The parent Claim Form Step
  private ClaimFormStep claimFormStep;

  // CM
  public static final String CM_CFSE_ELEMENT_LABEL_DESC_SUFFIX = " Element Label";
  public static final String CM_CFSE_HEADING_DESC_SUFFIX = " Heading";
  public static final String CM_CFSE_COPY_BLOCK_DESC_SUFFIX = " Copy Block";
  public static final String CM_CFSE_LINK_NAME_DESC_SUFFIX = " Link Name";
  public static final String CM_CFSE_LABEL_TRUE_DESC_SUFFIX = " Label True";
  public static final String CM_CFSE_LABEL_FALSE_DESC_SUFFIX = " Label Fale";
  public static final String CM_CFSE_BUTTON_LABEL_DESC_SUFFIX = " Button Label";

  // Basic Attributes
  private ClaimFormElementType claimFormElementType;

  // Number Field
  private Integer numberOfDecimals;
  private NumberFieldInputFormatType numberFieldInputFormat;

  // shared
  private boolean maskedOnEntry;
  private Long customerInformationBlockId;
  private HierarchyUniqueConstraintEnum uniqueness;
  private boolean shouldEncrypt;

  // Text Field
  private Integer maxSize;
  private TextFieldInputFormatType textFieldInputFormat;

  // Link
  private String linkURL;

  private String selectionPickListName;

  // SSI Phase-2 Changes
  private String fileType;
  private Integer fileSize;

  // Text Box
  private boolean whyField;

  private Integer sequenceNumber;

  /**
   * Empty Contructor
   */
  public ClaimFormStepElement()
  {
    super();
  }

  /**
   * @param id
   */
  public ClaimFormStepElement( Long id )
  {
    super();
    super.setId( id );
  }

  /**
   * Clones this object for copy claimForm. Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {

    ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)super.clone();
    claimFormStepElement.resetBaseDomain();

    return claimFormStepElement;
  }

  // TODO I'm not sure about the best way to handle this enum through Hibernate..
  // So for now, I'm only mapping the enum code in hibernate

  public String getUniquenessCode()
  {
    if ( uniqueness != null )
    {
      return uniqueness.getCode();
    }

    return null;
  }

  public void setUniquenessCode( String code )
  {
    uniqueness = HierarchyUniqueConstraintEnum.getEnum( code );
  }

  public HierarchyUniqueConstraintEnum getUniqueness()
  {
    return uniqueness;
  }

  public void setUniqueness( HierarchyUniqueConstraintEnum uniqueness )
  {
    this.uniqueness = uniqueness;
  }

  public Integer getNumberOfDecimals()
  {
    return numberOfDecimals;
  }

  public void setNumberOfDecimals( Integer numberOfDecimals )
  {
    this.numberOfDecimals = numberOfDecimals;
  }

  public ClaimFormElementType getClaimFormElementType()
  {
    return claimFormElementType;
  }

  public void setClaimFormElementType( ClaimFormElementType claimFormElementType )
  {
    this.claimFormElementType = claimFormElementType;
  }

  public NumberFieldInputFormatType getNumberFieldInputFormat()
  {
    return numberFieldInputFormat;
  }

  public void setNumberFieldInputFormat( NumberFieldInputFormatType numberFieldInputFormat )
  {
    this.numberFieldInputFormat = numberFieldInputFormat;
  }

  public boolean isMaskedOnEntry()
  {
    return maskedOnEntry;
  }

  public void setMaskedOnEntry( boolean maskedOnEntry )
  {
    this.maskedOnEntry = maskedOnEntry;
  }

  public boolean isShouldEncrypt()
  {
    return shouldEncrypt;
  }

  public void setShouldEncrypt( boolean shouldEncrypt )
  {
    this.shouldEncrypt = shouldEncrypt;
  }

  public Integer getMaxSize()
  {
    return maxSize;
  }

  public void setMaxSize( Integer maxSize )
  {
    this.maxSize = maxSize;
  }

  public TextFieldInputFormatType getTextFieldInputFormat()
  {
    return textFieldInputFormat;
  }

  public void setTextFieldInputFormat( TextFieldInputFormatType textFieldInputFormat )
  {
    this.textFieldInputFormat = textFieldInputFormat;
  }

  public String getLinkURL()
  {
    return linkURL;
  }

  public void setLinkURL( String linkURL )
  {
    this.linkURL = linkURL;
  }

  public String getSelectionPickListName()
  {
    return selectionPickListName;
  }

  public void setSelectionPickListName( String selectionPickListName )
  {
    this.selectionPickListName = selectionPickListName;
  }

  public ClaimFormStep getClaimFormStep()
  {
    return claimFormStep;
  }

  public void setClaimFormStep( ClaimFormStep claimFormStep )
  {
    this.claimFormStep = claimFormStep;
  }

  public String getFileType()
  {
    return fileType;
  }

  public void setFileType( String fileType )
  {
    this.fileType = fileType;
  }

  public Integer getFileSize()
  {
    return fileSize;
  }

  public void setFileSize( Integer fileSize )
  {
    this.fileSize = fileSize;
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( o == null || ! ( o instanceof ClaimFormStepElement ) )
    {
      return false;
    }

    final ClaimFormStepElement that = (ClaimFormStepElement)o;

    if ( claimFormStep != null ? !claimFormStep.equals( that.getClaimFormStep() ) : that.claimFormStep != null )
    {
      return false;
    }
    if ( cmKeyFragment != null ? !cmKeyFragment.equals( that.getCmKeyFragment() ) : that.cmKeyFragment != null )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result;
    result = claimFormStep != null ? claimFormStep.hashCode() : 0;
    result = 29 * result + ( cmKeyFragment != null ? cmKeyFragment.hashCode() : 0 );
    return result;
  }

  public String getCmKeyForElementLabel()
  {
    // If this prefix is changed - it effects the reports.
    return "ELEMENTLABEL_" + getCmKeyFragment();
  }

  public String getCmKeyForCopyBlock()
  {
    // If this prefix is changed - it effects the reports.
    return "COPYBLOCK_" + getCmKeyFragment();
  }

  public String getCmKeyForHeading()
  {
    // If this prefix is changed - it effects the reports.
    return "HEADING_" + getCmKeyFragment();
  }

  public String getCmKeyForLabelTrue()
  {
    // If this prefix is changed - it effects the reports.
    return "LABELTRUE_" + getCmKeyFragment();
  }

  public String getCmKeyForLabelFalse()
  {
    // If this prefix is changed - it effects the reports.
    return "LABELFALSE_" + getCmKeyFragment();
  }

  public String getCmKeyForLinkName()
  {
    // If this prefix is changed - it effects the reports.
    return "LINKNAME_" + getCmKeyFragment();
  }

  public String getCmKeyForButton()
  {
    // If this prefix is changed - it effects the reports.
    return "BUTTON_" + getCmKeyFragment();
  }

  public Long getCustomerInformationBlockId()
  {
    return customerInformationBlockId;
  }

  public void setCustomerInformationBlockId( Long customerInformationBlockId )
  {
    this.customerInformationBlockId = customerInformationBlockId;
  }

  public String getCmKeyFragment()
  {
    return cmKeyFragment;
  }

  public void setCmKeyFragment( String cmKeyFragment )
  {
    this.cmKeyFragment = cmKeyFragment;
  }

  /**
   * getter for returning the cm value for the claim form step element label.
   * 
   * @return String
   */
  public String getI18nLabel()
  {
    return ContentReaderManager.getText( claimFormStep.getClaimForm().getCmAssetCode(), getCmKeyForElementLabel() );
  }

  public String getI18nHeading()
  {
    return ContentReaderManager.getText( claimFormStep.getClaimForm().getCmAssetCode(), getCmKeyForHeading() );
  }

  public String getI18nCopy()
  {
    return ContentReaderManager.getText( claimFormStep.getClaimForm().getCmAssetCode(), getCmKeyForCopyBlock() );
  }

  public Integer getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( Integer sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  public boolean isWhyField()
  {
    return whyField;
  }

  public void setWhyField( boolean whyField )
  {
    this.whyField = whyField;
  }

}
