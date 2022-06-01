
package com.biperf.core.ui.homepage;

import java.io.Serializable;

@SuppressWarnings( "serial" )
public class ShopAndTravelView implements Serializable
{
  private String internalSupplierProgramId = null;
  private boolean isBiiSupplier = true;
  private boolean showTravel = false;
  private boolean showShop = false;
  private boolean isBiiExperience = true;

  public String getInternalSupplierProgramId()
  {
    return internalSupplierProgramId;
  }

  public void setInternalSupplierProgramId( String internalSupplierProgramId )
  {
    this.internalSupplierProgramId = internalSupplierProgramId;
  }

  public boolean isBiiSupplier()
  {
    return isBiiSupplier;
  }

  public void setBiiSupplier( boolean isBiiSupplier )
  {
    this.isBiiSupplier = isBiiSupplier;
  }

  public boolean isShowTravel()
  {
    return showTravel;
  }

  public void setShowTravel( boolean showTravel )
  {
    this.showTravel = showTravel;
  }

  public boolean isShowShop()
  {
    return showShop;
  }

  public void setShowShop( boolean showShop )
  {
    this.showShop = showShop;
  }
  
  public boolean isBiiExperience()
  {
    return isBiiExperience;
  }

  public void setBiiExperience( boolean isBiiExperience )
  {
    this.isBiiExperience = isBiiExperience;
  }


}
