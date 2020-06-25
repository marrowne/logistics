# authentication manual https://docs.aws.amazon.com/AmazonECR/latest/userguide/getting-started-cli.html
docker build -t hr .
docker tag hr:latest 171236569948.dkr.ecr.us-east-1.amazonaws.com/hr:latest
docker push 171236569948.dkr.ecr.us-east-1.amazonaws.com/hr:latest