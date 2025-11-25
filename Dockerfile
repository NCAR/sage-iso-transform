
# Stage 1: Build the application

FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn package spring-boot:repackage

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

# Stage 3:  Copy config and transform scripts
COPY config/setup_wafs.sh .
COPY config/check_git.sh .
COPY config/run_transformer.sh .

# Create WAFs if necessary
RUN ./setup_wafs.sh

CMD ["supervisord", "-n", "-c", "/etc/supervisord.d/supervisord.conf"]
