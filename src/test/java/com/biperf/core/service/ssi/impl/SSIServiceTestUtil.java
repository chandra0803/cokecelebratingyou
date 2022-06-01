
package com.biperf.core.service.ssi.impl;

import java.util.ArrayList;
import java.util.Date;

import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.SSIActivityMeasureType;
import com.biperf.core.domain.enums.SSIClaimStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.enums.SSIPayoutType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;

public class SSIServiceTestUtil
{

  public static SSIContest buildContest( Long contestId, SSIContestType contestType )
  {
    SSIContest contest = new SSIContest();
    contest.setContestType( contestType );
    SSIPromotion promotion = new SSIPromotion();
    contest.setId( contestId );
    contest.setPromotion( promotion );
    contest.setContestOwnerId( 5582L );
    contest.setActivityDescription( "Activity Description of Persist object" );
    contest.addContestParticipant( addParticipantOne() );
    contest.addContestParticipant( addParticipantTwo() );
    contest.setPayoutType( (SSIPayoutType)MockPickListFactory.getMockPickListItem( SSIPayoutType.class, SSIPayoutType.OTHER_CODE ) );
    contest.setActivityMeasureType( (SSIActivityMeasureType)MockPickListFactory.getMockPickListItem( SSIActivityMeasureType.class, SSIActivityMeasureType.UNIT_CODE ) );

    return contest;
  }

  public static SSIContest buildSSIContest( boolean partcipantRequired )
  {
    SSIContest ssiContest = new SSIContest();
    SSIPromotion promotion = new SSIPromotion();
    ssiContest.setId( 1L );
    ssiContest.setPromotion( promotion );
    ssiContest.setContestOwnerId( 5582L );
    ssiContest.setActivityMeasureType( (SSIActivityMeasureType)MockPickListFactory.getMockPickListItem( SSIActivityMeasureType.class, SSIActivityMeasureType.CURRENCY_CODE ) );
    ssiContest.setActivityMeasureCurrencyCode( "USD" );
    ssiContest.setPayoutType( (SSIPayoutType)MockPickListFactory.getMockPickListItem( SSIPayoutType.class, SSIPayoutType.POINTS_CODE ) );
    ssiContest.setPayoutOtherCurrencyCode( "USD" );
    ssiContest.setIncludeBonus( false );
    ssiContest.setStackRankOrder( "HightoLow" );
    ssiContest.setIncludeStackRank( true );
    ssiContest.setContestType( SSIContestType.lookup( SSIContestType.AWARD_THEM_NOW ) );
    if ( partcipantRequired )
    {
      ssiContest.addContestParticipant( addParticipantOne() );
      ssiContest.addContestParticipant( addParticipantTwo() );
    }
    return ssiContest;
  }

  public static SSIContestParticipant addParticipantOne()

  {
    SSIContestParticipant contestParticipants = new SSIContestParticipant();
    com.biperf.core.domain.participant.Participant participantOne = new Participant();
    participantOne.setId( 1L );
    participantOne.setFirstName( "firstName One" );
    participantOne.setLastName( "LastName One" );
    contestParticipants.setId( 1L );
    contestParticipants.setParticipant( participantOne );
    contestParticipants.setActivityDescription( "Activity in progress" );
    contestParticipants.setObjectiveAmount( 1000d );
    contestParticipants.setObjectiveBonusCap( 5000L );
    contestParticipants.setObjectiveBonusIncrement( 3000d );
    contestParticipants.setObjectiveBonusPayout( 7000L );
    contestParticipants.setObjectivePayoutDescription( "Increment payout" );
    return contestParticipants;

  }

  public static SSIContestParticipant addParticipantTwo()
  {
    SSIContestParticipant contestParticipants2 = new SSIContestParticipant();
    com.biperf.core.domain.participant.Participant participantTwo = new Participant();
    participantTwo.setId( 2L );
    participantTwo.setFirstName( "firstName Two" );
    participantTwo.setLastName( "LastName Two" );
    contestParticipants2.setId( 1L );
    contestParticipants2.setParticipant( participantTwo );
    contestParticipants2.setActivityDescription( "Activity in progress 2" );
    contestParticipants2.setObjectiveAmount( 1200d );
    contestParticipants2.setObjectiveBonusCap( 5200L );
    contestParticipants2.setObjectiveBonusIncrement( 3200d );
    contestParticipants2.setObjectiveBonusPayout( 7200L );
    contestParticipants2.setObjectivePayoutDescription( "Increment payout 2" );
    return contestParticipants2;
  }

  public static SSIContestParticipantValueBean addParticipantValueBeanOne()
  {
    SSIContestParticipantValueBean contestParticipants = new SSIContestParticipantValueBean();
    com.biperf.core.domain.participant.Participant participantOne = new Participant();
    participantOne.setId( 1L );
    participantOne.setFirstName( "firstName One" );
    participantOne.setLastName( "LastName One" );
    contestParticipants.setId( 1L );
    contestParticipants.setActivityDescription( "Activity in progress" );
    contestParticipants.setObjectiveAmount( "1000" );
    contestParticipants.setBonusForEvery( "3000" );
    contestParticipants.setBonusPayoutCap( "5000" );
    contestParticipants.setBonusPayoutCap( "7000" );
    contestParticipants.setObjectivePayoutDescription( "Increment payout" );
    return contestParticipants;

  }

  public static SSIContestPaxClaim addPaxClaimOne()
  {
    SSIContestPaxClaim contestClaimOne = new SSIContestPaxClaim();
    contestClaimOne.setStatus( (SSIClaimStatus)MockPickListFactory.getMockPickListItem( SSIClaimStatus.class, SSIClaimStatus.WAITING_FOR_APPROVAL ) );
    contestClaimOne.setClaimNumber( "1234567898888" );
    contestClaimOne.setContestId( new Long( 6356 ) );
    contestClaimOne.setActivities( new ArrayList<String>() );
    contestClaimOne.setApproverId( new Long( 6418 ) );
    contestClaimOne.setApprovedBy( "5662" );
    contestClaimOne.setApproveDenyDate( new Date() );
    return contestClaimOne;

  }

  public static SSIContestPaxClaim addPaxClaimTwo()
  {
    SSIContestPaxClaim contestClaimTwo = new SSIContestPaxClaim();
    contestClaimTwo.setStatus( (SSIClaimStatus)MockPickListFactory.getMockPickListItem( SSIClaimStatus.class, SSIClaimStatus.WAITING_FOR_APPROVAL ) );
    contestClaimTwo.setClaimNumber( "7878787878787" );
    contestClaimTwo.setContestId( new Long( 6367 ) );
    contestClaimTwo.setActivities( new ArrayList<String>() );
    contestClaimTwo.setApproverId( new Long( 6418 ) );
    contestClaimTwo.setApprovedBy( "5662" );
    contestClaimTwo.setApproveDenyDate( new Date() );
    return contestClaimTwo;

  }
  
  public static SSIContestLevel buildContestLevel()
  {
    SSIContestLevel contestLevel = new SSIContestLevel();
    return contestLevel;
  }

}
