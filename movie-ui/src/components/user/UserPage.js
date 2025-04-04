import React, { useEffect, useState } from 'react'
import { Navigate } from 'react-router-dom'
import { Container } from 'semantic-ui-react'
import MovieList from './MovieList'
import { useAuth } from '../context/AuthContext'
import { movieApi } from '../misc/MovieApi'
import { handleLogError } from '../misc/Helpers'

function UserPage() {
  const Auth = useAuth()
  const user = Auth.getUser()
  const isUser = user.data.rol[0] === 'USER'

  const [movies, setMovies] = useState([])
  const [movieTextSearch, setMovieTextSearch] = useState('')
  //Stores the text input for searching movies.
  const [isMoviesLoading, setIsMoviesLoading] = useState(false)
  //Controls the loading state while fetching movies.

  useEffect(() => {
    handleGetMovies()
  }, [])//to fetch all movies.

  const handleInputChange = (e, { name, value }) => {
    if (name === 'movieTextSearch') {
      setMovieTextSearch(value)
    }
  }//Updates the movieTextSearch state when the user types in the search input.

  const handleGetMovies = async () => {
    setIsMoviesLoading(true)
    try {
      const response = await movieApi.getMovies(user)
      //Calls movieApi.getMovies(user) to get movies from the API.
      setMovies(response.data)//Sets movies state with the fetched data.
    } catch (error) {
      handleLogError(error)
    } finally {
      setIsMoviesLoading(false)
    }
  }
//Search Movies Based on User Input
  const handleSearchMovie = async () => {
    try {
      //to fetch movies based on user search.
      const response = await movieApi.getMovies(user, movieTextSearch)
      const movies = response.data
      setMovies(movies)
    } catch (error) {
      handleLogError(error)
      setMovies([])
    }
  }
  //If the user does not have the "USER" role, they are redirected to the home page using:

 
  
  if (!isUser) {
    return <Navigate to='/' />
  }

  return (
    // to display the fetched movies.
    <Container>
      <MovieList
        isMoviesLoading={isMoviesLoading}
        movieTextSearch={movieTextSearch}
        movies={movies}
        handleInputChange={handleInputChange}
        handleSearchMovie={handleSearchMovie}
      />
    </Container>
  )
}

export default UserPage