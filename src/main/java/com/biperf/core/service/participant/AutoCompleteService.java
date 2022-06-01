
package com.biperf.core.service.participant;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.scheduling.annotation.Async;

import com.biperf.core.service.SAO;
import com.biperf.core.service.participant.impl.SearchFilterTypeCountValue;
import com.biperf.core.value.CountryValueBean;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.indexing.IndexSearchResult;
import com.biperf.core.value.participant.PaxCard;
import com.biperf.core.value.participant.PaxCard.PromotionBean;
import com.biperf.core.value.participant.PaxIndexData;
import com.biperf.core.value.participant.PaxIndexSearchCriteria;

/**
 *  AutoCompleteService  interface
 */
public interface AutoCompleteService extends SAO
{
  public static final String BEAN_NAME = "autoCompleteService";

  public IndexSearchResult<PaxIndexData> search( PaxIndexSearchCriteria criteria ) throws Exception;

  public Long indexAllActiveParticipants() throws Exception;

  @Async
  public Long indexParticipants( List<Long> paxIds );

  public List<PaxCard> getPaxCardsWithFrontViewDetails( List<PaxIndexData> paxIndexData, List<Long> paxFollowersList );

  public List<PaxCard> removePromotionFromCardThatNotEligibleToRecive( List<PaxCard> paxCards, Long userId );

  public PaxCard buildCardWithFrontViewDetails( Map<Long, String> nodeDescription, PaxIndexData indexData, List<Long> paxIdsWhoEnabledPublicProfile, List<Map<Long, CountryValueBean>> countryResults );

  // public Optional<PaxIndexData> findSearcherIndexData( List<PaxIndexData> indexData ) throws
  // Exception;

  public Optional<PaxIndexData> getIndexDataForUser( Long userId ) throws Exception;

  public List<PaxCard> populateEligiblePromotionBean( List<PaxCard> cards, List<PaxIndexData> indexData, List<PromotionMenuBean> eligiblePromotions );

  public PromotionBean getPromotionBean( PaxIndexData receiverIndexData, PromotionMenuBean menuBean );

  public List<PaxCard> buildPaxCards( List<PaxIndexData> paxIndexList, List<PromotionMenuBean> allEligiblePromotions, List<Long> paxFollowersList ) throws Exception;

  public List<SearchFilterTypeCountValue> getSearchFilterCountByName( Long userId, String startsWith );

  public int getAutoCompleteDelay();

  public List<PaxIndexData> findIndexData( List<String> paxIds ) throws Exception;
}
