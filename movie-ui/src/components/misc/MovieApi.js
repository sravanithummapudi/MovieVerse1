import axios from 'axios'
import { config } from '../../Constants'
import { parseJwt } from './Helpers'

export const movieApi = {
  authenticate,
  signup,
  numberOfUsers,
  numberOfMovies,
  getUsers,
  deleteUser,
  getMovies,
  deleteMovie,
  addMovie
}
//Sends a POST request to authenticate a user.
function authenticate(username, password) {
  return instance.post('/auth/authenticate', { username, password }, {
    headers: { 'Content-type': 'application/json' }
  })
}
//Sends a POST request to register a new user.
function signup(user) {
  return instance.post('/auth/signup', user, {
    headers: { 'Content-type': 'application/json' }
  })
}

function numberOfUsers() {
  return instance.get('/public/numberOfUsers')
}

function numberOfMovies() {
  return instance.get('/public/numberOfMovies')
}

function getUsers(user, username) {
  const url = username ? `/api/users/${username}` : '/api/users'
  return instance.get(url, {
    headers: { 'Authorization': bearerAuth(user) }
  })
}

function deleteUser(user, username) {
  return instance.delete(`/api/users/${username}`, {
    headers: { 'Authorization': bearerAuth(user) }
  })
}

function getMovies(user, text) {
  const url = text ? `/api/movies?text=${text}` : '/api/movies'
  return instance.get(url, {
    headers: { 'Authorization': bearerAuth(user) }
  })
}

function deleteMovie(user, id) {
  return instance.delete(`/api/movies/${id}`, {
    headers: { 'Authorization': bearerAuth(user) }
  })
}

function addMovie(user, movie) {
  return instance.post('/api/movies', movie, {
    headers: {
      'Content-type': 'application/json',
      'Authorization': bearerAuth(user)
    }
  })
}

// -- Axios

const instance = axios.create({
  baseURL: config.url.API_BASE_URL
})//Creates a reusable instance with a predefined base URL.

//Checks if the JWT token is expired before sending a request. If expired, redirects to login.
instance.interceptors.request.use(function (config) {
  // If token is expired, redirect user to login
  if (config.headers.Authorization) {
    const token = config.headers.Authorization.split(' ')[1]
    const data = parseJwt(token)
    if (Date.now() > data.exp * 1000) {
      window.location.href = "/login"
    }
  }
  return config
}, function (error) {
  return Promise.reject(error)
})

// -- Helper functions

function bearerAuth(user) {
  return `Bearer ${user.accessToken}`
}//→ Attaches the JWT token to API requests for authentication.