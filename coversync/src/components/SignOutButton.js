// src/components/SignOutButton.js
import React from 'react';
import './SignOutButton.css';

const SignOutButton = ({ onSignOut }) => {
  return (
    <button className="sign-out-button" onClick={onSignOut}>
      Sign Out
    </button>
  );
};

export default SignOutButton;
