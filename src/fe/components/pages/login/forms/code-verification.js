import React from 'react';
import classNames from 'classnames';

import LoaderButton from '../../../shared/loader-button/loader-button';
import NarrowForm from '../../../shared/narrow-form/narrow-form';

import { Alert } from 'component-library';
import '../loginReact.scss';




class CodeVerification extends React.Component {

    constructor( props ) {
        super( props );
        const token = new RegExp( '[\?&]userToken=([^&#]*)' ).exec( this.getLocation() );
		const activation = new RegExp( '[\?&]activation=([^&#]*)' ).exec( this.getLocation() );
        if( token && token[ 1 ] ) {
            if( activation && activation[ 1 ] ) {
                this.state = {
                    activation: true,
                    token: token[ 1 ],
                    hasToken: true,
                    focusedField: '',
                    display: false,
                    fromEmail: true
                };
            } else {

                this.state = {
					activation: false,
                    token: token[ 1 ],
                    hasToken: true,
                    focusedField: '',
                    display: false,
                    fromEmail: true
                };
            }

        } else {
          if( activation && activation[ 1 ] ) {
              this.state = {
                  activation: true,
                  token: '',
                  hasToken: false,
                  focusedField: '',
                  display: true,
                  fromEmail: false
              };
          } else {
            this.state = {
                activation: false,
                token: '',
                hasToken: false,
                focusedField: '',
                display: true,
                fromEmail: false
            };
          }
        }
        this.termedUseronSubmit = this.termedUseronSubmit.bind( this );
    }
    getLocation = () => {
        if( window.location.url ) {
            return window.location.url;
        } else {
            return window.location.href;
        }
    }
    componentWillMount() {
        if ( this.props.login.message && !this.state.display ) {
            this.setState( { display: true } );
        } else if ( !this.state.display ) {
                this.onSubmit();
        }
        if( !this.props.passConfirm.passwordRules ) {
            const { pwRulesUrl } = this.props.login;
            this.props.fetchPasswordRules( pwRulesUrl );
        }

    }
    componentWillUpdate( nextProps, nextState ) {
        if ( nextProps.login.message && !this.state.display ) {
            this.setState( { display: true } );
        }

    }
    componentWillReceiveProps( nextProps ) {
        if( nextProps.login.message && nextProps.login.message.error ) {
            this.setState( { token: '' } );
        }
    }
    handleField = ( event ) => {
        const name = event.target.name;
        this.setState( { [ name ]: event.target.value } );
    }

    onSubmit = ( ) => {
        const activation = this.state.activation ? true : false;
        const { activCodeVerifUrl } = this.props.login;
        this.props.sendToken( { token: this.state.token, fromEmail: this.state.fromEmail }, activCodeVerifUrl, activation );
    }

    checkEnter = ( event ) => {
        const enterPressed = event.charCode === 13;


        if ( enterPressed ) {
            event.preventDefault();
            this.onSubmit();
        }
    }

    termedUseronSubmit = ( e ) => {
        const user = false;
        const { activContMethUrl } = this.props.login;
        const contact = {
        contactId: window.login.termedUserId,
          contactType: 'NONE'
        };
        this.props.sendContact( contact, activContMethUrl, user );
      }

  render() {

    const {
        content
    } = this.props;
    const {
        token,
        display,
		activation
    } = this.state;
    const {
        message,
        toggleModal
    } = this.props.login;

    if( message && message.error && activation ) {
        message.error = message.error.template( { linkOpen: '<a href="login.do?activationPage=true" >', linkClose: '</a>' } );
        const newurl = window.location.protocol + '//' + window.location.host + window.location.pathname;
        window.history.pushState( { path: newurl }, '', newurl );
    } else if( message && message.error ) {
        message.error = message.error.template( { linkOpen: '<a href="login.do?forgotPass=true" >', linkClose: '</a>' } );
        const newurl = window.location.protocol + '//' + window.location.host + window.location.pathname;
        window.history.pushState( { path: newurl }, '', newurl );
    }

    const onetimeCodeClasses = classNames( {
        'input-wrap': true,
        'focused': true,
        'code-verification-input': true
    } );

    const clientLogo = '';
    const loginErrors = [];
    if( display === false ) {
        return null;
    }
    return (

		<NarrowForm page="login" clientLogo={ clientLogo } errorsExist={ loginErrors.length > 0 }>
            <p>
                <a tabIndex="3" onClick={ ( e ) => this.props.handleFormChange( 'login' ) }>
                    <span><i className="icon-arrow-1-circle-left"></i> { content[ 'login.loginpage.back_to_login' ] }</span>
                </a>
            </p>
            {!message &&
                <h3 className="no-bottom" dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.code_verification' ] } }/>
            }
            {message &&
                !message.error &&
                <h3 className="no-bottom" dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.code_verification' ] } }/>
            }
            {message &&
                message.error &&
                <h3 className="no-bottom" dangerouslySetInnerHTML={ { __html: content[ 'login.password.reset.reset_your_password' ] } }/>
            }
            <span className="code-verification-alert">
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
            </span>
			<div className={ onetimeCodeClasses }>
                <label htmlFor="token" className="control-label code-link-label">{ content[ 'login.loginpage.one_time_code' ] }</label>
                <span className="moreInfo">
                  <span className="userId" onClick={ ( e ) => this.props.toggleModalDisplay( !toggleModal, 'noCode' ) }>
                    <span className="icon-info"></span>
                  </span>
                  { window.login.termedUserId !== null &&
                  <a className="noneWorks" onClick={ this.termedUseronSubmit }>
                    <span>{ content[ 'login.forgotpwd.didnt_get_it' ] }</span>
                  </a> }
                  { window.login.termedUserId === null &&
                    <span>{ content[ 'login.forgotpwd.didnt_get_it' ] }</span>
                  }
                </span>
                    <input
                    type="text"
                    name="token"
                    tabIndex="1"
                    id="token"
                    autoFocus
                    className="gq-input"
                    value={ token }
                    onChange={ this.handleField }
                    onKeyPress={ this.checkEnter } />
            </div>


			<LoaderButton
				handleClick={ this.onSubmit }
				disabled={ !token.length ? true : false }
				customClass="btn btn-block btn-primary form-action-btn"
				tabIndex={ 2 }
				done={ this.props.done }>
				{ content[ 'system.button.submit' ] }
			</LoaderButton>
            <p className="help">
                <a tabIndex="3" onClick={ ( e ) => this.props.toggleModalDisplay( !toggleModal, 'help' ) }>
                    <span>{ content[ 'login.loginpage.help' ] }</span>
                </a>
            </p>
		</NarrowForm>

    );
  }
}


export default CodeVerification;
