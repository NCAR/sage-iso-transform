
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
