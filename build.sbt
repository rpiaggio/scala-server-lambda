lazy val Scala212Version = "2.12.13"
lazy val Scala213Version = "2.13.6"

def scalacVersionOptions(scalaVersion: String) =
  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, 12)) => Seq("-Ypartial-unification")
    case _ => Nil
  }

lazy val commonSettings = Seq(
  organization := "io.github.howardjohn",
  scalaVersion := Scala212Version,
  crossScalaVersions := Seq(Scala212Version, Scala213Version),
  version := "0.4.2-SNAPSHOT"
)

lazy val root = project
  .in(file("."))
  .settings(commonSettings)
  .settings(noPublishSettings)
  .aggregate(common, tests, http4s, exampleHttp4s)

lazy val CirceVersion = "0.14.1"
lazy val ScalaTestVersion = "3.1.1"
lazy val Http4sVersion = "0.23.3"

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
        "org.scalatest" %% "scalatest" % ScalaTestVersion % "test"
      )
  )

lazy val tests = project
  .in(file("tests"))
  .settings(commonSettings)
  .settings(publishSettings)
  .settings(
    moduleName := "scala-server-lambda-tests",
    libraryDependencies ++=
      Seq(
        "org.scalatest" %% "scalatest" % ScalaTestVersion
      )
  )
  .dependsOn(common)

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
        "org.scalatest" %% "scalatest" % ScalaTestVersion % "test",
        "org.http4s" %% "http4s-dsl" % Http4sVersion % "test",
        "org.http4s" %% "http4s-circe" % Http4sVersion % "test"
      )
    }
  )
  .dependsOn(common)
  .dependsOn(tests % "test")


lazy val exampleHttp4s = project
  .in(file("example-http4s"))
  .settings(noPublishSettings)
  .settings(commonSettings)
  .settings(
    moduleName := "example-http4s",
    assembly / assemblyJarName := "example-http4s.jar",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % Http4sVersion
    )
  )
  .dependsOn(http4s)

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
    )
  ),
  developers := List(
    Developer(
      id = "howardjohn",
      name = "John Howard",
      email = "johnbhoward96@gmail.com",
      url = url("https://github.com/howardjohn/")
    )
  ),
  publishMavenStyle := true,
  Test / publishArtifact  := false,
  pomIncludeRepository := { _ => false },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }
)
