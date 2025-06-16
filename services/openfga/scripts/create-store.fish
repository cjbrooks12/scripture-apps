#!/usr/local/bin/fish

set FGA_API_URL http://localhost:8080

set FGA_STORE_ID $(fga store create --name="Biblebits Store" | jq -r .store.id)

mkdir ./build/
mkdir ./build/tmp/

echo $FGA_STORE_ID > ./build/tmp/fga_store.txt
