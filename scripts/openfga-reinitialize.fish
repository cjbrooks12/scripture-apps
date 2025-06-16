#!/usr/local/bin/fish

# Manually pull images without SSL verification to get around stupid company proxies
cd ./services/openfga

./scripts/update-model.fish
./scripts/import-data.fish
