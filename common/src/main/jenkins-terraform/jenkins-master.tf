resource "aws_instance" "jenkins_master" {
  ami                    = data.aws_ami.jenkins-master.id
  instance_type          = "t2.micro"
  key_name               = "id_rsa"
  vpc_security_group_ids = [aws_security_group.logistics-ci.id]
  subnet_id              = aws_subnet.logistics-ci.0.id

  root_block_device {
    volume_type           = "gp2"
    volume_size           = 30
    delete_on_termination = false
  }
}

resource "aws_route53_record" "tracking" {
  zone_id = "Z104202635AGJVWV9H3HO"
  name    = "jenkins.logistics.mordawski.it"
  type    = "A"
  ttl     = "300"
  records = [aws_instance.jenkins_master.public_ip]
}