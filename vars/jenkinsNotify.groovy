#!/usr/bin/env groovy
def call(Map params = [:]) {
    def providers
    def messageBody
    def messageSubject
    switch(currentBuild.currentResult) {
        case "SUCCESS":
            providers = []
            messageSubject = "Build succeeded in Jenkins: ${currentBuild.fullDisplayName}"
            messageBody = """See ${currentBuild.absoluteUrl}"""
        break
        case "UNSTABLE":
            providers = [[$class: 'CulpritsRecipientProvider']]
            messageSubject = "Build unstable in Jenkins: ${currentBuild.fullDisplayName}"
            messageBody = """See ${currentBuild.absoluteUrl}"""
        break
        case "FAILURE":
            providers = [[$class: 'CulpritsRecipientProvider']]
            messageSubject = "Build failed in Jenkins: ${currentBuild.fullDisplayName}"
            messageBody = """See ${currentBuild.absoluteUrl}"""
        break
        case "ABORTED":
            providers = [[$class: 'CulpritsRecipientProvider']]
            messageSubject = "Build aborted in Jenkins: ${currentBuild.fullDisplayName}"
            messageBody = """See ${currentBuild.absoluteUrl}"""
        break
        default:
        // should never happen if we are actually being invoked.
        return
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
    emailext body: messageBody, recipientProviders: providers, replyTo: 'dev@maven.apache.org', subject: messageSubject, to: 'notifications@maven.apache.org'
}