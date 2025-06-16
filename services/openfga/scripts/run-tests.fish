#!/usr/local/bin/fish

set FGA_API_URL http://localhost:8080

set FGA_STORE_ID $(cat ./build/tmp/fga_store.txt)
set FGA_AUTHORIZATION_MODEL_ID $(cat ./build/tmp/fga_authorization_model_id.txt)

fga model test --tests ./data/tests.fga.yaml
