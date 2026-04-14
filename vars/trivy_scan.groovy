def call() {
  bat "docker run --rm -v %cd%:/app -w /app aquasec/trivy:0 fs ."
}
