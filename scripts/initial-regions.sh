#!/bin/sh

BASE_URL="http://localhost:1705/stamps/api/v1/regions"

curl -X 'POST' \
  "$BASE_URL" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "СССР",
  "yearStart": 1923,
  "yearEnd": 1991
}'

curl -X 'POST' \
  "$BASE_URL" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Россия",
  "yearStart": 1992
}'
