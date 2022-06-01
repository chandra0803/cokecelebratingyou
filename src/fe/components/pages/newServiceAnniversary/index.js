
import React from 'react';
import { render } from 'react-dom';
import SaUcTile from './SaUcTile';


export const initializeSA = () => {
    render(
        <SaUcTile />
        ,
        document.getElementById( 'sa-app' )
    );
};

window.initializeSA = initializeSA;