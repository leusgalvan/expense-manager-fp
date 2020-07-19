scalaVersion := "2.13.1"

lazy val `spendings-manager` = (project in file("."))
  .settings(name := "Spendings Manager")

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

libraryDependencies += "org.typelevel" %% "cats-core" % "2.1.1"
libraryDependencies += "org.typelevel" %% "cats-free" % "2.1.1"
libraryDependencies += "org.typelevel" %% "cats-effect" % "2.1.4"