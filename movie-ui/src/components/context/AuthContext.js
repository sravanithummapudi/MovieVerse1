import React, { createContext, useContext, useState, useEffect } from 'react'
//Creates a context to share auth-related data/functions.
const AuthContext = createContext()

function AuthProvider({ children }) {
  //Stores the currently logged-in user.
  const [user, setUser] = useState(null)

  useEffect(() => {
    const storedUser = JSON.parse(localStorage.getItem('user'))
    setUser(storedUser)//Loads the user from localStorage on initial render 
  }, [])

  const getUser = () => {//Returns the current user from localStorage.
    return JSON.parse(localStorage.getItem('user'))
  }

  const userIsAuthenticated = () => {
    //Checks if user exists in localStorage.
    let storedUser = localStorage.getItem('user')
    if (!storedUser) {
      return false
    }
    storedUser = JSON.parse(storedUser)

    // if user has token expired, logout user
    if (Date.now() > storedUser.data.exp * 1000) {
      userLogout()
      return false
    }
    return true
  }
//Saves user to localStorage.
  const userLogin = user => {
    localStorage.setItem('user', JSON.stringify(user))
    setUser(user)
  }
//Clears user from localStorage and state.
  const userLogout = () => {
    localStorage.removeItem('user')
    setUser(null)
  }

  const contextValue = {
    user,
    getUser,
    userIsAuthenticated,
    userLogin,
    userLogout,
  }

  return (
    <AuthContext.Provider value={contextValue}>
      {children}
    </AuthContext.Provider>
  )
}//{children} will render the <App /> component.

export default AuthContext

export function useAuth() {
  return useContext(AuthContext)
}

export { AuthProvider }