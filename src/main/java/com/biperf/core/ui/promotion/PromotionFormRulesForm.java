/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/promotion/PromotionFormRulesForm.java,v $
 */

package com.biperf.core.ui.promotion;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.PromotionClaimFormStepElementValidationType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionClaimFormStepElementValidation;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * PromotionFormRulesForm.
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
 * <td>Jul 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionFormRulesForm extends BaseActionForm
{
  private Long promotionId;
  private Long claimFormId;
  private String promotionName;
  private String promotionTypeName;
  private String promotionTypeCode;
  private String promotionStatus;
  private String method;

  private boolean hasParent;

  private List claimFormStepList;

  /**
   * Reset all properties to their default values.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // PromotionClaimFormStepBeans. If this is not done, the form wont initialize
    // properly.
    claimFormStepList = getEmptyValueList( request, RequestUtils.getOptionalParamInt( request, "claimFormStepValueListCount" ) );
  }

  /**
   * resets the value list with empty PromotionClaimFormStepBeans
   * 
   * @param valueListCount
   * @return List
   */
  private List getEmptyValueList( HttpServletRequest request, int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionClaimFormStepBean
      PromotionClaimFormStepBean promoClaimFormStepBean = new PromotionClaimFormStepBean();
      List elementValueList = new ArrayList();
      int claimFormStepElementListSize = RequestUtils.getOptionalParamInt( request, "claimFormStepList[" + i + "].claimFormStepElementValueListCount" );
      for ( int j = 0; j < claimFormStepElementListSize; j++ )
      {
        // create and add empty PromotionClaimFormStepElementBeans
        elementValueList.add( new PromotionClaimFormStepElementBean() );
      }
      promoClaimFormStepBean.setClaimFormStepElementValueList( elementValueList );
      valueList.add( promoClaimFormStepBean );
    }

    return valueList;
  }

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
    if ( !isHasParent() )
    {
      if ( getClaimFormStepValueListCount() > 0 )
      {
        Iterator promoClaimFormStepIterator = getClaimFormStepList().iterator();
        while ( promoClaimFormStepIterator.hasNext() )
        {
          PromotionClaimFormStepBean promoClaimFormStep = (PromotionClaimFormStepBean)promoClaimFormStepIterator.next();
          String cmAssetCode = promoClaimFormStep.getCmAssetCode();
          if ( promoClaimFormStep.getClaimFormStepElementValueListCount() > 0 )
          {
            Iterator promoClaimFormStepElementIterator = promoClaimFormStep.getClaimFormStepElementValueList().iterator();
            while ( promoClaimFormStepElementIterator.hasNext() )
            {
              PromotionClaimFormStepElementBean promoClaimFormStepElement = (PromotionClaimFormStepElementBean)promoClaimFormStepElementIterator.next();
              // There will only be fields to validate if the ValidationType is "validate"
              if ( promoClaimFormStepElement.getValidationType().equals( "validate" ) )
              {
                // Number field validations
                if ( promoClaimFormStepElement.getFieldTypeCode().equals( "number" ) )
                {
                  // At least one field needs to be populated
                  if ( promoClaimFormStepElement.getMinimumValue().equals( "" ) && promoClaimFormStepElement.getMaximumValue().equals( "" ) )
                  {
                    actionErrors
                        .add( ActionErrors.GLOBAL_MESSAGE,
                              new ActionMessage( "promotion.form.rule.errors.REQ_NUMBER_FIELD", CmsResourceBundle.getCmsBundle().getString( cmAssetCode, promoClaimFormStepElement.getFieldName() ) ) );
                  }

                  int minimumValue = 0;
                  int maximumValue = 0;
                  // If the field is not empty, check to see if it is an integer
                  if ( !promoClaimFormStepElement.getMinimumValue().equals( "" ) )
                  {
                    try
                    {
                      minimumValue = Integer.parseInt( promoClaimFormStepElement.getMinimumValue() );
                    }
                    catch( NumberFormatException nfe )
                    {
                      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                                        new ActionMessage( "promotion.form.rule.errors.INVALID_FIELD",
                                                           CmsResourceBundle.getCmsBundle().getString( cmAssetCode, promoClaimFormStepElement.getFieldName() ),
                                                           CmsResourceBundle.getCmsBundle().getString( "promotion.form.rules", "MIN_VALUE" ) ) );
                    }
                  }
                  // If the field is not empty, check to see if it is an integer
                  if ( !promoClaimFormStepElement.getMaximumValue().equals( "" ) )
                  {
                    try
                    {
                      maximumValue = Integer.parseInt( promoClaimFormStepElement.getMaximumValue() );
                    }
                    catch( NumberFormatException nfe )
                    {
                      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                                        new ActionMessage( "promotion.form.rule.errors.INVALID_FIELD",
                                                           CmsResourceBundle.getCmsBundle().getString( cmAssetCode, promoClaimFormStepElement.getFieldName() ),
                                                           CmsResourceBundle.getCmsBundle().getString( "promotion.form.rules", "MAX_VALUE" ) ) );
                    }
                  }

                  if ( minimumValue != 0 || maximumValue != 0 )
                  {
                    // Check to make sure the minimum is < than the maximum
                    if ( minimumValue >= maximumValue )
                    {
                      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                                        new ActionMessage( "promotion.form.rule.errors.MIN_MAX_INVALID",
                                                           CmsResourceBundle.getCmsBundle().getString( cmAssetCode, promoClaimFormStepElement.getFieldName() ) ) );
                    }
                  }
                }
                // Date field validations
                else if ( promoClaimFormStepElement.getFieldTypeCode().equals( "date" ) )
                {
                  // At least one field needs to be populated
                  if ( promoClaimFormStepElement.getStartDate().equals( "" ) && promoClaimFormStepElement.getEndDate().equals( "" ) )
                  {
                    actionErrors
                        .add( ActionErrors.GLOBAL_MESSAGE,
                              new ActionMessage( "promotion.form.rule.errors.REQ_DATE_FIELD", CmsResourceBundle.getCmsBundle().getString( cmAssetCode, promoClaimFormStepElement.getFieldName() ) ) );
                  }

                  Date startDate = null;
                  Date endDate = null;
                  SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
                  // If the field is not empty, check to make sure it has a valid date value
                  if ( !promoClaimFormStepElement.getStartDate().equals( "" ) )
                  {
                    try
                    {
                      startDate = sdf.parse( promoClaimFormStepElement.getStartDate().trim() );
                    }
                    catch( ParseException ex )
                    {
                      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                                        new ActionMessage( "promotion.form.rule.errors.INVALID_FIELD",
                                                           CmsResourceBundle.getCmsBundle().getString( cmAssetCode, promoClaimFormStepElement.getFieldName() ),
                                                           CmsResourceBundle.getCmsBundle().getString( "promotion.form.rules", "START_DATE" ) ) );
                    }
                  }
                  // If the field is not empty, check to make sure it has a valid date value
                  if ( !promoClaimFormStepElement.getEndDate().equals( "" ) )
                  {
                    try
                    {
                      endDate = sdf.parse( promoClaimFormStepElement.getEndDate().trim() );
                    }
                    catch( ParseException ex )
                    {
                      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                                        new ActionMessage( "promotion.form.rule.errors.INVALID_FIELD",
                                                           CmsResourceBundle.getCmsBundle().getString( cmAssetCode, promoClaimFormStepElement.getFieldName() ),
                                                           CmsResourceBundle.getCmsBundle().getString( "promotion.form.rules", "END_DATE" ) ) );
                    }
                  }
                  // If the fields are not null, then make sure that the startDate is before the
                  // endDate
                  if ( startDate != null && endDate != null )
                  {
                    if ( !startDate.before( endDate ) )
                    {
                      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                                        new ActionMessage( "promotion.form.rule.errors.START_END_DATE_INVALID",
                                                           CmsResourceBundle.getCmsBundle().getString( cmAssetCode, promoClaimFormStepElement.getFieldName() ) ) );
                    }
                  }
                }
                // Text field validations
                else if ( promoClaimFormStepElement.getFieldTypeCode().equals( "text" ) || promoClaimFormStepElement.getFieldTypeCode().equals( "text_box" ) )
                {
                  // Make sure at least one field is populated
                  if ( promoClaimFormStepElement.getStartsWith().equals( "" ) && promoClaimFormStepElement.getNotStartWith().equals( "" ) && promoClaimFormStepElement.getEndsWith().equals( "" )
                      && promoClaimFormStepElement.getNotEndWith().equals( "" ) && promoClaimFormStepElement.getContains().equals( "" ) && promoClaimFormStepElement.getNotContain().equals( "" )
                      && promoClaimFormStepElement.getMaxLength().equals( "" ) )
                  {
                    actionErrors
                        .add( ActionErrors.GLOBAL_MESSAGE,
                              new ActionMessage( "promotion.form.rule.errors.REQ_TEXT_FIELD", CmsResourceBundle.getCmsBundle().getString( cmAssetCode, promoClaimFormStepElement.getFieldName() ) ) );
                  }
                  // If the maxLength is not empty, make sure it is a valid integer
                  if ( !promoClaimFormStepElement.getMaxLength().equals( "" ) )
                  {
                    try
                    {
                      Integer.parseInt( promoClaimFormStepElement.getMaxLength() );
                    }
                    catch( NumberFormatException nfe )
                    {
                      actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                                        new ActionMessage( "promotion.form.rule.errors.INVALID_FIELD",
                                                           CmsResourceBundle.getCmsBundle().getString( cmAssetCode, promoClaimFormStepElement.getFieldName() ),
                                                           CmsResourceBundle.getCmsBundle().getString( "promotion.form.rules", "MAX_LENGTH" ) ) );
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return actionErrors;
  }

  /**
   * Load all the information needed for displaying the page
   * 
   * @param promotion - the looked up promotion object
   * @param promoValidations - the list of existing promoFormValidations
   */
  public void load( Promotion promotion, List promoValidations )
  {
    this.promotionId = promotion.getId();
    this.promotionName = promotion.getName();
    this.claimFormId = promotion.getClaimForm().getId();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionStatus = promotion.getPromotionStatus().getCode();
    this.hasParent = promotion.hasParent();

    // If the Promotion ClaimForm has ClaimFormSteps, iterate over them to build a list of
    // PromotionClaimFormStepBeans that will hold the ClaimFormStep info
    if ( promotion.getClaimForm().getClaimFormSteps() != null && promotion.getClaimForm().getClaimFormSteps().size() > 0 )
    {
      Iterator claimFormStepIterator = promotion.getClaimForm().getClaimFormSteps().iterator();
      while ( claimFormStepIterator.hasNext() )
      {
        ClaimFormStep claimFormStep = (ClaimFormStep)claimFormStepIterator.next();

        PromotionClaimFormStepBean promoClaimFormStepBean = new PromotionClaimFormStepBean();
        promoClaimFormStepBean.setClaimFormStepId( claimFormStep.getId() );
        promoClaimFormStepBean.setCmAssetCode( claimFormStep.getClaimForm().getCmAssetCode() );
        promoClaimFormStepBean.setCmName( claimFormStep.getCmKeyForName() );
        // If the ClaimFormStep has ClaimFormStepElements, iterate over them to build a list of
        // PromotionClaimFormStepElementBeans that will hold the element validation info
        if ( claimFormStep.getClaimFormStepElements() != null && claimFormStep.getClaimFormStepElements().size() > 0 )
        {
          List claimFormStepElements = new ArrayList();

          Iterator claimFormStepElementIterator = claimFormStep.getClaimFormStepElements().iterator();
          while ( claimFormStepElementIterator.hasNext() )
          {
            ClaimFormStepElement claimFormStepElement = (ClaimFormStepElement)claimFormStepElementIterator.next();
            // We only want ClaimFormStepElements that have a type of number, date, or text
            if ( claimFormStepElement.getClaimFormElementType().getCode().equals( "number" ) || claimFormStepElement.getClaimFormElementType().getCode().equals( "date" )
                || claimFormStepElement.getClaimFormElementType().getCode().equals( "text" ) || claimFormStepElement.getClaimFormElementType().getCode().equals( "text_box" ) )
            {
              Long claimFormStepElementId = claimFormStepElement.getId();

              PromotionClaimFormStepElementBean promoClaimFormStepElementBean = new PromotionClaimFormStepElementBean();
              promoClaimFormStepElementBean.setClaimFormStepElementId( claimFormStepElementId );
              promoClaimFormStepElementBean.setFieldName( claimFormStepElement.getCmKeyForElementLabel() );
              promoClaimFormStepElementBean.setFieldType( claimFormStepElement.getClaimFormElementType().getName() );
              promoClaimFormStepElementBean.setFieldTypeCode( claimFormStepElement.getClaimFormElementType().getCode() );
              // Set the ValidationType to the default value of "collect"
              promoClaimFormStepElementBean.setValidationType( "collect" );
              // If there are existing validation records, iterate over them to the validation
              // specific
              // information
              if ( promoValidations != null && promoValidations.size() > 0 )
              {
                Iterator promoValidationIterator = promoValidations.iterator();
                while ( promoValidationIterator.hasNext() )
                {
                  PromotionClaimFormStepElementValidation promoClaimFormStepElementValidation = (PromotionClaimFormStepElementValidation)promoValidationIterator.next();
                  // If the claimFormStepElement.id's match, then add the validation info to the
                  // PromotionClaimFormStepElementBean
                  if ( promoClaimFormStepElementValidation.getClaimFormStepElement().getId().equals( claimFormStepElementId ) )
                  {
                    promoClaimFormStepElementBean.setPromoClaimValidationId( promoClaimFormStepElementValidation.getId() );
                    promoClaimFormStepElementBean.setVersion( promoClaimFormStepElementValidation.getVersion() );
                    promoClaimFormStepElementBean.setValidationType( promoClaimFormStepElementValidation.getValidationType().getCode() );
                    if ( promoClaimFormStepElementValidation.getMinValue() != null )
                    {
                      promoClaimFormStepElementBean.setMinimumValue( promoClaimFormStepElementValidation.getMinValue().toString() );
                    }
                    if ( promoClaimFormStepElementValidation.getMaxValue() != null )
                    {
                      promoClaimFormStepElementBean.setMaximumValue( promoClaimFormStepElementValidation.getMaxValue().toString() );
                    }
                    if ( promoClaimFormStepElementValidation.getMaxLength() != null )
                    {
                      promoClaimFormStepElementBean.setMaxLength( promoClaimFormStepElementValidation.getMaxLength().toString() );
                    }
                    promoClaimFormStepElementBean.setStartDate( DateUtils.toDisplayString( promoClaimFormStepElementValidation.getStartDate() ) );
                    promoClaimFormStepElementBean.setEndDate( DateUtils.toDisplayString( promoClaimFormStepElementValidation.getEndDate() ) );
                    promoClaimFormStepElementBean.setStartsWith( promoClaimFormStepElementValidation.getStartsWith() );
                    promoClaimFormStepElementBean.setNotStartWith( promoClaimFormStepElementValidation.getNotStartWith() );
                    promoClaimFormStepElementBean.setEndsWith( promoClaimFormStepElementValidation.getEndsWith() );
                    promoClaimFormStepElementBean.setNotEndWith( promoClaimFormStepElementValidation.getNotEndWith() );
                    promoClaimFormStepElementBean.setContains( promoClaimFormStepElementValidation.getContains() );
                    promoClaimFormStepElementBean.setNotContain( promoClaimFormStepElementValidation.getNotContain() );
                    promoClaimFormStepElementBean.setCreatedBy( promoClaimFormStepElementValidation.getAuditCreateInfo().getCreatedBy().toString() );
                    promoClaimFormStepElementBean.setDateCreated( promoClaimFormStepElementValidation.getAuditCreateInfo().getDateCreated().getTime() );
                  }
                }
              }
              claimFormStepElements.add( promoClaimFormStepElementBean );
            }
          }
          promoClaimFormStepBean.setClaimFormStepElementValueList( claimFormStepElements );
        }

        claimFormStepList.add( promoClaimFormStepBean );
      }
    }
  }

  /**
   * Build a list of PromotionClaimFormStepElementValidation domain objects to be persisted
   * 
   * @return List
   */
  public List toDomainObjects()
  {
    List validations = new ArrayList();

    // TODO: review instantiation of promotion???
    // Create a promotion object to be used for each PromotionClaimFormStepElementValidation object
    // created here. The ID is the only thing set because the actual object will be looked up in the
    // service
    Promotion promotion = new ProductClaimPromotion();
    promotion.setId( getPromotionId() );

    if ( getClaimFormStepValueListCount() > 0 )
    {
      Iterator promoClaimFormStepIterator = getClaimFormStepList().iterator();
      while ( promoClaimFormStepIterator.hasNext() )
      {
        PromotionClaimFormStepBean promoClaimFormStep = (PromotionClaimFormStepBean)promoClaimFormStepIterator.next();
        if ( promoClaimFormStep.getClaimFormStepElementValueListCount() > 0 )
        {
          Iterator promoClaimFormStepElementIterator = promoClaimFormStep.getClaimFormStepElementValueList().iterator();
          while ( promoClaimFormStepElementIterator.hasNext() )
          {
            PromotionClaimFormStepElementBean promoClaimFormStepElement = (PromotionClaimFormStepElementBean)promoClaimFormStepElementIterator.next();
            // Create a ClaimFormStepElement object to be used for each
            // PromotionClaimFormStepElementValidation
            // object belonging to this element. Only set the id because the actual object will be
            // looked
            // up in the service
            ClaimFormStepElement claimFormStepElement = new ClaimFormStepElement();
            claimFormStepElement.setId( promoClaimFormStepElement.getClaimFormStepElementId() );

            // Create a new PromotionClaimFormStepElementValidation object and set all the
            // appropriate
            // info
            PromotionClaimFormStepElementValidation pcfsev = new PromotionClaimFormStepElementValidation();

            if ( promoClaimFormStepElement.getPromoClaimValidationId().longValue() != 0 )
            {
              pcfsev.setId( promoClaimFormStepElement.getPromoClaimValidationId() );
              pcfsev.setVersion( promoClaimFormStepElement.getVersion() );
            }

            pcfsev.setPromotion( promotion );
            pcfsev.setClaimFormStepElement( claimFormStepElement );
            pcfsev.setValidationType( PromotionClaimFormStepElementValidationType.lookup( promoClaimFormStepElement.getValidationType() ) );
            if ( promoClaimFormStepElement.getValidationType().equals( "validate" ) )
            {
              if ( promoClaimFormStepElement.getMinimumValue() != null && !promoClaimFormStepElement.getMinimumValue().equals( "" ) )
              {
                pcfsev.setMinValue( new Integer( promoClaimFormStepElement.getMinimumValue() ) );
              }
              if ( promoClaimFormStepElement.getMaximumValue() != null && !promoClaimFormStepElement.getMaximumValue().equals( "" ) )
              {
                pcfsev.setMaxValue( new Integer( promoClaimFormStepElement.getMaximumValue() ) );
              }
              if ( promoClaimFormStepElement.getMaxLength() != null && !promoClaimFormStepElement.getMaxLength().equals( "" ) )
              {
                pcfsev.setMaxLength( new Integer( promoClaimFormStepElement.getMaxLength() ) );
              }
              pcfsev.setStartDate( DateUtils.toDate( promoClaimFormStepElement.getStartDate() ) );
              pcfsev.setEndDate( DateUtils.toDate( promoClaimFormStepElement.getEndDate() ) );
              pcfsev.setStartsWith( promoClaimFormStepElement.getStartsWith() );
              pcfsev.setNotStartWith( promoClaimFormStepElement.getNotStartWith() );
              pcfsev.setEndsWith( promoClaimFormStepElement.getEndsWith() );
              pcfsev.setNotEndWith( promoClaimFormStepElement.getNotEndWith() );
              pcfsev.setContains( promoClaimFormStepElement.getContains() );
              pcfsev.setNotContain( promoClaimFormStepElement.getNotContain() );
            }
            if ( !promoClaimFormStepElement.getCreatedBy().equals( "" ) )
            {
              pcfsev.getAuditCreateInfo().setCreatedBy( Long.valueOf( promoClaimFormStepElement.getCreatedBy() ) );
            }
            pcfsev.getAuditCreateInfo().setDateCreated( new Timestamp( promoClaimFormStepElement.getDateCreated() ) );

            validations.add( pcfsev );
          }
        }
      }
    }
    return validations;
  }

  public Long getClaimFormId()
  {
    return claimFormId;
  }

  public void setClaimFormId( Long claimFormId )
  {
    this.claimFormId = claimFormId;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * @return List of PromotionClaimFormStepBean objects
   */
  public List getClaimFormStepList()
  {
    return claimFormStepList;
  }

  public void setClaimFormStepList( List claimFormStepList )
  {
    this.claimFormStepList = claimFormStepList;
  }

  /**
   * Accessor for the number of PromotionClaimFormStepBean objects in the list.
   * 
   * @return int
   */
  public int getClaimFormStepValueListCount()
  {
    if ( claimFormStepList == null )
    {
      return 0;
    }

    return claimFormStepList.size();
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionClaimFormStepBean from the value list
   */
  public PromotionClaimFormStepBean getClaimFormStepValueList( int index )
  {
    try
    {
      return (PromotionClaimFormStepBean)claimFormStepList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public boolean isHasParent()
  {
    return hasParent;
  }

  public void setHasParent( boolean hasParent )
  {
    this.hasParent = hasParent;
  }

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }
}
