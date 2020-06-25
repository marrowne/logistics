resource "aws_vpc" "logistics-ci" {
  cidr_block = "10.0.0.0/16"

  tags = map(
    "Name", "terraform-logistics-ci",
  )
}

resource "aws_subnet" "logistics-ci" {
  count = 1

  availability_zone       = data.aws_availability_zones.available.names[count.index]
  cidr_block              = "10.0.${count.index}.0/24"
  map_public_ip_on_launch = true
  vpc_id                  = aws_vpc.logistics-ci.id

  tags = map(
    "Name", "terraform-logistics-ci",
  )
}

resource "aws_internet_gateway" "logistics-ci" {
  vpc_id = aws_vpc.logistics-ci.id

  tags = {
    Name = "terraform-logistics-ci"
  }
}

resource "aws_route_table" "logistics-ci" {
  vpc_id = aws_vpc.logistics-ci.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.logistics-ci.id
  }
}

resource "aws_route_table_association" "logistics-ci" {
  count = 1

  subnet_id      = aws_subnet.logistics-ci.0.id
  route_table_id = aws_route_table.logistics-ci.id
}

resource "aws_security_group" "logistics-ci" {
  name        = "terraform-ci-logistics"
  description = "SG for logistics CI"
  vpc_id      = aws_vpc.logistics-ci.id

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "terraform-logistics-ci"
  }
}