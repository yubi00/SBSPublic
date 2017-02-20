cd ~/git
cd dbenvy-che
mvn clean install
./run.bash clean
cd ../dbenvy
./dev/deploy.bash
npm install
npm start
