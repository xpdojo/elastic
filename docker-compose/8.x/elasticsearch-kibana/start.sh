#!/usr/bin/env bash

set -e

echo "Set up..."
sudo docker compose up setup

echo "Starting Elasticsearch..."
sudo docker compose up
