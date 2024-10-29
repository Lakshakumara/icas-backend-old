echo "------------Task list----------------"
echo "1. Remove Docker container"
echo "2. Remove Docker image"
echo "3. Build and run Docker container"
echo "4. Remove image and container, Build application and docker image and run docker container"
echo "5. Clean and Build application"
#..........................................
read task
if [ $task -eq 1 ]
then
	clear
        docker ps
        echo "Enter container/s name/s or ID/s "
        read containerid
        docker rm -f $containerid
#...........................................
elif [ $task -eq 2 ]
then
clear
docker ps
docker images
echo"Enter Docker image/s name/s"
read containerid
docker image remove $containerid
#............................................
elif [ $task -eq 3 ]
then
docker images
echo "Please Enter Imagename"
read imagename
echo "Please Enter external port? eg: front->back end port 4200 "
read portout
echo "Please Enter Internal port? eg: 9000"
read portin
docker build -t $imagename .
echo "press enter to host"
docker run -d --name $imagename --restart=unless-stopped -p $portout:$portin $imagename
echo "Successfully created."
#...............................................

elif [ $task -eq 4 ]
then
clear
docker ps
echo "Enter container/s name/s or ID/s to remove image and container.."
read containerid
echo "Please Enter Imagename to build docker image"
read imagename
echo "Please Enter external port? eg: front->back end port 4200 "
read portout
echo "Please Enter Internal port? eg: 9000"
read portin
mvn clean
mvn install
docker build -t $imagename .
docker rm -f $containerid
docker image remove $containerid
echo "press enter to host"
docker run -d --name $imagename --restart=unless-stopped -p $portout:$portin $imagename
echo "Successfully created."
#.................................................
elif [ $task -eq 5 ]
then
mvn clean
mvn install
fi
#.................................................
