FROM node:latest

WORKDIR /app

RUN usermod -m -d /home/nonroot -l nonroot node
COPY --chown=nonroot:node entrypoint.sh docker-entrypoint.sh

RUN chmod +x docker-entrypoint.sh && \
    chown -R nonroot:node /app

COPY --chown=nonroot:node ["index.js", "package.json", "package-lock.json", "./"]
COPY --chown=nonroot:node data ./data

EXPOSE 3000

RUN npm install

USER nonroot

CMD ["npm", "run", "dev"]
