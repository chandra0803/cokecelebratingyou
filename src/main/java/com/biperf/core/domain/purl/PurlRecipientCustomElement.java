/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/purl/PurlRecipientCustomElement.java,v $
 */

package com.biperf.core.domain.purl;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.crypto.Base64;
import com.biperf.core.utils.crypto.ByteArrayGuard;
import com.biperf.core.utils.crypto.InvalidMacException;

/**
 * PurlRecipientCustomElement
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
 * <td>drahn</td>
 * <td>Dec 12, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

/**
 * This class holds a value which the user enters for a Claim Form Step Element (i.e. "Erik" for the
 * element "Name").
 */
public class PurlRecipientCustomElement extends BaseDomain implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final Log logger = LogFactory.getLog( PurlRecipientCustomElement.class );

  private PurlRecipient purlRecipient;
  private ClaimFormStepElement claimFormStepElement;
  private String value;
  private String valueDecrypted;

  private static final String CLAIM_FORM_ENCRYPT_PASSWORD = "password";

  public PurlRecipient getPurlRecipient()
  {
    return purlRecipient;
  }

  public void setPurlRecipient( PurlRecipient purlRecipient )
  {
    this.purlRecipient = purlRecipient;
  }

  public ClaimFormStepElement getClaimFormStepElement()
  {
    return claimFormStepElement;
  }

  public void setClaimFormStepElement( ClaimFormStepElement element )
  {
    this.claimFormStepElement = element;
  }

  public String getValue()
  {
    // decrypt if necessary
    if ( claimFormStepElement.isShouldEncrypt() && StringUtils.isNotBlank( value ) )
    {
      if ( valueDecrypted == null )
      {
        // decrypt the value and store in the valueDecrypted variable so we don't decrypt twice.
        byte[] encryptedBytes = Base64.decode( value );
        ByteArrayGuard byteArrayGuard = new ByteArrayGuard( CLAIM_FORM_ENCRYPT_PASSWORD );
        try
        {
          valueDecrypted = new String( byteArrayGuard.decrypt( encryptedBytes ) );
        }
        catch( InvalidMacException e )
        {
          logger.info( "Cannot decrypt PurlRecipientCustomElement value for PurlRecipientCustomElementId=" + getId() );
          valueDecrypted = value;
        }
      }
      return valueDecrypted;
    }
    return value;
  }

  public String getDisplayValue()
  {
    // decrypt if necessary
    if ( claimFormStepElement.isShouldEncrypt() && StringUtils.isNotBlank( value ) )
    {
      if ( valueDecrypted == null )
      {
        // decrypt the value and store in the valueDecrypted variable so we don't decrypt twice.
        byte[] encryptedBytes = Base64.decode( value );
        ByteArrayGuard byteArrayGuard = new ByteArrayGuard( CLAIM_FORM_ENCRYPT_PASSWORD );
        try
        {
          valueDecrypted = new String( byteArrayGuard.decrypt( encryptedBytes ) );
        }
        catch( InvalidMacException e )
        {
          logger.info( "Cannot decrypt PurlRecipientCustomElement value for PurlRecipientCustomElementId=" + getId() );
          valueDecrypted = value;
        }
      }
      value = valueDecrypted;
    }

    String displayValue = value;

    if ( StringUtils.isNotBlank( value ) )
    {
      if ( claimFormStepElement.getClaimFormElementType().isSelectField() )
      {
        displayValue = DynaPickListType.lookup( claimFormStepElement.getSelectionPickListName(), value ).getName();
      }
      else if ( claimFormStepElement.getClaimFormElementType().isMultiSelectField() )
      {
        displayValue = "";
        Iterator pickListCodes = ArrayUtil.convertDelimitedStringToList( value, "," ).iterator();
        while ( pickListCodes.hasNext() )
        {
          String code = (String)pickListCodes.next();
          displayValue += DynaPickListType.lookup( claimFormStepElement.getSelectionPickListName(), code ).getName();
          if ( pickListCodes.hasNext() )
          {
            displayValue += ", ";
          }
        }
      }
    }

    return displayValue;
  }

  public void setValue( String value )
  {
    // encrypt if necessary
    if ( claimFormStepElement.isShouldEncrypt() && StringUtils.isNotBlank( value ) )
    {
      ByteArrayGuard byteArrayGuard = new ByteArrayGuard( CLAIM_FORM_ENCRYPT_PASSWORD );
      this.value = Base64.encodeBytes( byteArrayGuard.encrypt( value.getBytes() ) );
      this.valueDecrypted = value;
    }
    else
    {
      this.value = value;
      this.valueDecrypted = null;
    }
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( o == null || getClass() != o.getClass() )
    {
      return false;
    }

    final PurlRecipientCustomElement element1 = (PurlRecipientCustomElement)o;

    if ( purlRecipient != null ? !purlRecipient.equals( element1.purlRecipient ) : element1.purlRecipient != null )
    {
      return false;
    }
    if ( claimFormStepElement != null ? !claimFormStepElement.equals( element1.claimFormStepElement ) : element1.claimFormStepElement != null )
    {
      return false;
    }

    return true;
  }

  public int hashCode()
  {
    int result;
    result = purlRecipient != null ? purlRecipient.hashCode() : 0;
    result = 29 * result + ( claimFormStepElement != null ? claimFormStepElement.hashCode() : 0 );
    return result;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toProcessString()
  {
    final StringBuffer buf = new StringBuffer();
    buf.append( this.getClaimFormStepElement().getId().toString() ).append( "," );
    buf.append( this.getValue() );
    return buf.toString();
  }

}
