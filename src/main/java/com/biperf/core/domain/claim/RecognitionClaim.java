/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/claim/RecognitionClaim.java,v $
 */

package com.biperf.core.domain.claim;

import com.biperf.core.domain.promotion.CelebrationManagerMessage;

/**
 * RecognitionClaim.
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
 * <td>crosenquest</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RecognitionClaim extends AbstractRecognitionClaim
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * If true, send a copy of the e-mail message sent the recipient to the
   * recipient's manager; if false, do not.
   */
  private boolean copyManager;
  private boolean copyOthers;
  private String sendCopyToOthers;
  private Integer anniversaryNumberOfDays = new Integer( 0 );
  private Integer anniversaryNumberOfYears = new Integer( 0 );
  private CelebrationManagerMessage celebrationManagerMessage;

  private boolean managerAward = false;
  private Long certificateId;

  // Currently set to TRUE by PURL if sending PURL email
  private boolean skipStandardRecognitionEmail = false;

  // transient
  private String purlUrl;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  public boolean isManagerAward()
  {
    return managerAward;
  }

  public void setManagerAward( boolean managerAward )
  {
    this.managerAward = managerAward;
  }

  /**
   * Constructs a <code>RecognitionClaim</code> object.
   */
  public RecognitionClaim()
  {
    // empty constructor.
  }

  /**
   * Constructs a <code>RecognitionClaim</code> object.
   *
   * @param claimFormStep  a claim form step.
   */
  public RecognitionClaim( ClaimFormStep claimFormStep )
  {
    super( claimFormStep );
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public boolean isCopyManager()
  {
    return copyManager;
  }

  public void setCopyManager( boolean copyManager )
  {
    this.copyManager = copyManager;
  }

  public Long getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( Long certificateId )
  {
    this.certificateId = certificateId;
  }

  public boolean isSkipStandardRecognitionEmail()
  {
    return skipStandardRecognitionEmail;
  }

  public void setSkipStandardRecognitionEmail( boolean skipStandardRecognitionEmail )
  {
    this.skipStandardRecognitionEmail = skipStandardRecognitionEmail;
  }

  public void setCopyOthers( boolean copyOthers )
  {
    this.copyOthers = copyOthers;
  }

  public boolean isCopyOthers()
  {
    return copyOthers;
  }

  public void setSendCopyToOthers( String sendCopyToOthers )
  {
    this.sendCopyToOthers = sendCopyToOthers;
  }

  public String getSendCopyToOthers()
  {
    return sendCopyToOthers;
  }

  public String getPurlUrl()
  {
    return purlUrl;
  }

  public void setPurlUrl( String purlUrl )
  {
    this.purlUrl = purlUrl;
  }

  public Integer getAnniversaryNumberOfDays()
  {
    return anniversaryNumberOfDays;
  }

  public void setAnniversaryNumberOfDays( Integer anniversaryNumberOfDays )
  {
    this.anniversaryNumberOfDays = anniversaryNumberOfDays;
  }

  public Integer getAnniversaryNumberOfYears()
  {
    return anniversaryNumberOfYears;
  }

  public void setAnniversaryNumberOfYears( Integer anniversaryNumberOfYears )
  {
    this.anniversaryNumberOfYears = anniversaryNumberOfYears;
  }

  public CelebrationManagerMessage getCelebrationManagerMessage()
  {
    return celebrationManagerMessage;
  }

  public void setCelebrationManagerMessage( CelebrationManagerMessage celebrationManagerMessage )
  {
    this.celebrationManagerMessage = celebrationManagerMessage;
  }

}
