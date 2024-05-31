FROM node:18.17.0-alpine

WORKDIR /customer_app

COPY package.json pnpm-lock.yaml ./

RUN npm install -g pnpm

RUN pnpm install

COPY . .

EXPOSE 3001

CMD ["pnpm", "dev"]