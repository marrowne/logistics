# authentication manual https://docs.aws.amazon.com/AmazonECR/latest/userguide/getting-started-cli.html
docker build -t tracking .
docker tag tracking:latest 171236569948.dkr.ecr.us-east-1.amazonaws.com/tracking:latest
docker push 171236569948.dkr.ecr.us-east-1.amazonaws.com/tracking:latest