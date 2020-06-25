provider "aws" {
  profile = "default"
  region  = "us-east-1"
  version = ">= 2.38.0"
}

terraform {
  backend "s3" {
    bucket         = "logistics-jenkins-tfstates"
    key            = "terraform/logistics-jenkins"
    region         = "us-east-1"
  }
}

data "aws_region" "current" {}

data "aws_availability_zones" "available" {}