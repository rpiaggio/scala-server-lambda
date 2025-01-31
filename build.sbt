// lazy val Scala212Version = "2.12.10"
// lazy val Scala213Version = "2.13.1"
lazy val Scala3Version = "3.6.3"

// def scalacVersionOptions(scalaVersion: String) =
//   CrossVersion.partialVersion(scalaVersion) match {
//     case Some((2, 12)) => Seq("-Ypartial-unification")
//     case _ => Nil
//   }

lazy val commonSettings = Seq(
  organization := "io.github.howardjohn",
  scalaVersion := Scala3Version,
  // crossScalaVersions := Seq(Scala212Version, Scala213Version),
  version := "0.4.2-SNAPSHOT"
)

lazy val root = project
  .in(file("."))
  .settings(commonSettings)
  .settings(noPublishSettings)
  .aggregate(common, tests, http4s, http4sZio, akka, exampleHttp4s, exampleAkka)

lazy val CirceVersion = "0.14.10"
// lazy val ScalaTestVersion = "3.1.1"
lazy val Http4sVersion = "0.23.30"

lazy val common = project
  .in(file("common"))
  .settings(commonSettings)
  .settings(publishSettings)
  .settings(
    moduleName := "scala-server-lambda-common",
    libraryDependencies ++=
      Seq(
        "io.circe" %% "circe-generic" % CirceVersion,
        "io.circe" %% "circe-parser" % CirceVersion,
        // "org.scalatest" %% "scalatest" % ScalaTestVersion % "test"
      )
  )

// lazy val tests = project
//   .in(file("tests"))
//   .settings(commonSettings)
//   .settings(publishSettings)
//   .settings(
//     moduleName := "scala-server-lambda-tests",
//     libraryDependencies ++=
//       Seq(
//         "org.scalatest" %% "scalatest" % ScalaTestVersion
//       )
//   )
//   .dependsOn(common)

lazy val http4s = project
  .in(file("http4s-lambda"))
  .settings(publishSettings)
  .settings(commonSettings)
  .settings(
    name := "http4s-lambda",
    moduleName := "http4s-lambda",
    scalacOptions ++= scalacVersionOptions(scalaVersion.value),
    libraryDependencies ++= {
      Seq(
        "org.http4s" %% "http4s-core" % Http4sVersion,
        // "org.scalatest" %% "scalatest" % ScalaTestVersion % "test",
        "org.http4s" %% "http4s-dsl" % Http4sVersion % "test",
        "org.http4s" %% "http4s-circe" % Http4sVersion % "test"
      )
    }
  )
  .dependsOn(common)
  .dependsOn(tests % "test")

// lazy val http4sZio = project
//   .in(file("http4s-lambda-zio"))
//   .settings(publishSettings)
//   .settings(commonSettings)
//   .settings(
//     name := "http4s-lambda-zio",
//     moduleName := "http4s-lambda-zio",
//     scalacOptions ++= scalacVersionOptions(scalaVersion.value),
//     libraryDependencies ++= {
//       Seq(
//         "org.http4s" %% "http4s-core" % Http4sVersion,
//         "org.scalatest" %% "scalatest" % ScalaTestVersion % "test",
//         "org.http4s" %% "http4s-dsl" % Http4sVersion % "test",
//         "org.http4s" %% "http4s-circe" % Http4sVersion % "test",
//         "dev.zio" %% "zio" % "1.0.0-RC14",
//         "dev.zio" %% "zio-interop-cats" % "2.0.0.0-RC5"
//       )
//     }
//   )
//   .dependsOn(common)
//   .dependsOn(tests % "test")
//   .dependsOn(http4s % "test->test;compile->compile")

// lazy val akka = project
//   .in(file("akka-http-lambda"))
//   .settings(publishSettings)
//   .settings(commonSettings)
//   .settings(
//     name := "akka-http-lambda",
//     moduleName := "akka-http-lambda",
//     scalacOptions ++= scalacVersionOptions(scalaVersion.value),
//     libraryDependencies ++= {
//       Seq(
//         "com.typesafe.akka" %% "akka-http" % "10.1.10",
//         "com.typesafe.akka" %% "akka-stream" % "2.5.26",
//         "org.scalatest" %% "scalatest" % ScalaTestVersion % "test"
//       )
//     }
//   )
//   .dependsOn(common)
//   .dependsOn(tests % "test")

// lazy val exampleHttp4s = project
//   .in(file("example-http4s"))
//   .settings(noPublishSettings)
//   .settings(commonSettings)
//   .settings(
//     moduleName := "example-http4s",
//     assemblyJarName in assembly := "example-http4s.jar",
//     libraryDependencies ++= Seq(
//       "org.http4s" %% "http4s-dsl" % Http4sVersion
//     )
//   )
//   .dependsOn(http4s)

// lazy val exampleAkka = project
//   .in(file("example-akka-http"))
//   .settings(noPublishSettings)
//   .settings(commonSettings)
//   .settings(
//     moduleName := "example-akka-http",
//     assemblyJarName in assembly := "example-akka-http.jar",
//     libraryDependencies ++= Seq(
//       "com.typesafe.akka" %% "akka-http" % "10.1.10",
//       "com.typesafe.akka" %% "akka-stream" % "2.5.26"
//     )
//   )
//   .dependsOn(akka)

lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

lazy val publishSettings = Seq(
  homepage := Some(url("https://github.com/howardjohn/scala-server-lambda")),
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/howardjohn/scala-server-lambda"),
      "scm:git@github.com:howardjohn/scala-server-lambda.git"
    )),
  developers := List(
    Developer(
      id = "howardjohn",
      name = "John Howard",
      email = "johnbhoward96@gmail.com",
      url = url("https://github.com/howardjohn/")
    )
  ),
  credentials += Credentials(Path.userHome / ".sbt" / ".credentials"),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ =>
    false
  },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }
)
