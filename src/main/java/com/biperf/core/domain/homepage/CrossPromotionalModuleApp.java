
package com.biperf.core.domain.homepage;

@SuppressWarnings( "serial" )
public class CrossPromotionalModuleApp extends ModuleApp
{
  private int order = -1;
  private boolean selected;

  public int getOrder()
  {
    return order;
  }

  public void setOrder( int order )
  {
    this.order = order;
  }

  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected( boolean selected )
  {
    this.selected = selected;
  }

}
