
package com.biperf.core.strategy.impl;

import java.util.Random;

import com.biperf.core.service.oracle.OracleSequenceService;
import com.biperf.core.strategy.BaseStrategy;
import com.biperf.core.strategy.RandomNumberStrategy;

/**
 * RandomNumberStrategyImpl
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Tammy Cheng</td>
 * <td>Dec 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RandomNumberStrategyImpl extends BaseStrategy implements RandomNumberStrategy
{
  OracleSequenceService oracleSequenceService;

  /**
   * Randomly generate a claim number by taking a oracle sequence number and for each digit in the
   * sequence number, append a random single digit next to it For example: seq nbr = 1000, insert a
   * random number X into the second, fourth, sixth and eight pos to make 1X0X0X0X0 Overridden from
   * 
   * @see com.biperf.core.strategy.RandomNumberStrategy#getRandomizedClaimNumber()
   * @return an unique claim number
   */
  public String getRandomizedClaimNumber()
  {
    StringBuilder randomizedNumber = new StringBuilder();

    // Get next val from the sequence as a base to start with
    String seqNbr = getClaimNumberAsSequenceNumber().toString();

    // for each digit in the sequence number, append a random single digit next to it
    for ( int i = 0; i < seqNbr.length(); i++ )
    {
      Random generator = new Random( System.currentTimeMillis() + i );
      // Get a random digit between 0 to 9
      int randomDigit = generator.nextInt( 10 );

      // Insert the random digit into position
      randomizedNumber.append( randomDigit );
      randomizedNumber.append( seqNbr.charAt( i ) );

    }

    // Return a randomized unique claim number
    return randomizedNumber.toString();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.strategy.RandomNumberStrategy#getClaimNumberAsSequenceNumber()
   * @return an Oracle sequence number
   */
  public Long getClaimNumberAsSequenceNumber()
  {
    return new Long( oracleSequenceService.getOracleSequenceNextValue( "claim_nbr_sq" ) );
  }

  /**
   * Set activity oracleSequenceService
   * 
   * @param oracleSequenceService
   */
  public void setOracleSequenceService( OracleSequenceService oracleSequenceService )
  {
    this.oracleSequenceService = oracleSequenceService;
  }

}
