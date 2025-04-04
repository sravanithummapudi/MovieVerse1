import React from 'react'
import { Form, Icon, Button } from 'semantic-ui-react'
/*prop	Type	Purpose
movieImdb	string	Controlled input for IMDB
movieTitle	string	Controlled input for Title
moviePoster	string	Controlled input for Poster
handleInputChange	function	Updates form input values
handleAddMovie	function	Submits the form to add a movie*/  

function MovieForm({ movieImdb, movieTitle, moviePoster, handleInputChange, handleAddMovie }) {
  //Disables the Create button unless both IMDB and Title are filled in.
  const createBtnDisabled = movieImdb.trim() === '' || movieTitle.trim() === ''
  return (
    <Form onSubmit={handleAddMovie}>
      <Form.Group>
        <Form.Input
          name='movieImdb'
          placeholder='IMDB *'
          value={movieImdb}
          onChange={handleInputChange}
        />
        <Form.Input
          name='movieTitle'
          placeholder='Title *'
          value={movieTitle}
          onChange={handleInputChange}
        />
        <Form.Input
          name='moviePoster'
          placeholder='Poster'
          value={moviePoster}
          onChange={handleInputChange}
        />
        <Button icon labelPosition='right' disabled={createBtnDisabled}>
          Create<Icon name='add' />
        </Button>
      </Form.Group>
    </Form>
  )
}

export default MovieForm
