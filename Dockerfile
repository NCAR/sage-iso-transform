
# Stage 1: Build the application

FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn package spring-boot:repackage


# Create WAF folders if necessary
RUN if [ ! -d "/usr/local/wafs" ]; then \
       echo "Creating WAF dir"; \
       mkdir -p /usr/local/wafs; \
    fi

ARG WAF_PUSH_TOKEN
RUN if [ ! -d "/usr/local/wafs/dset-web-accessible-folder-iso19115-3-dev" ]; then \
       cd /usr/local/wafs; \
       git clone "https://${WAF_PUSH_TOKEN}@github.com/NCAR/dset-web-accessible-folder-iso19115-3-dev.git"; \
    fi

RUN if [ ! -d "/usr/local/wafs/dset-web-accessible-folder-dev" ]; then \
       cd /usr/local/wafs; \
       git clone "https://${WAF_PUSH_TOKEN}@github.com/NCAR/dset-web-accessible-folder-dev.git"; \
    fi

# Stage 2: Set up cron and supervisord
RUN apt-get update && apt-get install -y --no-install-recommends \
    cron \
    supervisor \
    && rm -rf /var/lib/apt/lists/* \
    && mkdir -p /etc/supervisor/conf.d

COPY config/supervisord.conf /etc/supervisord.d/supervisord.conf
COPY config/crontab /etc/cron.d/root-cron

# Install crontab
RUN crontab -u root /etc/cron.d/root-cron

# Stage 3:  Copy transform scripts
COPY config/*.sh .

# Return to home directory for supervisor startup
RUN cd

CMD ["supervisord", "-n", "-c", "/etc/supervisord.d/supervisord.conf"]

