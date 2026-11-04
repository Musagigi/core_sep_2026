pipeline {
    agent any

    stages {
        stage('Build DB image') {
            steps {
                sh 'docker build -f DbDockerfile -t loandb:0.5 .'
            }
        }

        stage('Build app image') {
            steps {
                sh 'docker build -t hw5app:0.1 .'
            }
        }

        stage('Cleanup old containers') {
            steps {
                sh '''
                    docker rm -f hw5app hw-5-postgres hw-5-mortgage-minio || true
                '''
            }
        }

        stage('Start postgres') {
            steps {
                sh '''
                    docker run -d --name hw-5-postgres \
                      -e POSTGRES_USER=mortgage \
                      -e POSTGRES_PASSWORD=mortgage \
                      -e POSTGRES_DB=mortgage \
                      -p 5439:5432 \
                      loandb:0.5
                '''
            }
        }

        stage('Start minio') {
            steps {
                sh '''
                    docker run -d --name hw-5-mortgage-minio \
                      -e MINIO_ROOT_USER=minioadmin \
                      -e MINIO_ROOT_PASSWORD=minioadmin \
                      -p 9000:9000 -p 9001:9001 \
                      cgr.dev/chainguard/minio:latest \
                      server /data --console-address ":9001"
                '''
            }
        }

        stage('Wait for dependencies') {
            steps {
                sh '''
                    for i in $(seq 1 30); do
                      docker exec hw-5-postgres pg_isready -U mortgage -d mortgage && break
                      sleep 2
                    done
                    for i in $(seq 1 30); do
                      curl -sf http://localhost:9000/minio/health/live && break
                      sleep 2
                    done
                '''
            }
        }

        stage('Start app container') {
            steps {
                sh '''
                    docker run -d --name hw5app \
                      -p 4343:8080 \
                      -e MINIO_USER=minioadmin \
                      -e MINIO_PASSWORD=minioadmin \
                      -e MINIO_URL=http://localhost:9000 \
                      -e POSTGRES_HOSTNAME=localhost \
                      -e POSTGRES_PORT=5439 \
                      -e POSTGRES_DATABASE=mortgage \
                      -e POSTGRES_USER=mortgage \
                      -e POSTGRES_PASSWORD=mortgage \
                      hw5app:0.1 \
                      sleep infinity
                '''
            }
        }

        stage('Pull / Build / Test / Run') {
            steps {
                sh 'docker exec hw5app git -C /app pull'
                sh 'docker exec hw5app /app/gradlew clean build -x test'
                sh 'docker exec hw5app /app/gradlew test'
                sh 'docker exec -d hw5app java -jar /app/build/libs/lesson3-0.0.1-SNAPSHOT.jar'
            }
        }

        stage('Health check') {
            steps {
                sh '''
                    for i in $(seq 1 30); do
                      curl -sf http://localhost:4343/actuator/health && exit 0
                      sleep 2
                    done
                    echo "App is not healthy"
                    docker logs hw5app
                    exit 1
                '''
            }
        }
    }

    post {
        always {
            sh 'docker rm -f hw5app hw-5-postgres hw-5-mortgage-minio || true'
        }
    }
}