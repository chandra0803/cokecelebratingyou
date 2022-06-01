import React, { Component } from 'react';
import Slider from 'react-slick';

class SimpleSlider extends Component {
  constructor( props ) {
    super( props );
    this.state = {
      items: this.props.dataSet
    };
  }

  render() {
    const { items } = this.state;
    const renderItem = this.props.needCarousel;
    const envPrefix = '/' + window.location.pathname.split( '/' )[ 1 ] + '/';
    const settings = {
      dots: false,
      infinite: false,
      speed: 500,
      slidesToShow: 3,
      slidesToScroll: 3,
      initialSlide: 0,
      responsive: [
        {
          breakpoint: 1199,
          settings: {
            slidesToShow: 2,
            slidesToScroll: 2
          }
        },
        {
          breakpoint: 768,
          settings: {
            slidesToShow: 2,
            slidesToScroll: 2
          }
        },
        {
          breakpoint: 640,
          settings: {
            slidesToShow: 1,
            slidesToScroll: 1
          }
        }
      ]
    };

    return (
      <div>
        {renderItem ? (
          <Slider { ...settings }>
            {items}
          </Slider>
        ) : (
            <div className="noSACelebration">
              <p>{this.props.contentHandle( this.props.feedContent, 'NO_CELEBRATIONS' )}</p>
              <a href={ envPrefix + 'purl/purlCelebratePage.do?purlPastPresentSelect=past' } className="btn btn-primary">{this.props.contentHandle( this.props.feedContent, 'VIEW_PAST_PURLS' )}</a>
            </div>
          )}
      </div>
    );
  }
}

export default SimpleSlider;