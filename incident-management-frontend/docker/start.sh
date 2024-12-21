echo "inc-frontend startup"
echo "删除容器"
docker stop inc-front
docker rm inc-front
echo "构建镜像"
docker build -t inc-front .
echo "根据镜像构建容器"
docker run -d --name inc-front -p 80:80 inc-front
echo "inc-frontend startup success"
