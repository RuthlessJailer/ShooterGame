@echo off
pushd "%1"
echo Starting static webserver in %cd%...
python -m http.server 80
popd