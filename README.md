# Elastic Stack

## local env

```sh
> ls -l /var/run/docker.sock
srw-rw----. 1 root docker 0 Jul 19 08:43 /var/run/docker.sock
```

```sh
> sudo chown $USER /var/run/docker.sock
# sudo usermod -a -G docker $USER
```

```sh
> ls -l /var/run/docker.sock
srw-rw----. 1 markruler docker 0 Jul 19 08:43 /var/run/docker.sock
```

## Create a cluster

```sh
> docker-compose up -d
```

## Delete a cluster

```sh
> docker-compose down
```

```sh
> docker volume ls
DRIVER    VOLUME NAME
local     elastic_elasticsearch-data

> docker volume prune
```

## 설정 파일 지정

```sh
> docker-compose -f single-node.yaml --env-file .env.production up -d
> docker-compose -f single-node.yaml down
```

## 참조

- [Docker ELK Example](https://github.com/deviantony/docker-elk)
- [Environment variables in Compose](https://docs.docker.com/compose/environment-variables/)
