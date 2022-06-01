import React, { Component } from 'react';

class SaUcTileHeader extends Component {
  render() {
    const renderHeader = this.props.renderCount;    
    return (
      <div>
        {renderHeader ? (
          <div className="saSliderHeader">
            <h3>
            {this.props.renderCount > 1 ? this.props.renderCount + ' ' + this.props.contentHandle( this.props.feedContent, 'UPCOMING_PURLS' ) : this.props.renderCount + ' ' + this.props.contentHandle( this.props.feedContent, 'UPCOMING_PURLS' ).slice( 0, -1 )  } </h3>
            <a href={ this.props.seeMore + 'purl/purlCelebratePage.do' } className="seeMore">{ this.props.contentHandle( this.props.feedContent, 'SEE_MORE' ) }</a>
          </div>
        ) : ( <div className="hide"></div>
          )
        }
      </div>
    );
  }
}

export default SaUcTileHeader;