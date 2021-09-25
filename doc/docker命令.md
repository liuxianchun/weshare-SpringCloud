##　docker命令

### 1.docker安装elasticsearch

```
docker run -d --name es2 --privileged=true \
-v /home/liuxianchun/docker/es/plugins:/usr/share/elasticsearch/plugins \
-v /home/liuxianchun/docker/es/config:/usr/share/elasticsearch/config \
-v /home/liuxianchun/docker/es/logs:/usr/share/elasticsearch/logs \
-p 9200:9200 -p 9300:9300 \
-e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:7.8.0
```

多目录挂载

```
docker run -it -d -v /宿主机目录:/容器目录 -v /宿主机目录2:/容器目录2 镜像名
```

获得权限　--privileged=true

单机

```
docker run -d -e ES_JAVA_OPTS='-Xms128m -Xmx128m' -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" --name es elasticsearch:7.8.0
```

进入容器

```
docker exec -it [es的容器id] /bin/bash
```



安装ik分词器

```
./usr/share/elasticsearch/bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.8.0/elasticsearch-analysis-ik-7.8.0.zip
```



docker设置JVM

```
docker run -d --name mybatis -e JAVA_OPTS='-Xmx100m -Xms100m -Xmn50m -Xms100m -XX:MaxMetaspaceSize=16m -XX:MetaspaceSize=16m' -p 8080:8080 mybatis-jar
```

限制容器内存大小(内存太小会导致es无法启动)

````
docker run -d -e ES_JAVA_OPTS='-Xms128m -Xmx128m' -m 400M --memory-swap 400M -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" --name es3 docker.elastic.co/elasticsearch/elasticsearch:7.8.0
````



##  2.docker安装nginx

```
docker run --name nginx -d -p 8088:80 \
  -v /home/liuxianchun/docker/nginx/log:/var/log/nginx  \
  -v /home/liuxianchun/docker/nginx/conf/nginx.conf:/etc/nginx/nginx.conf \
  -v /home/liuxianchun/docker/nginx/conf.d:/etc/nginx/conf.d \
  -v /home/liuxianchun/docker/nginx/html:/usr/share/nginx/html \
  nginx:latest
```

## 3.docker安装rabbitmq

```
docker run -dit --name rabbitmq -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -p 15672:15672 -p 5672:5672 rabbitmq:management
```

## 4.docker安装redis

```
docker run -d --privileged=true -p 6379:6379 --restart always -v /home/liuxianchun/docker/redis/conf/redis.conf:/etc/redis/redis.conf -v /home/liuxianchun/docker/redis/data:/data --name redis redis redis-server /etc/redis/redis.conf --appendonly yes
```

```
修改密码
rabbitmqctl  change_password admin 'admin'
增加用户
rabbitmqctl add_user guest guest
授予权限
rabbitmqctl set_user_tags guest administrator
rabbitmqctl set_permissions -p / guest '.*' '.*' '.*'
```

## 5.docker安装nacos

```
docker run -e JVM_XMS=128m -e JVM_XMX=128m -e JVM_XMN=64m --env MODE=standalone --name nacos -d -p 8848:8848 nacos/nacos-server
```

```
docker run -m 450M --memory-swap 450M -e JVM_XMS=80m -e JVM_XMX=80m -e JVM_XMN=80m  --env MODE=standalone --name nacos2 -d -p 8848:8848 nacos/nacos-server
```



## 6.docker安装mysql

```
docker run -d -p 3306:3306  -e MYSQL_ROOT_PASSWORD=123456 --name cmysql --restart=always -v /home/liuxianchun/docker/mysql/config/my.cnf:/etc/mysql/my.cnf -v /home/liuxianchun/docker/mysql/data:/var/lib/mysql mysql:latest
```

## 7.docker运行kibana

```
docker run --link es -d -e ES_JAVA_OPTS="-Xms64m -Xmx64m" --name kibana -p 5601:5601 kibana:7.8.0
es为elasticsearch容器的name或者ID
```

