FROM maven:amazoncorretto
LABEL MAINTAINER="antonello.calabro@isti.cnr.it"
COPY /* /
EXPOSE 61616:61616
EXPOSE 8161 5672 61613 1883 61614 8181
ENTRYPOINT ["mvn","package","test","install"]