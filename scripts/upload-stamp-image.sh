#!/bin/bash

BASE_URL="http://localhost:1710/stamps/api/v1/images"

if [ $# -ne 3 ]; then
  echo "Usage: $0 <region> <numberZag> <fileName>"
  exit 1
fi

REGION="$1"
NUMBER_ZAG="$2"
FILE_NAME="$3"

if [ ! -f "$FILE_NAME" ]; then
  echo "File not found: $FILE_NAME"
  exit 1
fi

BASE64=$(base64 -w 0 "$FILE_NAME")

BODY="{\"region\": \"$REGION\", \"numberZag\": $NUMBER_ZAG, \"isBlock\": false, \"image\": \"$BASE64\"}"
echo $BODY > ./curl-args.txt

curl -X 'POST' \
  "$BASE_URL" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d "@./curl-args.txt"
