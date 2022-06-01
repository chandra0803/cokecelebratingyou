
package com.biperf.core.service.underarmour.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.biperf.core.service.underarmour.UARepository;
import com.biperf.core.value.underArmour.v1.BaseRestResponseObject;
import com.biperf.core.value.underArmour.v1.actigraphy.ActigraphyRequest;
import com.biperf.core.value.underArmour.v1.actigraphy.response.Actigraphy;
import com.biperf.core.value.underArmour.v1.actigraphy.response.ActigraphyResponse;
import com.biperf.core.value.underArmour.v1.actigraphy.response.Aggregate;
import com.biperf.core.value.underArmour.v1.application.ApplicationRequest;
import com.biperf.core.value.underArmour.v1.participant.ParticipantRequest;
import com.biperf.core.value.underArmour.v1.participant.PaxAuthStatusResponse;

@Service( "UARepoMock" )
public class UARepoMock implements UARepository
{

  public BaseRestResponseObject insertApplication( ApplicationRequest applicationRequest )
  {
    return null;
  }

  public BaseRestResponseObject updateApplication( ApplicationRequest applicationRequest )
  {
    return null;
  }

  public BaseRestResponseObject authorizeParticipant( ParticipantRequest participantRequest )
  {
    return getSuccessResponse();

  }

  public BaseRestResponseObject deAuthorizeParticipant( ParticipantRequest participantRequest )
  {
    return getSuccessResponse();

  }

  private BaseRestResponseObject getSuccessResponse()
  {
    BaseRestResponseObject response = new BaseRestResponseObject( BaseRestResponseObject.SUCCESS, "from mock" );
    return response;
  }

  public PaxAuthStatusResponse isAuthorizeParticipant( ParticipantRequest participantRequest )
  {

    return new PaxAuthStatusResponse( BaseRestResponseObject.SUCCESS, "from mock", false );

  }

  public ActigraphyResponse getActigraphy( ActigraphyRequest actigraphyRequest )
  {

    int min = 100;
    int max = 1000;
    int range = max - min + 1;

    int size = 10;
    List<Aggregate> aggregateList = new ArrayList<>();
    List<Actigraphy> graphyList = new ArrayList<>();

    ActigraphyResponse result = new ActigraphyResponse();
    result.setReturnCode( BaseRestResponseObject.SUCCESS );
    result.setReturnMessage( BaseRestResponseObject.SUCCESS_MESSAGE );

    for ( int i = 0; i < size; i++ )
    {
      Aggregate aggregate = new Aggregate();
      int randNum = new Random().nextInt( range ) + min;
      aggregate.setSteps( (double)randNum );
      aggregateList.add( aggregate );
    }

    Actigraphy graphy = new Actigraphy();
    graphy.setAggregates( new HashSet<Aggregate>( aggregateList ) );
    graphyList.add( graphy );

    result.setActigraphys( graphyList );

    return result;
  }

}
