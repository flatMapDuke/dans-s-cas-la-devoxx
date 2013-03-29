scalaVersion := "2.10.0"


scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

traceLevel := -1

logLevel := Level.Info

// disable printing timing information, but still print [success]
showTiming := false

// disable printing a message indicating the success or failure of running a task
showSuccess := false

offline := true

libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.10.0"

libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.10.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1"

addCommandAlias("partie1-1", "~ test-only premiers_pas")

addCommandAlias("partie1-2", "~ test-only pas_suivant")

addCommandAlias("partie2", "~ test-only we_need_to_go_deeper")

addCommandAlias("partie3", "~ test-only cons_et_nil")

addCommandAlias("partie4", "~ test-only type_classes")

addCommandAlias("partie5", "~ test-only un_sac_avec_des_items")

addCommandAlias("partie6", "~ test-only bonus_event_sourcing")

addCommandAlias("go", "~ test-only HandsOnScala")