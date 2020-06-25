# authentication manual https://docs.aws.amazon.com/AmazonECR/latest/userguide/getting-started-cli.html
docker build -t identity .
docker tag identity:latest 171236569948.dkr.ecr.us-east-1.amazonaws.com/identity:latest
docker push 171236569948.dkr.ecr.us-east-1.amazonaws.com/identity:latest