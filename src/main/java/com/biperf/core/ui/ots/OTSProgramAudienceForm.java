
package com.biperf.core.ui.ots;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.AudienceType;
import com.biperf.core.domain.enums.OTSProgramStatusType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.ots.OTSProgram;
import com.biperf.core.domain.ots.ProgramAudience;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.service.ots.OTSProgramService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ServiceLocator;

public class OTSProgramAudienceForm extends BaseActionForm
{
  public static final String PRIMARY = "primary";

  private String clientName;
  private String description;
  private String promotionStatus;
  private String audienceType;
  private String programAudienceId;
  private List programAudienceList = new ArrayList();
  private Long version;
  private String method;
  private Long programNumber;

  public int getProgramAudienceListCount()
  {
    if ( programAudienceList == null )
    {
      return 0;
    }

    return programAudienceList.size();
  }

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public String getAudienceType()
  {
    return audienceType;
  }

  public void setAudienceType( String primaryAudienceType )
  {
    this.audienceType = primaryAudienceType;
  }

  public String getProgramAudienceId()
  {
    return programAudienceId;
  }

  public void setProgramAudienceId( String programAudienceId )
  {
    this.programAudienceId = programAudienceId;
  }

  public OTSProgramAudienceFormBean getProgramAudienceList( int index )
  {
    try
    {
      return (OTSProgramAudienceFormBean)programAudienceList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public List getProgramAudienceAsList()
  {
    return programAudienceList;
  }

  public void setProgramAudienceAsList( List primaryAudienceList )
  {
    this.programAudienceList = primaryAudienceList;
  }

  public int getProgramAudienceCount()
  {
    return programAudienceList != null ? programAudienceList.size() : 0;
  }

  @SuppressWarnings( "unchecked" )
  public void addAudience( Audience audience, String promoAudienceType )
  {
    OTSProgramAudienceFormBean audienceFormBean = new OTSProgramAudienceFormBean();
    audienceFormBean.setAudienceId( audience.getId() );
    audienceFormBean.setName( audience.getName() );
    audienceFormBean.setAudienceType( audience.getAudienceType().getCode() );
    audienceFormBean.setSize( audience.getSize() );

    programAudienceList.add( audienceFormBean );

  }

  private void processProgramAudience( OTSProgram program )
  {
    program.setAudienceType( PrimaryAudienceType.lookup( this.getAudienceType() ) );
    if ( !program.getAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      if ( getProgramAudienceAsList().size() > 0 )
      {
        Set<ProgramAudience> primaryAudienceSet = buildDomainAudienceSet( program, PRIMARY );

        program.setProgramAudience( primaryAudienceSet );
      }
    }
    else
    {
      program.getProgramAudience().clear();
    }
  }

  public OTSProgram toDomainObject( OTSProgram program )
  {

    program.setProgramNumber( new Long( this.getProgramNumber() ) );

    processProgramAudience( program );
    return program;
  }

  private Set buildDomainAudienceSet( OTSProgram program, String audienceType )
  {

    Set<ProgramAudience> audienceSet = new LinkedHashSet<ProgramAudience>();

    Iterator audienceIter = null;

    if ( audienceType.equals( PRIMARY ) )
    {
      getProgramAudienceAsList().removeAll( program.getAudiences() );
      audienceIter = getProgramAudienceAsList().iterator();
    }

    if ( audienceIter != null )
    {
      while ( audienceIter.hasNext() )
      {
        boolean includeAudience = true;

        OTSProgramAudienceFormBean audienceBean = (OTSProgramAudienceFormBean)audienceIter.next();

        if ( includeAudience )
        {
          ProgramAudience promoAudience = getOTSProgramService().getOTSProgramAudienceByProgramNumberAndAudienceId( program.getId(), audienceBean.getAudienceId() );
          if ( audienceType.equals( PRIMARY ) && !Objects.nonNull( promoAudience ) )
          {
            promoAudience = new ProgramAudience();
          }
          Audience audience = null;
          // TODO maybe move this into the audience itself
          if ( audienceBean.getAudienceType().equals( AudienceType.SPECIFIC_PAX_TYPE ) )
          {
            audience = new PaxAudience();
          }
          else
          {
            audience = new CriteriaAudience();
          }

          if ( !Objects.isNull( getAudienceService().getAudienceById( audienceBean.getAudienceId(), null ) ) )
          {
            audience = getAudienceService().getAudienceById( audienceBean.getAudienceId(), null );

          }
          else
          {
            audience.setId( audienceBean.getId() );
            audience.setName( audienceBean.getName() );
          }

          Long formPromoAudienceId = audienceBean.getId();
          Long promoAudienceId;
          if ( formPromoAudienceId == null || formPromoAudienceId.equals( new Long( 0 ) ) )
          {
            // Because audienceBean.getId( is Long, we'll get 0 when we really want null.
            // TODO: switch Form bean promoAudienceId to a String, Long not so good, because this
            // check is easy to miss
            // and it ends up causing funky Hibernate exceptions since Hibernate thinks 0 is a valid
            // id and that
            // we want to update when we really want to insert
            promoAudienceId = null;
          }
          else
          {
            promoAudienceId = formPromoAudienceId;
          }

          if ( promoAudience != null )
          {
            promoAudience.setId( promoAudience.getId() );
            promoAudience.setAudience( audience );
            promoAudience.setOtsProgram( program );
            audienceSet.add( promoAudience );

          }
        }
      }
    }

    return audienceSet;
  }

  public Long getProgramNumber()
  {
    return programNumber;
  }

  public void setProgramNumber( Long programNumber )
  {
    this.programNumber = programNumber;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getClientName()
  {
    return clientName;
  }

  public void setClientName( String clientName )
  {
    this.clientName = clientName;
  }

  public void load( OTSProgram program )
  {
    clientName = program.getClientName();
    description = program.getDescription();
    promotionStatus = program.getProgramStatus().getCode();

    if ( program.getAudienceType() != null )
    {
      audienceType = program.getAudienceType().getCode();
    }
    else
    {
      audienceType = PrimaryAudienceType.ALL_ACTIVE_PAX_CODE;
    }
    buildProgramAudience( program );
    OTSProgramStatusType promotionStatus = program.getProgramStatus();

  }

  private void loadAudience( Set audienceList, String audienceType )
  {
    ProgramAudience promotionAudience = null;
    Iterator audienceIter = null;
    audienceIter = audienceList.iterator();
    while ( audienceIter.hasNext() )
    {
      promotionAudience = (ProgramAudience)audienceIter.next();

      OTSProgramAudienceFormBean audienceBean = new OTSProgramAudienceFormBean();
      audienceBean.setId( promotionAudience.getId() );
      audienceBean.setAudienceId( promotionAudience.getAudience().getId() );
      audienceBean.setName( promotionAudience.getAudience().getName() );
      audienceBean.setSize( promotionAudience.getAudience().getSize() );
      audienceBean.setAudienceType( promotionAudience.getAudience().getAudienceType().getCode() );
      audienceBean.setVersion( promotionAudience.getAudience().getVersion() );
      if ( audienceType.equals( PRIMARY ) )
      {
        programAudienceList.add( audienceBean );
      }

    }
  }

  private void buildProgramAudience( OTSProgram program )
  {
    loadAudience( program.getProgramAudience(), PRIMARY );

    if ( program.getAudienceType() != null )
    {
      this.setAudienceType( program.getAudienceType().getCode() );
    }
  }

  private List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      // create an empty PromotionAudienceFormBean
      OTSProgramAudienceFormBean promoAudienceBean = new OTSProgramAudienceFormBean();
      valueList.add( promoAudienceBean );
    }

    return valueList;
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    programAudienceList = getEmptyValueList( RequestUtils.getOptionalParamInt( request, "programAudienceListCount" ) );
  }

  public void removeItems( String promoAudienceType )
  {
    Iterator it = null;
    if ( promoAudienceType.equals( PRIMARY ) )
    {
      it = getProgramAudienceAsList().iterator();
    }
    while ( it.hasNext() )
    {
      OTSProgramAudienceFormBean audienceFormBean = (OTSProgramAudienceFormBean)it.next();
      if ( audienceFormBean.isRemoved() )
      {
        it.remove();
      }
    }
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( errors == null )
    {
      errors = new ActionErrors();
    }

    if ( ( this.getAudienceType() != null ) && ( !this.getAudienceType().equals( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) ) && ( this.getProgramAudienceAsList().size() == 0 ) )
    {
      errors.add( "audienceType", new ActionMessage( "promotion.errors.NO_RECEIVER_AUDIENCE" ) );
    }
    return errors;
  }

  private AudienceService getAudienceService()
  {
    return (AudienceService)ServiceLocator.getService( AudienceService.BEAN_NAME );
  }

  private OTSProgramService getOTSProgramService()
  {
    return (OTSProgramService)ServiceLocator.getService( OTSProgramService.BEAN_NAME );
  }

}
