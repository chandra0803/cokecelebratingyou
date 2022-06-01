
package com.biperf.core.dao.throwdown;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.promotion.Division;

public interface DivisionDAO extends DAO
{
  /** name of bean in factory * */
  public static final String BEAN_NAME = "divisionDAO";

  public Division save( Division division );

  public void delete( Long divisionId );

  public List<Division> getDivisionsByPromotionId( Long promotionId );

  public Division getDivision( Long id );
}
