 #!/bin/bash
CONTAINER=spring
if docker container inspect "${CONTAINER}" &>/dev/null; then
docker stop ${CONTAINER};
docker rm ${CONTAINER};
fi;
docker build -t "backend:latest" . 
docker run -d -p 8080:8080 --name ${CONTAINER} backend:latest