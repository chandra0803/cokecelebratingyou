/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claimform/ClaimFormStepElementForm.java,v $
 */

package com.biperf.core.ui.claimform;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.HierarchyUniqueConstraintEnum;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.NumberFieldInputFormatType;
import com.biperf.core.domain.enums.TextFieldInputFormatType;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.claim.ClaimFormStepElementCMDataHolder;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * ClaimFormStepElementForm.
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
 * <td>zahler</td>
 * <td>Jun 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormStepElementForm extends BaseForm
{
  public static final String FORM_NAME = "claimFormStepElementForm";
  private static final Log logger = LogFactory.getLog( ClaimFormStepElementForm.class );

  private String method;
  private Long claimFormStepElementId;
  private Long claimFormStepId;
  private Long claimFormId;
  private Long version;
  private long dateCreated;
  private String createdBy;

  private String cmKeyFragment;

  private String description;
  private Boolean required;
  private String claimFormStepElementTypeCode;
  private String claimFormStepElementTypeDesc;

  // Number Field
  private String numberFieldInputFormatTypeCode;
  private String numberFieldInputFormatTypeDesc;
  private String maxDecimals;

  // shared
  private Boolean maskedOnEntry;
  private Boolean shouldEncrypt;

  // Text Field
  private String maxSize;
  private String textFieldInputFormatTypeCode;
  private String textFieldInputFormatTypeDesc;

  // Link
  private String linkURL;

  private String selectionPickListName;

  private String buttonScript;

  private ClaimFormStepElementCMDataHolder cmData;
  private String uniquenessTypeCode;

  // File Field
  private String fileType;
  private String fileSize;

  // Text Box
  private Boolean whyField;

  public ClaimFormStepElementForm()
  {
    // make sure this is initialized with all Strings
    cmData = new ClaimFormStepElementCMDataHolder( "", "", "", "", "", "" );

    // set the following default values so the initial JSP displays properly
    required = new Boolean( false );
    maskedOnEntry = new Boolean( false );
    shouldEncrypt = new Boolean( false );
    whyField = new Boolean( false );
    uniquenessTypeCode = HierarchyUniqueConstraintEnum.NONE_CODE;
  }

  public ClaimFormStepElementCMDataHolder getCmData()
  {
    return cmData;
  }

  public void setCmData( ClaimFormStepElementCMDataHolder cmData )
  {
    this.cmData = cmData;
  }

  /**
   * @param claimFormStepElement
   */
  public void load( ClaimFormStepElement claimFormStepElement )
  {
    claimFormStepElementId = claimFormStepElement.getId();
    version = claimFormStepElement.getVersion();
    dateCreated = claimFormStepElement.getAuditCreateInfo().getDateCreated().getTime();
    createdBy = claimFormStepElement.getAuditCreateInfo().getCreatedBy().toString();
    cmKeyFragment = claimFormStepElement.getCmKeyFragment();
    description = claimFormStepElement.getDescription();
    required = Boolean.valueOf( claimFormStepElement.isRequired() );
    claimFormStepElementTypeCode = claimFormStepElement.getClaimFormElementType().getCode();
    claimFormStepElementTypeDesc = claimFormStepElement.getClaimFormElementType().getName();
    if ( claimFormStepElement.getNumberFieldInputFormat() != null )
    {
      numberFieldInputFormatTypeCode = claimFormStepElement.getNumberFieldInputFormat().getCode();
      numberFieldInputFormatTypeDesc = claimFormStepElement.getNumberFieldInputFormat().getName();
    }
    maskedOnEntry = Boolean.valueOf( claimFormStepElement.isMaskedOnEntry() );
    if ( claimFormStepElement.getUniqueness() != null )
    {
      uniquenessTypeCode = claimFormStepElement.getUniqueness().getCode();
    }
    shouldEncrypt = Boolean.valueOf( claimFormStepElement.isShouldEncrypt() );
    maxSize = String.valueOf( claimFormStepElement.getMaxSize() );
    maxDecimals = String.valueOf( claimFormStepElement.getNumberOfDecimals() );
    if ( claimFormStepElement.getTextFieldInputFormat() != null )
    {
      textFieldInputFormatTypeCode = claimFormStepElement.getTextFieldInputFormat().getCode();
      textFieldInputFormatTypeDesc = claimFormStepElement.getTextFieldInputFormat().getName();
    }
    linkURL = claimFormStepElement.getLinkURL();

    selectionPickListName = claimFormStepElement.getSelectionPickListName();

    // SSI Phase-2 Changes
    fileSize = claimFormStepElement.getFileSize() != null ? String.valueOf( claimFormStepElement.getFileSize() ) : "";
    fileType = claimFormStepElement.getFileType();

    whyField = Boolean.valueOf( claimFormStepElement.isWhyField() );
  }

  /**
   * @return ClaimFormStepElement
   */
  public ClaimFormStepElement toDomainObject()
  {
    ClaimFormStepElement claimFormStepElement = new ClaimFormStepElement();

    claimFormStepElement.setId( this.claimFormStepElementId );
    claimFormStepElement.setVersion( this.version );
    claimFormStepElement.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );
    if ( this.createdBy != null && !this.createdBy.equals( "" ) )
    {
      claimFormStepElement.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    }

    claimFormStepElement.setCmKeyFragment( this.cmKeyFragment );
    claimFormStepElement.setDescription( this.description );
    if ( this.required != null )
    {
      claimFormStepElement.setRequired( this.required.booleanValue() );
    }
    else
    {
      claimFormStepElement.setRequired( false );
    }

    claimFormStepElement.setClaimFormElementType( ClaimFormElementType.lookup( this.claimFormStepElementTypeCode ) );

    claimFormStepElement.setNumberFieldInputFormat( NumberFieldInputFormatType.lookup( this.numberFieldInputFormatTypeCode ) );

    if ( this.maskedOnEntry != null )
    {
      claimFormStepElement.setMaskedOnEntry( this.maskedOnEntry.booleanValue() );
    }

    claimFormStepElement.setUniquenessCode( this.uniquenessTypeCode );

    if ( this.maskedOnEntry != null )
    {
      claimFormStepElement.setShouldEncrypt( this.shouldEncrypt.booleanValue() );
    }

    if ( StringUtils.isNotEmpty( this.maxSize ) )
    {
      claimFormStepElement.setMaxSize( new Integer( this.maxSize ) );
    }

    if ( StringUtils.isNotEmpty( this.maxDecimals ) )
    {
      claimFormStepElement.setNumberOfDecimals( new Integer( this.maxDecimals ) );
    }

    claimFormStepElement.setTextFieldInputFormat( TextFieldInputFormatType.lookup( this.textFieldInputFormatTypeCode ) );
    claimFormStepElement.setLinkURL( this.linkURL );
    claimFormStepElement.setSelectionPickListName( this.selectionPickListName );

    // SSI Phase-2 Changes
    if ( !StringUtil.isNullOrEmpty( this.fileSize ) )
    {
      claimFormStepElement.setFileSize( Integer.parseInt( this.fileSize ) );
    }
    claimFormStepElement.setFileType( this.fileType );

    if ( this.whyField != null )
    {
      claimFormStepElement.setWhyField( this.whyField.booleanValue() );
    }

    return claimFormStepElement;
  }

  public String getMaxDecimals()
  {
    return maxDecimals;
  }

  public void setMaxDecimals( String maxDecimals )
  {
    this.maxDecimals = maxDecimals;
  }

  public Long getClaimFormStepId()
  {
    return claimFormStepId;
  }

  public void setClaimFormStepId( Long claimFormStepId )
  {
    this.claimFormStepId = claimFormStepId;
  }

  public Long getClaimFormId()
  {
    return claimFormId;
  }

  public void setClaimFormId( Long claimFormId )
  {
    this.claimFormId = claimFormId;
  }

  public String getButtonScript()
  {
    return buttonScript;
  }

  public void setButtonScript( String buttonScript )
  {
    this.buttonScript = buttonScript;
  }

  public Long getClaimFormStepElementId()
  {
    return claimFormStepElementId;
  }

  public void setClaimFormStepElementId( Long claimFormStepElementId )
  {
    this.claimFormStepElementId = claimFormStepElementId;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getUniquenessTypeCode()
  {
    return uniquenessTypeCode;
  }

  public void setUniquenessTypeCode( String uniquenessTypeCode )
  {
    this.uniquenessTypeCode = uniquenessTypeCode;
  }

  public String getFileType()
  {
    return fileType;
  }

  public void setFileType( String fileType )
  {
    this.fileType = fileType;
  }

  public String getFileSize()
  {
    return fileSize;
  }

  public void setFileSize( String fileSize )
  {
    this.fileSize = fileSize;
  }

  public String getClaimFormStepElementTypeCode()
  {
    return claimFormStepElementTypeCode;
  }

  public void setClaimFormStepElementTypeCode( String claimFormElementTypeCode )
  {
    this.claimFormStepElementTypeCode = claimFormElementTypeCode;
  }

  public String getClaimFormStepElementTypeDesc()
  {
    return claimFormStepElementTypeDesc;
  }

  public void setClaimFormStepElementTypeDesc( String claimFormElementTypeDesc )
  {
    this.claimFormStepElementTypeDesc = claimFormElementTypeDesc;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getLinkURL()
  {
    return linkURL;
  }

  public void setLinkURL( String linkURL )
  {
    this.linkURL = linkURL;
  }

  public Boolean getMaskedOnEntry()
  {
    return maskedOnEntry;
  }

  public void setMaskedOnEntry( Boolean maskedOnEntry )
  {
    this.maskedOnEntry = maskedOnEntry;
  }

  public String getMaxSize()
  {
    return maxSize;
  }

  public void setMaxSize( String maxSize )
  {
    this.maxSize = maxSize;
  }

  public String getNumberFieldInputFormatTypeCode()
  {
    return numberFieldInputFormatTypeCode;
  }

  public void setNumberFieldInputFormatTypeCode( String numberFieldInputFormatTypeCode )
  {
    this.numberFieldInputFormatTypeCode = numberFieldInputFormatTypeCode;
  }

  public String getNumberFieldInputFormatTypeDesc()
  {
    return numberFieldInputFormatTypeDesc;
  }

  public void setNumberFieldInputFormatTypeDesc( String numberFieldInputFormatTypeDesc )
  {
    this.numberFieldInputFormatTypeDesc = numberFieldInputFormatTypeDesc;
  }

  public Boolean getRequired()
  {
    return required;
  }

  public void setRequired( Boolean required )
  {
    this.required = required;
  }

  public String getSelectionPickListName()
  {
    return selectionPickListName;
  }

  public void setSelectionPickListName( String selectionPickListName )
  {
    this.selectionPickListName = selectionPickListName;
  }

  public Boolean getShouldEncrypt()
  {
    return shouldEncrypt;
  }

  public void setShouldEncrypt( Boolean shouldEncrypt )
  {
    this.shouldEncrypt = shouldEncrypt;
  }

  public String getTextFieldInputFormatTypeCode()
  {
    return textFieldInputFormatTypeCode;
  }

  public void setTextFieldInputFormatTypeCode( String textFieldInputFormatTypeCode )
  {
    this.textFieldInputFormatTypeCode = textFieldInputFormatTypeCode;
  }

  public String getTextFieldInputFormatTypeDesc()
  {
    return textFieldInputFormatTypeDesc;
  }

  public void setTextFieldInputFormatTypeDesc( String textFieldInputFormatTypeDesc )
  {
    this.textFieldInputFormatTypeDesc = textFieldInputFormatTypeDesc;
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

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getCmKeyFragment()
  {
    return cmKeyFragment;
  }

  public void setCmKeyFragment( String cmKeyFragment )
  {
    this.cmKeyFragment = cmKeyFragment;
  }

  public boolean isWhyField()
  {
    return whyField;
  }

  public void setWhyField( boolean whyField )
  {
    this.whyField = whyField;
  }

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
    ClaimFormElementType elementType = ClaimFormElementType.lookup( this.claimFormStepElementTypeCode );
    Iterator elementsIterator = ClaimFormStepElementItem.getItemListByType( elementType ).iterator();
    while ( elementsIterator.hasNext() )
    {
      ClaimFormStepElementItem claimFormStepElementItem = (ClaimFormStepElementItem)elementsIterator.next();
      try
      {
        // validate required properties.
        if ( claimFormStepElementItem.isRequired() && isEmpty( PropertyUtils.getNestedProperty( this, claimFormStepElementItem.getFormProperty() ) ) )
        {
          actionErrors.add( claimFormStepElementItem.getFormProperty(),
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED,
                                               CmsResourceBundle.getCmsBundle().getString( "claims.form.step.element." + claimFormStepElementItem.getLabelKey() ) ) );

        }
        // validate that number fields are actually numbers, only if they enter
        if ( !isEmpty( PropertyUtils.getNestedProperty( this, claimFormStepElementItem.getFormProperty() ) ) && claimFormStepElementItem.isNumberField()
            && !isInteger( PropertyUtils.getNestedProperty( this, claimFormStepElementItem.getFormProperty() ) ) )
        {
          actionErrors.add( claimFormStepElementItem.getFormProperty(),
                            new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INTEGER,
                                               CmsResourceBundle.getCmsBundle().getString( "claims.form.step.element." + claimFormStepElementItem.getLabelKey() ) ) );
        }
        else if ( claimFormStepElementItem.getFormProperty().equals( "fileSize" ) && !isValidSize( PropertyUtils.getNestedProperty( this, claimFormStepElementItem.getFormProperty() ) ) )
        {
          actionErrors.add( claimFormStepElementItem.getFormProperty(), new ActionMessage( "claims.form.step.element.INVALID_SIZE", getAllowedFileSize() ) );
        }

        // Only one step element can be marked as the why field at a time
        if ( claimFormStepElementItem.getFormProperty().equals( "whyField" ) && whyField )
        {
          List<ClaimFormStepElement> siblingStepElements = getClaimFormDefinitionService().getAllClaimFormStepElementsByClaimFormId( claimFormId );
          long numWhyFieldElements = siblingStepElements.stream().filter( ( el ) -> el.isWhyField() && !claimFormStepElementId.equals( el.getId() ) ).count();
          if ( numWhyFieldElements > 0 )
          {
            actionErrors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "claims.form.step.element.MULTIPLE_WHY_FIELDS" ) );
          }
        }
      }
      catch( IllegalAccessException e )
      {
        actionErrors.add( claimFormStepElementItem.getFormProperty(), new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION, claimFormStepElementItem.getFormProperty() ) );
      }
      catch( InvocationTargetException e )
      {
        actionErrors.add( claimFormStepElementItem.getFormProperty(), new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION, claimFormStepElementItem.getFormProperty() ) );
      }
      catch( NoSuchMethodException e )
      {
        actionErrors.add( claimFormStepElementItem.getFormProperty(), new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_SYSTEM_EXCEPTION, claimFormStepElementItem.getFormProperty() ) );
      }
    }

    logger.info( "<<< " + METHOD_NAME );
    return actionErrors;
  } // end validate

  private boolean isValidSize( Object object )
  {
    boolean validSize = true;
    Integer allowedFileSize = getAllowedFileSize();
    Integer size = Integer.parseInt( (String)object );
    if ( size.intValue() < 0 || size > allowedFileSize.intValue() )
    {
      validSize = false;
    }
    return validSize;
  }

  private int getAllowedFileSize()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal();
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

  private ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)BeanLocator.getBean( ClaimFormDefinitionService.BEAN_NAME );
  }

  /**
   * Check if this object is null or the empty string.
   * 
   * @param objectk
   * @return boolean
   */
  public boolean isEmpty( Object object )
  {
    if ( object instanceof String )
    {
      return StringUtils.isEmpty( (String)object );
    }

    return object == null;
  }

  /**
   * Check to see if the object is a valid number.
   * 
   * @param object
   * @return boolean
   */
  public boolean isInteger( Object object )
  {
    if ( object instanceof String )
    {
      if ( StringUtils.isNumeric( (String)object ) )
      {
        try
        {
          Integer i = new Integer( (String)object );
          return i != null;
        }
        catch( NumberFormatException e )
        {
          // do nothing..we want to return false here.
        }
      }
      return false;
    }

    return object instanceof Long || object instanceof Integer || object instanceof Short || object instanceof BigDecimal;
  }

  // I use this in the claimFormStepElemCreate.jsp and claimFormStepElemUpdate.jsp for a TinyMCE
  // textarea
  // It's a bit of a cheat/hack because right now copy block is the only textarea element
  public String getCopyBlock()
  {
    return cmData.getCopyBlock();
  }
}
