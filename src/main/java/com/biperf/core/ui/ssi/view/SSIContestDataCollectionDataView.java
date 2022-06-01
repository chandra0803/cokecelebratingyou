
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.participant.NameIdBean;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.domain.ssi.SSIContestClaimField;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * SSIContestPayoutObjectivesContestView.
 * 
 * @author patelp
 * @since May 12, 2015
 * @version 1.0
 */
@JsonInclude( value = Include.NON_NULL )
public class SSIContestDataCollectionDataView
{
  // properties used by creator in data collection step
  private String nextUrl;
  private String ssiContestClientState;
  private String collectDataMethod;
  private String claimDeadlineDate;
  private Boolean uploadSpreadsheetAvailable;
  private Boolean fillOutFormAvailable;
  private String activityDescription;
  private String contestEndDate;
  // properties used by pax in submit claim form
  private String contestType;
  private String measureActivityIn;
  private String activity;

  // properites used by both creator & pax
  private Long id;
  private List<SSIContestDataCollectionFieldsView> fields = new ArrayList<SSIContestDataCollectionFieldsView>();
  private List<NameIdBean> activities = new ArrayList<NameIdBean>();

  public SSIContestDataCollectionDataView()
  {
    super();
  }

  // constructor for creator in data collection step
  public SSIContestDataCollectionDataView( SSIContest contest, String ssiContestClientState, ClaimForm claimForm, String sysUrl )
  {
    this.nextUrl = sysUrl + "/ssi/dataCollection.do";
    this.ssiContestClientState = ssiContestClientState;
    this.id = contest.getId();
    if ( contest.getPromotion().getAllowActivityUpload() && contest.getPromotion().getAllowClaimSubmission() )
    {
      this.collectDataMethod = contest.getDataCollectionType();
    }
    else if ( contest.getPromotion().getAllowActivityUpload() )
    {
      this.collectDataMethod = SSIContestUtil.ACTIVITY_UPLOAD;
    }
    else
    {
      this.collectDataMethod = SSIContestUtil.CLAIM_SUBMISSION;
    }
    this.claimDeadlineDate = contest.getClaimSubmissionLastDate() != null ? DateUtils.toDisplayString( contest.getClaimSubmissionLastDate() ) : null;
    this.contestEndDate = contest.getEndDate() != null ? DateUtils.toDisplayString( DateUtils.getDateAfterNumberOfDays( contest.getEndDate(), 1 ) ) : null;
    this.uploadSpreadsheetAvailable = contest.getPromotion().getAllowActivityUpload();
    this.fillOutFormAvailable = contest.getPromotion().getAllowClaimSubmission();
    this.activityDescription = contest.getActivityDescription();
    if ( claimForm != null )
    {
      // separating required & optional elements and sorting alphabetically
      List<ClaimFormStepElement> allUnFilteredElements = claimForm.getClaimFormSteps().get( 0 ).getClaimFormStepElements();
      List<ClaimFormStepElement> requiredElements = new ArrayList<ClaimFormStepElement>();
      List<ClaimFormStepElement> optionalElements = new ArrayList<ClaimFormStepElement>();
      List<ClaimFormStepElement> allFilteredElements = new ArrayList<ClaimFormStepElement>();
      // separating
      for ( ClaimFormStepElement element : allUnFilteredElements )
      {
        if ( element.isRequired() )
        {
          if ( contest.getActivityMeasureType().isCurrency() && !element.getI18nLabel().equalsIgnoreCase( SSIContestUtil.CLAIM_FIELD_QUANTITY ) )
          {
            requiredElements.add( element );
          }
          else if ( contest.getActivityMeasureType().isUnit() && !element.getI18nLabel().equalsIgnoreCase( SSIContestUtil.CLAIM_FIELD_AMOUNT ) )
          {
            requiredElements.add( element );
          }
        }
        else
        {
          optionalElements.add( element );
        }
      }
      // sorting
      ClaimFieldAlphabiticalComparer comparator = new ClaimFieldAlphabiticalComparer();
      Collections.sort( requiredElements, comparator );
      Collections.sort( optionalElements, comparator );
      // combining
      allFilteredElements.addAll( requiredElements );
      allFilteredElements.addAll( optionalElements );

      // set sequence for first time visiting 4th step(Data Collection Step)
      int sequence = 1;
      for ( ClaimFormStepElement element : allFilteredElements )
      {
        this.fields.add( new SSIContestDataCollectionFieldsView( element, sequence ) );
        ++sequence;
      }

      // contest fields already persisted in db, pre populate sequence no, selected, required
      for ( SSIContestClaimField claimField : contest.getClaimFields() )
      {
        for ( SSIContestDataCollectionFieldsView field : this.fields )
        {
          if ( claimField.getFormElement().getId().longValue() == field.getId().longValue() )
          {
            field.setSequenceNumber( claimField.getSequenceNumber() );
            field.setIsSelected( true );
            field.setIsRequired( claimField.isRequired() );
            break;
          }
        }
      }
    }
    if ( contest.getContestType().isDoThisGetThat() )
    {
      for ( SSIContestActivity activity : contest.getContestActivities() )
      {
        this.activities.add( new NameIdBean( activity.getId(), activity.getDescription(), activity.getDescription() ) );
      }
    }
  }

