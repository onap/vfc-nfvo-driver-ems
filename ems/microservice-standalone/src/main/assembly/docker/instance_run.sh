#!/bin/bash
cd /service/ems/
chmod +x run.sh
./run.sh

while [ ! -f logs/emsdriver.log ]; do
    sleep 1
done
tail -F logs/emsdirver.log
