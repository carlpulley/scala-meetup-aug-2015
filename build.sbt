import sbt._
import Keys._

Defaults.defaultConfigs ++ Seq(
    organization := "cakesolutions",
    version := "0.1.0-SNAPSHOT",
    resolvers := Seq(
      Resolver.defaultLocal,
      Resolver.mavenLocal,
      Resolver.typesafeRepo("releases"),
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots")
    ),
    scalaVersion := "2.11.7",
    scalacOptions ++= Seq(
      "-encoding", "UTF-8",
      "-deprecation",
      "-unchecked",
      "-Ywarn-dead-code",
      "-feature"
    ),
    javacOptions ++= Seq(
      "-Xlint:unchecked",
      "-Xlint:deprecation"
    ),
    javaOptions ++= Seq(
      "-Xmx2G"
    )
  )

name := "Abduls-Kebabs"

libraryDependencies ++= Seq(
  // Core Akka
  "com.typesafe.akka" %% "akka-actor" % "2.4.0-RC1",
  "com.typesafe.akka" %% "akka-contrib" % "2.4.0-RC1",
  "com.typesafe.akka" %% "akka-http-core-experimental" % "1.0",
  "com.typesafe.akka" %% "akka-http-experimental" % "1.0",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.0-RC1",
  // Miscellaneous
  "ch.qos.logback" %  "logback-classic" % "1.1.3",
  "com.typesafe" %  "config" % "1.3.0",
  // Testing
  "org.scalatest" %% "scalatest" % "2.2.5" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.4" % "test",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.0-RC1" % "test"
)

mainClass in Compile := Some("cakesolutions.Main")
