# Elastic Stack

## local env

```sh
sudo chown $USER /var/run/docker.sock
# or
sudo usermod -a -G docker $USER
```

## Create a cluster

```sh
docker-compose -f single-node.yaml up -d
```

## Delete a cluster

```sh
docker-compose -f single-node.yaml down -d
docker volume prune
```

