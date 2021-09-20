package io.github.howardjohn.lambda.http4s

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import io.github.howardjohn.lambda.ProxyEncoding._
import org.http4s._
import cats.effect.IOApp

class Http4sLambdaHandler(val service: HttpRoutes[IO]) extends Http4sLambdaHandlerK[IO] {
  def handleRequest(request: ProxyRequest): ProxyResponse =
    parseRequest(request)
      .map(runRequest)
      .flatMap(_.attempt.unsafeRunSync())
      .fold(errorResponse, identity)
}
