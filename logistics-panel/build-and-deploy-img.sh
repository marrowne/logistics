# authentication manual https://docs.aws.amazon.com/AmazonECR/latest/userguide/getting-started-cli.html
docker build -t logistics-panel .
docker tag logistics-panel:latest 171236569948.dkr.ecr.us-east-1.amazonaws.com/logistics-panel:latest
docker push 171236569948.dkr.ecr.us-east-1.amazonaws.com/logistics-panel:latest