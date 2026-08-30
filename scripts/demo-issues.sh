#!/bin/sh

BASE_URL="http://localhost:1705/stamps/api/v1/issues"

curl -X 'POST' \
  "$BASE_URL" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d "@./demo/issue_2.json"
