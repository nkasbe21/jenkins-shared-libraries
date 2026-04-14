def call() {
  bat "docker run --rm -v %cd%:/app -w /app aquasec/trivy:canary fs ."
}
