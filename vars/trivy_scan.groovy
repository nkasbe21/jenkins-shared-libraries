def call(){
  bat "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy fs ."
}
