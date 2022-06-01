
package com.biw.hc.core.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.SAO;
import com.biperf.core.value.hc.GoalquestDetailsResponse;
import com.biperf.core.value.hc.GoalquestSSOResponse;
import com.biperf.core.value.hc.ParticipantSyncResponse;

public interface HCServices extends SAO
{
  /** name referenced in Spring applicationConfig.xml */
  public static final String BEAN_NAME = "hcServices";

  /** Filename for secure honeycomb data file */
  public static final String SERVICE_FILENAME_SALT = "gsaltproperties.yaml";
  
  public static final String HONEYCOMB_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

  public static final String PROPERTY_KEY_SALT = "salt";
  public static final String PROPERTY_KEY_CLIENT_CODE = "clientCode";
  public static final String PROPERTY_KEY_FARM = "farm";

  /**
   * Returns the properties from the honeycomb secure properties file
   * The mapping is from the site context to a map of properties.  The properties are a string key, to an object value
   */
  public Map<String, Map<String, Object>> readSecureProperties() throws FileNotFoundException;

  /**
   * Add properties for the given context. Creates the file if it doesn't exist yet. Overwrites properties if they already 
   * exist for the given context.
   */
  public void addSecureProperties( String context, Map<String, Object> properties ) throws IOException;
  
  /**
   * Convert from the honeycomb date string to a participant friendly display format on G
   */
  public String convertDateString( String honeycombDate );
  
  /**
   * Gets parameters to post in a form when doing sso to a honeycomb site
   */
  public GoalquestSSOResponse getGoalquestSSOParameters( String username ) throws BeaconRuntimeException;
  
  /**
   * Gets a path to forward the user to.  The path is a URL that will SSO them into their goalquest program site.
   * @param routerParameters Query parameters to be used as routing hints on the goalquest site
   * @return Forward URL
   */
  public String getGoalquestSSOPath( String username, Map<String, String> routerParameters ) throws URISyntaxException;
  
  /**
   * Send account sync details to honeycomb gateway.
   * This method will send details for all participants, in batches
   */
  @Async
  public Future<ParticipantSyncResponse> syncAllParticipantDetails() throws BeaconRuntimeException;
  
  /**
   * Send account sync details to honeycomb gateway.
   * This method will send participant details in batches for all user IDs listed. May pass a large list - this method will perform batching.
   */
  @Async
  public Future<ParticipantSyncResponse> syncParticipantDetails( List<Long> userIds ) throws BeaconRuntimeException;
  
  /**
   * Get GoalQuest program and progress information from Honeycomb
   */
  public GoalquestDetailsResponse getGoalquestProgramDetails( Long hcParticipantId ) throws BeaconRuntimeException;
  
  /**
   * Get GoalQuest programs for a manager. Only lists programs, does not contain progress details
   */
  public GoalquestDetailsResponse getGoalquestManagerPrograms( Long hcParticipantId ) throws BeaconRuntimeException;

}
