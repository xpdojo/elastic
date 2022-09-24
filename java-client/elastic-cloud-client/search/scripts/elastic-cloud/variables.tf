# https://www.terraform.io/language/values
variable "api_key" {
  type        = string
  description = "API Key for Elastic Cloud"
}

variable "allowed_ip" {
  type        = string
  description = "Allowed IP for Elastic Cloud"
}
