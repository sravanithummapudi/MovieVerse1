import React from 'react'
import { Tab } from 'semantic-ui-react'
import UserTable from './UserTable'
import MovieTable from './MovieTable'
//AdminTab component displays two tabs: one for Users and one for Movies
function AdminTab(props) {
  const { handleInputChange } = props
  const { isUsersLoading, users, userUsernameSearch, handleDeleteUser, handleSearchUser } = props
  const { isMoviesLoading, movies, movieImdb, movieTitle, moviePoster, movieTextSearch, handleAddMovie, handleDeleteMovie, handleSearchMovie } = props

  const panes = [
    {
      menuItem: { key: 'users', icon: 'users', content: 'Users' },
      render: () => (
        <Tab.Pane loading={isUsersLoading}>
          <UserTable
            users={users}
            userUsernameSearch={userUsernameSearch}
            handleInputChange={handleInputChange}
            handleDeleteUser={handleDeleteUser}
            handleSearchUser={handleSearchUser}
          />
        </Tab.Pane>
      )
    },
    {
      /*isMoviesLoading: Boolean – shows loading state
movies: List of movie objects
movieImdb, movieTitle, moviePoster: Controlled values for adding a movie
movieTextSearch: Controlled value for movie search
handleAddMovie: Submits a new movie
handleDeleteMovie: Deletes a movie
handleSearchMovie: Triggers movie search*/

      menuItem: { key: 'movies', icon: 'video camera', content: 'Movies' },
      render: () => (
        <Tab.Pane loading={isMoviesLoading}>
          <MovieTable
            movies={movies}
            movieImdb={movieImdb}
            movieTitle={movieTitle}
            moviePoster={moviePoster}
            movieTextSearch={movieTextSearch}
            handleInputChange={handleInputChange}
            handleAddMovie={handleAddMovie}
            handleDeleteMovie={handleDeleteMovie}
            handleSearchMovie={handleSearchMovie}
          />
        </Tab.Pane>
      )
    }
  ]

  return (
    <Tab menu={{ attached: 'top' }} panes={panes} />
  )
}

export default AdminTab