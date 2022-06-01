import React from 'react';

class RecAdvisorModalBadgeBody extends React.Component {
  constructor( props ) {
			super( props );
		}

   getBadgeDetails = ( badgeDetails, content, baseURI ) => {
     const badges = badgeDetails.map( ( badge ) =>
     <div key={ badge.badgeName } className="badge-group">
     <div className="badges">
     <ul>
       <li className="badge-item progress-type earned-true" >
      <img src={ baseURI + badge.img } style={ { textAlign: 'left' } } />
      <span className="badge-name">{ badge.badgeName }<br /></span>
      <span className="badge-how-to-earn">{ badge.badgeDescription }<br/></span>
      { !badge.earned && badge.badgeType === 'progress' &&
       <div className="progress">
       <div className="bar" style = { { width: ( badge.progressNumerator * 100 / badge.progressDenominator ) + '%' } } >{badge.progressNumerator}/{badge.progressDenominator}</div>
       </div>
      }
      { badge.earned &&
          <span className="badge-date-earned">{content[ 'gamification.admin.labels.earned_date' ]} { badge.dateEarned }</span>
      }
      </li>
      </ul>
     </div>
     </div>
     );

     return badges;
   }

  render () {

      const {
        content
      } = this.props;

      const raEndModal = this.props.recognitionAdvisor.raEndModal;
      const badgeDetailsCount = this.props.recognitionAdvisor.badgeDetailsCount;
      const baseURI = this.props.recognitionAdvisor.baseURI;
      let badgesForModal ;

      if( badgeDetailsCount >= 1 && raEndModal === false )
      {
        badgesForModal = this.getBadgeDetails( this.props.recognitionAdvisor.raRecognitionSentBean, content, baseURI );
      }

    return (
      <div className="modal-body">
        {  badgesForModal }
      </div>
    );
  }
}


export default RecAdvisorModalBadgeBody;
