
package com.biperf.core.domain.ssi;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.ClaimFormElementType;

/**
 * 
 * 
 * @author kancherl
 * @since May 22, 2015
 * @version 1.0
 */

public class SSIContestPaxClaimField extends BaseDomain
{
  private static final long serialVersionUID = 1L;

  private SSIContestPaxClaim paxClaim;
  private SSIContestClaimField claimField;
  private ClaimFormElementType fieldType;
  private String fieldValue;

  public SSIContestPaxClaim getPaxClaim()
  {
    return paxClaim;
  }

  public void setPaxClaim( SSIContestPaxClaim paxClaim )
  {
    this.paxClaim = paxClaim;
  }

  public SSIContestClaimField getClaimField()
  {
    return claimField;
  }

  public void setClaimField( SSIContestClaimField claimField )
  {
    this.claimField = claimField;
  }

  public ClaimFormElementType getFieldType()
  {
    return fieldType;
  }

  public void setFieldType( ClaimFormElementType fieldType )
  {
    this.fieldType = fieldType;
  }

  public String getFieldValue()
  {
    return fieldValue;
  }

  public void setFieldValue( String fieldValue )
  {
    this.fieldValue = fieldValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( claimField == null ? 0 : claimField.hashCode() );
    result = prime * result + ( fieldType == null ? 0 : fieldType.hashCode() );
    result = prime * result + ( fieldValue == null ? 0 : fieldValue.hashCode() );
    result = prime * result + ( paxClaim == null ? 0 : paxClaim.hashCode() );
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }
    SSIContestPaxClaimField other = (SSIContestPaxClaimField)obj;
    if ( claimField == null )
    {
      if ( other.claimField != null )
      {
        return false;
      }
    }
    else if ( !claimField.equals( other.claimField ) )
    {
      return false;
    }
    if ( fieldType == null )
    {
      if ( other.fieldType != null )
      {
        return false;
      }
    }
    else if ( !fieldType.equals( other.fieldType ) )
    {
      return false;
    }
    if ( fieldValue == null )
    {
      if ( other.fieldValue != null )
      {
        return false;
      }
    }
    else if ( !fieldValue.equals( other.fieldValue ) )
    {
      return false;
    }
    if ( paxClaim == null )
    {
      if ( other.paxClaim != null )
      {
        return false;
      }
    }
    else if ( !paxClaim.equals( other.paxClaim ) )
    {
      return false;
    }
    return true;
  }

}
