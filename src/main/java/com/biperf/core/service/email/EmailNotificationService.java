/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/email/EmailNotificationService.java,v $
 */

package com.biperf.core.service.email;

import java.util.List;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.service.SAO;

/**
 * EmailNotificationServiceImpl
 * 
 *
 */
public interface EmailNotificationService extends SAO
{
  /** * BEAN_NAME is the reference for spring ** */
  public static final String BEAN_NAME = "emailNotificationService";

  /**
   * Process submitted claim notifications given claimId
   * 
   * @param claim
   * @param claimFormStepId
   * @param unnotifiedApprovers TODO
   */
  public void processSubmittedClaimNotifications( Claim claim, Long claimFormStepId, List unnotifiedApprovers, boolean forApprovers );

  /**
   * Process closed claim (claims w/ all claim products that have status notifications given claimId
   * 
   * @param claim
   */
  public void processClosedClaimNotifications( Claim claim );

  public void processNominationApproverNotification( Claim claim, List unnotifiedApprovers );

}
