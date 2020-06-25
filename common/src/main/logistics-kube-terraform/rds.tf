resource "random_string" "lower" {
  length  = 16
  upper   = false
  lower   = true
  number  = true
  special = false
}

resource "aws_db_instance" "logistics_db" {
  allocated_storage    = 20
  storage_type         = "gp2"
  engine               = "mysql"
  engine_version       = "5.7"
  instance_class       = "db.t2.micro"
  name                 = "logistics_mysql"
  username             = "logistics"
  password             = "parcelsSecured"
  parameter_group_name = "default.mysql5.7"
  publicly_accessible  = "true"
  final_snapshot_identifier = "${var.cluster-name}-final-snapshot-${random_string.lower.result}"
}

provider "mysql" {
  endpoint = aws_db_instance.logistics_db.endpoint
  username = aws_db_instance.logistics_db.username
  password = aws_db_instance.logistics_db.password
}

resource "mysql_database" "identity" {
  name = "identity"
  default_collation = "utf8_unicode_ci"
}

resource "mysql_database" "hr" {
  name = "hr"
  default_collation = "utf8_unicode_ci"
}

resource "mysql_database" "tracking" {
  name = "tracking"
  default_collation = "utf8_unicode_ci"
}