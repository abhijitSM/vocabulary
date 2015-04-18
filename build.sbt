name := "vocabulary"

version := "1.0"

scalaVersion := "2.11.5"

lazy val vocabulary = (project in file(".")).enablePlugins(PlayScala)

com.typesafe.sbt.SbtScalariform.scalariformSettings

PlayKeys.routesImport += "binders.PathBinders._"

PlayKeys.routesImport += "binders.QueryStringBinders._"

libraryDependencies += filters