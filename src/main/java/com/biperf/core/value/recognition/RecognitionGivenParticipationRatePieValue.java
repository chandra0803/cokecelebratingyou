/**
 * 
 */

package com.biperf.core.value.recognition;

/**
 * @author poddutur
 *
 */
public class RecognitionGivenParticipationRatePieValue
{

  private double haveGivenPct;
  private double haveNotGivenPct;

  public RecognitionGivenParticipationRatePieValue()
  {
    super();
  }

  public RecognitionGivenParticipationRatePieValue( double haveGivenPct, double haveNotGivenPct )
  {
    super();
    this.haveGivenPct = haveGivenPct;
    this.haveNotGivenPct = haveNotGivenPct;
  }

  public double getHaveGivenPct()
  {
    return haveGivenPct;
  }

  public void setHaveGivenPct( double haveGivenPct )
  {
    this.haveGivenPct = haveGivenPct;
  }

  public double getHaveNotGivenPct()
  {
    return haveNotGivenPct;
  }

  public void setHaveNotGivenPct( double haveNotGivenPct )
  {
    this.haveNotGivenPct = haveNotGivenPct;
  }

}