  public class ClaimFieldAlphabiticalComparer implements Comparator<ClaimFormStepElement>
  {
    @Override
    public int compare( ClaimFormStepElement c1, ClaimFormStepElement c2 )
    {
      return c1.getI18nLabel().compareTo( c2.getI18nLabel() );
    }
  }

  // constructor for pax while submitting claim
  public SSIContestDataCollectionDataView( SSIContest contest, List<Country> countries, SSIContestParticipant contestParticipant )
  {
    this.id = contest.getId();
    this.contestType = contest.getContestTypeName();
    this.measureActivityIn = contest.getActivityMeasureType().getCode();
    this.activity = contestParticipant == null ? contest.getActivityDescription() : contestParticipant.getActivityDescription();
    // only for dtgt contest type
    if ( contest.getContestType().isDoThisGetThat() )
    {
      for ( SSIContestActivity activity : contest.getContestActivities() )
      {
        NameIdBean activityBean = new NameIdBean();
        activityBean.setId( activity.getId() );
        activityBean.setName( activity.getDescription() );
        activityBean.setValue( "" );
        this.activities.add( activityBean );
      }
    }
    for ( SSIContestClaimField claimField : contest.getClaimFields() )
    {
      if ( ! ( contest.getContestType().isDoThisGetThat() && ( SSIContestUtil.CLAIM_FIELD_QUANTITY.equalsIgnoreCase( claimField.getFormElement().getI18nLabel() ) || SSIContestUtil.CLAIM_FIELD_AMOUNT
          .equalsIgnoreCase( claimField.getFormElement().getI18nLabel() ) ) ) )
      {
        if ( claimField.getFormElement().getClaimFormElementType().isAddressBlock() )
        {
          for ( int sequence = 1; sequence <= 7; sequence++ )
          {
            this.fields.add( new SSIContestDataCollectionFieldsView( claimField, sequence, countries ) );
          }
        }
        else
        {
          this.fields.add( new SSIContestDataCollectionFieldsView( contest, claimField ) );
        }
      }
    }
  }

  public String getNextUrl()
  {
    return nextUrl;
  }

  public void setNextUrl( String nextUrl )
  {
    this.nextUrl = nextUrl;
  }

  public String getSsiContestClientState()
  {
    return ssiContestClientState;
  }

  public void setSsiContestClientState( String ssiContestClientState )
  {
    this.ssiContestClientState = ssiContestClientState;
  }

  public String getCollectDataMethod()
  {
    return collectDataMethod;
  }

  public void setCollectDataMethod( String collectDataMethod )
  {
    this.collectDataMethod = collectDataMethod;
  }

  public String getClaimDeadlineDate()
  {
    return claimDeadlineDate;
  }

  public void setClaimDeadlineDate( String claimDeadlineDate )
  {
    this.claimDeadlineDate = claimDeadlineDate;
  }

  @JsonProperty( "isUploadSpreadsheetAvailable" )
  public Boolean getUploadSpreadsheetAvailable()
  {
    return uploadSpreadsheetAvailable;
  }

  public void setUploadSpreadsheetAvailable( Boolean uploadSpreadsheetAvailable )
  {
    this.uploadSpreadsheetAvailable = uploadSpreadsheetAvailable;
  }

  @JsonProperty( "isFillOutFormAvailable" )
  public Boolean getFillOutFormAvailable()
  {
    return fillOutFormAvailable;
  }

  public void setFillOutFormAvailable( Boolean fillOutFormAvailable )
  {
    this.fillOutFormAvailable = fillOutFormAvailable;
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public String getContestType()
  {
    return contestType;
  }

  public void setContestType( String contestType )
  {
    this.contestType = contestType;
  }

  public String getMeasureActivityIn()
  {
    return measureActivityIn;
  }

  public void setMeasureActivityIn( String measureActivityIn )
  {
    this.measureActivityIn = measureActivityIn;
  }

  public String getActivity()
  {
    return activity;
  }

  public void setActivity( String activity )
  {
    this.activity = activity;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public List<SSIContestDataCollectionFieldsView> getFields()
  {
    return fields;
  }

  public void setFields( List<SSIContestDataCollectionFieldsView> fields )
  {
    this.fields = fields;
  }

  public List<NameIdBean> getActivities()
  {
    return activities;
  }

  public void setActivities( List<NameIdBean> activities )
  {
    this.activities = activities;
  }

  public String getContestEndDate()
  {
    return contestEndDate;
  }

  public void setContestEndDate( String contestEndDate )
  {
    this.contestEndDate = contestEndDate;
  }

}
