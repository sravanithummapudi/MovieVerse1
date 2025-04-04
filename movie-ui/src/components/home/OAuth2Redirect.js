import React, { useContext, useEffect, useState } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import AuthContext from '../context/AuthContext'
import { parseJwt } from '../misc/Helpers'

function OAuth2Redirect() {
  const { userLogin } = useContext(AuthContext)//Accesses userLogin to store the authenticated user.
  const [redirectTo, setRedirectTo] = useState('/login')// Initially redirects to /login (in case OAuth fails).

  const location = useLocation()

  useEffect(() => {
    const accessToken = extractUrlParameter('token')//Extracts the OAuth token from the URL using extractUrlParameter('token').
    if (accessToken) {
      handleLogin(accessToken)
      const redirect = '/'
      setRedirectTo(redirect)//If a token exists, calls handleLogin(accessToken) and redirects to '/' (home page).
    }
  }, [])

  const extractUrlParameter = (key) => {
    return new URLSearchParams(location.search).get(key)
  }//ses URLSearchParams to get the OAuth token from the URL.

  const handleLogin = (accessToken) => {
    const data = parseJwt(accessToken)//Decodes JWT (parseJwt(accessToken)) to extract user data.
    const user = { data, accessToken }

    userLogin(user)//Stores user session in the authentication context
  };

  return <Navigate to={redirectTo} />
}

export default OAuth2Redirect