FROM node:18.17.0-alpine

WORKDIR /staff_app

RUN npm install -g pnpm

COPY package.json pnpm-lock.yaml ./

RUN pnpm install --frozen-lockfile

COPY . .

EXPOSE 3002

CMD ["pnpm", "dev"]
