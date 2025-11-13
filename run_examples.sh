#!/bin/sh

set -x

JARFILE=`ls target/*.jar`

cd examples
INPUTS=`ls *.xml`
cd -

# Produce output in top-level directory

for input in $INPUTS; do
   output=$input
   java -jar $JARFILE --input=examples/$input --output=./$output
done

