def call(){
  bat "docker run --rm -v %cd%:/app aquasec/trivy fs /app"
}
