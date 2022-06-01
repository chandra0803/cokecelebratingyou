
package com.biperf.core.value.ssi;

/**
 * 
 * SSIContestTranslationsCountValueBean.
 * 
 * @author kandhi
 * @since Nov 17, 2014
 * @version 1.0
 */
public class SSIContestTranslationsCountValueBean
{
  private String key;
  private int count;

  public SSIContestTranslationsCountValueBean()
  {
    super();
  }

  public SSIContestTranslationsCountValueBean( int count, String key )
  {
    super();
    this.key = key;
    this.count = count;
  }

  public String getKey()
  {
    return key;
  }

  public void setKey( String key )
  {
    this.key = key;
  }

  public int getCount()
  {
    return count;
  }

  public void setCount( int count )
  {
    this.count = count;
  }

}
