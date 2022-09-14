# Logstash

- [Logstash](#logstash)
  - [Download](#download)
    - [Debian](#debian)
    - [RedHat](#redhat)
  - [Docker](#docker)

## Download

- [Binary](https://www.elastic.co/kr/downloads/logstash)
  - [Past Releases](https://www.elastic.co/kr/downloads/past-releases#logstash)

```sh
cd /tmp
curl -LO https://artifacts.elastic.co/downloads/logstash/logstash-7.17.6-linux-x86_64.tar.gz
```

```sh
curl https://artifacts.elastic.co/downloads/logstash/logstash-7.17.6-linux-x86_64.tar.gz.sha512
# 339e27791dc92d5cd2338174e886273e0ed91a42fa6a1c5ac8a34c84e3454fa19b7fa346ee8c312a9db40278e2e6145c0e759c33ea42df4879b43f9515fb6672  logstash-7.17.6-linux-x86_64.tar.gz
sha512sum logstash-7.17.6-linux-x86_64.tar.gz
# 339e27791dc92d5cd2338174e886273e0ed91a42fa6a1c5ac8a34c84e3454fa19b7fa346ee8c312a9db40278e2e6145c0e759c33ea42df4879b43f9515fb6672  logstash-7.17.6-linux-x86_64.tar.gz
```

```sh
tar xvf logstash-7.17.6-linux-x86_64.tar.gz
cp -r logstash-7.17.6 /usr/local/logstash
echo 'export PATH=$PATH:/usr/local/logstash/bin\n' >> ~/.bashrc
source ~/.bashrc

logstash -V
# logstash 7.17.6
```

### Debian

- [apt](https://www.elastic.co/guide/en/logstash/7.17/installing-logstash.html#_apt)

```sh
wget -qO - https://artifacts.elastic.co/GPG-KEY-elasticsearch | sudo apt-key add -
sudo apt-get install apt-transport-https

# version 7.x
echo "deb https://artifacts.elastic.co/packages/7.x/apt stable main" | sudo tee -a /etc/apt/sources.list.d/elastic-7.x.list
sudo apt-get update
sudo apt-get install logstash
```

```sh
systemctl status logstash
```

```sh
echo 'export PATH=$PATH:/usr/share/logstash/bin\n' >> ~/.bashrc
source ~/.bashrc

logstash -V
# logstash 7.17.6
```

### RedHat

- [yum](https://www.elastic.co/guide/en/logstash/7.17/installing-logstash.html#_yum)

```sh
sudo rpm --import https://artifacts.elastic.co/GPG-KEY-elasticsearch
```

```toml
# /etc/yum.repos.d/elastic.repo
[logstash-7.x]
name=Elastic repository for 7.x packages
baseurl=https://artifacts.elastic.co/packages/7.x/yum
gpgcheck=1
gpgkey=https://artifacts.elastic.co/GPG-KEY-elasticsearch
enabled=1
autorefresh=1
type=rpm-md
```

```sh
sudo yum install logstash
```

```sh
systemctl status logstash
```

## Docker

```sh
sudo docker-compose up
```
