/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionClaimFormStepElementBean.java,v $
 */

package com.biperf.core.ui.promotion;

import java.io.Serializable;

/**
 * PromotionClaimFormStepElementBean.
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
 * <td>June 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionClaimFormStepElementBean implements Serializable
{
  private Long claimFormStepElementId;
  private Long promoClaimValidationId = null;
  private Long version = null;
  private String fieldName;
  private String fieldTypeCode;
  private String fieldType;
  private String validationType;
  private String minimumValue;
  private String maximumValue;
  private String maxLength;
  private String startDate;
  private String endDate;
  private String startsWith;
  private String notStartWith;
  private String endsWith;
  private String notEndWith;
  private String contains;
  private String notContain;
  private String createdBy;
  private long dateCreated;

  /**
   * empty constructor
   */
  public PromotionClaimFormStepElementBean()
  {
    // empty constructor
  }

  /**
   * @return claimFormStepElementId
   */
  public Long getClaimFormStepElementId()
  {
    return claimFormStepElementId;
  }

  /**
   * @param claimFormStepElementId
   */
  public void setClaimFormStepElementId( Long claimFormStepElementId )
  {
    this.claimFormStepElementId = claimFormStepElementId;
  }

  /**
   * @return validationType
   */
  public String getValidationType()
  {
    return validationType;
  }

  /**
   * @param validationType
   */
  public void setValidationType( String validationType )
  {
    this.validationType = validationType;
  }

  /**
   * @return fieldName
   */
  public String getFieldName()
  {
    return fieldName;
  }

  /**
   * @param fieldName
   */
  public void setFieldName( String fieldName )
  {
    this.fieldName = fieldName;
  }

  /**
   * @return fieldType
   */
  public String getFieldType()
  {
    return fieldType;
  }

  /**
   * @param fieldType
   */
  public void setFieldType( String fieldType )
  {
    this.fieldType = fieldType;
  }

  /**
   * @return minimumValue
   */
  public String getMinimumValue()
  {
    return minimumValue;
  }

  /**
   * @param minimumValue
   */
  public void setMinimumValue( String minimumValue )
  {
    this.minimumValue = minimumValue;
  }

  /**
   * @return fieldTypeCode
   */
  public String getFieldTypeCode()
  {
    return fieldTypeCode;
  }

  /**
   * @param fieldTypeCode
   */
  public void setFieldTypeCode( String fieldTypeCode )
  {
    this.fieldTypeCode = fieldTypeCode;
  }

  /**
   * @return contains
   */
  public String getContains()
  {
    return contains;
  }

  /**
   * @param contains
   */
  public void setContains( String contains )
  {
    this.contains = contains;
  }

  /**
   * @return endDate
   */
  public String getEndDate()
  {
    return endDate;
  }

  /**
   * @param endDate
   */
  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  /**
   * @return endsWith
   */
  public String getEndsWith()
  {
    return endsWith;
  }

  /**
   * @param endsWith
   */
  public void setEndsWith( String endsWith )
  {
    this.endsWith = endsWith;
  }

  /**
   * @return maximumValue
   */
  public String getMaximumValue()
  {
    return maximumValue;
  }

  /**
   * @param maximumValue
   */
  public void setMaximumValue( String maximumValue )
  {
    this.maximumValue = maximumValue;
  }

  /**
   * @return maxLength
   */
  public String getMaxLength()
  {
    return maxLength;
  }

  /**
   * @param maxLength
   */
  public void setMaxLength( String maxLength )
  {
    this.maxLength = maxLength;
  }

  /**
   * @return notContain
   */
  public String getNotContain()
  {
    return notContain;
  }

  /**
   * @param notContain
   */
  public void setNotContain( String notContain )
  {
    this.notContain = notContain;
  }

  /**
   * @return notEndWith
   */
  public String getNotEndWith()
  {
    return notEndWith;
  }

  /**
   * @param notEndWith
   */
  public void setNotEndWith( String notEndWith )
  {
    this.notEndWith = notEndWith;
  }

  /**
   * @return notStartWith
   */
  public String getNotStartWith()
  {
    return notStartWith;
  }

  /**
   * @param notStartWith
   */
  public void setNotStartWith( String notStartWith )
  {
    this.notStartWith = notStartWith;
  }

  /**
   * @return startDate
   */
  public String getStartDate()
  {
    return startDate;
  }

  /**
   * @param startDate
   */
  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  /**
   * @return startsWith
   */
  public String getStartsWith()
  {
    return startsWith;
  }

  /**
   * @param startsWith
   */
  public void setStartsWith( String startsWith )
  {
    this.startsWith = startsWith;
  }

  /**
   * @return version
   */
  public Long getVersion()
  {
    return version;
  }

  /**
   * @param version
   */
  public void setVersion( Long version )
  {
    this.version = version;
  }

  /**
   * @return version
   */
  public String getCreatedBy()
  {
    return createdBy;
  }

  /**
   * @param createdBy
   */
  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  /**
   * @return dateCreated
   */
  public long getDateCreated()
  {
    return dateCreated;
  }

  /**
   * @param dateCreated
   */
  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  /**
   * @return promoClaimValidationId
   */
  public Long getPromoClaimValidationId()
  {
    return promoClaimValidationId;
  }

  /**
   * @param promoClaimValidationId
   */
  public void setPromoClaimValidationId( Long promoClaimValidationId )
  {
    this.promoClaimValidationId = promoClaimValidationId;
  }

}
