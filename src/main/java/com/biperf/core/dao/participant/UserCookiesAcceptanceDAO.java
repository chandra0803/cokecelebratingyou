
package com.biperf.core.dao.participant;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.user.UserCookiesAcceptance;

public interface UserCookiesAcceptanceDAO extends DAO
{

  UserCookiesAcceptance getUserCookiesAcceptanceDetailsByPaxID( Long userId );

  void saveUserCookiesAcceptanceDetails( UserCookiesAcceptance userCookiesAcceptance );
}
