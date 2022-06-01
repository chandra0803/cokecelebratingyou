import React from 'react';

class RecAdvisorModalBody extends React.Component {
  constructor( props ) {
			super( props );
		}


    getBaseAndContext = ( e ) => {
      const origin = window.location.origin;
      const loc = window.location.pathname;
      const context = loc.split( '/' )[ 1 ];
      const reqUrl = `${origin}/${context}`;
       return reqUrl;
    }

    handleRecognize( e ) {
      const paxId = e.target.getAttribute( 'data-id' );
      const url = `${this.getBaseAndContext ( e )}/ra/${paxId}/sendRecognition.action`;
      window.location.href = url;
    }

    moveToRADetailsPage = ( e ) => {
      const url = `${this.getBaseAndContext ( e )}/raDetails.do`;
      window.location.href = url;
    }

    getPaxCards = ( paxList, content ) => {
        const opCards = paxList.map( ( cardUser ) =>
        <div key={ cardUser.id } className="modelCardDsiplay">
        <div className="card card-small" >
          <div className="card-front">
            <div className="card-top">
              <span className="avatar">
                <span className="avatarContainer">
                { cardUser.avatarUrl ? (
                    <img alt="avatar" src= { G5.util.generateTimeStamp( cardUser.avatarUrl ) } style={ { textAlign: 'left' } } />
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
                <div className="participant-name">{cardUser.firstName} {cardUser.lastName}</div>
                <div className="participant-info">
                  <span className="pi-title">{cardUser.positionType}</span>
                </div>
              </div>
            </div>
            <div className="card-action">
              <button type="button" className="btnRecognize btn btn-primary btn-block flipButton" data-id={ cardUser.id } onClick={ ( e ) => this.handleRecognize( e ) } >{ content[ 'recognition.content.model.info.ra_recognize' ] }</button>
            </div>
            { cardUser.participantGroupType == 1 ? (
                <div className="recognitionAdvisorStatus"><span className="icon-star removeCircle"></span>{content[ 'recognition.content.model.info.ra_new_employee' ]}</div>
              ) :
              (
                <div className="recognitionAdvisorStatus"><span className="icon-clock removeCircle"></span>{cardUser.currentMgrLastRecogDays} {content[ 'recognition.content.model.info.ra_days_over_due' ]}</div>
              )
            }
          </div>
        </div>
         </div>

      );
          return opCards;
    }

    getBodyFooter = ( raModalBodyFooter ) => {
     const raModalFooter = (
        <p> <a className="raReminderDidplayCount" onClick={ ( e ) =>  this.moveToRADetailsPage( e ) }> { raModalBodyFooter }
          <span className="arrow raModalPosition"  >&rsaquo; </span>
         </a></p>

     );
     return raModalFooter;

   }

  render () {

    const {
      content
    } = this.props;

      const raReminderParticipants = this.props.recognitionAdvisor.raReminderParticipants;
      const raModalBodyFooterPaxCount = this.props.recognitionAdvisor.raEndModalTotalParticipants;
      let raDisplayPaxList, raReminderPaxCards, raModalBodyFooter ;


    if( raReminderParticipants ) {

			raDisplayPaxList = raReminderParticipants.recentlyAddedParticipants;
      raReminderPaxCards = this.getPaxCards( raDisplayPaxList, content );
		}

    if( raModalBodyFooterPaxCount >= 1 ) {
      raModalBodyFooter = this.getBodyFooter( content[ 'recognition.content.model.info.ra_modal_body_footer' ] );
    }

    return (
        <div id="ModalBody" className="modal-body">
        <div className="raInsights">	
		<div>
				<div className="raInsightsLogoPosition">
					<div className="raInsightsLogo">
					<svg id="Layer_1" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 191.97 189.22"><defs><clipPath id="clip-path" transform="translate(0 0)"><rect width="191.97" height="189.21" /></clipPath></defs><title>Vector Smart Object</title><g><path className="raBrandingOuter" d="M183.85,33a95.66,95.66,0,0,0-22-31.16,6.69,6.69,0,0,0-9.19,0A82.26,82.26,0,0,1,96,24.31,82.26,82.26,0,0,1,39.35,1.82a6.7,6.7,0,0,0-9.19,0,96,96,0,0,0,7.38,146h0l-8.9,41.4,54.95-22.35h0a96.83,96.83,0,0,0,12.41.8,95.91,95.91,0,0,0,96-96A95.26,95.26,0,0,0,183.85,33m-29.46,97.09A82,82,0,0,1,96,154.27a83.63,83.63,0,0,1-13.79-1.14L46.92,166.91,52.7,142a83.23,83.23,0,0,1-15.12-12A82.51,82.51,0,0,1,35.08,15.9,95.56,95.56,0,0,0,96,37.7a95.56,95.56,0,0,0,60.91-21.8,82.59,82.59,0,0,1-2.51,114.18" transform="translate(0 0)"/><path className="raBrandingInner"  d="M140.63,59.42a24.39,24.39,0,0,0-17,6.88,10.21,10.21,0,0,0-1,.83L96,92.2,69.39,67.13a10.1,10.1,0,0,0-1-.83,24.55,24.55,0,1,0,.32,35A24.74,24.74,0,0,0,72,97.22l17.07,16.09a10,10,0,0,0,13.78,0l17.07-16.09a24.74,24.74,0,0,0,3.32,4.11,24.55,24.55,0,1,0,17.36-41.92M51.34,95.14A11.16,11.16,0,1,1,62.5,84,11.16,11.16,0,0,1,51.34,95.14m89.29,0A11.16,11.16,0,1,1,151.79,84a11.16,11.16,0,0,1-11.16,11.16" transform="translate(0 0)" /></g>
					</svg>
					</div>
				</div>
			<div className="raInsightsBackgrondImg"></div>
			<div className="raInsightsGradgient"></div>
		</div>
		<div className="raInsightsBodyContent">
                    <h4 className="raModelBodyHeader">{ content[ 'recognition.content.model.info.ra_modal_body_header' ] }</h4>
                        { raReminderPaxCards }
                        { raModalBodyFooterPaxCount >= 1 &&
                          raModalBodyFooter }
                        
            			</div>
            		
            		</div>
                        
        </div>
    );
  }
}


export default RecAdvisorModalBody;
