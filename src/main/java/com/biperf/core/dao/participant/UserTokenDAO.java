
package com.biperf.core.dao.participant;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.user.UserToken;

public interface UserTokenDAO extends DAO
{
  public static final String BEAN_NAME = "userTokenDAO";

  public void saveUserToken( UserToken userToken );

  public boolean validateToken( Long id, String encryptDefault );

  public boolean validateClearTextToken( Long userId, String clearTextToken );

  public UserToken getTokenById( String encryptToken );

  public void purgeUserTokens();
}