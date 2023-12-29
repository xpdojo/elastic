#!/usr/bin/env bash

set -e

echo "Stopping Elasticsearch..."
sudo docker compose down -v
