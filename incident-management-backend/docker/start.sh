echo "inc-backend startup"
echo "删除容器"
docker stop inc
docker rm inc
echo "构建镜像"
docker build -t inc .
echo "根据镜像构建容器"
docker run -d --name inc --network inc_net -p 8080:8080 inc
echo "inc-backend startup success"
