FROM node:18.17.0-alpine

WORKDIR /screens_app

RUN npm install -g pnpm

COPY . .

RUN pnpm install --frozen-lockfile

EXPOSE 3003

CMD ["pnpm", "dev"]

# For production environment

# FROM nginx:stable-alpine

# COPY --from=build /app/build /usr/share/nginx/html
# COPY --from=build /app/nginx/nginx.conf /etc/nginx/conf.d/default.conf

# EXPOSE 80

# CMD ["nginx", "-g", "daemon off;"]