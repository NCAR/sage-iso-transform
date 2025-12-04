#!/bin/bash

# Uncomment to debug this script
#set -x

INPUT_WAF=$1
OUTPUT_WAF=$2
DEV_OR_PROD=$3

#
# Constants that may require admin configuration
#
APP_DIR='/app'
JAVA='/opt/java/openjdk/bin/java'
TRANSFORM_JAR_PATH=`ls $APP_DIR/target/*.jar`
LOG_FILE="$APP_DIR/transformer_${DEV_OR_PROD}.log"

WAF_STATUS_COMMAND="$APP_DIR/check_git.sh"

SECRETS_TOKEN="@github.com"

#
# Function definitions
#

function LogOperation {
   local operation="$1"
   local outputMessage="$2"
   
   timeStamp=`date`
   echo "$timeStamp":  "$operation" >> $LOG_FILE
   if [ -n "$outputMessage" ]; then
       # Filter out secrets for the log file.
       echo "   " "$outputMessage" | grep -v $SECRETS_TOKEN >> $LOG_FILE
   fi
}


function PerformTransforms {
   local operation="$1"
   local files="$2"
   local TRANSFORM_BASE="$JAVA -jar $TRANSFORM_JAR_PATH"
   local tranformOutput

   # Transform all files
   for file in $files; do
       transformCommand="$TRANSFORM_BASE --input=$INPUT_WAF/$file --output=$OUTPUT_WAF/$file"
       LogOperation "Performing transform command:" "$transformCommand"
       transformOutput=`$transformCommand`
       exitStatus=$?
       if [ $exitStatus -ne 0 ]; then
          LogOperation "ERROR: $OUTPUT_WAF/$file not transformed:" "$transformOutput"
       fi
   done
}


###
### Main Program
###

LogOperation "================ Starting run_transformer.sh ==============="
 
cd $INPUT_WAF
 
# Update local repository metadata.
gitOutput=`git remote update`
LogOperation "Git Remote Update Input WAF: $INPUT_WAF" "$gitOutput"

# Check for file differences with remote.
statusWAF=`$WAF_STATUS_COMMAND`
if [ "$statusWAF" == "Up-to-date" ]; then
    LogOperation "No remote changes detected." ""
else
    # Get lists for files that were added, modified, or deleted.
    gitOutput=`git fetch`
    LogOperation "Git Fetch Input WAF" "$gitOutput"

    addedFiles=`git diff --name-only --diff-filter=A main origin/main`
    modifiedFiles=`git diff --name-only --diff-filter=M main origin/main`
    deletedFiles=`git diff --name-only --diff-filter=D main origin/main`
 
    # Update local INPUT_WAF
    gitOutput=`git pull`
    LogOperation "Git Pull Input WAF" "$gitOutput"
 
    # Perform ISO Transforms on added and modified files.
    PerformTransforms "Added" "$addedFiles"
    PerformTransforms "Modified" "$modifiedFiles"
     
    # Update local OUTPUT_WAF 
    cd $OUTPUT_WAF
    gitOutput=`git pull`
    LogOperation "Git Pull Output WAF" "$gitOutput"

    # Remove deleted files from OUTPUT_WAF 
    for file in $deletedFiles; do
        git rm $file
        LogOperation "Removed $OUTPUT_WAF/$file" ""
    done

    # Commit and Push all OUTPUT_WAF repo changes
    git add .

    gitOutput=`git pull -s recursive -X ours 2>&1`
    LogOperation "Git Pull-Merge Output WAF: $OUTPUT_WAF" "$gitOutput"

    git commit -a -m "Auto update from `basename $INPUT_WAF`"

    gitOutput=`git push 2>&1`
    LogOperation "Git Push Output WAF" "$gitOutput"
fi
