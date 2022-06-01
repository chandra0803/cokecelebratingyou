
package com.biperf.core.value.underArmour.v1.actigraphy.response;

import java.util.List;

import com.biperf.core.value.underArmour.v1.BaseRestResponseObject;

public class ActigraphyResponse extends BaseRestResponseObject
{
  private List<Actigraphy> actigraphys;

  public ActigraphyResponse()
  {
    // default constructor : do nothing. will be required during unmarshal
  }

  public ActigraphyResponse( int returnCode, String returnMessage, List<Actigraphy> actigraphys )
  {
    super( returnCode, returnMessage );
    this.actigraphys = actigraphys;
  }

  public List<Actigraphy> getActigraphys()
  {
    return actigraphys;
  }

  public void setActigraphys( List<Actigraphy> actigraphys )
  {
    this.actigraphys = actigraphys;
  }

}
