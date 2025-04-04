import React from 'react'
////using predefined css classes and react components (semantic ui)
import { Grid, Header, Form, Icon, Image, Input, Item, Segment } from 'semantic-ui-react'
// Generating Movie List
//If movies array is empty, it shows "No Movie".
//If movies exist, it maps through the list and displays:
 //Movie Poster (movie.poster)
 //Movie Title (movie.title)
//IMDB ID (movie.imdb)
//Placeholder description image

function MovieList({ isMoviesLoading, movieTextSearch, movies, handleInputChange, handleSearchMovie }) {
  let movieList
  if (movies.length === 0) {
    movieList = <Item key='no-movie'>No Movie</Item>
  } else {
    movieList = movies.map(movie => {
      return (
        <Item key={movie.imdb}>
          <Image src={movie.poster} size='small' bordered rounded />
          <Item.Content>
            <Item.Header>{movie.title}</Item.Header>
            <Item.Meta>{movie.imdb}</Item.Meta>
            <Item.Description>
              <Image src='https://react.semantic-ui.com/images/wireframe/short-paragraph.png' />
            </Item.Description>
          </Item.Content>
        </Item>
      )
    })
  }
//Displaying the Search Bar and Movies

  return (
    <Segment loading={isMoviesLoading} color='purple'>
      <Grid stackable divided>
        <Grid.Row columns='2'>
          <Grid.Column width='3'>
            <Header as='h2'>
              <Icon name='video camera' />
              <Header.Content>Movies</Header.Content>
            </Header>
          </Grid.Column>
          <Grid.Column>
            <Form onSubmit={handleSearchMovie}>
              <Input
                action={{ icon: 'search' }}
                name='movieTextSearch'
                placeholder='Search by IMDB or Title'
                value={movieTextSearch}
                onChange={handleInputChange}
              />
            </Form>
          </Grid.Column>
        </Grid.Row>
      </Grid>
      <Item.Group divided unstackable relaxed link>
        {movieList}
      </Item.Group>
    </Segment>
  )
}

export default MovieList