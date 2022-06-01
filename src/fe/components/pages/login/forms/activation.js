import React from 'react';
import classNames from 'classnames';

import LoaderButton from '../../../shared/loader-button/loader-button';
import NarrowForm from '../../../shared/narrow-form/narrow-form';
import 'whatwg-fetch';
import { Alert } from 'component-library';

import '../loginReact.scss';

class Activation extends React.Component {

    constructor( props ) {
        super( props );
        const loginID = new RegExp( '[\?&]loginID=([^&#]*)' ).exec( this.getLocation() );
        if( loginID && loginID[ 1 ] ) {
            this.state = {
                userId: loginID[ 1 ],
                focusedField: ''
            };
        } else {
            this.state = {
                userId: '',
                focusedField: ''
            };
        }
    }
    getLocation = () => {
        if( window.location.url ) {
            return window.location.url;
        } else {
            return window.location.href;
        }
    }
    handleField = ( event ) => {
        const name = event.target.name;
        const newState = Object.assign( {}, this.state, { [ name ]: event.target.value } );
        this.setState( newState );
    }


    onSubmit = () => {

        const {
            fetchActivationData } = this.props;
        const {
            activInitUrl } = this.props.login;
        const { userId } = this.state;
        const url = activInitUrl.replace( '(userId)', encodeURIComponent( userId ) );
        fetchActivationData( userId, url );

    }
    componentWillReceiveProps( nextProps ) {
        if( nextProps.login.message && nextProps.login.message.error ) {
            this.setState ( { userId: '' } );
        }
    }
    checkEnter = ( event ) => {
        const enterPressed = event.charCode === 13;
        const { userId } = this.state;

        if ( enterPressed && userId.length ) {
            event.preventDefault();
            this.onSubmit();
        } else if ( enterPressed ) {
            event.preventDefault();
        }
    }

  render() {
	const loggingIn = false;
    const {
        content
    } = this.props;
    const {
        message,
    } = this.props.login;
    const {
        userId
    } = this.state;

    const userIdFieldClasses = classNames( {
        'email-field': true,
        'input-wrap': true,
        'focused': true,
    } );
    const clientLogo = '';
    if( message && message.error ) {
        message.error = message.error.template( { linkOpen: '<a href="login.do" >', linkClose: '</a>' } );
    }
    return (
		<NarrowForm page="login" clientLogo={ clientLogo } method="post">
            <p>
                <a tabIndex="3" onClick={ ( e ) => this.props.handleFormChange( 'login' ) }>
                    <span><i className="icon-arrow-1-circle-left"></i> { content[ 'login.loginpage.back_to_login' ] }</span>
                </a>
            </p>          
            <h3 dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.activation' ] } }/>

            {
                message &&
                message.error &&
                        <Alert type={ 'error' } key={ message.id }>
                            <p dangerouslySetInnerHTML={ { __html: message.error } }/>
                        </Alert>

            }
            {
                message &&
                message.success &&
                        <Alert type={ 'success' } key={ message.id }>
                            <p dangerouslySetInnerHTML={ { __html: message.success } }/>
                        </Alert>

            }



                <div className={ userIdFieldClasses }>
                    <label htmlFor="formUserId" className="control-label">{ content[ 'login.loginpage.username' ] }</label>
                    <input
                        type="text"
                        name="userId"
                        tabIndex="1"
                        id="formUserId"
                        className="gq-input"
                        autoFocus
                        value={ userId }
                        onChange={ this.handleField }
                        onKeyPress={ this.checkEnter } />
                    </div>

                    <LoaderButton
                        fetching={ loggingIn }
                        handleClick={ this.onSubmit }
                        disabled={ !userId.length }
                        customClass="btn btn-block btn-primary form-action-btn"
                        tabIndex={ 2 }
                        done={ this.props.done }>
                    	<span dangerouslySetInnerHTML={ { __html: content[ 'system.button.submit' ] } }/>                        
                    </LoaderButton>


		</NarrowForm>
    );
  }
}


export default Activation;
