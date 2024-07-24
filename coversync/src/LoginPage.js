import React, { useState } from 'react';


import './App.css';


function LoginPage() {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleUsernameChange = (event) => {
        setUsername(event.taget.value);
    };


    const handlePasswordChange = (event) => {
        setPassword(event.target.value);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        //console.log('Login with:', username, password);

    };

    return (
        <div className = 'login-page'>
            <form onSubmit = {handleSubmit} className = "login-form">
                <div className="username-section">
                    <label className= "login-username">Username: </label>
                    <input type="text" value={username} onChange={handleUsernameChange} />  </div>
                <div className="password-section">
                    <label className= "login-password">Password: </label>
                    <input type = "password" value={password} onChange ={handlePasswordChange} />
                </div >
                 <button className ="button" type ="submit"> login</button>
            </form>
        </div>
    )

}
export default LoginPage;
