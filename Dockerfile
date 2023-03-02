FROM amazoncorretto:19.0.1-al2
LABEL MAINTAINER="antonello.calabro@isti.cnr.it"
COPY src/main/resources/startupRule.drl src/main/resources/
COPY /* /
EXPOSE 61616
EXPOSE 8181
ENTRYPOINT ["java","-cp", "RestBiecoMonitoring-1.0-SNAPSHOT-jar-with-dependencies.jar", "it.cnr.isti.labsedc.concern.rest.Main"]
