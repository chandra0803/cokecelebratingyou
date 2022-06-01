/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/strategy/PasswordPolicyStrategy.java,v $
 */

package com.biperf.core.strategy;

import java.util.List;

import com.biperf.core.domain.user.User;
import com.biperf.core.service.util.ServiceError;

/**
 * This class will contain methods relating to management of passwords.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>Apr 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface PasswordPolicyStrategy extends Strategy
{
  /**
   * name referenced in Spring applicationConfig.xml
   */
  public static String BEAN_NAME = "passwordPolicyStrategy";

  /**
   * @param oldPasswordEncrypted TODO
   * @param newPassword
   * @return true is acceptable
   */
  public List getPasswordValidationErrors( String oldPasswordEncrypted, String newPassword );

  /**
   * Strategy to make decision for password expiration.
   * 
   * @param user
   * @return boolean
   */
  public boolean isPasswordExpired( User user );

  /**
   * @return a random password based on the current default implementation of PasswordGenerator
   */
  public String generatePassword();

  /**
   * @param pattern
   * @return a random password based on a user specified pattern.
   */
  public String generatePassword( char[] pattern );

  /**
   * @param oldPasswordEncrypted
   * @param oldPasswordProvided
   * @return Service error
   */
  public List<ServiceError> isValidOldPassword( String oldPasswordEncrypted, String oldPasswordProvided );

  public PasswordRequirements getPasswordRequirements();
  
  public PasswordRequirements getCharacterTypeAvailability();
  
  public String buildCharacterTypesAvailableList( PasswordRequirements characterTypeAvailability );
}
