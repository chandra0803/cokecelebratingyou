
package com.biperf.core.domain.promotion;

import java.io.Serializable;

public class RegistrationCode implements Serializable

{
  /**
   * This is the audience name setup on the promotion audience 
   * for self-enrolling (secondary) audience. This identifies
   * the promotion and the audience.
   */
  private String enrollProgramCode;

  public static final String DELIMITER = "-";

  /**
   * This is the location code setup on the node characteristic
   * to identify an unique node the participant should be assigned to.
   * This identifies the node.
   * 
   */
  private String locationCode;

  public String getEnrollProgramCode()
  {
    return enrollProgramCode;
  }

  public void setEnrollProgramCode( String enrollProgramCode )
  {
    this.enrollProgramCode = enrollProgramCode;
  }

  public String getLocationCode()
  {
    return locationCode;
  }

  public void setLocationCode( String locationCode )
  {
    this.locationCode = locationCode;
  }

  /**
   * A self-enrolling participant would enter this format
   * as the registration code.
   * 
   * Overridden from @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    return enrollProgramCode + DELIMITER + locationCode;
  }

}
