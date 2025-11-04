FROM mongo:8.0.1

# Скопировать mongod.conf как есть
COPY mongod.conf /etc/mongod.conf

# Скопировать содержимое keyFile в контейнер правильным способом
# (создаётся новый файл с нужными правами и владельцем)
RUN mkdir -p /etc/mongo && \
    touch /etc/mongo/keyFile && \
    chown 999:999 /etc/mongo/keyFile && \
    chmod 400 /etc/mongo/keyFile

# Вставляем содержимое keyFile в созданный файл
COPY keyFile /tmp/keyFile
RUN cat /tmp/keyFile > /etc/mongo/keyFile && rm /tmp/keyFile

CMD ["mongod", "--config", "/etc/mongod.conf"]
