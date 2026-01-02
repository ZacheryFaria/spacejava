docker stop spacejava
docker rm spacejava
docker run -p 9095:8080 -d --restart always --privileged -v /home/zach/spacejava/build/libs/space-0.0.1-SNAPSHOT.jar:/app/space.jar --name spacejava space-runner 
