
package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;

public class PromotionWizard extends BaseDomain implements Cloneable
{

  Promotion promotion;
  private String wizardOrderName;
  private String wizardOrder;

  public PromotionWizard()
  {
    super();
  }

  public PromotionWizard( String wizardOrderName, String wizardOrder )
  {
    this.wizardOrderName = wizardOrderName;
    this.wizardOrder = wizardOrder;
  }

  public PromotionWizard deepCopy()
  {
    PromotionWizard clone = new PromotionWizard();

    clone.setWizardOrderName( this.getWizardOrderName() );
    clone.setWizardOrder( this.getWizardOrder() );

    return clone;
  }

  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof PromotionWizard ) )
    {
      return false;
    }

    final PromotionWizard other = (PromotionWizard)object;

    if ( getId() == null )
    {
      if ( other.getId() != null )
      {
        return false;
      }
    }
    else if ( !getId().equals( other.getId() ) )
    {
      return false;
    }

    if ( getWizardOrderName() != null )
    {
      if ( !getWizardOrderName().equals( other.getWizardOrderName() ) )
      {
        return false;
      }
    }

    return true;
  }

  public int hashCode()
  {
    final int prime = 31;
    int result = 0;
    result = prime * result + ( this.getId() != null ? this.getId().hashCode() : 0 );
    result = prime * result + ( this.getWizardOrderName() != null ? this.getWizardOrderName().hashCode() : 0 );
    return result;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public String getWizardOrderName()
  {
    return wizardOrderName;
  }

  public void setWizardOrderName( String wizardOrderName )
  {
    this.wizardOrderName = wizardOrderName;
  }

  public String getWizardOrder()
  {
    return wizardOrder;
  }

  public void setWizardOrder( String wizardOrder )
  {
    this.wizardOrder = wizardOrder;
  }
}
