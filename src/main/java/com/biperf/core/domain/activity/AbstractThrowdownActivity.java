
package com.biperf.core.domain.activity;

@SuppressWarnings( "serial" )
public abstract class AbstractThrowdownActivity extends Activity
{
  private Long awardQuantity;

  public AbstractThrowdownActivity()
  {
  }

  public AbstractThrowdownActivity( String guid )
  {
    super( guid );
  }

  public Long getAwardQuantity()
  {
    return awardQuantity;
  }

  public void setAwardQuantity( Long awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }
}
