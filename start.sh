docker-compose down

# build backend image
docker build -t commerce_api_image:latest .

#start environment
docker-compose up --build --force-recreate --remove-orphans

# 1 - ls 2- chmod +x start.sh 3- ./start.sh