
package com.biperf.core.value.nomination;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.biperf.core.domain.enums.CustomApproverType;

public class NominationApproverValueBean
{

  private String username;

  private String lastname;

  private String firstname;

  private String approverType;

  private String approverValue;

  /**
   * No args constructor for use in serialization
   * 
   */
  public NominationApproverValueBean()
  {
  }

  /**
   * 
   * @param approverType
   * @param username
   * @param lastname
   * @param approverValue
   * @param firstname
   */
  public NominationApproverValueBean( String username, String lastname, String firstname, String approverType, String approverValue )
  {
    this.username = username;
    this.lastname = lastname;
    this.firstname = firstname;
    this.approverType = approverType;
    this.approverValue = approverValue;
  }

  /**
   * 
   * @return
   *     The username
   */

  public String getUsername()
  {
    return username;
  }

  /**
   * 
   * @param username
   *     The username
   */

  public void setUsername( String username )
  {
    this.username = username;
  }

  /**
   * 
   * @return
   *     The lastname
   */

  public String getLastname()
  {
    return lastname;
  }

  /**
   * 
   * @param lastname
   *     The lastname
   */

  public void setLastname( String lastname )
  {
    this.lastname = lastname;
  }

  /**
   * 
   * @return
   *     The firstname
   */

  public String getFirstname()
  {
    return firstname;
  }

  /**
   * 
   * @param firstname
   *     The firstname
   */

  public void setFirstname( String firstname )
  {
    this.firstname = firstname;
  }

  /**
   * 
   * @return
   *     The approverType
   */

  public String getApproverType()
  {
    return approverType;
  }

  /**
   * 
   * @param approverType
   *     The approverType
   */

  public void setApproverType( String approverType )
  {
    this.approverType = approverType;
  }

  /**
   * 
   * @return
   *     The approverValue
   */

  public String getApproverValue()
  {
    return approverValue;
  }

  /**
   * 
   * @param approverValue
   *     The approverValue
   */

  public void setApproverValue( String approverValue )
  {
    this.approverValue = approverValue;
  }

  @Override
  public String toString()
  {
    return ToStringBuilder.reflectionToString( this );
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append( username ).append( lastname ).append( firstname ).append( approverType ).append( approverValue ).toHashCode();
  }

  @Override
  public boolean equals( Object other )
  {
    if ( other == this )
    {
      return true;
    }
    if ( ! ( other instanceof NominationApproverValueBean ) )
    {
      return false;
    }
    NominationApproverValueBean rhs = (NominationApproverValueBean)other;
    return new EqualsBuilder().append( username, rhs.username ).append( lastname, rhs.lastname ).append( firstname, rhs.firstname ).append( approverType, rhs.approverType )
        .append( approverValue, rhs.approverValue ).isEquals();
  }

  public boolean behaviourType()
  {
    return CustomApproverType.BEHAVIOR.equalsIgnoreCase( this.approverType );
  }

}
