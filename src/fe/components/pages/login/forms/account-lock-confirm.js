import React from 'react';

import NarrowForm from '../../../shared/narrow-form/narrow-form';

import '../loginReact.scss';




class AccountLockConfirm extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            username: '',
            email: '',
            focusedField: ''
        };
    }
    handleConfirmLock = () => {
        const { accountLockCheck } = this.props.login;
        const LockedScreen = window.location.protocol + '//' + window.location.host + "/" + window.location.pathname.split("/")[1] + accountLockCheck + '?' + this.props.handleKeyValue;
        window.history.pushState( { path: LockedScreen }, '', LockedScreen );
        window.location.href = LockedScreen;
    }

    handleLeave = () => {
        const { redirectLock } = this.props.login;
        const userLeave = window.location.protocol + '//' + window.location.host + "/" + window.location.pathname.split("/")[1] + "/" + redirectLock;
        window.location.href = userLeave;
    }
    

  render() {
    const {
      content,
      accountLockCheck,
      redirectLock
    } = this.props,
    clientLogo = '';

    return (
        <NarrowForm page="login" clientLogo={ clientLogo } >
            <h3 className="no-bottom no-alert-bottom">{ content[ 'login.loginpage.suspicious_activity' ] }</h3>
            <span>
                <div className="locked">{ content[ 'login.loginpage.lock_safeguard' ] }</div>
                <div className="user-lock-selection">
                { content[ 'login.loginpage.user_account_lock_info' ] }
                <button type="button" className="gq-btn btn btn-danger" onClick={this.handleConfirmLock}><span>{ content[ 'login.loginpage.lock_account' ] }</span></button>
                <button type="button" className="gq-btn btn" onClick={this.handleLeave}><span>{ content[ 'login.loginpage.leave' ] }</span></button>
                </div>
            </span>

        </NarrowForm>

    );
  }
}


export default AccountLockConfirm;
