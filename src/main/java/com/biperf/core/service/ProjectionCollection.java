/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/Attic/ProjectionCollection.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.service;

import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.transform.Transformers;

import com.biperf.core.exception.BeaconRuntimeException;

/**
 * 
 * 
 * This class is used to hold a list of ProjectionAttributes.  This list is used by the
 * DAO layer to process any Projections.  This allows the client to determine exactly 
 * what elements to return, thus potentially drastically reducing the SQL necessary.  For example,
 * find a Participant object by id generates at least 4-5 calls to the database for eager fetching -
 * including Roles, ACL lists, Nodes, etc.  In many cases, we don't need that.  The following collection,
 * for example, would only make 1 call to the database which would product SQL as follows:
 * 
 *    ProjectionCollection collection = new ProjectionCollection() ;
 *    collection.add( new ProjectionAttribute( "id" ) ) ;
 *    collection.add( new ProjectionAttribute( "firstName" ) ) ;
 *    collection.add( new ProjectionAttribute( "lastName" ) ) ;
 *    collection.add( new ProjectionAttribute( "avatarOriginal" ) ) ;
 *    collection.add( new ProjectionAttribute( "avatarSmall" ) ) ;
 *    participant = getParticipantService().getParticipantByIdWithProjections( participant.getId(), collection ) ;
 *    
 *    results in the following SQL with is much more efficient:
 *    
 *   select
 *       this_.USER_ID as y0_,
 *       this_1_.FIRST_NAME as y1_,
 *       this_1_.LAST_NAME as y2_,
 *       this_.AVATAR_ORIGINAL as y3_,
 *       this_.AVATAR_SMALL as y4_ 
 *   from
 *       PARTICIPANT this_ 
 *   inner join
 *       APPLICATION_USER this_1_ 
 *           on this_.USER_ID=this_1_.USER_ID 
 *   where
 *       this_.USER_ID=?
 * 
 * This class currently only fetches attributes of the mapped 
 * class and/or super class, it can later be enhanced to also optionally fetch other associations.  
 * 
 */
@SuppressWarnings( "serial" )
public class ProjectionCollection extends ArrayList<ProjectionAttribute>
{
  @SuppressWarnings( "rawtypes" )
  public Criteria processProjections( Criteria criteria, Class aliasClass )
  {
    ProjectionList projectionList = Projections.projectionList();
    for ( Iterator<ProjectionAttribute> projectionAttributes = this.iterator(); projectionAttributes.hasNext(); )
    {
      ProjectionAttribute projection = projectionAttributes.next();
      projectionList.add( Projections.property( projection.getAttributeName() ), projection.getAttributeAlias() );
    }

    if ( projectionList.getLength() > 0 )
    {
      criteria.setProjection( projectionList );
    }

    if ( aliasClass != null )
    {
      criteria.setResultTransformer( Transformers.aliasToBean( aliasClass ) );
    }

    return criteria;
  }

  public Criteria processProjections( Criteria criteria )
  {
    Class<?> clazz;
    try
    {
      clazz = Class.forName( ( (CriteriaImpl)criteria ).getEntityOrClassName() );
      return processProjections( criteria, clazz );
    }
    catch( ClassNotFoundException e )
    {
      throw new BeaconRuntimeException( e.getMessage(), e );
    }
  }
}
