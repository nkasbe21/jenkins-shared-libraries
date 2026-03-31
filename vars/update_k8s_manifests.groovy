#!/usr/bin/env groovy

def call(Map config = [:]) {

    def imageTag = config.imageTag ?: error("Image tag is required")
    def manifestsPath = config.manifestsPath ?: 'kubernetes'
    def gitCredentials = config.gitCredentials ?: 'github-credentials'
    def gitUserName = config.gitUserName ?: 'nkasbe21'
    def gitUserEmail = config.gitUserEmail ?: 'nishakasbe2101@gmail.com'
    def repoUrl = config.repoUrl ?: 'github.com/nkasbe21/tws-e-commerce-app.git'
    def branch = env.GIT_BRANCH ?: 'main'

    echo "Updating Kubernetes manifests with image tag: ${imageTag}"

    withCredentials([usernamePassword(
        credentialsId: gitCredentials,
        usernameVariable: 'GIT_USERNAME',
        passwordVariable: 'GIT_PASSWORD'
    )]) {

        // Configure Git
        bat """
            git config user.name "${nkasbe21}"
            git config user.email "${nkasbe2101}"
        """

        bat """
            set -e

            # Update deployment if exists
            if [ -f "${manifestsPath}/08-easyshop-deployment.yaml" ]; then
                sed -i "s|image: nkasbe2101/easyshop-app:.*|image: nkasbe2101/easyshop-app:${imageTag}|g" ${manifestsPath}/08-easyshop-deployment.yaml
            fi

            # Update migration job
            if [ -f "${manifestsPath}/12-migration-job.yaml" ]; then
                sed -i "s|image: nkasbe2101/easyshop-migration:.*|image: nkasbe2101/easyshop-migration:${imageTag}|g" ${manifestsPath}/12-migration-job.yaml
            fi

            # Update ingress domain
            if [ -f "${manifestsPath}/10-ingress.yaml" ]; then
                sed -i "s|host: .*|host: easyshop.letsdeployit.com|g" ${manifestsPath}/10-ingress.yaml
            fi

            # Commit only if changes exist
            if git diff --quiet; then
                echo "No changes to commit"
            else
                git add ${manifestsPath}/*.yaml
                git commit -m "Update image tag to ${imageTag} [ci skip]"

                git remote set-url origin https://\${GIT_USERNAME}:\${GIT_PASSWORD}@${repoUrl}
                git push origin HEAD:${branch}
            fi
        """
    }
}
