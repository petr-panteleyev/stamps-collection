#!/bin/sh

TAGS=("Космос" "Искусство" "Живопись" "Скульптура" "Спорт" "Разное" "Авиация" "Флот"
  "Флора и фауна" "Автомобили" "Маяки" "Ледоколы" "Маршалы" "Железная дорога")

BASE_URL="http://localhost:1710/stamps/api/v1/tags"

for tag in "${TAGS[@]}"; do
  curl -X 'POST' \
    "$BASE_URL" \
    -H 'accept: application/json' \
    -H 'Content-Type: application/json' \
    -d "{\"name\": \"$tag\"}"
  echo
done
