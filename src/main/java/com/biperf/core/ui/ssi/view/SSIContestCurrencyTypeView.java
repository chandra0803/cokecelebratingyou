
package com.biperf.core.ui.ssi.view;

/**
 * 
 * SSIContestCurrencyTypeView.
 * 
 * @author kandhi
 * @since Dec 1, 2014
 * @version 1.0
 */
public class SSIContestCurrencyTypeView
{
  private String id;
  private String name;
  private String symbol;

  public SSIContestCurrencyTypeView( String id, String name, String symbol )
  {
    super();
    this.id = id;
    this.name = name;
    this.symbol = symbol;
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getSymbol()
  {
    return symbol;
  }

  public void setSymbol( String symbol )
  {
    this.symbol = symbol;
  }

}
