artifact = [
        allureResultsDir: "drm-brl-test/allure-results"
]

pipeline {
    agent {
        docker {
            image "maven:3.8.3-adoptopenjdk-8"
            args "-v /var/lib/jenkins/agent/tools:/var/lib/jenkins/agent/tools:rw,z -e TZ=Europe/Moscow"    //монтируем тулзы в контейнер, чтобы оттуда сгенерировать allure-отчет, устанавливаем подходящий часовой пояс
            label "dev02"
        }
    }
    options {
        disableConcurrentBuilds()
    }
    parameters {
         booleanParam (defaultValue: true, description: "", name: "api")
         booleanParam (defaultValue: true, description: "", name: "angular")
         booleanParam (defaultValue: true, description: "", name: "blazor")
         booleanParam (defaultValue: true, description: "", name: "failed")
         choice (choices: ["tst03", "tst04"], name: "STAND_NAME")
         //choice (choices: ["master", "features", "features_jenkins"], name: "BRANCH")
         gitParameter (branchFilter: "origin/(.*)", defaultValue: "master", name: "BRANCH", type: "PT_BRANCH", selectedValue: "DEFAULT")
         string defaultValue: "0", description: "TestRail run id", name: "RUN_ID", trim: true
    }
    stages {
        stage("health check"){
            steps {
                echo "health check realisation"
                //sh "curl -k -Is http://msk-cps-${params.STAND_NAME}.alrosa.ru"
                //sh "curl -k -Is http://msk-cps-${params.STAND_NAME}.alrosa.ru:8080"
                //sh "curl -k -Is http://msk-cps-${params.STAND_NAME}.alrosa.ru:9080"
            }
        }
        stage("get sources") {
            steps {
                catchError (buildResult: "SUCCESS", stageResult: "FAILURE") {
                    git url: "https://msk-atl-bb.alrosa.ru/scm/cpsat/cps_ui_tests.git",
                    credentialsId: "crtci00001",
                    branch: "${params.BRANCH}"
                }
            }
        }
        stage("prepare workspace") {
            steps{
                catchError (buildResult: "SUCCESS", stageResult: "FAILURE") {
                    script{
                        sh "mvn clean"
                        sh "rm -r ${WORKSPACE}/${artifact.allureResultsDir}" //почистим результаты от предыдущих запусков
                        //если запускаем только с флагом failed, стейдж "rerun failed" запустит зафейлыенные тесты с предыдущего запуска джобы (они будут скопированы из временной папки)
                        //но если запускаем c флагами "api" ИЛИ "angular" ИЛИ "blazor", то они сегенируют failed.xml в рамках текущего запуска джобы, а стейдж "rerun failed" запустит только зафейленные в рамках текущего запуска джобы
                        if (params.failed && !params.api && !params.angular && !params.blazor) {
                            sh "cp ${WORKSPACE_TMP}/failed.xml ${WORKSPACE}/drm-brl-test/suites/failed.xml"
                        }
                    }
                }
            }
        }
        stage("run API tests") {
            when {
                expression {return params.api}
            }
            steps{
                catchError (buildResult: "SUCCESS", stageResult: "FAILURE") {
                   sh "mvn test -Dsuitename=rest.xml -DrunId=${params.RUN_ID} -DSTAND_NAME=${params.STAND_NAME} -DRUN_MODE=remote"
                }
            }
        }
        stage("run Angular tests") {
            when {
                expression {return params.angular}
            }
            steps{
                catchError (buildResult: "SUCCESS", stageResult: "FAILURE") {
                    sh "mvn test -Dsuitename=prices.xml -DrunId=${params.RUN_ID} -DSTAND_NAME=${params.STAND_NAME} -DRUN_MODE=remote"
                }
            }
        }
        stage("run Blazor tests") {
            when {
                expression {return params.blazor}
            }
            steps {
                catchError (buildResult: "SUCCESS", stageResult: "FAILURE") {
                    sh "mvn test -Dsuitename=smoke.xml -DrunId=${params.RUN_ID} -DSTAND_NAME=${params.STAND_NAME} -DRUN_MODE=remote"
                }
            }
        }
        stage("rerun failed") {
            when {
                expression {return params.failed}
            }
            steps {
                catchError (buildResult: "SUCCESS", stageResult: "FAILURE") {
                    sh "mvn test -Dsuitename=failed.xml -DrunId=${params.RUN_ID} -DSTAND_NAME=${params.STAND_NAME} -DRUN_MODE=remote"
                }
            }
        }
        stage ("clean up") {
            steps {
                catchError (buildResult: "SUCCESS", stageResult: "FAILURE") {
                    sh "cp ${WORKSPACE}/drm-brl-test/suites/failed.xml ${WORKSPACE_TMP}/failed.xml" //копируем failed.xml во временную папку для использования в следующем запуске (по умолчанию файл восстановится из репо)
                }
            }
        }
    }
    post {
        always {
            allure([
                reportBuildPolicy: "ALWAYS",
                results: [[path: "${artifact.allureResultsDir}"]]
            ])
            //раскомментить после настройки Extension mail plugin
            // emailext (to: "kvart_MarkovPV@alrosa.ru",
            //     subject: "autotests done: ${env.JOB_NAME}",
            //     body: "More info:\n-build ${env.BUILD_URL}\n-allure ${env.BUILD_URL}/allure")
        }
    }
}