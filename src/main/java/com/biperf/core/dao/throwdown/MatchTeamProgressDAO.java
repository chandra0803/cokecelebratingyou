
package com.biperf.core.dao.throwdown;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.MatchTeamProgress;

public interface MatchTeamProgressDAO extends DAO
{
  /** name of bean in factory * */
  public static final String BEAN_NAME = "matchTeamProgressDAO";

  public MatchTeamProgress save( MatchTeamProgress matchTeamProgress );

  public List<MatchTeamProgress> getProgressListByOutcome( Long id );

}
