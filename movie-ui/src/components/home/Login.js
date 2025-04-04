import React, { useState } from 'react'
import { NavLink, Navigate } from 'react-router-dom'
import { Button, Form, Grid, Icon, Segment, Menu, Message, Divider } from 'semantic-ui-react'
import { useAuth } from '../context/AuthContext'
import { movieApi } from '../misc/MovieApi'
import { parseJwt, getSocialLoginUrl, handleLogError } from '../misc/Helpers'

function Login() {
  const Auth = useAuth()
  const isLoggedIn = Auth.userIsAuthenticated()
  //Checks if user is already logged in (Auth.userIsAuthenticated())

  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [isError, setIsError] = useState(false)//true if login fails, false otherwise.

  const handleInputChange = (e, { name, value }) => {
    if (name === 'username') {
      setUsername(value)//Updates state when user types in the login form.
    } else if (name === 'password') {
      setPassword(value)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()

    if (!(username && password)) {
      setIsError(true)
      return
    }//If username or password is empty, it sets isError = true.

    try {
      const response = await movieApi.authenticate(username, password)//sends credentials to backend.
      const { accessToken } = response.data//Extracts accessToken from API response.
      const data = parseJwt(accessToken)//Decodes token to get user details.
      const authenticatedUser = { data, accessToken }//Stores user session

      Auth.userLogin(authenticatedUser)

      setUsername('')
      setPassword('')
      setIsError(false)
    } catch (error) {
      handleLogError(error)
      setIsError(true)
    }
  }

  if (isLoggedIn) {
    return <Navigate to='/' />
  }//If true, redirects the user to the home page (/).

  return (
    <Grid textAlign='center'>
      <Grid.Column style={{ maxWidth: 450 }}>
        <Form size='large' onSubmit={handleSubmit}>
          <Segment>
            <Form.Input
              fluid
              autoFocus
              name='username'
              icon='user'
              iconPosition='left'
              placeholder='Username'
              onChange={handleInputChange}
            />
            <Form.Input
              fluid
              name='password'
              icon='lock'
              iconPosition='left'
              placeholder='Password'
              type='password'
              onChange={handleInputChange}
            />
            <Button color='purple' fluid size='large'>Login</Button>
          </Segment>
        </Form>
        <Message>{`Don't have already an account? `}
          <NavLink to="/signup" color='purple'>Sign Up</NavLink>
        </Message>
        {isError && <Message negative>The username or password provided are incorrect!</Message>}

        <Divider horizontal>or connect with</Divider>

        <Menu compact icon='labeled'>
          <Menu.Item name='github' href={getSocialLoginUrl('github')}>
            <Icon name='github' />Github
          </Menu.Item>
          <Menu.Item name='google' href={getSocialLoginUrl('google')}>
            <Icon name='google' />Google
          </Menu.Item>
          <Menu.Item name='facebook'>
            <Icon name='facebook' disabled />Facebook
          </Menu.Item>
          <Menu.Item name='instagram'>
            <Icon name='instagram' disabled />Instagram
          </Menu.Item>
        </Menu>
      </Grid.Column>
    </Grid>
  )
}

export default Login
