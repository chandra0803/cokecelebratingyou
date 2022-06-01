
package com.biperf.core.domain.client;

import java.util.Date;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;

/**
 * TcccApproverMatrix.
 * 
 * @author dudam
 * @since Feb 19, 2018
 * @version 1.0
 * This domain class is not being used, Just left it for backup
 * 
 * This domain class is created as part of WIP #42701
 */
public class TcccApproverMatrix extends BaseDomain
{
  private static final long serialVersionUID = 1L;
  private Promotion promotion;
  private String divisionNumber;
  private String approverUsername;
  private int approvalLevel;
  private boolean processing;
  private Date dateEnd;

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public String getDivisionNumber()
  {
    return divisionNumber;
  }

  public void setDivisionNumber( String divisionNumber )
  {
    this.divisionNumber = divisionNumber;
  }

  public String getApproverUsername()
  {
    return approverUsername;
  }

  public void setApproverUsername( String approverUsername )
  {
    this.approverUsername = approverUsername;
  }

  public int getApprovalLevel()
  {
    return approvalLevel;
  }

  public void setApprovalLevel( int approvalLevel )
  {
    this.approvalLevel = approvalLevel;
  }

  public boolean isProcessing()
  {
    return processing;
  }

  public void setProcessing( boolean processing )
  {
    this.processing = processing;
  }

  public Date getDateEnd()
  {
    return dateEnd;
  }

  public void setDateEnd( Date dateEnd )
  {
    this.dateEnd = dateEnd;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + approvalLevel;
    result = prime * result + ( ( approverUsername == null ) ? 0 : approverUsername.hashCode() );
    result = prime * result + ( ( dateEnd == null ) ? 0 : dateEnd.hashCode() );
    result = prime * result + ( ( divisionNumber == null ) ? 0 : divisionNumber.hashCode() );
    result = prime * result + ( processing ? 1231 : 1237 );
    result = prime * result + ( ( promotion == null ) ? 0 : promotion.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
      return true;
    if ( obj == null )
      return false;
    if ( getClass() != obj.getClass() )
      return false;
    TcccApproverMatrix other = (TcccApproverMatrix)obj;
    if ( approvalLevel != other.approvalLevel )
      return false;
    if ( approverUsername == null )
    {
      if ( other.approverUsername != null )
        return false;
    }
    else if ( !approverUsername.equals( other.approverUsername ) )
      return false;
    if ( dateEnd == null )
    {
      if ( other.dateEnd != null )
        return false;
    }
    else if ( !dateEnd.equals( other.dateEnd ) )
      return false;
    if ( divisionNumber == null )
    {
      if ( other.divisionNumber != null )
        return false;
    }
    else if ( !divisionNumber.equals( other.divisionNumber ) )
      return false;
    if ( processing != other.processing )
      return false;
    if ( promotion == null )
    {
      if ( other.promotion != null )
        return false;
    }
    else if ( !promotion.equals( other.promotion ) )
      return false;
    return true;
  }

}
