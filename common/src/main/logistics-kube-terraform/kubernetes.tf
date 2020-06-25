resource "kubernetes_deployment" "logistics-panel" {
  metadata {
    name = "logistics-panel"
    labels = {
      deployment = "logistics-panel"
    }
  }
  spec {
    replicas = 1

    selector {
      match_labels = {
        pod = "logistics-panel"
      }
    }

    template {
      metadata {
        labels = {
          pod = "logistics-panel"
        }
      }
      spec {
        container {
          name = "logistics-panel"
          image = "171236569948.dkr.ecr.us-east-1.amazonaws.com/logistics-panel:latest"
          env {
            name  = "REACT_APP_STAGE"
            value = "prod"
          }
        }
      }
    }
  }

  depends_on = [
    aws_eks_cluster.logistics,
    aws_eks_node_group.logistics-eks-master,
    aws_eks_node_group.logistics-eks-nodes
  ]
}
//
resource "kubernetes_service" "logistics-panel" {
  metadata {
    name = "logistics-panel"
    labels = {
      pod = "logistics-panel"
    }
  }

  spec {
    selector = {
      pod = kubernetes_deployment.logistics-panel.metadata[0].labels.deployment
    }

    port {
      port        = 80
      target_port = 80
    }

    type = "LoadBalancer"
  }

  depends_on = [
    aws_eks_cluster.logistics,
    aws_eks_node_group.logistics-eks-master,
    aws_eks_node_group.logistics-eks-nodes
  ]
}

resource "kubernetes_pod" "identity" {
  metadata {
    name = "identity"
    labels = {
      pod = "identity"
    }
  }

  spec {
    container {
      name = "identity"
      image = "171236569948.dkr.ecr.us-east-1.amazonaws.com/identity:latest"
      env {
        name = "DB_HOST"
        value = aws_db_instance.logistics_db.address
      }
      env {
        name = "DB_PORT"
        value = aws_db_instance.logistics_db.port
      }
    }
  }

  depends_on = [
    aws_eks_cluster.logistics,
    aws_eks_node_group.logistics-eks-master,
    aws_eks_node_group.logistics-eks-nodes
  ]
}

resource "kubernetes_service" "identity" {
  metadata {
    name = "identity"
    labels = {
      deployment = "identity"
    }
  }
  spec {
    selector = {
      pod = kubernetes_pod.identity.metadata[0].labels.pod
    }
    port {
      port        = 8082
      target_port = 8082
    }

    type = "LoadBalancer"
  }

  depends_on = [
    aws_eks_cluster.logistics,
    aws_eks_node_group.logistics-eks-master,
    aws_eks_node_group.logistics-eks-nodes
  ]
}

resource "kubernetes_pod" "hr" {
  metadata {
    name = "hr"
    labels = {
      pod = "hr"
    }
  }

  spec {
    container {
      name = "hr"
      image = "171236569948.dkr.ecr.us-east-1.amazonaws.com/hr:latest"
      env {
        name = "DB_HOST"
        value = aws_db_instance.logistics_db.address
      }
      env {
        name = "DB_PORT"
        value = aws_db_instance.logistics_db.port
      }
      env {
        name = "IDENTITY_HOST"
        value = aws_route53_record.identity.name
      }
      env {
        name = "IDENTITY_PORT"
        value = "8082"
      }
      env {
        name = "IDENTITY_PROTOCOL"
        value = "http"
      }
      env {
        name = "IDENTITY_CLIENT_SECRET"
        value = "secret"
      }
    }
  }

  depends_on = [
    aws_eks_cluster.logistics,
    aws_eks_node_group.logistics-eks-master,
    aws_eks_node_group.logistics-eks-nodes
  ]
}


resource "kubernetes_service" "hr" {
  metadata {
    name = "hr"
    labels = {
      deployment = "hr"
    }
  }
  spec {
    selector = {
      pod = kubernetes_pod.hr.metadata[0].labels.pod
    }
    port {
      port        = 8081
      target_port = 8081
    }

    type = "LoadBalancer"
  }

  depends_on = [
    aws_eks_cluster.logistics,
    aws_eks_node_group.logistics-eks-master,
    aws_eks_node_group.logistics-eks-nodes
  ]
}

resource "kubernetes_pod" "tracking" {
  metadata {
    name = "tracking"
    labels = {
      pod = "tracking"
    }
  }

  spec {
    container {
      name = "tracking"
      image = "171236569948.dkr.ecr.us-east-1.amazonaws.com/tracking:latest"
      env {
        name = "DB_HOST"
        value = aws_db_instance.logistics_db.address
      }
      env {
        name = "DB_PORT"
        value = aws_db_instance.logistics_db.port
      }
      env {
        name = "IDENTITY_HOST"
        value = aws_route53_record.identity.name
      }
      env {
        name = "IDENTITY_PORT"
        value = "8082"
      }
      env {
        name = "IDENTITY_PROTOCOL"
        value = "http"
      }
      env {
        name = "IDENTITY_CLIENT_SECRET"
        value = "secret"
      }
      env {
        name = "IDENTITY_ADMIN_PASSWORD"
        value = "password"
      }
      env {
        name = "HR_HOST"
        value = aws_route53_record.hr.name
      }
      env {
        name = "HR_PORT"
        value = "8081"
      }
      env {
        name = "HR_PROTOCOL"
        value = "http"
      }
    }
  }

  depends_on = [
    aws_eks_cluster.logistics,
    aws_eks_node_group.logistics-eks-master,
    aws_eks_node_group.logistics-eks-nodes
  ]
}


resource "kubernetes_service" "tracking" {
  metadata {
    name = "tracking"
    labels = {
      deployment = "tracking"
    }
  }
  spec {
    selector = {
      pod = kubernetes_pod.tracking.metadata[0].labels.pod
    }
    port {
      port        = 8080
      target_port = 8080
    }

    type = "LoadBalancer"
  }

  depends_on = [
    aws_eks_cluster.logistics,
    aws_eks_node_group.logistics-eks-master,
    aws_eks_node_group.logistics-eks-nodes
  ]
}

resource "aws_route53_record" "logistics" {
  zone_id = "Z104202635AGJVWV9H3HO"
  name    = "www.logistics.mordawski.it"
  type    = "CNAME"
  ttl     = "300"
  records = [kubernetes_service.logistics-panel.load_balancer_ingress[0].hostname]
}

resource "aws_route53_record" "identity" {
  zone_id = "Z104202635AGJVWV9H3HO"
  name    = "identity.logistics.mordawski.it"
  type    = "CNAME"
  ttl     = "300"
  records = [kubernetes_service.identity.load_balancer_ingress[0].hostname]
}

resource "aws_route53_record" "hr" {
  zone_id = "Z104202635AGJVWV9H3HO"
  name    = "hr.logistics.mordawski.it"
  type    = "CNAME"
  ttl     = "300"
  records = [kubernetes_service.hr.load_balancer_ingress[0].hostname]
}

resource "aws_route53_record" "tracking" {
  zone_id = "Z104202635AGJVWV9H3HO"
  name    = "tracking.logistics.mordawski.it"
  type    = "CNAME"
  ttl     = "300"
  records = [kubernetes_service.tracking.load_balancer_ingress[0].hostname]
}