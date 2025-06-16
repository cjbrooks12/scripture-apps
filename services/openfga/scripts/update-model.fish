#!/usr/local/bin/fish

set FGA_API_URL http://localhost:8080

set FGA_STORE_ID $(cat ./build/tmp/fga_store.txt)

set FGA_AUTHORIZATION_MODEL_ID $(fga model write --store-id=$FGA_STORE_ID --file=./data/model.fga | jq -r '.authorization_model_id')
echo $FGA_AUTHORIZATION_MODEL_ID > ./build/tmp/fga_authorization_model_id.txt
