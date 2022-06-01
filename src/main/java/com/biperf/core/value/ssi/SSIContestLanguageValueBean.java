
package com.biperf.core.value.ssi;

/**
 * 
 * SSIContestLanguageView.
 * 
 * @author kandhi
 * @since Nov 10, 2014
 * @version 1.0
 */
public class SSIContestLanguageValueBean
{
  private String id;
  private String name;

  public SSIContestLanguageValueBean()
  {
    super();
  }

  public SSIContestLanguageValueBean( String id, String name )
  {
    super();
    this.id = id;
    this.name = name;
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

}
