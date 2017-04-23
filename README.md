#Movie Service

Example micro-service application showcasing a simple http server consuming/serving JSON through http APIs

- Akka-Http for lightweght Http API
- Keeps state at MongoDb
- Example actor usage for easy distribution/clustering at later stages
- Guice for dependency injection
- API/Service/Repository layers at project structure
- Logging
- ScalaTest and Mockito at tests
- Unit tests, mocking layers at each level
- Integration tests at Repo level
- Reading separate configuration for test
- Escaping Json to Case Class binding for performance
- Play Json library for convenience
- Sbt-Revolver for quick reloads at development

## Example Usage

#####Run Service for development:

```
$ sbt 
> re-start
```

#####Register a movie:

```
$ curl -H "Content-Type: application/json" -X POST -d '{
  "imdbId": "tt0114500",
  "title": "Shawshank Redemption",
  "availableSeats": 10,
  "screenId": "screenId"
}' http://localhost:9000/movies
```
#####Reserve a movie:

```
$ curl -H "Content-Type: application/json" -X POST -d '{
  "imdbId": "tt0114500",
  "screenId": "screenId"
}' http://localhost:9000/reservations
```

#####Get info about a movie

```
$ curl http://localhost:9000/movies?imdbId=tt0114500&screenId=screenId
```
Response:
```
{
  "imdbId": "tt0114500",
  "title": "Shawshank Redemption",
  "availableSeats": 10,
  "reservedSeats": 0,
  "screenId": "screenId"
}
```

#####Testing Services
```
$ sbt test
```
