# terraform init
# terraform validate
# terraform apply auto-approve

terraform {
  required_version = ">= 1.0.0"

  required_providers {
    # https://registry.terraform.io/providers/elastic/ec/latest/docs
    ec = {
      source  = "elastic/ec"
      version = "0.4.1"
    }
  }
}

provider "ec" {
  # https://www.elastic.co/guide/en/cloud/current/ec-api-authentication.html
  apikey = var.api_key
}

locals {
  # https://www.elastic.co/guide/en/cloud/current/ec-regions-templates-instances.html
  region = "aws-ap-northeast-2"
}

data "ec_stack" "v7" {
  version_regex = "7.17.?"
  region        = local.region
}

# https://registry.terraform.io/providers/elastic/ec/0.4.1/docs/resources/ec_deployment
resource "ec_deployment" "test-deployment" {
  # endpoint까지 name으로 자동 생성
  name = "test-search"

  # Elastic Stack version to use for all the deployment resources.
  version = data.ec_stack.v7.version

  # Elasticsearch Service (ESS) region where to create the deployment.
  # For Elastic Cloud Enterprise (ECE) installations, set "ece-region".
  # https://www.elastic.co/guide/en/cloud/current/ec-regions-templates-instances.html
  # region = "aws-ap-northeast-2"
  region = local.region

  # https://www.elastic.co/guide/en/cloud/current/ec-regions-templates-instances.html
  #  deployment_template_id = "aws-storage-optimized-v2"
  # 60 GB storage | 2GB RAM | Up to 2.2 vCPU
  # Hourly rate $0.1556

  # deployment_template_id = "aws-general-purpose-v2"
  # 20 GB storage | 2GB RAM | Up to 4.3 vCPU
  # Hourly rate $0.2020

  deployment_template_id = "aws-cpu-optimized-v3"
  # 24 GB storage | 2GB RAM | Up to 8.5 vCPU
  # Hourly rate $0.2732
  # 96 GB storage | 8GB RAM | Up to 8.5 vCPU
  # Hourly rate $1.0928

  traffic_filter = [
    ec_deployment_traffic_filter.test-filter.id
  ]

  elasticsearch {
    autoscale = "false"

    topology {
      id         = "hot_content"
      zone_count = 2

      size          = "8g"
      size_resource = "memory"

      node_type_data   = true
      node_type_ingest = true
      node_type_master = true
      node_type_ml     = false
    }
  }

  kibana {
    topology {
      zone_count    = 1
      size          = "1g"
      size_resource = "memory"
    }
  }

  # apm {}

  # integrations_server {}

  # enterprise_search {}

  tags = {
    owner = "Changsu Im"
    env   = "dev"
  }
}

# https://registry.terraform.io/providers/elastic/ec/0.4.1/docs/resources/ec_deployment_traffic_filter
resource "ec_deployment_traffic_filter" "test-filter" {
  name   = "internal-network"
  region = local.region
  type   = "ip"

  rule {
    source = var.allowed_ip
  }
}
