#/bin/sh

set -x

if [ -z "${WAF_PUSH_TOKEN}" ]; then
  WAF_PUSH_TOKEN=`cat /run/secrets/waf_push_token`
fi

# Create WAF folders if necessary
if [ ! -d "/usr/local/wafs" ]; then
     echo "Creating WAF dir"
     mkdir -p /usr/local/wafs
fi

# Clone dev-WAF folders if necessary
if [ ! -d "/usr/local/wafs/dset-web-accessible-folder-iso19115-3-dev" ]; then
     cd /usr/local/wafs
     git clone "https://${WAF_PUSH_TOKEN}@github.com/NCAR/dset-web-accessible-folder-iso19115-3-dev.git"
fi

if [ ! -d "/usr/local/wafs/dset-web-accessible-folder-dev" ]; then \
     cd /usr/local/wafs
     git clone "https://${WAF_PUSH_TOKEN}@github.com/NCAR/dset-web-accessible-folder-dev.git"
fi

# Return to working directory to start supervisor
cd /app

# This should run in the foreground to keep the container alive
supervisord -n -c /etc/supervisord.d/supervisord.conf