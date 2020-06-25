resource "aws_iam_role" "logistics-cluster" {
  name = "terraform-eks-logistics-cluster"

  assume_role_policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "eks.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
POLICY
}

resource "aws_iam_role_policy_attachment" "logistics-cluster-AmazonEKSClusterPolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
  role       = aws_iam_role.logistics-cluster.name
}

resource "aws_iam_role_policy_attachment" "logistics-cluster-AmazonEKSServicePolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSServicePolicy"
  role       = aws_iam_role.logistics-cluster.name
}

resource "aws_security_group" "logistics-cluster" {
  name        = "terraform-eks-logistics-cluster"
  description = "Cluster communication with worker nodes"
  vpc_id      = aws_vpc.logistics.id

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "terraform-eks-logistics"
  }
}

resource "aws_security_group_rule" "logistics-cluster-ingress-workstation-https" {
  cidr_blocks       = [local.workstation-external-cidr]
  description       = "Allow workstation to communicate with the cluster API Server"
  from_port         = 443
  protocol          = "tcp"
  security_group_id = aws_security_group.logistics-cluster.id
  to_port           = 443
  type              = "ingress"
}

resource "aws_eks_cluster" "logistics" {
  name     = var.cluster-name
  role_arn = aws_iam_role.logistics-cluster.arn

  vpc_config {
    security_group_ids = [aws_security_group.logistics-cluster.id]
    subnet_ids         = aws_subnet.logistics[*].id
  }

  depends_on = [
    aws_iam_role_policy_attachment.logistics-cluster-AmazonEKSClusterPolicy,
    aws_iam_role_policy_attachment.logistics-cluster-AmazonEKSServicePolicy,
  ]
}

resource "aws_eks_node_group" "logistics-eks-nodes" {
  cluster_name = aws_eks_cluster.logistics.name
  node_group_name = "logistics-nodes"
  node_role_arn = aws_iam_role.logistics-node.arn
  subnet_ids = aws_subnet.logistics[*].id
  instance_types = ["t2.micro"]

  scaling_config {
    desired_size = 1
    max_size = 2
    min_size = 1
  }

  depends_on = [
    aws_iam_role_policy_attachment.logistics-node-AmazonEKSWorkerNodePolicy,
    aws_iam_role_policy_attachment.logistics-node-AmazonEKS_CNI_Policy,
    aws_iam_role_policy_attachment.logistics-node-AmazonEC2ContainerRegistryReadOnly,
  ]
}

