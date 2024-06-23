ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "Student_Loan_Model"
  )

libraryDependencies ++= Seq(
  "com.github.pureconfig" %% "pureconfig" % "0.17.6"
  ,"org.scalactic" %% "scalactic" % "3.2.18"
  ,"org.scalatest" %% "scalatest" % "3.2.18" % "test"
)