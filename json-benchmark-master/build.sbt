name := "json-benchmark"

version := "0.1"                                                       

organization := "io.gatling.benchmark"                                        

scalaVersion := "2.10.3"

/// ScalaMeter
resolvers += "Sonatype OSS Snapshots" at
  "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "com.github.axel22" %% "scalameter" % "0.4" % "test"

testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework")


/// Spray
resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies += "io.spray" %%  "spray-json" % "1.2.5" 

/// Lift-json
libraryDependencies += "net.liftweb" %% "lift-json" % "2.6-M2"

/// Jackson
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.3.0"

/// Json-Smart
libraryDependencies += "net.minidev" % "json-smart" % "2.0-RC3"

/// Gson
libraryDependencies += "com.google.code.gson" % "gson" % "2.2.4"

libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.10.3"

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.10.3"

libraryDependencies += "org.scala-lang" % "scalap" % "2.10.3"
