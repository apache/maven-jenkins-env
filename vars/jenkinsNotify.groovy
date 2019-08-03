#!/usr/bin/env groovy

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

def call(Map params = [:]) {
    echo "Build result: ${currentBuild.currentResult}"
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
    def jiraIssues = null
    def jiraMavens = []
    try {
        jiraIssues = jiraIssueSelector(issueSelector: [$class: 'DefaultIssueSelector'])
        for (def jiraIssue in jiraIssues) {
            // only comment on maven's issues, all our trackers start with M
            // may end up commenting on other TLPs in some cases but should be very rare
            if (jiraIssue.startsWith("M")) {
                try {
                    jiraComment body: "${messageSubject}\n\n${messageBody}", issueKey: jiraIssue
                } catch (e) {
                    echo "WARNING: Could not update ${jiraIssue}: ${e.message}"
                }
                jiraMavens += jiraIssue
            }
        }
    } catch (e) {
        echo "WARNING: Could not determine JIRA issues: ${e.message}"
    }
    // set the build description to the jira ticket id's
    if (!jiraMavens.empty) {
        currentBuild.description = "${jiraMavens.join(', ')}"
    }

    // add the changes to the email
	def changes = currentBuild?.changeSets
	def authors = []
    if (changes.isEmpty() ) {
        messageBody = messageBody + "\n\nNo changes.\n";
    } else {
        messageBody = messageBody + "\n\nChanges:\n";
        for (def changeSet in changes) {
            for (def change in changeSet) {
                messageBody = messageBody + "\n* ${change.msg.trim().replaceAll('\n','\n  ')}"
                authors += change.author
            }
        }
        messageBody = messageBody + "\n"
    }
    println("The authors of changes ${authors.unique()}.")
    if (authors.contains('github')) sendMail = false
    if (sendMail) {
        messageBody = messageBody + '\n${FAILED_TESTS}\n' + messageTail
        println("Sending email ...")
        emailext body: messageBody, recipientProviders: providers, replyTo: 'dev@maven.apache.org', subject: messageSubject, to: 'notifications@maven.apache.org'
    }
}
