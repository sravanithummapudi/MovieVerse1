import React from 'react'
import { Navigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
// If user is authenticated:
//It renders the children — i.e., the protected component/page.
//If user is NOT authenticated:
//Redirects the user to the /login page.
function PrivateRoute({ children }) {
  const { userIsAuthenticated } = useAuth()
  return userIsAuthenticated() ? children : <Navigate to="/login" />
}

export default PrivateRoute