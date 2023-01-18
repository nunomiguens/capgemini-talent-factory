#!/bin/sh
docker stop git-server-container
docker run -p 8081:8081 -p 2222:22 -d -it --rm --name git-server-container \
  --mount type=bind,source="$(pwd)",target=/app git-server-image
