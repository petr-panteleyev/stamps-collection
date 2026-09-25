#!/bin/sh

# Copyright © 2026 Petr Panteleyev
# SPDX-License-Identifier: BSD-2-Clause
if [ -z "$1" ]
then
  echo "Usage: install.sh <install dir>"
  exit
fi

LAUNCH_DIR=$(cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd)
INSTALL_DIR=$(realpath -m "$1")/stamps-collection-server

echo -n "Installing into $INSTALL_DIR... "
mkdir -p $INSTALL_DIR
rm -rf $INSTALL_DIR/*
cp -r $LAUNCH_DIR/../backend/target/dist/Stamps\ Backend/* $INSTALL_DIR
echo "done"

echo -n "Creating desktop entry... "
echo "[Desktop Entry]
Type=Application
Version=1.5
Name=Stamps Collection Server
Name[ru_RU]=Коллекция марок (сервер)
Comment=Application to store stamps collection catalogue
Comment[ru_RU]=Сервер каталога коллекции марок
Exec=\"$INSTALL_DIR/bin/Stamps Backend\"
Categories=Office;Java;
" > $HOME/.local/share/applications/stamps-collection-server.desktop
echo "done"
