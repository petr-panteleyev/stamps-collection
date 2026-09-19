#!/bin/sh

# Copyright © 2026 Petr Panteleyev
# SPDX-License-Identifier: BSD-2-Clause
if [ -z "$1" ]
then
  echo "Usage: install.sh <install dir>"
  exit
fi

LAUNCH_DIR=$(cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd)
INSTALL_DIR=$(realpath -m "$1")/stamps-collection

echo -n "Installing into $INSTALL_DIR... "
mkdir -p $INSTALL_DIR
rm -rf $INSTALL_DIR/*
cp -r $LAUNCH_DIR/../desktop/target/dist/Stamps\ Collection/* $INSTALL_DIR
echo "done"

echo -n "Creating desktop entry... "
echo "[Desktop Entry]
Type=Application
Version=1.5
Name=Stamps Collection
Name[ru_RU]=Коллекция марок
Comment=Application to store stamps collection catalogue
Comment[ru_RU]=Каталог коллекции марок
Icon=$INSTALL_DIR/lib/Stamps\sCollection.png
Exec=\"$INSTALL_DIR/bin/Stamps Collection\"
Categories=Office;Java;
" > $HOME/.local/share/applications/stamps-collection.desktop
echo "done"
