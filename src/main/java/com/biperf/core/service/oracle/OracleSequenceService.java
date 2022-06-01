
package com.biperf.core.service.oracle;

import com.biperf.core.service.SAO;

public interface OracleSequenceService extends SAO
{
  /** BEAN_NAME for referencing in tests and spring config files. */
  public final String BEAN_NAME = "oracleSequenceService";

  /**
   * @param sequenceName
   * @return the next val on the sequence number object on Oracle
   */
  public long getOracleSequenceNextValue( String sequenceName );

}
