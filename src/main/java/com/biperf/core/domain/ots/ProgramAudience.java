
package com.biperf.core.domain.ots;

import java.io.Serializable;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Audience;

public class ProgramAudience extends BaseDomain implements Serializable
{
  private Audience audience;
  private OTSProgram otsProgram;

  private static final long serialVersionUID = 1L;

  public ProgramAudience( Audience audience, OTSProgram otsProgram )
  {
    super();
    setAudience( audience );
    setOtsProgram( otsProgram );
  }

  public Audience getAudience()
  {
    return this.audience;
  }

  /**
   * 
   */
  public ProgramAudience()
  {
    super();
  }

  /**
   * @param audience
   */
  public void setAudience( Audience audience )
  {
    this.audience = audience;
  }

  /**
   * @return promotion
   */

  /**
   * @param promotion
   */
  
  public boolean equals( Object obj )
  {
    ProgramAudience promoAudience = (ProgramAudience)obj;

    boolean equals = false;

    equals = otsProgram.equals( promoAudience.getOtsProgram() );

    if ( equals )
    {
      equals = audience.equals( promoAudience.getAudience() );
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

    hashCode += getOtsProgram() != null ? getOtsProgram().hashCode() : 13;
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

    StringBuffer sb = new StringBuffer();
    sb.append( "ProgramAudience [" );
    sb.append( "{id=" + super.getId() + "}, " );
    sb.append( "{promotion.id =" + this.getOtsProgram().getId() + "}, " );
    sb.append( "{audience.id=" + this.getAudience().getId() + "}" );
    sb.append( "]" );

    return sb.toString();

  }

  public OTSProgram getOtsProgram()
  {
    return otsProgram;
  }

  public void setOtsProgram( OTSProgram otsProgram )
  {
    this.otsProgram = otsProgram;
  }

}
