/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/claim/ClaimElement.java,v $
 */

package com.biperf.core.domain.claim;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.crypto.Base64;
import com.biperf.core.utils.crypto.ByteArrayGuard;
import com.biperf.core.utils.crypto.InvalidMacException;

/**
 * ClaimElement
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
 * <td>tennant</td>
 * <td>Jun 30, 2005</td>
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
public class ClaimElement extends BaseDomain implements Serializable
{
  private static final Log logger = LogFactory.getLog( ClaimElement.class );

  private Claim claim;
  private ClaimFormStepElement claimFormStepElement;
  private String value;
  private String valueName;
  private String valueDecrypted;

  private String claimFormAssetCode;

  private static final String CLAIM_FORM_ENCRYPT_PASSWORD = "password";

  /**
   * The pickListItems are used only for display in the UI. If this ClaimFormStepElement is of type
   * Select or Multi-Select, then the ClaimFormStepElement has a selectionPickListName. For a single
   * select type, the pickListItems will be a list of a sinlge PickListItem. For multi-selects,
   * pickListItems will be a list of all user selected values. It will probably be the controllers
   * job to set the pickListItem or pickListItems, single or multi select respectively.
   */
  private List pickListItems;

  public Claim getClaim()
  {
    return claim;
  }

  public void setClaim( Claim claim )
  {
    this.claim = claim;
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
          logger.info( "Cannot decrypt ClaimElement value for ClaimElementId=" + getId() );
          valueDecrypted = value;
        }
      }
      return valueDecrypted;
    }
    return value;
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

  public String getValueName()
  {
    List pickListCodesList = ArrayUtil.convertDelimitedStringToList( this.value, "," );
    if ( pickListCodesList.size() > 0 )
    {
      Iterator pickListCodes = pickListCodesList.iterator();
      String[] tempNames = new String[pickListCodesList.size()];
      int i = 0;
      while ( pickListCodes.hasNext() )
      {
        String code = (String)pickListCodes.next();
        tempNames[i] = DynaPickListType.lookup( this.claimFormStepElement.getSelectionPickListName(), code ).getName();
        i++;
      }
      this.valueName = ArrayUtil.convertStringArrayToDelimited( tempNames, ", " );
    }
    return valueName;
  }

  public void setValueName( String valueName )
  {
    this.valueName = valueName;
  }

  public List getPickListItems()
  {
    return pickListItems;
  }

  public void setPickListItems( List pickListItems )
  {
    this.pickListItems = pickListItems;
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

    final ClaimElement element1 = (ClaimElement)o;

    if ( claim != null ? !claim.equals( element1.claim ) : element1.claim != null )
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
    result = claim != null ? claim.hashCode() : 0;
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

  public String getClaimFormAssetCode()
  {
    return claimFormAssetCode;
  }

  public void setClaimFormAssetCode( String claimFormAssetCode )
  {
    this.claimFormAssetCode = claimFormAssetCode;
  }

}
