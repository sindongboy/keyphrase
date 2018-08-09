#!/bin/bash

function usage() {
	echo "usage: $0 [options]"
	echo "-h	help"
	echo "-i	input file"
	echo "-o	output path"
	echo "-v	verbose [optional]"
	echo "-k	keep frequency"
	exit 1
}

debug=0
freq=0
while test $# -gt 0; do
	case "$1" in
		-h)
			shift
			usage
			;;
		-i)
			shift
			input=$1
			shift ;;
		-o)
			shift
			outpout=$1
			shift ;;
		-v)
			shift
			debug=1
			;;
		-k)
			shift
			freq=1
			;;
		*)
			break
			;;
	esac
done

if [[ -z ${input} ]] || [[ -z ${outpout} ]]; then 
	usage
fi


# ------------------
# Env. 
# ------------------
NLPCONFIG="/Users/sindongboy/Dropbox/Documents/workspace/nlp_indexterms/config"
NLPRES="/Users/sindongboy/Dropbox/Documents/workspace/nlp_indexterms/resource"
CONFIG="/Users/sindongboy/Dropbox/Documents/workspace/keyphrase-extractor/config"
RESOURCE="/Users/sindongboy/Dropbox/Documents/workspace/keyphrase-extractor/resource"


# ------------------
# Dependency
# ------------------
DEP=`find ../lib -type f -name "*" | awk '{printf("%s:", $0);}' | sed 's/:$//g'`
TARGET="../target/keyphrase-extractor-1.0.2-SNAPSHOT.jar"

CP="${NLPCONFIG}:${NLPRES}:${CONFIG}:${RESOURCE}:${DEP}:${TARGET}"


if [[ ${debug} == 0 ]]; then
	if [[ ${freq} == 0 ]]; then 
		java -Xms2G -Xmx8G -cp ${CP} com.skplanet.nlp.keyphrase.driver.GenerateRakeKeyword -i ${input} -o ${outpout}
	else
		java -Xms2G -Xmx8G -cp ${CP} com.skplanet.nlp.keyphrase.driver.GenerateRakeKeyword -i ${input} -o ${outpout} -k
	fi
else
	if [[ ${freq} == 0 ]]; then 
		java -Xms2G -Xmx8G -cp ${CP} com.skplanet.nlp.keyphrase.driver.GenerateRakeKeyword -i ${input} -o ${outpout} -v
	else
		java -Xms2G -Xmx8G -cp ${CP} com.skplanet.nlp.keyphrase.driver.GenerateRakeKeyword -i ${input} -o ${outpout} -v -k
	fi
fi

