/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/PromoMerchCountry.java,v $
 */

package com.biperf.core.domain.promotion;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.country.Country;

/**
 * PromoMerchCountry.
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
 * <td>Ernste</td>
 * <td>July 19, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromoMerchCountry extends BaseDomain
{
  private Promotion promotion;
  private Country country;
  private String programId;
  private Set levels;

  /**
   * Default Constructor
   */
  public PromoMerchCountry()
  {
    super();
  }

  /**
   * Construct this with a user and a node.
   * 
   * @param user
   * @param node
   */
  public PromoMerchCountry( Promotion promotion, Country country )
  {
    super();
    this.promotion = promotion;
    this.country = country;
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param o
   * @return boolean
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof PromoMerchCountry ) )
    {
      return false;
    }

    final PromoMerchCountry promoMerchCountryProgram = (PromoMerchCountry)o;

    if ( getPromotion() != null ? !getPromotion().equals( promoMerchCountryProgram.getPromotion() ) : promoMerchCountryProgram.getPromotion() != null )
    {
      return false;
    }
    if ( getCountry() != null ? !getCountry().equals( promoMerchCountryProgram.getCountry() ) : promoMerchCountryProgram.getCountry() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = getPromotion() != null ? getPromotion().hashCode() : 0;
    result = 31 * result + ( getCountry() != null ? getCountry().hashCode() : 0 );

    return result;
  }

  /**
   * Deep copy PromoMerchCountry
   * 
    * @return PromoMerchCountry
   */
  public PromoMerchCountry deepCopy()
  {
    PromoMerchCountry clone = new PromoMerchCountry();
    clone.setCountry( this.getCountry() );
    clone.setProgramId( this.getProgramId() );
    clone.setLevels( new TreeSet( new PromoMerchProgramLevelComparator() ) );
    if ( this.getLevels() != null )
    {
      for ( Iterator iter = this.getLevels().iterator(); iter.hasNext(); )
      {
        PromoMerchProgramLevel level = ( (PromoMerchProgramLevel)iter.next() ).deepCopy();
        level.setPromoMerchCountry( clone );
        clone.getLevels().add( level );
      }
    }
    return clone;
  }

  public Country getCountry()
  {
    return country;
  }

  public void setCountry( Country country )
  {
    this.country = country;
  }

  public String getProgramId()
  {
    return programId;
  }

  public void setProgramId( String programId )
  {
    this.programId = programId;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  /**
   * @return the levels
   */
  public Set<PromoMerchProgramLevel> getLevels()
  {
    return levels;
  }

  /**
   * @param levels the levels to set
   */
  public void setLevels( Set levels )
  {
    this.levels = levels;
  }

  public int getNumberOfLevels()
  {
    if ( getLevels() == null )
    {
      return 0;
    }
    return getLevels().size();
  }
}
