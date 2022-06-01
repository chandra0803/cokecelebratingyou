
import React, { Component } from 'react';
import SaUcTileHeader from './SaUcTileHeader';
import SimpleSlider from '../../shared/carousel/carousel';


class SaUcTile extends Component {
    constructor( props ) {
        super( props );
        const { content } = window.serviceAnniversary;
        this.state = {
            error: null,
            isLoaded: false,
            items: [],
            count: null,
            hasCelebrations: false,
            seeMoreUrl: null,
            content: content

        };
    }
    handleContent( data, text ) {
        const displayContent = data.filter( function( item ) { if( item.key === text ) {return item.content; }} )[ 0 ].content;
        return displayContent;
    }
    checkData( celebset ) {
        return celebset.isDefault === true;
    }
    getShareUrl = ( event, cid ) => {
        const e = window.event || event;
        G5.util.saContribute( e, cid );
    };
    emitUcs( ucSet, total ) {
        const upto =  window.location.pathname.lastIndexOf( '/' );//To form the see more url from js        
        const { content } = this.state;
        ( total ) ? total : ucSet.length;
        this.setState( {
            count: total,
            hasCelebrations: true,
            seeMoreUrl: window.location.origin + window.location.pathname.slice( 0, upto + 1 )

        } );
        const listInner = ucSet.map( ( celebset, index ) =>
            (
                <div className="cardTileInfo" key={ 'item' + index + celebset.id }>
                    <div className="cardHolder">
                        <div className="cardTileHeader" style={ {
                            'background': `${celebset.primaryColor} linear-gradient( to right, ${celebset.primaryColor} 0%,${celebset.secondaryColor} 100% )`
                        } }>
                        {/* Removed JS truncation and handled in flex ellipsis way */}
                            <div className="designation">
                            <h4>{celebset.anniversary}</h4>
                            <span className="time-left">
                            <i className="icon-clock"></i>
                            <span>{( celebset.isToday === true ) ? this.handleContent( content, 'CELEBRATION_TOMORROW' ) : celebset.timeLeft }</span>
                            </span>
                            </div>
                            <a href="#" onClick={ ( event ) => { this.getShareUrl( event, celebset.celebrationId ); } } style={ { 'color': `${celebset.primaryColor}` } }>{ this.handleContent( content, 'SHARE_A_MEMORY' ) }</a>
                        </div>
                        <div className="cardTileBody">
                            {celebset.avatarUrl &&
                                <div className="avatar-image"><img src={ G5.util.generateTimeStamp( celebset.avatarUrl ) } /></div>
                            }
                            {!celebset.avatarUrl &&
                                <div className="avatar-text">{celebset.firstName.charAt( 0 )} {celebset.lastName.charAt( 0 )}</div>
                            }
                            <div className="personalInfo">
                                <a href="#" className="personName profile-popover" data-participant-ids={ `[ ${celebset.id} ]` }>{celebset.firstName} {celebset.lastName}</a>
                                {celebset.anniversary &&
                                    <span className="celebrationInfo">{celebset.positionType}</span>
                                }
                            </div>
                        </div>
                    </div>
                </div>
            )
        );
        this.setState( {
            isLoaded: true,
            items: listInner
        } );
    }
    noUcs() {        
        const noRecord = <p>There are no Upcoming Celebrations</p>;
        this.setState( {
            count: 0,
            hasCelebrations: false,
            isLoaded: true,
            items: noRecord

        } );
    }
    componentDidMount() {       
        fetch( G5.props.URL_JSON_PURL_CELEBRATE_DATA )
            .then( res => res.json() )
            .then(
                ( result ) => {
                    const defaultSet = result.celebrationSets.filter( this.checkData );
                    const ucSet = defaultSet[ 0 ].celebrations;
                    ( ucSet.length !== 0 ) ? this.emitUcs( ucSet, defaultSet[ 0 ].total ) : this.noUcs();
                    
                },
                // Note: it's important to handle errors here
                // instead of a catch() block so that we don't swallow
                // exceptions from actual bugs in components.
                ( error ) => {
                    this.setState( {
                        isLoaded: true,
                        error
                    } );
                }
            );
    }

    render() {
        window.saLog = {};            
            const { error, isLoaded, items, hasCelebrations, seeMoreUrl, count, content  } = this.state;
        if ( error ) {
            window.saLog.error = 'Error in Carousel Loading: ' + error.message;
            return <div className="sa-load"><img src="./assets/img/pageLoadingSpinner.gif" /></div>;

        } else if ( !isLoaded ) {
            window.saLog.initial = 'Initial statue before the response';
            return <div className="sa-load"><img src="./assets/img/pageLoadingSpinner.gif" /></div>;
        } else {
            window.saLog.success = 'Successfully Loaded';
            return (
                <div className="sa-app-inner">
                    <SaUcTileHeader renderCount={ count } hasCelebrations={ hasCelebrations } seeMore = { seeMoreUrl } feedContent = { content } contentHandle = { this.handleContent } />
                    <SimpleSlider dataSet = { items } needCarousel={ count } seeMore = { seeMoreUrl } feedContent = { content } contentHandle = { this.handleContent } />
                </div>
            );
        }
    }
}

export default SaUcTile;