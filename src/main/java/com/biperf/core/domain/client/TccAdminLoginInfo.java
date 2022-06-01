package com.biperf.core.domain.client;

import java.sql.Timestamp;

import com.biperf.core.domain.BaseDomain;

/**
 * 
 * TccAdminLoginInfo.
 * 
 * @author bethke
 * @since Feb 4, 2015
 * @version 1.0
 */
public class TccAdminLoginInfo extends BaseDomain{

    
    private static final long serialVersionUID = 1L;

    private Long adminUserId;
    private Long paxUserId;
    private Timestamp launchAsDateAndTime;  
    
    
    /**
     * Ensure equality between this and the object param. Overridden from
     * 
     * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
     * @param object
     * @return boolean
     */
    public boolean equals( Object object )
    {
      if ( this == object )
      {
        return true;
      }

      if ( ! ( object instanceof TccAdminLoginInfo ) )
      {
        return false;
      }

      TccAdminLoginInfo cokeAdminLoginInfo = (TccAdminLoginInfo)object;

      if ( this.getId() != null && this.getId().equals( cokeAdminLoginInfo.getId() ) )
      {
        return true;
      }
      return false;
    }

    /**
     * Define the hashCode from the id. Overridden from
     * 
     * @see com.biperf.core.domain.BaseDomain#hashCode()
     * @return int
     */
    public int hashCode()
    {
      return ( this.getId() != null ? this.getId().hashCode() : 0 );
    }

    /**
     * Builds a String representation of this class. Overridden from
     * 
     * @see java.lang.Object#toString()
     * @return String
     */

    public String toString()
    {
      final StringBuffer buf = new StringBuffer();
      buf.append( "CokeAdminLoginInfo [" );
      buf.append( "{id=" ).append( super.getId() ).append( "}, " );
      buf.append( "]" );
      return buf.toString();
    }
    
     
    public Long getAdminUserId()
    {
      return adminUserId;
    }

    public void setAdminUserId( Long adminUserId )
    {
      this.adminUserId = adminUserId;
    }

    public Long getPaxUserId()
    {
      return paxUserId;
    }

    public void setPaxUserId( Long paxUserId )
    {
      this.paxUserId = paxUserId;
    }

    public Timestamp getLaunchAsDateAndTime()
    {
      return launchAsDateAndTime;
    }

    public void setLaunchAsDateAndTime( Timestamp launchAsDateAndTime )
    {
      this.launchAsDateAndTime = launchAsDateAndTime;
    }

   

}
