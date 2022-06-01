import React from 'react';

class ItemList extends React.Component {
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
        item
    } = this.props;

    return (
     <div>
      <a className="arrow icon-arrow-1-circle-right" data-id={ item.id } onClick={ this.handleRecognize }></a>
        <div className="avatarwrap">
                <span className="avatar-initials">
                  { item.avatarUrl ? (
                    <img alt="avatar" src= { G5.util.generateTimeStamp( item.avatarUrl ) } />
                  ) :
                  (
                    <span>{item.firstName.charAt( 0 )} {item.lastName.charAt( 0 )}</span>
                  )
                }
                </span>
            </div>
            <a className="itemName leaderName profile-popover" data-participant-ids={ `[ ${item.id} ]` }>{item.firstName} {item.lastName}
            </a><span className="desgination">{item.positionType}</span>
      </div>
    );
  }
}


export default ItemList;
