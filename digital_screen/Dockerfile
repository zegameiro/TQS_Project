FROM node:18.17.0-alpine

WORKDIR /digital_screen

RUN npm install -g pnpm

COPY package.json pnpm-lock.yaml ./

RUN pnpm install --frozen-lockfile

COPY . .

EXPOSE 3003

CMD ["pnpm", "dev"]
