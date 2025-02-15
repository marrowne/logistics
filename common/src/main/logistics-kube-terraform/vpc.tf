resource "aws_vpc" "logistics" {
  cidr_block = "10.0.0.0/16"

  tags = map(
    "Name", "terraform-eks-logistics-node",
    "kubernetes.io/cluster/${var.cluster-name}", "shared",
  )
}

resource "aws_subnet" "logistics" {
  count = 2

  availability_zone       = data.aws_availability_zones.available.names[count.index]
  cidr_block              = "10.0.${count.index}.0/24"
  map_public_ip_on_launch = true
  vpc_id                  = aws_vpc.logistics.id

  tags = map(
    "Name", "terraform-eks-logistics-node",
    "kubernetes.io/cluster/${var.cluster-name}", "shared",
  )
}

resource "aws_internet_gateway" "logistics" {
  vpc_id = aws_vpc.logistics.id

  tags = {
    Name = "terraform-eks-logistics"
  }
}

resource "aws_route_table" "logistics" {
  vpc_id = aws_vpc.logistics.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.logistics.id
  }
}

resource "aws_route_table_association" "logistics" {
  count = 2

  subnet_id      = aws_subnet.logistics.*.id[count.index]
  route_table_id = aws_route_table.logistics.id
}
