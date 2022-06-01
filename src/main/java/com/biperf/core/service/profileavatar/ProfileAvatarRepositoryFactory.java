
package com.biperf.core.service.profileavatar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ProfileAvatarRepositoryFactory
{
  @Autowired
  @Qualifier( "ProfileAvatarRepositoryImpl" )
  private ProfileAvatarRepository original;

  public ProfileAvatarRepository getRepo()
  {
    return original;
  }

}
