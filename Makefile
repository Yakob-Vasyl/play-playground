run:
	sbt -Dconfig.resource=local.conf run
test:
	sbt -Dconfig.resource=local.conf test