package io.github.howardjohn.lambda.http4s.example

import cats.effect.IO
import io.github.howardjohn.lambda.http4s.Http4sLambdaHandler
import org.http4s.HttpRoutes
import org.http4s.dsl.io._

object Route {
  // Set up the route
  val service: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name => Ok(s"Hello, $name!")
  }

  // Define the entry point for Lambda
  class EntryPoint extends Http4sLambdaHandler(service)
}
