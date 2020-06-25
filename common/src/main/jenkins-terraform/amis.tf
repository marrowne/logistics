data "aws_ami" "jenkins-master" {
  most_recent = true
  owners      = ["self"]

  filter {
    name   = "name"
    values = ["jenkins-master-2.107.2"]
  }
}