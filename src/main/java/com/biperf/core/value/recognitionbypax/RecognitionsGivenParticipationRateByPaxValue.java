/**
 * 
 */

package com.biperf.core.value.recognitionbypax;

/**
 * @author poddutur
 *
 */
public class RecognitionsGivenParticipationRateByPaxValue
{

  private double haveGivenUniquePct;
  private double haveNotGivenUniquePct;

  public RecognitionsGivenParticipationRateByPaxValue()
  {
    super();
  }

  public RecognitionsGivenParticipationRateByPaxValue( double haveGivenUniquePct, double haveNotGivenUniquePct )
  {
    super();
    this.haveGivenUniquePct = haveGivenUniquePct;
    this.haveNotGivenUniquePct = haveNotGivenUniquePct;
  }

  public double getHaveGivenUniquePct()
  {
    return haveGivenUniquePct;
  }

  public void setHaveGivenUniquePct( double haveGivenUniquePct )
  {
    this.haveGivenUniquePct = haveGivenUniquePct;
  }

  public double getHaveNotGivenUniquePct()
  {
    return haveNotGivenUniquePct;
  }

  public void setHaveNotGivenUniquePct( double haveNotGivenUniquePct )
  {
    this.haveNotGivenUniquePct = haveNotGivenUniquePct;
  }

}
