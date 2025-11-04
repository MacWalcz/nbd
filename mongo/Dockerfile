FROM mongo:8.0.1

COPY mongod.conf /etc/mongod.conf

RUN mkdir -p /etc/mongo && \
    touch /etc/mongo/keyFile && \
    chown 999:999 /etc/mongo/keyFile && \
    chmod 400 /etc/mongo/keyFile

COPY keyFile /tmp/keyFile
RUN cat /tmp/keyFile > /etc/mongo/keyFile && rm /tmp/keyFile

CMD ["mongod", "--config", "/etc/mongod.conf"]
