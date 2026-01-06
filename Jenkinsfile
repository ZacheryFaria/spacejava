pipeline {
    agent any

    tools {
        jdk 'java 25'
    }

    environment {
        BRANCH_TAG = env.BRANCH_NAME.replace('/', '__').replace('-', '_').toLowerCase()
        BUILD_TAG = "${env.BRANCH_TAG}_${env.GIT_COMMIT.substring(0, 8)}_${env.BUILD_NUMBER}"
    }

    stages {
        stage('PR Title Check') {
            when {
                expression { env.CHANGE_ID != null }
            }
            steps {
                script {
                    def prTitle = env.CHANGE_TITLE ?: ""
                    def validPrefixes = ['major', 'minor', 'patch']
                    def hasValidPrefix = validPrefixes.any { prefix -> prTitle.startsWith(prefix) }

                    if (!hasValidPrefix) {
                        def message = "PR title '${prTitle}' must start with one of: ${validPrefixes.join(', ')}"
                        publishChecks name: 'PR Title Validation',
                                summary: message,
                                conclusion: 'FAILURE',
                                detailsURL: env.BUILD_URL
                    } else {
                        publishChecks name: 'PR Title Validation',
                                summary: 'PR title has valid version prefix',
                                conclusion: 'SUCCESS',
                                detailsURL: env.BUILD_URL
                    }
                }
            }
        }

        stage('Build Backend') {
            steps {
                script {
                    def commitMessage = sh(script: 'git log -1 --pretty=%B', returnStdout: true).trim()
                    def prTitle = env.CHANGE_TITLE ?: ""
                    def shouldSkipBump = commitMessage.startsWith('nobump') || prTitle.startsWith('nobump')

                    if (env.BRANCH_NAME == "master" && !shouldSkipBump) {
                        // Determine version bump type from commit message or PR title
                        def bumpType = "patch" // default
                        if (commitMessage.startsWith('major:') || prTitle.startsWith('major:')) {
                            bumpType = "major"
                        } else if (commitMessage.startsWith('minor:') || prTitle.startsWith('minor:')) {
                            bumpType = "minor"
                        } else if (commitMessage.startsWith('patch:') || prTitle.startsWith('patch:')) {
                            bumpType = "patch"
                        }

                        sh "./gradlew final -Prelease.scope=${bumpType}"
                    } else {
                        sh "./gradlew devSnapshot"
                    }
                }
            }

            post {
                always {
                    archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
                    sh "rm -rf build/libs/*"
                }
                success {
                    publishChecks name: 'Build Backend',
                            summary: 'Build the backend and bump version',
                            conclusion: 'SUCCESS',
                            detailsURL: "${env.BUILD_URL}console"
                }
                failure {
                    publishChecks name: 'Build Backend',
                            summary: 'Build the backend and bump version',
                            conclusion: 'FAILURE',
                            detailsURL: "${env.BUILD_URL}console"
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    sh "./gradlew test"
                }
            }

            post {
                always {
                    junit 'build/test-results/**/*.xml'
                    publishHTML(target: [allowMissing         : false,
                                         alwaysLinkToLastBuild: true,
                                         keepAll              : true,
                                         reportDir            : 'build/reports/tests/test',
                                         reportFiles          : 'index.html',
                                         includes             : '**/*',
                                         reportName           : 'Gradle Test Report',
                                         reportTitles         : 'Gradle Test Report'])
                    publishHTML(target: [allowMissing         : false,
                                         alwaysLinkToLastBuild: true,
                                         keepAll              : true,
                                         reportDir            : 'build/reports/problems',
                                         reportFiles          : 'problems-report.html',
                                         includes             : '**/*',
                                         reportName           : 'Gradle Problems Report',
                                         reportTitles         : 'Gradle Problems Report'])
                }
            }
        }

        stage('Bump version') {
            when {
                expression { env.BRANCH_NAME == 'master' }
            }
            steps {
                withCredentials([gitUsernamePassword(credentialsId: '21b3e862-9623-4901-a99e-19697920e65b', gitToolName: 'git-tool')]) {
                    sh "git push --tags"
                }
            }
        }
    }
}