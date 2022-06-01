import React from 'react';
import PropTypes from 'prop-types';
import {
    EyeOpenIcon,
    EyeClosedIcon,
} from '../icons/icons.js';

import './password-input.scss';

class PasswordInput extends React.Component {
    static propTypes = {
        label: PropTypes.oneOfType( [
            PropTypes.string,
            PropTypes.object
        ] ),
        name: PropTypes.string,
        tabIndex: PropTypes.oneOfType( [
            PropTypes.string,
            PropTypes.number
        ] ),
        onChange: PropTypes.func,
        onKeyPress: PropTypes.func,
        onFocus: PropTypes.func,
        onBlur: PropTypes.func,
        helpComponent: PropTypes.object
    };

    constructor( props ) {
        super( props );

        this.state = {
            passwordVisible: true
        };
    }

    togglePasswordVisible = () => {
        this.setState( {
            passwordVisible: !this.state.passwordVisible
        } );
    };

    render() {
        const { passwordVisible } = this.state;
        const {
            label,
            name,
            value,
            disabled,
            tabIndex,
            onChange,
            onKeyPress,
            onFocus,
            onBlur,
            helpComponent,
            autoFocus
        } = this.props;

        const pwdInputOpts = {
            type: passwordVisible ? 'text' : 'password',
            name,
            value,
            tabIndex,
            disabled,
            onChange,
            onKeyPress,
            onFocus,
            onBlur,
            autoFocus
        };

        return (
            <div className="password-input">
                <div className="input-wrap">
                    { helpComponent }
                    <label htmlFor="password1" className="control-label">{ label }</label>
                    <span className="eye" onClick={ this.togglePasswordVisible }>
                        { passwordVisible ? <EyeOpenIcon/> : <EyeClosedIcon/> }
                    </span>
                    <input { ...pwdInputOpts } className="gq-input" />
                </div>
            </div>
        );
    }
}

export default PasswordInput;
