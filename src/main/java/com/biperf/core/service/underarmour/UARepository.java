
package com.biperf.core.service.underarmour;

import com.biperf.core.value.underArmour.v1.BaseRestResponseObject;
import com.biperf.core.value.underArmour.v1.actigraphy.ActigraphyRequest;
import com.biperf.core.value.underArmour.v1.actigraphy.response.ActigraphyResponse;
import com.biperf.core.value.underArmour.v1.application.ApplicationRequest;
import com.biperf.core.value.underArmour.v1.participant.ParticipantRequest;
import com.biperf.core.value.underArmour.v1.participant.PaxAuthStatusResponse;

public interface UARepository
{

  public BaseRestResponseObject insertApplication( ApplicationRequest applicationRequest );

  public BaseRestResponseObject updateApplication( ApplicationRequest applicationRequest );

  public BaseRestResponseObject authorizeParticipant( ParticipantRequest participantRequest );

  public BaseRestResponseObject deAuthorizeParticipant( ParticipantRequest participantRequest );

  public PaxAuthStatusResponse isAuthorizeParticipant( ParticipantRequest participantRequest );

  public ActigraphyResponse getActigraphy( ActigraphyRequest actigraphyRequest );

}
