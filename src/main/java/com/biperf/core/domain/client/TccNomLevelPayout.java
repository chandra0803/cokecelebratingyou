  // Client customization for WIP 58122
package com.biperf.core.domain.client;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;

/**
 * TODO Javadoc for TccNomLevelPayout.
 * 
 * @author prasad
 * @since Jul 10, 2019
 * @version 1.0
 */
public class TccNomLevelPayout extends BaseDomain
{
  
  private static final long serialVersionUID = 1L;
  private Promotion promotion;
  private String levelId;
  private String levelDescription;
  private Long totalPoints;
  

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public String getLevelDescription()
  {
    return levelDescription;
  }

  public void setLevelDescription( String levelDescription )
  {
    this.levelDescription = levelDescription;
  }

  public Long getTotalPoints()
  {
    return totalPoints;
  }

  public void setTotalPoints( Long totalPoints )
  {
    this.totalPoints = totalPoints;
  }

  @Override
  public int hashCode()
  {
    return ( this.getId() != null ? this.getId().hashCode() : 0 );
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof TccNomLevelPayout ) )
    {
      return false;
    }

    TccNomLevelPayout tccNomLevelPayout = (TccNomLevelPayout)object;

    if ( this.getId() != null && this.getId().equals( tccNomLevelPayout.getId() ) )
    {
      return true;
    }
    return false;
  }

public String getLevelId() {
	return levelId;
}

public void setLevelId(String levelId) {
	this.levelId = levelId;
}

}
