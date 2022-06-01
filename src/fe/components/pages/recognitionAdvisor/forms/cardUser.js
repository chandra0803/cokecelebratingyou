import React from 'react';

class CardUser extends React.Component {
  constructor( props ) {
      super( props );
  }

  handleRecognize( e ) {
    const origin = window.location.origin;
    const loc = window.location.pathname;
    const context = loc.split( '/' )[ 1 ];
    const paxId = e.target.getAttribute( 'data-id' );
    const url = `${origin}/${context}/ra/${paxId}/sendRecognition.action`;
    window.location.href = url;
  }

  render() {

    const {
      content
    } = this.props;

    if ( this.props.groupType === 3 ) {
      return (
        <div className="card card-large highlightedUser upComing">
          <div className="upcomingInfo">
            <span className="icon-check-circle"></span>
            <h4>{ content[ 'recognition.content.model.info.ra_caught' ] }</h4>
          </div>
        </div>
      );
    }
    if ( !this.props.cardUser.length ) {
      return false;
    }
    const cardUser  = this.props.cardUser[ 0 ];
    return (
      <div className="card card-large highlightedUser" >
        <div className="card-front">
        { cardUser.participantGroupType == 1 ? (
                <div className="recognitionAdvisorStatus"><span className="icon-star removeCircle"></span>{ content[ 'recognition.content.model.info.ra_new_employee' ] }</div>
              ) :
              (
                <div className="recognitionAdvisorStatus"><span className="icon-clock removeCircle"></span>{cardUser.currentMgrLastRecogDays} { content[ 'recognition.content.model.info.ra_days_over_due' ] }</div>
              )
            }
          <div className="card-top">
            <span className="avatar">
              <span className="avatarContainer">
              { cardUser.avatarUrl ? (
                  <img alt="avatar" src= { G5.util.generateTimeStamp( cardUser.avatarUrl ) } />
                ) :
                (
                  <span className="avatar-initials">{cardUser.firstName.charAt( 0 )} {cardUser.lastName.charAt( 0 )}</span>
                )
              }
              </span>
            </span>
          </div>
          <div className="card-details">
            <div className="card-details-inner-wrap">
              <div className="participant-name">
				<a className="itemName" data-participant-ids={ `[ ${cardUser.id} ]` } data-title-page-view="Public Profile">{cardUser.firstName} {cardUser.lastName}</a>
        </div>
              <div className="participant-info">
                <span className="pi-title">{cardUser.positionType}</span>
              </div>
            </div>
          </div>
          <div className="card-action">
            <button type="button" className="btnRecognize btn btn-primary btn-block flipButton" data-id={ cardUser.id } onClick={ ( e ) => this.handleRecognize( e ) } >{ content[ 'recognition.content.model.info.ra_recognize' ] }</button>
          </div>

        </div>
      </div>
    );
  }
}

export default CardUser;
