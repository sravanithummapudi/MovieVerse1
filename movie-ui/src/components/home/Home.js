import React, { useState, useEffect } from 'react'
import { Statistic, Icon, Grid, Container, Image, Segment, Dimmer, Loader } from 'semantic-ui-react'
import { movieApi } from '../misc/MovieApi'
import { handleLogError } from '../misc/Helpers'

function Home() {
  const [numberOfUsers, setNumberOfUsers] = useState(0)
  const [numberOfMovies, setNumberOfMovies] = useState(0)
  //isLoading: true while fetching data, false after API response.
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    async function fetchData() {
      setIsLoading(true)
      try {
        let response = await movieApi.numberOfUsers()
        const users = response.data

        response = await movieApi.numberOfMovies()
        const movies = response.data

        setNumberOfUsers(users)//Stores API results
        setNumberOfMovies(movies)
      } catch (error) {
        handleLogError(error)//Handles errors using handleLogError().
      } finally {
        setIsLoading(false)//Stops loading after fetching data.
      }
    }
    fetchData()
  }, [])

  if (isLoading) {
    return (
      <Segment basic style={{ marginTop: window.innerHeight / 2 }}>
        <Dimmer active inverted>
          <Loader inverted size='huge'>Loading</Loader>
        </Dimmer>
      </Segment>
    )
  }

  return (
    //two statistics in a grid layout.
    //Each statistic shows:
    //  Icon (👤 for users, 💻 for movies)
//Value (numberOfUsers, numberOfMovies)
// Label (Users, Movies)

    <Container text>
      <Grid stackable columns={2}>
        <Grid.Row>
          <Grid.Column textAlign='center'>
            <Segment color='purple'>
              <Statistic>
                <Statistic.Value><Icon name='user' color='grey' />{numberOfUsers}</Statistic.Value>
                <Statistic.Label>Users</Statistic.Label>
              </Statistic>
            </Segment>
          </Grid.Column>
          <Grid.Column textAlign='center'>
            <Segment color='purple'>
              <Statistic>
                <Statistic.Value><Icon name='laptop' color='grey' />{numberOfMovies}</Statistic.Value>
                <Statistic.Label>Movies</Statistic.Label>
              </Statistic>
            </Segment>
          </Grid.Column>
        </Grid.Row>
      </Grid>
/
      <Image src='https://react.semantic-ui.com/images/wireframe/media-paragraph.png' style={{ marginTop: '2em' }} />
      <Image src='https://react.semantic-ui.com/images/wireframe/paragraph.png' style={{ marginTop: '2em' }} />
    </Container>
    //placeholders for paragraphs 
  )
}

export default Home