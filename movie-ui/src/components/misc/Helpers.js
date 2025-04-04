import { config } from '../../Constants'

//Takes a JWT access token as input.
//Extracts the payload (middle part of the token: header.payload.signature).
//Decodes it from Base64 to JSON.
//Returns the decoded payload (which contains user details like roles, expiration, etc.).
export function parseJwt(token) {
  if (!token) { return }
  const base64Url = token.split('.')[1]
  const base64 = base64Url.replace('-', '+').replace('_', '/')
  return JSON.parse(window.atob(base64))
}

export function getSocialLoginUrl(name) {
  return `${config.url.API_BASE_URL}/oauth2/authorization/${name}?redirect_uri=${config.url.OAUTH2_REDIRECT_URI}`
}//Redirects the user to config.url.OAUTH2_REDIRECT_URI after authentication.

export const handleLogError = (error) => {
  if (error.response) {
    console.log(error.response.data)//Logs response data when API returns an error
  } else if (error.request) {
    console.log(error.request)// Logs request object if no response is received.
  } else {
    console.log(error.message)//	Logs API errors for debugging.
  }
}