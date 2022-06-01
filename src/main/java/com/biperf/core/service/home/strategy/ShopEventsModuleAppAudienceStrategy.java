//
// package com.biperf.core.service.home.strategy;
//
// import java.util.List;
// import java.util.Map;
//
// import com.biperf.core.domain.homepage.FilterAppSetup;
// import com.biperf.core.domain.participant.Participant;
// import com.biperf.services.rest.rewardoffering.domain.RewardOffering;
//
// public class ShopEventsModuleAppAudienceStrategy extends AbstractModuleAppAudienceStrategy
// implements ModuleAppAudienceStrategy
// {
// @Override
// public boolean isUserInAudience( Participant participant, FilterAppSetup filter, Map<String,
// Object> parameterMap )
// {
// if ( participant.isParticipant() )
// {
// String programId = getUserService().getCountryProgramIdByUserId( participant.getId() );
// if ( programId != null )
// {
// List<RewardOffering> rewardsList = getRewardOfferingsService().getRewardOfferings( programId );
// if ( rewardsList != null )
// {
// for ( RewardOffering rewards : rewardsList )
// {
// if ( "etc".equalsIgnoreCase( rewards.getType() ) )
// {
// return true;
// }
// }
// }
// }
// }
// return false;
// }
// }
