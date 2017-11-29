#!/usr/bin/env groovy
def call(Map params = [:]) {
    def providers
    def messageBody
    def messageSubject
    def sendMail
    switch(currentBuild.currentResult) {
        case "SUCCESS":
            providers = []
            messageSubject = "Build succeeded in Jenkins: ${currentBuild.fullDisplayName}"
            messageBody = """See ${currentBuild.absoluteUrl}"""
            // only send successfuly builds if the previous build was unsuccessful or incomplete
            sendMail = currentBuild.previousBuild == null || !"SUCCESS".equals(currentBuild.previousBuild.result)
        break
        case "UNSTABLE":
            providers = [[$class: 'CulpritsRecipientProvider']]
            messageSubject = "Build unstable in Jenkins: ${currentBuild.fullDisplayName}"
            messageBody = """See ${currentBuild.absoluteUrl}"""
            sendMail = true
        break
        case "FAILURE":
            providers = [[$class: 'CulpritsRecipientProvider']]
            messageSubject = "Build failed in Jenkins: ${currentBuild.fullDisplayName}"
            messageBody = """See ${currentBuild.absoluteUrl}"""
            sendMail = true
        break
        case "ABORTED":
            providers = [[$class: 'CulpritsRecipientProvider']]
            messageSubject = "Build aborted in Jenkins: ${currentBuild.fullDisplayName}"
            messageBody = """See ${currentBuild.absoluteUrl}"""
            sendMail = true
        break
        default:
        // should never happen if we are actually being invoked.
        return
    }
    try {
        jiraComment body: "${messageSubject}\n\n${messageBody}"
    } catch (e) {
        echo "jiraComment failed: ${e.message}"
    }
    if (currentBuild.changeSets.empty) {
        messageBody = messageBody+"\n\nNo changes.\n";
    } else {
        messageBody = messageBody + "\n\nChanges:\n";
        for (def changeSet in currentBuild.changeSets) {
            for (def change in changeSet) {
                messageBody = messageBody + "\n* ${change.msg.trim().replaceAll('\n','\n  ')}"
            }
        }
    }
    if (sendMail) {
        emailext body: messageBody, recipientProviders: providers, replyTo: 'dev@maven.apache.org', subject: messageSubject, to: 'notifications@maven.apache.org'
    }
}