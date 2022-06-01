
package com.biperf.core.service.ots;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.ots.OTSProgram;
import com.biperf.core.service.BaseAssociationRequest;

public class OTSProgramAssociationRequest extends BaseAssociationRequest
{
  private static final Log log = LogFactory.getLog( OTSProgramAssociationRequest.class );

  protected int hydrateLevel = 0;

  public static final int PRIMARY_AUDIENCES = 101;

  public OTSProgramAssociationRequest( int hydrateLevel )
  {
    super();
    this.hydrateLevel = hydrateLevel;
  }

  public boolean equals( Object object )
  {
    return true;
  }

  public void execute( Object domainObject )
  {
    OTSProgram program = (OTSProgram)domainObject;
    program = BaseAssociationRequest.initializeAndUnproxy( program );
    switch ( hydrateLevel )
    {
      case PRIMARY_AUDIENCES:
        hydrateProgramAudiences( program );
        break;
      default:
        break;
    }
  }

  private void hydrateProgramAudiences( OTSProgram program )
  {
    initialize( program.getProgramAudience() );
  }

}
