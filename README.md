# MovieVerse
A full-stack web application to manage movies using a Spring Boot backend (`movie-api`) and a React frontend (`movie-ui`). The application supports user authentication, role-based access, and social login using GitHub and Google OAuth2.
# Tech Stack

- **Frontend**: React, Semantic UI React  
- **Backend**: Spring Boot, OAuth2, JWT  
- **Database**: PostgreSQL  
- **Authentication**: GitHub OAuth2, Google OAuth2 
- **API Docs**: Swagger

# Endpoint Summary ( movie-api)
  | Endpoint                                                      | Secured | Roles           |
  | ------------------------------------------------------------- | ------- | --------------- |
  | `POST /auth/authenticate -d {"username","password"}`          | No      |                 |
  | `POST /auth/signup -d {"username","password","name","email"}` | No      |                 |
  | `GET /public/numberOfUsers`                                   | No      |                 |
  | `GET /public/numberOfMovies`                                  | No      |                 |
  | `GET /api/users/me`                                           | Yes     | `ADMIN`, `USER` |
  | `GET /api/users`                                              | Yes     | `ADMIN`         |
  | `GET /api/users/{username}`                                   | Yes     | `ADMIN`         |
  | `DELETE /api/users/{username}`                                | Yes     | `ADMIN`         |
  | `GET /api/movies [?text]`                                     | Yes     | `ADMIN`, `USER` |
  | `POST /api/movies -d {"imdb","description"}`                  | Yes     | `ADMIN`         |
  | `DELETE /api/movies/{imdb}`                                   | Yes     | `ADMIN`         |


# Frontend (movie-ui)
Built using React and Semantic UI.
Supports GitHub & Google Oauth2 and direct login.
JWT token is used for all secured API requests.
Role-based UI:
USER: Can view movies.
ADMIN: Can view, create, and delete movies and users.

# Application Url
Application	URL	Credentials
movie-api	http://localhost:8080/swagger-ui.html	
movie-ui	http://localhost:3000	admin/admin, user/user, or signing up a new user

# Testing the Application
## Manual Testing:
Access movie-ui at http://localhost:3000, Login using GitHub or google credentials.

## Automatic Endpoints Test:
Open a terminal and make sure you are in the root folder.
Run the following script: ./movie-api/test-endpoints.sh
It should return something like the output below, where it shows the http code for different requests:
   Endpoints | without token |  user token |  admin token |
    ------------------------- + ------------- + ----------- + ------------ |
     GET public/numberOfUsers |           200 |         200 |          200 |
    GET public/numberOfMovies |           200 |         200 |          200 |
    ......................... + ............. + ........... + ............ |
            GET /api/users/me |           401 |         200 |          200 |
               GET /api/users |           401 |         403 |          200 |
         GET /api/users/user2 |           401 |         403 |          200 |
      DELETE /api/users/user2 |           401 |         403 |          200 |
    ......................... + ............. + ........... + ............ |
              GET /api/movies |           401 |         200 |          200 |
             POST /api/movies |           401 |         403 |          201 |
       DELETE /api/movies/abc |           401 |         403 |          200 |
    ------------------------------------------------------------------------
     [200] Success -  [201] Created -  [401] Unauthorized -  [403] Forbidden







