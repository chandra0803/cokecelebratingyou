
package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Audience;

@SuppressWarnings( "serial" )
public class DivisionCompetitorsAudience extends BaseDomain implements Cloneable
{
  private Division division;
  private Audience audience;

  public DivisionCompetitorsAudience()
  {
    super();
  }

  public Audience getAudience()
  {
    return this.audience;
  }

  public void setAudience( Audience audience )
  {
    this.audience = audience;
  }

  public Division getDivision()
  {
    return this.division;
  }

  public void setDivision( Division division )
  {
    this.division = division;
  }

  public boolean equals( Object obj )
  {
    DivisionCompetitorsAudience divAudience = (DivisionCompetitorsAudience)obj;

    boolean equals = false;

    equals = division.equals( divAudience.getDivision() );

    if ( equals )
    {
      equals = audience.equals( divAudience.getAudience() );
    }

    return equals;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#hashCode()
   * @return int the hashCode
   */
  public int hashCode()
  {

    int hashCode = 0;

    hashCode += getDivision() != null ? getDivision().hashCode() : 13;
    hashCode += getAudience() != null ? getAudience().hashCode() : 17;

    return hashCode;
  }

  /**
   * Builds a String representation of this. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {

    StringBuilder sb = new StringBuilder();
    sb.append( "DivisionCompetitorsAudience [" );
    sb.append( "{id=" + super.getId() + "}, " );
    sb.append( "{division.id =" + this.getDivision().getId() + "}, " );
    sb.append( "{audience.id=" + this.getAudience().getId() + "}" );
    sb.append( "]" );

    return sb.toString();
  }

  /**
   * Clones this, removes the auditInfo and id. Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {

    DivisionCompetitorsAudience cloneDivisionCompetitors = (DivisionCompetitorsAudience)super.clone();
    cloneDivisionCompetitors.resetBaseDomain();

    return cloneDivisionCompetitors;

  }
}
