import React from 'react';

import NarrowForm from '../../../shared/narrow-form/narrow-form';

import '../loginReact.scss';




class AccountLock extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            username: '',
            email: '',
            focusedField: ''
        };
    }
    goForgotPassword = () => {
        this.props.changeFormFunc( 'forgotPassword' );
    }

  render() {
    const {
      content, userOpted, userFromEmail
    } = this.props;
    const {
        toggleModal
    } = this.props.login;
    const clientLogo = '';

    return (
        <NarrowForm page="login" clientLogo={ clientLogo } >
         { userFromEmail === false && userOpted === false &&
            <p >
                <a tabIndex="5" onClick={ ( e ) => this.props.handleFormChange( 'login' ) }>
                    <span><i className="icon-arrow-1-circle-left"></i> { content[ 'login.loginpage.back_to_login' ] }</span>
                </a>
            </p>
        }
            <h3 className="no-bottom no-alert-bottom">{ content[ 'login.loginpage.account_locked' ] }</h3>
            <span>
                {userOpted === true &&
                    <div className="locked account-lock-chosen">{ content[ 'login.loginpage.account_lock_chosen' ] }</div>
                }
                {userOpted === false &&
                    <div className="locked account-locked-already">{ content[ 'login.loginpage.account_locked_already' ] }</div>
                }
                <div className="locked" dangerouslySetInnerHTML={ { __html: content[ 'login.loginpage.contact_adminstrator' ] } }></div>            
                <div className="locked">{ content[ 'login.loginpage.after_account_unlock' ] }</div>
                <ol>
                <li>{ content[ 'login.loginpage.set_secure_pwd' ] } <a href="javascript:void(0)" onClick={this.goForgotPassword}>{ content[ 'login.loginpage.account_lock_password' ] }</a></li>
                <li>{ content[ 'login.loginpage.security_settings' ] }</li>
                <li>{ content[ 'login.loginpage.signs_suspicious_activity' ] }</li>
                </ol>                
                {userOpted === true &&
                    <div className="locked thanks-msg">{ content[ 'login.loginpage.thank_you_vigilant' ] }</div> 
                }
            </span>
        </NarrowForm>
    );
  }
}


export default AccountLock;
