#!/bin/sh

BASE_URL="http://localhost:1705/stamps/api/v1/albums"

curl -X 'POST' \
  "$BASE_URL" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "СССР Космос",
    "region": "СССР",
    "tags": [
      "Космос"
    ],
    "subalbums": [
      {
        "name": "1957-1960",
        "region": "СССР",
        "startYear": 1957,
        "endYear": 1960,
        "tags": [
          "Космос"
        ],
        "subalbums": []
      },
      {
        "name": "1961-1965",
        "region": "СССР",
        "startYear": 1961,
        "endYear": 1965,
        "tags": [
          "Космос"
        ],
        "subalbums": []
      },
      {
        "name": "1966-1970",
        "region": "СССР",
        "startYear": 1966,
        "endYear": 1970,
        "tags": [
          "Космос"
        ],
        "subalbums": []
      },
      {
        "name": "1971-1975",
        "region": "СССР",
        "startYear": 1971,
        "endYear": 1975,
        "tags": [
          "Космос"
        ],
        "subalbums": []
      },
      {
        "name": "1976-1980",
        "region": "СССР",
        "startYear": 1976,
        "endYear": 1980,
        "tags": [
          "Космос"
        ],
        "subalbums": []
      },
      {
        "name": "1981-1985",
        "region": "СССР",
        "startYear": 1981,
        "endYear": 1985,
        "tags": [
          "Космос"
        ],
        "subalbums": []
      },
      {
        "name": "1985-1991",
        "region": "СССР",
        "startYear": 1985,
        "endYear": 1991,
        "tags": [
          "Космос"
        ],
        "subalbums": []
      }
    ]
  }'

curl -X 'POST' \
  "$BASE_URL" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "СССР Живопись",
    "region": "СССР",
    "subalbums": [
      {
        "name": "1961-1970",
        "region": "СССР",
        "startYear": 1961,
        "endYear": 1970,
        "tags": [
          "Живопись"
        ],
        "subalbums": []
      },
      {
        "name": "1971-1980",
        "region": "СССР",
        "startYear": 1971,
        "endYear": 1980,
        "tags": [
          "Живопись"
        ],
        "subalbums": []
      },
      {
        "name": "1981-1991",
        "region": "СССР",
        "startYear": 1981,
        "endYear": 1991,
        "tags": [
          "Живопись"
        ],
        "subalbums": []
      }
    ]
  }'

curl -X 'POST' \
  "$BASE_URL" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "СССР Флот",
    "region": "СССР",
    "tags": [
      "Флот"
    ]
  }'

curl -X 'POST' \
  "$BASE_URL" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "СССР Разное",
    "region": "СССР",
    "excludedTags": [
      "Живопись", "Космос"
    ]
  }'

curl -X 'POST' \
  "$BASE_URL" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Россия Разное",
    "region": "Россия",
    "excludedTags": [
      "Живопись", "Космос"
    ]
  }'
