#!/usr/bin/env groovy
def call(Map params = [:]) {
    // determine the message details
    def providers
    def messageBody
    def messageTail = ''
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
            messageTail = '\nBuild log:\n${BUILD_LOG}'
        break
        case "FAILURE":
            providers = [[$class: 'CulpritsRecipientProvider']]
            messageSubject = "Build failed in Jenkins: ${currentBuild.fullDisplayName}"
            messageBody = """See ${currentBuild.absoluteUrl}"""
            sendMail = true
            messageTail = '\nBuild log:\n${BUILD_LOG}'
        break
        case "ABORTED":
            providers = [[$class: 'CulpritsRecipientProvider']]
            messageSubject = "Build aborted in Jenkins: ${currentBuild.fullDisplayName}"
            messageBody = """See ${currentBuild.absoluteUrl}"""
            sendMail = true
            messageTail = '\nBuild log:\n${BUILD_LOG}'
        break
        default:
            echo "Unknown status: ${currentBuild.currentResult}"
        // should never happen if we are actually being invoked.
        return
    }

    // comment on any jira tickets
    def jiraIssues = jiraIssueSelector(issueSelector: [$class: 'DefaultIssueSelector'])
    for (def jiraIssue in jiraIssues) {
        try {
            jiraComment body: "${messageSubject}\n\n${messageBody}", issueKey: jiraIssue
        } catch (e) {
            echo "WARNING: Could not update ${jiraIssue}: ${e.message}"
        }        
    }
    // set the build description to the jira ticket id's
    if (!jiraIssues.empty) {
        currentBuild.description = "${jiraIssues.join(', ')}"
    }

    // add the changes to the email
    if (currentBuild.changeSets.empty) {
        messageBody = messageBody+"\n\nNo changes.\n";
    } else {
        messageBody = messageBody + "\n\nChanges:\n";
        for (def changeSet in currentBuild.changeSets) {
            for (def change in changeSet) {
                messageBody = messageBody + "\n* ${change.msg.trim().replaceAll('\n','\n  ')}"
            }
        }
        messageBody = messageBody + "\n"
    }
    messageBody = messageBody + '\n${FAILED_TESTS}\n' + messageTail
    // send the mail
    if (sendMail) {
        emailext body: messageBody, recipientProviders: providers, replyTo: 'dev@maven.apache.org', subject: messageSubject, to: 'notifications@maven.apache.org'
    }
}